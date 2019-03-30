/* ./modules/io/writers/SolutionWriter.java
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

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Vector;

/**
 * This interface defines a solution writer which writes the solution to various destinations
 */
public interface SolutionWriter {
    /**
     * Do any initialization required of the solution writer.
     */
    public void begin();
    
    /**
     * Emit a solution point.
     *
     * @param t The time to write.
     * @param y The solution at the time t.
     */
    public void emit(Float64 t, Float64Vector y);
    
    /**
     * Clean-up the solution writer.
     */
    public void end();
}
