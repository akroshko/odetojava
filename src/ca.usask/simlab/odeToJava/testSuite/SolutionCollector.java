/* ./testSuite/SolutionCollector.java
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
package ca.usask.simlab.odeToJava.testSuite;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Matrix;
import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.modules.io.writers.SolutionWriter;

/**
 * Collect a solution from the solver as it is proceeding.
 */
public class SolutionCollector implements SolutionWriter {
    /**
     * The set of solution times, as a Float64Vector.
     */      
    protected Float64Vector time;  

    /**
     * The set of solution points corresponding to each time, as rows
     * in a Float64Matrix.
     */     
    protected Float64Matrix solution;

    /**
     * The Vector of times that are being collected.
     */     
    protected java.util.Vector<Float64> time_vector;
 
    /**
     * The Vector of solution points that are being collected.
     */
    protected java.util.Vector<Float64Vector> solution_vector;
   
    /**
     * The default constructor.
     */
    public SolutionCollector() {
        solution_vector = new java.util.Vector<Float64Vector>();
        time_vector = new java.util.Vector<Float64>();
    }
    
    /*
     * @inheritDoc
     */
    public void begin() {
        time_vector.clear();
        solution_vector.clear();
    }
    
    /*
     * @inheritDoc
     * 
     * Collects the solution points and times together in this.time and
     * this.solution.
     */
    public void end() {
        double[] time = new double[time_vector.size()];
        double[][] solution = new double[solution_vector.size()][solution_vector.get(0).getDimension()];
        
        for (int i = 0; i < solution.length; i++) {
            time[i] = time_vector.get(i).doubleValue();
            for (int j = 0; j < solution[0].length; j++) {
                double temp = solution_vector.get(i).getValue(j);
                solution[i][j] = temp;
            }
        }
        
        this.time = Float64Vector.valueOf(time);
        this.solution = Float64Matrix.valueOf(solution);
    }
    
    /**
     * Return the solutions at the solution times.
     * 
     * @return The solution points as a Float64Matrix.
     */
    public Float64Matrix get_solution() {
        return solution;
    }
    
    /**
     * Return a vector of the solution times.
     * 
     * @return The solution times as a Float64Vector.  
     */
    public Float64Vector get_time() {
        return time;
    }
    
    /*
     * @inheritDoc
     *
     * Emits the solution to the collector to be used later.
     */ 
    public void emit(Float64 t, Float64Vector y) {
        // Collect solution from odeToJava
        time_vector.add(t);
        solution_vector.add(y);
    }
}
