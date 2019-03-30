/* ./scheme/ERKButcherTableau.java
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
import ca.usask.simlab.odeToJava.interpolant.DormandPrinceInterpolant;
import ca.usask.simlab.odeToJava.interpolant.DefaultInterpolant;
import ca.usask.simlab.odeToJava.interpolant.Interpolant;
import ca.usask.simlab.odeToJava.interpolant.RKInterpolant;

/**
 * This class holds information about a single explicit Runge-Kutta scheme in the
 * form of a Butcher tableau.
 * <p>
 * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 135, pg 166.
 */
public class ERKButcherTableau extends Scheme {
    /**
     * The order of the main method.
     */
    protected int order; 

    /**
     * The order of the embedded method.
     */
    protected int order_embedded;

    /**
     * The order of the interpolant.
     */
    protected int order_interpolant;

    /**
     * The A matrix of the intermediate stages in the Butcher tableau.
     */
    protected Float64Matrix A;

    /**
     * The b vector of quadrature weights in the Butcher tableau.
     */
    protected Float64Vector b;

    /**
     * The c vector of the stages in the Butcher Tableau.
     */
    protected Float64Vector c;

    /**
     * The b vector of the embedded quadrature weights in the Butcher tableau.
     */
    protected Float64Vector b_embedded;

    /**
     * The interpolant for this tableau used for dense output.
     */
    protected Interpolant interpolant;

    /**
     * If this tableau has the First-Same-As-Last (FSAL) property.
     */
    protected boolean is_FSAL;

    /**
     * Creates a new Butcher tableau with no embedded method.
     *
     * @param A           The A matrix of the Butcher tableau representing the
     *                    stages.
     * @param b           The b vector of the Butcher tableau representing the
     *                    quadrature weights.
     * @param c           The c vector of the butcher tableau representing the
     *                    evaluation times of the stages.
     * @param order       The order of convergence for the main method.
     * @param interpolant An interpolant for this method. 
     * @param name The name of the method.
     */
    public ERKButcherTableau(Float64Matrix A, Float64Vector b, Float64Vector c, boolean fsal, int order, Interpolant interpolant, String name) {
        this.A = A;
        this.b = b;
        this.c = c == null ? generate_c() : c;
        this.order = order;
        b_embedded = null;
        order_embedded = 0;
        this.interpolant = interpolant;
        this.name = name;
        is_FSAL = false;
    }

    /**
     * Creates a new Butcher tableau with the embedded method.
     *
     * @param A              The A matrix of the Butcher tableau representing the
     *                       stages.
     * @param b              The b vector of the Butcher tableau representing the
     *                       quadrature weights.
     * @param b_embedded     The b vector of the embedded butcher tableau
     *                       representing the quadrature weights of the embedded
     *                       method.
     * @param c              The c vector of the butcher tableau representing the
     *                       evaluation times of the stages.
     * @param order          The order of convergence for the main method.
     * @param order_embedded The order of convergence for the embedded method.
     * @param interpolant    An interpolant for this method.
     * @param name The name of the method.
     */
    public ERKButcherTableau(Float64Matrix A, Float64Vector b, Float64Vector b_embedded, Float64Vector c, boolean fsal, int order, int order_embedded, Interpolant interpolant, String name) {
        this.A = A;
        this.b = b;
        if (b_embedded != null) {
            this.b_embedded = b_embedded;
        }
        this.c = c == null ? generate_c() : c;
        this.order = order;
        this.order_embedded = order_embedded;
        this.interpolant = interpolant;
        this.name = name;
        is_FSAL = fsal;
    }

    /**
     * Generate the c vector in the Butcher tableau.
     *
     * @return A vector that contains the c vector.
     */
    private Float64Vector generate_c() {
        c = A.getRow(0);
        for (int i = 1; i < A.getNumberOfRows(); i++) {
            c = c.plus(A.getRow(0));
        }
        return c;
    }

    /**
     * Get the A matrix of this Butcher tableau.
     *
     * @return The A matrix of this Butcher tableau.
     */
    public Float64Matrix get_A() {
        return A;
    }

    /**
     * Get the b vector of this Butcher tableau.
     *
     * @return The b vector of this Butcher tableau.
     */
    public Float64Vector get_b() {
        return b;
    }

    /**
     * Get the b vector of the embedded method in this Butcher tableau.
     *
     * @return The b vector of the embedded method, or null if there is
     *         no embedded method
     */
    public Float64Vector get_bemb() {
        return b_embedded;
    }

    /**
     * Get the c vector of this Butcher tableau.
     *
     * @return The c vector of the Butcher tableau.
     */
    public Float64Vector get_c() {
        return c;
    }

    /**
     * Determine the number of stages of this Butcher Tableau.
     *
     * @return The number of stages of this Butcher Tableau.
     */
    public int get_number_stages() {
        return b.getDimension();
    }

    @Override
    public boolean is_single() {
        return true;
    }

    @Override
    public int get_order() {
        return get_scheme_order();
    }

    @Override
    public boolean is_additive() {
        return false;
    }

    @Override
    public boolean has_emb() {
        return b_embedded != null;
    }

    @Override
    public int get_scheme_order() {
        return order;
    }

    @Override
    public int get_emb_order() {
        return order_embedded;
    }

    @Override
    public boolean is_FSAL() {
        return is_FSAL;
    }

    @Override
    public int get_order_interpolant() {
        return order_interpolant;
    }

    @Override
    public Interpolant get_interpolant() {
        return interpolant;
    }

    /**
     * Forward Euler integration, order 1.
     * <p>
     * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 132.
     *
     * @return The tableau for explicit Euler.
     */
    public static ERKButcherTableau get_explicitEulerTableau() {
        double[][] a = new double[1][1];
        double[] b = new double[1];
        double[] c = new double[1];
        int order = 1;

        b[0] = 1;

        ERKButcherTableau tableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), Float64Vector.valueOf(c), false, order, new DefaultInterpolant(), "Forward Euler");
        return tableau;
    }

    /**
     * Explicit Runge-Kutta, Runge, order 2.
     * <p>
     * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 135.
     *
     * @return The tableau for Runge, order 2.
     */
    public static ERKButcherTableau get_Runge2_tableau() {
        double[][] a = new double[2][2];
        double[] b = new double[2];
        double[] c = new double[2];
        int order = 2;

        a[1][0] = 1.0/2.0;

        b[0] = 0.0;
        b[1] = 1.0;

        ERKButcherTableau tableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), Float64Vector.valueOf(c), false, order, new DefaultInterpolant(), "Runge, order 2");
        return tableau;
    }

    /**
     * Explicit Runge-Kutta, Heun's method, order 2.
     * <p>
     * John C. Butcher. "The numerical analysis of ordinary differential equations", pg 173.
     *
     * @return The tableau for Heun's method, order 2.
     */
    public static ERKButcherTableau get_Heun2_tableau() {
        double[][] a = new double[2][2];
        double[] b = new double[2];
        double[] c = new double[2];
        int order = 2;

        a[1][0] = 1.0;


        b[0] = 1.0/2.0;
        b[1] = 1.0/2.0;

        ERKButcherTableau tableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), Float64Vector.valueOf(c), false, order, new DefaultInterpolant(), "Heun's method, order 2");
        return tableau;
    }

    /**
     * Explicit Runge-Kutta, strong stability-preserving, order 2.
     * <p>
     * Raymond J. Spiteri and Steven J. Ruuth. "A new class of optimal high-order strong-stability-preserving time discretization methods", SIAM Journal of Numerical Analysis, vol 40, pg 469-491, 2002.
     *
     * @return The tableau for the strong stability-preserving method, order 2.
     */
    public static ERKButcherTableau get_SSP22_tableau() {
        double[][] a = new double[2][2];
        double[] b = new double[2];
        double[] c = new double[2];
        int order = 2;

        a[1][0] = 1.0;

        b[0] = 1.0/2.0;
        b[1] = 1.0/2.0;

        ERKButcherTableau tableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), Float64Vector.valueOf(c), false, order, new DefaultInterpolant(), "Strong stability-preserving, order 2");
        return tableau;
    }

    /**
     * Explicit Runge-Kutta, extended stability region Runge-Kutta, order 2.
     * <p>
     * P.J. van der Houwen. "Explicit Runge-Kutta Formulas with Increased Stability Boundaries ", Numerical mathematics, vol 20, pg 149-164, 1972.
     *
     * @return The tableau for an extended stability region Runge-Kutta method, order 2.
     */
    public static ERKButcherTableau get_VDH_tableau() {
        double[][] a = new double[5][5];
        double[] b = new double[5];
        double[] c = new double[5];
        int order = 2;

        a[1][0] = 1.0 / 4.0;

        a[2][1] = 1.0 / 6.0;;

        a[3][2] = 3.0 / 8.0;

        a[4][0] = 0.0;
        a[4][0] = 0.0;
        a[4][0] = 0.0;
        a[4][0] = 1.0 / 2.0;

        b[4] = 1.0;

        ERKButcherTableau tableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), Float64Vector.valueOf(c), false, order, new DefaultInterpolant(), "Extended stability region Runge-Kutta, order 2");
        return tableau;
    }

    /**
     * Explicit Runge-Kutta, Runge's method, order 3.
     * <p>
     * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 135.
     *
     * @return The tableau for Runge's method, order 3.
     */
    public static ERKButcherTableau get_Runge3_tableau() {
        double[][] a = new double[4][4];
        double[] b = new double[4];
        double[] c = new double[4];
        int order = 4;

        a[1][0] = 1.0 / 2.0;
        a[2][1] = 1.0 / 2.0;
        a[3][2] = 1.0;

        b[0] = 1.0 / 6.0;
        b[1] = 2.0 / 6.0;
        b[2] = 2.0 / 6.0;
        b[3] = 1.0 / 6.0;

        c[1] = 1.0 / 2.0;
        c[2] = 1.0 / 2.0;
        c[3] = 1.0;

        ERKButcherTableau tableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), Float64Vector.valueOf(c), false, order, new DefaultInterpolant(), "Runge's method, order 3");
        return tableau;
    }

    /**
     * Explicit Runge-Kutta, Heun's method, order 3.
     * <p>
     * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 135.
     *
     * @return The tableau for Heun's method, order 3.
     */
    public static ERKButcherTableau get_Heun3_tableau() {
        double[][] a = new double[3][3];
        double[] b = new double[3];
        double[] c = new double[3];
        int order = 3;

        a[1][0] = 1.0/3.0;

        a[2][0] = 0.0;
        a[2][1] = 2.0/3.0;

        b[0] = 1.0/4.0;
        b[1] = 0.0;
        b[2] = 3.0/4.0;

        ERKButcherTableau tableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), Float64Vector.valueOf(c), false, order, new DefaultInterpolant(), "Heun's method, order 3");
        return tableau;
    }

    /**
     * Explicit Runge-Kutta, strong stability-preserving method, order 3.
     * <p>
     * Raymond J. Spiteri and Steven J. Ruuth. "A new class of optimal high-order strong-stability-preserving time discretization methods", SIAM Journal of Numerical Analysis, vol 40, pg 469-491, 2002.
     *
     * @return The tableau for the strong stability-preserving method, order 3.
     */
    public static ERKButcherTableau get_SSP32_tableau() {
        double[][] a = new double[3][3];
        double[] b = new double[3];
        double[] c = new double[3];
        int order = 3;

        a[1][0] = 1.0 / 2.0;

        a[2][0] = 1.0 / 2.0;
        a[2][1] = 1.0 / 2.0;

        b[0] = 1.0 / 3.0;
        b[1] = 1.0 / 3.0;
        b[2] = 1.0 / 3.0;

        ERKButcherTableau tableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), Float64Vector.valueOf(c), false, order, new DefaultInterpolant(), "Strong stability-preserving method, order 3");
        return tableau;
    }


    /**
     * Explicit Runge-Kutta, "the" Runge-Kutta method, order 4.
     * <p>
     * Originally derived by Kutta near the beginning of the 20th century.
     * <p>
     * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 138.
     *
     * @return "The" Runge-Kutta method as derived by Kutta.
     */
    public static ERKButcherTableau get_RK4_tableau() {
        double[][] a = new double[4][4];
        double[] b = new double[4];
        double[] c = new double[4];
        int order = 4;

        a[1][0] = 1.0 / 2.0;
        a[2][1] = 1.0 / 2.0;
        a[3][2] = 1.0;

        b[0] = 1.0 / 6.0;
        b[1] = 2.0 / 6.0;
        b[2] = 2.0 / 6.0;
        b[3] = 1.0 / 6.0;

        ERKButcherTableau tableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), Float64Vector.valueOf(c), false, order, new DefaultInterpolant(), "\"The\" Runge-Kutta method, order 4");
        return tableau;
    }

    /**
     * Explicit Runge-Kutta, three-eighths rule, order 4.
     * <p>
     * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 138.
     *
     * @return The tableau for the three-eighths rule, order 4.
     */
    public static ERKButcherTableau get_three_eighths_tableau() {
        double[][] a = new double[4][4];
        double[] b = new double[4];
        double[] c = new double[4];
        int order = 4;

        a[1][0] = 1.0 / 3.0;
        a[2][0] = 1.0;
        a[2][1] = -1.0 / 3.0;
        a[3][0] = 1.0;
        a[3][1] = -1.0;
        a[3][2] = 1.0;

        b[0] = 1.0 / 8.0;
        b[1] = 3.0 / 8.0;
        b[2] = 3.0 / 8.0;
        b[3] = 1.0 / 8.0;

        ERKButcherTableau tableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), Float64Vector.valueOf(c), false, order, new DefaultInterpolant(), "Three-eighths rule, order 4");
        return tableau;
    }

    /**
     * Explicit Runge-Kutta, Merson, order 4, embedded order 3.
     * <p>
     * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 167.
     *
     * @return The tableau for Runge-Kutta Merson, order 4(3).
     */
    public static ERKButcherTableau get_RKMerson43_tableau() {
        double[][] a = new double[5][5];
        double[] b = new double[5];
        double[] b_embedded = new double[5];
        double[] c = new double[5];
        int order = 4;
        int order_embedded = 5;

        a[1][0] = 1.0 / 3.0;

        a[2][0] = 1.0 / 6.0;
        a[2][1] = 1.0 / 6.0;

        a[3][0] = 1.0 / 8.0;
        a[3][1] = 0.0;
        a[3][2] = 3.0 / 8.0;

        a[4][0] = 1.0 / 2.0;
        a[4][2] = -3.0 / 2.0;
        a[4][3] = 2.0;

        b[0] = 1.0 / 6.0;
        b[1] = 0.0;
        b[2] = 0.0;
        b[3] = 2.0 / 3.0;
        b[4] = 1.0 / 6.0;

        b_embedded[0] = 1.0 / 10.0;
        b_embedded[1] = 0.0;
        b_embedded[2] = 3.0 / 10.0;
        b_embedded[3] = 2.0 / 5.0;
        b_embedded[4] = 1.0 / 5.0;

        ERKButcherTableau tableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), Float64Vector.valueOf(b_embedded), Float64Vector.valueOf(c), true, order, order_embedded, new DefaultInterpolant(), "Merson, order 4, embedded order 3");
        return tableau;
    }

    /**
     * Explicit Runge-Kutta, Merson, order 4, embedded order 5 (linear).
     * <p>
     * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 167.
     *
     * @return The tableau for Runge-Kutta Merson, order 4 (5 linear).
     */
    public static ERKButcherTableau get_RKMerson45_tableau() {
        double[][] a = new double[5][5];
        double[] b = new double[5];
        double[] b_embedded = new double[5];
        double[] c = new double[5];
        int order = 4;
        int order_embedded = 5;

        a[1][0] = 1.0 / 3.0;

        a[2][0] = 1.0 / 6.0;
        a[2][1] = 1.0 / 6.0;

        a[3][0] = 1.0 / 8.0;
        a[3][1] = 0.0;
        a[3][2] = 3.0 / 8.0;

        a[4][0] = 1.0 / 2.0;
        a[4][2] = -3.0 / 2.0;
        a[4][3] = 2.0;

        b[0] = 1.0 / 6.0;
        b[1] = 0.0;
        b[2] = 0.0;
        b[3] = 2.0 / 3.0;
        b[4] = 1.0 / 6.0;

        b_embedded[0] = 1.0 / 10.0;
        b_embedded[1] = 0.0;
        b_embedded[2] = 3.0 / 10.0;
        b_embedded[3] = 2.0 / 5.0;
        b_embedded[4] = 1.0 / 5.0;

        ERKButcherTableau tableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), Float64Vector.valueOf(b_embedded), Float64Vector.valueOf(c), true, order, order_embedded, new DefaultInterpolant(), "Merson, order 4, embedded order 5 (linear)");
        return tableau;
    }

    /**
     * Explicit Runge-Kutta, Zonneveld, order 4, embedded order 3.
     * <p>
     * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 167.
     *
     * @return The tableau for Runge-Kutta Zonneveld, order 4(3).
     */
    public static ERKButcherTableau get_RKZonneveld43_tableau() {
        double[][] a = new double[5][5];
        double[] b = new double[5];
        double[] b_embedded = new double[5];
        double[] c = new double[5];
        int order = 4;
        int order_embedded = 3;

        a[1][0] = 1.0 / 2.0;

        a[2][0] = 0.0;
        a[2][1] = 1.0 / 2.0;

        a[3][0] = 0.0;
        a[3][1] = 0.0;
        a[3][2] = 1.0;

        a[4][0] = 5.0 / 32.0;
        a[4][1] = 7.0 / 32.0;
        a[4][2] = 13.0 /32.0;
        a[4][3] = -1.0/32.0;

        b[0] = 1.0 / 6.0;
        b[1] = 1.0 / 3.0;
        b[2] = 1.0 / 3.0;
        b[3] = 1.0 / 6.0;

        b_embedded[0] = -1.0 / 2.0;
        b_embedded[1] = 7.0 / 3.0;
        b_embedded[2] = 7.0 / 3.0;
        b_embedded[3] = 13.0 / 6.0;
        b_embedded[4] = -16.0 / 3.0;

        ERKButcherTableau tableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), Float64Vector.valueOf(b_embedded), Float64Vector.valueOf(c), true, order, order_embedded, new DefaultInterpolant(), "Zonneveld, order 4, embedded order 3");
        return tableau;
    }

    /**
     * Explicit Runge-Kutta-Fehlberg, order 4, embedded order 5.
     * <p>
     * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 177.
     *
     * @return The tableau for Runge-Kutta-Fehlberg, order 4(5).
     */
    public static ERKButcherTableau get_RKF45_tableau() {
        double[][] a = new double[6][6];
        double[] b = new double[6];
        double[] b_embedded = new double[6];
        double[] c = new double[6];
        int order = 4;
        int order_embedded = 5;

        a[1][0] = 1.0 / 4.0;

        a[2][0] = 3.0 / 32.0;
        a[2][1] = 9.0 / 32.0;

        a[3][0] = 1932.0 / 2197.0;
        a[3][1] = -7200.0 / 2197.0;
        a[3][2] = 7296.0  / 2197.0;

        a[4][0] = 439.0 / 216.0;
        a[4][1] = -8.0;
        a[4][2] = 3680.0 / 513;
        a[4][3] = -845.0 / 4104.0;

        a[5][0] = -8.0 / 27.0;
        a[5][1] = 2.0;
        a[5][2] = -3544.0 / 2565.0;
        a[5][3] = 1859.0 / 4104.0;
        a[5][4] = -11.0 / 40.0;

        b[0] = 25.0 / 216.0;
        b[1] = 0.0;
        b[2] = 1408.0 / 2565.0;
        b[3] = 2197.0 / 4104.0;
        b[4] = -1.0 / 5.0;
        b[5] = 0.0;

        b_embedded[0] = 16.0 / 135.0;
        b_embedded[1] = 0.0;
        b_embedded[2] = 6656.0 / 12825.0;
        b_embedded[3] = 28561.0 / 56430.0;
        b_embedded[4] = -9.0 / 50.0;
        b_embedded[5] = 2.0 / 55.0;

        ERKButcherTableau tableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), Float64Vector.valueOf(b_embedded), Float64Vector.valueOf(c), true, order, order_embedded, new DefaultInterpolant(), "Runge-Kutta-Fehlberg, order 4, embedded order 5");
        return tableau;
    }

    /**
     * Explicit Runge-Kutta, Dormand-Prince, order 5, embedded order 4.
     * <p>
     * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 178.
     *
     * @return The tableau for Dormand-Prince, order 5(4).
     */
    public static ERKButcherTableau get_DormandPrince54_tableau() {
        double[][] a = new double[7][7];
        double[] b = new double[7];
        double[] b_embedded = new double[7];
        double[] c = new double[7];
        int order = 5;
        int order_embedded = 4;

        a[1][0] = 1.0 / 5.0;// assignment

        a[2][0] = 3.0 / 40.0;
        a[2][1] = 9.0 / 40.0;

        a[3][0] = 44.0 / 45.0;
        a[3][1] = -56.0 / 15.0;
        a[3][2] = 32.0 / 9.0;

        a[4][0] = 19372.0 / 6561.0;
        a[4][1] = -25360.0 / 2187.0;
        a[4][2] = 64448.0 / 6561.0;
        a[4][3] = -212.0 / 729.0;

        a[5][0] = 9017.0 / 3168.0;
        a[5][1] = -355.0 / 33.0;
        a[5][2] = 46732.0 / 5247.0;
        a[5][3] = 49.0 / 176.0;
        a[5][4] = -5103.0 / 18656.0;

        a[6][0] = 35.0 / 384.0;
        a[6][1] = 0.0;
        a[6][2] = 500.0 / 1113.0;
        a[6][3] = 125.0 / 192.0;
        a[6][4] = -2187.0 / 6784.0;
        a[6][5] = 11.0 / 84.0;

        b[0] = 35.0 / 384.0;
        b[1] = 0.0;
        b[2] = 500.0 / 1113.0;
        b[3] = 125.0 / 192.0;
        b[4] = -2187.0 / 6784.0;
        b[5] = 11.0 / 84.0;
        b[6] = 0.0;

        b_embedded[0] = 5179.0 / 57600.0;
        b_embedded[1] = 0.0;
        b_embedded[2] = 7571.0 / 16695.0;
        b_embedded[3] = 393.0 / 640.0;
        b_embedded[4] = -92097.0 / 339200.0;
        b_embedded[5] = 187.0 / 2100.0;
        b_embedded[6] = 1.0 / 40.0;

        c[0] = 0.0;
        c[1] = 1.0 / 5.0;
        c[2] = 3.0 / 10.0;
        c[3] = 4.0 / 5.0;
        c[4] = 8.0 / 9.0;
        c[5] = 1.0;
        c[6] = 1.0;

        ERKButcherTableau tableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), Float64Vector.valueOf(b_embedded), Float64Vector.valueOf(c), true, order, order_embedded, new DormandPrinceInterpolant(), "Dormand-Prince, order 5, embedded order 4");
        return tableau;
    }

    /**
     * Explicit Runge-Kutta, Verner, order 6, embedded order 5.
     * <p>
     * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 180.
     *
     * @return The tableau for Verner, order 6(5).
     */
    public static ERKButcherTableau get_Verner65_tableau() {
        double[][] a = new double[8][8];
        double[] b = new double[8];
        double[] b_embedded = new double[8];
        double[] c = new double[8];
        int order = 6;
        int order_embedded = 5;

        a[1][0] = 1.0 / 6.0;

        a[2][0] = 4.0/75.0;
        a[2][1] = 16.0/75.0;

        a[3][0] = 5.0/6.0;
        a[3][1] = -8.0/3.0;
        a[3][2] = 5.0/2.0;

        a[4][0] = -165.0/64.0;
        a[4][1] = 55.0/6.0;
        a[4][2] = -425.0/64.0;
        a[4][3] = 85.0/96.0;

        a[5][0] = 12.0/5.0;
        a[5][1] = -8.0;
        a[5][2] = 4015.0/612.0;
        a[5][3] = -11.0/36.0;
        a[5][4] = 88.0/255.0;

        a[6][0] = -8263.0/15000.0;
        a[6][1] = 124.0/75.0;
        a[6][2] = -643.0/680.0;
        a[6][3] = -81.0/250.0;
        a[6][4] = 2484.0/10625.0;
        a[6][5] = 0.0;

        a[7][0] = 3501.0/1720.0;
        a[7][1] = -300.0/43.0;
        a[7][2] = 297275.0/52632.0;
        a[7][3] = -319.0/2322.0;
        a[7][4] = 24068.0/84065.0;
        a[7][5] = 0.0;
        a[7][6] = 3850.0/26703.0;

        b[0] = 3.0/40.0;
        b[1] = 0.0;
        b[2] = 875.0/2244.0;
        b[3] = 23.0/72.0;
        b[4] = 264.0/1955.0;
        b[5] = 0.0;
        b[6] = 125.0/11592.0;
        b[7] = 43.0/616.0;

        b_embedded[0] = 13.0/160.0;
        b_embedded[1] = 0.0;
        b_embedded[2] = 2375.0/5984.0;
        b_embedded[3] = 5.0/16.0;
        b_embedded[4] = 12.0/85.0;
        b_embedded[5] = 3.0/44.0;
        b_embedded[6] = 0.0;
        b_embedded[7] = 0.0;

        ERKButcherTableau tableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), Float64Vector.valueOf(b_embedded), Float64Vector.valueOf(c), true, order, order_embedded, new DefaultInterpolant(), "Verner, order 6, embedded order 5");
        return tableau;
    }

    /**
     * Explicit Runge-Kutta-Fehlberg, order 7, embedded order 8.
     * <p>
     * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 180.
     *
     * @return The tableau for Runge-Kutta-Fehlberg, order 7(8).
     */
    public static ERKButcherTableau get_RKF78_tableau() {
        double[][] a = new double[13][13];
        double[] b = new double[13];
        double[] b_embedded = new double[13];
        double[] c = new double[13];
        int order = 7;
        int order_embedded = 8;

        a[1][0] = 2.0 / 27.0;

        a[2][0] = 1.0 / 36.0;
        a[2][1] = 1.0 / 12.0;

        a[3][0] = 1.0 / 24.0;
        a[3][1] = 0.0;
        a[3][2] = 1.0/8.0;

        a[4][0] = 5.0/12.0;
        a[4][1] = 0.0;
        a[4][2] = -25.0/16.0;
        a[4][3] = 25.0/16.0;

        a[5][0] = 1.0/20.0;
        a[5][1] = 0.0;
        a[5][2] = 0.0;
        a[5][3] = 1.0/4.0;
        a[5][4] = 1.0/5.0;

        a[6][0] = -25.0/108.0;
        a[6][1] = 0.0;
        a[6][2] = 0.0;
        a[6][3] = 125.0/108.0;
        a[6][4] = -65.0/27.0;
        a[6][5] = 125.0/54.0;

        a[7][0] = 31.0/300.0;
        a[7][1] = 0.0;
        a[7][2] = 0.0;
        a[7][3] = 0.0;
        a[7][4] = 61.0/225.0;
        a[7][5] = -2.0/9.0;
        a[7][6] = 13.0/900;

        a[8][0] = 2.0;
        a[8][1] = 0.0;
        a[8][2] = 0.0;
        a[8][3] = -53.0/6.0;
        a[8][4] = 704.0/45.0;
        a[8][5] = -107.0/9.0;
        a[8][6] = 67.0/90.0;
        a[8][7] = 3.0;

        a[9][0] = -91.0/108.0;
        a[9][1] = 0.0;
        a[9][2] = 0.0;
        a[9][3] = 23.0/108.0;
        a[9][4] = -976.0/135.0;
        a[9][5] = 311.0/54.0;
        a[9][6] = -19.0/60.0;
        a[9][7] = 17.0/6.0;
        a[9][8] = -1.0/12.0;

        a[10][0] = 2383.0/4100.0;
        a[10][1] = 0.0;
        a[10][2] = 0.0;
        a[10][3] = -341.0/164.0;
        a[10][4] = 4496.0/1025.0;
        a[10][5] = -301.0/82.0;
        a[10][6] = 2133.0/4100.0;
        a[10][7] = 45.0/82.0;
        a[10][8] = 45.0/164.0;
        a[10][9] = 18.0/41.0;

        a[11][0] = 3.0/205.0;
        a[11][1] = 0.0;
        a[11][2] = 0.0;
        a[11][3] = 0.0;
        a[11][4] = 0.0;
        a[11][5] = -6.0/41.0;
        a[11][6] = -3.0/205.0;
        a[11][7] = -3.0/41.0;
        a[11][8] = 3.0/41.0;
        a[11][9] = 6.0/41.0;
        a[11][10] = 0.0;

        a[12][0] = -1777.0/4100.0;
        a[12][1] = 0.0;
        a[12][2] = 0.0;
        a[12][3] = -341.0/164;
        a[12][4] = 4496.0/1025.0;
        a[12][5] = -289.0/82.0;
        a[12][6] = 2193.0/4100.0;
        a[12][7] = 51.0/82.0;
        a[12][8] = 33.0/164.0;
        a[12][9] = 12.0/41.0;
        a[12][10] = 0.0;
        a[12][11] = 1.0;

        b[0] = 41.0/840.0;
        b[1] = 0.0;
        b[2] = 0.0;
        b[3] = 0.0;
        b[4] = 0.0;
        b[5] = 34.0/105.0;
        b[6] = 9.0/35.0;
        b[7] = 9.0/35.0;
        b[8] = 9.0/280.0;
        b[9] = 9.0/280.0;
        b[10] = 41.0/840.0;
        b[11] = 0.0;
        b[12] = 0.0;

        b_embedded[0] = 0.0;
        b_embedded[1] = 0.0;
        b_embedded[2] = 0.0;
        b_embedded[3] = 0.0;
        b_embedded[4] = 0.0;
        b_embedded[5] = 34.0/105.0;
        b_embedded[6] = 9.0/35.0;
        b_embedded[7] = 9.0/35.0;
        b_embedded[8] = 9.0/280.0;
        b_embedded[9] = 9.0/280.0;
        b_embedded[10] = 0.0;
        b_embedded[11] = 41.0/840.0;
        b_embedded[12] = 41.0/840.0;

        ERKButcherTableau tableau = new ERKButcherTableau(Float64Matrix.valueOf(a), Float64Vector.valueOf(b), Float64Vector.valueOf(b_embedded), Float64Vector.valueOf(c), true, order, order_embedded, new DefaultInterpolant(), "Runge-Kutta-Fehlberg, order 7, embedded order 8");
        return tableau;
    }
}
