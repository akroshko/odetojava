/* ./modules/errorControl/SdErrEstimatorModule.java
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
package ca.usask.simlab.odeToJava.modules.errorControl;

import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.property.PropertyHolder;
import ca.usask.simlab.odeToJava.solver.PropertySolver;
import ca.usask.simlab.odeToJava.solver.SolverModule;
import ca.usask.simlab.odeToJava.util.Matrix;

/**
 * Module to estimate the error based on a step-doubling error-estimation method.
 * <p>
 * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 167.
 * <p>
 * Josef Stoer and Roland Bulirsch. "Introduction to Numerical analysis", pg 448-454.
 */
public class SdErrEstimatorModule extends SolverModule {
    private int q;
    /**
     * The constructor for an step-doubling error estimator.
     */
    public SdErrEstimatorModule() {
        require_property("schemeOrder");
        require_property("finalValuesError");
        require_property("finalValues");
        supply_property("errorEstimate");
    }

    @Override
    public void begin_stepping(PropertySolver solver, PropertyHolder constant_properties) {
        super.begin_stepping(solver, constant_properties);
        q = constant_properties.get_int_property("schemeOrder");
    }

    @Override
    public void step(PropertyHolder properties) {
        Float64Vector yfinal = properties.get_Float64Vector_property("finalValues");
        Float64Vector yerror = properties.get_Float64Vector_property("finalValuesError");
        Float64Vector error_estimate = yerror.minus(yfinal).times(1.0 / (Math.pow(2.0, q) - 1.0));
        properties.set_property("errorEstimate", error_estimate);
    }
}
