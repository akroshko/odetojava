/* ./tests/OrbitTest.java
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
package ca.usask.simlab.odeToJava.tests;

import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import org.jscience.mathematics.numbers.Float64;
import ca.usask.simlab.odeToJava.scheme.IMEXESDIRKButcherTableau;
import ca.usask.simlab.odeToJava.scheme.ERKButcherTableau;
import ca.usask.simlab.odeToJava.scheme.StormerVerletTableau;
import ca.usask.simlab.odeToJava.odes.OrbitArenstorfODE;
import ca.usask.simlab.odeToJava.odes.PleiadesODE;
import ca.usask.simlab.odeToJava.nonstiffDETest.C5;
import ca.usask.simlab.odeToJava.nonstiffDETest.D1;
import ca.usask.simlab.odeToJava.nonstiffDETest.D2;
import ca.usask.simlab.odeToJava.nonstiffDETest.D3;
import ca.usask.simlab.odeToJava.nonstiffDETest.D4;
import ca.usask.simlab.odeToJava.nonstiffDETest.D5;
import ca.usask.simlab.odeToJava.modules.errorControl.EmbErrControllerModule;
import ca.usask.simlab.odeToJava.modules.errorControl.EmbErrEstimatorModule;
import ca.usask.simlab.odeToJava.modules.errorControl.InitialStepSizeSelectorModule;
import ca.usask.simlab.odeToJava.modules.errorControl.SdErrControllerModule;
import ca.usask.simlab.odeToJava.modules.errorControl.SdErrEstimatorModule;
import ca.usask.simlab.odeToJava.modules.io.AllSolutionWriterModule;
import ca.usask.simlab.odeToJava.modules.io.ProgressReporterModule;
import ca.usask.simlab.odeToJava.modules.io.writers.DiskWriter;
import ca.usask.simlab.odeToJava.modules.scheme.IMEXESDIRKModule;
import ca.usask.simlab.odeToJava.modules.scheme.ERKModule;
import ca.usask.simlab.odeToJava.modules.scheme.ForwardEulerModule;
import ca.usask.simlab.odeToJava.modules.scheme.StormerVerletArenstorfOrbitModule;
import ca.usask.simlab.odeToJava.modules.scheme.StormerVerletVariableStepsizeArenstorfOrbitModule;
import ca.usask.simlab.odeToJava.ode.RHS;
import ca.usask.simlab.odeToJava.solver.PropertySolver;
import ca.usask.simlab.odeToJava.solver.ConstantStepSolver;
import ca.usask.simlab.odeToJava.solver.SymmetricVariableStepsizeSolver;
import ca.usask.simlab.odeToJava.solver.EmbErrSolver;
import ca.usask.simlab.odeToJava.solver.SdErrSolver;
import ca.usask.simlab.odeToJava.controller.IVP;
import ca.usask.simlab.odeToJava.controller.IVPController;
import ca.usask.simlab.odeToJava.testSuite.SolutionTester;
import ca.usask.simlab.odeToJava.testSuite.Testable;

/**
 * Run the celestial mechanics in ODEToJava.
 */
public class OrbitTest {
    static RHS ode;
    static double[] initial_values;
    static double initial_time;
    static double final_time;
    static double atol;
    static double rtol;

    /**
     * The main method for testing celestial mechanics problems.
     *
     * @param args The standard variable for holding command-line arguments.
     */
    public static void main(String args[]) throws Exception {
        Vector euler_rtol = new Vector(Arrays.asList(1e-4, 1e-6));
        Vector euler_atol = new Vector(Arrays.asList(1e-8, 1e-10));
        Vector rtol = new Vector(Arrays.asList(1e-4, 1e-6, 1e-8, 1e-10));
        Vector atol = new Vector(Arrays.asList(1e-8, 1e-10, 1e-12, 1e-14));
        Vector hp_rtol = new Vector(Arrays.asList(1e-8, 1e-10, 1e-12, 1e-14));
        Vector hp_atol = new Vector(Arrays.asList(1e-8, 1e-10, 1e-12, 1e-14));
        Vector stepsize_orbit = new Vector(Arrays.asList(1e-4, 3.16277e-5, 1e-5, 3.16277e-6, 1e-6));
        Testable c5 = new Testable(new C5(), "referenceSolutions/nonstiffC5Reference.txt");
        Testable d1 = new Testable(new D1(), "referenceSolutions/nonstiffD1Reference.txt");
        Testable d2 = new Testable(new D2(), "referenceSolutions/nonstiffD2Reference.txt");
        Testable d3 = new Testable(new D3(), "referenceSolutions/nonstiffD3Reference.txt");
        Testable d4 = new Testable(new D4(), "referenceSolutions/nonstiffD4Reference.txt");
        Testable d5 = new Testable(new D5(), "referenceSolutions/nonstiffD5Reference.txt");
        Testable pleiades = new Testable(new PleiadesODE(), "referenceSolutions/pleiadesReference.txt");
        String orbit_number;
        if (args.length == 0 || args[0].equals("${arg0}")) {
            orbit_number = "01";
        } else {
            orbit_number = args[0];
        }
        Testable arenstorf = new Testable(new OrbitArenstorfODE(), "referenceSolutions/" + orbit_number + "OrbitReference.txt");

        Testable orbit;
        Vector<Testable> orbits = new Vector(Arrays.asList(c5, d1, d2, d3, d4, d5, pleiades, arenstorf));

        for (int i = 0; i < 8; i++) {
            if (i != 7) {
                continue;
            }
            orbit = orbits.get(i);
            orbit.test_sd_ForwardEuler(euler_rtol,
                                       euler_atol);
            // orbit.test_const(ERKButcherTableau.get_Heun2_tableau(),stepsize_orbit);
            // use a special Stormer-Verlet method for the 3-body Arenstorf orbit
            if (i == 7) {
                SolutionTester tests;
                IVP ivp;
                IVPController controller;

                // create a controller for using the special Stormer-Verlet method
                // XXX this a bit of a hacky way to do this, depends on
                // the current ANT build script
                tests = new SolutionTester("referenceSolutions/" + orbit_number + "OrbitReference.txt");
                // XXX OrbitArenstorfODE not actually used at all but just here
                //     for reference
                ivp = new IVP(new OrbitArenstorfODE(), tests.get_initial_time(), tests.get_initial_values());
                controller = new IVPController(ivp, tests.get_final_time());
                controller.set_no_error_control();
                controller.set_butcher_tableau(new StormerVerletTableau());
                // add the special module
                controller.add_module(new StormerVerletArenstorfOrbitModule());
                controller.write_at_array(tests.get_solution_times());
                tests.add_IVPController(controller, null, null, stepsize_orbit);
                tests.output_test_stats(System.out);
                tests.run();

                // create a controller for using the variable-step Stormer-Verlet method
                // XXX use the following to loops to examine the data in the paper                    
                for (int j = 0; j < 5; j++) {
                    for (int k = 0; k < 5; k++) { 
//              for (int j = 0; j < 21; j++) {
//                  for (int k = 0; k < 21; k++) {
//              for (int j = 0; j < 11; j++) {
//                  for (int k = 0; k < 21; k++) {
                        // Vector<Double> alphas = new Vector(Arrays.asList(0.5,0.6,0.7,0.8,0.9,1.0,1.1,1.2,1.3,1.4,1.5));
                        // Vector<Double> alphas = new Vector(Arrays.asList(0.7,0.71,0.72,0.73,0.74,0.75,0.76,0.77,0.78,0.79,0.8,0.81,0.82,0.83,0.84,0.85,0.86,0.87,0.88,0.89,0.9));
                        // Vector<Double> epsilons = new Vector(Arrays.asList(9.999999999999999547e-07, 7.943282347242821957e-07, 6.309573444801929638e-07, 5.011872336272724970e-07, 3.981071705534969225e-07, 3.162277660168379191e-07, 2.511886431509582273e-07, 1.995262314968878707e-07, 1.584893192461114080e-07, 1.258925411794166167e-07, 9.999999999999999547e-08, 7.943282347242821957e-08, 6.309573444801929638e-08, 5.011872336272724970e-08, 3.981071705534968960e-08, 3.162277660168379191e-08, 2.511886431509582074e-08, 1.995262314968878641e-08, 1.584893192461114279e-08, 1.258925411794166101e-08, 1.000000000000000021e-08));
                        // Vector<Double> epsilons = new Vector(Arrays.asList(9.999999999999999547e-08));
                        Vector<Double> alphas = new Vector(Arrays.asList(0.7,0.75,0.8,0.85,0.9));
                        Vector<Double> epsilons = new Vector(Arrays.asList(9.999999999999999547e-07, 3.162277660168379191e-07, 9.999999999999999547e-08, 3.162277660168379191e-08, 1.000000000000000021e-08));
                        System.out.format("alpha = %f, epsilon = %e\n", alphas.get(j), epsilons.get(k));
                        Vector variable_initial_stepsize = new Vector(Arrays.asList(epsilons.get(k)));
                        // XXX this a bit of a hacky way to do this, depends on
                        // the current ANT build script
                        tests = new SolutionTester("referenceSolutions/Hamiltonian" + orbit_number + "OrbitReference.txt");
                        ivp = new IVP(new OrbitArenstorfODE(), tests.get_initial_time(), tests.get_initial_values());
                        controller = new IVPController(ivp, tests.get_final_time());
                        PropertySolver solver = new SymmetricVariableStepsizeSolver();
                        solver.add_solver_module(new StormerVerletVariableStepsizeArenstorfOrbitModule(epsilons.get(k),
                                                                                                       alphas.get(j)));
                        controller.set_custom_solver(solver);
                        controller.set_butcher_tableau(new StormerVerletTableau());
                        // add the special module
                        controller.write_at_array(tests.get_solution_times());
                        tests.add_IVPController(controller, null, null, variable_initial_stepsize);
                        tests.output_test_stats(System.out);
                        tests.run();
                    }
                }
            } else {
                orbit.test_const_StormerVerlet(stepsize_orbit);
            }

            orbit.test_sd(ERKButcherTableau.get_Runge2_tableau(),
                          rtol,
                          atol);
            orbit.test_sd(ERKButcherTableau.get_Heun2_tableau(),
                          rtol,
                          atol);
            orbit.test_sd(ERKButcherTableau.get_SSP22_tableau(),
                          rtol,
                          atol);
            orbit.test_sd(ERKButcherTableau.get_VDH_tableau(),
                          rtol,
                          atol);
            orbit.test_sd(ERKButcherTableau.get_Heun3_tableau(),
                          rtol,
                          atol);
            orbit.test_sd(ERKButcherTableau.get_Runge3_tableau(),
                          rtol,
                          atol);
            orbit.test_sd(ERKButcherTableau.get_SSP32_tableau(),
                          rtol,
                          atol);
            orbit.test_sd(ERKButcherTableau.get_RK4_tableau(),
                          rtol,
                          atol);
            orbit.test_sd(ERKButcherTableau.get_three_eighths_tableau(),
                          rtol,
                          atol);
            orbit.test_embedded(ERKButcherTableau.get_RKMerson43_tableau(),
                                rtol,
                                atol);
            orbit.test_embedded(ERKButcherTableau.get_RKZonneveld43_tableau(),
                                rtol,
                                atol);
            orbit.test_embedded(ERKButcherTableau.get_RKF45_tableau(),
                                rtol,
                                atol);
            orbit.test_embedded_Dopr(rtol,
                                     atol);
            orbit.test_embedded(ERKButcherTableau.get_Verner65_tableau(),
                                hp_rtol,
                                hp_atol);
            orbit.test_embedded(ERKButcherTableau.get_RKF78_tableau(),
                                hp_rtol,
                                hp_atol);
        }
    }
}
