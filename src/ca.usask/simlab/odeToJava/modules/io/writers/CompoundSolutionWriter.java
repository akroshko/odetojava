/* ./modules/io/writers/CompoundSolutionWriter.java
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
package ca.usask.simlab.odeToJava.modules.io.writers;

import javolution.util.FastList;
import java.util.List;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Vector;

/**
 * This class is used to write solutions to using several writes at a time.
 */
public class CompoundSolutionWriter implements SolutionWriter {
    
    private List<SolutionWriter> solutionWriters = new FastList<SolutionWriter>();
    
    /**
     * Add a new solution writer to the compound solution writer.
     * 
     * @param writer The new writer to add.
     */
    public void add_solution_writer(SolutionWriter writer) {
        solutionWriters.add(writer);
    }
    
    @Override
    public void begin() {
        for (SolutionWriter e : solutionWriters) {
            e.begin();
        }
    }
    
    @Override
    public void end() {
        for (SolutionWriter e : solutionWriters) {
            e.end();
        }
    }
    
    @Override
    public void emit(Float64 t, Float64Vector y) {
        for (SolutionWriter e : solutionWriters) {
            e.emit(t, y);
        }
    }
}
