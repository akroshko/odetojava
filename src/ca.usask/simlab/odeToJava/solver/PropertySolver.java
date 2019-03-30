/* ./solver/PropertySolver.java
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

import java.util.Iterator;
import java.util.List;

import javolution.util.FastList;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.ode.RHS;
import ca.usask.simlab.odeToJava.property.DebugPropertySatisfier;
import ca.usask.simlab.odeToJava.property.PropertyHolder;
import ca.usask.simlab.odeToJava.property.PropertyOrderer;
import ca.usask.simlab.odeToJava.property.PropertySatisfier;
import ca.usask.simlab.odeToJava.property.PropertyUser;
import ca.usask.simlab.odeToJava.property.RequiredIfPresentPropertyFinder;

/**
 * This class contains all the methods and variables required to form a framework for solving the ODE this includes
 * methods for adding, ordering and removing modules from the solver the solve method contains the actual loop for
 * solving the equations
 */
public abstract class PropertySolver extends PropertyUser implements Solver {
    
    /**
     * The modules for this solver.
     */
    protected FastList<PropertyUser> solver_modules;
    
    FastList.Node<PropertyUser> head;
    
    FastList.Node<PropertyUser> tail;
    
    FastList.Node<PropertyUser> n;
    
    /**
     * The properties for this solver.
     */
    protected PropertyHolder properties = new PropertyHolder();
    
    private boolean running;
    
    /**
     * Whether the solver is in debug mode.
     */ 
    protected boolean debug;
    
    private RHS ode;
    
    private Float64 initial_time, final_time, current_time, initial_stepsize, current_stepsize, final_stepsize;
    
    // contains the time the solver ran for
    private Float64 runtime;
    
    /**
     * The time in nanoseconds the solver has been running.
     */ 
    protected long time = 0;
    
    private Float64Vector initial_values, final_values, current_values;
    
    // Solver usually stops from time up
    private String reason;
    
    /**
     * 
     */
    public PropertySolver() {
        initial_time = Float64.ZERO;
        final_time = Float64.ZERO;
        current_time = Float64.ZERO;
        initial_stepsize = Float64.ZERO;
        current_stepsize = Float64.ZERO;
        final_stepsize = Float64.ZERO;
        solver_modules = new FastList<PropertyUser>();
        debug = false;
    }
    
    /**
     * Is the PropertySolver being used in debug mode.
     *
     * @return Whether the PropertySolver is in debug mode.
     */
    public boolean is_debug() {
        return debug;
    }
    
    /**
     * Set the PropertySolver debug mode.
     *
     * @param d Indicate whether the PropertySolver is to be set to debug mode.
     */
    public void set_debug(boolean d) {
        debug = d;
    }
    
    /**
     * Add a SolverModule to the PropertySolver.
     *
     * @param module The module to add.
     */
    public void add_solver_module(SolverModule module) {
        if (running) {
            throw new IllegalStateException("Cannot add modules to a solver once it has been started.");
        }
        solver_modules.add(module);
    }
    
    /**
     * Get the SolverModule objects associated with this PropertySolver.
     *
     * @return The list of SolverModule objects.
     */
    public List get_solver_modules() {
        return solver_modules;
    }
    
    protected void begin_stepping(PropertyHolder constant_properties) {
        Iterator i = solver_modules.iterator();
        while (i.hasNext()) {
            SolverModule solver_module = (SolverModule) i.next();
            solver_module.begin_stepping(this, constant_properties);
        }
    }
    
    /**
     * Order the modules in the solver.
     **/
    protected void order_modules() {
        RequiredIfPresentPropertyFinder finder = new RequiredIfPresentPropertyFinder(this, solver_modules);
        finder.find_required_if_present_properties();
        
        PropertyOrderer orderer = new PropertyOrderer(this, solver_modules);
        solver_modules = orderer.get_ordered_modules();
        
        PropertySatisfier satisfier;
        if (debug) {
            satisfier = new DebugPropertySatisfier(this, solver_modules);
        } else {
            satisfier = new PropertySatisfier(this, solver_modules);
        }
        satisfier.satisfy_modules();
    }
    
    protected void end_stepping() {
        Iterator i = solver_modules.iterator();
        while (i.hasNext()) {
            ((SolverModule) i.next()).end_stepping();
        }
    }

    /**
     * Solve an IVP using the solver once it has been set up.
     *
     * @param ode            The ODE to solve.
     * @param initial_time   The initial time to solve from.
     * @param final_time     The final time to solve to.
     * @param initial_values The initial values to use.
     */ 
    public String solve(RHS ode, double initial_time, double final_time, double[] initial_values) {
        return solve(ode, Float64.valueOf(initial_time), Float64.valueOf(final_time), Float64Vector.valueOf(initial_values));
    }
    
    /**
     * Solve an IVP using the solver once it has been set up.
     *
     * @param ode            The ODE to solve.
     * @param initial_time   The initial time to solve from.
     * @param final_time     The final time to solve to.
     * @param initial_values The initial values to use.
     */
    public String solve(RHS ode, Float64 initial_time, Float64 final_time, Float64Vector initial_values) {
        if (running) {
            throw new IllegalStateException("Solver already running.");
        }
        
        this.ode = ode;
        this.initial_time = initial_time;
        this.initial_values = initial_values;
        this.final_time = final_time;

        final_values = null;
        final_stepsize = Float64.ZERO;
        
        order_modules();
        begin_stepping(properties);
        head = solver_modules.head();
        tail = solver_modules.tail();
        
        prepare_attributes();
        running = true;
        try {
            boolean done = false;
            
            while (!done) {
                done = step(properties);
            }
            
        } finally {
            finalize_attributes();
            end_stepping();
            running = false;
        }
        return reason;
    }

    /**
     * Solve an IVP using the solver once it has been set up.
     *
     * @param other          The other PropertySolver to start from.
     * @param final_time     The final time to solve to.
     */ 
    public String solve_from(PropertySolver other, Float64 final_time) {
        return solve(other.get_ODE(), other.get_final_time(), final_time, other.get_final_values());
    }
    
    /**
     * Set the reason for stopping the solver.
     *
     * @param reason The reason for stopping the solver.
     */
    protected void set_stop_reason(String reason) {
        reason = reason;
    }

    /**
     * Get the reason for stopping the solver.
     *
     * @return The reason for stopping the solver.
     */ 
    protected String get_stop_reason() {
        return reason;
    }
    
    private void finalize_attributes() {
        set_final_time(get_current_time());
        set_final_values(get_current_values());
        set_final_stepsize(get_current_stepsize());
    }
    
    private void prepare_attributes() {
        set_current_time(get_initial_time());
        set_current_values(get_initial_values());
        set_current_stepsize(get_initial_stepsize());
    }
    
    protected boolean step(PropertyHolder properties) {
        n = head;
        
        while ((n = n.getNext()) != tail) {
            SolverModule module = (SolverModule) n.getValue();
            module.step(properties);
        }
        return false;
        
    }
    
    /**
     * Get the RHS being solved.
     *
     * @return The RHS being solved.
     */
    public RHS get_ODE() {
        return ode;
    }
    
    /**
     * Get the initial time.
     *
     * @return The initial time.
     */ 
    public Float64 get_initial_time() {
        return initial_time;
    }
    

    /**
     * Get the initial values.
     *
     * @return The initial values.
     */ 
    public Float64Vector get_initial_values() {
        return initial_values;
    }
    
    /**
     * Get the initial stepsize.
     *
     * @return The initial stepsize.
     */  
    public Float64 get_initial_stepsize() {
        return initial_stepsize;
    }
    
    /**
     * Set the initial stepsize. 
     *
     * @param d The initial stepsize.
     */  
    public void set_initial_stepsize(Float64 d) {
        initial_stepsize = d;
    }
    
    /**
     * Get the final time.
     *
     * @return The final time.
     */  
    public Float64 get_final_time() {
        return final_time;
    }
    
    /**
     * Get the final values.
     *
     * @return The final values.
     */   
    public Float64Vector get_final_values() {
        return final_values;
    }
    
    /**
     * Get the final stepsize.
     *
     * @return The final stepsize.
     */    
    public Float64 get_final_stepsize() {
        return final_stepsize;
    }
    
    /**
     * Get the final runtime.
     *
     * @return The runtime.
     */     
    public Float64 get_runtime() {
        return runtime;
    }
    
    /**
     * Get the current problem time.
     *
     * @return The current problem time.
     */      
    protected Float64 get_current_time() {
        return current_time;
    }
    
    /**
     * Get the current values.
     *
     * @return The current values.
     */       
    protected Float64Vector get_current_values() {
        return current_values;
    }
    
    /**
     * Get the current stepsize.
     *
     * @return The current stepsize.
     */        
    protected Float64 get_current_stepsize() {
        return current_stepsize;
    }
    
    /**
     * Set the current problem time.
     *
     * @param d The current problem time.
     */         
    protected void set_current_time(Float64 d) {
        current_time = d;
    }
    
    /**
     * Set the current values.
     *
     * @param y The current values.
     */          
    protected void set_current_values(Float64Vector y) {
        current_values = y;
    }
    
    /**
     * Set the current stepsize.
     *
     * @param d The current stepsize.
     */           
    protected void set_current_stepsize(Float64 d) {
        current_stepsize = d;
    }
    
    /**
     * Set the current final time.
     *
     * @param tf The final time.
     */            
    private void set_final_time(Float64 tf) {
        final_time = tf;
    }
    
    /**
     * Set the final values.
     *
     * @param yf The final values.
     */             
    private void set_final_values(Float64Vector yf) {
        final_values = yf;
    }
    
    /**
     * Set the final stepsize.
     *
     * @param d The final stepsize.
     */             
    private void set_final_stepsize(Float64 d) {
        final_stepsize = d;
    }
}
