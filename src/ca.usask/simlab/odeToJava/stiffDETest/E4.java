/* ./stiffDETest/E4.java
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
package ca.usask.simlab.odeToJava.stiffDETest;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Matrix;
import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.ode.RHS;

/* Stiff DE test set problem E4, non-linear with non-real eigenvalues, an
 * artificial test problem.
 * <p>
 * Wayne Enright, JD Pryce. "Two FORTRAN packages for assessing initial value
 * methods", ACM transactions on mathematical software, vol 13, no 1, pg
 * 1-27, 1987.
 * <p>
 * Wayne Enright, TE Hull, B Lindberg. "Comparing numerical methods for stiff systems of ODEs", BIT numerical mathematics, vol 15, pg 10-48, 1975.
 */                       
public class E4 extends RHS {
    @Override
    public Float64Vector f(Float64 t, Float64Vector y) {
        Float64Vector yp;

        double[][] doubleU = new double[4][4];
        Float64Matrix U;
        Float64Vector Z;
        double[] Zarg;
        Float64Vector G;
        Float64Matrix extra;
        Float64Matrix negU; 
        
        // make the matrix 1/2 U
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == j) {
                    doubleU[i][j] = -0.5;
                } else {
                    doubleU[i][j] = 0.5;
                }
            }
        }
        U = Float64Matrix.valueOf(doubleU);
        Z = U.times(y); // Z = Uy
        
        Zarg = new double[4];
        Zarg[0] = (Z.getValue(0)*Z.getValue(0) - Z.getValue(1)*Z.getValue(1)) / 2.0;
        Zarg[1] = Z.getValue(0) * Z.getValue(1);
        Zarg[2] = Z.getValue(2) * Z.getValue(2);
        Zarg[3] = Z.getValue(3) * Z.getValue(3);
        G = U.transpose().times(Float64Vector.valueOf(Zarg));
        
        double[][] doubleExtra = new double[4][4];
        doubleExtra[0][0] = -10.0;
        doubleExtra[0][1] = -10.0;
        doubleExtra[1][0] = 10.0;
        doubleExtra[1][1] = -10.0;
        doubleExtra[2][2] = 1000.0;
        doubleExtra[3][3] = 0.01;
        extra = Float64Matrix.valueOf(doubleExtra);
        
        yp = U.transpose().times(Float64.valueOf(-1.0)).times(extra).times(Z).plus(G);
        
        return yp;
    }
    
    @Override
    public int get_size() {
        return 4;
    }
    
    
}
