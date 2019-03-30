/* ./odes/BurgersMOLODE.java
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
import org.jscience.mathematics.vectors.Float64Matrix;
import ca.usask.simlab.odeToJava.ode.AdditiveRHS;

/**
 * An ODE derived from the spatial discretization by finite differences of the Burgers advection equation with diffusion.
 * <p>
 * Uri Ascher, Steven Ruuth, Raymond Spiteri. "Implicit-explicit Runge-Kutta methods for time-dependent partial differential equations", Applied Numerical Mathematics, vol 25, pg 151-167, 1997.
 */
public class BurgersMOLODE extends AdditiveRHS {
    private int n = 0;

    private double dx; // spatial stepsize
    private double nu; // the diffusion coefficient
    private double[][] jac;

    @Override
    public int get_size() {
        return n;
    }

    /*
     * The constructor for the ODE.
     */
    public BurgersMOLODE(int n, Float64 dx, Float64 nu) {
        this.dx = dx.doubleValue();
        this.n = n;
        this.nu = nu.doubleValue();
        jac = new double[n][n];
    }

    @Override
    public Float64Vector f1(Float64 t, Float64Vector y) {
        double[] yp = new double[y.getDimension()];

        // apply upwind finite differences
        // the front point as a Dirchlet boundary condition
        yp[0] = 0.0;
        // the middle points
        for (int i = 1; i < y.getDimension() - 1; i++)
        {
            yp[i] = y.getValue(i)*(y.getValue(i-1) - y.getValue(i)) / dx;
        }
        // the end point as a Dirchlet boundary condition
        yp[n-1] = 0.0;

        return Float64Vector.valueOf(yp);
    }

    @Override
    public Float64Vector f2(Float64 t, Float64Vector y) {
        double[] yp = new double[y.getDimension()];

        // the front point as a Dirchlet boundary condition
        yp[0] =  0.0;
        // the middle points
        for (int i = 1; i < y.getDimension() - 1; i++)
        {
            yp[i] = nu*(y.getValue(i-1) - 2.0*y.getValue(i) + y.getValue(i+1)) / (dx*dx);
        }
        // the end point as a Dirchlet boundary condition
        yp[n-1] = 0.0;

        return Float64Vector.valueOf(yp);
    }

    @Override
    public Float64Matrix jacobian(Float64 t, Float64Vector y) {

        double coeff = nu/(dx*dx);
        for (int i = 1; i < n-1; i++) {
            jac[i][i-1] = coeff;
            jac[i][i] = -2.0*coeff;
            jac[i][i+1] = coeff;
        }

        return Float64Matrix.valueOf(jac);
    }
}
