/* ./modules/errorControl/BaseErrControllerModule.java
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
package ca.usask.simlab.odeToJava.modules.errorControl;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.ode.RHS;
import ca.usask.simlab.odeToJava.solver.SolverModule;
import ca.usask.simlab.odeToJava.util.Check;
import ca.usask.simlab.odeToJava.util.Matrix;

/**
 * This abstract class implements the basic methods needed for typical stepsize
 * controllers to estimate the next stepsize based on an error estimate.
 * <p>
 * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 167.
 * <p>
 * Josef Stoer and Roland Bulirsch. "Introduction to Numerical analysis", pg 448-454.
 */
public abstract class BaseErrControllerModule extends SolverModule {
    /**
     * The default maximum increase in stepsize.
     */
    public static final Float64 DEFAULT_A_MAX_NORMAL = Float64.valueOf(5.0);

    /**
     * The default maximum decrease in stepsize.
     */
    public static final Float64 DEFAULT_A_MIN = Float64.valueOf(1.0 / 5.0);

    /**
     * The default maximum increase in stepsize if the last step was rejected.
     */
    public static final Float64 DEFAULT_A_MAX_REJECTED = Float64.valueOf(1.0);

    /**
     * The default size of the next step as compared to the calculated size of the next step.
     */
    public static final Float64 DEFAULT_SAFETY = Float64.valueOf(0.85);

    /**
     * The default acceptance factor, generally used for error control via
     * step-doubling error estimation.
     */
    public static final Float64 DEFAULT_THRESHOLD = Float64.valueOf(3.0);

    /**
     * The default alpha, a factor used for predictive and PI error control.
     * <p>
     * Generally overidden by the controller itself based on the order of the
     * method.
     */
    public static final Float64 DEFAULT_ALPHA = Float64.ZERO;

    /**
     * The default beta, a factor used for predictive and PI error control.
     * <p>
     * Generally overidden by the controller itself based on the order of the
     * method.
     */
    public static final Float64 DEFAULT_BETA = Float64.ZERO;

    /**
     * The default absolute tolerance.
     */
    public static final Float64 DEFAULT_ATOL = Float64.valueOf(1E-6);

    /**
     * The default relative tolerance.
     */
    public static final Float64 DEFAULT_RTOL = Float64.valueOf(1E-3);

    private Float64 amax_rejected;
    private boolean current_step_accepted;
    private Float64 next_stepsize;
    private Float64Vector atol;
    private Float64Vector rtol;
    private Float64 safety;
    private Float64 threshold;
    private Float64 alpha;
    private Float64 beta;
    private Float64 amin;
    private Float64 amax_normal;
    private boolean previous_step_accepted;
    private int size;

    /**
     * Constructor that sets up the error control with the default tolerances.
     *
     * @param ode The ODE that is being solved.
     */
    public BaseErrControllerModule(RHS ode) {
        this(BaseErrControllerModule.DEFAULT_ATOL, BaseErrControllerModule.DEFAULT_RTOL, ode);
    }

    /**
     * Constructor that sets up the error control with user-specified uniform tolerances.
     *
     * @param atol The absolute tolerance used for all variables.
     * @param rtol The relative tolerance used for all variables.
     * @param ode The ODE that is being solved.
     */
    public BaseErrControllerModule(double atol, double rtol, RHS ode) {
        this(Float64.valueOf(atol), Float64.valueOf(rtol), ode);
    }

    /**
     * Constructor that sets up the error control with user-specified uniform tolerances.
     *
     * @param atol The absolute tolerance used for all variables.
     * @param rtol The relative tolerance used for all variables.
     * @param ode The ODE that is being solved.
     */
    public BaseErrControllerModule(Float64 atol, Float64 rtol, RHS ode) {
        this(Matrix.fill(atol, ode.get_size()), Matrix.fill(rtol, ode.get_size()), ode);
    }

    /**
     * Constructor that sets up the error control with user-specified component-wise tolerances.
     *
     * @param atol A vector of absolute tolerances.
     * @param rtol A vector of relative tolerances.
     * @param ode The ODE that is being solved.
     */
    public BaseErrControllerModule(double[] atol, double[] rtol, RHS ode) {
        this(Float64Vector.valueOf(atol), Float64Vector.valueOf(rtol), ode);
    }

    /**
     * Constructor that sets up the error control with user-specified component-wise tolerances.
     *
     * @param atol A vector of absolute tolerances.
     * @param rtol A vector of relative tolerances.
     * @param ode The ODE that is being solved.
     */
    public BaseErrControllerModule(Float64Vector atol, Float64Vector rtol, RHS ode) {
        this.atol = atol;
        this.rtol = rtol;
        amax_normal = BaseErrControllerModule.DEFAULT_A_MAX_NORMAL;
        amax_rejected = BaseErrControllerModule.DEFAULT_A_MAX_REJECTED;
        amin = BaseErrControllerModule.DEFAULT_A_MIN;
        safety = BaseErrControllerModule.DEFAULT_SAFETY;
        threshold = BaseErrControllerModule.DEFAULT_THRESHOLD;
        alpha = BaseErrControllerModule.DEFAULT_ALPHA;
        beta = BaseErrControllerModule.DEFAULT_BETA;
        previous_step_accepted = true;
        current_step_accepted = true;
        size = ode.get_size();
        checkTolerances();
    }

    private void checkTolerances() {
        if (!(Check.dimension(atol, size) && Check.dimension(rtol, size))) {
            throw new IllegalArgumentException("tolerance vectors must be the same size as the ODE");
        }
    }

    /**
     * Set uniform absolute tolerances.
     *
     * @param atol The absolute tolerance used for all variables.
     */
    public void set_atol(double atol) {
        set_atol(Float64.valueOf(atol));
    }

    /**
     * Set uniform absolute tolerances.
     *
     * @param atol The absolute tolerance used for all variables.
     */
    public void set_atol(double[] atol) {
        set_atol(Float64Vector.valueOf(atol));
    }

    /**
     * Set uniform absolute tolerances.
     *
     * @param atol The absolute tolerance used for all variables.
     */
    public void set_atol(Float64 atol) {
        set_atol(Matrix.fill(atol, size));
    }

    /**
     * Set component-wise absolute tolerances.
     *
     * @param atol A vector of absolute tolerances used for each component.
     */
    public void set_atol(Float64Vector atol) {
        this.atol = atol;
        checkTolerances();
    }

    /**
     * Return a vector of component-wise absolute tolerances.
     *
     * @return The absolute tolerances as an array of doubles.
     */
    public double[] get_atol_value() {
        return Matrix.toDouble(atol);
    }

    /**
     * Return a vector of component-wise absolute tolerances.
     *
     * @return The absolute tolerances as a vector.
     */
    public Float64Vector get_atol() {
        return atol;
    }

    /**
     * Set uniform relative tolerances.
     *
     * @param rtol The relative tolerances used for all variables.
     */
    public void set_rtol(double rtol) {
        set_rtol(Float64Vector.valueOf(rtol));
    }

    /**
     * Set uniform relative tolerances.
     *
     * @param rtol The relative tolerances used for all variables.
     */
    public void set_rtol(double[] rtol) {
        set_rtol(Float64Vector.valueOf(rtol));
    }

    /**
     * Set uniform relative tolerances.
     *
     * @param rtol The relative tolerances used for all variables.
     */
    public void set_rtol(Float64 rtol) {
        set_rtol(Matrix.fill(rtol, size));
    }

    /**
     * Set component-wise relative tolerances.
     */
    public void set_rtol(Float64Vector rtol) {
        this.rtol = rtol;
        checkTolerances();
    }

    /**
     * Return a vector of relative tolerances.
     *
     * @return The relative tolerances as an array of doubles.
     */
    public double[] get_rtol_value() {
        return Matrix.toDouble(rtol);
    }

    /**
     * Return a vector of relative tolerances.
     *
     * @return The relative tolerances as a vector.
     */
    public Float64Vector get_rtol() {
        return rtol;
    }

    /**
     * Calculate the specific tolerance values for a particular timestep.
     *
     * Based on the relative tolerances, absolute tolerances, and two solution
     * vectors that are typically from the beginning and end of the timestep.
     *
     * @param y1 The first solution vector.
     * @param y2 The second solution vector.
     *
     * @return A vector of the tolerances for the particular timestep.
     */
    protected Float64Vector get_tolerances(Float64Vector y1, Float64Vector y2) {
        Float64Vector maxValues = Matrix.max(Matrix.abs(y1), Matrix.abs(y2));
        return Matrix.times(maxValues, rtol).plus(atol);
    }

    /**
     * Set the next size after it's been calculated by the error controller.
     *
     * @param hnew The new stepsize from the error controller.
     */
    protected void set_next_stepsize(Float64 hnew) {
        next_stepsize = hnew;
    }

    /**
     * Set whether the previous step has been accepted or not.
     *
     * @param step_accepted A boolean value indicating whether the step
     *                     is accepted.
     */
    protected void set_step_accepted(boolean step_accepted) {
        previous_step_accepted = current_step_accepted;
        current_step_accepted = step_accepted;
    }

    /**
     * Check if the last step was accepted.
     *
     * @return Indication whether the last step was accepted.
     */
    public boolean is_step_accepted() {
        return current_step_accepted;
    }

    /**
     * Get the next stepsize.
     *
     * @return next_stepsize
     */
    public Float64 get_next_stepSize() {
        return next_stepsize;
    }

    /**
     * Get the current value of the threshold, the factor to determine
     * whether a step is accepted when using step-doubling error
     * estimation.
     *
     * @return The value of threshold.
     */
    public double get_threshold_value() {
        return get_threshold().doubleValue();
    }

    /**
     * Get the current value of the threshold, the factor to determine
     * whether a step is accepted when using step-doubling error
     * estimation.
     *
     * @return The value of threshold.
     */
    public Float64 get_threshold() {
        return threshold;
    }

    /**
     * Set the current value of the threshold, the factor to determine
     * whether a step is accepted when using step-doubling error
     * estimation.
     *
     * @param threshold The value of threshold to set.
     */
    public void set_threshold(double threshold) {
        set_threshold(Float64.valueOf(threshold));
    }

    /**
     * Set the current value of the threshold, the factor to determine
     * whether a step is accepted when using step-doubling error
     * estimation.
     *
     * @param threshold The value of threshold to set.
     */
    public void set_threshold(Float64 threshold) {
        if (Check.positive(threshold)) {
            this.threshold = threshold;
        } else {
            throw new IllegalArgumentException("Threshold must be positive");
        }
    }

    /**
     * Get the current value of the alpha, a factor used for
     * predictive and PI step control.
     *
     * @return The value of alpha.
     */
    public double get_alpha_value() {
        return get_alpha().doubleValue();
    }

    /**
     * Get the current value of the alpha, a factor used for
     * predictive and PI step control.
     *
     * @return The value of alpha.
     */
    public Float64 get_alpha() {
        return alpha;
    }

    /**
     * Set the current value of the alpha, a factor used for
     * predictive and PI step control.
     *
     * @param alpha The value of alpha to set.
     */
    public void set_alpha(double alpha) {
        set_alpha(Float64.valueOf(alpha));
    }

    /**
     * Set the current value of the alpha, a factor used for
     * predictive and PI step control.
     *
     * @param alpha The value of alpha to set.
     */
    public void set_alpha(Float64 alpha) {
        if (Check.positive(alpha)) {
            this.alpha = alpha;
        } else {
            throw new IllegalArgumentException("alpha must be positive");
        }
    }

    /**
     * Get the current value of the beta, a factor used for
     * predictive and PI step control.
     *
     * @return The value of beta.
     */
    public double get_beta_value() {
        return get_beta().doubleValue();
    }

    /**
     * Get the current value of the beta, a factor used for
     * predictive and PI step control.
     *
     * @return The value of beta.
     */
    public Float64 get_beta() {
        return beta;
    }

    /**
     * Set the current value of the beta, a factor used for
     * predictive and PI step control.
     *
     * @param beta The value of beta to set.
     */
    public void set_beta(double beta) {
        set_beta(Float64.valueOf(beta));
    }

    /**
     * Set the current value of the beta, a factor used for
     * predictive and PI step control.
     *
     * @param beta The value of beta to set.
     */
    public void set_beta(Float64 beta) {
        if (Check.positive(beta)) {
            this.beta = beta;
        } else {
            throw new IllegalArgumentException("beta must be positive");
        }
    }

    /**
     * Get the current value of the safety factor to apply to the
     * calculated optimal stepsize in order to get the actual stepsize.
     *
     * @return The value of safety factor.
     */
    public double get_safety_value() {
        return get_safety().doubleValue();
    }

    /**
     * Get the current value of the safety factor to apply to the
     * calculated optimal stepsize in order to get the actual stepsize.
     *
     * @return The value of safety factor.
     */
    public Float64 get_safety() {
        return safety;
    }

    /**
     * Set the value of the safety factor to apply to the calculated optimal stepsize
     * in order to get the actual stepsize.
     *
     * @param safety The value of safety factor to set.
     */
    public void set_safety(double safety) {
        set_safety(Float64.valueOf(safety));
    }

    /**
     * Set the value of the safety factor to apply to the calculated optimal stepsize
     * in order to get the actual stepsize.
     *
     * @param safety The value of safety factor to set.
     */
    public void set_safety(Float64 safety) {
        if (Check.positive(safety)) {
            this.safety = safety;
        } else {
            throw new IllegalArgumentException("The safety factor must be positive");
        }
    }

    /**
     * Set the value of amax, the maximum increase in stepsize if the last step was
     * not rejected.
     *
     * @return The value of amax.
     */
    public double get_amax_normal_value() {
        return get_amax_normal().doubleValue();
    }

    /**
     * Set the value of amax, the maximum increase in stepsize if the last step was
     * not rejected.
     *
     * @return The value of amax.
     */
    public Float64 get_amax_normal() {
        return amax_normal;
    }

    /**
     * Set the value of amax, the maximum increase in stepsize if the last step was
     * not rejected.
     *
     * @param amax The value of amax.
     */
    public void set_amax_normal(double amax) {
        set_amax_normal(Float64.valueOf(amax));
    }

    /**
     * Set the value of amax, the maximum increase in stepsize if the last step was
     * not rejected.
     *
     * @param amax The value of amax.
     */
    public void set_amax_normal(Float64 amax) {
        if (Check.valid(amax) && amax.isGreaterThan(Float64.ONE)) {
            amax_normal = amax;
            return;
        } else {
            throw new IllegalArgumentException("amax_normal must be greater than one");
        }
    }

    /**
     * Set the value of amax(rejected), the maximum increase in stepsize if the last
     * step was rejected.
     *
     * @param amax The value of amax(rejected).
     */
    public void set_amax_rejected(double amax) {
        set_amax_rejected(Float64.valueOf(amax));
    }

    /**
     * Set the value of amax(rejected), the maximum increase in stepsize if the last
     * step was rejected.
     *
     * @param amax The value of amax(rejected).
     */
    public void set_amax_rejected(Float64 amax) {
        if (Check.valid(amax) && !amax.isLessThan(Float64.ONE)) {
            amax_rejected = amax;
            return;
        } else {
            throw new IllegalArgumentException("amax_rejected must not be less than one");
        }
    }

    /**
     * Set the value of amax(rejected), the maximum increase in stepsize if the last step
     * was rejected.
     *
     * @return The value of amax(rejected).
     */
    public double get_amax_rejected_value() {
        return get_amax_rejected().doubleValue();
    }

    /**
     * Set the value of amax(rejected), the maximum increase in stepsize if the last step was
     * rejected.
     *
     * @return The value of amax(rejected).
     */
    public Float64 get_amax_rejected() {
        return amax_rejected;
    }

    /**
     * Set the value of amax(rejected), the maximum increase in stepsize taking into account if
     * the last step was rejected.
     *
     * @return The value of amax(rejected).
     */
    public double get_amax_value() {
        return get_amax().doubleValue();
    }

    /**
     * Set the value of amax(rejected), the maximum increase in stepsize taking into account if
     * the last step was rejected.
     *
     * @return The value of amax(rejected).
     */
    public Float64 get_amax() {
        if (previous_step_accepted) {
            return amax_normal;
        } else {
            return amax_rejected;
        }
    }

    /**
     * Set the value of amin, the maximum decrease allowed in the stepsize.
     *
     * @param amin The value of amin.
     */
    public void set_amin(Float64 amin) {
        if (Check.positive(amin) && amin.isLessThan(Float64.ONE)) {
            amin = amin;
        } else {
            throw new IllegalArgumentException("amin must be between zero and one");
        }

    }

    /**
     * Set the value of amin, the maximum decrease allowed in the stepsize.
     *
     * @param amin The value of amin.
     */
    public void set_amin(double amin) {
        if (amin > 0 && amin < 1) {
            this.amin = Float64.valueOf(amin);
        } else {
            throw new IllegalArgumentException("amin must be between zero and one");
        }
    }

    /**
     * Get the value of amin, the maximum decrease allowed in the stepsize.
     *
     * @return The value of amin.
     */
    public Float64 get_amin() {
        return amin;
    }

    /**
     * Get the value of amin, the maximum decrease allowed in the stepsize.
     *
     * @return The value of amin.
     */
    public double get_amin_value() {
        return get_amin().doubleValue();
    }
}
