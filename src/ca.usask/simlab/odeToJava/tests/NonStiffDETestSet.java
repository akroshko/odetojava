/* ./tests/NonStiffDETestSet.java
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

import ca.usask.simlab.odeToJava.ode.RHS;
import ca.usask.simlab.odeToJava.scheme.ERKButcherTableau;
import ca.usask.simlab.odeToJava.interpolant.DefaultInterpolant;
import ca.usask.simlab.odeToJava.testSuite.Testable;
import ca.usask.simlab.odeToJava.controller.IVPController;
import org.jscience.mathematics.vectors.Float64Vector;

import ca.usask.simlab.odeToJava.nonstiffDETest.A1;
import ca.usask.simlab.odeToJava.nonstiffDETest.A2;
import ca.usask.simlab.odeToJava.nonstiffDETest.A3;
import ca.usask.simlab.odeToJava.nonstiffDETest.A4;
import ca.usask.simlab.odeToJava.nonstiffDETest.A5;
import ca.usask.simlab.odeToJava.nonstiffDETest.B1;
import ca.usask.simlab.odeToJava.nonstiffDETest.B2;
import ca.usask.simlab.odeToJava.nonstiffDETest.B3;
import ca.usask.simlab.odeToJava.nonstiffDETest.B4;
import ca.usask.simlab.odeToJava.nonstiffDETest.B5;
import ca.usask.simlab.odeToJava.nonstiffDETest.C1;
import ca.usask.simlab.odeToJava.nonstiffDETest.C2;
import ca.usask.simlab.odeToJava.nonstiffDETest.C3;
import ca.usask.simlab.odeToJava.nonstiffDETest.C4;
import ca.usask.simlab.odeToJava.nonstiffDETest.C5;
import ca.usask.simlab.odeToJava.nonstiffDETest.D1;
import ca.usask.simlab.odeToJava.nonstiffDETest.D2;
import ca.usask.simlab.odeToJava.nonstiffDETest.D3;
import ca.usask.simlab.odeToJava.nonstiffDETest.D4;
import ca.usask.simlab.odeToJava.nonstiffDETest.D5;
import ca.usask.simlab.odeToJava.nonstiffDETest.E1;
import ca.usask.simlab.odeToJava.nonstiffDETest.E2;
import ca.usask.simlab.odeToJava.nonstiffDETest.E3;
import ca.usask.simlab.odeToJava.nonstiffDETest.E4;
import ca.usask.simlab.odeToJava.nonstiffDETest.E5;
import ca.usask.simlab.odeToJava.nonstiffDETest.F1;
import ca.usask.simlab.odeToJava.nonstiffDETest.F2;
import ca.usask.simlab.odeToJava.nonstiffDETest.F3;
import ca.usask.simlab.odeToJava.nonstiffDETest.F4;
import ca.usask.simlab.odeToJava.nonstiffDETest.F5;

/**
 * Test the methods of the non-stiff DE test set with standard methods.
 */
public class NonStiffDETestSet {
    /**
     * The main method for the non-stiff DE test set.
     *
     * @param args The standard variable for holding command-line arguments.
     */  
    public static void main(String[] args) throws Exception {
        Vector RTOLS = new Vector(Arrays.asList(1e-3, 1e-4, 1e-5, 1e-6, 1e-7, 1e-8, 1e-9, 1e-10, 1e-11, 1e-12));
        Vector ATOLS = new Vector(Arrays.asList(1e-3, 1e-4, 1e-5, 1e-6, 1e-7, 1e-8, 1e-9, 1e-10, 1e-11, 1e-12)); 
        Vector odes = new Vector();
        Vector references = new Vector();

        // 
        odes.add(new A1());
        references.add("referenceSolutions/nonstiffA1Reference.txt");
        odes.add(new A2());
        references.add("referenceSolutions/nonstiffA2Reference.txt");
        odes.add(new A3());
        references.add("referenceSolutions/nonstiffA3Reference.txt");
        odes.add(new A4());
        references.add("referenceSolutions/nonstiffA4Reference.txt");
        odes.add(new A5());
        references.add("referenceSolutions/nonstiffA5Reference.txt");
        //  
        odes.add(new B1());
        references.add("referenceSolutions/nonstiffB1Reference.txt");
        odes.add(new B2());
        references.add("referenceSolutions/nonstiffB2Reference.txt");
        odes.add(new B3());
        references.add("referenceSolutions/nonstiffB3Reference.txt");
        odes.add(new B4());
        references.add("referenceSolutions/nonstiffB4Reference.txt");
        odes.add(new B5());
        references.add("referenceSolutions/nonstiffB5Reference.txt");
        //
        odes.add(new C1());
        references.add("referenceSolutions/nonstiffC1Reference.txt");
        odes.add(new C2());
        references.add("referenceSolutions/nonstiffC2Reference.txt");
        odes.add(new C3());
        references.add("referenceSolutions/nonstiffC3Reference.txt");
        odes.add(new C4());
        references.add("referenceSolutions/nonstiffC4Reference.txt");
        odes.add(new C5());
        references.add("referenceSolutions/nonstiffC5Reference.txt");
        // 
        odes.add(new D1());
        references.add("referenceSolutions/nonstiffD1Reference.txt");
        odes.add(new D2());
        references.add("referenceSolutions/nonstiffD2Reference.txt");
        odes.add(new D3());
        references.add("referenceSolutions/nonstiffD3Reference.txt");
        odes.add(new D4());
        references.add("referenceSolutions/nonstiffD4Reference.txt");
        odes.add(new D5());
        references.add("referenceSolutions/nonstiffD5Reference.txt");
        //
        odes.add(new E1());
        references.add("referenceSolutions/nonstiffE1Reference.txt");
        odes.add(new E2());
        references.add("referenceSolutions/nonstiffE2Reference.txt");
        odes.add(new E3());
        references.add("referenceSolutions/nonstiffE3Reference.txt");
        odes.add(new E4());
        references.add("referenceSolutions/nonstiffE4Reference.txt");
        odes.add(new E5());
        references.add("referenceSolutions/nonstiffE5Reference.txt");
        //
        odes.add(new F1());
        references.add("referenceSolutions/nonstiffF1Reference.txt");
        odes.add(new F2());
        references.add("referenceSolutions/nonstiffF2Reference.txt");
        odes.add(new F3());
        references.add("referenceSolutions/nonstiffF3Reference.txt");
        odes.add(new F4());
        references.add("referenceSolutions/nonstiffF4Reference.txt");
        odes.add(new F5());
        references.add("referenceSolutions/nonstiffF5Reference.txt");
        
        Testable detest;
        for (int i = 0; i < 30; i++) {
            detest = new Testable((RHS) odes.get(i), (String) references.get(i));

            // forward Euler
            detest.test_sd_ForwardEuler(RTOLS, ATOLS);
            // Heun, order 2 
            detest.test_sd(ERKButcherTableau.get_Heun2_tableau(), RTOLS, ATOLS);
            // "the" Runge-Kutta, order 4 with step-doubling
            detest.test_sd_RK4(RTOLS, ATOLS);
            // the Merson method of order 4(3)
            detest.test_embedded(ERKButcherTableau.get_RKMerson43_tableau(), RTOLS, ATOLS);
            // the Zonneveld method of order 4(3)
            detest.test_embedded(ERKButcherTableau.get_RKZonneveld43_tableau(), RTOLS, ATOLS);
            // Runge-Kutta Fehlberg
            if (i != 22) {
                detest.test_embedded(ERKButcherTableau.get_RKF45_tableau(), RTOLS, ATOLS); 
            }
            // Dormand-Prince 5(4) 
            detest.test_embedded_Dopr(RTOLS, ATOLS);
            // Verner 6(5) 
            if (i != 22) {
                detest.test_embedded(ERKButcherTableau.get_Verner65_tableau(), RTOLS, ATOLS);
            }
            // Runge-Kutta-Fehlberg 7(8)
            if (i != 22) {
                detest.test_embedded(ERKButcherTableau.get_RKF78_tableau(), RTOLS, ATOLS);
            }

            // Test the orbital problems, C5 to D5, with Stormer-Verlet
            if (14 <= i && i < 20) {
                detest.test_const_StormerVerlet();
            }
        }
    }
}
