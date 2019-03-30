/* ./modules/errorControl/EmbErrControllerModule.java
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
 * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 167.
 * <p>
 * Josef Stoer and Roland Bulirsch. "Introduction to Numerical analysis", pg 448-454.
 */
public class EmbErrControllerModule extends BaseErrControllerModule {
    /**
     * The value of the order of the method used in this error controller.
     */
    protected int q;

    /**
     * Constructor that sets up the error control with user-specified uniform tolerances.
     *
     * @param atol The absolute tolerance used for all variables.
     * @param rtol The relative tolerance used for all variables.
     * @param ode  The ODE that is being solved.
     */
    public EmbErrControllerModule(double atol, double rtol, RHS ode) {
        super(atol, rtol, ode);
        initialize_properties();
    }

    /**
     * Constructor that sets up the error control with user-specified uniform tolerances.
     *
     * @param atol The absolute tolerance used for all variables.
     * @param rtol The relative tolerance used for all variables.
     * @param ode  The ODE that is being solved.
     */
    public EmbErrControllerModule(Float64 atol, Float64 rtol, RHS ode) {
        super(atol, rtol, ode);
        initialize_properties();
    }

    /**
     * Constructor that sets up the error control with user-specified component-wise tolerances.
     *
     * @param atol A vector of absolute tolerances.
     * @param rtol A vector of relative tolerances.
     * @param ode  The ODE that is being solved.
     */
    public EmbErrControllerModule(double[] atol, double[] rtol, RHS ode) {
        super(atol, rtol, ode);
        initialize_properties();
    }

    /**
     * Constructor that sets up the error control with user-specified component-wise tolerances.
     *
     * @param atol A vector of absolute tolerances.
     * @param rtol A vector of relative tolerances.
     * @param ode  The ODE that is being solved.
     */
    public EmbErrControllerModule(Float64Vector atol, Float64Vector rtol, RHS ode) {
        super(atol, rtol, ode);
        initialize_properties();
    }

    private void initialize_properties() {
        require_property("initialValues");
        require_property("initialTime");
        require_property("finalTime");
        require_property("finalValues");
        require_property("errorEstimate");
        require_property("schemeOrder");
        require_property("embOrder");

        supply_property("stepAccepted");
        supply_property("nextStepSize");

        supply_property("absoluteTolerances");
        supply_property("relativeTolerances");
        supply_property("aMax");
        supply_property("aMin");
    }

    @Override
    public void begin_stepping(PropertySolver solver, PropertyHolder properties) {
        // get the minimum order
        q = Math.min(properties.get_int_property("schemeOrder"), properties.get_int_property("embOrder"));

        // set properties that may be needed elsewhere
        properties.set_property("absoluteTolerances", get_atol());
        properties.set_property("relativeTolerances", get_rtol());
        properties.set_property("aMax", get_amax_normal());
        properties.set_property("aMin", get_amin());
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
        factor = epsilon.pow(Float64.ONE.divide(q+1));
        factor = Matrix.min(Float64.ONE.divide(get_amin()), factor.divide(get_safety()));
        if (!epsilon.isGreaterThan(Float64.ONE)) {
            factor = Matrix.max(Float64.ONE.divide(get_amax()), factor);
            step_accepted = true;
        } else {
            factor = Matrix.max(Float64.ONE.divide(get_amax_rejected()), factor);
            step_accepted = false;
        }
        set_step_accepted(step_accepted);
        Float64 next_stepsize = dt.divide(factor);
        set_next_stepsize(next_stepsize);
        step.set_property("stepAccepted", step_accepted);
        step.set_property("nextStepSize", next_stepsize);
    }
}
