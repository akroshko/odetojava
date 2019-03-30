/* ./ode/Jacobian.java
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

import ca.usask.simlab.odeToJava.util.Etc;
import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Matrix;
import org.jscience.mathematics.vectors.Float64Vector;

/**
 * This class implements a Jacobian based on an ODE.
 * @see RHS#jacobian
 */
public class Jacobian {
    // some sane defaults
    private static final Float64 DELTA_Y_DEFAULT = Etc.get_epsilon().sqrt();
    private static final Float64 DELTA_MIN_DEFAULT =  Etc.get_epsilon().sqrt();
    
    public static Float64Matrix finiteDifference(RHS ode, Float64 t, Float64Vector y) {
        return Jacobian.finiteDifference(ode, t, y, DELTA_Y_DEFAULT, DELTA_MIN_DEFAULT);
    }
    
    /**
     * Calculates the Jacobian of an ODE using a finite difference method.
     * <p>
     * This is the default implementation of the Jacobian of an ODE.
     * 
     * @param ode      The ODE for which to calculate the Jacobian.
     * @param t        The solution time to evaluate the Jacobian at.
     * @param y        The solution value to evaluate the Jacobian at.
     * @param deltay   The factor to apply to a solution component for applying the finite differences.
     * @param deltamin The smallest acceptable magnitude to change the solution by.
     *
     * @return         The Jacobian matrix.
     */
    public static Float64Matrix finiteDifference(RHS ode, Float64 t, Float64Vector y, Float64 deltay, Float64 deltamin) {
        // column vectors
        Float64Vector[] jacobian = new Float64Vector[ode.get_size()];

        // find function at current x
        Float64Vector f1 = ode.f(t, y);
        double[] delta_array = new double[ode.get_size()];

        for (int i = 0; i < y.getDimension(); i++) {
            // taken from finite difference Jacobians
            // in RADAU5 and RODAS codes
            // http://www.unige.ch/~hairer/software.html
            Float64 delta;
            if (y.get(i).abs().isLessThan(deltay)) {
                delta = deltay;
            } else {
                delta = y.get(i).abs();
            }
            delta = delta.times(Etc.get_epsilon()).sqrt();
            if (delta.isLessThan(deltamin)) {
                delta = deltamin;
            }
            delta_array[i] = delta.doubleValue();
            Float64Vector offsetX = y.plus(Float64Vector.valueOf(delta_array));
            
            Float64Vector f2 = ode.f(t, offsetX);
            delta_array[i] = 0.0;
            jacobian[i] = f2.minus(f1).times(Float64.ONE.divide(delta));
        }
        // required because Jacobian is calculated column-wise
        // but is stored row-wise and this works better with Jscience.
        return Float64Matrix.valueOf(jacobian).transpose();
    }
}
