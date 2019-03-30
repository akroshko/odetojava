/* ./property/PropertySatisfier.java
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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * Ensure that the dependencies for the properties for the solver and all the
 * modules can be satisfied.
 */
public class PropertySatisfier extends PropertyUtilities {
    /**
     * The standard constructor.
     *
     * @param wrapper_module_in
     * @param modules_in
     */
    public PropertySatisfier(PropertyUser wrapper_module_in, List modules_in) {
        wrapper_module = wrapper_module_in;
        modules = modules_in;
    }

    /**
     * Get the map of properties while determining if the modules are satisfied.
     *
     * @throws IllegalStateException
     */
    public Map<String, String> satisfy_modules() {

        add_supplied_properties(wrapper_module);

        Iterator moduleIterator = modules.iterator();
        while (moduleIterator.hasNext()) {
            PropertyUser module = (PropertyUser) moduleIterator.next();
            if (!satisfy_module(module)) {
                throw new IllegalStateException("Cannot satisfy all modules requirements!");
            }
            add_supplied_properties(module);
        }

        satisfy_module(wrapper_module);

        return property_map;
    }

    /**
     * Satisfy the requests of a module and return the module requirements.
     *
     * @param module The module to determine the requests and requirements for.
     *
     * @return       If the requirements of the module this is true.
     */
    protected boolean satisfy_module(PropertyUser module) {
        satisfy_module_requests(module);
        return satisfy_module_requires(module);
    }

    /**
     * Determine if the requirements of a module are satisfied.
     *
     * @param module The module to determine the requests and requirements for.
     *
     * @return       If the requirements of the module are satisfied this is true.
     */
    protected boolean satisfy_module_requires(PropertyUser module) {
        Iterator requires_iterator = get_requires(module).iterator();
        while (requires_iterator.hasNext()) {
            String to_satisfy = (String) requires_iterator.next();
            boolean found = satisfy_property(to_satisfy);
            if (!found) {
                return false;
            }
        }

        return true;
    }

    /**
     * Determine if a particular property is satisfied by the module.
     *
     * @param to_satisfy The string uniquely identifying the target property.
     *
     * @return          If the property can be satisfied.
     */
    protected boolean satisfy_property(String to_satisfy) {
        Iterator available_iterator = available_properties.iterator();
        boolean found = false;
        while (available_iterator.hasNext()) {
            String available = (String) available_iterator.next();
            if (available.equals(to_satisfy)) {
                // strip attr so we don't need to supply it when setting the
                // value, the parent still keeps it unique
                add_property_pair(to_satisfy, available);
                found = true;
                break;
            }
        }
        return found;
    }

    /**
     * Add a pair of properties to be satisfied and available properties.
     *
     * @param to_satisfy The string uniquely identifying the target property.
     */
    protected void add_property_pair(String to_satisfy, String available) {
        property_map.put(to_satisfy, available);
    }

    /**
     * Find if the properties requested by a module can be satisfied.
     *
     * @param module The module to check if the properties can be satisfied.
     */
    protected void satisfy_module_requests(PropertyUser module) {
        Iterator requestsIterator = get_requests(module).iterator();
        while (requestsIterator.hasNext()) {
            String to_satisfy = (String) requestsIterator.next();
            if (satisfy_property(to_satisfy)) {
                set_present(module, to_satisfy);
            }
        }
    }

    /**
     * Get the propties a particular module supplies.
     *
     * @param module The module to check if the properties can be satisfied.
     */
    private void add_supplied_properties(PropertyUser module) {
        Iterator i = get_supplies(module).iterator();
        while (i.hasNext()) {
            String next = (String) i.next();
            // add to front so we can
            // search from there
            // and we'll get the latest properties first
            available_properties.add(0, next);
        }
    }

    private List<String> available_properties = new FastList<String>();

    private Map<String, String> property_map = new FastMap<String, String>();

    private PropertyUser wrapper_module;

    private List modules;
}
