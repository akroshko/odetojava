/* ./tests/FunctionalityTest.java
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

import java.util.Vector;
import java.util.Arrays;

import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.scheme.IMEXESDIRKButcherTableau;
import ca.usask.simlab.odeToJava.scheme.ERKButcherTableau;
import ca.usask.simlab.odeToJava.odes.HiresODE;
import ca.usask.simlab.odeToJava.odes.LuoRudyODE;
import ca.usask.simlab.odeToJava.odes.OrbitArenstorfODE;
import ca.usask.simlab.odeToJava.odes.PleiadesODE;
import ca.usask.simlab.odeToJava.odes.PollutionODE;
import ca.usask.simlab.odeToJava.testSuite.Testable;

/**
 * Class that tests most of the functionality of ODEToJava using standard
 * well-proven methods on several well-known IVPs.
 */
public class FunctionalityTest {
    /**
     * The main method for the functionality tests.
     *
     * @param args The standard variable for holding command-line arguments.
     */ 
    public static void main(String[] args) throws Exception {
        Vector tol_orbit_euler_rtol = new Vector(Arrays.asList(1e-4, 1e-6));
        Vector tol_orbit_euler_atol = new Vector(Arrays.asList(1e-8, 1e-10));
        Vector stepsize_orbit = new Vector(Arrays.asList(1e-4, 3.16277e-5, 1e-5));
        Vector tol_orbit_rtol = new Vector(Arrays.asList(1e-4, 1e-6, 1e-8, 1e-10)); 
        Vector tol_orbit_atol = new Vector(Arrays.asList(1e-8, 1e-10, 1e-12, 1e-14));
        Vector tol_hires_rtol = new Vector(Arrays.asList(1e-5, 1e-6, 1e-7)); 
        Vector tol_hires_atol = new Vector(Arrays.asList(1e-5, 1e-6, 1e-7));

        Testable orbit = new Testable(new OrbitArenstorfODE(), "referenceSolutions/01orbitReference.txt");
        orbit.test_sd_ForwardEuler(tol_orbit_euler_rtol, 
                                 tol_orbit_euler_atol); 
        orbit.test_const(ERKButcherTableau.get_Heun2_tableau(),
                        stepsize_orbit); 
        orbit.test_sd(ERKButcherTableau.get_Heun2_tableau(),
                     tol_orbit_rtol,
                     tol_orbit_atol);   
        orbit.test_const_StormerVerlet(stepsize_orbit); 
        orbit.test_sd_StormerVerlet(tol_orbit_rtol,
                                  tol_orbit_atol);  
        orbit.test_const_RK4(stepsize_orbit); 
        orbit.test_sd_RK4(tol_orbit_rtol,
                        tol_orbit_atol);   
        orbit.test_sd_ARS232(tol_orbit_rtol,
                            tol_orbit_atol);
        orbit.test_embedded_KC43(tol_orbit_rtol, 
                          tol_orbit_atol);
        orbit.test_sd_KC43(tol_orbit_rtol, 
                         tol_orbit_atol);  
        orbit.test_embedded_Dopr(tol_orbit_rtol, 
                          tol_orbit_atol); 
        orbit.test_sd_Dopr(tol_orbit_rtol, 
                         tol_orbit_atol);  
        orbit.test_sd(IMEXESDIRKButcherTableau.get_BHR5531_tableau(),
                     tol_orbit_rtol,
                     tol_orbit_atol);    
        orbit.test_sd(IMEXESDIRKButcherTableau.get_BHR5532_tableau(),
                     tol_orbit_rtol,
                     tol_orbit_atol);     
        Testable hires = new Testable(new HiresODE(), "referenceSolutions/hiresReference.txt");
        hires.test_sd_ARS232(tol_hires_rtol, 
                            tol_hires_atol); 
        hires.test_embedded_KC43(tol_hires_rtol, 
                          tol_hires_atol);
        hires.test_sd_KC43(tol_hires_rtol, 
                         tol_hires_atol); 
        hires.test_embedded_Dopr(tol_hires_rtol, 
                          tol_hires_atol);
        Testable pleiades = new Testable(new PleiadesODE(), "referenceSolutions/pleiadesReference.txt");
        pleiades.test_sd_RK4(tol_orbit_rtol, 
                           tol_orbit_atol);       
        pleiades.test_embedded_Dopr(tol_orbit_rtol, 
                             tol_orbit_atol);
        pleiades.test_sd_Dopr(tol_orbit_rtol, 
                            tol_orbit_atol); 
        pleiades.test_embedded_KC43(tol_orbit_rtol, 
                             tol_orbit_atol);  
        Testable luorudy = new Testable(new LuoRudyODE(), "referenceSolutions/luorudyReference.txt");
        luorudy.test_sd_ARS232(tol_hires_rtol, 
                              tol_hires_atol);  
        luorudy.test_sd_RK4(tol_hires_rtol, 
                          tol_hires_atol); 
        luorudy.test_embedded_KC43(tol_hires_rtol, 
                            tol_hires_atol);
        luorudy.test_sd_KC43(tol_hires_rtol, 
                           tol_hires_atol);
        luorudy.test_embedded_Dopr(tol_hires_rtol, 
                            tol_hires_atol);
        luorudy.test_sd_Dopr(tol_hires_rtol, 
                           tol_hires_atol);  
    }
}
