/* ./tests/StiffDETestSet.java
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
import ca.usask.simlab.odeToJava.util.Matrix;
import ca.usask.simlab.odeToJava.scheme.Scheme;
import ca.usask.simlab.odeToJava.scheme.IMEXESDIRKButcherTableau;
import ca.usask.simlab.odeToJava.testSuite.Testable;
import ca.usask.simlab.odeToJava.controller.IVPController;
import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Vector;

import ca.usask.simlab.odeToJava.stiffDETest.A1;
import ca.usask.simlab.odeToJava.stiffDETest.A2;
import ca.usask.simlab.odeToJava.stiffDETest.A3;
import ca.usask.simlab.odeToJava.stiffDETest.A4;
import ca.usask.simlab.odeToJava.stiffDETest.B1;
import ca.usask.simlab.odeToJava.stiffDETest.B2;
import ca.usask.simlab.odeToJava.stiffDETest.B3;
import ca.usask.simlab.odeToJava.stiffDETest.B4;
import ca.usask.simlab.odeToJava.stiffDETest.B5;
import ca.usask.simlab.odeToJava.stiffDETest.C1;
import ca.usask.simlab.odeToJava.stiffDETest.C2;
import ca.usask.simlab.odeToJava.stiffDETest.C3;
import ca.usask.simlab.odeToJava.stiffDETest.C4;
import ca.usask.simlab.odeToJava.stiffDETest.C5;
import ca.usask.simlab.odeToJava.stiffDETest.D1;
import ca.usask.simlab.odeToJava.stiffDETest.D2;
import ca.usask.simlab.odeToJava.stiffDETest.D3;
import ca.usask.simlab.odeToJava.stiffDETest.D4;
import ca.usask.simlab.odeToJava.stiffDETest.D5;
import ca.usask.simlab.odeToJava.stiffDETest.D6;
import ca.usask.simlab.odeToJava.stiffDETest.E1;
import ca.usask.simlab.odeToJava.stiffDETest.E2;
import ca.usask.simlab.odeToJava.stiffDETest.E3;
import ca.usask.simlab.odeToJava.stiffDETest.E4;
import ca.usask.simlab.odeToJava.stiffDETest.E5;
import ca.usask.simlab.odeToJava.stiffDETest.F1;
import ca.usask.simlab.odeToJava.stiffDETest.F2;
import ca.usask.simlab.odeToJava.stiffDETest.F3;
import ca.usask.simlab.odeToJava.stiffDETest.F4;
import ca.usask.simlab.odeToJava.stiffDETest.F5;

/**
 * Test the methods of the stiff DE test set with standard methods.
 */ 
public class StiffDETestSet {

    /**
     * The main method for the tutorial.
     *
     * @param args The standard variable for holding command-line arguments.
     */
    public static void main(String[] args) throws Exception {
        Vector RTOLS = new Vector(Arrays.asList(1e-3, 1e-4, 1e-5, 1e-6, 1e-7, 1e-8));
        Vector ATOLS = new Vector(Arrays.asList(1e-3, 1e-4, 1e-5, 1e-6, 1e-7, 1e-8));
        Vector atol;
        Float64Vector stepsizes;
        // the odes we are solving
        Vector odes = new Vector();
        // the initial stepsizes to use
        Vector initial_stepsizes = new Vector();
        // a scale vector applied to the absolute tolerances
        Vector scale = new Vector();
        // a new vector of references
        Vector references = new Vector();

        odes.add(new A1());
        initial_stepsizes.add(1e-2);
        // tolerances based on magnitude of components of final solution
        scale.add(Float64Vector.valueOf(1e-10, 1e-10, 1e-10, 1e-10));
        references.add("referenceSolutions/stiffA1Reference.txt");
        odes.add(new A2());
        initial_stepsizes.add(5e-4);
        scale.add(Float64Vector.valueOf(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0));
        references.add("referenceSolutions/stiffA2Reference.txt");
        odes.add(new A3());
        initial_stepsizes.add(1e-5);
        scale.add(Float64Vector.valueOf(1.0, 1.0, 1.0, 1.0));
        references.add("referenceSolutions/stiffA3Reference.txt");
        odes.add(new A4());
        initial_stepsizes.add(1e-5);
        scale.add(Float64Vector.valueOf(1e-10, 1e-10, 1e-10, 1e-10, 1e-10, 1e-10, 1e-10, 1e-10, 1e-10, 1e-10));
        references.add("referenceSolutions/stiffA4Reference.txt");
        //
        odes.add(new B1());
        initial_stepsizes.add(7e-3);
        scale.add(Float64Vector.valueOf(1e-10, 1e-10, 1e-10, 1e-10));
        references.add("referenceSolutions/stiffB1Reference.txt");
        odes.add(new B2());
        initial_stepsizes.add(1e-2);
        scale.add(Float64Vector.valueOf(1.0, 1.0, 1.0, 1.0, 1.0, 1.0));
        references.add("referenceSolutions/stiffB2Reference.txt");
        odes.add(new B3());
        initial_stepsizes.add(1e-2);
        scale.add(Float64Vector.valueOf(1.0, 1.0, 1.0, 1.0, 1.0, 1.0));
        references.add("referenceSolutions/stiffB3Reference.txt");
        odes.add(new B4());
        initial_stepsizes.add(1e-2);
        scale.add(Float64Vector.valueOf(1.0, 1.0, 1.0, 1.0, 1.0, 1.0));
        references.add("referenceSolutions/stiffB4Reference.txt");
        odes.add(new B5());
        initial_stepsizes.add(1e-2);
        scale.add(Float64Vector.valueOf(1.0, 1.0, 1.0, 1.0, 1.0, 1.0));
        references.add("referenceSolutions/stiffB5Reference.txt");
        //
        odes.add(new C1());
        initial_stepsizes.add(1e-2);
        scale.add(Float64Vector.valueOf(1.0, 1.0, 1.0, 1.0));
        references.add("referenceSolutions/stiffC1Reference.txt");
        odes.add(new C2());
        initial_stepsizes.add(1e-2);
        scale.add(Float64Vector.valueOf(1.0, 1.0, 1.0, 1.0));
        references.add("referenceSolutions/stiffC2Reference.txt");
        odes.add(new C3());
        initial_stepsizes.add(1e-2);
        scale.add(Float64Vector.valueOf(1.0, 1.0, 1.0, 1.0));
        references.add("referenceSolutions/stiffC3Reference.txt");
        odes.add(new C4());
        initial_stepsizes.add(1e-2);
        scale.add(Float64Vector.valueOf(1.0, 1.0, 1.0, 1.0));
        references.add("referenceSolutions/stiffC4Reference.txt");
        odes.add(new C5());
        initial_stepsizes.add(1e-2);
        scale.add(Float64Vector.valueOf(1.0, 1.0, 1.0, 1.0));
        references.add("referenceSolutions/stiffC5Reference.txt");
        //
        odes.add(new D1());
        initial_stepsizes.add(1.7e-2);
        scale.add(Float64Vector.valueOf(1.0, 1.0, 1.0));
        references.add("referenceSolutions/stiffD1Reference.txt");
        odes.add(new D2());
        initial_stepsizes.add(1e-5);
        scale.add(Float64Vector.valueOf(1.0, 1.0, 1.0));
        references.add("referenceSolutions/stiffD2Reference.txt");
        odes.add(new D3());
        initial_stepsizes.add(2.5e-5);
        scale.add(Float64Vector.valueOf(1.0, 1.0, 1.0, 1.0));
        references.add("referenceSolutions/stiffD3Reference.txt");
        odes.add(new D4());
        initial_stepsizes.add(2.9e-4);
        scale.add(Float64Vector.valueOf(1.0, 1.0, 1.0));
        references.add("referenceSolutions/stiffD4Reference.txt");
        odes.add(new D5());
        initial_stepsizes.add(1e-4);
        scale.add(Float64Vector.valueOf(1.0, 1.0));
        references.add("referenceSolutions/stiffD5Reference.txt");
        odes.add(new D6());
        initial_stepsizes.add(3.3e-8);
        scale.add(Float64Vector.valueOf(1e-10, 1e-10, 1e-10));
        references.add("referenceSolutions/stiffD6Reference.txt");
        //
        odes.add(new E1());
        initial_stepsizes.add(6.8e-3);
        scale.add(Float64Vector.valueOf(1e-10, 1e-10, 1e-10, 1e-10));
        references.add("referenceSolutions/stiffE1Reference.txt");
        odes.add(new E2());
        initial_stepsizes.add(1e-3);
        scale.add(Float64Vector.valueOf(1.0, 1.0));
        references.add("referenceSolutions/stiffE2Reference.txt");
        odes.add(new E3());
        initial_stepsizes.add(0.02);
        scale.add(Float64Vector.valueOf(1.0, 1.0, 1.0));
        references.add("referenceSolutions/stiffE3Reference.txt");
        odes.add(new E4());
        initial_stepsizes.add(1e-3);
        scale.add(Float64Vector.valueOf(1e-5, 1e-5, 1e-5, 1e-5));
        references.add("referenceSolutions/stiffE4Reference.txt");
        odes.add(new E5());
        initial_stepsizes.add(5e-5);
        scale.add(Float64Vector.valueOf(1e-10, 1e-10, 1e-10, 1e-10));
        references.add("referenceSolutions/stiffE5Reference.txt");
        //
        odes.add(new F1());
        initial_stepsizes.add(1e-4);
        scale.add(Float64Vector.valueOf(1e-10, 1e-10, 1e-10, 1e-10));
        references.add("referenceSolutions/stiffF1Reference.txt");
        odes.add(new F2());
        initial_stepsizes.add(1e-2);
        scale.add(Float64Vector.valueOf(1.0, 1.0));
        references.add("referenceSolutions/stiffF2Reference.txt");
        odes.add(new F3());
        initial_stepsizes.add(1e-6);
        scale.add(Float64Vector.valueOf(1e-10, 1e-10, 1e-10, 1e-10, 1e-10));
        references.add("referenceSolutions/stiffF3Reference.txt");
        odes.add(new F4());
        initial_stepsizes.add(1e-3);
        scale.add(Float64Vector.valueOf(1.0, 1.0, 1.0));
        references.add("referenceSolutions/stiffF4Reference.txt");
        odes.add(new F5());
        initial_stepsizes.add(1e-7);
        scale.add(Float64Vector.valueOf(1e-5, 1e-5, 1e-5, 1e-5));
        references.add("referenceSolutions/stiffF5Reference.txt");

        Vector<Scheme> embedded_tableaux = new Vector(Arrays.asList(IMEXESDIRKButcherTableau.get_KC32_tableau(),
                                                                    IMEXESDIRKButcherTableau.get_KC43_tableau(),
                                                                    IMEXESDIRKButcherTableau.get_KC54_tableau()));

        Vector<Scheme> doubling_tableaux = new Vector(Arrays.asList(IMEXESDIRKButcherTableau.get_ARS232_tableau(),
                                                                    IMEXESDIRKButcherTableau.get_ARS233_tableau(),
                                                                    IMEXESDIRKButcherTableau.get_ARS343_tableau(),
                                                                    IMEXESDIRKButcherTableau.get_ARS443_tableau(),
                                                                    IMEXESDIRKButcherTableau.get_BHR5531_tableau(),
                                                                    IMEXESDIRKButcherTableau.get_BHR5532_tableau(),
                                                                    IMEXESDIRKButcherTableau.get_LIRK32_tableau(),
                                                                    IMEXESDIRKButcherTableau.get_LIRK43_tableau()));
        // get the sizes of the various parameters
        int n_embedded = embedded_tableaux.size();
        int n_doubling = doubling_tableaux.size();
        int n_odes = odes.size();
        int n_tols = RTOLS.size();

        // embedded tests
        for (int i = 0; i < n_odes; i++) {
            for (int j = 0; j < n_embedded; j++) {
                // skip D3 and KC 5(4)
                if (i == 16 && j == 2) {
                    continue;
                } 
                // skip F5 and KC 5(4)
                if (i == 29 && j == 2) {
                    continue;
                }  
                Testable detest = new Testable((RHS) odes.get(i), (String) references.get(i));
                Vector stepsize = new Vector();

                atol = new Vector();
                for (int k = 0; k < n_tols; k++) {
                    atol.add(((Float64Vector) scale.get(i)).times((Double) ATOLS.get(k)));
                    stepsize.add(Float64.valueOf((Double) initial_stepsizes.get(i)));
                }
                detest.test_embedded(embedded_tableaux.get(j), RTOLS, atol, stepsize);
             }
            // step-doubling tests
            for (int j = 0; j < n_doubling; j++) {
                // skip A2 and ARS 343
                if (i == 1 && j == 2) {
                    continue;
                }
                // skip D6 and ARS 343, only goes part way with poor accuracy
                if (i == 19 && j == 2) {
                    continue;
                }
                // skip E1 and ARS 343, only goes part way with poor accuracy
                if (i == 20 && j == 2) {
                    continue;
                }
                // skip E4 and ARS 343, only goes part way with poor accuracy
                if (i == 23 && j == 2) {
                    continue;
                }
                // skip F1 and ARS 343, only goes part way with poor accuracy
                if (i == 25 && j == 2) {
                    continue;
                }
                // skip F4 and ARS 343, only goes part way with poor accuracy
                if (i == 28 && j == 2) {
                    continue;
                }
                // skip F5 and ARS 343, only goes part way with poor accuracy
                if (i == 29 && j == 2) {
                    continue;
                }

                // skip F5 and LIRK 43, only goes part way with poor accuracy
                if (i == 29 && j == 7) {
                    continue;
                }

                Testable detest = new Testable((RHS) odes.get(i), (String) references.get(i));
                Vector stepsize = new Vector();

                atol = new Vector();
                for (int k = 0; k < n_tols; k++) {
                    atol.add(((Float64Vector) scale.get(i)).times((Double) ATOLS.get(k)));
                    stepsize.add(Float64.valueOf((Double) initial_stepsizes.get(i)));
                }
                detest.test_sd(doubling_tableaux.get(j), RTOLS, atol, stepsize);
            }
        }
    }
}
