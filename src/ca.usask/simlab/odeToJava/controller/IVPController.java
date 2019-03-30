/* ./controller/IVPController.java
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
package ca.usask.simlab.odeToJava.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import javolution.util.FastList;
import java.util.List;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.scheme.IMEXESDIRKButcherTableau;
import ca.usask.simlab.odeToJava.scheme.Scheme;
import ca.usask.simlab.odeToJava.scheme.ERKButcherTableau;
import ca.usask.simlab.odeToJava.scheme.ForwardEulerTableau;
import ca.usask.simlab.odeToJava.scheme.StormerVerletTableau;
import ca.usask.simlab.odeToJava.modules.errorControl.BaseErrControllerModule;
import ca.usask.simlab.odeToJava.modules.errorControl.EmbErrControllerModule;
import ca.usask.simlab.odeToJava.modules.errorControl.EmbErrExplicitPIControllerModule;
import ca.usask.simlab.odeToJava.modules.errorControl.EmbErrImplicitPredictiveControllerModule;
import ca.usask.simlab.odeToJava.modules.errorControl.EmbErrEstimatorModule;
import ca.usask.simlab.odeToJava.modules.errorControl.InitialStepSizeSelectorModule;
import ca.usask.simlab.odeToJava.modules.errorControl.SdErrControllerModule;
import ca.usask.simlab.odeToJava.modules.errorControl.SdErrEstimatorModule;
import ca.usask.simlab.odeToJava.modules.io.AllSolutionWriterModule;
import ca.usask.simlab.odeToJava.modules.io.InterpolatingSolutionWriterModule;
import ca.usask.simlab.odeToJava.modules.io.writers.CompoundSolutionWriter;
import ca.usask.simlab.odeToJava.modules.io.writers.DiskWriter;
import ca.usask.simlab.odeToJava.modules.io.writers.SolutionWriter;
import ca.usask.simlab.odeToJava.modules.scheme.IMEXESDIRKModule;
import ca.usask.simlab.odeToJava.modules.scheme.ERKModule;
import ca.usask.simlab.odeToJava.modules.scheme.ForwardEulerModule;
import ca.usask.simlab.odeToJava.modules.scheme.StormerVerletModule;
import ca.usask.simlab.odeToJava.solver.ConstantStepSolver;
import ca.usask.simlab.odeToJava.solver.EmbErrSolver;
import ca.usask.simlab.odeToJava.solver.PropertySolver;
import ca.usask.simlab.odeToJava.solver.SdErrSolver;
import ca.usask.simlab.odeToJava.solver.SolverModule;
import ca.usask.simlab.odeToJava.util.Matrix;

/**
 * This class acts as a wrapper for much of the functionality of ODEToJava.
 * <p>
 * Use this class by creating a new instance of it, then using the methods to
 * set the parameters that control the solver, and then by calling the run()
 * method to actually run the solver.
 */
public class IVPController {
    /**
     * Wrapper for an ODEToJava solver in a class that can be run in a separate
     * thread.
     */
    public class RunnableSolver implements Runnable {
        /**
         * The solver to use for solver.
         */
        public final PropertySolver solver;
        /**
         * The IVP for this solver.
         */
        public final IVP ivp;
        /**
         * The final time for this solver.
         */
        public final Float64 final_time;
        /**
         * The reason the solver stopped.
         */
        public String stopReason;
        /**
         * The constructor for this solver.
         *
         * @param solver The solver to use.
         * @param ivp The IVP to solve.
         * @param final_time The final time to integrate the IVP to.
         */
        public RunnableSolver(PropertySolver solver, IVP ivp, Float64 final_time) {
            this.solver = solver;
            this.ivp = ivp;
            this.final_time = final_time;
        }
        /**
         * Start the solver running.
         */
        public void run() {
            stopReason = solver.solve(ivp.get_ODE(), ivp.get_initial_time(), final_time, ivp.get_initial_values());
        }
    }

    // parameters for this controller
    private final IVP ivp;
    private final Float64 final_time;
    private String output_path;
    private PropertySolver solver = null;

    private ErrorControl error_control;
    private Float64Vector rtol, atol;
    private Float64 amax_normal, amax_rejected, amin, safety, threshold;
    private boolean initial_stepsize_selection;
    private Float64 initial_stepsize;
    /**
     * The supported error control methods.
     */
    public enum ErrorControl {
        /**
         * Use a constant stepsize solver with no error control.
         */
        NO_ERROR_CONTROL,
        /**
         * Use a custom solver for the error control.
         */
        CUSTOM_SOLVER,
        /**
         * Use an embedded error-estimation for error control.
         */
        EMB_ERROR_CONTROL,
        /**
         * Use an embedded error-estimation for error control and a special
         * controller for the stepsizes.
         */
        SPECIAL_EMB_ERROR_CONTROL,
        /**
         * Use a step doubling to estimate and control error.
         */
        SD_ERROR_CONTROL
    };
    private int num_points;
    private Float64 interp_interval;
    private Float64Vector interp_array;
    private Output output_type;
    /**
     * The types of interpolation/output methods.
     */
    public enum Output {
        /**
         * Output all of the solution points.
         */
        ALL_POINTS,
        /**
         * Output the solution points at approximately evenly spaced intervals.
         */
        FIXED_POINTS,
        /**
         * Interpolate at fixed intervals from the initial value except include
         * the last point.
         */
        INTERP_INTERVAL,
        /**
         * Interpolate to points specified in an array.
         */
        INTERP_ARRAY
    };
    // The Butcher tableau to use for this solution.
    private Scheme butcher_tableau;
    // A list of other modules to add to the solver.
    protected List<SolverModule> other_modules = new FastList<SolverModule>();
    // A list of writers for this solver.
    private List<SolutionWriter> writers = new FastList<SolutionWriter>();
    // The name of this IVPController.
    private String controller_name;

    /**
     * Constructor for a new controller with the given initial value problem and
     * final time.
     *
     * @param ivp The IVP to use.
     * @param final_time The final time to solve to.
     */
    public IVPController(IVP ivp, double final_time) {
        this(ivp, Float64.valueOf(final_time));
    }

    /**
     * Constructor for a new controller with the given initial value problem and
     * final time.
     *
     * @param ivp The IVP to use.
     * @param final_time The final time to solve to.
     */
    public IVPController(IVP ivp, Float64 final_time) {
        this.ivp = ivp;
        this.final_time = final_time;

        if (!final_time.isLargerThan(ivp.get_initial_time())) {
            throw new IllegalArgumentException();
        }

        controller_name = "";
        butcher_tableau = null;
        error_control = ErrorControl.EMB_ERROR_CONTROL;
        output_type = Output.FIXED_POINTS;
        num_points = 1000;
        output_path = null;
        atol = Matrix.fill(BaseErrControllerModule.DEFAULT_ATOL, ivp.get_size());
        rtol = Matrix.fill(BaseErrControllerModule.DEFAULT_RTOL, ivp.get_size());
        amax_normal = BaseErrControllerModule.DEFAULT_A_MAX_NORMAL;
        amax_rejected = BaseErrControllerModule.DEFAULT_A_MAX_REJECTED;
        amin = BaseErrControllerModule.DEFAULT_A_MIN;
        safety = BaseErrControllerModule.DEFAULT_SAFETY;
        threshold = BaseErrControllerModule.DEFAULT_THRESHOLD;
        initial_stepsize_selection = true;
    }


    /**
     * Get the butcher tableau for this controller.
     *
     * @return The Butcher tableau used.
     */
    public Scheme get_butcher_tableau() {
        return butcher_tableau;
    }

    /**
     * Set the butcher tableau for this controller.
     *
     * @param butcher_tableau The Butcher tableau to use.
     */
    public void set_butcher_tableau(Scheme butcher_tableau) {
        this.butcher_tableau = butcher_tableau;
    }

    /**
     * Use forward Euler as the method for this controller.
     */
    public void set_forward_euler() {
        this.butcher_tableau = new ForwardEulerTableau();
    }

    /**
     * Use Stormer-Verlet as the method for this controller.
     */
    public void set_stormer_verlet() {
        this.butcher_tableau = new StormerVerletTableau();
    }

    /**
     * Get The final time for this controller.
     *
     * @return The final time to solve the IVP to.
     */
    public Float64 get_final_time() {
        return final_time;
    }

    /**
     * Get the IVP for this controller.
     *
     * @return The IVP this controller is solving.
     */
    public IVP get_IVP() {
        return ivp;
    }

    /**
     * Get the error control method this controller is using
     *
     * @return The error control method being used.
     */
    public ErrorControl get_error_control() {
        return error_control;
    }

    /**
     * Set this controller to use constant stepsize and no error control.
     */
    public void set_no_error_control() {
        error_control = ErrorControl.NO_ERROR_CONTROL;
    }

    public void set_custom_solver(PropertySolver solver) {
        error_control = ErrorControl.CUSTOM_SOLVER;
        this.solver = solver;
    }

    /**
     * Set this controller to use embedded error-estimation for error control.
     */
    public void set_emb_error_control() {
        error_control = ErrorControl.EMB_ERROR_CONTROL;
    }

    /**
     * Set this controller to use a special stepsize-controller for embedded
     * error-estimation for error control.
     */
    public void set_special_emb_error_control() {
        error_control = ErrorControl.SPECIAL_EMB_ERROR_CONTROL;
    }

    /**
     * Set this controller to use step-doubling error-estimation for error
     * control.
     */
    public void set_sd_error_control() {
        error_control = ErrorControl.SD_ERROR_CONTROL;
    }

    /**
     * Set the output path for this controller.
     *
     * @param path The output path for this controller.
     */
    public void set_output_path(String path) {
        output_path = path;
    }

    /**
     * Get the output path for this controller.
     *
     * @return The output path for this controller.
     */
    public String get_output_path() {
        return output_path;
    }

    /**
     * Write the solution at approximately evenly spaced points.
     */
    public void write_all_points() {
        if (num_points < 0) {
            throw new IllegalArgumentException();
        }
        output_type = Output.ALL_POINTS;
    }

    /**
     * Write the solution at a fixed interval.
     *
     * Requires the use of interpolation.
     *
     * @param interval The interval between outputs.
     */
    public void write_at_interval(double interval) {
        write_at_interval(Float64.valueOf(interval));
    }

    /**
     * Write the  solution at a fixed interval.
     *
     * Requires the use of interpolation.
     *
     * @param interval The interval between outputs.
     */
    public void write_at_interval(Float64 interval) {
        if (!interval.isGreaterThan(Float64.ZERO)) {
            throw new IllegalArgumentException();
        }
        output_type = Output.INTERP_INTERVAL;
        interp_interval = interval;
    }

    /**
     * Write at specific points in the solution interval.
     *
     * @param array The array of points to write at. Must be in increasing order and
     *              fall within the interval from the initial time of the IVP to its
     *              final time.
     * @throws IllegalArgumentException The array is invalid.
     */
    public void write_at_array(Float64Vector array) throws IllegalArgumentException {
        if (array.getDimension() > 0) {
            Float64 current = array.get(0);
            for (int i = 1; i < array.getDimension(); i++) {
                if (!current.isLessThan(array.get(i))) {
                    throw new IllegalArgumentException();
                }
            }
            if (array.get(0).isLessThan(ivp.get_initial_time()) || array.get(array.getDimension() - 1).isGreaterThan(final_time)) {
                throw new IllegalArgumentException();
            }
        }

        output_type = Output.INTERP_ARRAY;
        interp_array = array;
    }

    /**
     * Get the method used for handling the output from this controller.
     *
     * @return The output method handler.
     */
    public Output get_output_type() {
        return output_type;
    }

    /**
     * Get the interval the solver will use for the interpolated output.
     *
     * @return The interval for interpolated output.
     */
    public Float64 get_points_interval() {
        if (output_type != Output.INTERP_INTERVAL) {
            throw new IllegalStateException();
        }
        return interp_interval;
    }

    /**
     * Get the array of points the solver will use for the interpolated
     * output.
     *
     * @return The array of points for the interpolated output.
     */
    public Float64Vector get_points_array() {
        if (output_type != Output.INTERP_ARRAY) {
            throw new IllegalStateException();
        }
        return interp_array;
    }

    /**
     * Set a single absolute tolerance for all solution components.
     *
     * @param atol The absolute tolerance.
     */
    public void set_atol(double atol) {
        set_atol(Float64.valueOf(atol));
    }

    /**
     * Set a separate absolute tolerance for each solution component.
     *
     * @param atol The array of tolerances.
     *
     * @throws IllegalArgumentException If the array of tolerances is
     *                                  not the same size as the ODE.
     */
    public void set_atol(double[] atol) {
        set_atol(Float64Vector.valueOf(atol));
    }

    /**
     * Set a single absolute tolerance for all solution components.
     *
     * @param atol The absolute tolerance.
     */
    public void set_atol(Float64 atol) {
        set_atol(Matrix.fill(atol, ivp.get_size()));
    }

    /**
     * Set a separate absolute tolerance for each solution component.
     *
     * @param atol The array of tolerances.
     *
     * @throws IllegalArgumentException If the array of tolerances is
     *                                  not the same size as the ODE.
     */
    public void set_atol(Float64Vector atol) {
        if (atol.getDimension() != ivp.get_size()) {
            throw new IllegalArgumentException("tolerance vectors must be the same size as the ODE");
        }
        this.atol = atol;
    }

    /**
     * Get the absolute tolerance.
     *
     * @return The absolute tolerance as a Float64 vector.
     */
    public Float64Vector get_atol() {
        return atol;
    }

    /**
     * Set a single relative tolerance for all solution components.
     *
     * @param rtol The relative tolerance.
     */
    public void set_rtol(double rtol) {
        set_rtol(Float64.valueOf(rtol));
    }

    /**
     * Set a separate absolute tolerance for each solution component.
     *
     * @param rtol The array of relative tolerances.
     */
    public void set_rtol(double[] rtol) {
        set_rtol(Float64Vector.valueOf(rtol));
    }

    /**
     * Set a single relative tolerance for all solution components.
     *
     * @param rtol The relative tolerance.
     */
    public void set_rtol(Float64 rtol) {
        set_rtol(Matrix.fill(rtol, ivp.get_size()));
    }

    /**
     * Set a separate absolute tolerance for each solution component.
     *
     * @param rtol The array of relative tolerances.
     */
    public void set_rtol(Float64Vector rtol) {
        if (rtol.getDimension() != ivp.get_size()) {
            throw new IllegalArgumentException("tolerance vectors must be the same size as the ODE");
        }
        this.rtol = rtol;
    }

    /**
     * Get the relative tolerances
     *
     * @return The relative tolerance as a Float64Vector object.
     */
    public Float64Vector get_rtol() {
        return rtol;
    }

    /**
     * Set the parameter for the maximum increase in stepsize if the last step was not rejected.
     *
     * @param amax_normal The maximum increase in stepsize.
     */
    public void set_amax_normal(Float64 amax_normal) {
        this.amax_normal = amax_normal;
    }

    /**
     * Set the parameter for the maximum increase in stepsize if the last step was not rejected.
     *
     * @param amax_normal The parameter for maximum increase in stepsize.
     */
    public void set_amax_normal(double amax_normal) {
        this.amax_normal = Float64.valueOf(amax_normal);
    }

    /**
     * Get the parameter for the maximum increase in stepsize if the last step was not rejected.
     *
     * @return The parameter for maximum increase in stepsize.
     */
    public Float64 get_amax_normal() {
        return this.amax_normal;
    }

    /**
     * Get the parameter for the maximum increase in stepsize if the last step was not rejected.
     *
     * @return The parameter for maximum increase in stepsize.
     */
    public double get_amax_normal_value() {
        return this.amax_normal.doubleValue();
    }

    /**
     * Set the parameter for the maximum increase in stepsize if the last step was rejected.
     *
     * @param amax_rejected The parameter for maximum increase in stepsize.
     */
    public void set_amax_rejected(Float64 amax_rejected) {
        this.amax_rejected = amax_rejected;
    }

    /**
     * Set the parameter for the maximum increase in stepsize if the last step was rejected.
     *
     * @param amax_rejected The parameter for maximum increase in stepsize.
     */
    public void set_amax_rejected(double amax_rejected) {
        this.amax_rejected = Float64.valueOf(amax_rejected);
    }

    /**
     * Get the parameter for the maximum increase in stepsize if the last step was rejected.
     *
     * @return The parameter for maximum increase in stepsize.
     */
    public Float64 get_amax_rejected() {
        return this.amax_rejected;
    }

    /**
     * Get the parameter for the maximum increase in stepsize if the last step was rejected.
     *
     * @return The maximum increase in stepsize.
     */
    public double get_amax_rejected_value() {
        return this.amax_rejected.doubleValue();
    }

    /**
     * Set the parameter for the maximum decrease in stepsize.
     *
     * @param amin The parameter for the maximum decrease in stepsize.
     */
    public void set_amin(Float64 amin) {
        this.amin = amin;
    }

    /**
     * Set the parameter for the maximum decrease in stepsize.
     *
     * @param amin The parameter for the maximum decrease in stepsize.
     */
    public void set_amin(double amin) {
        this.amin = Float64.valueOf(amin);
    }

    /**
     * Get the parameter for the maximum decrease in stepsize.
     *
     * @return The parameter for the maximum decrease in stepsize.
     */
    public Float64 get_amin() {
        return amin;
    }

    /**
     * Get the parameter for the maximum decrease in stepsize.
     *
     * @return The parameter for the maximum decrease in stepsize.
     */
    public double get_amin_value() {
        return amin.doubleValue();
    }

    /**
     * Set the threshold for accepted steps in the case of step-doubling error
     * estimation.
     *
     * @param threshold The parameter for the fraction of the optimal stepsize.
     */
    public void set_threshold(Float64 threshold) {
        this.threshold = threshold;
    }

    /**
     * Set the threshold for accepted steps in the case of step-doubling error
     * estimation.
     *
     * @param threshold The parameter for the fraction of the optimal stepsize.
     */
    public void set_threshold(double threshold) {
        this.threshold = Float64.valueOf(threshold);
     }

    /**
     * Get the threshold for accepted steps in the case of step-doubling error
     * estimation.
     *
     * @return The parameter for the fraction of the optimal stepsize.
     */
    public Float64 get_threshold() {
        return threshold;
    }

    /**
     * Get the threshold for accepted steps in the case of step-doubling error
     * estimation.
     *
     * @return The parameter for the fraction of the optimal stepsize.
     */
    public double get_threshold_value() {
        return threshold.doubleValue();
    }

    /**
     * Set the fraction of the optimal stepsize to use.
     *
     * @param safety The parameter for the fraction of the optimal stepsize.
     */
    public void set_safety(Float64 safety) {
        this.safety = safety;
    }

    /**
     * Set the fraction of the optimal stepsize to use.
     *
     * @param safety The parameter for the fraction of the optimal stepsize.
     */
    public void set_safety(double safety) {
        this.safety = Float64.valueOf(safety);
     }

    /**
     * Get the fraction of the optimal stepsize to use.
     *
     * @return The parameter for the fraction of the optimal stepsize.
     */
    public Float64 get_safety() {
        return safety;
    }

    /**
     * Get the fraction of the optimal stepsize to use.
     *
     * @return The parameter for the fraction of the optimal stepsize.
     */
    public double get_safety_value() {
        return safety.doubleValue();
    }

    /**
     * Use the initial stepsize selection routine.
     */
    public void use_initial_stepsize_selection() {
        initial_stepsize_selection = true;
    }

    /**
     * Determine if this controller is using initial stepsize selection
     *
     * @return Indicate if the controller is using initial stepsize selection.
     */
    public boolean using_initial_stepsize_selection() {
        return initial_stepsize_selection;
    }

    /**
     * Set the initial stepsize.
     *
     * If this is a constant-stepsize solver, this is the stepsize that will be
     * used for the entire solution process.
     *
     * @param step The initial stepsize.
     *
     * @throws IllegalArgumentException If the stepsize is non-positive.
     */
    public void set_initial_stepsize(double step) {
        set_initial_stepsize(Float64.valueOf(step));
    }

    /**
     * Set the initial stepsize.
     *
     * If this is a constant-stepsize solver, this is the stepsize that will be
     * used for the entire solution process.
     *
     * @param step                      The initial stepsize.
     * @throws IllegalArgumentException If the stepsize is non-positive.
     */
    public void set_initial_stepsize(Float64 step) {
        if (!step.isGreaterThan(Float64.ZERO)) {
            throw new IllegalArgumentException();
        }
        initial_stepsize_selection = false;
        initial_stepsize = step;
    }

    /**
     * Get whether the initial stepsize.
     *
     * @return the initial stepsize
     */
    public Float64 get_initial_stepsize() {
        if (initial_stepsize_selection) {
            return Float64.valueOf(-1.0);
        }
        return initial_stepsize;
    }

    /**
     * Start the solver running.
     *
     * @throws FileNotFoundException If any of the specified input or output files
     *                               cannot be found.
     */
    public void run() throws FileNotFoundException, IOException {
        boolean has_emb;
        if (butcher_tableau != null) {
            has_emb = butcher_tableau.has_emb();
        } else {
            has_emb = false;
        }

        if ((error_control == ErrorControl.SPECIAL_EMB_ERROR_CONTROL || error_control == ErrorControl.EMB_ERROR_CONTROL) && !has_emb) {
            throw new IllegalStateException();
        }

        RunnableSolver toRun = get_runnable();
        toRun.run();
    }


    /**
     * Start the solver running.
     *
     * @throws FileNotFoundException If any of the specified input or output files
     *                               cannot be found.
     */
    public RunnableSolver get_runnable() throws FileNotFoundException, IOException {
        final PropertySolver solver = get_solver();
        return new RunnableSolver(solver, ivp, final_time);
    }

    /**
     * Add a module to the IVPController.
     *
     * @param module The module to add to the solver.
     */
    public void add_module(SolverModule module) {
        other_modules.add(module);
    }

    /**
     * Add a new solution writer.
     *
     * @param writer The solution writer to add to the solver.
     */
    public void add_solution_writer(SolutionWriter writer) {
        writers.add(writer);
    }

    /**
     * Remove a solution writer.
     *
     * @param writer The solution writer to add to the solver.
     */
    public void remove_solution_writer(SolutionWriter writer) {
        writers.remove(writer);
    }

    /**
     * Name this controller.
     *
     * @param name The name of this controller.
     */
    public void set_controller_name(String name) {
        controller_name = name;
    }

    @Override
    public String toString() {
        return controller_name;
    }

    private PropertySolver get_solver() throws FileNotFoundException, IOException {
        switch (error_control) {
            case NO_ERROR_CONTROL:
                return create_no_error_control();
            case CUSTOM_SOLVER:
                solver.set_initial_stepsize(initial_stepsize);
                solver.add_solver_module(get_writer_module());
                add_other_modules(solver);
                return solver;
            case EMB_ERROR_CONTROL:
                return create_embedded_error_control();
            case SPECIAL_EMB_ERROR_CONTROL:
                return create_embedded_error_control();
            case SD_ERROR_CONTROL:
                return create_sd_error_controller();
            default:
                throw new IllegalStateException("Unspecified error control method");
        }
    }

    /**
     * Create a solver that uses no error control.
     *
     * @throws FileNotFoundException If any of the specified input or output files
     *                               cannot be found.
     * @throws IOException           If there was an error opening, writing to,
     *                               or reading from a file.
     */
    private PropertySolver create_no_error_control() throws FileNotFoundException, IOException {
        ConstantStepSolver solver = new ConstantStepSolver();

        if (butcher_tableau instanceof ForwardEulerTableau) {
            solver.add_solver_module(new ForwardEulerModule());
        } else if (butcher_tableau instanceof StormerVerletTableau) {
            solver.add_solver_module(new StormerVerletModule());
        }
        else if (butcher_tableau instanceof ERKButcherTableau) {
            solver.add_solver_module(new ERKModule((ERKButcherTableau)butcher_tableau));
        } else if (butcher_tableau instanceof IMEXESDIRKButcherTableau) {
            solver.add_solver_module(new IMEXESDIRKModule((IMEXESDIRKButcherTableau)butcher_tableau));
        }

        if (initial_stepsize_selection) {
            throw new IllegalStateException("Cannot use Initial stepsize selection with no error control method.");
        } else {
            solver.set_initial_stepsize(initial_stepsize);
        }

        solver.add_solver_module(get_writer_module());

        add_other_modules(solver);
        return solver;
    }

    private PropertySolver create_sd_error_controller() throws FileNotFoundException, IOException {
        SdErrSolver solver = new SdErrSolver();

        if (butcher_tableau instanceof ForwardEulerTableau) {
            solver.addStdSolverModule(new ForwardEulerModule());
            solver.addErrSolverModule(new ForwardEulerModule());
        } else if (butcher_tableau instanceof StormerVerletTableau) {
            solver.addStdSolverModule(new StormerVerletModule());
            solver.addErrSolverModule(new StormerVerletModule());
        }
        else if (butcher_tableau instanceof ERKButcherTableau) {
            solver.addStdSolverModule(new ERKModule((ERKButcherTableau)butcher_tableau));
            solver.addErrSolverModule(new ERKModule((ERKButcherTableau)butcher_tableau));
        } else if (butcher_tableau instanceof IMEXESDIRKButcherTableau) {
            solver.addStdSolverModule(new IMEXESDIRKModule((IMEXESDIRKButcherTableau)butcher_tableau));
            solver.addErrSolverModule(new IMEXESDIRKModule((IMEXESDIRKButcherTableau)butcher_tableau));
        }

        solver.add_solver_module(get_error_estimator_module());
        solver.add_solver_module(get_stepsize_controller_module());

        if (initial_stepsize_selection) {
            solver.add_solver_module(get_initial_stepsize_module());
        } else {
            solver.set_initial_stepsize(initial_stepsize);
        }

        solver.add_solver_module(get_writer_module());

        add_other_modules(solver);

        return solver;
    }

    /**
     * Get the module used to set the initial stepsize.
     *
     * @return The module to select the initial stepsize.
     */
    private SolverModule get_initial_stepsize_module() {
        return new InitialStepSizeSelectorModule();
    }

    /**
     * Create a solver that has embedded error control.
     *
     * @return The solver with embedded error control.
     *
     * @throws FileNotFoundException If any of the specified input or output files
     *                               cannot be found.
     * @throws IOException           If there was an error opening, writing to,
     *                               or reading from a file.
     */
    private PropertySolver create_embedded_error_control() throws FileNotFoundException, IOException {
        EmbErrSolver solver = new EmbErrSolver();

        if (butcher_tableau instanceof ERKButcherTableau) {
            solver.add_solver_module(new ERKModule((ERKButcherTableau)butcher_tableau));
        } else if (butcher_tableau instanceof IMEXESDIRKButcherTableau) {
            solver.add_solver_module(new IMEXESDIRKModule((IMEXESDIRKButcherTableau)butcher_tableau));
        }
        solver.add_solver_module(get_error_estimator_module());
        solver.add_solver_module(get_stepsize_controller_module());

        if (initial_stepsize_selection) {
            solver.add_solver_module(get_initial_stepsize_module());
        } else {
            solver.set_initial_stepsize(initial_stepsize);
        }

        solver.add_solver_module(get_writer_module());

        add_other_modules(solver);

        return solver;
    }

    /**
     * Get the error control module.
     */
    private BaseErrControllerModule get_stepsize_controller_module() {
        BaseErrControllerModule module = null;
        switch (error_control) {
            case EMB_ERROR_CONTROL:
            {
                module = new EmbErrControllerModule(atol, rtol, ivp.get_ODE());
                break;
            }
            case SPECIAL_EMB_ERROR_CONTROL:
            {
                if (butcher_tableau instanceof ERKButcherTableau) {
                    module = new EmbErrExplicitPIControllerModule(atol, rtol, ivp.get_ODE());
                } else if (butcher_tableau instanceof IMEXESDIRKButcherTableau) {
                    module = new EmbErrImplicitPredictiveControllerModule(atol, rtol, ivp.get_ODE());
                }
                break;
            }
            case SD_ERROR_CONTROL:
            {
                module = new SdErrControllerModule(atol, rtol, ivp.get_ODE());
                module.set_threshold(threshold);
                break;
            }
            default:
                throw new IllegalStateException("Unknown error controller module.");
        }
        module.set_amax_normal(amax_normal);
        module.set_amax_rejected(amax_rejected);
        module.set_amin(amin);
        module.set_safety(safety);
        return module;
    }

    /**
     * Get the error estimator module.
     */
    private SolverModule get_error_estimator_module() {
        switch (error_control) {
            case EMB_ERROR_CONTROL:
                return new EmbErrEstimatorModule();
            case SPECIAL_EMB_ERROR_CONTROL:
                return new EmbErrEstimatorModule();
            case SD_ERROR_CONTROL:
                return new SdErrEstimatorModule();
            default:
                throw new IllegalStateException("Unknown error estimator module.");
        }
    }

    /**
     * Get the solution writer module.
     *
     * @throws FileNotFoundException If any of the specified input or output files
     *                               cannot be found.
     * @throws IOException           If there was an error opening, writing to,
     *                               or reading from a file.
     */
    private SolverModule get_writer_module() throws FileNotFoundException, IOException {
        CompoundSolutionWriter compoundWriter = new CompoundSolutionWriter();
        for (SolutionWriter i : writers) {
            compoundWriter.add_solution_writer(i);
        }

        if (output_path != null) {
            compoundWriter.add_solution_writer(new DiskWriter(output_path));
        }

        switch (output_type) {
            case FIXED_POINTS:
                if (num_points == 0) {
                    return new AllSolutionWriterModule(compoundWriter);
                } else {
                    return new AllSolutionWriterModule(compoundWriter);
                }
            case INTERP_INTERVAL:
                return new InterpolatingSolutionWriterModule(compoundWriter, interp_interval);
            case INTERP_ARRAY:
                return new InterpolatingSolutionWriterModule(compoundWriter, interp_array);
            default:
                throw new IllegalStateException("Unknown Points Handling method.");
        }
    }

    /**
     * Add miscellaneous modules other than the integration modules to the solver.
     *
     * @param solver The solver to add the modules to.
     */
    private void add_other_modules(PropertySolver solver) {
        Iterator i = other_modules.iterator();
        while (i.hasNext()) {
            solver.add_solver_module((SolverModule) i.next());
        }
    }
}
