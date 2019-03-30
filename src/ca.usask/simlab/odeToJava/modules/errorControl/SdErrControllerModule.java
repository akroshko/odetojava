/* ./modules/errorControl/SdErrControllerModule.java
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
package ca.usask.simlab.odeToJava.modules.errorControl;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.ode.RHS;
import ca.usask.simlab.odeToJava.property.PropertyHolder;
import ca.usask.simlab.odeToJava.solver.PropertySolver;
import ca.usask.simlab.odeToJava.util.Check;
import ca.usask.simlab.odeToJava.util.Matrix;

/**
 * This class implements a stepsize controller that uses an optimal stepsize
 * based on an step-doubling error estimate.
 * <p>
 * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 164.
 * <p>
 * Josef Stoer and Roland Bulirsch. "Introduction to Numerical analysis", Springer-Verlag, pg 448-454.
 */
public class SdErrControllerModule extends BaseErrControllerModule {
    protected int q;

    /**
     * Constructor that sets up the error control with user-specified uniform tolerances.
     *
     * @param atol The absolute tolerance used for all variables.
     * @param rtol The relative tolerance used for all variables.
     * @param ode  The ODE that is being solved.
     */
    public SdErrControllerModule(double atol, double rtol, RHS ode) {
        super(atol, rtol, ode);
        initialize_properties();
    }

    /**
     * Constructor that sets up the error control with user-specified uniform tolerances.
     *
     * @param atol The absolute tolerance used for all variables.
     * @param rtol The relative tolerance used for all variables.
     * @param ode  The ODE that is being solved.
     */
    public SdErrControllerModule(Float64 atol, Float64 rtol, RHS ode) {
        super(atol, rtol, ode);
        initialize_properties();
    }

    /**
     * Constructor that sets up the error control with user-specified component-wise tolerances.
     *
     * @param atol A vector of absolute tolerances.
     * @param rtol A vector of relative tolerances.
     * @param ode  The ODE that is being solved.
     */
    public SdErrControllerModule(double[] atol, double[] rtol, RHS ode) {
        super(atol, rtol, ode);
        initialize_properties();
    }

    /**
     * Constructor that sets up the error control with user-specified component-wise tolerances.
     *
     * @param atol A vector of absolute tolerances.
     * @param rtol A vector of relative tolerances.
     * @param ode  The ODE that is being solved.
     */
    public SdErrControllerModule(Float64Vector atol, Float64Vector rtol, RHS ode) {
        super(atol, rtol, ode);
        initialize_properties();
    }

    private void initialize_properties() {
     require_property("initialTime");
        require_property("finalTime");
        require_property("finalValues");
        require_property("initialValues");
        require_property("schemeOrder");
        require_property("errorEstimate");

        // the next stepsize if for the doubled step
        supply_property("stepAccepted");
        supply_property("nextStepSize");

        supply_property("absoluteTolerances");
        supply_property("relativeTolerances");
        supply_property("aMax");
        supply_property("aMin");
    }

    @Override
    public void begin_stepping(PropertySolver solver, PropertyHolder constant_properties) {
        q = constant_properties.get_int_property("schemeOrder");

        constant_properties.set_property("absoluteTolerances", get_atol());
        constant_properties.set_property("relativeTolerances", get_rtol());
        constant_properties.set_property("aMax", get_amax_normal());
        constant_properties.set_property("aMin", get_amin());
    }


    @Override
    public void step(PropertyHolder properties) {
        Float64Vector yinitial = properties.get_Float64Vector_property("initialValues");
        Float64 dt = properties.getFloat64Property("finalTime").minus(properties.getFloat64Property("initialTime"));
        Float64Vector yfinal = properties.get_Float64Vector_property("finalValues");
        Float64Vector errEstimate = properties.get_Float64Vector_property("errorEstimate");
        // make sure our floating point numbers are valid and reject if not
        if (!Check.valid(yfinal)) {
            set_next_stepsize(get_amin().times(dt));
            set_step_accepted(false);
            properties.set_property("stepAccepted", is_step_accepted());
            properties.set_property("nextStepSize", get_next_stepSize());
            return;
        }

        Float64Vector tolerances = get_tolerances(yinitial, yfinal);
        Float64Vector ratios = Matrix.divide(errEstimate, tolerances);
        Float64 epsilon = Matrix.rms(ratios);
        Float64 hopt = dt.divide(epsilon.times(Math.pow(2.0, q)).pow(Float64.ONE.divide(Float64.valueOf(q).plus(Float64.ONE))));
        Float64 hnew = Matrix.min(get_amax().times(dt), Matrix.max(get_amin().times(dt), get_safety().times(hopt.times(2.0))));
        boolean step_accepted = !((dt.divide(hopt)).isGreaterThan(get_threshold()));
        set_step_accepted(step_accepted);
        set_next_stepsize(hnew);
        properties.set_property("stepAccepted", step_accepted);
        properties.set_property("nextStepSize", hnew);
    }
}
