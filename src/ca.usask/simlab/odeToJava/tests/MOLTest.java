/* ./tests/MOLTest.java
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
import org.jscience.mathematics.numbers.Float64;
import ca.usask.simlab.odeToJava.odes.BurgersMOLODE;
import ca.usask.simlab.odeToJava.testSuite.Testable;
import ca.usask.simlab.odeToJava.scheme.IMEXESDIRKButcherTableau;

/**
 * This class tests the method of lines models in order to test IMEX methods.
 *
 * Tests all IMEX methods for accuracy on the mildly stiff Luo-Rudy model.
 */ 
public class MOLTest {
    /**
     * The main method for the method of lines tests.
     *
     * @param args The standard variable for holding command-line arguments.
     */
    public static void main(String[] args) throws Exception {  
        Vector RTOLS = new Vector(Arrays.asList(1e-2, 3.16277e-3, 1e-3, 3.16277e-4, 1e-4));
        Vector ATOLS = new Vector(Arrays.asList(1e-2, 3.16277e-3, 1e-3, 3.16277e-4, 1e-4));
        // just using one spatial stepsize for now
        Testable burgers_mol = new Testable(new BurgersMOLODE(127,Float64.valueOf(1./126.),Float64.valueOf(0.01)), "referenceSolutions/burgersMOLReference.txt");
        // Forward Euler
        burgers_mol.test_sd_ForwardEuler(RTOLS, 
                                       ATOLS);
        // the Kennedy and Carpenter IMEX methods
        burgers_mol.test_embedded(IMEXESDIRKButcherTableau.get_KC32_tableau(),
                            RTOLS, 
                            ATOLS);  
        burgers_mol.test_embedded_KC43(RTOLS, 
                                ATOLS);   
        burgers_mol.test_embedded(IMEXESDIRKButcherTableau.get_KC54_tableau(),
                            RTOLS, 
                            ATOLS);  
        // LIRK IMEX methods of Calvo, de Frutos, and Nova 
        burgers_mol.test_embedded(IMEXESDIRKButcherTableau.get_LIRK32_tableau(),
                            RTOLS, 
                            ATOLS);  
        burgers_mol.test_embedded(IMEXESDIRKButcherTableau.get_LIRK43_tableau(),
                            RTOLS, 
                            ATOLS);  
        burgers_mol.test_embedded_Dopr(RTOLS,ATOLS);
    }
}
