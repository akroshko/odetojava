/* ./nonstiffDETest/C5.java
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
package ca.usask.simlab.odeToJava.nonstiffDETest;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.ode.RHS;
import ca.usask.simlab.odeToJava.util.Matrix;

/**
 * Nonstiff DE test set problem C5, five body problem, the motion of 5 outer
 * planets about the sun.
 * <p>
 * Wayne Enright, JD Pryce. "Two FORTRAN packages for assessing initial value
 * methods", ACM transactions on mathematical software, vol 13, no 1, pg
 * 1-27, 1987.
 * <p>
 * TE Hull, Wayne Enright, BM Fellen, AE Sedgwick. "Comparing numerical methods
 * for ordinary differential differential equations", SIAM journal of numerical
 * analysis, vol 9, no 4, pg 603-637, 1972.
 */
public class C5 extends RHS {

    @Override
    public Float64Vector f(Float64 t, Float64Vector y) {
        double yp[] = new double[30];

        double[][] y_pos = new double[3][5];
        double[][] ypp = new double[3][5];
        double[] r_squared = new double[5];
        double[] r_cubed = new double[5];
        double[][] d_squared = new double[5][5];
        double[][] d_cubed = new double[5][5];
        double dist; 
 
        // get an array to work in
        double y_double[] = Matrix.toDouble(y);
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 3; i++) { 
                y_pos[i][j] = y_double[i + j*3];
            }
        }

        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 3; i++) {
                r_squared[j] += y_pos[i][j]*y_pos[i][j];
            }
            r_cubed[j] = r_squared[j]*Math.sqrt(r_squared[j]);
        }

        for (int k = 0; k < 5; k++) {
            for (int j = 0; j < 5; j++) {
                for (int i = 0; i < 3; i++) {
                    dist = y_pos[i][k] - y_pos[i][j];
                    d_squared[k][j] += dist*dist;
                }
                d_cubed[k][j] = d_squared[k][j]*Math.sqrt(d_squared[k][j]);
            }
            
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                ypp[i][j] = -(this.m0 + this.m[j]) * y_pos[i][j]/r_cubed[j];
                for (int k = 0; k < 5; k++) {
                    if (k != j) {
                        ypp[i][j] += m[k] * (((y_pos[i][k] - y_pos[i][j])/d_cubed[j][k]) - y_pos[i][k]/r_cubed[k]);
                    }
                }
                ypp[i][j] *= this.k2;
            }
        }

        // create the final vector
        for (int i = 0; i < 15; i++) {
            yp[i] = y_double[15+i];
        }
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 5; k++) {
                yp[15 + j+k*3] = ypp[j][k];
            }
        }
        return Float64Vector.valueOf(yp);
    }

    @Override
    public int get_size() {
        return 30;
    }
    private double k2 = 2.9591220828;
    private double m0 = 1.0000059768;                        // mass of the Sun and the 4 inner planets
    private double m[] = new double[] { 0.00095478610404,    // mass of the Jupiter
                                        0.00028558373315,    // mass of the Saturn
                                        0.000043727316454,   // mass of the Uranus
                                        0.000051775913844,   // mass of the Neptune
                                        0.000002777777777 }; // mass of the Pluto
}
