/* ./odes/PleiadesODE.java
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
import ca.usask.simlab.odeToJava.ode.RHS;

/**
 * An ODE describing a celestial mechanics problem of 7 stars in which quasi-collisions occur, i.e., close approaches between stars.  
 * <p>
 * Francesca, Mazzia, Cecilia Magherini. "Test set for initial value problem solvers, release 2.4", pg II-6-1 - II-6-9, Department of Mathematics, University of Bari, Italy, 2008.
 * <p>
 */     
public class PleiadesODE extends RHS {

    final int DERIVATIVE_OFFSET = 14; // index offset to indicate the partitions of the position and first derivative,
                                      // indicates the partitions of the first and second derivatives in the RHS evaluation
    final int Y_OFFSET = 7;           // index indicates the offset for the y components within the positions and derivatives
    double[][] r;                     // matrix of radii
    
    public PleiadesODE() {
        r = new double[Y_OFFSET][Y_OFFSET];
    }
    
    @Override
    public int get_size() {
        return 28;
    }
    
    @Override
    public Float64Vector f(Float64 t, Float64Vector z) {
        // uses z to indicate the full vector the first-order ODE RHS
        double[] zp = new double[z.getDimension()];
        
        // initialize the r_ij matrix
        for (int i = 0; i < Y_OFFSET; i++) {
            for (int j = 0; j < Y_OFFSET; j++) {
                // default to -1, this is a physically impossible value for the r
                r[i][j] = -1;
            }
        }
        
        for (int i = 0; i < DERIVATIVE_OFFSET; i++) {
            // set up the first derivatives, x' and y',
            // which are in the first half of the zp vector
            zp[i] = z.getValue(i + DERIVATIVE_OFFSET);
        }

        // set up the second derivatives, x"
        for (int i = 0; i < Y_OFFSET; i++) {
            double sum = 0.0;
            for (int j = 0; j < Y_OFFSET; j++) {
                // for efficiency r_ij only needs to be computed once
                if (r[i][j] == -1) {
                    r[i][j] = Math.pow(
                                (Math.pow((z.getValue(i) - z.getValue(j)), 2.0) + 
                                 Math.pow((z.getValue(i + Y_OFFSET) - z.getValue(j + Y_OFFSET)), 2.0)),
                                (3.0 / 2.0));
                }
                // sum up the forces on a star i in the x coordinate
                if (j != i) {
                    sum += (j + 1) * (z.getValue(j) - z.getValue(i)) / r[i][j];
                }
            }
            zp[i + DERIVATIVE_OFFSET] = sum;
        }
        
        // set up the second derivatives, y"
        for (int i = Y_OFFSET; i < DERIVATIVE_OFFSET; i++) {
            double sum = 0.0;
            for (int j = 0; j < Y_OFFSET; j++) {
                if (r[i - Y_OFFSET][j] == -1) {
                    r[i - Y_OFFSET][j] = Math.pow(
                                           (Math.pow((z.getValue(i - Y_OFFSET) - z.getValue(j)), 2.0) +
                                            Math.pow((z.getValue(i) - z.getValue(j + Y_OFFSET)), 2.0)),
                                           (3.0 / 2.0));
                }
                
                if (j != i - Y_OFFSET) {
                    sum += (j + 1) * (z.getValue(j + Y_OFFSET) - z.getValue(i)) / r[i - Y_OFFSET][j];
                }
            }
            // sum up the forces on a star i in the y coordinate
            zp[i + DERIVATIVE_OFFSET] = sum;
        }
        
        return Float64Vector.valueOf(zp);
    }
}
