/* ./property/RequiredIfPresentPropertyFinder.java
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
import java.util.List;

/**
 * This class finds properties that are not required unless they are present.
 */
public class RequiredIfPresentPropertyFinder extends PropertyUtilities {
    /**
     * @param solver_module The module to add to the list of modules.
     * @param modules_in    The list of modules.
     */
    public RequiredIfPresentPropertyFinder(PropertyUser solver_module, List<PropertyUser> modules_in) {
        modules = new FastList<PropertyUser>(modules_in);
        modules.add(solver_module);
    }

    /**
     * Find the modules that are required if present.
     */
    public void find_required_if_present_properties() {
        // collect all the supplied modules
        Iterator<PropertyUser> modules_iterator = modules.iterator();
        while (modules_iterator.hasNext()) {
            PropertyUser user = modules_iterator.next();
            all_supplied.addAll(get_supplies(user));
        }

        // now check for all requests
        modules_iterator = modules.iterator();
        while (modules_iterator.hasNext()) {
            PropertyUser user = modules_iterator.next();
            List<String> req_if_pres = get_requires_if_present(user);
            Iterator<String> req_if_pres_iterator = req_if_pres.iterator();
            while (req_if_pres_iterator.hasNext()) {
                String requested = req_if_pres_iterator.next();
                if (is_supplier_for(requested)) {
                    set_present(user, requested);
                }
            }
        }
    }

    /**
     * Check if the modules supply the requested property.
     *
     * @param requested The required property.
     */
    private boolean is_supplier_for(String requested) {
        Iterator<String> supplied_properties = all_supplied.iterator();
        while (supplied_properties.hasNext()) {
            String supplied = supplied_properties.next();
            if (supplied.equals(requested)) {
                return true;
            }
        }
        return false;
    }

    /**
     * The list of all supplied properties.
     */
    List<String> all_supplied = new FastList<String>();
    /**
     * The list of modules.
     */
    List<PropertyUser> modules;
}
