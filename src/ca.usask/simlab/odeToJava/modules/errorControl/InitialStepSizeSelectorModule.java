/* ./modules/errorControl/InitialStepSizeSelectorModule.java
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

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.property.PropertyHolder;
import ca.usask.simlab.odeToJava.solver.PropertySolver;
import ca.usask.simlab.odeToJava.solver.SolverModule;
import ca.usask.simlab.odeToJava.util.Etc;
import ca.usask.simlab.odeToJava.util.Matrix;

/**
 * This class calculates an initial stepsize for variable stepsize methods for the particular problem
 *
 */
public class InitialStepSizeSelectorModule extends SolverModule {
    // maximum possible step h (user defined)
    private Float64 max_stepsize = Float64.valueOf(Double.POSITIVE_INFINITY);

    private Float64 min_stepsize = Float64.valueOf(0.0);

    private Float64 safety = Float64.valueOf(0.8); // safety factor

    /**
     * The default constructor.
     */
    public InitialStepSizeSelectorModule() {
        supply_property("initialStepSize");
        require_property("absoluteTolerances");
        require_property("relativeTolerances");
        require_property("schemeOrder");
    }

    @Override
    public void begin_stepping(PropertySolver solver, PropertyHolder properties) {
        Float64Vector atol = properties.get_Float64Vector_property("absoluteTolerances");
        Float64Vector rtol = properties.get_Float64Vector_property("relativeTolerances");
        int scheme_order = properties.get_int_property("schemeOrder");
        Float64 t0 = solver.get_initial_time();
        Float64 tf = solver.get_final_time();
        Float64Vector y0 = solver.get_initial_values();
        max_stepsize = Matrix.min(tf.minus(t0).abs(), max_stepsize);
        min_stepsize = Matrix.max(Etc.get_epsilon().times(16.0), min_stepsize);

        Float64Vector threshold = Matrix.divide(atol, rtol);

        Float64Vector rtol_pow = Matrix.pow(rtol, (1.0 / scheme_order)).times(safety);

        Float64Vector max = Matrix.max(y0, threshold);

        Float64Vector f0 = solver.get_ODE().f(solver.get_initial_time(), solver.get_initial_values());

        Float64Vector fOverMax = Matrix.divide(f0, max);

        Float64 rh = fOverMax.norm().divide(rtol_pow.norm().doubleValue());

        Float64 h = Matrix.min(max_stepsize, Float64.ONE.divide(rh));

        h = Matrix.max(min_stepsize, h);

        properties.set_property("intialStepSize", h);
    }

    @Override
    public void step(PropertyHolder step) {
    }
}
