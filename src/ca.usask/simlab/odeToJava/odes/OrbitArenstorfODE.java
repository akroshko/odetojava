/* ./odes/OrbitArenstorfODE.java
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
package ca.usask.simlab.odeToJava.odes;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.ode.RHS;

/**
 * ODE that describes a 3-body problem with the Earth, Moon, and a massless satellite.
 * <p>
 * Benedict Leimkuhler, Sebastian Reich. "Simulating Hamiltonian dynamics", Cambridge University Press, pg 161-163, 2004.
 */    
public class OrbitArenstorfODE extends RHS {

    @Override
    public int get_size() {
        return 4;
    }

    @Override
    public Float64Vector f(Float64 t, Float64Vector y) {
        double y0 = y.getValue(0);
        double y1 = y.getValue(1);
        double y2 = y.getValue(2);
        double y3 = y.getValue(3);
        double[] yp = new double[y.getDimension()];
        
        double d1 = Math.pow(Math.pow((y0 + mu), 2) + y1 * y1, 1.5);
        double d2 = Math.pow(Math.pow((y0 - muhat), 2) + y1 * y1, 1.5);
        
        yp[0] = y2;
        yp[1] = y3;
        yp[2] = y0 + 2 * y3 - muhat * (y0 + mu) / d1 - mu * (y0 - muhat) / d2;
        yp[3] = y1 - 2 * y2 - muhat * y1 / d1 - mu * y1 / d2;
        
        return Float64Vector.valueOf(yp);
    }
    
    private final double mu = 0.012277471; // mass of the moon
    private final double muhat = 1.0 - mu; // mass of the earth
}
