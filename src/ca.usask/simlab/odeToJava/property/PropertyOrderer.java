/* ./property/PropertyOrderer.java
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

import javolution.util.FastList;

/**
 * This class decides on the order of the modules, typically within a solver, based
 * on their dependencies.
 */
public class PropertyOrderer extends PropertyUtilities {
    /**
     * The standard constructor.
     *
     * @param modules_user_in The object that supplies properties to start with and
     *                      requires them at the end. This is typically the
     *                      solver that uses the modules.
     * @param modules_in     The list of modules to be ordered.
     */
    public PropertyOrderer(PropertyUser modules_user_in, FastList<PropertyUser> modules_in) {
        unused_modules = new FastList<PropertyUser>(modules_in);
        wrapper_user = modules_user_in;
    }

    /**
     * Get the list of ordered modules based on their dependencies.
     */
    public FastList<PropertyUser> get_ordered_modules() {
        add_supplied_properties(wrapper_user);

        while (unused_modules.size() > 0) {
            boolean found_potential = false;
            Iterator<PropertyUser> i = unused_modules.iterator();
            while (i.hasNext()) {
                PropertyUser potential = i.next();
                if (is_satisfied(potential) && is_permitted(potential)) {
                    found_potential = true;
                    ordered_modules.add(potential);
                    add_supplied_properties(potential);
                    i.remove();
                    break;
                }
            }
            if (!found_potential) {
                throw new IllegalStateException("Circular Module Requirements.");
            }
        }

        return ordered_modules;
    }

    /**
     * Check if a module can be added to the list of ordered modules.
     *
     * @param potential The module to check.
     */
    private boolean is_permitted(PropertyUser potential) {
        // check each property we require to see if it can go before all unused modules
        Iterator requires_iterator = get_requires(potential).iterator();
        while (requires_iterator.hasNext()) {
            String required_property = (String) requires_iterator.next();
            boolean isChained = has_property_with_name(required_property, get_supplies(potential));

            Iterator<PropertyUser> unusedIterator = unused_modules.iterator();
            while (unusedIterator.hasNext()) {
                PropertyUser other = unusedIterator.next();
                if (other == potential) {
                    continue;
                }
                if (isChained) {
                    if (user_satisfies_property(other, required_property)) {
                        boolean otherChains = has_property_with_name(required_property, get_supplies(other));
                        if (!otherChains) {
                            // we can't go before the other user because we
                            // chain the property and it only supplies it, which
                            // means it must go at the start of the chain
                            return false;
                        }
                    }
                } else {
                    // not chained by potential
                    // if the other user satisfies a property that the potential
                    // requires, then the potential must go after that other
                    // user
                    // (since it is not chaining the property);
                    if (user_satisfies_property(other, required_property)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Check if a module satisfies a particular property.
     *
     * @param other            The module to check.
     * @param required_property The particular property to check.
     *
     * @return true if the module satisfies the property.
     */
    private boolean user_satisfies_property(PropertyUser other, String required_property) {
        Iterator<String> property_iterator = get_supplies(other).iterator();
        while (property_iterator.hasNext()) {
            String supplied = property_iterator.next();
            if (supplied.equals(required_property)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if a property with a particular name is in a list of properties.
     *
     * @param name       The name of the propery to check for.
     * @param properties The list of properties.
     *
     * @return true if this list of properties has one with a particular name.
     */
    private boolean has_property_with_name(String name, FastList<String> properties) {
        Iterator<String> property_iterator = properties.iterator();
        while (property_iterator.hasNext()) {
            String p = property_iterator.next();
            if (p.equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a modules requirements are satisfied by the available properties.
     *
     * @param potential The module to check.
     *
     * @return true if the module satisfies the property.
     */
    private boolean is_satisfied(PropertyUser potential) {
        Iterator<String> requires_iterator = get_requires(potential).iterator();
        while (requires_iterator.hasNext()) {
            String required = requires_iterator.next();
            boolean found = false;

            // look for an available property to satisfy this one
            Iterator<String> available_iterator = available_properties.iterator();
            while (available_iterator.hasNext()) {
                String available = available_iterator.next();
                if (available.equals(required)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    /**
     * Add the list of supplied properties from a module.
     *
     * @param module The module to get the supplied properties from.
     */
    private void add_supplied_properties(PropertyUser module) {
        Iterator<String> i = get_supplies(module).iterator();
        while (i.hasNext()) {
            available_properties.add(i.next());
        }
    }

    /*
     * The list of available properties.
     */
    private final FastList<String> available_properties = new FastList<String>();

    /*
     * The list of ordered modules.
     */
    private final FastList<PropertyUser> ordered_modules = new FastList<PropertyUser>();

    /*
     * The list of unused modules.
     */
    private final FastList<PropertyUser> unused_modules;

    /*
     * The solver that contains the modules.
     */
    private final PropertyUser wrapper_user;

}
