/* ./solver/SymmetricVariableStepsizeSolver.java
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
 * This class implements variable step-size solver for .
 */
public class SymmetricVariableStepsizeSolver extends VariableStepsizeSolver {
    /**
     * The standard constructor.
     */
    public SymmetricVariableStepsizeSolver() {
        super();
        step_accepted = true;
        supply_property("finalTime");
        supply_property("initialTime");
        supply_property("initialValues");
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
        properties.set_property("stepAccepted", true);
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

        properties.set_property("finalTime", get_current_time().plus(get_current_stepsize()));
        properties.set_property("initialTime", get_current_time());
        properties.set_property("initialValues", get_current_values());

        // run all the modules that have been added
        n = head;

        while ((n = n.getNext()) != tail) {
            SolverModule module = (SolverModule) n.getValue();
            module.step(properties);
        }

        step_accepted = properties.get_boolean_property("stepAccepted");

        Float64 previous_time = get_current_time();

        // advance the time
        set_current_time(properties.getFloat64Property("finalTime"));
        set_current_values(properties.get_Float64Vector_property("finalValues"));

        // we have stepped onto or after the final time
        if (previous_time.isLessThan(get_current_time()) && !get_current_time().isLessThan(get_final_time())) {
            done = true;
        }


       // get the next stepsize.
        set_current_stepsize(properties.getFloat64Property("nextStepSize"));

        try {
            done = done || properties.get_boolean_property("stopSolver");
            set_stop_reason((String) properties.get_property("stopReason"));
        } catch (PropertyNotFoundException e) {
        } catch (ClassCastException e) {
        }

        return done;
    }
}
