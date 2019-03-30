/* ./testSuite/SolutionTester.java
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
package ca.usask.simlab.odeToJava.testSuite;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Vector;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Matrix;
import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.controller.IVPController;
import ca.usask.simlab.odeToJava.util.FileSolutionReader;

/**
 * Test a set of IVP solutions generated with different stepsizes and tolerances against a 
 * reference solution for that IVP.
 *
 */
public class SolutionTester {
    /**
     * The default number of maximum significant figures.  
     */
    public static final int DEFAULTMAXSIGFIG = 10;

    /**
     * A Vector of IVPcontroller objects to test with.
     */     
    protected Vector<IVPController> controllers;

    /**
     * The collected solutions from the tests.
     */          
    protected SolutionCollector collector;

    /**
     * A Vector of absolute tolerances to test with.
     */               
    protected Vector<Vector> atols;

    /**
     * A Vector of relative tolerances to test with.
     */                    
    protected Vector<Vector> rtols;

    /**
     * The component-wise absolute errors from each test.
     */                         
    protected double[][] absolute_errors;

    /**
     * The maximum significant figures to display in the digit comparison.
     */                              
    protected int maximum_significant_figures;
    
    /**
     * The stream to display the output from the tests to.
     */                               
    protected PrintStream out;
    
    /**
     * The FileSolutionReader that is used to read the reference solution for
     * this IVP.
     */                               
    protected FileSolutionReader reader;

    /**
     * The path of the reference solution.
     */                               
    protected String reference_solution_filename; 
    
    /**
     * The reference solution points as rows of a matrix. 
     */                               
    protected Float64Matrix reference_solution;
    
    /**
     * The times of the reference solution points as entries in a vector.
     */                               
    protected Float64Vector reference_time;

    /**
     * The component-wise relative errors from each test.
     */                          
    protected double[][] relative_errors;
    
    /**
     * The component-wise significant figures from each test.
     */                               
    protected int[][] significant_figures;
    
    /**
     * The average significant figures from each test.
     */                               
    protected int[][] average_significant_figures;
    
    /**
     * The stepsizes used for each test, corresponding to the tolerances.
     */                               
    protected Vector<Vector> stepsizes;
    
    /**
     * The solution times in nanoseconds from each test. 
     */                               
    protected long[][] times;
    
    /**
     * The standard constructor.
     *
     * @param reference_solution_filename The path to the reference solution.
     */
    public SolutionTester(String reference_solution_filename) throws IOException, ReferenceTimeNotEqualException {
        reader = new FileSolutionReader(reference_solution_filename);
        collector = new SolutionCollector();
        maximum_significant_figures = SolutionTester.DEFAULTMAXSIGFIG;
        reference_time = null;
        reference_solution = null;
        atols = new Vector<Vector>();
        rtols = new Vector<Vector>();
        stepsizes = new Vector<Vector>();
        controllers = new Vector<IVPController>();
        out = null;
        this.reference_solution_filename = reference_solution_filename;
        // read the reference solution so we can extract good things
        read_reference_solution();
    }
    
    /**
     * Add an IVPController for generating a solution.
     * 
     * @param controller An IVPController to add to the solution tester.
     */
    public void add_IVPController(IVPController controller) {
        add_IVPController(controller, null, null, null);
    }
    
    /**
     * Add an IVPController for generating a solution that runs with different absolute tolerances,
     * relative tolerances, and initial stepsizes.
     * <p>
     * If the IVPController uses a constant step-size solver, the initial stepsizes are the
     * stepsizes to be used, and both relative tolerances and absolute tolerances have no effect.
     * 
     * @param controller The IVPController to add.
     * @param atol An array of absolute tolerances.
     * @param rtol An array of relative tolerances.
     * @param initial_stepsizes An array of initial stepsizes to use.
     */
    public void add_IVPController(IVPController controller, Vector rtol, Vector atol, Vector initial_stepsizes) {
        controllers.add(controller);
        this.rtols.add(rtol); 
        this.atols.add(atol);
        this.stepsizes.add(initial_stepsizes);
    }

    /**
     * Add a collection of IVPController objects in order to generate generate
     * solutions with different absolute tolerances, relative tolerances, and
     * initial stepsizes.
     * <p>
     * If the IVPController uses a constant step-size solver, the initial stepsizes
     * are the stepsizes to be used, and both relative tolerances and absolute
     * tolerances have no effect.
     * 
     * @param controllers A collection of IVPControllers to add.
     * @param atol An array of absolute tolerances.
     * @param rtol An array of relative tolerances.
     * @param initial_stepsizes An array of initial stepsizes to use.
     */
    public void add_IVPControllers(Collection<IVPController> controllers, Vector rtol, Vector atol, Vector initial_stepsizes) {
        this.controllers.addAll(controllers);
        for (int i = 0; i < controllers.size(); i++) {
            this.atols.add(atol);
            this.rtols.add(rtol);
            this.stepsizes.add(initial_stepsizes);
        }
    } 
    
    /**
     * Add a collection of IVPController to generate solution.
     * 
     * @param controllers A collection of IVPControllers to add.
     */
    public void add_IVPControllers(Collection<IVPController> controllers) {
        add_IVPControllers(controllers, null, null, null);
    }

    /**
     * Get the initial time for the IVP.
     * 
     * @return The initial time.
     */     
    public Float64 get_initial_time() {
        return reference_time.get(0);
    }

    /**
     * Get the final time the IVP is solved to.    
     */     
    public Float64 get_final_time() {
        return reference_time.get(reference_time.getDimension() - 1);
    }

    /**
     * Get the solution times where the IVP solution is compared to the
     * reference solution.    
     *
     * @return The solution times.
     */          
    public Float64Vector get_solution_times() {
        return reference_time;
    }
    
    /**
     * Get the initial values for the IVP.
     *
     * @return The initial values. 
     */           
    public Float64Vector get_initial_values() {
        return reference_solution.getRow(0);
    }
    
    /**
     * Get the array of absolute errors corresponding to each
     * IVPController object.
     * 
     * @return An array of absolute errors.
     */
    public double[][] get_absolute_errors() {
        return absolute_errors;
    }
    
    /**
     * Get the array of relative errors corresponding to each
     * IVPController object.
     * 
     * @return An array of relative errors.
     */
    public double[][] get_relative_errors() {
        return relative_errors;
    } 

    /**
     * Get the vector of IVPControllers for this SolutionTester object.
     * 
     * @return A vector of IVPController objects.
     */
    public Vector<IVPController> get_IVPControllers() {
        return controllers;
    }
    
    /**
     * Determine the number of runs for each IVPController
     * object needed for testing.
     * 
     * @param current_controller The index corresponding to the current IVPController.
     *
     * @return The number of runs needed for the particular IVPController.
     */
    protected int get_number_runs(int current_controller) {
        Vector atol = this.atols.get(current_controller);
        Vector rtol = this.rtols.get(current_controller);
        Vector stepsize = this.stepsizes.get(current_controller);
        
        int number_runs = 1;
       
        if (rtol != null) {
            number_runs = Math.max(rtol.size(), number_runs);
        }
        
        if (atol != null) {
            number_runs = Math.max(atol.size(), number_runs);
        }

        if (stepsize != null) {
            number_runs = Math.max(stepsize.size(), number_runs);
        }
        
        return number_runs;
    }
    
    /**
     * Return a matrix of number of matching significant figures corresponding
     * to each IVPController
     * 
     * @return An matrix of the number of significant figures.
     */
    public int[][] get_number_sigfigs() {
        return significant_figures;
    }
    
    /**
     * Return an matrix of CPU execution times for each IVPController.
     * 
     * @return An matrix of CPU execution times.
     */
    public long[][] get_times() {
        return times;
    }
    
    /**
     * Set an output stream for the statistics from the testing.
     * 
     * @param out A Prinstream output object.
     */
    public void output_test_stats(PrintStream out) {
        this.out = out;
    }
    
    /**
     * Output the test results.
     * 
     * @param current_controller Index corresponding to the current IVPController.
     * @param current_run Index corresponding to the current run of the
     *                   particular controller. 
     */
    protected void print_stats(int current_controller, int current_run) {
        Vector<String> info = new Vector<String>();
        DecimalFormat form = new DecimalFormat("0.00000E00");
        
        // solver name
        IVPController controller = controllers.get(current_controller);
        
        if (controller.toString().equals("")) {
            info.add(Integer.toString(current_controller));
        } else {
            info.add(controller.toString());
        }
        
        // initial stepsize
        if (controller.get_initial_stepsize().isGreaterThan(Float64.ZERO)) {
            info.add(form.format(controller.get_initial_stepsize().doubleValue()));
        } else {
            info.add("Automatic");
        }
        
        // absolute tolerance and relative tolerance
        if (controller.get_error_control() != IVPController.ErrorControl.NO_ERROR_CONTROL) {
            info.add(form.format(controller.get_atol().getValue(0)));
            info.add(form.format(controller.get_rtol().getValue(0)));
        } else {
            info.add("N/A");
            info.add("N/A");
        }
        
        // Execution time
        info.add(form.format(times[current_controller][current_run] / 1000000000.0));
        
        // Absolute error
        info.add(form.format(absolute_errors[current_controller][current_run]));
        
        // Relative error
        info.add(form.format(relative_errors[current_controller][current_run]));
        
        // Minimum number of significant figures
        info.add(Integer.toString(significant_figures[current_controller][current_run]));
        
        // Average number of significant figures
        info.add(Integer.toString(average_significant_figures[current_controller][current_run]));
        
        print_vector(info);
    }
    
    /**
     * Output the header of the test results to the output stream.
     */
    protected void print_stats_header() {
        
        Vector<String> info = new Vector<String>();
        
        out.println("Reference Solution: " + reference_solution_filename);
        // XXX this is a hack
        // gives the first method used, if different methods are used this is
        // deceptive
        out.println("Method: " + controllers.get(0).get_butcher_tableau().get_name());
        info.add("Solver");
        info.add("InitStepSize");
        info.add("AbsTol");
        info.add("RelTol");
        info.add("Time (s)");
        info.add("AbsError");
        info.add("RelError");
        info.add("MinSigFigs");
        info.add("AvgSigFigs");
        
        print_vector(info);
    }
    
    /**
     * Print each string in a particular vector to a line.
     * 
     * @param info A vector of strings.
     */
    protected void print_vector(Vector<String> info) {       
        for (int i = 0; i < info.size(); i++) {
            out.printf("%15s", info.get(i));
        }
        out.println();
    }
    
    /**
     * Read the reference solution from its file.
     * 
     * @throws FileNotFoundException Reference solution file not found.
     * @throws IOException Error reading the reference solution.
     */
    protected void read_reference_solution() throws FileNotFoundException, IOException {
        reader.readSolution();
        reference_time = reader.get_time();
        reference_solution = reader.get_solution();
    }
    
    /*
     * Reset the test results.
     */
    protected void resetStats() {
        
        for (int i = 0; i < controllers.size(); i++) {
            int number_runs = get_number_runs(i);
            
            times = new long[controllers.size()][number_runs];
            absolute_errors = new double[controllers.size()][number_runs];
            relative_errors = new double[controllers.size()][number_runs];
            significant_figures = new int[controllers.size()][number_runs];
            average_significant_figures = new int[controllers.size()][number_runs];
        }
    }
    
    /**
     * Compare each solution from the various IVPControllers to the reference solution.
     * 
     * @throws IOException Error reading the reference solution file.
     * @throws ReferenceTimeNotEqualException The solution times are not equal.
     */
    public void run() throws IOException, ReferenceTimeNotEqualException {
        resetStats();
        if (reference_solution == null) {
            read_reference_solution();
        }
        
        if (out != null && controllers.size() > 0) {
            print_stats_header();
            
        }
        
        for (int i = 0; i < controllers.size(); i++) {
            IVPController controller = controllers.get(i);
            controller.add_solution_writer(collector);
            
            int number_runs = get_number_runs(i);
            
            for (int j = 0; j < number_runs; j++) {
                
                updateController(i, j);
                
                long startTime = System.nanoTime();
                controller.run();
                long stopTime = System.nanoTime();
                times[i][j] = stopTime - startTime;
               
                update_stats(i, j);
                
                if (out != null) {
                    print_stats(i, j);
                }
            }
            controller.remove_solution_writer(collector);
            
        }
        
        // output a newline for the statistics
        if (out != null && controllers.size() > 0) {
            out.println();
        }
    }
    
    /**
     * Set the maximum number of significant figures to compare.
     * 
     * @param max_sigfigs The maximum number of significant figures.
     */
    public void set_max_sigfigs(int max_sigfigs) {
        maximum_significant_figures = max_sigfigs;
    }
    
    /**
     * Set the SolutionCollector object.
     * 
     * @param collector A SolutionCollector object.
     */
    public void set_solution_collector(SolutionCollector collector) {
        
        this.collector = collector;
    }
    
    /**
     * Update the controller prior to each test run.
     * 
     * @param current_controller The index of the current controller.
     * @param current_run The index of the current run.
     */
    protected void updateController(int current_controller, int current_run) {
        IVPController controller = controllers.get(current_controller);
        Vector rtol = rtols.get(current_controller);
        Vector atol = atols.get(current_controller);
        Vector stepsize = stepsizes.get(current_controller);
       
        if (rtol != null && current_run < rtol.size()) {
            if (rtol.get(current_run) instanceof Float64Vector) {
                controller.set_rtol((Float64Vector) rtol.get(current_run));
            } else if (rtol.get(current_run) instanceof Float64) {
                controller.set_rtol((Float64) rtol.get(current_run));
            } else {
                controller.set_rtol((Double) rtol.get(current_run));
            }
        }

        if (atol != null && current_run < atol.size()) {
            if (atol.get(current_run) instanceof Float64Vector) {
                controller.set_atol((Float64Vector) atol.get(current_run));
            } else if (rtol.get(current_run) instanceof Float64) {
                controller.set_atol((Float64) atol.get(current_run));
            } else {
                controller.set_atol((Double) atol.get(current_run));
            } 
        }
 
        if (stepsize != null && current_run < stepsize.size()) {
            if (stepsize.get(current_run) instanceof Float64) {
                controller.set_initial_stepsize((Float64) stepsize.get(current_run));
            } else {
                controller.set_initial_stepsize((Double) stepsize.get(current_run));
            }
        }
    }
    
    /**
     * Update the test results after each test run.
     * 
     * @param current_controller The index of the current controller.
     * @param current_run The index of the current run.
     */
    protected void update_stats(int current_controller, int current_run) {
        absolute_errors[current_controller][current_run] = NumberComparator.absoluteError(collector.get_solution(), reference_solution);
        relative_errors[current_controller][current_run] = NumberComparator.relativeError(collector.get_solution(), reference_solution);
        significant_figures[current_controller][current_run] = NumberComparator.numSigFigs(collector.get_solution(), reference_solution, maximum_significant_figures, NumberComparator.SIGFIG.MINIMUM);
        average_significant_figures[current_controller][current_run] = NumberComparator.numSigFigs(collector.get_solution(), reference_solution, maximum_significant_figures, NumberComparator.SIGFIG.AVERAGE);
    }
}
