/* ./tests/PollutionTest.java
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

import ca.usask.simlab.odeToJava.controller.IVP;
import ca.usask.simlab.odeToJava.controller.IVPController;
import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.odes.PollutionODE;
import ca.usask.simlab.odeToJava.testSuite.Testable;
import ca.usask.simlab.odeToJava.scheme.IMEXESDIRKButcherTableau;

/**
 * This class runs the tests on the air pollution problem.
 */
public class PollutionTest {
    /**
     * The main method for the air pollution problem. 
     *
     * @param args The standard variable for holding command-line arguments.
     */
    public static void main(String[] args) throws Exception { 
        Vector RTOLS_LOW = new Vector(Arrays.asList(1e-4, 3.16277e-5, 1e-5, 3.16277e-6, 1e-6, 3.16277e-7, 1e-7));
        Vector ATOLS_LOW = new Vector(Arrays.asList(1e-4, 3.16277e-5, 1e-5, 3.16277e-6, 1e-6, 3.16277e-7, 1e-7)); 
        Vector RTOLS = new Vector(Arrays.asList(1e-4, 3.16277e-5, 1e-5, 3.16277e-6, 1e-6, 3.16277e-7, 1e-7, 3.16277e-8, 1e-8, 3.16277e-9, 1e-9, 3.16277e-10, 1e-10));
        Vector ATOLS = new Vector(Arrays.asList(1e-4, 3.16277e-5, 1e-5, 3.16277e-6, 1e-6, 3.16277e-7, 1e-7, 3.16277e-8, 1e-8, 3.16277e-9, 1e-9, 3.16277e-10, 1e-10));
        Testable pollution = new Testable(new PollutionODE(), "referenceSolutions/pollutionReference.txt");
        // the Ascher, Ruuth, and Spiteri IMEX methods
        pollution.test_sd(IMEXESDIRKButcherTableau.get_ARS121_tableau(),
                         RTOLS_LOW,
                         ATOLS_LOW); 
        pollution.test_sd(IMEXESDIRKButcherTableau.get_ARS122_tableau(),
                         RTOLS_LOW,
                         ATOLS_LOW); 
        pollution.test_sd_ARS232(RTOLS, 
                               ATOLS);   
        pollution.test_sd(IMEXESDIRKButcherTableau.get_ARS233_tableau(),
                         RTOLS, 
                         ATOLS);    
        pollution.test_sd(IMEXESDIRKButcherTableau.get_ARS222_tableau(),
                         RTOLS, 
                         ATOLS);      
        pollution.test_sd(IMEXESDIRKButcherTableau.get_ARS343_tableau(),
                         RTOLS, 
                         ATOLS);  
        pollution.test_sd(IMEXESDIRKButcherTableau.get_ARS443_tableau(),
                         RTOLS, 
                         ATOLS); 
        // the Boscarino IMEX methods
        pollution.test_sd(IMEXESDIRKButcherTableau.get_BHR5531_tableau(),
                         RTOLS, 
                         ATOLS);   
        pollution.test_sd(IMEXESDIRKButcherTableau.get_BHR5532_tableau(),
                         RTOLS, 
                         ATOLS);  
        // the Kennedy and Carpenter ARK methods
        pollution.test_embedded(IMEXESDIRKButcherTableau.get_KC32_tableau(),
                          RTOLS, 
                          ATOLS);
        pollution.test_sd_KC43(RTOLS, 
                             ATOLS);
        pollution.test_embedded_KC43(RTOLS, 
                              ATOLS);  
        pollution.test_embedded(IMEXESDIRKButcherTableau.get_KC54_tableau(),
                          RTOLS, 
                          ATOLS);   
        // the LIRK IMEX methods of Calvo, de Frutos, and Nova 
        pollution.test_embedded(IMEXESDIRKButcherTableau.get_LIRK32_tableau(),
                          RTOLS, 
                          ATOLS);   
        pollution.test_embedded(IMEXESDIRKButcherTableau.get_LIRK43_tableau(),
                          RTOLS, 
                          ATOLS); 
    }
}
