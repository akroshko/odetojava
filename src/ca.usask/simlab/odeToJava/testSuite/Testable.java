/* ./testSuite/Testable.java
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

import java.util.Vector;
import java.util.Arrays;
import org.jscience.mathematics.vectors.Float64Vector;
import org.jscience.mathematics.numbers.Float64;
import ca.usask.simlab.odeToJava.scheme.Scheme;
import ca.usask.simlab.odeToJava.scheme.ERKButcherTableau;
import ca.usask.simlab.odeToJava.scheme.IMEXESDIRKButcherTableau;
import ca.usask.simlab.odeToJava.controller.IVP;
import ca.usask.simlab.odeToJava.controller.IVPController;
import ca.usask.simlab.odeToJava.ode.RHS;

/**
 * Actually run tests with some standard methods and classes.
 */
public class Testable {
    /**
     * The ODE being solved.
     */  
    protected RHS ode;
    /**
     * A path to the reference solution for the IVP being solved..
     */  
    protected String reference_solution;
    /**
     * The default set of relative tolerances to use.
     */   
    protected Vector DEFAULT_RTOL = new Vector(Arrays.asList(1e-3, 1e-6, 1e-9, 1e-12));
    /**
     * The default set of absolute tolerances to use.
     */  
    protected Vector DEFAULT_ATOL = new Vector(Arrays.asList(1e-6, 1e-9, 1e-12, 1e-15)); 
    /**
     * The default set of constant stepsizes to use.
     */   
    protected Vector DEFAULT_STEPSIZES = new Vector(Arrays.asList(1e-2, 1e-3, 1e-4)); 

    /**
     * The default constructor.
     *
     * @param ode The ODE object giving the ODE to solve.
     * @param reference_solution The path to the reference solution.  
     */  
    public Testable(RHS ode, String reference_solution) {
        this.ode = ode;
        this.reference_solution = reference_solution;
    }

    /**
     * Test the IVP with Stormer-Verlet and the default set of stepsizes.
     */     
    public void test_const_ForwardEuler() throws Exception {
        test_const_ForwardEuler(null);
    } 
 
    /**
     * Test the IVP with forward Euler and a specified set of stepsizes.
     */    
    public void test_const_StormerVerlet() throws Exception {
        test_const_StormerVerlet(null);
    } 

    /**
     * Test the IVP with RK4 and the default set of stepsizes.
     */     
    public void test_const_RK4() throws Exception {
        test_const(ERKButcherTableau.get_RK4_tableau(), null);
    }

    /**
     * Test the IVP with RK4 and a specified set of stepsizes.
     */     
    public void test_const_RK4(Vector stepsizes) throws Exception {
        test_const(ERKButcherTableau.get_RK4_tableau(), stepsizes);
    }
 
    /**
     * Test the IVP with Dormand-Prince and the default set of stepsizes.
     */     
    public void test_const_Dopr() throws Exception {
        test_const(ERKButcherTableau.get_DormandPrince54_tableau(), null);
    }

    /**
     * Test the IVP with Dormand-Prince and a specified set of stepsizes.
     */     
    public void test_const_Dopr(Vector stepsizes) throws Exception {
        test_const(ERKButcherTableau.get_DormandPrince54_tableau(), stepsizes);
    }

    /**
     * Test the IVP with ARS 232 and the default set of stepsizes.
     */     
    public void test_const_ARS232() throws Exception {
        test_const(IMEXESDIRKButcherTableau.get_ARS232_tableau(), null);
    }

    /**
     * Test the IVP with ARS 232 and a specified set of stepsizes.
     */     
    public void test_const_ARS232(Vector stepsizes) throws Exception {
        test_const(IMEXESDIRKButcherTableau.get_ARS232_tableau(), stepsizes);
    } 

    /**
     * Test the IVP with KC 4(3) and the default set of stepsizes.
     */     
    public void test_const_KC43() throws Exception {
        test_const(IMEXESDIRKButcherTableau.get_KC43_tableau(), null);
    }

    /**
     * Test the IVP with KC 4(3) and a specified set of stepsizes.
     */     
    public void test_const_KC43(Vector stepsizes) throws Exception {
        test_const(IMEXESDIRKButcherTableau.get_KC43_tableau(), stepsizes);
    }
    
    /**
     * Test the IVP with a series of constant stepsizes.
     *
     * @param tableau The Butcher tableau that defines the method to use for the tests.
     * @param stepsizes An array of stepsizes to use.
     */    
    public void test_const(Scheme tableau, Vector stepsizes) throws Exception {
        SolutionTester tests = new SolutionTester(reference_solution);
        IVP ivp = new IVP(ode, tests.get_initial_time(), tests.get_initial_values());

        IVPController controller = prepareConstController(tests, ivp, tests.get_final_time());
        if (tableau != null) {
            controller.set_butcher_tableau(tableau);
        }
        tests = add_IVPControllerConstant(tests, controller, stepsizes);
        tests.output_test_stats(System.out);
        tests.run();
    }

    /**
     * Test the IVP with forward Euler along with a series of constant stepsizes.
     *
     * @param stepsizes An array of stepsizes to use.
     */    
    public void test_const_ForwardEuler(Vector stepsizes) throws Exception {
        SolutionTester tests = new SolutionTester(reference_solution);
        IVP ivp = new IVP(ode, tests.get_initial_time(), tests.get_initial_values());

        IVPController controller = prepareConstController(tests, ivp, tests.get_final_time());
        controller.set_forward_euler();
        tests = add_IVPControllerConstant(tests, controller, stepsizes);
        tests.output_test_stats(System.out);
        tests.run();
    }

    /**
     * Test the IVP with Stormer-Verlet along with a series of constant stepsizes.
     *
     * @param stepsizes An array of stepsizes to use.
     */    
    public void test_const_StormerVerlet(Vector stepsizes) throws Exception {
        SolutionTester tests = new SolutionTester(reference_solution);
        IVP ivp = new IVP(ode, tests.get_initial_time(), tests.get_initial_values());

        IVPController controller = prepareConstController(tests, ivp, tests.get_final_time());
        controller.set_stormer_verlet();
        tests = add_IVPControllerConstant(tests, controller, stepsizes);
        tests.output_test_stats(System.out);
        tests.run();
    }

    private IVPController prepareConstController(SolutionTester tests, IVP ivp, Float64 final_time) {
        IVPController controller = new IVPController(ivp, final_time);
        controller.set_no_error_control();
        controller.write_at_array(tests.get_solution_times()); 
        return controller;
    }

    private SolutionTester add_IVPControllerConstant(SolutionTester tests, IVPController controller, Vector stepsizes) {
        if (stepsizes != null) {
            tests.add_IVPController(controller, null, null, stepsizes);
        } else {
            tests.add_IVPController(controller, null, null, DEFAULT_STEPSIZES);
        } 
        return tests;
    }

    /**
     * Test the IVP with embedded error-estimation and the specified tableau, the default set of tolerances
     * and initial stepsizes.
     *
     * @param tableau The embedded Butcher tableau that defines the method to use for the tests.
     */      
    public void test_embedded(Scheme tableau, Vector stepsizes) throws Exception {
        test_embedded(tableau, null, null, stepsizes);
    }

    /**
     * Test the IVP with embedded error-estimation and the specified tableau, the default set of tolerances
     * and initial-stepsize selection.
     *
     * @param tableau The embedded Butcher tableau that defines the method to use for the tests.
     */      
    public void test_embedded(Scheme tableau) throws Exception {
        test_embedded(tableau, null, null, null);
    }
     
    /**
     * Test the IVP with embedded error-estimation and the specified tableau and tolerances and the default
     * set of initial-stepsize selection.
     *
     * @param tableau The embedded Butcher tableau that defines the method to use for the tests.
     * @param rtols An array of relative tolerances.
     * @param atols An array of absolute tolerances.
     */     
    public void test_embedded(Scheme tableau, Vector rtols, Vector atols) throws Exception {
        test_embedded(tableau, rtols, atols, null);
    }

    /**
     * Test the IVP with embedded error-estimation and the Dormand-Prince tableau along with the default set
     * of tolerances and initial-stepsize selection.
     */       
    public void test_embedded_Dopr()  throws Exception {
        test_embedded(ERKButcherTableau.get_DormandPrince54_tableau(), null, null, null);
    }
 
    /**
     * Test the IVP with embedded error-estimation and the Dormand-Prince tableau along with a specified set
     * of tolerances and initial-stepsize selection.
     *
     * @param rtols An array of relative tolerances.
     * @param atols An array of absolute tolerances.
     */       
    public void test_embedded_Dopr(Vector rtols, Vector atols)  throws Exception {
        test_embedded(ERKButcherTableau.get_DormandPrince54_tableau(), rtols, atols, null);
    }
 
    /**
     * Test the IVP with embedded error-estimation and the Dormand-Prince tableau along with a specified set
     * of tolerances and initial stepsizes.
     *
     * @param rtols An array of relative tolerances.
     * @param atols An array of absolute tolerances.
     * @param stepsizes An array of initial stepsizes.
     */       
    public void test_embedded_Dopr(Vector rtols, Vector atols, Vector stepsizes)  throws Exception {
        test_embedded(ERKButcherTableau.get_DormandPrince54_tableau(), rtols, atols, stepsizes);
    }

    /**
     * Test the IVP with embedded error-estimation and the KC 4(3) tableau along with the default
     * tolerances and initial stepsize-selection.
     *
     */       
    public void test_embedded_KC43()  throws Exception {
        test_embedded(IMEXESDIRKButcherTableau.get_KC43_tableau(), null, null, null);
    }

    /**
     * Test the IVP with embedded error-estimation and the KC 4(3) tableau along with a specified set
     * of tolerances and initial stepsize-selection.
     *
     * @param rtols An array of relative tolerances.
     * @param atols An array of absolute tolerances.
     */       
    public void test_embedded_KC43(Vector rtols, Vector atols)  throws Exception {
        test_embedded(IMEXESDIRKButcherTableau.get_KC43_tableau(), rtols, atols, null);
    }
 
    /**
     * Test the IVP with embedded error-estimation and the KC 4(3) tableau along with a specified set
     * of tolerances and initial stepsizes.
     *
     * @param rtols An array of relative tolerances.
     * @param atols An array of absolute tolerances.
     * @param stepsizes An array of initial stepsizes.
     */       
    public void test_embedded_KC43(Vector rtols, Vector atols, Vector stepsizes)  throws Exception {
        test_embedded(IMEXESDIRKButcherTableau.get_KC43_tableau(), rtols, atols, stepsizes);
    }

    /**
     * Test the IVP with a series of tolerances using embedded error estimation.
     *
     * @param tableau The Butcher tableau that defines the method to use for the tests.
     * @param rtols An array of relative tolerances.
     * @param atols An array of absolute tolerances.
     * @param stepsizes An array of initial stepsizes or null for initial
     *                  stepsize selection..
     */               
    public void test_embedded(Scheme tableau, Vector rtols, Vector atols, Vector stepsizes) throws Exception {
        SolutionTester tests = new SolutionTester(reference_solution);
        IVP ivp = new IVP(ode, tests.get_initial_time(), tests.get_initial_values());
        if (rtols == null) { 
            rtols = DEFAULT_RTOL;
        }
        if (atols == null) { 
            atols = DEFAULT_ATOL;

        }
        
        IVPController controller = new IVPController(ivp, tests.get_final_time());
        controller.set_emb_error_control();
        controller.write_at_array(tests.get_solution_times()); 
        controller.set_butcher_tableau(tableau);
        if (stepsizes == null) {
            controller.use_initial_stepsize_selection();
            tests.add_IVPController(controller, rtols, atols, null);
        } else {
            tests.add_IVPController(controller, rtols, atols, stepsizes);
        }
        tests.output_test_stats(System.out);
        tests.run();
    }

    /**
     * Test the IVP with the specified tableau, the default set of tolerances
     * and initial stepsize selection.
     *
     * @param tableau The embedded Butcher tableau that defines the method to use for the tests.
     */      
    public void test_sd(Scheme tableau) throws Exception {
        test_sd(tableau, null, null, null );
    }
    
    /**
     * Test the IVP with the specified tableau and tolerances and the default
     * set of initial stepsize selection.
     *
     * @param tableau The embedded Butcher tableau that defines the method to use for the tests.
     * @param rtols An array of relative tolerances.
     * @param atols An array of absolute tolerances.
     */     
    public void test_sd(Scheme tableau, Vector rtols, Vector atols) throws Exception {
        test_sd(tableau, rtols, atols, null);
    } 

    /**
     * Test the IVP with step-doubling error-estimation and the forward Euler
     * tableau along with a specified set of tolerances and initial stepsize
     * selection.
     */       
    public void test_sd_ForwardEuler()  throws Exception {
        test_sd_ForwardEuler(null, null, null);
    }
  
    /**
     * Test the IVP with step-doubling error-estimation and the forward Euler
     * tableau along with a specified set of tolerances and initial stepsize
     * selection.
     *
     * @param rtols An array of relative tolerances.
     * @param atols An array of absolute tolerances.
     */       
    public void test_sd_ForwardEuler(Vector rtols, Vector atols)  throws Exception {
        test_sd_ForwardEuler(rtols, atols, null);
    }
 
    /**
     * Test the IVP with step-doubling error-estimation and the Stormer-Verlet
     * tableau along with a specified set of tolerances and initial stepsize
     * selection.
     *
     */       
    public void test_sd_StormerVerlet()  throws Exception {
        test_sd_StormerVerlet(null, null, null);
    }
 

    /**
     * Test the IVP with step-doubling error-estimation and the Stormer-Verlet
     * tableau along with a specified set of tolerances and initial stepsize
     * selection.
     *
     * @param rtols An array of relative tolerances.
     * @param atols An array of absolute tolerances.
     */       
    public void test_sd_StormerVerlet(Vector rtols, Vector atols)  throws Exception {
        test_sd_StormerVerlet(rtols, atols, null);
    }
 
    /**
     * Test the IVP with step-doubling error-estimation and the IMEX 232 tableau
     * along with a specified set of tolerances and initial stepsize selection.
     *
     */       
    public void test_sd_ARS232()  throws Exception {
        test_sd(IMEXESDIRKButcherTableau.get_ARS232_tableau(), null, null, null);
    } 

    /**
     * Test the IVP with step-doubling error-estimation and the IMEX 232 tableau
     * along with a specified set of tolerances and initial stepsize selection.
     *
     * @param rtols An array of relative tolerances.
     * @param atols An array of absolute tolerances.
     */       
    public void test_sd_ARS232(Vector rtols, Vector atols)  throws Exception {
        test_sd(IMEXESDIRKButcherTableau.get_ARS232_tableau(), rtols, atols, null);
    }
 
    /**
     * Test the IVP with step-doubling error-estimation and the IMEX 232 tableau
     * along with a specified set of tolerances and initial stepsizes.
     *
     * @param rtols An array of relative tolerances.
     * @param atols An array of absolute tolerances.
     * @param stepsizes An array of initial stepsizes.
     */       
    public void test_sd_ARS232(Vector rtols, Vector atols, Vector stepsizes)  throws Exception {
        test_sd(IMEXESDIRKButcherTableau.get_ARS232_tableau(), rtols, atols, stepsizes);
    }


    /**
     * Test the IVP with step-doubling error-estimation and "the" Runge-Kutta
     * order 4 tableau along with a specified set of tolerances and initial
     * stepsize selection.
     */       
    public void test_sd_RK4() throws Exception {
        test_sd(ERKButcherTableau.get_RK4_tableau(), null, null, null);
    }
 
    /**
     * Test the IVP with step-doubling error-estimation and "the" Runge-Kutta
     * order 4 tableau along with a specified set of tolerances and initial
     * stepsize selection.
     *
     * @param rtols An array of relative tolerances.
     * @param atols An array of absolute tolerances.
     */       
    public void test_sd_RK4(Vector rtols, Vector atols)  throws Exception {
        test_sd(ERKButcherTableau.get_RK4_tableau(), rtols, atols, null);
    }
 
    /**
     * Test the IVP with step-doubling error-estimation and the Dormand-Prince
     * tableau along with a specified set of tolerances and initial stepsize-selection.
     *
     */       
    public void test_sd_Dopr()  throws Exception {
        test_sd(ERKButcherTableau.get_DormandPrince54_tableau(), null, null, null);
    }
 
    /**
     * Test the IVP with step-doubling error-estimation and the Dormand-Prince tableau along with
     * a specified set of tolerances and initial stepsize-selection.
     *
     * @param rtols An array of relative tolerances.
     * @param atols An array of absolute tolerances.
     */       
    public void test_sd_Dopr(Vector rtols, Vector atols)  throws Exception {
        test_sd(ERKButcherTableau.get_DormandPrince54_tableau(), rtols, atols, null);
    }
 
    /**
     * Test the IVP with step-doubling error-estimation and the Dormand-Prince tableau along with
     * a specified set of tolerances and initial stepsizes.
     *
     * @param rtols An array of relative tolerances.
     * @param atols An array of absolute tolerances.
     * @param stepsizes An array of initial stepsizes.
     */       
    public void test_sd_Dopr(Vector rtols, Vector atols, Vector stepsizes)  throws Exception {
        test_sd(ERKButcherTableau.get_DormandPrince54_tableau(), rtols, atols, stepsizes);
    }

    /**
     * Test the IVP with step-doubling error-estimation and the KC43 tableau along with the default
     * tolerances.
     *
     */       
    public void test_sd_KC43()  throws Exception {
        test_sd(IMEXESDIRKButcherTableau.get_KC43_tableau(), null, null, null);
    } 

    /**
     * Test the IVP with step-doubling error-estimation and the KC43 tableau along with a specified set
     * of tolerances and initial stepsize-selection.
     *
     * @param rtols An array of relative tolerances.
     * @param atols An array of absolute tolerances.
     */       
    public void test_sd_KC43(Vector rtols, Vector atols)  throws Exception {
        test_sd(IMEXESDIRKButcherTableau.get_KC43_tableau(), rtols, atols, null);
    }
 
    /**
     * Test the IVP with step-doubling error-estimation and the KC43 tableau along with a specified set
     * of tolerances and initial stepsizes.
     *
     * @param rtols An array of relative tolerances.
     * @param atols An array of absolute tolerances.
     * @param stepsizes An array of initial stepsizes.
     */       
    public void test_sd_KC43(Vector rtols, Vector atols, Vector stepsizes)  throws Exception {
        test_sd(IMEXESDIRKButcherTableau.get_KC43_tableau(), rtols, atols, stepsizes);
    }

    /**
     * Test the IVP with a series of tolerances using step-doubling error estimation.
     *
     * @param tableau The Butcher tableau that defines the method to use for the tests.
     * @param rtols An array of relative tolerances.
     * @param atols An array of absolute tolerances.
     * @param stepsizes An array of initial stepsizes or null for initial
     *                  stepsize selection..
     */              
    public void test_sd(Scheme tableau, Vector rtols, Vector atols, Vector stepsizes) throws Exception {
        SolutionTester tests = new SolutionTester(reference_solution);
        IVP ivp = new IVP(ode, tests.get_initial_time(), tests.get_initial_values());
        
        IVPController controller = prepareSdController(tests, ivp, tests.get_final_time()); 
        controller.set_butcher_tableau(tableau);
        if (stepsizes == null) {
            controller.use_initial_stepsize_selection();
            tests.add_IVPController(controller, rtols, atols, null);
        } else {
            tests.add_IVPController(controller, rtols, atols, stepsizes);
        } 
        // need different tolerances
        tests.output_test_stats(System.out);
        tests.run();
    }

    /**
     * Test the IVP using forward Euler with a series of tolerances using step-doubling error estimation.
     *
     * @param rtols An array of relative tolerances.
     * @param atols An array of absolute tolerances.
     * @param stepsizes An array of initial stepsizes or null for initial
     *                  stepsize selection.
     */              
    public void test_sd_ForwardEuler(Vector rtols, Vector atols, Vector stepsizes) throws Exception {
        SolutionTester tests = new SolutionTester(reference_solution);

        IVP ivp = new IVP(ode, tests.get_initial_time(), tests.get_initial_values());
        IVPController controller = prepareSdController(tests, ivp, tests.get_final_time()); 

        controller.set_forward_euler();
        tests = add_IVPControllerSd(tests, controller, rtols, atols, stepsizes);
        // need different tolerances
        tests.run();
    } 

    /**
     * Test the IVP using Stormer-Verlet with a series of tolerances using step-doubling error estimation.
     *
     * @param rtols An array of relative tolerances.
     * @param atols An array of absolute tolerances.
     * @param stepsizes An array of initial stepsizes or null for initial
     *                  stepsize selection..
     */              
    public void test_sd_StormerVerlet(Vector rtols, Vector atols, Vector stepsizes) throws Exception {
        SolutionTester tests = new SolutionTester(reference_solution);
        IVP ivp = new IVP(ode, tests.get_initial_time(), tests.get_initial_values());
        
        IVPController controller = prepareSdController(tests, ivp, tests.get_final_time()); 
        controller.set_stormer_verlet();
        tests = add_IVPControllerSd(tests, controller, rtols, atols, stepsizes);
        tests.run();
    } 

    private IVPController prepareSdController(SolutionTester tests, IVP ivp, Float64 final_time) {
        IVPController controller = new IVPController(ivp, final_time);
        controller.set_sd_error_control();
        controller.write_at_array(tests.get_solution_times()); 
        return controller;
    }

    private SolutionTester add_IVPControllerSd(SolutionTester tests, IVPController controller, Vector rtols, Vector atols, Vector stepsizes) {
        if (stepsizes == null) {
            controller.use_initial_stepsize_selection();
            tests.add_IVPController(controller, rtols, atols, null);
        } else {
            tests.add_IVPController(controller, rtols, atols, stepsizes);
        } 
        tests.output_test_stats(System.out); 
        return tests;
    }
}

