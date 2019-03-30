/* ./controller/IVP.java
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
package ca.usask.simlab.odeToJava.controller;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.ode.RHS;

/**
 * Stores an IVP, which is an ODE together with initial conditions.
 */
public class IVP {
    /**
     * The ODE for this IVP.
     */
    protected RHS ode;

    /**
     * The initial time for this IVP.
     */
    protected Float64 initial_time;

    /**
     * The initial values for this IVP.
     */
    protected Float64Vector initial_values;


    /**
     * Construct an IVP.
     *
     * @param ode           The ODE for this this IVP.
     * @param initial_time   The initial time for this IVP.
     * @param initial_values The initial values for this IVP.
     */
    public IVP(RHS ode, double initial_time, double[] initial_values) {
        this(ode, Float64.valueOf(initial_time), Float64Vector.valueOf(initial_values));
    }

    /**
     * Construct an IVP.
     *
     * @param ode           The ODE for this this IVP.that
     * @param initial_time   The initial time for this IVP.
     * @param initial_values The initial values for this IVP.
     */
    public IVP(RHS ode, Float64 initial_time, Float64Vector initial_values) {
        this.ode = ode;
        this.initial_time = initial_time;
        this.initial_values = initial_values;
    }

    /**
     * Get the initial time for this IVP.
     *
     * @return The initial time of this IVP.
     */
    public Float64 get_initial_time() {
        return initial_time;
    }

    /**
     * Get the initial values for this IVP.
     *
     * @return The initial values of this IVP.
     */
    public Float64Vector get_initial_values() {
        return initial_values;
    }

    /**
     * Get the ODE for this IVP.
     *
     * @return The ODE for this IVP.
     */
    public RHS get_ODE() {
        return ode;
    }

    /**
     * Get the number of components of the ODE.
     *
     * @return The number of components of the ODE.
     */
    public int get_size() {
        return initial_values.getDimension();
    }

}
