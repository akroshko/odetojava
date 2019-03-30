/* ./solver/EmbErrSolver.java
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

/**
 * A variable-stepsize solver using an embedded error estimate.
 * <p>
 * A method typically provides two solutions with a differing orders of
 * convergence.
 * <p>
 * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 165.
 * <p>
 * Josef Stoer and Roland Bulirsch. "Introduction to Numerical analysis", pg 448-454. * *
 */
public class EmbErrSolver extends VariableStepsizeSolver {
    
    /**
     * The standard constructor.
     */
    public EmbErrSolver() {
        
        super();
        require_property("stepAccepted");
        require_property("nextStepSize");
        require_property("finalValues");
        require_property("finalTime");
        
        require_if_present_property("stopSolver");
        require_if_present_property("stopReason");
        
        require_if_present_property("initialStepSize");
        
        supply_property("finalTime");
        supply_property("initialTime");
        supply_property("initialValues");
    }
}
