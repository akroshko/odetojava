/* ./stiffDETest/B2.java
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
 * Stiff DE test set problem B2, linear with non-real eigenvalues.
 * <p>
 * Eigenvalues of -0.1, -0.5, -1.0, -4.0, -10+-i*alpha, where alpha = 3.0.
 * <p>
 * Wayne Enright, JD Pryce. "Two FORTRAN packages for assessing initial value
 * methods", ACM transactions on mathematical software, vol 13, no 1, pg
 * 1-27, 1987.
 * <p>
 * Wayne Enright, TE Hull, B Lindberg. "Comparing numerical methods for stiff systems of ODEs", BIT numerical mathematics, vol 15, pg 10-48, 1975.
 */      
public class B2 extends RHS {
    @Override
    public Float64Vector f(Float64 t, Float64Vector y) {
        return Float64Vector.valueOf(
            -10 * y.getValue(0) + this.alpha * y.getValue(1),
            -this.alpha * y.getValue(0) - 10 * y.getValue(1),
            -4 * y.getValue(2),
            -y.getValue(3),
            -0.5 * y.getValue(4),
            -0.1 * y.getValue(5));
    }
    
    @Override
    public int get_size() {
        return 6;
    }

    protected double alpha = 3.0;
}
