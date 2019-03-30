/* ./property/PropertyUser.java
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
 * this class is holds of lists of properties and the methods that interact with them
 */
public class PropertyUser {
    /**
     * The standard constructor.
     */
    public PropertyUser() {
        required_properties = new FastList<String>();
        required_if_present_properties = new FastList<String>();
        supplied_properties = new FastList<String>();
        requested_properties = new FastList<String>();
        merged_users = new FastList<PropertyUser>();
    }

    /*
     * The list of required properties.
     */
    protected FastList<String> required_properties;
    /*
     * The list of supplied properties.
     */
    protected FastList<String> supplied_properties;
    /*
     * The list of requested properties.
     */
    protected FastList<String> requested_properties;
    /*
     * The list of merged users.
     */
    protected FastList<PropertyUser> merged_users;
    /*
     * The list of properties that are required only if they are present.
     */
    protected FastList<String> required_if_present_properties;

    protected void require_property(String name) {
        required_properties.add(name);
    }

    /**
     * Add a property that is required if present.
     *
     * @param name The name of the property to add.
     */
    protected void require_if_present_property(String name) {
        required_if_present_properties.add(name);
    }

    protected void request_property(String name, Object attr) {
        requested_properties.add(name);
    }

    /**
     * Add a property that is supplied.
     *
     * @param name The name of the property to add.
     */
    protected void supply_property(String name) {
        supplied_properties.add(name);
    }

    /**
     * Add a property that is required.
     *
     * @param name The name of the property to add.
     */
    protected void request_property(String name) {
        requested_properties.add(name);
    }

    /**
     * Add a merged user.
     *
     * @param other The name of the other PropertyUser object to merge.
     */
    protected void add_merged_user(PropertyUser other) {
        merged_users.add(other);
    }

    /**
     * Add the name of a present property.
     *
     * @param name The name of the property to add.
     */
    public void property_is_present(String name) {
        required_properties.add(name);
    }

    /**
     * Get the list of requested properties.
     *
     * @return The list of requested properties.
     */
    public FastList<String> get_requested_properties() {
        FastList<String> properties = new FastList<String>(requested_properties);
        Iterator<PropertyUser> i = merged_users.iterator();
        while (i.hasNext()) {
            PropertyUser mergedUser = i.next();
            // get merged properties and set them to use us as the user
            Iterator<String> mergedProperties = mergedUser.get_requested_properties().iterator();
            while (mergedProperties.hasNext()) {
                String toAdd = mergedProperties.next();
                properties.add(toAdd);
            }
        }
        return properties;
    }

    /**
     * Get the list of required properties.
     *
     * @return The list of required properties.
     */
    public FastList<String> get_required_properties() {
        FastList<String> properties = new FastList<String>(required_properties);
        Iterator<PropertyUser> i = merged_users.iterator();
        while (i.hasNext()) {
            PropertyUser mergedUser = i.next();
            // get merged properties and set them to use us as the user
            Iterator<String> mergedProperties = mergedUser.get_required_properties().iterator();
            while (mergedProperties.hasNext()) {
                String toAdd = mergedProperties.next();
                properties.add(toAdd);
            }
        }
        return properties;
    }

    /**
     * Get the list of required if present properties.
     *
     * @return The list of required if present properties.
     */
    public FastList<String> get_required_if_present_properties() {
        FastList<String> properties = new FastList<String>(required_if_present_properties);
        Iterator i = merged_users.iterator();
        while (i.hasNext()) {
            PropertyUser mergedUser = (PropertyUser) i.next();
            // get merged properties and set them to use us as the user
            Iterator<String> mergedProperties = mergedUser.get_required_if_present_properties().iterator();
            while (mergedProperties.hasNext()) {
                String toAdd = mergedProperties.next();
                properties.add(toAdd);
            }
        }
        return properties;
    }

    /**
     * Get the list of supplied properties.
     *
     * @return The list of supplied properties.
     */
    public FastList<String> get_supplied_properties() {
        FastList<String> properties = new FastList<String>(supplied_properties);
        Iterator<PropertyUser> i = merged_users.iterator();
        while (i.hasNext()) {
            PropertyUser mergedUser = i.next();
            // get merged properties and set them to use us as the user
            Iterator<String> mergedProperties = mergedUser.get_supplied_properties().iterator();
            while (mergedProperties.hasNext()) {
                String toAdd = mergedProperties.next();
                properties.add(toAdd);
            }
        }
        return properties;
    }
}
