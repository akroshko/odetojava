/* ./interpolant/RKInterpolant.java
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
package ca.usask.simlab.odeToJava.interpolant;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Matrix;
import org.jscience.mathematics.vectors.Float64Vector;

/**
 * This is a standard interpolant for a Runge-Kutta method.
 * <p>
 * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 189.
 */
public class RKInterpolant implements Interpolant {
    Float64Matrix coefficients;
    
    /**
     * Constructor to initialize the coefficients.
     * <p>
     * The number of rows are equal to the number of stages and number the
     * columns equal to the order of the interpolating polynomial.
     *
     * @param coefficients The coefficients for a Runge-Kutta interpolant.
     */
    public RKInterpolant(double[][] coefficients) {
        this(Float64Matrix.valueOf(coefficients));
    }
    
    /**
     * Constructor to initialize the coefficients.
     * <p>
     * The number of rows are equal to the number of stages and number the
     * columns equal to the order of the interpolating polynomial.
     *
     * @param coefficients The coefficients for a Runge-Kutta interpolant.
     */ 
    public RKInterpolant(Float64Matrix coefficients) {
        this.coefficients = coefficients;
    }
    
    /**
     * Evaluates the interpolant.
     * 
     * @param y0          The solution at the beginning of the step.
     * @param y1          The solution at the end of the step.
     * @param theta       The fractional distance within the current step to find the dense output.
     * @param dt          The current stepsize.
     * @param stage_values The stage values for this step when applicable.
     *
     * @return            The dense output value at the fraction theta within the stepsize.
     */
    public Float64Vector evaluate_interpolant(Float64Vector y0, Float64Vector y1, Float64 theta, Float64 dt, Object stage_values) {
        Float64Vector btheta = btheta(theta);
        // use the interpolant weights based on the polynomial to find the
        // desired dense output point
        return Float64Matrix.valueOf((Float64Vector[]) stage_values).transpose().times(btheta.times(dt));
    }
    
    /**
     * This method finds the quadrature weights based on the specific fraction
     * within the step.
     * 
     * @param theta The fractional distance within the current step to find the dense output.
     *
     * @return      The quadrature weights for the particular fractional
     *              distance within the current step.
     */
    protected Float64Vector btheta(Float64 theta) {
        double[] thetas = new double[coefficients.getNumberOfColumns()];
        
        for (int i = 0; i < thetas.length; i++) {
            thetas[i] = theta.pow(i + 1).doubleValue();
        }
        return coefficients.times(Float64Vector.valueOf(thetas));
    }
}
