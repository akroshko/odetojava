/* ./modules/errorControl/EmbErrImplicitPredictiveControllerModule.java
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
import ca.usask.simlab.odeToJava.ode.RHS;
import ca.usask.simlab.odeToJava.property.PropertyHolder;
import ca.usask.simlab.odeToJava.solver.PropertySolver;
import ca.usask.simlab.odeToJava.util.Check;
import ca.usask.simlab.odeToJava.util.Matrix;

/**
 * This class implements a stepsize controller that finds an optimal stepsize
 * based on an embedded error estimate.
 * <p>
 * Ernst Hairer and Gerhard Wanner. "Solving Order Differential Equations II, 2nd Edition", pg 124.
 * <p>
 * Kjell Gustafsson. "Control-theoretic techniques for stepsize selection in implicit Runge-Kutta methods", ACM Transactions on Mathematical Software, vol  20, no 4, pg 496-517, December 1994.
 */
public class EmbErrImplicitPredictiveControllerModule extends EmbErrControllerModule {
    private boolean last_accept = false;
    private Float64 last_accept_dt;
    private Float64 last_accept_epsilon;

    /**
     * Constructor that sets up the error control with user-specified uniform tolerances.
     *
     * @param atol The absolute tolerance used for all variables.
     * @param rtol The relative tolerance used for all variables.
     * @param ode  The ODE that is being solved.
     */
    public EmbErrImplicitPredictiveControllerModule(double atol, double rtol, RHS ode) {
        super(atol, rtol, ode);
    }

    /**
     * Constructor that sets up the error control with user-specified uniform tolerances.
     *
     * @param atol The absolute tolerance used for all variables.
     * @param rtol The relative tolerance used for all variables.
     * @param ode  The ODE that is being solved.
     */
    public EmbErrImplicitPredictiveControllerModule(Float64 atol, Float64 rtol, RHS ode) {
        super(atol, rtol, ode);
    }

    /**
     * Constructor that sets up the error control with user-specified component-wise tolerances.
     *
     * @param atol A vector of absolute tolerances.
     * @param rtol A vector of relative tolerances.
     * @param ode  The ODE that is being solved.
     */
    public EmbErrImplicitPredictiveControllerModule(double[] atol, double[] rtol, RHS ode) {
        super(atol, rtol, ode);
    }

    /**
     * Constructor that sets up the error control with user-specified component-wise tolerances.
     *
     * @param atol A vector of absolute tolerances.
     * @param rtol A vector of relative tolerances.
     * @param ode  The ODE that is being solved.
     */
    public EmbErrImplicitPredictiveControllerModule(Float64Vector atol, Float64Vector rtol, RHS ode) {
        super(atol, rtol, ode);
    }

    @Override
    public void step(PropertyHolder step) {
        Float64Vector initial_values = step.get_Float64Vector_property("initialValues");
        Float64 dt = step.getFloat64Property("finalTime").minus(step.getFloat64Property("initialTime"));
        Float64Vector final_values = step.get_Float64Vector_property("finalValues");
        Float64Vector error_estimate = step.get_Float64Vector_property("errorEstimate");
        boolean step_accepted;
        Float64 factor;
        // make sure our floating point numbers are valid and reject if not
        if (!Check.valid(final_values)) {
            Float64 hnew = get_amin().times(dt);
            set_next_stepsize(hnew);
            set_step_accepted(false);
            step.set_property("nextStepSize", hnew);
            step.set_property("stepAccepted", false);
            return;
        }

        Float64Vector tolerances = get_tolerances(initial_values, final_values);
        Float64Vector ratios = Matrix.divide(error_estimate, tolerances);
        Float64 epsilon = Matrix.rms(ratios);
        // This calculation is correct only when the embedded method is of one order lower than the main method
        if (last_accept) {
            factor = (last_accept_dt.divide(dt)).times((epsilon.times(epsilon).divide(last_accept_epsilon).pow(Float64.ONE.divide(q+1))));
        } else {
            factor = epsilon.pow(Float64.ONE.divide(q+1));
        }
        factor = Matrix.min(Float64.ONE.divide(get_amin()), factor.divide(get_safety()));

        if (!epsilon.isGreaterThan(Float64.ONE)) {
            factor = Matrix.max(Float64.ONE.divide(get_amax()), factor);
            step_accepted = true;
            last_accept_epsilon = epsilon;
            last_accept_dt = dt.divide(factor);
        } else {
            factor = Matrix.max(Float64.ONE.divide(get_amax_rejected()), factor);
            step_accepted = false;
        }
        set_step_accepted(step_accepted);
        last_accept = step_accepted;
        Float64 next_stepsize = dt.divide(factor);
        set_next_stepsize(next_stepsize);
        step.set_property("stepAccepted", step_accepted);
        step.set_property("nextStepSize", next_stepsize);
    }
}
