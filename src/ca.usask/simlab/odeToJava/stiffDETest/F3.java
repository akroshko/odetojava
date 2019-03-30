/* ./stiffDETest/F3.java
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
 * Stiff DE test set problem F3, a chemical kinetics problem.
 * <p>
 * Wayne Enright, JD Pryce. "Two FORTRAN packages for assessing initial value
 * methods", ACM transactions on mathematical software, vol 13, no 1, pg
 * 1-27, 1987.
 */   
public class F3 extends RHS {
    @Override
    public Float64Vector f(Float64 t, Float64Vector y) {
        return Float64Vector.valueOf(
            -10000000.0 * y.getValue(1) * y.getValue(0) + 10.0 * y.getValue(2),
            -10000000.0 * y.getValue(1) * y.getValue(0) - 10000000.0 * y.getValue(1) * y.getValue(4) + 10.0 * y.getValue(2) + 10.0 * y.getValue(3),
            10000000 * y.getValue(1) * y.getValue(0) - 1.001 * 10000 * y.getValue(2) + 0.001 * y.getValue(3),
            10000 * y.getValue(2) - 10.001 * y.getValue(3) + 10000000 * y.getValue(1) * y.getValue(4),
            10 * y.getValue(3) - 10000000 * y.getValue(1) * y.getValue(4)); 
    }
    
    @Override
    public int get_size() {
        return 5;
    }
}