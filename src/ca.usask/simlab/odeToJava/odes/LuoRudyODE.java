/* ./odes/LuoRudyODE.java
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
package ca.usask.simlab.odeToJava.odes;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.ode.RHS;

/**
 * An ODE describing cardiac electrical electrical activity in the heart.
 * <p>
 * Ching-hsing Luo and Yoram Rudy, "A Model of the Ventricular Cardiac Action Potential", Circulation Research, vol 68, pg 1501-1526, 1991.
 */    
public class LuoRudyODE extends RHS {
    @Override
    public Float64Vector f(Float64 t, Float64Vector y) {
        Float64Vector yp;
        yp = Float64Vector.valueOf(mDot(t, y), hDot(t, y), jDot(t, y), dDot(t, y), fDot(t, y), caDot(t, y), xDot(t, y), vDot(t, y));
        return yp;
    }
    
    @Override
    public int get_size() {
        return 8;
    }
    
    private double mDot(Float64 t, Float64Vector y) {
        double alpha_m = 0.32 * (y.getValue(7) + 47.13) / (1.0 - Math.exp(-0.1 * (y.getValue(7) + 47.13)));
        double beta_m = 0.08 * Math.exp(-y.getValue(7) / 11.0);
        double dmdt = (alpha_m / (alpha_m + beta_m) - y.getValue(0)) / (1.0 / (alpha_m + beta_m));
        return dmdt;
    }
    
    private double hDot(Float64 t, Float64Vector y) {
        double alpha_h, beta_h;
        if (y.getValue(7) < -40.0) {
            alpha_h = 0.135 * Math.exp((y.getValue(7) + 80) / -6.8);
            beta_h = 3.56 * Math.exp(0.079 * y.getValue(7) + 3.1 * Math.pow(10, 5) * Math.exp(0.35 * y.getValue(7)));
        } else {
            alpha_h = 0.0;
            beta_h = 1 / (0.13 * (1 + Math.exp((y.getValue(7) + 10.66) / -11.1)));
        }
        double dhdt = (alpha_h / (alpha_h + beta_h) - y.getValue(1)) / (1.0 / (alpha_h + beta_h));
        return dhdt;
        
    }
    
    private double jDot(Float64 t, Float64Vector y) {
        double alpha_j, beta_j;
        
        if (y.getValue(7) < -40.0) {
            alpha_j = (-1.2714 * Math.pow(10.0, -5.0) * Math.exp(0.2444 * y.getValue(7)) - 3.474 * Math.pow(10, -5.0) * Math.exp(-0.04391 * y.getValue(7))) * (y.getValue(7) + 37.78) / (1 + Math.exp(0.311 * (y.getValue(7) + 79.23)));
            beta_j = 0.1212 * Math.exp(-0.01052 * y.getValue(7)) / (1 + Math.exp(-0.1378 * (y.getValue(7) + 40.14)));
        } else {
            alpha_j = 0.0;
            beta_j = 0.3 * Math.exp(-2.535 * Math.pow(10.0, -7.0) * y.getValue(7)) / (1 + Math.exp(-0.1 * (y.getValue(7) + 32.0)));
        }
        double djdt = (alpha_j / (alpha_j + beta_j) - y.getValue(2)) / (1.0 / (alpha_j + beta_j));
        return djdt;
    }
    
    private double dDot(Float64 t, Float64Vector y) {
        double alpha_d = 0.095 * Math.exp(-0.01 * (y.getValue(7) - 5)) / (1.0 + Math.exp(-0.072 * (y.getValue(7) - 5.0)));
        double beta_d = 0.07 * Math.exp(-0.017 * (y.getValue(7) + 44.0)) / (1.0 + Math.exp(0.05 * (y.getValue(7) + 44.0)));
        double dddt = (alpha_d / (alpha_d + beta_d) - y.getValue(3)) / (1.0 / (alpha_d + beta_d));
        return dddt;
    }
    
    private double fDot(Float64 t, Float64Vector y) {
        double alpha_f = 0.012 * Math.exp(-0.008 * (y.getValue(7) + 28.0)) / (1.0 + Math.exp(0.15 * (y.getValue(7) + 28.0)));
        double beta_f = 0.0065 * Math.exp(-0.02 * (y.getValue(7) + 30.0)) / (1.0 + Math.exp(-0.2 * (y.getValue(7) + 30.0)));
        double dfdt = (alpha_f / (alpha_f + beta_f) - y.getValue(4)) / (1.0 / (alpha_f + beta_f));
        return dfdt;
    }
    
    private double caDot(Float64 t, Float64Vector y) {
        double Ca = y.getValue(5);
        double Gbar_si = 0.09;
        double E_si = 7.7 - 13.0287 * Math.log(Ca);
        double I_si = Gbar_si * y.getValue(3) * y.getValue(4) * (y.getValue(7) - E_si);
        double dCadt = -Math.pow(10.0, -4.0) * I_si + 0.07 * (Math.pow(10.0, -4.0) - Ca);
        return dCadt;
        
    }
    
    private double xDot(Float64 t, Float64Vector y) {
        double alpha_X = 0.0005 * Math.exp(0.083 * (y.getValue(7) + 50)) / (1.0 + Math.exp(0.057 * (y.getValue(7) + 50.0)));
        double beta_X = 0.0013 * Math.exp(-0.06 * (y.getValue(7) + 20.0)) / (1.0 + Math.exp(-0.04 * (y.getValue(7) + 20.0)));
        double dXdt = (alpha_X / (alpha_X + beta_X) - y.getValue(6)) / (1.0 / (alpha_X + beta_X));
        return dXdt;
        
    }
    
    public double vDot(Float64 t, Float64Vector y) {
        double C = 1;
        
        double Gbar_Na = 23.0; // maximum conductance of the sodium channel
        double E_Na = 54.4; // reversal potential for sodium
        double I_Na = Gbar_Na * Math.pow(y.getValue(0), 3) * y.getValue(1) * y.getValue(2) * (y.getValue(7) - E_Na); // Fast
        // sodium
        // current
        
        double Gbar_si = 0.09; // maximum conductance of the calcium channel
        
        double E_si = 7.7 - 13.0287 * Math.log(y.getValue(5)); // Reversal
        // potential
        // for
        // calcium
        
        double I_si = Gbar_si * y.getValue(3) * y.getValue(4) * (y.getValue(7) - E_si); // Slow
        // inward
        // current
        // (calcium)
        
        double Gbar_K = 0.282; // maximum conductance of the potassium channel
        // "K"
        double E_K = -77.0; // reversal potential of K
        double Xi;
        if (y.getValue(7) <= -100.0) {
            Xi = 1;
        } else if (y.getValue(7) == -77.0) {
            Xi = 0.6088832917; // made change here
        } else {
            Xi = 2.837 * (Math.exp(0.04 * (y.getValue(7) + 77.0)) - 1.0) / ((y.getValue(7) + 77.0) * Math.exp(0.04 * (y.getValue(7) + 35.0)));
        }
        
        double I_K = Gbar_K * y.getValue(6) * Xi * (y.getValue(7) - E_K); // time-dependent
        // potassium
        // current
        
        double Gbar_K1 = 0.6047; // maximum conductance of the potassium
        // channel "K1"
        double E_K1 = -87.2; // reversal potential of K1
        double alpha_K1 = 1.02 / (1.0 + Math.exp(0.2385 * (y.getValue(7) - E_K1 - 59.215)));
        double beta_K1 = (0.49124 * Math.exp(0.08032 * (y.getValue(7) - E_K1 + 5.476)) + Math.exp(0.06175 * (y.getValue(7) - E_K1 - 594.31))) / (1 + Math.exp(-0.5143 * (y.getValue(7) - E_K1 + 4.753)));
        double K1inf = alpha_K1 / (alpha_K1 + beta_K1);
        double I_K1 = Gbar_K1 * K1inf * (y.getValue(7) - E_K1); // time-independent
        // potassium
        // current
        
        double Gbar_Kp = 0.0183; // maximum conductance of the potassium
        // channel "Kp"
        double E_Kp = -87.2; // reversal potential of Kp
        double Kp = 1.0 / (1.0 + Math.exp((7.488 - y.getValue(7)) / 5.98));
        double I_Kp = Gbar_Kp * Kp * (y.getValue(7) - E_Kp); // Plateau
        // potassium
        // current
        
        double Gbar_b = 0.03921; // maximum conductance of the potassium
        // channel "b"
        double E_b = -59.87; // reversal potential of b
        double I_b = Gbar_b * (y.getValue(7) - E_b); // background potassium
        // current
        
        double dVmdt = -(1.0 / C) * (I_Na + I_si + I_K + I_K1 + I_Kp + I_b);
        return dVmdt;
    }
}
