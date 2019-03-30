/* ./scheme/IMEXESDIRKButcherTableau.java
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
package ca.usask.simlab.odeToJava.scheme;

import org.jscience.mathematics.vectors.Float64Matrix;
import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.interpolant.Interpolant;
import ca.usask.simlab.odeToJava.interpolant.DefaultInterpolant;
import ca.usask.simlab.odeToJava.interpolant.ARKInterpolant;

/**
 * Class that defines IMEX Butcher tableau with an explicit method as the first
 * tableau and an explicit singly diagonally-implicit Runge-Kutta method as the second tableau,
 * which has the first stage explicit in the tableau.
 * <p>
 * Christopher A. Kennedy, Mark H. Carpenter. "Additive Runge-Kutta schemes for convection-diffusion-reaction equations.". Applied numerical mathematics, vol 44, pg 139-181, 2003.
 * <p>
 * Sebastiano Boscarino. "On an accurate third order implicit-explicit Runge-Kutta method for stiff problems", Applied Numerical Mathematics, vol 59, pg 1515-1528, 2009.
 */
public class IMEXESDIRKButcherTableau extends ARKButcherTableau {

    public IMEXESDIRKButcherTableau(ERKButcherTableau tableau_one, ERKButcherTableau tableau_two, int scheme_order, Interpolant interpolant, String name) {
        this(tableau_one, tableau_two, scheme_order, 0, interpolant, name);
    }

    public IMEXESDIRKButcherTableau(ERKButcherTableau tableau_one, ERKButcherTableau tableau_two, int scheme_order, int embedded_order, Interpolant interpolant, String name) {
        super(tableau_one, tableau_two, scheme_order, embedded_order, interpolant, name);
    }

    /**
     * Implicit-explicit Runge-Kutta forward-backward Euler (1,2,1), order 1.
     * <p>
     * Uri Ascher, Steven Ruuth, Raymond Spiteri. "Implicit-explicit Runge-Kutta methods for time-dependent partial differential equations", Applied Numerical Mathematics, vol 25, pg 151-167, 1997.
     *
     * @return The Butcher tableau for ARS (1,2,1).
     */
    public static IMEXESDIRKButcherTableau get_ARS121_tableau() {
        double[][] ahat = new double[2][];
        ahat[0] = new double[2];
        ahat[1] = new double[] {1, 0};
        double[] bhat = new double[] {0, 1};

        double[][] a = new double[2][];
        a[0] = new double[2];
        a[1] = new double[] {0, 1};
        double[] b = bhat;

        int order = 1;

        ERKButcherTableau explicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(ahat), Float64Vector.valueOf(bhat), null, true, order, new DefaultInterpolant(),"explicit");
        ERKButcherTableau implicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), null, true, order, new DefaultInterpolant(),"implicit");
        explicitTableau.is_FSAL = true;
        implicitTableau.is_FSAL = true;

        return new IMEXESDIRKButcherTableau(explicitTableau, implicitTableau, order, new DefaultInterpolant(), "Implicit-explicit Runge-Kutta forward-backward Euler (1,2,1), order 1.");
    }

    /**
     * Implicit-explicit  Runge-Kutta midpoint (1,2,2), order 2.
     * <p>
     * Uri Ascher, Steven Ruuth, Raymond Spiteri. "Implicit-explicit Runge-Kutta methods for time-dependent partial differential equations", Applied Numerical Mathematics, vol 25, pg 151-167, 1997.
     *
     * @return The Butcher tableau for ARS (1,2,2).
     */
    public static IMEXESDIRKButcherTableau get_ARS122_tableau() {
        double[][] ahat = new double[2][];
        ahat[0] = new double[2];
        ahat[1] = new double[] {0.5, 0};
        double[] bhat = new double[] {0, 1};

        double[][] a = new double[2][];
        a[0] = new double[2];
        a[1] = new double[] {0, 0.5};
        double[] b = bhat;

        int order = 2;

        ERKButcherTableau explicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(ahat), Float64Vector.valueOf(bhat), null, false, order, new DefaultInterpolant(), "explicit");
        ERKButcherTableau implicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), null, false, order, new DefaultInterpolant(),"implicit");
        explicitTableau.is_FSAL = true;
        implicitTableau.is_FSAL = true;

        return new IMEXESDIRKButcherTableau(explicitTableau, implicitTableau, order, new DefaultInterpolant(), "Implicit-explicit  Runge-Kutta midpoint (1,2,2), order 2");
    }

    /**
     * Implicit-explicit Runge-Kutta (2,3,3), order 3.
     * <p>
     * Uri Ascher, Steven Ruuth, Raymond Spiteri. "Implicit-explicit Runge-Kutta methods for time-dependent partial differential equations", Applied Numerical Mathematics, vol 25, pg 151-167, 1997.
     *
     * @return The Butcher tableau for ARS (2,3,3).
     */
    public static IMEXESDIRKButcherTableau get_ARS233_tableau() {
        double gamma = (3 + Math.sqrt(3)) / 6;
        double[][] ahat = new double[3][];
        ahat[0] = new double[3];
        ahat[1] = new double[] {gamma, 0 , 0};
        ahat[2] = new double[] {gamma - 1, 2*(1-gamma), 0 };
        double[] bhat = new double[] {0, 0.5, 0.5};

        double[][] a = new double[3][];
        a[0] = new double[3];
        a[1] = new double[] {0, gamma, 0 };
        a[2] = new double[] {0, 1 - 2*gamma, gamma };
        double[] b = bhat;

        int order = 3;

        ERKButcherTableau explicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(ahat), Float64Vector.valueOf(bhat), null, false, order, new DefaultInterpolant(), "explicit");
        ERKButcherTableau implicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), null, false, order, new DefaultInterpolant(),"implicit");

        return new IMEXESDIRKButcherTableau(explicitTableau, implicitTableau, order, new DefaultInterpolant(), "Implicit-explicit Runge-Kutta (2,3,3), order 3");
    }

    /**
     * Implicit-explicit Runge-Kutta L-stable, (2,3,2), order 2.
     * <p>
     * Uri Ascher, Steven Ruuth, Raymond Spiteri. "Implicit-explicit Runge-Kutta methods for time-dependent partial differential equations", Applied Numerical Mathematics, vol 25, pg 151-167, 1997.
     *
     * @return The Butcher tableau for ARS (2,3,2).
     */
    public static IMEXESDIRKButcherTableau get_ARS232_tableau() {
        double gamma = (2.0 - Math.sqrt(2)) / 2;
        double delta = (2 * Math.sqrt(2)) / 3;
        double[][] ahat = new double[3][];
        ahat[0] = new double[3];
        ahat[1] = new double[] { gamma, 0, 0 };
        ahat[2] = new double[] { delta, 1 - delta, 0};
        double[] bhat = new double[] { 0, 1 - gamma, gamma };

        double[][] a = new double[3][];
        a[0] = new double[3];
        a[1] = new double[] {0, gamma, 0};
        a[2] = new double[] {0, 1 - gamma, 0 };
        double[] b = bhat;

        int order = 2;

        ERKButcherTableau explicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(ahat), Float64Vector.valueOf(bhat), null, true, order, null, "explicit");
        ERKButcherTableau implicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), null, true, order, null,"implicit");
        explicitTableau.is_FSAL = true;
        implicitTableau.is_FSAL = true;

        return new IMEXESDIRKButcherTableau(explicitTableau, implicitTableau, order, new DefaultInterpolant(), "Implicit-explicit Runge-Kutta L-stable, (2,3,2), order 2");
    }

    /**
     * Implicit-explicit Runge Kutta, L-stable (2,2,2), order 2.
     * <p>
     * Uri Ascher, Steven Ruuth, Raymond Spiteri. "Implicit-explicit Runge-Kutta methods for time-dependent partial differential equations", Applied Numerical Mathematics, vol 25, pg 151-167, 1997.
     *
     * @return The Butcher tableau for ARS (2,2,2).
     */
    public static IMEXESDIRKButcherTableau get_ARS222_tableau() {
        double gamma = (2.0 - Math.sqrt(2)) / 2;
        double delta = 1 - (1 / (2 * gamma));
        double[][] ahat = new double[3][];
        ahat[0] = new double[3];
        ahat[1] = new double[] { gamma, 0, 0 };
        ahat[2] = new double[] { delta, 1 - delta, 0};
        double[] bhat = new double[] {delta, 1 - delta, 0 };

        double[][] a = new double[3][];
        a[0] = new double[3];
        a[1] = new double[] {0, gamma, 0};
        a[2] = new double[] {0, 1 - gamma, 0 };
        double[] b = new double[] { 0, 1 - gamma, gamma };

        int order = 2;

        ERKButcherTableau explicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(ahat), Float64Vector.valueOf(bhat), null, true, order, new DefaultInterpolant(), "explicit");
        ERKButcherTableau implicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), null, true, order, new DefaultInterpolant(),"implicit");

        return new IMEXESDIRKButcherTableau(explicitTableau, implicitTableau, order, new DefaultInterpolant(), "Implicit-explicit Runge Kutta, L-stable (2,2,2), order 2");
    }

    /**
     * Implicit-explicit Runge-Kutta, L-stable (3,4,3), order 3.
     * <p>
     * Uri Ascher, Steven Ruuth, Raymond Spiteri. "Implicit-explicit Runge-Kutta methods for time-dependent partial differential equations", Applied Numerical Mathematics, vol 25, pg 151-167, 1997.
     *
     * @return The Butcher tableau for ARS (3,4,3).
     */
    public static IMEXESDIRKButcherTableau get_ARS343_tableau() {
         double[][] ahat = new double[4][];
         ahat[0] = new double[4];
         ahat[1] = new double[] { 0.4358665215, 0, 0, 0 };
         ahat[2] = new double[] { 0.3212188860, 0.3966543747, 0, 0};
         ahat[3] = new double[] { -0.105858296, 0.5529291479, 0.5529291479, 0};
         double[] bhat = new double[] {0, 1.208496649, -0.644363171, 0.4358665215};

         double[][] a = new double[4][];
         a[0] = new double[4];
         a[1] = new double[] {0, 0.4358665215, 0, 0};
         a[2] = new double[] {0, 0.2820667392, 0.4358665215, 0};
         a[3] = new double[] {0, 1.208496649, -0.644363171, 0.4358665215};
         double[] b = bhat;

         int order = 3;

         ERKButcherTableau explicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(ahat), Float64Vector.valueOf(bhat), null, true, order, new DefaultInterpolant(), "explicit");
         ERKButcherTableau implicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), null, true, order, new DefaultInterpolant(),"implicit");

         return new IMEXESDIRKButcherTableau(explicitTableau, implicitTableau, order, new DefaultInterpolant(), "Implicit-explicit Runge-Kutta, L-stable (3,4,3), order 3");
    }

    /**
     * Implicit-explicit Runge-Kutta, L-stable (4,4,3), order 3.
     * <p>
     * Uri Ascher, Steven Ruuth, Raymond Spiteri. "Implicit-explicit Runge-Kutta methods for time-dependent partial differential equations", Applied Numerical Mathematics, vol 25, pg 151-167, 1997.
     *
     * @return The Butcher tableau for ARS (4,4,3).
     */
    public static IMEXESDIRKButcherTableau get_ARS443_tableau() {
        double[][] a = new double[5][5];
        double[] b = new double[5];

        double[][] ahat = new double[5][5];
        double[] bhat = new double[5];

        int order = 3;

        a[1][1] = 1.0 / 2.0;

        a[2][1] = 1.0 / 6.0;
        a[2][2] = 1.0 / 2.0;

        a[3][1] = -1.0 / 2.0;
        a[3][2] = 1.0 / 2.0;
        a[3][3] = 1.0 / 2.0;

        a[4][1] = 3.0 / 2.0;
        a[4][2] = -3.0 / 2.0;
        a[4][3] = 1.0 / 2.0;
        a[4][4] = 1.0 / 2.0;

        b[1] = 3.0 / 2.0;
        b[2] = -3.0 / 2.0;
        b[3] = 1.0 / 2.0;
        b[4] = 1.0 / 2.0;

        ahat[1][0] = 1.0 / 2.0;

        ahat[2][0] = 11.0 / 18.0;
        ahat[2][1] = 1.0 / 18.0;

        ahat[3][0] = 5.0 / 6.0;
        ahat[3][1] = -5.0 / 6.0;
        ahat[3][2] = 1.0 / 2.0;

        ahat[4][0] = 1.0 / 4.0;
        ahat[4][1] = 7.0 / 4.0;
        ahat[4][2] = 3.0 / 4.0;
        ahat[4][3] = -7.0 / 4.0;

        bhat[0] = 1.0 / 4.0;
        bhat[1] = 7.0 / 4.0;
        bhat[2] = 3.0 / 4.0;
        bhat[3] = -7.0 / 4.0;

        ERKButcherTableau explicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(ahat), Float64Vector.valueOf(bhat), null, true, 3, new DefaultInterpolant(), "explicit");
        ERKButcherTableau implicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), null, true, 3, new DefaultInterpolant(),"implicit");

        return new IMEXESDIRKButcherTableau(explicitTableau, implicitTableau, order, new DefaultInterpolant(), "Implicit-explicit Runge-Kutta, L-stable (4,4,3), order 3");
    }

    /**
     * IMEX Runge-Kutta, LIRK, order 3, embedded order 2.
     * <p>
     * Manuel Calvo, Javier de Frutos, Julia Novo. "Linearly implicit Runge—Kutta methods for advection—reaction—diffusion equations.". Applied numerical mathematics, vol 37, pg 535-549, 2001.
     *
     * @return The butcher tableau for LIRK 3(2).
     */
    public static IMEXESDIRKButcherTableau get_LIRK32_tableau() {
        double gamma = 0.435866521508459;
        double b2 = -3.0/2.0 * gamma*gamma + 4.0*gamma - 1.0/4.0;
        double b3 = 3.0/2.0 * gamma*gamma - 5.0*gamma + 5.0/4.0;
        double a32 = -0.35;
        double a43 = (1.0/3.0 - 2.0*gamma*gamma - 2.0*b3*a32*gamma) / gamma*(1.0 - gamma);
        double[][] ahat = new double[5][];
        ahat[0] = new double[5];
        ahat[1] = new double[] { gamma, 0.0, 0.0, 0.0, 0.0 };
        ahat[2] = new double[] { (1 + gamma)/2.0 - a32, a32, 0.0, 0.0, 0.0 }; 
        ahat[3] = new double[] { 0.0, 1.0 - a43, a43, 0.0, 0.0 }; 
        ahat[4] = new double[] { 0.0, b2, b3, gamma, 0.0 }; 

        double[] bhat = new double[] {  0.0, b2, b3, gamma, 0.0 };
        double[] bhat_embedded = new double[] { 0.0, b2, b3, gamma, 0.0 };

        double[][] a = new double[5][];
        a[0] = new double[5];
        a[1] = new double[] { 0.0, gamma, 0.0, 0.0, 0.0 };
        a[2] = new double[] { 0.0, (1 - gamma)/2.0 , gamma, 0.0, 0.0 };
        a[3] = new double[] { 0.0, b2, b3, gamma, 0.0 };
        a[4] = new double[] { 0.0, b2, b3, 0.0, gamma };

        double[] b = bhat;
        double[] b_embedded = new double[] { 0.0, b2, b3, 0.0, gamma };

        int order = 3;
        int order_embedded = 2;

        ERKButcherTableau explicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(ahat), Float64Vector.valueOf(bhat), Float64Vector.valueOf(bhat_embedded), null, true, 3, 2, new DefaultInterpolant(), "explicit");
        ERKButcherTableau implicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b),  Float64Vector.valueOf(b_embedded), null, true, 3, 2, new DefaultInterpolant(), "implicit");

        return new IMEXESDIRKButcherTableau(explicitTableau, implicitTableau, order, order_embedded, new DefaultInterpolant(), "IMEX Runge-Kutta, LIRK, order 3, embedded order 2");
    } 

    /**
     * IMEX Runge-Kutta, LIRK, order 4, embedded order 3.
     * <p>
     * Manuel Calvo, Javier de Frutos, Julia Novo. "Linearly implicit Runge—Kutta methods for advection—reaction—diffusion equations.". Applied numerical mathematics, vol 37, pg 535-549, 2001.
     *
     * @return The butcher tableau for LIRK 4(3).
     */
    public static IMEXESDIRKButcherTableau get_LIRK43_tableau() {
        double[][] ahat = new double[6][];
        ahat[0] = new double[6];
        ahat[1] = new double[]          { 0.25, 0.0, 0.0, 0.0, 0.0, 0.0 };
        ahat[2] = new double[]          { -0.25, 1.0, 0.0, 0.0, 0.0, 0.0 };
        ahat[3] = new double[]          { -13.0/100.0, 43.0/75.0, 8.0/75.0, 0.0, 0.0, 0.0 }; 
        ahat[4] = new double[]          { -6.0/85.0, 42.0/85.0, 179.0/1360.0, -15.0/272.0, 0.0, 0.0 }; 
        ahat[5] = new double[]          { 0.0, 79.0/24.0, -5.0/8.0, 25.0/2.0, -85.0/6.0, 0.0 }; 

        double[] bhat = new double[]    { 0.0, 25.0/24.0, -49.0/48.0, 125.0/16.0, -85.0/12.0, 1.0/4.0 }; // b for the explicit part
        double[] bhat_embedded = new double[] { 0.0, 59.0/48.0, -17.0/96.0, 225.0/32.0, -85.0/12.0, 0.0 }; // embedded b for the explicit part

        double[][] a = new double[6][];
        a[0] = new double[6];
        a[1] = new double[] { 0.0, 0.25, 0.0, 0.0, 0.0, 0.0 };
        a[2] = new double[] { 0.0, 0.5, 0.25, 0.0, 0.0, 0.0 };
        a[3] = new double[] { 0.0, 17.0/50.0, -1.0/25.0, 0.25, 0.0, 0.0 };
        a[4] = new double[] { 0.0, 371.0/1360.0, -137.0/2720.0, 15.0/544.0, 0.25, 0.0 }; 
        a[5] = new double[] { 0.0, 25.0/24.0, -49.0/48.0, 125.0/16.0, -85.0/12.0, 0.25}; 

        double[] b = bhat;
        double[] b_embedded = bhat_embedded;

        int order = 4;
        int order_embedded = 3;

        ERKButcherTableau explicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(ahat), Float64Vector.valueOf(bhat), Float64Vector.valueOf(bhat_embedded), null, true, order, order_embedded, new DefaultInterpolant(), "explicit");
        ERKButcherTableau implicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b),  Float64Vector.valueOf(b_embedded), null, true, order, order_embedded, new DefaultInterpolant(), "implicit");

        return new IMEXESDIRKButcherTableau(explicitTableau, implicitTableau, order, order_embedded, new DefaultInterpolant(), "IMEX Runge-Kutta, LIRK, order 4, embedded order 3");
    }


    /**
     * Additive Runge-Kutta, order 3, embedded order 2.
     * <p>
     * Christopher A. Kennedy, Mark H. Carpenter. "Additive Runge-Kutta schemes for convection-diffusion-reaction equations.". Applied numerical mathematics, vol 44, pg 139-181, 2003.
     *
     * @return The butcher tableau for ARK3(2)4L[2]SA.
     */
    public static IMEXESDIRKButcherTableau get_KC32_tableau() {
         double[][] ahat = new double[4][];
         ahat[0] = new double[4];
         ahat[1] = new double[] { 1767732205903.0/2027836641118.0, 0, 0, 0 };
         ahat[2] = new double[] { 5535828885825.0/10492691773637.0, 788022342437.0/10882634858940.0, 0, 0 };
         ahat[3] = new double[] { 6485989280629.0/16251701735622.0, -4246266847089.0/9704473918619.0, 10755448449292.0/10357097424841.0, 0};


         double[] bhat = new double[] { 1471266399579.0/7840856788654.0, -4482444167858.0/7529755066697.0, 11266239266428.0/11593286722821.0, 1767732205903.0/4055673282236.0 };

         double[] bhat_embedded = new double[] { 2756255671327.0/12835298489170.0, -10771552573575.0/22201958757719.0, 9247589265047.0/10645013368117.0, 2193209047091.0/5459859503100.0};

         double[][] a = new double[4][];
         a[0] = new double[4];
         a[1] = new double[] { 1767732205903.0/4055673282236.0, 1767732205903.0/4055673282236.0, 0.0, 0.0 };
         a[2] = new double[] { 2746238789719.0/10658868560708.0, -640167445237.0/6845629431997.0, 1767732205903.0/4055673282236.0, 0 };
         a[3] = new double[] { 1471266399579.0/7840856788654.0, -4482444167858.0/7529755066697.0, 11266239266428.0/11593286722821.0, 1767732205903.0/4055673282236.0};

         double[] b = bhat; // b for the implicit part

         double[] b_embedded = bhat_embedded; // embedded b for the implicit part

         int order = 3;
         int order_embedded = 2;

         ERKButcherTableau explicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(ahat), Float64Vector.valueOf(bhat), Float64Vector.valueOf(bhat_embedded), null, true, 3, 2, new DefaultInterpolant(), "explicit");
         ERKButcherTableau implicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b),  Float64Vector.valueOf(b_embedded), null, true, 3, 2, new DefaultInterpolant(),"implicit");

         return new IMEXESDIRKButcherTableau(explicitTableau, implicitTableau, order, order_embedded, new DefaultInterpolant(), "Additive Runge-Kutta, order 3, embedded order 2");
    }

    /**
     * Additive Runge-Kutta, order 4, embedded order 3.
     * <p>
     * Christopher A. Kennedy, Mark H. Carpenter. "Additive Runge-Kutta schemes for convection-diffusion-reaction equations.". Applied numerical mathematics, vol 44, pg 139-181, 2003.
     *
     * @return The butcher tableau for ARK4(3)6L[2]SA
     */
    public static IMEXESDIRKButcherTableau get_KC43_tableau() {
        double[][] ahat = new double[6][];
        ahat[0] = new double[6];
        ahat[1] = new double[] { 1.0 / 2.0, 0, 0, 0, 0, 0 };
        ahat[2] = new double[] { 13861.0 / 62500.0, 6889.0 / 62500.0, 0, 0, 0, 0 };
        ahat[3] = new double[] { -116923316275.0 / 2393684061468.0, -2731218467317.0 / 15368042101831.0, 9408046702089.0 / 11113171139209.0, 0, 0, 0 };
        ahat[4] = new double[] { -451086348788.0 / 2902428689909.0, -2682348792572.0 / 7519795681897.0, 12662868775082.0 / 11960479115383.0, 3355817975965.0 / 11060851509271.0, 0, 0 };
        ahat[5] = new double[] { 647845179188.0 / 3216320057751.0, 73281519250.0 / 8382639484533.0, 552539513391.0 / 3454668386233.0, 3354512671639.0 / 8306763924573.0, 4040.0 / 17871.0, 0 };

        double[] bhat = new double[] { 82889.0 / 524892.0, 0, 15625.0 / 83664.0, 69875.0 / 102672.0, -2260.0 / 8211.0, 1.0 / 4.0 };

        double[] bhat_embedded = new double[] { 4586570599.0 / 29645900160.0, 0, 178811875.0 / 945068544.0, 814220225.0 / 1159782912.0, -3700637.0 / 11593932.0, 61727.0 / 225920.0 };

        double[][] a = new double[6][];
        a[0] = new double[6];
        a[1] = new double[] { 1.0 / 4.0, 1.0 / 4.0, 0, 0, 0, 0 };
        a[2] = new double[] { 8611.0 / 62500.0, -1743.0 / 31250.0, 1.0 / 4.0, 0, 0, 0 };
        a[3] = new double[] { 5012029.0 / 34652500.0, -654441.0 / 2922500.0, 174375.0 / 388108.0, 1.0 / 4.0, 0, 0 };
        a[4] = new double[] { 15267082809.0 / 155376265600.0, -71443401.0 / 120774400.0, 730878875.0 / 902184768.0, 2285395.0 / 8070912.0, 1.0 / 4.0, 0 };
        a[5] = new double[] { 82889.0 / 524892.0, 0, 15625.0 / 83664.0, 69875.0 / 102672.0, -2260.0 / 8211.0, 1.0 / 4.0 };

        double[] b = new double[] { 82889.0 / 524892.0, 0, 15625.0 / 83664.0, 69875.0 / 102672.0, -2260.0 / 8211.0, 1.0 / 4.0 };

        double[] b_embedded = new double[] { 4586570599.0 / 29645900160.0, 0, 178811875.0 / 945068544.0, 814220225.0 / 1159782912.0, -3700637.0 / 11593932.0, 61727.0 / 225920.0 };

        int order = 4;
        int order_embedded = 3;

        ERKButcherTableau explicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(ahat), Float64Vector.valueOf(bhat), Float64Vector.valueOf(bhat_embedded), null, true, order, order_embedded, null, "explicit");
        ERKButcherTableau implicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), Float64Vector.valueOf(b_embedded), null, true, order, order_embedded, null,"implicit");
        return new IMEXESDIRKButcherTableau(explicitTableau, implicitTableau, order, order_embedded, ARKInterpolant.get_KC43_interpolant(), "Additive Runge-Kutta, order 4, embedded order 3");

    }

    /**
     * Additive Runge-Kutta, order 5, embedded order 4.
     * <p>
     * Christopher A. Kennedy, Mark H. Carpenter. "Additive Runge-Kutta schemes for convection-diffusion-reaction equations.". Applied numerical mathematics, vol 44, pg 139-181, 2003.
     *
     * @return The butcher tableau for ARK5(4)8L[2]SA
     */
    public static IMEXESDIRKButcherTableau get_KC54_tableau() {
        double[][] ahat = new double[8][];
        ahat[0] = new double[8];
        ahat[1] = new double[] { 41.0 / 100.0, 0, 0, 0, 0, 0, 0, 0 };
        ahat[2] = new double[] { 367902744464.0 / 2072280473677.0, 677623207551.0 / 8224143866563.0, 0, 0, 0, 0, 0, 0 };
        ahat[3] = new double[] { 1268023523408.0 / 10340822734521.0, 0, 1029933939417.0 / 13636558850479.0, 0, 0, 0, 0, 0 };
        ahat[4] = new double[] { 14463281900351.0 / 6315353703477.0, 0, 66114435211212.0 / 5879490589093.0, -54053170152839.0 / 4284798021562.0, 0, 0, 0, 0 };
        ahat[5] = new double[] { 14090043504691.0 / 34967701212078.0, 0, 15191511035443.0 / 11219624916014.0, -18461159152457.0 / 12425892160975.0, -281667163811.0 / 9011619295870.0, 0, 0, 0 };
        ahat[6] = new double[] { 19230459214898.0 / 13134317526959.0, 0, 21275331358303.0 / 2942455364971.0, -38145345988419.0 / 4862620318723.0, -1.0 / 8.0, -1.0 / 8.0, 0, 0 };
        ahat[7] = new double[] { -19977161125411.0 / 11928030595625.0, 0, -40795976796054.0 / 6384907823539.0, 177454434618887.0 / 12078138498510.0, 782672205425.0 / 8267701900261.0, -69563011059811.0 / 9646580694205.0, 7356628210526.0 / 4942186776405.0, 0 };

        double[] bhat = new double[] { -872700587467.0 / 9133579230613.0, 0, 0, 22348218063261.0 / 9555858737531.0, -1143369518992.0 / 8141816002931.0, -39379526789629.0 / 19018526304540.0, 32727382324388.0 / 42900044865799.0, 41.0 / 200.0 };
        double[] bhat_embedded = new double[] { -975461918565.0 / 9796059967033.0, 0, 0, 78070527104295.0 / 32432590147079.0, -548382580838.0 / 3424219808633.0, -33438840321285.0 / 15594753105479.0, 3629800801594.0 / 4656183773603.0, 4035322873751.0 / 18575991585200.0 };

        double[][] a = new double[8][];
        a[0] = new double[8];
        a[1] = new double[] { 41.0 / 200.0, 41.0 / 200.0, 0, 0, 0, 0, 0, 0 };
        a[2] = new double[] { 41.0 / 400.0, -567603406766.0 / 11931857230679.0, 41.0 / 200.0, 0, 0, 0, 0, 0 };
        a[3] = new double[] { 683785636431.0 / 9252920307686.0, 0, -110385047103.0 / 1367015193373.0, 41.0 / 200.0, 0, 0, 0, 0 };
        a[4] = new double[] { 3016520224154.0 / 10081342136671.0, 0, 30586259806659.0 / 12414158314087.0, -22760509404356.0 / 11113319521817.0, 41.0 / 200.0, 0, 0, 0 };
        a[5] = new double[] { 218866479029.0 / 1489978393911.0, 0, 638256894668.0 / 5436446318841.0, -1179710474555.0 / 5321154724896.0, -60928119172.0 / 8023461067671.0, 41.0 / 200.0, 0, 0 };
        a[6] = new double[] { 1020004230633.0 / 5715676835656.0, 0, 25762820946817.0 / 25263940353407.0, -2161375909145.0 / 9755907335909.0, -211217309593.0 / 5846859502534.0, -4269925059573.0 / 7827059040749.0, 41.0 / 200.0, 0 };
        a[7] = new double[] { -872700587467.0 / 9133579230613.0, 0, 0, 22348218063261.0 / 9555858737531.0, -1143369518992.0 / 8141816002931.0, -39379526789629.0 / 19018526304540.0, 32727382324388.0 / 42900044865799.0, 41.0 / 200.0 };

        double[] b = new double[] { -872700587467.0 / 9133579230613.0, 0, 0, 22348218063261.0 / 9555858737531.0, -1143369518992.0 / 8141816002931.0, -39379526789629.0 / 19018526304540.0, 32727382324388.0 / 42900044865799.0, 41.0 / 200.0 };
        double[] b_embedded = new double[] { -975461918565.0 / 9796059967033.0, 0, 0, 78070527104295.0 / 32432590147079.0, -548382580838.0 / 3424219808633.0, -33438840321285.0 / 15594753105479.0, 3629800801594.0 / 4656183773603.0, 4035322873751.0 / 18575991585200.0 };

        int order = 5;
        int order_embedded = 4;

        ERKButcherTableau explicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(ahat), Float64Vector.valueOf(bhat), Float64Vector.valueOf(bhat_embedded), null, true, order, order_embedded, new DefaultInterpolant(), "explicit");
        ERKButcherTableau implicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), Float64Vector.valueOf(b_embedded), null, true, order, order_embedded, new DefaultInterpolant(),"implicit");

        return new IMEXESDIRKButcherTableau(explicitTableau, implicitTableau, order, order_embedded, ARKInterpolant.get_KC54_interpolant(), "Additive Runge-Kutta, order 5, embedded order 4");
    }

    /**
     * Implicit-explicit Runge-Kutta, uniformly accurate, order 3, tableau
     * version 1.
     * <p>
     * Sebastiano Boscarino. "On an accurate third order implicit-explicit Runge–Kutta method
for stiff problems", Applied Numerical Mathematics, vol 59, pg 1515-1528, 2009.
     *
     * @return The Butcher tableau for BHR (5,5,3), tableau version 1.
     */
    public static IMEXESDIRKButcherTableau get_BHR5531_tableau() {
        double b1 = 487698502336740678603511.0/1181159636928185920260208.0;
        double b2 = 0.0;
        double b3 = 302987763081184622639300143137943089.0/1535359944203293318639180129368156500.0;
        double b4 = -105235928335100616072938218863.0/2282554452064661756575727198000.0;
        double gamma = 424782.0/974569.0;
        double[][] a = new double[5][5];
        double[] b = new double[5];

        double[][] ahat = new double[5][5];
        double[] bhat = new double[5];

        int order = 3;

        a[1][0] = gamma;
        a[1][1] = gamma;

        a[2][0] = gamma;
        a[2][1] = -31733082319927313.0/455705377221960889379854647102.0;
        a[2][2] = gamma;

        a[3][0] = -3012378541084922027361996761794919360516301377809610.0/45123394056585269977907753045030512597955897345819349.0;
        a[3][1] = -62865589297807153294268.0/102559673441610672305587327019095047.0;
        a[3][2] = 418769796920855299603146267001414900945214277000.0/212454360385257708555954598099874818603217167139.0;
        a[3][3] = gamma;

        a[4][0] = b1;
        a[4][1] = 0.0;
        a[4][2] = b3;
        a[4][3] = b4;
        a[4][4] = gamma;


        b[0] = b1;
        b[1] = 0.0;
        b[2] = b3;
        b[3] = b4;
        b[4] = gamma;

        ahat[1][0] = 2.0*gamma;

        ahat[2][0] = gamma;
        ahat[2][1] = gamma;

        ahat[3][0] = -475883375220285986033264.0/594112726933437845704163.0;
        ahat[3][1] = 0.0;
        ahat[3][2] = 1866233449822026827708736.0/594112726933437845704163.0;

        ahat[4][0] = 62828845818073169585635881686091391737610308247.0/176112910684412105319781630311686343715753056000.0;
        ahat[4][1] = -b3;
        ahat[4][2] = 262315887293043739337088563996093207.0/297427554730376353252081786906492000.0;
        ahat[4][3] = -987618231894176581438124717087.0/23877337660202969319526901856000.0;

        bhat[0] = b1;
        bhat[1] = 0.0;
        bhat[2] = b3;
        bhat[3] = b4;
        bhat[4] = gamma;

        ERKButcherTableau explicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(ahat), Float64Vector.valueOf(bhat), null, true, 3, new DefaultInterpolant(), "explicit");
        ERKButcherTableau implicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), null, true, 3, new DefaultInterpolant(),"implicit");

        return new IMEXESDIRKButcherTableau(explicitTableau, implicitTableau, order, new DefaultInterpolant(), "Implicit-explicit Runge-Kutta, uniformly accurate, order 3, tableau version 1");
    }

    /**
     * Implicit-explicit Runge-Kutta, uniformly accurate, order 3, tableau
     * version 2.
     * <p>
     * Sebastiano Boscarino. "On an accurate third order implicit-explicit Runge–Kutta method for stiff problems", Applied Numerical Mathematics, vol 59, pg 1515-1528, 2009.
     *
     * @return The Butcher tableau for BHR (5,5,3), tableau version 2.
     */
    public static IMEXESDIRKButcherTableau get_BHR5532_tableau() {
        double b1 = -2032971420760927701493589.0/38017147656515384190997416.0;
        double b2 = 0.0;
        double b3 = 2197602776651676983265261109643897073447.0/945067123279139583549933947379097184164.0;
        double b4 = -128147215194260398070666826235339.0/69468482710687503388562952626424.0;
        double gamma = 2051948.0/3582211.0;
        double[][] a = new double[5][5];
        double[] b = new double[5];
    
        double[][] ahat = new double[5][5];
        double[] bhat = new double[5];
    
        int order = 3;
    
        a[1][0] = gamma;
        a[1][1] = gamma;
    
        a[2][0] =  259252258169672523902708425780469319755.0/4392887760843243968922388674191715336228.0;
        a[2][1] = -172074174703261986564706189586177.0/1226306256343706154920072735579148.0;
        a[2][2] = gamma;
    
        a[3][0] = 1103202061574553405285863729195740268785131739395559693754.0/9879457735937277070641522414590493459028264677925767305837.0;
        a[3][1] = -103754520567058969566542556296087324094.0/459050363888246734833121482275319954529.0;
        a[3][2] = 3863207083069979654596872190377240608602701071947128.0/19258690251287609765240683320611425745736762681950551.0;
        a[3][3] = gamma;
    
        a[4][0] = b1;
        a[4][1] = 0.0;
        a[4][2] = b3;
        a[4][3] = b4;
        a[4][4] = gamma;
    
        b[0] = b1;
        b[1] = 0.0;
        b[2] = b3;
        b[3] = b4;
        b[4] = gamma;
    
        ahat[1][0] = 2.0*gamma;
    
        ahat[2][0] = 473447115440655855452482357894373.0/1226306256343706154920072735579148.0;
        ahat[2][1] = 129298766034131882323069978722019.0/1226306256343706154920072735579148.0;
    
        ahat[3][0] = 37498105210828143724516848.0/172642583546398006173766007.0;
        ahat[3][1] = 0.0;
        ahat[3][2] = 76283359742561480140804416.0/172642583546398006173766007.0;
    
        ahat[4][0] = -3409975860212064612303539855622639333030782744869519.0/5886704102363745137792385361113084313351870216475136.0;
        ahat[4][1] = -237416352433826978856941795734073.0/554681702576878342891447163499456.0;
        ahat[4][2] = 4298159710546228783638212411650783228275.0/2165398513352098924587211488610407046208.0;
        ahat[4][3] = 6101865615855760853571922289749.0/272863973025878249803640374568448.0;
    
        bhat[0] = b1;
        bhat[1] = 0.0;
        bhat[2] = b3;
        bhat[3] = b4;
        bhat[4] = gamma;
    
        ERKButcherTableau explicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(ahat), Float64Vector.valueOf(bhat), null, true, 3, new DefaultInterpolant(), "explicit");
        ERKButcherTableau implicitTableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), null, true, 3, new DefaultInterpolant(),"implicit");
        return new IMEXESDIRKButcherTableau(explicitTableau, implicitTableau, order, new DefaultInterpolant(), "Implicit-explicit Runge-Kutta, uniformly accurate, order 3, tableau version 2");
    }
}
