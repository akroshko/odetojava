/* ./scheme/ForwardEulerTableau.java
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
import ca.usask.simlab.odeToJava.interpolant.DefaultInterpolant;

/**
 * A dummy tableau for the forward Euler method.
 * <p>
 * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 35.
 */ 
public class ForwardEulerTableau extends Scheme {
    public ForwardEulerTableau() {
        order = 1;
        order_interpolant = 2;
        interpolant = new DefaultInterpolant();
        name = "Forward Euler";
    }

    @Override
    public boolean is_single() {
        return true;
    }

    @Override
    public boolean is_additive() {
        return false;
    }
    
    @Override
    public boolean is_FSAL() {
        return false;
    }
       
    @Override
    public int get_order() {
        return order;
    }
    
    @Override
    public boolean has_emb() {
        return false;
    }
    
    @Override
    public int get_scheme_order() {
        return order;
    }
    
    @Override
    public int get_emb_order() {
        return order;
    }

    @Override
    public int get_high_order() {
        return get_scheme_order();
    }
    
    @Override
    public Interpolant get_interpolant() {
        return interpolant;
    }

    @Override
    public int get_order_interpolant() {
        return order_interpolant;
    }
} 
