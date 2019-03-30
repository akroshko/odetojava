/* ./scheme/StormerVerletTableau.java
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
 * A dummy tableau for the Stormer-Verlet method of order 2.
 * <p>
 * Ernst Hairer, Christian Lubich, and Gerhard Wanner. "Geometric Numerical Integration: Structure-preserving algorithms for ordinary differential", 2002.
 * Ernst Hairer, Christian Lubich, and Gerhard Wanner. "Geometric numerical integration illustrated by the Störmer–Verlet method ", Acta Numerica, vol 12, pg 399-450, 2003.
 * Benedict Leimkuhler, Sebastian Reich. "Simulating Hamiltonian dynamics", Cambridge University Press, pg 161-163, 2004.
 */
public class StormerVerletTableau extends Scheme {
    public StormerVerletTableau() {
        order = 2;
        order_interpolant = 2;
        interpolant = new DefaultInterpolant(); 
        name = "Stomer-Verlet";
    }

    @Override
    public boolean is_single() {
        return true;
    }

    @Override
    public boolean is_additive() {
        return false;
    };
    
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
