/* ./modules/io/InterpolatingSolutionWriterModule.java
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
package ca.usask.simlab.odeToJava.modules.io;

import java.io.PrintWriter;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.scheme.Scheme;
import ca.usask.simlab.odeToJava.modules.io.writers.SolutionWriter;
import ca.usask.simlab.odeToJava.property.PropertyHolder;
import ca.usask.simlab.odeToJava.solver.PropertySolver;
import ca.usask.simlab.odeToJava.solver.SolverModule;

/**
 * Module that writes the numerical solution at a fixed set of points or at a
 * fixed spacing by using a dense output methods.
 */
public class InterpolatingSolutionWriterModule extends SolverModule {
    Float64Vector interpolationTimes;
    Float64 interpolation_interval;
    boolean useInterval;
    Float64 last_written_time;
    int last_written_index;
    boolean started;
    Scheme butcher_tableau;
    PrintWriter out;
    SolutionWriter writer;
   
    /**
     * The default constructor that uses a set of specific times for dense
     * output.
     * 
     * @param writer             The solution writer to use.
     * @param interpolationTimes An array containing the times to write the solution at.
     */
    public InterpolatingSolutionWriterModule(SolutionWriter writer, Float64Vector interpolationTimes) {
        this();
        this.writer = writer;
        this.interpolationTimes = interpolationTimes;
        useInterval = false;
    }
    
    /**
     * The default constructor for writing the solution at particular intervals.
     * 
     * @param writer   The solution writer to use.
     * @param interval The interval to write the solution at.
     */
    public InterpolatingSolutionWriterModule(SolutionWriter writer, Float64 interval) {
        this();
        this.writer = writer;
        interpolation_interval = interval;
        useInterval = true;
    }
    
    private InterpolatingSolutionWriterModule() {
        started = false;
        require_property("scheme");
        require_property("initialTime");
        require_property("initialValues");
        require_property("finalTime");
        require_property("finalValues");
        require_property("stageValues");
        require_property("stepAccepted");
    }
    
    @Override
    public void begin_stepping(PropertySolver solver, PropertyHolder constant_properties) {
        // prevent initializing twice
        if (!started) {
            writer.begin();
            butcher_tableau = (Scheme) constant_properties.get_property("scheme");
            Float64 t0 = solver.get_initial_time();
            Float64Vector y0 = solver.get_initial_values();
            last_written_time = t0;
            last_written_index = 0;
            // print the initial point
            writer.emit(t0, y0);
            started = true;
        }
    }
    
    @Override
    public void step(PropertyHolder properties) {
        Float64 initial_time = properties.getFloat64Property("initialTime");
        Float64 final_time = properties.getFloat64Property("finalTime");
        Float64 h = final_time.minus(initial_time);
        Float64Vector yinitial = properties.get_Float64Vector_property("initialValues");
        Float64Vector yfinal = properties.get_Float64Vector_property("finalValues");
        Object stage_values = properties.get_property("stageValues");
        // do not write if the step was not accepted
        if (!properties.get_boolean_property("stepAccepted")) {
            return;
        }

        if (useInterval) {
            while (!final_time.isLessThan(last_written_time.plus(interpolation_interval))) {
                Float64 theta = last_written_time.plus(interpolation_interval).minus(initial_time).divide(h);
                Float64Vector y = interpolate_values(yinitial, yfinal, theta, h, stage_values);
                writer.emit(last_written_time.plus(interpolation_interval), y);
                last_written_time = last_written_time.plus(interpolation_interval);
            }
        } else {
            // do not go past the end of the set of interpolation times
            while (
                   (last_written_index + 1) < interpolationTimes.getDimension() &&
                   // ensure the desired time is bracketed
                   initial_time.isLessThan(interpolationTimes.get(last_written_index + 1)) &&
                   !final_time.isLessThan(interpolationTimes.get(last_written_index + 1))) {
                Float64 theta = interpolationTimes.get(last_written_index + 1).minus(initial_time).divide(h);
                Float64Vector y = interpolate_values(yinitial, yfinal, theta, h, stage_values);

                writer.emit(interpolationTimes.get(last_written_index + 1), y);
                last_written_index++;
            }
        }
    }
    
    @Override
    public void end_stepping() {
        writer.end();
    }
    
    /**
     * Evaluates the interpolant.
     * 
     * @param y0           The solution at the beginning of the step.
     * @param y1           The solution at the end of the step.
     * @param theta        The fractional distance within the current step to find the dense output.
     * @param dt           The current stepsize.
     * @param stage_values The stage values for this step when applicable.
     *
     * @return             The dense output value at the fraction theta within the stepsize.
     */ 
    private Float64Vector interpolate_values(Float64Vector y0, Float64Vector y1, Float64 theta, Float64 dt, Object stage_values) {
        if (theta.isLessThan(Float64.ZERO) || theta.isGreaterThan(Float64.ONE)) {
            throw new IllegalArgumentException("Theta passed (" + theta + ") is not between 0 and 1");
        }
        Float64Vector interpolant = butcher_tableau.get_interpolant().evaluate_interpolant(y0, y1, theta, dt, stage_values);
        return interpolant.plus(y0);
    }
}
