/* ./solver/SolverModule.java
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

import ca.usask.simlab.odeToJava.property.PropertyHolder;
import ca.usask.simlab.odeToJava.property.PropertyUser;

/**
 * This abstract class defines a module for a solver.
 */
public abstract class SolverModule extends PropertyUser {
    /**
     * This method is called before any integration steps occur in order
     * to perform any initializations a module may require.
     * 
     * @param solver     The solver for this solver module. 
     * @param properties The properties for the solver.
     */
    public void begin_stepping(PropertySolver solver, PropertyHolder properties) {
    };
    
    /**
     * This method is called to perform each integration step.
     * 
     * @param properties The properties for the solver.
     */
    public abstract void step(PropertyHolder properties);
    
    /**
     * This method is called after the solver has finished the integration steps
     * in order to perform any cleanup required.
     */
    public void end_stepping() {
    }
}
