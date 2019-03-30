/* ./modules/scheme/StormerVerletModule.java
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
 * Module to implement the Stormer-Verlet method.
 * <p>
 * Defined for problems where the first half of the solution vector
 * represents first derivatives and the second half of the solution 
 * represents second derivatives.
 * <p>
 * Ernst Hairer, Christian Lubich, and Gerhard Wanner. "Geometric Numerical Integration: Structure-preserving algorithms for ordinary differential", 2002, pg 7.
 */ 
public class StormerVerletModule extends SolverModule {
    protected RHS ode;
    protected Scheme tableau;

    public StormerVerletModule() {
        // the required properties for the initial conditions
        require_property("initialTime");
        require_property("initialValues");
        require_property("finalTime");
        
        // the supplied properties from the integration
        supply_property("finalValues");
        supply_property("stageValues"); 
        // the supplied properties related to the method
        supply_property("scheme");
        supply_property("schemeOrder");
        // the dummy tableau for Stormer-Verlet
        tableau = new StormerVerletTableau(); 
    }

    @Override
    public void begin_stepping(PropertySolver solver, PropertyHolder properties) { 
        // set the properties related to the method
        properties.set_property("scheme", tableau);
        properties.set_property("schemeOrder", tableau.get_scheme_order()); 

        ode = (RHS) solver.get_ODE();
    }

    @Override
    public void step(PropertyHolder step) {
        // using double arrays because of need to work with half vectors
        double t0 = step.getFloat64Property("initialTime").doubleValue();
        Float64Vector y0 = step.get_Float64Vector_property("initialValues");
        double h = step.getFloat64Property("finalTime").minus(t0).doubleValue(); 
        Float64Vector ynew;
        // find the size and half the size
        int m = ode.get_size();
        int half_m = ode.get_size() / 2; 
        // get the function evaluation
        double[] f0 = Matrix.toDouble(ode.f(Float64.valueOf(t0), Float64Vector.valueOf(y0)));
        // extract the first and second derivatives
        // p half
        double[] p0_half = new double[half_m];
        // q1
        double[] q1 = new double[half_m];
        // p1
        double[] p1 = new double[half_m];
        // f1
        double[] y_half = new double[m];
        for (int i = 0; i < half_m; i++) {
            // first formula
            p0_half[i] = y0.getValue(i + half_m) + h/2*f0[i+half_m];
            // second formula
            q1[i] = y0.getValue(i) + h*p0_half[i];
            y_half[i] = q1[i];
            y_half[i+half_m] = y0.getValue(i + half_m);
        }
        // third formula
        double[] y1 = new double[m];
        double[] f1 = Matrix.toDouble(ode.f(Float64.valueOf(t0+h), Float64Vector.valueOf(y_half)));
        for (int i = 0; i < half_m; i++) {
            p1[i] = p0_half[i] + (h/2)*f1[i+half_m]; 
            y1[i] = q1[i];
            y1[i+half_m] = p1[i];
        }

        step.set_property("finalValues", Float64Vector.valueOf(y1));
        step.set_property("stageValues", Float64.ZERO);
    }
}
