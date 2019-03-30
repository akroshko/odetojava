/* ./odes/BrusselatorODE.java
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
 * ODE describing the Brusselator oscillatory chemical reaction problem.
 * <p>
 * John J. Tyson, "Some further studies of nonlinear oscillations in chemical systems", The journal of chemical physics, vol 58, no 9, pg 3919-3930, 1973.
 */  
public class BrusselatorODE extends RHS {
    private double alpha;
    private double beta;

    public BrusselatorODE(double alpha, double beta) {
        this.alpha = alpha;
        this.beta = beta;
    }

    @Override
    public Float64Vector f(Float64 t, Float64Vector y)
    {
        Float64Vector yp;
        double y0 = y.getValue(0);
        double x1 = y.getValue(1);
        
        yp = Float64Vector.valueOf(this.alpha + y0 * y0 * x1 - this.beta * y0,
                                   this.beta * y0 - y0 * y0 * x1);
        return yp;
    }
    
    @Override
    public int get_size() {
        return 2;
    }
}
