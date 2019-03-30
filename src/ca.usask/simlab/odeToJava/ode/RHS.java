/* ./ode/RHS.java
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
import org.jscience.mathematics.vectors.Float64Matrix;

/**
 * Represents a typical ordinary differential equation with a single
 * non-additive RHS.
 * <p>
 * In order to define the RHS of an ODE, the method f is overridden.
 */
public abstract class RHS {
    /**
     * Get the number of components in this ODE.
     * 
     * @return The number of components in this ODE.
     */
    public abstract int get_size();
    
    /**
     * This method defines the RHS and returns the derivatives of each component
     * with respect to a solution and solution value.
     *
     * @param t The solution time to evaluate the RHS at.
     * @param y The solution values to evaluate the RHS with.
     *
     * @return The value of the RHS at the given time and solution values. 
     */
    public abstract Float64Vector f(Float64 t, Float64Vector y);

    /**
     * This method defines the Jacobian matrix at the given solution time
     * and solution values.
     * 
     * @param t The solution time to evaluate the Jacobian at.
     * @param y The solution values to evaluate the Jacobian with.
     *
     * @return The value of the Jacobian at the given time and solution values.  
     */
    public Float64Matrix jacobian(Float64 t, Float64Vector y) {
        // calculates the Jacobian with a finite difference method by default
        return Jacobian.finiteDifference(this, t, y);
    } 
}
