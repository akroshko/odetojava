/* ./odes/PollutionODE.java
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
 * An ODE describing an air pollution model.
 * <p>
 * Francesca, Mazzia, Cecilia Magherini. "Test set for initial value problem solvers, release 2.4", pg II-2-1 - II-1-10, Department of Mathematics, University of Bari, Italy, 2008.
 */
public class PollutionODE extends RHS {
    double r[];

    public PollutionODE() {
        r = new double[25];
    }

    @Override
    public int get_size() {
        return 20;
    }

    @Override
    public Float64Vector f(Float64 t, Float64Vector y)
    {
        double y0 = y.getValue(0);
        double y1 = y.getValue(1);
        double y2 = y.getValue(2);
        double y3 = y.getValue(3);
        double y4 = y.getValue(4);
        double y5 = y.getValue(5);
        double y6 = y.getValue(6);
        // double y7 = y.getValue(7);
        double y8 = y.getValue(8);
        double y9 = y.getValue(9);
        double y10 = y.getValue(10);
        // double y11 = y.getValue(11);
        double y12 = y.getValue(12);
        double y13 = y.getValue(13);
        // double y14 = y.getValue(14);
        double y15 = y.getValue(15);
        double y16 = y.getValue(16);
        // double y17 = y.getValue(17);
        double y18 = y.getValue(18);
        double y19 = y.getValue(19);

        r[0] = PollutionODE.k[0] * y0;
        r[1] = PollutionODE.k[1] * y1 * y3;
        r[2] = PollutionODE.k[2] * y4 * y1;
        r[3] = PollutionODE.k[3] * y6;
        r[4] = PollutionODE.k[4] * y6;
        r[5] = PollutionODE.k[5] * y6 * y5;
        r[6] = PollutionODE.k[6] * y8;
        r[7] = PollutionODE.k[7] * y8 * y5;
        r[8] = PollutionODE.k[8] * y10 * y1;
        r[9] = PollutionODE.k[9] * y10 * y0;
        r[10] = PollutionODE.k[10] * y12;
        r[11] = PollutionODE.k[11] * y9 * y1;
        r[12] = PollutionODE.k[12] * y13;
        r[13] = PollutionODE.k[13] * y0 * y5;
        r[14] = PollutionODE.k[14] * y2;
        r[15] = PollutionODE.k[15] * y3;
        r[16] = PollutionODE.k[16] * y3;
        r[17] = PollutionODE.k[17] * y15;
        r[18] = PollutionODE.k[18] * y15;
        r[19] = PollutionODE.k[19] * y16 * y5;
        r[20] = PollutionODE.k[20] * y18;
        r[21] = PollutionODE.k[21] * y18;
        r[22] = PollutionODE.k[22] * y0 * y3;
        r[23] = PollutionODE.k[23] * y18 * y0;
        r[24] = PollutionODE.k[24] * y19;

        Float64Vector yp = Float64Vector.valueOf(
        -r[0] - r[9] - r[13] - r[22] - r[23] + r[1] + r[2] + r[8] + r[10] + r[11] + r[21] + r[24],
        -r[1] - r[2] - r[8] - r[11] + r[0] + r[20], // the matrix
        -r[14] + r[0] + r[16] + r[18] + r[21], // of values (a sum
        -r[1] - r[15] - r[16] - r[22] + r[14], // of r's)
        -r[2] + 2 * r[3] + r[5] + r[6] + r[12] + r[19],
        -r[5] - r[7] - r[13] - r[19] + r[2] + 2 * r[17],
        -r[3] - r[4] - r[5] + r[12],
         r[3] + r[4] + r[5] + r[6],
        -r[6] - r[7],
        -r[11] + r[6] + r[8],
        -r[8] - r[9] + r[7] + r[10],
         r[8],
        -r[10] + r[9],
        -r[12] + r[11],
         r[13],
        -r[17] - r[18] + r[15],
        -r[19],
         r[19],
        -r[20] - r[21] - r[23] + r[22] + r[24],
        -r[24] + r[23]);

        return yp;
    }

    private static final double[] k = { // array of equilibrium constants
    0.350, 0.266e2, 0.123e5, 0.860e-3, 0.820e-3, 0.150e5, 0.130e-3, 0.240e5, 0.165e5, 0.900e4, 0.220e-1, 0.120e5, 0.188e1, 0.163e5, 0.480e7, 0.350e-3, 0.175e-1, 0.100e9, 0.444e12, 0.124e4, 0.210e1, 0.578e1, 0.474e-1, 0.178e4, 0.312e1 };
}
