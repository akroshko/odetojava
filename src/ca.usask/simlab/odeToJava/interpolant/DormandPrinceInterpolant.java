/* ./interpolant/DormandPrinceInterpolant.java
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
package ca.usask.simlab.odeToJava.interpolant;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Vector;
import org.jscience.mathematics.vectors.Float64Matrix;

/**
 * The interpolant for the Dormand-Prince 5(4) method.
 * <p>
 * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 191.
 */
public class DormandPrinceInterpolant extends RKInterpolant {
    private Float64Vector b;

    /**
     * The standard constructor.
     */
    public DormandPrinceInterpolant() {
        super(Float64Matrix.valueOf(new double[][] {{0}, {0}}));
        double[] b = { 35.0 / 384.0, 0, 500.0 / 1113.0, 125.0 / 192.0, -2187.0 / 6784.0, 11.0 / 84.0, 0 };
        this.b = Float64Vector.valueOf(b);
    }

    @Override
    protected Float64Vector btheta(Float64 theta_num) {
        double theta = theta_num.doubleValue();
        double[] bth = new double[7];
        double theta1 = DormandPrinceInterpolant.get_theta1(theta);
        double theta2 = DormandPrinceInterpolant.get_theta2(theta);

        bth[0] = theta1 * b.getValue(0) + theta * (theta - 1.0) * (theta - 1.0) - theta2 * 5.0 * (2558722523.0 - 31403016.0 * theta) / 11282082432.0;
        bth[1] = 0;
        bth[2] = theta1 * b.getValue(2) + theta2 * 100.0 * (882725551.0 - 15701508.0 * theta) / 32700410799.0;
        bth[3] = theta1 * b.getValue(3) - theta2 * 25.0 * (443332067.0 - 31403016.0 * theta) / 1880347072.0;
        bth[4] = theta1 * b.getValue(4) + theta2 * 32805.0 * (23143187.0 - 3489224.0 * theta) / 199316789632.0;
        bth[5] = theta1 * b.getValue(5) - theta2 * 55.0 * (29972135.0 - 7076736.0 * theta) / 822651844.0;
        bth[6] = theta * theta * (theta - 1.0) + theta2 * 10.0 * (7414447.0 - 829305.0 * theta) / 29380423.0;

        return Float64Vector.valueOf(bth);
    }

    private static double get_theta1(double theta) {
        return theta * theta * (3.0 - 2.0 * theta);
    }

    private static double get_theta2(double theta) {
        return theta * theta * (theta - 1.0) * (theta - 1.0);
    }
}
