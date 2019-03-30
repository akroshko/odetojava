/* ./ode/AdditiveRHS.java
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
package ca.usask.simlab.odeToJava.ode;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Vector;

/**
 * Represents an ordinary differential equation with a two-additive RHS.
 * <p>
 * The full vector field for the RHS is defined as f = f1 + f2.
 * <p>
 * In order to define the RHS of an ODE, the methods f1 and f2 are overridden.  
 * The convention in ODEToJava for additive Runge-Kutta methods is that the f1
 * method is non-stiff and non-linear while the f2 method is stiff and linear.
 */ 
public abstract class AdditiveRHS extends RHS {
    @Override
    public Float64Vector f(Float64 t, Float64Vector y) {
        Float64Vector f1 = f1(t, y);
        Float64Vector f2 = f2(t, y);
        
        return f1.plus(f2);
    }
    
    /**
     * The first part of the vector field f1.
     * 
     * @param t The solution time to do the evaluation.
     * @param y The solution to use in the RHS evaluation.
     * @return  The nonlinear part of the RHS evaluation at the time with the
     *          given solution value. 
     */
    public abstract Float64Vector f1(Float64 t, Float64Vector y);
    
    /**
     * The second part of the vector field f2.
     * 
     * @param t The solution time to do the evaluation.
     * @param y The solution to use in the RHS evaluation.
     * @return  The nonlinear part of the RHS evaluation at the time with the
     *          given solution value.  
     */
    public abstract Float64Vector f2(Float64 t, Float64Vector y);
}
