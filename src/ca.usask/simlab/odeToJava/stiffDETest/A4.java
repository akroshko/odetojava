/* ./stiffDETest/A4.java
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
package ca.usask.simlab.odeToJava.stiffDETest;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.ode.RHS;

/**
 * Stiff DE test set problem A4.
 * <p>
 * Wayne Enright, JD Pryce. "Two FORTRAN packages for assessing initial value
 * methods", ACM transactions on mathematical software, vol 13, no 1, pg
 * 1-27, 1987.
 * <p>
 * Wayne Enright, TE Hull, B Lindberg. "Comparing numerical methods for stiff systems of ODEs", BIT numerical mathematics, vol 15, pg 10-48, 1975.
 */     
public class A4 extends RHS {
    @Override
    public Float64Vector f(Float64 t, Float64Vector y) {
        return Float64Vector.valueOf(
            -y.getValue(0),
            -32.0 * y.getValue(1),
            -243.0 * y.getValue(2),
            -1024.0 * y.getValue(3),
            -3125.0 * y.getValue(4),
            -7776.0 * y.getValue(5),
            -16807.0 * y.getValue(6),
            -32768.0 * y.getValue(7),
            -59049.0 * y.getValue(8),
            -100000.0 * y.getValue(9)); 
    }
    
    @Override
    public int get_size() {
        return 10;
    }
}
