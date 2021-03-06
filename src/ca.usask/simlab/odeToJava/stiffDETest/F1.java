/* ./stiffDETest/F1.java
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
 * Stiff DE test set problem F1, a chemical kinetics problem.
 * <p>
 * Wayne Enright, JD Pryce. "Two FORTRAN packages for assessing initial value
 * methods", ACM transactions on mathematical software, vol 13, no 1, pg
 * 1-27, 1987.
 */
public class F1 extends RHS {
    @Override
    public Float64Vector f(Float64 t, Float64Vector y) {
        double k = 6e-3*Math.pow(Math.E, (20.7 - 15000.0/y.getValue(0)));
        return Float64Vector.valueOf(
            1.3*(y.getValue(2) - y.getValue(0)) + 10400.0*k*y.getValue(1),
            1880.0*(y.getValue(3) - y.getValue(1)*(1.0 + k)),
            1752.0 - 269.0*y.getValue(2) + 267.0*y.getValue(0),
            0.1 + 320.0*y.getValue(1) - 321.0*y.getValue(3));
    }
    
    @Override
    public int get_size() {
        return 4;
    }
}
