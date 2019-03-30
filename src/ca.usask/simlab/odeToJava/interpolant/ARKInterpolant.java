/* ./interpolant/ARKInterpolant.java
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
import org.jscience.mathematics.vectors.Float64Matrix;
import org.jscience.mathematics.vectors.Float64Vector;

/**
 * An interpolant for a 2-additive Runge-Kutta method when it can be represented
 * by two standard Runge-Kutta interpolants.
 * <p>
 * Christopher A. Kennedy, Mark H. Carpenter. "Additive Runge-Kutta schemes for convection-diffusion-reaction equations.". Applied numerical mathematics, vol 44, pg 139-181, 2003.
 *
 */
public class ARKInterpolant implements Interpolant {
    protected Interpolant interpolant_one;
    protected Interpolant interpolant_two;

    /**
     * Constructor to initialize the coefficients.
     * <p>
     * The number of rows are equal to the number of stages and number the
     * columns equal to the order of the interpolating polynomial.
     *
     * @param coefficients_one The coefficients for the interpolant for the first Runge-Kutta method.
     * @param coefficients_two The coefficients for the interpolant for the second Runge-Kutta method.
     */
    public ARKInterpolant(double[][] coefficients_one, double[][] coefficients_two) {
        this(Float64Matrix.valueOf(coefficients_one), Float64Matrix.valueOf(coefficients_two));
    }

    /**
     * Constructor to initialize the coefficients.
     * <p>
     * The number of rows are equal to the number of stages and number the
     * columns equal to the order of the interpolating polynomial.
     *
     * @param coefficients_one The coefficients for the interpolant for the first Runge-Kutta method.
     * @param coefficients_two The coefficients for the interpolant for the second Runge-Kutta method.
     */
    public ARKInterpolant(Float64Matrix coefficients_one, Float64Matrix coefficients_two) {
        interpolant_one = new RKInterpolant(coefficients_one);
        interpolant_two = new RKInterpolant(coefficients_two);
    }

    /**
     * Constructor to initialize the coefficients.
     * <p>
     * The number of rows are equal to the number of stages and number the
     * columns equal to the order of the interpolating polynomial.
     *
     * @param interpolant_one The interpolant to use for the first component RK method.
     * @param interpolant_two The interpolant to use for the second component RK method.
     */
    public ARKInterpolant(Interpolant interpolant_one, Interpolant interpolant_two) {
        this.interpolant_one = interpolant_one;
        this.interpolant_two = interpolant_two;
    }

    /**
     * Evaluates the interpolant.
     *
     * @param y0           The solution at the beginning of the step.
     * @param y1           The solution at the end of the step.
     * @param theta        The fractional distance within the current step to find the dense output.
     * @param dt           The current stepsize.
     * @param stage_values The stage values for this step when applicable.
     *
     * @return            The dense output value at the fraction theta within the stepsize.
     */
    public Float64Vector evaluate_interpolant(Float64Vector y0, Float64Vector y1, Float64 theta, Float64 dt, Object stage_values) {
        Float64Vector[][] stage_values_array = (Float64Vector[][]) stage_values;
        Float64Vector result1 = interpolant_two.evaluate_interpolant(y0, y1, theta, dt, stage_values_array[0]);
        Float64Vector result2 = interpolant_one.evaluate_interpolant(y0, y1, theta, dt, stage_values_array[1]);
        return result1.plus(result2);
    }

    /**
     * Get the interpolant for the KC43 method.
     * <p>
     * Christopher A. Kennedy, Mark H. Carpenter. "Additive Runge-Kutta schemes for convection-diffusion-reaction equations.". Applied numerical mathematics, vol 44, pg 139-181, 2003.
     *
     * @return The interpolant for the KC43 method.
     */
    public static ARKInterpolant get_KC43_interpolant() {
        double[][] implicit_coeffs = new double[][] {
            { 6943876665148.0 / 7220017795957.0, -54480133.0 / 30881146.0, 6818779379841.0 / 7100303317025.0 },
            { 0, 0, 0 },
            { 7640104374378.0 / 9702883013639.0, -11436875.0 / 14766696.0, 2173542590792.0 / 12501825683035.0 },
            { -20649996744609.0 / 7521556579894.0, 174696575.0 / 18121608.0, -31592104683404.0 / 5083833661969.0 },
            { 8854892464581.0 / 2390941311638.0, -12120380.0 / 966161.0, 61146701046299.0 / 7138195549469.0 },
            { -11397109935349.0 / 6675773540249.0, 3843.0 / 706.0, -17219254887155.0 / 4939391667607.0 }};
        double[][] explicit_coeffs = new double[][] {
            { 6943876665148.0 / 7220017795957.0, -54480133.0 / 30881146.0, 6818779379841.0 / 7100303317025.0 },
            { 0, 0, 0 },
            { 7640104374378.0 / 9702883013639.0, -11436875.0 / 14766696.0, 2173542590792.0 / 12501825683035.0 },
            { -20649996744609.0 / 7521556579894.0, 174696575.0 / 18121608.0, -31592104683404.0 / 5083833661969.0 },
            { 8854892464581.0 / 2390941311638.0, -12120380.0 / 966161.0, 61146701046299.0 / 7138195549469.0 },
            { -11397109935349.0 / 6675773540249.0, 3843.0 / 706.0, -17219254887155.0 / 4939391667607.0 }};
        return new ARKInterpolant(implicit_coeffs, explicit_coeffs);
    }

    /**
     * Get the interpolant for the KC54 method.
     * <p>
     * Christopher A. Kennedy, Mark H. Carpenter. "Additive Runge-Kutta schemes for convection-diffusion-reaction equations.". Applied numerical mathematics, vol 44, pg 139-181, 2003.
     *
     *
     * @return The interpolant for the KC54 method.
     */
    public static ARKInterpolant get_KC54_interpolant() {
        double[][] implicit_coeffs = new double[][] {
            { -17674230611817.0 / 10670229744614.0, 43486358583215.0 / 12773830924787.0, -9257016797708.0 / 5021505065439.0 },
            { 0, 0, 0 },
            { 0, 0, 0 },
            { 65168852399939.0 / 7868540260826.0, -91478233927265.0 / 11067650958493.0, 26096422576131.0 / 11239449250142.0 },
            { 15494834004392.0 / 5936557850923.0, -79368583304911.0 / 10890268929626.0, 92396832856987.0 / 20362823103730.0 },
            { -99329723586156.0 / 26959484932159.0, -12239297817655.0 / 9152339842473.0, 30029262896817.0 / 10175596800299.0 },
            { -19024464361622.0 / 5461577185407.0, 115839755401235.0 / 10719374521269.0, -26136350496073.0 / 3983972220547.0 },
            { -6511271360970.0 / 6095937251113.0, 5843115559534.0 / 2180450260947.0, -5289405421727.0 / 3760307252460.0 }};
        double[][] explicit_coeffs = new double[][] {
            { -17674230611817.0 / 10670229744614.0, 43486358583215.0 / 12773830924787.0, -9257016797708.0 / 5021505065439.0 },
            { 0, 0, 0 },
            { 0, 0, 0 },
            { 65168852399939.0 / 7868540260826.0, -91478233927265.0 / 11067650958493.0, 26096422576131.0 / 11239449250142.0 },
            { 15494834004392.0 / 5936557850923.0, -79368583304911.0 / 10890268929626.0, 92396832856987.0 / 20362823103730.0 },
            { -99329723586156.0 / 26959484932159.0, -12239297817655.0 / 9152339842473.0, 30029262896817.0 / 10175596800299.0 },
            { -19024464361622.0 / 5461577185407.0, 115839755401235.0 / 10719374521269.0, -26136350496073.0 / 3983972220547.0 },
            { -6511271360970.0 / 6095937251113.0, 5843115559534.0 / 2180450260947.0, -5289405421727.0 / 3760307252460.0 }};
        return new ARKInterpolant(implicit_coeffs, explicit_coeffs);
    }
}


