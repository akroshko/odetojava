/* ./solver/Solver.java
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
import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.ode.RHS;

/**
 * This interface defines a solver within ODEToJava.
 */
public interface Solver {
    /**
     * Get the ODE this solver solves.
     * 
     * @return The ODE being solved.
     */
    public abstract RHS get_ODE();
    
    /**
     * Retrieve the initial stepsize of this solver.
     * <p>
     * For constant-stepsize solvers, this is the stepsize used for
     * the entire solution.
     * 
     * @return The stepsize (may be 0 if initial stepsize selection is being used).
     */
    public abstract Float64 get_initial_stepsize();
    
    /**
     * Set the initial stepsize.
     * <p>
     * For constant-stepsize solvers, this is the stepsize used for
     * the entire solution.
     * 
     * @param dt
     */
    public abstract void set_initial_stepsize(Float64 dt);
    
    /**
     * Get the stepsize at the end of the solution.
     * <p>
     * Only available after the solver has finished all integration steps.
     * 
     * @return the final stepsize
     */
    public abstract Float64 get_final_stepsize();
    
    /**
     * Get the initial time for the IVP.
     *
     * @return The initial time for the IVP.
     */
    public abstract Float64 get_initial_time();
    
    /**
     * Get the final time for the IVP.
     *
     * @return The final time for the IVP.
     */
    public abstract Float64 get_final_time();
    
    /**
     * Get the final values from solving this IVP.
     * <p>
     * Only available after the solver has finished all integration steps.
     * 
     * @return The final values.
     */
    public abstract Float64Vector get_final_values();
    
    /**
     * Get the initial values for solving this IVP.
     *
     * @return The initial values.
     */
    public abstract Float64Vector get_initial_values();
    
    /**
     * Begin solving from where another solver finished.
     * 
     * @param other The other solver that was solving the same IVP.
     * @param final_time The final time to solve the IVP until.
     *
     * @return The reason that the solver stopped solving.
     */
    public abstract String solve_from(PropertySolver other, Float64 final_time);
    
    /**
     * Start solving the IVP until the final time.
     * 
     * @param ode           The ODE to solve. 
     * @param initial_time   The initial time for the IVP.
     * @param final_time     The final time to solve the IVP until.
     * @param initial_values The initial values for the IVP.
     *
     * @return The reason that the solver stopped solving.
     */
    public abstract String solve(RHS ode, Float64 initial_time, Float64 final_time, Float64Vector initial_values);
}
