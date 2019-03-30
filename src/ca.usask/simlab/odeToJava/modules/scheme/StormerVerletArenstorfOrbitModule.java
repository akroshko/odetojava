/* ./modules/scheme/StormerVerletArenstorfOrbitModule.java
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
package ca.usask.simlab.odeToJava.modules.scheme;

import ca.usask.simlab.odeToJava.ode.RHS;
import ca.usask.simlab.odeToJava.solver.SolverModule;
import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.ode.RHS;
import ca.usask.simlab.odeToJava.property.PropertyHolder;
import ca.usask.simlab.odeToJava.solver.PropertySolver;
import ca.usask.simlab.odeToJava.solver.SolverModule;
import ca.usask.simlab.odeToJava.util.Matrix;
import ca.usask.simlab.odeToJava.scheme.Scheme;
import ca.usask.simlab.odeToJava.scheme.StormerVerletTableau;

/**
 * Module to implement the Stormer-Verlet method for an "Arenstorf" 3-body
 * problem.
 * <p>
 * Even though the canonical form of this problem has first derivatives, it is
 * possible to apply the Stormer-Verlet method and rewrite the equations to 
 * avoid solving implicit systems of equation.
 * <p>
 * Ernst Hairer, Christian Lubich, and Gerhard Wanner. "Geometric Numerical Integration: Structure-preserving algorithms for ordinary differential", 2002.
 * Ernst Hairer, Christian Lubich, and Gerhard Wanner. "Geometric numerical integration illustrated by the Störmer–Verlet method ", Acta Numerica, vol 12, pg 399-450, 2003.
 * Benedict Leimkuhler, Sebastian Reich. "Simulating Hamiltonian dynamics", Cambridge University Press, pg 161-163, 2004.
 */  
public class StormerVerletArenstorfOrbitModule extends SolverModule {

    public StormerVerletArenstorfOrbitModule() {
        // the required properties for the initial conditions
        require_property("initialTime");
        require_property("initialValues");
        require_property("finalTime");
        // the supplied properties from the integration
        supply_property("finalValues");
        supply_property("stageValues");
        supply_property("scheme");
        supply_property("schemeOrder");
    }

    @Override
    public void begin_stepping(PropertySolver solver, PropertyHolder properties) {
        Scheme tableau = new StormerVerletTableau();
        // set the properties related to the method
        properties.set_property("scheme", tableau);
        properties.set_property("schemeOrder", tableau.get_scheme_order());
    }

    @Override
    public void step(PropertyHolder step) {
        // using double arrays because of need to work with half vectors
        double t0 = step.getFloat64Property("initialTime").doubleValue();
        Float64Vector y0_full = step.get_Float64Vector_property("initialValues");
        double dt = step.getFloat64Property("finalTime").minus(t0).doubleValue();
        // the problem variables
        double[] q0 = new double[2];
        double[] p0 = new double[2];
        double[] qp = new double[2];
        double[] pp = new double[2];
        double[] q_half = new double[2];
        double[] p_half = new double[2];
        double[] p_half_intermediate = new double[2];
        double[] q1_intermediate = new double[2];
        double[] q1 = new double[2];
        double[] p1 = new double[2];
        // the final values to give back to the ODE
        double[] y1_full = new double[4];
        // extract the position and first and derivative
        q0[0] = y0_full.getValue(0);
        q0[1] = y0_full.getValue(1);
        p0[0] = y0_full.getValue(2);
        p0[1] = y0_full.getValue(3);
        // half the stepsize
        double dtb2 = dt/2.0;
        double denominator = 1.0 + (dt*dt)/4.0; 
        // the first formula
        qp = fqp_partial(q0,p0);
        p_half_intermediate[0] = p0[0] - dtb2*qp[0];
        p_half_intermediate[1] = p0[1] - dtb2*qp[1];
        p_half[0] = (p_half_intermediate[0] + dtb2*p_half_intermediate[1])/denominator;
        p_half[1] = (-dtb2*p_half_intermediate[0] + p_half_intermediate[1])/denominator;
        // the second formula
        pp = fpp(q0,p_half);
        q1_intermediate[0] = q0[0] + dtb2*pp[0];
        q1_intermediate[1] = q0[1] + dtb2*pp[1];
        q1[0] = (q1_intermediate[0] + dtb2*q1_intermediate[1])/denominator;
        q1[1] = (-dtb2*q1_intermediate[0] + q1_intermediate[1])/denominator;
        // the third formula
        qp = fqp(q1,p_half);
        p1[0] = p_half[0] - dtb2*qp[0];
        p1[1] = p_half[1] - dtb2*qp[1];
        // pack the vectors
        y1_full[0] = q1[0];
        y1_full[1] = q1[1];
        y1_full[2] = p1[0];
        y1_full[3] = p1[1];
        step.set_property("finalValues", Float64Vector.valueOf(y1_full));
    }

    // the function for the first derivative of position
    protected double[] fpp(double[] q, double[] p) {
        double[] pp = new double[2];

        pp[0] = 2.0*p[0] + q[1];
        pp[1] = 2.0*p[1] - q[0]; 

        return pp;
    }

    // the function for the partial second derivative of position
    protected double[] fqp_partial(double[] q, double[] p) { 
        double qp[] = new double[2];
        double d[] = denominators(q);

        qp[0] = muhat*(q[0]+mu)/d[0] + mu*(q[0]-muhat)/d[1];
        qp[1] = muhat*q[1]/d[0] + mu*q[1]/d[1]; 

        return qp; 
    }

    // the function for the partial second derivative of position
    protected double[] fqp(double[] q, double[] p) { 
        double qp[] = new double[2];
        double d[] = denominators(q);

        qp[0] = -p[1] + muhat*(q[0]+mu)/d[0] + mu*(q[0]-muhat)/d[1];
        qp[1] = p[0] + muhat*q[1]/d[0] + mu*q[1]/d[1]; 

        return qp; 
    }
 

    protected double[] denominators(double[] q) {
        double d[] = new double[2];

        d[0] = Math.pow(Math.pow((q[0]+mu), 2.0) + q[1]*q[1], 1.5);
        d[1] = Math.pow(Math.pow((q[0]-muhat), 2.0) + q[1]*q[1], 1.5); 

        return d;
    }

    protected final double mu = 0.012277471; // mass of the moon
    protected final double muhat = 1.0 - mu; // mass of the earth
}
