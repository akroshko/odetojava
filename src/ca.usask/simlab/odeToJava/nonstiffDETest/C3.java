/* ./nonstiffDETest/C3.java
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
package ca.usask.simlab.odeToJava.nonstiffDETest;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.ode.RHS;

/**
 * Nonstiff DE test set problem C3, a parabolic partial differential equation of
 * size 10.
 * <p>
 * Wayne Enright, JD Pryce. "Two FORTRAN packages for assessing initial value
 * methods", ACM transactions on mathematical software, vol 13, no 1, pg
 * 1-27, 1987.
 * <p>
 * TE Hull, Wayne Enright, BM Fellen, AE Sedgwick. "Comparing numerical methods
 * for ordinary differential differential equations", SIAM journal of numerical
 * analysis, vol 9, no 4, pg 603-637, 1972.
 */          
public class C3 extends RHS {
    @Override
    public Float64Vector f(Float64 t, Float64Vector y) {
        return Float64Vector.valueOf(
                          - 2.0*y.getValue(0) + y.getValue(1),
            y.getValue(0) - 2.0*y.getValue(1) + y.getValue(2),
            y.getValue(1) - 2.0*y.getValue(2) + y.getValue(3),
            y.getValue(2) - 2.0*y.getValue(3) + y.getValue(4),
            y.getValue(3) - 2.0*y.getValue(4) + y.getValue(5),
            y.getValue(4) - 2.0*y.getValue(5) + y.getValue(6),
            y.getValue(5) - 2.0*y.getValue(6) + y.getValue(7),
            y.getValue(6) - 2.0*y.getValue(7) + y.getValue(8),
            y.getValue(7) - 2.0*y.getValue(8) + y.getValue(9),
            y.getValue(8) - 2.0*y.getValue(9));
    }
    
    @Override
    public int get_size() {
        return 10;
    }
}
