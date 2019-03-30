/* ./tests/Cardiac.java
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
import ca.usask.simlab.odeToJava.odes.LuoRudyODE;
import ca.usask.simlab.odeToJava.testSuite.Testable;
import ca.usask.simlab.odeToJava.scheme.IMEXESDIRKButcherTableau;

/**
 * This class runs tests with the Luo-Rudy cardiac models in order to test the
 * IMEX methods.
 *
 * Tests all IMEX methods for accuracy on the mildly stiff Luo-Rudy model.
 */
public class Cardiac {
    /**
     * The main method for the cardiac tests.
     *
     * @param args The standard variable for holding command-line arguments.
     */
    public static void main(String[] args) throws Exception { 
        Vector RTOLS = new Vector(Arrays.asList(1e-3, 3.16277e-4, 1e-4, 3.16277e-5, 1e-5, 3.16277e-6, 1e-6));
        Vector ATOLS = new Vector(Arrays.asList(1e-3, 3.16277e-4, 1e-4, 3.16277e-5, 1e-5, 3.16277e-6, 1e-6));
        Testable luorudy = new Testable(new LuoRudyODE(), "referenceSolutions/luorudyReference.txt");
        // the forward Euler method
        luorudy.test_sd_ForwardEuler(RTOLS,
                                   ATOLS);
        // the Ascher, Ruuth, and Spiteri IMEX methods
        luorudy.test_sd(IMEXESDIRKButcherTableau.get_ARS121_tableau(),
                       RTOLS,
                       ATOLS);
        luorudy.test_sd(IMEXESDIRKButcherTableau.get_ARS122_tableau(),
                       RTOLS,
                       ATOLS);
        luorudy.test_sd_ARS232(RTOLS, 
                             ATOLS);   
        luorudy.test_sd(IMEXESDIRKButcherTableau.get_ARS233_tableau(),
                       RTOLS, 
                       ATOLS);    
        luorudy.test_sd(IMEXESDIRKButcherTableau.get_ARS222_tableau(),
                       RTOLS, 
                       ATOLS);    
        luorudy.test_sd(IMEXESDIRKButcherTableau.get_ARS343_tableau(),
                       RTOLS, 
                       ATOLS);  
        // luorudy.test_sd(IMEXESDIRKButcherTableau.get_ARS443_tableau(),
        //                RTOLS, 
        //                ATOLS); 
        // the Boscarino IMEX methods
        // do not finish their tests with given parameters
        // luorudy.test_sd(IMEXESDIRKButcherTableau.get_BHR5531_tableau(),
        //                RTOLS, 
        //                ATOLS);   
        // luorudy.test_sd(IMEXESDIRKButcherTableau.get_BHR5532_tableau(),
        //                RTOLS, 
        //                ATOLS);     
        // the Kennedy and Carpenter ARK methods
        luorudy.test_embedded(IMEXESDIRKButcherTableau.get_KC32_tableau(),
                        RTOLS, 
                        ATOLS);  
        luorudy.test_sd_KC43(RTOLS, 
                           ATOLS); 
        luorudy.test_embedded_KC43(RTOLS, 
                            ATOLS);   
        luorudy.test_embedded(IMEXESDIRKButcherTableau.get_KC54_tableau(),
                        RTOLS, 
                        ATOLS);  
        // the LIRK IMEX methods of Calvo, de Frutos, and Nova 
        luorudy.test_embedded(IMEXESDIRKButcherTableau.get_LIRK32_tableau(),
                        RTOLS, 
                        ATOLS);   
        luorudy.test_embedded(IMEXESDIRKButcherTableau.get_LIRK43_tableau(),
                        RTOLS, 
                        ATOLS);
        // the Dormand-Prince 5(4) ERK method for comparison 
        luorudy.test_embedded_Dopr(RTOLS,
                            ATOLS);
    }
} 
