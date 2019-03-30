/* ./interpolant/DefaultInterpolant.java
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

import ca.usask.simlab.odeToJava.interpolant.Interpolant;
import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Vector;

/**
 * Interpolates a solution with a straight line between the beginning and the
 * end of the step.
 * <p>
 * This is a second-order interpolant that can be applied to any IVP method.
 */
public class DefaultInterpolant implements Interpolant {
    /**
     * Evaluates the interpolant.
     *
     * @param y0           The solution at the beginning of the step.
     * @param y1           The solution at the end of the step.
     * @param theta        The fractional distance within the current step to find the dense output.
     * @param dt           The current stepsize.
     * @param stage_values The stage values for this step when applicable.
     *
     * @return            The dense output value at the fraction theta within the stepsize.
     */
    public Float64Vector evaluate_interpolant(Float64Vector y0, Float64Vector y1, Float64 theta, Float64 dt, Object stage_values) {
        return (y1.minus(y0)).times(theta);
    }
}
