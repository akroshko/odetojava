/* ./modules/io/AllSolutionWriterModule.java
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

import java.io.FileNotFoundException;
import java.io.IOException;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.modules.io.writers.SolutionWriter;
import ca.usask.simlab.odeToJava.property.PropertyHolder;
import ca.usask.simlab.odeToJava.solver.PropertySolver;
import ca.usask.simlab.odeToJava.solver.SolverModule;

/**
 * Write all solution points to disk.
 */
public class AllSolutionWriterModule extends SolverModule {
    private boolean started;
    SolutionWriter writer;
    
    /**
     * The standard constructor.
     *
     * @param  writer The solution write to use.
     *
     * @throws FileNotFoundException If the file was not found.
     * @throws IOException           If there was an error opening or writing to
     *                               the file.
     */
    public AllSolutionWriterModule(SolutionWriter writer) throws FileNotFoundException, IOException {
        this.writer = writer;
        require_property("finalTime");
        require_property("finalValues");
        require_property("stepAccepted");
        started = false;
    }
    
    @Override
    public void begin_stepping(PropertySolver solver, PropertyHolder constant_properties) {
        // prevent initializing twice
        if (!started) {
            writer.begin();
            Float64 t0 = solver.get_initial_time();
            Float64Vector y0 = solver.get_initial_values();
            // Print initial point
            writer.emit(t0, y0);
            started = true;
        }
    }
    
    /**
     * This prints the final point of every time step, the initial solution
     * point for the IVP is printed in begin_stepping.
     *
     * @param properties The properties for this step. 
     */
    @Override
    public void step(PropertyHolder properties) {
        Float64 t = properties.getFloat64Property("finalTime");
        Float64Vector y = properties.get_Float64Vector_property("finalValues");
        boolean emit = properties.get_boolean_property("stepAccepted");
        
        // Decide if we should emit this point,
        // emit a point if we have no max,
        // or if, given the total distance, it is time to emit another
        // to reach maxPoints by the end
        if (!emit) {
            return;
        }
        writer.emit(t, y);
        
    }
    
    @Override
    public void end_stepping() {
        writer.end();
    }
}
