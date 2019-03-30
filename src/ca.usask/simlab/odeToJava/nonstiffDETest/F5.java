/* ./nonstiffDETest/F5.java
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
 * F5, problems with discontinuities.
 * <p>
 * Wayne Enright, JD Pryce. "Two FORTRAN packages for assessing initial value
 * methods", ACM transactions on mathematical software, vol 13, no 1, pg
 * 1-27, 1987.
 */                   
public class F5 extends RHS {
    private double c;

    public F5() {
        this.c = 0;
        for (int i = 1; i < 20; i++) {
            c = c + Math.pow(i, 4/3);
        }
    }

    @Override
    public Float64Vector f(Float64 t, Float64Vector y) {
        double pprime = 0.0;
        
        for (int i = 1; i < 20; i++) {
            pprime = pprime + Math.copySign(Math.pow((t.doubleValue() - i), 1/3), t.doubleValue() - i);
        }
        
        return Float64Vector.valueOf(4 / (3*c) * pprime * y.getValue(0));
    }
    
    @Override
    public int get_size() {
        return 1;
    }
}
