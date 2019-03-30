/* ./scheme/Scheme.java
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
package ca.usask.simlab.odeToJava.scheme;

import ca.usask.simlab.odeToJava.interpolant.Interpolant;

/**
 * An abstract class that defines a method scheme, including the order,
 * coefficients, and any other essential properties.
 *
 * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 135.
 * Christopher A. Kennedy, Mark H. Carpenter. "Additive Runge-Kutta schemes for convection-diffusion-reaction equations.". Applied numerical mathematics, vol 44, pg 139-181, 2003.
 */  
public abstract class Scheme {
    /**
     * The order of the method.
     */
    protected int order;  

    /**
     * The order of the method.
     */
    protected int order_interpolant;  

    /**
     * The order of the method.
     */
    protected Interpolant interpolant;   

    /**
     * The name of the method.
     */
    protected String name;

    /**
     * Finds if this tableau is just one Butcher Tableau and not an additive
     * Runge-Kutta tableau.
     * 
     * @return true if this has only one piece.
     */
    public abstract boolean is_single();
    
    /**
     * Finds if this tableau is an additive RK tableau.
     * 
     * @return true to indicate that this tableau has two additive parts.
     */
    public abstract boolean is_additive();
    
    /**
     * Finds if this tableau has the First-Same-As-Last (FSAL) property.
     * 
     * @return true if this has the FSAL property
     */
    public abstract boolean is_FSAL();
       
    /**
     * Finds the order of convergence for this method.
     * 
     * @return The order of the method.
     */
    public abstract int get_order();
    
    /**
     * Finds if this tableau has an embedded method.
     * 
     * @return true if this tableau has a embedded method.
     */
    public abstract boolean has_emb();
    
    /**
     * Finds the order of convergence for the main method.
     * 
     * @return The order of the main method.
     */
    public abstract int get_scheme_order();
    
    /**
     * Finds the order of convergence for the embedded method.
     * 
     * @return The order of convergence of the method or null if there
     *         is no embedded method.
     */
    public abstract int get_emb_order();
    
    /**
     * Finds the larger of the embedded and main order of convergences.
     * 
     * @return The larger of the orders.
     */
    public int get_high_order() {
        if (has_emb()) {
            return Math.max(get_scheme_order(), get_emb_order());
        } else {
            return get_scheme_order();
        }
    }
    
    /**
     * Get the interpolant for this tableau used for dense output.
     * 
     * @return The interpolant, or null if null if it does not have one.
     */
    public Interpolant get_interpolant() {
        return interpolant;
    } 
    
    /**
     * Return the order of the interpolant used for dense output.
     * 
     * @return The order of the interpolant or null if it does not have one.
     */
    public int get_order_interpolant() {
        return order_interpolant;
    } 

    /**
     * Get the name for this tableau.
     * 
     * @return The interpolant, or null if null if it does not have one.
     */
    public String get_name() {
        return name;
    }
}
