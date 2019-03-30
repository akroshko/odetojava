import org.odeToJava.ode.RHS;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Vector;

/**
 * ODE that describes a 3-body problem with the Earth, Moon, and
 * a massless satellite.
 * <p>
 * Benedict Leimkuhler, Sebastian Reich. "Simulating Hamiltonian dynamics", Cambridge University Press, pg 161-163, 2004.
 */
public class OrbitArenstorfODE extends RHS {
    public int get_size() {
        return 4;
    }

    public Float64Vector f(Float64 t,Float64Vector y) {
        double y0 = y.getValue(0);
        double y1 = y.getValue(1);
        double y2 = y.getValue(2);
        double y3 = y.getValue(3);

        double[] yp = new double[y.getDimension()];

        double d1 = Math.pow(Math.pow((y0 + mu), 2) + y1*y1, 1.5);
        double d2 = Math.pow(Math.pow((y0 - muhat), 2) + y1*y1, 1.5);

        yp[0] = y2;
        yp[1] = y3;
        yp[2] = y0 + 2 * y3 - muhat * (y0 + mu) / d1 - mu*(y0 - muhat) / d2;
        yp[3] = y1 - 2 * y2 - muhat * y1 / d1 - mu*y1 / d2;

        return Float64Vector.valueOf(yp);
    }

    private final double mu = 0.012277471; // masses of planet and sun
    private final double muhat = 1.0 - mu; // respectively
}
