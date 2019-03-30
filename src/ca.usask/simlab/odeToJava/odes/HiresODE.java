/* ./odes/HiresODE.java
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
 * An ODE from plant physiology describing the 'High Irradiance RESponses' that model how light is involved in morphogenesis.
 * <p>
 * Francesca, Mazzia, Cecilia Magherini. "Test set for initial value problem solvers, release 2.4", pg II-1-1 - II-1-3, Department of Mathematics, University of Bari, Italy, 2008.
 */
public class HiresODE extends RHS {

    @Override
    public Float64Vector f(Float64 t, Float64Vector y)
    {
        Float64Vector yp;
        double y0 = y.getValue(0);
        double y1 = y.getValue(1);
        double y2 = y.getValue(2);
        double y3 = y.getValue(3);
        double y4 = y.getValue(4);
        double y5 = y.getValue(5);
        double y6 = y.getValue(6);
        double y7 = y.getValue(7);

        yp = Float64Vector.valueOf(
                -1.71*y0     + 0.43*y1 + 8.32*y2 + 0.0007,
                 1.71*y0     - 8.75*y1,
                -10.03*y2    + 0.43*y3 + 0.035*y4,
                 8.32*y1     + 1.71*y2 - 1.12*y3,
                -1.745*y4    + 0.43*y5 + 0.43*y6,
                -280.0*y5*y7 + 0.69*y3 + 1.71*y4 - 0.43*y5 + 0.69*y6,
                 280.0*y5*y7 - 1.81*y6,
                -280.0*y5*y7 + 1.81*y6);
        return yp;
    }

    @Override
    public int get_size() {
        return 8;
    }

}
