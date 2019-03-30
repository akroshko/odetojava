/* ./modules/scheme/ERKModule.java
 * Copyright (C) 2002-2012 the odeToJava Team. All rights reserved.
 * This file is part of odeToJava.
 *
 * odeToJava is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * odeToJava is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with odeToJava.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.usask.simlab.odeToJava.modules.scheme;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Matrix;
import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.scheme.ERKButcherTableau;
import ca.usask.simlab.odeToJava.ode.RHS;
import ca.usask.simlab.odeToJava.property.PropertyHolder;
import ca.usask.simlab.odeToJava.solver.PropertySolver;
import ca.usask.simlab.odeToJava.solver.SolverModule;

/**
 * A module that calls an explicit Runge-Kutta method with an arbitrary explicit Butcher tableau.
 * <p>
 * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 134, pg 165.
 */
public class ERKModule extends SolverModule {
    // the ODE
    private RHS ode;
    // coefficients and properties of the method
    private ERKButcherTableau tableau;
    private Float64Matrix a;
    private Float64Vector b, b_embedded, c;
    private boolean fsal; 
    private int s;
    // the intermediate stage values
    private Float64Vector[] k; 
    
    /**
     * The default constructor for this module.
     * 
     * @param tableau The Butcher tableau defining the coefficients of the
     *                explicit Runge-Kutta method.
     */
    public ERKModule(ERKButcherTableau tableau) {
        // the required properties for the initial conditions
        require_property("initialTime");
        require_property("initialValues");
        require_property("finalTime");
        // the supplied properties from the integration
        supply_property("finalValues");
        supply_property("finalValuesEmb");
        supply_property("stageValues");
        // the supplied properties related to the method
        supply_property("scheme");
        supply_property("schemeOrder");
        supply_property("embOrder");
        
        // set up the Butcher tableau
        this.tableau = tableau;
        a = tableau.get_A();
        b = tableau.get_b();
        if (tableau.has_emb()) {
            b_embedded = tableau.get_bemb();
        }
        c = tableau.get_c();
        // the number of stages 
        s = b.getDimension();
        // whether tableau has first-same-as-last
        fsal = tableau.is_FSAL();
    }
    
    @Override
    public void begin_stepping(PropertySolver solver, PropertyHolder properties) {
        // set the properties related to the method
        properties.set_property("scheme", tableau);
        properties.set_property("schemeOrder", tableau.get_scheme_order());
        properties.set_property("embOrder", tableau.get_emb_order());
        
        ode = (RHS) solver.get_ODE();

        // initialize the stage values
        k = new Float64Vector[s];
        // set up the last stage for first-same-as-last if it exists
        if (fsal) {
            // initialize the stage values for first-same-as-last if it exists
            k[s - 1] = ode.f(solver.get_initial_time(), solver.get_initial_values());
        }
    }
    
    @Override
    public void step(PropertyHolder step) {
        Float64 t0 = step.getFloat64Property("initialTime");
        Float64Vector y0 = step.get_Float64Vector_property("initialValues");
        Float64 dt = step.getFloat64Property("finalTime").minus(t0);
        Boolean step_accepted = step.get_boolean_property("stepAccepted");
        Float64Matrix adt = a.times(dt);
        Float64Vector cdt = c.times(dt);
        Float64Vector ynew;
        // use first-same-as-last if the last step was accepted
        if (fsal && step_accepted) {
            k[0] = k[s - 1];
        } else {
            k[0] = ode.f(t0, y0);
        }
        
        for (int i = 1; i < s; i++) {
            ynew = y0;
            for (int j = 0; j < i; j++) {
                ynew = ynew.plus(k[j].times(adt.get(i, j)));
            }
            k[i] = ode.f(t0.plus(cdt.get(i)), ynew);
        }
        // calculate the new solution using the quadrature weights
        ynew = Float64Matrix.valueOf(k).transpose().times(b.times(dt)).plus(y0);
        // set the properties related to the integration
        step.set_property("finalValues", ynew);
        step.set_property("stageValues", k); 
        // calculate the new embedded method solution using the quadrature weights
        if (tableau.has_emb()) {
            Float64Vector yEmb = Float64Matrix.valueOf(k).transpose().times(b_embedded.times(dt)).plus(y0);
            step.set_property("finalValuesEmb", yEmb);
        }

    }
}
