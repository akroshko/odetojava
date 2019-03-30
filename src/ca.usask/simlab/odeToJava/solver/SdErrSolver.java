/* ./solver/SdErrSolver.java
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
package ca.usask.simlab.odeToJava.solver;

import org.jscience.mathematics.numbers.Float64;
import ca.usask.simlab.odeToJava.property.PropertyHolder;

/**
 * A variable-stepsize solver using an step-doubling error estimate.
 * <p>
 * The step-doubling error-control scheme uses a two small steps to advance the
 * solution along with a single double step to facilitate error estimation.
 * <p>
 * Ernst Hairer, Syvert Norsett, and Gerhard Wanner. "Solving Order Differential Equations I, 2nd Edition", pg 165.
 * <p>
 * Josef Stoer and Roland Bulirsch. "Introduction to Numerical analysis", pg 448-454.
 */ 
public class SdErrSolver extends VariableStepsizeSolver { 
    /**
     * A string identifying the module for the error control step.
     */
    public final static String error_controlStep = "SdErrSolver.errorControlStep";
    /**
     * A string identifying the module for the first standard step.
     */
    public final static String standardStep1 = "SdErrSolver.standardStep1";
    /**
     * A string identifying the module for the second standard step.
     */
    public final static String standardStep2 = "SdErrSolver.standardStep2";
    
    /**
     * The standard constructor.
     */
    public SdErrSolver() {
        super();
        require_property("stepAccepted");
        require_property("nextStepSize");
        require_property("finalValues");
        require_property("finalTime");
        
        require_if_present_property("stopSolver");
        require_if_present_property("stopReason");
        
        require_if_present_property("initialStepSize");
        
        supply_property("finalTime");
        supply_property("initialTime");
        supply_property("initialValues");
    }
    
    /**
     * Add a module for the error control steps.
     *
     * @param module The module to add.
     */
    public void addErrSolverModule(SolverModule module) {
        super.add_solver_module(new SdErrModuleWrapper(module, SdErrSolver.error_controlStep));
    }
    
    /**
     * Add a module for the error control steps.
     *
     * @param module The module to add.
     */
    public void addStdSolverModule(SolverModule module) {
        super.add_solver_module(new SdStd1ModuleWrapper(module, SdErrSolver.standardStep1));
        super.add_solver_module(new SdStd2ModuleWrapper(module, SdErrSolver.standardStep2));
    }
    
    /**
     * Class to wrap the error step.
     * <p>
     * All calls are passed along to this module.
     */
    class SdErrModuleWrapper extends SolverModule {
        SdErrModuleWrapper(SolverModule moduleIn, String stepAttributeIn) {
            supply_property("finalValuesError");
            module = moduleIn;
            stepAttribute = stepAttributeIn;
            add_merged_user(module);
        }
        
        @Override
        public void begin_stepping(PropertySolver solver, PropertyHolder constant_properties) {
            module.begin_stepping(solver, constant_properties);
        }
        
        @Override
        public void step(PropertyHolder properties) {
            module.step(properties);  
            properties.set_property("finalValuesError", properties.get_Float64Vector_property("finalValues"));
        }
        
        @Override
        public void end_stepping() {
            module.end_stepping();        
        }
        
        @Override
        public String toString() {
            return stepAttribute + ":" + module;
        }
        
        SolverModule module;
        
        String stepAttribute;
    }
    
    /**
     * Class to wrap the first standard step.
     * <p>
     * All calls are passed along to this module.
     */ 
    class SdStd1ModuleWrapper extends SolverModule {
        SdStd1ModuleWrapper(SolverModule moduleIn, String stepAttributeIn) {
            // we want the standard steps to go after the error control step
            // so anything supplied by both comes from the last standard step
            require_property("finalValuesError");
            supply_property("finalValuesHalf");
            supply_property("initialTimePush");
            supply_property("finalTimePush");
            supply_property("initialValuesPush");
            
            module = moduleIn;
            stepAttribute = stepAttributeIn;
            add_merged_user(module);
        }
        
        @Override
        public void begin_stepping(PropertySolver solver, PropertyHolder constant_properties) {
            module.begin_stepping(solver, constant_properties);
        }
        
        @Override
        public void step(PropertyHolder properties) {
            // push the data onto to a backup
            properties.set_property("initialTimePush", properties.getFloat64Property("initialTime"));
            properties.set_property("finalTimePush", properties.getFloat64Property("finalTime"));
            properties.set_property("initialValuesPush", properties.get_Float64Vector_property("initialValues"));
            Float64 dt = get_current_stepsize().divide(2.0);
            properties.set_property("finalTime", SdErrSolver.this.get_current_time().plus(dt));
            module.step(properties);
            // set the final values to finalValuesHalf
            properties.set_property("finalValuesHalf", properties.get_Float64Vector_property("finalValues"));
        }
        
        @Override
        public void end_stepping() {
            module.end_stepping();
        }
        
        @Override
        public String toString() {
            return stepAttribute + ":" + module;
        }
        
        SolverModule module;
        
        String stepAttribute;
    }
    
    /**
     * Class to wrap the first standard step.
     * <p>
     * All calls are passed along to this module.
     */  
    class SdStd2ModuleWrapper extends SolverModule {
        SdStd2ModuleWrapper(SolverModule moduleIn, String stepAttributeIn) {
            // we want the standard steps to go after the error control step
            // so anything supplied by both comes from the last standard step
            require_property("finalValuesHalf");
            require_property("initialTimePush");
            require_property("finalTimePush");
            require_property("initialValuesPush");
            
            module = moduleIn;
            stepAttribute = stepAttributeIn;
            add_merged_user(module);
        }
        
        @Override
        public void begin_stepping(PropertySolver solver, PropertyHolder constant_properties) {
            module.begin_stepping(solver, constant_properties);
        }
        
        @Override
        public void step(PropertyHolder properties) {
            Float64 dt = SdErrSolver.this.get_current_stepsize().divide(2.0);
            properties.set_property("initialTime", properties.getFloat64Property("finalTime"));
            properties.set_property("initialValues", properties.get_Float64Vector_property("finalValuesHalf"));
            properties.set_property("finalTime", properties.getFloat64Property("initialTime").plus(dt));
            module.step(properties);
            //restore the properties the way they were
            properties.set_property("initialTime", properties.getFloat64Property("initialTimePush"));
            properties.set_property("initialValues", properties.get_Float64Vector_property("initialValuesPush"));
            properties.set_property("finalTime", properties.getFloat64Property("finalTimePush"));
        }
        
        @Override
        public void end_stepping() {
            module.end_stepping();
        }
        
        @Override
        public String toString() {
            return stepAttribute + ":" + module;
        }
        
        SolverModule module;
        
        String stepAttribute;
    }
}
