/* ./property/DebugPropertySatisfier.java
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
package ca.usask.simlab.odeToJava.property;

import java.util.List;
import java.util.Map;

/**
 * A subclass of PropertySatisfier that provides debugging information.
 */
public class DebugPropertySatisfier extends PropertySatisfier {

    /**
     * The default constructor.
     *
     * @param wrapper_module_in  The PropertyUser to debug.
     * @param modules_in         The list of modules.
     */
    public DebugPropertySatisfier(PropertyUser wrapper_module_in, List modules_in) {
        super(wrapper_module_in, modules_in);
    }

    @Override
    public Map<String, String> satisfy_modules() {
        return super.satisfy_modules();
    }

    @Override
    protected boolean satisfy_module(PropertyUser module) {
        System.err.println("Satisfying " + module + ":");
        return super.satisfy_module(module);
    }

    @Override
    protected void add_property_pair(String to_satisfy, String available) {
        System.err.println("\tSupplied " + to_satisfy + " with " + available);
        super.add_property_pair(to_satisfy, available);
    }
}
