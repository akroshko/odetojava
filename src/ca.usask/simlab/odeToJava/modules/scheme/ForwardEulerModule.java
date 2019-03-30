/* ./modules/scheme/ForwardEulerModule.java
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
import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.scheme.Scheme;
import ca.usask.simlab.odeToJava.scheme.ForwardEulerTableau;
import ca.usask.simlab.odeToJava.ode.RHS;
import ca.usask.simlab.odeToJava.property.PropertyHolder;
import ca.usask.simlab.odeToJava.solver.PropertySolver;
import ca.usask.simlab.odeToJava.solver.SolverModule;

/**
 * Module to call a Runge Kutta method
 * <p>
 * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 35.
 */
public class ForwardEulerModule extends SolverModule {
    private RHS ode;
    private Scheme tableau;
    
    /**
     * The default constructor for this module.
     */
    public ForwardEulerModule() {
        // the required properties for the initial conditions
        require_property("initialTime");
        require_property("initialValues");
        require_property("finalTime");
        // the supplied properties from the integration
        supply_property("finalValues");
        supply_property("stageValues");
        // the supplied properties related to the method
        supply_property("scheme");
        supply_property("schemeOrder");
        // the dummy tableau for forward Euler
        tableau = new ForwardEulerTableau();
    }
    
    @Override
    public void begin_stepping(PropertySolver solver, PropertyHolder properties) {
        // the supplied properties related to the method
        properties.set_property("scheme", tableau);
        properties.set_property("schemeOrder", tableau.get_scheme_order());

        ode = (RHS) solver.get_ODE();
    }
    
    @Override
    public void step(PropertyHolder step) {
        Float64 t0 = step.getFloat64Property("initialTime");
        Float64Vector y0 = step.get_Float64Vector_property("initialValues");
        Float64 h = step.getFloat64Property("finalTime").minus(t0);
        
        Float64Vector ynew;
        ynew = ode.f(t0, y0).times(h).plus(y0);
        
        step.set_property("finalValues", ynew);
        step.set_property("stageValues", Float64.ZERO);
    }
}
