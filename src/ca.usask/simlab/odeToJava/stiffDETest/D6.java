/* ./stiffDETest/D6.java
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
 * Stiff DE test set problem D6, non-linear with real eigenvalues from insulator
 * physics.
 * <p>
 * Wayne Enright, JD Pryce. "Two FORTRAN packages for assessing initial value
 * methods", ACM transactions on mathematical software, vol 13, no 1, pg
 * 1-27, 1987.
 * <p>
 * Wayne Enright, TE Hull, B Lindberg. "Comparing numerical methods for stiff systems of ODEs", BIT numerical mathematics, vol 15, pg 10-48, 1975.
 */                   
public class D6 extends RHS {
    @Override
    public Float64Vector f(Float64 t, Float64Vector y) {
        double y1p = -y.getValue(0) + 1e8*y.getValue(2)*(1 - y.getValue(0));
        double y2p = -10*y.getValue(1) + 3e7*y.getValue(2)*(1 - y.getValue(1));
        return Float64Vector.valueOf(
            y1p,
            y2p,
            -y1p - y2p);
    }
    
    @Override
    public int get_size() {
        return 3;
    }
}
