/* ./modules/scheme/IMEXESDIRKModule.java
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
import ca.usask.simlab.odeToJava.scheme.ARKButcherTableau;
import ca.usask.simlab.odeToJava.scheme.IMEXESDIRKButcherTableau;
import ca.usask.simlab.odeToJava.ode.AdditiveRHS;
import ca.usask.simlab.odeToJava.ode.RHS;
import ca.usask.simlab.odeToJava.property.PropertyHolder;
import ca.usask.simlab.odeToJava.solver.PropertySolver;
import ca.usask.simlab.odeToJava.solver.SolverModule;
import ca.usask.simlab.odeToJava.util.Matrix;

/**
 * A module that calls a linearly implicit Runge-Kutta method with
 * arbitrary explicit and implicit-ESDIRK (explicit diagonally
 * implicit Runge-Kutta method) Butcher tableaux.
 * <p>
 * Christopher A. Kennedy, Mark H. Carpenter. "Additive Runge-Kutta schemes for convection-diffusion-reaction equations.". Applied numerical mathematics, vol 44, pg 139-181, 2003.
 * <p>
 * Sebastiano Boscarino. "On an accurate third order implicit-explicit Runge–Kutta method for stiff problems", Applied Numerical Mathematics, vol 59, pg 1515-1528, 2009.
 */ 
public class IMEXESDIRKModule extends SolverModule {
    // related to the ODE
    protected RHS ode;
    protected Float64Matrix jacobian;
    protected Float64Matrix identity; 
    private boolean additive;
    // coefficients and properties of the method
    protected ARKButcherTableau tableau; 
    protected Float64Matrix a_explicit, a_implicit;
    protected Float64Vector b_explicit, b_implicit, c;
    protected Float64Vector b_explicit_emb, b_implicit_emb;  
    private boolean fsal;
    protected int s_explicit, s_implicit;
    // related to the stage values property
    protected Float64Vector[] k_explicit, k_implicit;
    protected Float64Vector[][] stage_values;

    /**
     * The default constructor for this SolverModule.
     * 
     * @param tableau The Butcher tableaux defining the coefficients of the
     *                explicit and implicit DIRK Runge-Kutta method. 
     */
    public IMEXESDIRKModule(ARKButcherTableau tableau) {
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
        stage_values = new Float64Vector[2][];
        a_explicit = tableau.get_tableau_one().get_A();
        b_explicit = tableau.get_tableau_one().get_b();
        s_explicit = b_explicit.getDimension();
        a_implicit = tableau.get_tableau_two().get_A();
        b_implicit = tableau.get_tableau_two().get_b();
        if (tableau.get_tableau_one().has_emb() || tableau.get_tableau_two().has_emb()) {
            b_explicit_emb = tableau.get_tableau_one().get_bemb();
            b_implicit_emb = tableau.get_tableau_two().get_bemb();
        }
        // assumes the c's are equal
        c = tableau.get_tableau_two().get_c();
        // the number of stages
        s_explicit = b_explicit.getDimension();
        s_implicit = b_implicit.getDimension();
        // whether tableau has first-same-as-last
        fsal = tableau.get_tableau_one().is_FSAL() && tableau.get_tableau_two().is_FSAL();
    }

    @Override
    public void begin_stepping(PropertySolver solver, PropertyHolder properties) {
        // set the properties related to the method
        properties.set_property("scheme", tableau);
        properties.set_property("schemeOrder", tableau.get_scheme_order());
        properties.set_property("embOrder", tableau.get_emb_order());

        ode = (RHS) solver.get_ODE();
        additive = ode instanceof AdditiveRHS;

        identity = Matrix.eye(solver.get_ODE().get_size());
        // initialize the stage values
        k_explicit = new Float64Vector[s_explicit];
        k_implicit = new Float64Vector[s_implicit]; 
    }

    @Override
    public void step(PropertyHolder step) {
        Float64 t0 = step.getFloat64Property("initialTime");
        Float64Vector y0 = step.get_Float64Vector_property("initialValues");
        Float64 dt = step.getFloat64Property("finalTime").minus(t0);

        Float64Matrix adt_explicit = a_explicit.times(dt);
        Float64Matrix adt_implicit = a_implicit.times(dt);
        Float64Vector cdt = c.times(dt);

        Float64Vector ynew = Float64Vector.valueOf(new double[y0.getDimension()]);
        
        // the first explicit stages, the Jacobian needs to be evaluated here
        jacobian = ode.jacobian(t0, y0);
        k_explicit[0] = evaluate_fnonlinear(t0, y0);
        k_implicit[0] = evaluate_flinear(t0, y0);
        // find the newton direction, SDIRK has same direction for all stages
        Float64Matrix newton = identity.minus(jacobian.times(adt_implicit.get(1, 1)));
        for (int i = 1; i < s_implicit; i++) {
            // evaluate the explicit method
            ynew = Matrix.fill(Float64.ZERO, ynew.getDimension());
            for (int j = 0; j < i; j++) {
                ynew = k_implicit[j].times(adt_implicit.get(i, j)).plus(k_explicit[j].times(adt_explicit.get(i, j))).plus(ynew);
            }
            Float64Vector fn = evaluate_flinear(t0.plus(cdt.get(i)), y0.plus(ynew));
            k_implicit[i] = Float64Vector.valueOf(newton.solve(fn));
            ynew = ynew.plus(k_implicit[i].times(adt_implicit.get(1, 1)))
;
            k_explicit[i] = evaluate_fnonlinear(t0.plus(cdt.get(i)), y0.plus(ynew));
        }
        
        ynew = Float64Matrix.valueOf(k_implicit).transpose().times(b_implicit).times(dt).plus(Float64Matrix.valueOf(k_explicit).transpose().times(b_explicit).times(dt)).plus(y0);
        step.set_property("finalValues", ynew);
        if (tableau.has_emb()) {
            Float64Vector ynewEmb = Float64Matrix.valueOf(k_implicit).transpose().times(b_implicit_emb).times(dt).plus(Float64Matrix.valueOf(k_explicit).transpose().times(b_explicit_emb).times(dt)).plus(y0);
            step.set_property("finalValuesEmb", ynewEmb);
        }
        
        stage_values[0] = k_explicit;
        stage_values[1] = k_implicit;
        step.set_property("stageValues", stage_values);
    }

    /**
     * Evaluate the linear part of the ODE.
     * 
     * If the ODE does not already have a splitting, the Jacobian matrix is 
     * used to perform an autosplitting with Jy being the linear portion.
     *
     * @param t The time to do the evaluation.
     * @param y The solution to use in the ODE evaluation.
     *
     * @return  The linear part of the ODE evaluation at the time with the given
     *          solution value.
     */
    protected Float64Vector evaluate_flinear(Float64 t, Float64Vector y) {
        if (additive) {
            return ((AdditiveRHS) ode).f2(t, y);
        } else {
            return jacobian.times(y);
        }
    }
    
    /**
     * Evaluate the nonlinear part of the ODE.
     *
     * If the ODE does not already have a splitting, the Jacobian matrix is 
     * used to perform an autosplitting with Jy being the linear portion.
     *
     * @param t The time to do the evaluation.
     * @param y The solution to use in the ODE evaluation.
     *
     * @return  The nonlinear part of the ODE evaluation at the time with the
     *          given solution value.
     */
    protected Float64Vector evaluate_fnonlinear(Float64 t, Float64Vector y) {
        if (additive) {
            return ((AdditiveRHS) ode).f1(t, y);
        } else {
            Float64Vector linear = evaluate_flinear(t, y);
            Float64Vector fn = ode.f(t, y);
            Float64Vector nonlinear = fn.minus(linear);
            return nonlinear;
        }
    }
}
