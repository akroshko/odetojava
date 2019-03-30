/* ./property/PropertyUtilities.java
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

import javolution.util.FastList;

/**
 * This class contains the utility methods required to use
 * property objects.
 */
public class PropertyUtilities {
    /**
     * Get a list of supplied properties.
     *
     * @param module The module to get the list of properties from.
     *
     * @return The list of supplied properties.
     */
    protected FastList<String> get_supplies(PropertyUser module) {
        return module.get_supplied_properties();
    }

    /**
     * Get a list of required properties.
     *
     * @param module The module to get the list of properties from.
     *
     * @return The list of required properties.
     */
    protected FastList<String> get_requires(PropertyUser module) {
        return module.get_required_properties();
    }

    /**
     * Get a list of requested properties.
     *
     * @param module The module to get the list of properties from.
     *
     * @return The list of requested properties.
     */
    protected FastList<String> get_requests(PropertyUser module) {
        return module.get_requested_properties();
    }

    /**
     * Get a list of properties that are required if they are present.
     *
     * @param module The module to get the list of properties from.
     *
     * @return The list of properties that are required if they are present.
     */
    protected FastList<String> get_requires_if_present(PropertyUser module) {
        return module.get_required_properties();
    }

    /**
     * Set a list of present properties.
     *
     * @param user The user to set the list of present properties for.
     */
    protected void set_present(PropertyUser user, String property) {
        user.property_is_present(property);
    }
}
