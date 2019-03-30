/* ./solver/ConstantStepSolver.java
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

/**
 * A constant stepsize solver.
 */
public class ConstantStepSolver extends PropertySolver {
    /**
     * The standard constructor.
     */
    public ConstantStepSolver() {
        supply_property("initialTime");
        supply_property("initialValues");
        supply_property("finalTime");
        supply_property("stepAccepted");
        
        require_property("finalValues");
        require_property("finalTime");
        
        require_if_present_property("stopSolver");
        require_if_present_property("stopReason");
    }
    
    @Override
    public void begin_stepping(PropertyHolder constant_properties) {
        super.begin_stepping(constant_properties);
        if (!get_initial_stepsize().isGreaterThan(Float64.ZERO)) {
            set_initial_stepsize(Float64.valueOf(0.01));
        }
        properties.set_property("stepAccepted", false);
    }
    
    @Override
    public boolean step(PropertyHolder step) {
        boolean done = false;
        if (get_current_stepsize().isGreaterThan(get_final_time().minus(get_current_time()))) {
            set_current_stepsize(get_final_time().minus(get_current_time()));
        }
        
        step.set_property("initialTime", get_current_time());
        step.set_property("initialValues", get_current_values());
        step.set_property("finalTime", get_current_time().plus(get_current_stepsize()));
        
        super.step(step);
        
        set_current_time(step.getFloat64Property("finalTime"));
        set_current_values(step.get_Float64Vector_property("finalValues"));
        step.set_property("stepAccepted", true);
        if (!get_current_time().isLessThan(get_final_time())) {
            done = true;
        }
        
        return done;
    }
}
