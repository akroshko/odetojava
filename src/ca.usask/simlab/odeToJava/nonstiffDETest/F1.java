/* ./nonstiffDETest/F1.java
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
 * F1, problems with discontinuities.
 * <p>
 * Wayne Enright, JD Pryce. "Two FORTRAN packages for assessing initial value
 * methods", ACM transactions on mathematical software, vol 13, no 1, pg
 * 1-27, 1987.
 */                   
public class F1 extends RHS {
    @Override
    public Float64Vector f(Float64 t, Float64Vector y) {
        double PI2 = Math.PI * Math.PI;
        double a2 = this.a*this.a;
        if (Math.floor(t.doubleValue()) % 2 == 0) {
            return Float64Vector.valueOf(
                y.getValue(1),
                2*this.a*y.getValue(1) - (PI2+a2)*y.getValue(0) + 1);
        } else {
            return Float64Vector.valueOf(
                y.getValue(1),
                2*this.a*y.getValue(1) - (PI2+a2)*y.getValue(0) - 1);
        }
    }
    
    @Override
    public int get_size() {
        return 2;
    }

    private double a = 0.1;
}
