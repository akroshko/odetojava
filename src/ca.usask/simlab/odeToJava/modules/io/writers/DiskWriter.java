/* ./modules/io/writers/DiskWriter.java
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

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Vector;

/**
 * Write the solution to disk.
 */
public class DiskWriter implements SolutionWriter {
    PrintWriter out;
    
    /**
     * The standard constructor for writing the solution to disk.
     *
     * @param filename               The filename to write to.
     *
     * @throws FileNotFoundException If the file was not found.
     * @throws IOException           If there was an error opening or writing to
     *                               the file. 
     */
    public DiskWriter(String filename) throws FileNotFoundException, IOException {
        out = new PrintWriter(new FileWriter(filename));
    }
    
    @Override
    public void begin() {
    }
    
    @Override
    public void emit(Float64 t, Float64Vector y) {
        out.print(t + " ");
        for (int i = 0; i < y.getDimension(); i++) {
            out.print(" " + y.get(i));
        }
        out.print("\n");
        return;
    }
    
    @Override
    public void end() {
        out.print("\n");
        out.flush();
        out.close();
    }
}
