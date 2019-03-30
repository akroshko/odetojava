/* ./tests/ManualTestable.java
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
import org.jscience.mathematics.numbers.Float64;

import ca.usask.simlab.odeToJava.odes.OrbitArenstorfODE;
import ca.usask.simlab.odeToJava.odes.BurgersMOLODE;

import ca.usask.simlab.odeToJava.testSuite.Testable;

import ca.usask.simlab.odeToJava.scheme.ERKButcherTableau;
import ca.usask.simlab.odeToJava.scheme.IMEXESDIRKButcherTableau;

/**
 * This class goes along with the example in the manual to demonstrate
 * the Testable class.
 */ 
public class ManualTestable {
    /**
     * The main method for the tutorial.
     *
     * @param args The standard variable for holding command-line arguments.
     */
    public static void main(String[] args) throws Exception { 
         Vector STEPSIZES = new Vector(Arrays.asList(1e-2, 3.16277e-3, 1e-3, 3.16277e-4, 1e-4));
         Vector ORBIT_RTOLS = new Vector(Arrays.asList(1e-3, 3.16277e-4, 1e-4, 3.16277e-5, 1e-5, 3.16277e-6, 1e-6));
         Vector ORBIT_ATOLS = new Vector(Arrays.asList(1e-3, 3.16277e-4, 1e-4, 3.16277e-5, 1e-5, 3.16277e-6, 1e-6));
         Vector MOL_RTOLS = new Vector(Arrays.asList(1e-2, 3.16277e-3, 1e-3, 3.16277e-4, 1e-4, 3.16277e-5, 1e-5));
         Vector MOL_ATOLS = new Vector(Arrays.asList(1e-2, 3.16277e-3, 1e-3, 3.16277e-4, 1e-4, 3.16277e-5, 1e-5));
 

         Testable orbit = new Testable(new OrbitArenstorfODE(), "referenceSolutions/01orbitReference.txt");
         Testable mol = new Testable(new BurgersMOLODE(127,Float64.valueOf(1./126.),Float64.valueOf(0.01)), "referenceSolutions/burgersMOLReference.txt");

         orbit.test_const(ERKButcherTableau.get_DormandPrince54_tableau(),STEPSIZES);
         orbit.test_embedded(ERKButcherTableau.get_DormandPrince54_tableau(),ORBIT_RTOLS,ORBIT_ATOLS);
         orbit.test_sd(ERKButcherTableau.get_DormandPrince54_tableau(),ORBIT_RTOLS,ORBIT_ATOLS);

         mol.test_embedded(IMEXESDIRKButcherTableau.get_KC32_tableau(),MOL_RTOLS,MOL_ATOLS);
         mol.test_embedded(IMEXESDIRKButcherTableau.get_KC43_tableau(),MOL_RTOLS,MOL_ATOLS);
         mol.test_embedded(IMEXESDIRKButcherTableau.get_KC54_tableau(),MOL_RTOLS,MOL_ATOLS);
    }
}
