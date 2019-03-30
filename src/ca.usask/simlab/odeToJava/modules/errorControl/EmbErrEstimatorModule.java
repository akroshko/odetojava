/* ./modules/errorControl/EmbErrEstimatorModule.java
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
import ca.usask.simlab.odeToJava.solver.SolverModule;

/**
 * Module to estimate the error based on a embedded error-estimation methods.
 * <p>
 * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 165.
 * <p>
 * Josef Stoer and Roland Bulirsch. "Introduction to Numerical analysis", Springer-Verlag, pg 448-454.
 */
public class EmbErrEstimatorModule extends SolverModule {
    /**
     * The constructor for an embedded error estimator.
     */
    public EmbErrEstimatorModule() {
        require_property("finalValues");
        require_property("finalValuesEmb");
        supply_property("errorEstimate");
    }

    @Override
    public void step(PropertyHolder step) {
        Float64Vector final_values = step.get_Float64Vector_property("finalValues");
        Float64Vector final_values_emb = step.get_Float64Vector_property("finalValuesEmb");
        step.set_property("errorEstimate", final_values.minus(final_values_emb));
    }
}
