/* ./modules/io/ProgressReporterModule.java
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
package ca.usask.simlab.odeToJava.modules.io;

import java.text.DecimalFormat;

import org.jscience.mathematics.numbers.Float64;
import ca.usask.simlab.odeToJava.property.PropertyHolder;
import ca.usask.simlab.odeToJava.solver.PropertySolver;
import ca.usask.simlab.odeToJava.solver.SolverModule;

/**
 * Reports the percentage progress of a solution towards the final time.
 */
public class ProgressReporterModule extends SolverModule {
    static DecimalFormat donePercentFormat = new DecimalFormat("##.##%");
    
    Float64 tf = Float64.ZERO;
    
    Float64 last_t = Float64.ZERO;
    
    Float64 precision = Float64.valueOf(0.01);
    
    /**
     * Set the default interval at 1%.
     */
    public ProgressReporterModule() {
        this(Float64.valueOf(0.01));
    }
    
    /**
     * The standard constructor for variable precision.
     *
     * @param precision The precision of the progress reporter.
     */
    public ProgressReporterModule(Float64 precision) {
        this.precision = precision;
        require_property("initialTime");
    }

    @Override
    public void step(PropertyHolder properties) {
        Float64 t = properties.getFloat64Property("initialTime");
        if (t.divide(tf).isGreaterThan(last_t.divide(tf).plus(precision))) {
            System.out.println(ProgressReporterModule.donePercentFormat.format(t.divide(tf).doubleValue()));
            System.out.flush();
            last_t = t;
        }
    }
    
    @Override
    public void begin_stepping(PropertySolver solver, PropertyHolder constant_properties) {
        super.begin_stepping(solver, constant_properties);
        tf = solver.get_final_time();
    }
    
}
