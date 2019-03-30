/* ./solver/VariableStepsizeSolver.java
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
package ca.usask.simlab.odeToJava.solver;

import org.jscience.mathematics.numbers.Float64;
import ca.usask.simlab.odeToJava.property.PropertyHolder;
import ca.usask.simlab.odeToJava.property.PropertyNotFoundException;

/**
 * This is an abstract class that implements variable step-size solvers.
 */
public abstract class VariableStepsizeSolver extends PropertySolver {
    /**
     * Whether the current step has been accepted.
     */
    protected boolean step_accepted;

    /**
     * The standard constructor.
     */
    public VariableStepsizeSolver() {
        step_accepted = false;
    }

    /**
     * The method that is called before the integration steps take place.
     *
     * @param constant_properties The constant properties for this solver.
     */
    @Override
    public void begin_stepping(PropertyHolder constant_properties) {
        super.begin_stepping(constant_properties);
        if (!get_initial_stepsize().isGreaterThan(Float64.ZERO)) {
            // check to see if the user has set one
            // nope, so we see if a module has, or just go with default
            set_initial_stepsize(find_initial_stepsize(constant_properties));

        }
        properties.set_property("stepAccepted", false);
    }

    /**
     * Obtain an initial stepsize from the constant properties or use a sane
     * default.
     */
    protected Float64 find_initial_stepsize(PropertyHolder constant_properties) {
        try {
            return constant_properties.getFloat64Property("initialStepSize");
        } catch (PropertyNotFoundException e) {
            set_current_stepsize(Float64.valueOf(1e-4));
            return Float64.valueOf(1e-4);
        }
    }

    /**
     * Perform an integration step and test to see if the solver has reached the
     * final time.
     *
     * @param properties The properties associated with this solver.
     */
    @Override
    protected boolean step(PropertyHolder properties) {
        boolean done = false;

        // Stretch 10% of the stepsize if it could step over the final time but
        // only if the last step was accepted
        if (!get_current_time().plus(Float64.valueOf(1.1).times(get_current_stepsize())).isLessThan(get_final_time()) && step_accepted) {
            set_current_stepsize(get_final_time().minus(get_current_time()));
            properties.set_property("finalTime", get_final_time());
        } else {
            properties.set_property("finalTime", get_current_time().plus(get_current_stepsize()));
        }
        properties.set_property("initialTime", get_current_time());
        properties.set_property("initialValues", get_current_values());

        // run all the modules that have been added
        super.step(properties);

        step_accepted = properties.get_boolean_property("stepAccepted");

        // advance the time if the step has been accepted
        if (step_accepted) {
            set_current_time(properties.getFloat64Property("finalTime"));
            set_current_values(properties.get_Float64Vector_property("finalValues"));

            if (!get_current_time().isLessThan(get_final_time())) {
                done = true;
            }
        } else {
            done = false; // don't finish if step not accepted
        }
        // get next optimal stepsize. This will be truncated in the
        // next iteration if t + h > tf.
        set_current_stepsize(properties.getFloat64Property("nextStepSize"));

        try {
            done = done || properties.get_boolean_property("stopSolver");
            set_stop_reason((String) properties.get_property("stopReason"));
        } catch (PropertyNotFoundException e) {
        } catch (ClassCastException e) {
        }
        return done;
    }

    /**
     * Determine if the current step is accepted.
     *
     * @return true if the current step is accepted, false otherwise.
     */
    public boolean is_step_accepted() {
        return step_accepted;
    }
}
