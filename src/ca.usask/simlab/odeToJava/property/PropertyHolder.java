/* ./property/PropertyHolder.java
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

import java.util.Map;

import javolution.util.FastMap;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Matrix;
import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.scheme.Scheme;

/**
 * This class is the central data structure in ODEToJava that is responsible for
 * holding all the properties used by solvers and their modules.
 */
public class PropertyHolder {
    /**
     * The mapping of property names to their actual values.
     */
    protected Map<String, Object> property_values;

    /**
     * The default constructor.
     */
    public PropertyHolder() {
        property_values = new FastMap<String, Object>();
    }

    /**
     * Get a boolean property from the property holder.
     *
     * @param  name The unique string that identifies the property to be set.
     *
     * @return The value of the property.
     *
     * @throws PropertyNotFoundException
     */
    public boolean get_boolean_property(String name) throws PropertyNotFoundException {
        return (Boolean) get_property(name);
    }

    /**
     * Get an integer property from the property holder.
     *
     * @param  name The unique string that identifies the property to be set.
     *
     * @return The value of the property.
     *
     * @throws PropertyNotFoundException
     */
    public int get_int_property(String name) throws PropertyNotFoundException {
        return (Integer) get_property(name);
    }

    /**
     * Get a long integer property from the property holder.
     *
     * @param  name The unique string that identifies the property to be set.
     *
     * @return The value of the property.
     *
     * @throws PropertyNotFoundException
     */
    public long get_long_property(String name) throws PropertyNotFoundException {
        return (Long) get_property(name);
    }

    /**
     * Get a double property from the property holder.
     *
     * @param  name The unique string that identifies the property to be set.
     *
     * @return The value of the property.
     *
     * @throws PropertyNotFoundException
     */
    public double get_double_property(String name) throws PropertyNotFoundException {
        return (Double) get_property(name);
    }

    public Float64 getFloat64Property(String name) throws PropertyNotFoundException {
        return (Float64) get_property(name);
    }

    /**
     * Get a JScience Float64 property from the property holder.
     *
     * @param  name The unique string that identifies the property to be set.
     *
     * @return The value of the property.
     *
     * @throws PropertyNotFoundException
     */
    public Float64Vector get_Float64Vector_property(String name) throws PropertyNotFoundException {
        return (Float64Vector) get_property(name);
    }

    /**
     * Get an array of Float64Vector objects from the property holder.
     *
     * @param  name The unique string that identifies the property to be set.
     *
     * @return The value of the property.
     *
     * @throws PropertyNotFoundException
     */
    public Float64Vector[] get_Float64VectorArray_property(String name) throws PropertyNotFoundException {
        return (Float64Vector[]) get_property(name);
    }

    /**
     * Get an two-dimensional array of Float64Vector objects from
     * the property holder.
     *
     * @param  name The unique string that identifies the property to be set.
     *
     * @return The value of the property.
     *
     * @throws PropertyNotFoundException
     */
    public Float64Vector[][] get_Float64Vector_array_array_property(String name) throws PropertyNotFoundException {
        return (Float64Vector[][]) get_property(name);
    }

    /**
     * Get a Float64Matrix from the property holder.
     *
     * @param  name The unique string that identifies the property to be set.
     *
     * @return The value of the property.
     *
     * @throws PropertyNotFoundException
     */
    public Float64Matrix get_Float64Matrix_property(String name) throws PropertyNotFoundException {
        return (Float64Matrix) get_property(name);
    }

    /**
     * Get a Float64Matrix array from the property holder.
     *
     * @param  name The unique string that identifies the property to be set.
     *
     * @return The value of the property.
     *
     * @throws PropertyNotFoundException
     */
    public Float64Matrix[] get_Float64Matrix_array_property(String name) throws PropertyNotFoundException {
        return (Float64Matrix[]) get_property(name);
    }

    /**
     * Get a property from this property holder.
     *
     * The caller should never modify the returned object. Solvers or modules
     * should should supply a new version in order to update a value.
     *
     * @param  name The unique string that identifies the property to be set.
     *
     * @return The value of the property.
     *
     * @throws PropertyNotFoundException
     */
    public Object get_property(String name) throws PropertyNotFoundException {
        if (property_values.containsKey(name)) {
            return property_values.get(name);
        } else {
            throw new PropertyNotFoundException(name + " is not mapped to a supplier.");
        }
    }

    /**
     * Set a boolean property.
     *
     * @param name  The unique string that identifies the property to be set.
     * @param value The value to set this property as.
     */
    public void set_property(String name, boolean value) {
        set_property_internal(name, Boolean.valueOf(value));
    }

    /**
     * Set an integer property.
     *
     * @param name  The unique string that identifies the property to be set.
     * @param value The value to set this property as.
     */
    public void set_property(String name, int value) {
        set_property_internal(name, Integer.valueOf(value));
    }

    /**
     * Set a long integer property.
     *
     * @param name  The unique string that identifies the property to be set.
     * @param value The value to set this property as.
     */
    public void set_property(String name, long value) {
        set_property_internal(name, Long.valueOf(value));
    }

    /**
     * Set a double property.
     *
     * @param name  The unique string that identifies the property to be set.
     * @param value The value to set this property as.
     */
    public void set_property(String name, double value) {
        set_property_internal(name, Double.valueOf(value));
    }

    /**
     * Set a Jscience Float64 as a property.
     *
     * @param name  The unique string that identifies the property to be set.
     * @param value The value to set this property as.
     */
    public void set_property(String name, Float64 value) {
        set_property_internal(name, value);
    }

    /**
     * Set a String as a property.
     *
     * @param name  The unique string that identifies the property to be set.
     * @param value The value to set this property as.
     */
    public void set_property(String name, String value) {
        set_property_internal(name, value);
    }

    /**
     * Set a Jscience Float64Vector object as a property.
     *
     * @param name  The unique string that identifies the property to be set.
     * @param value The value to set this property as.
     */
    public void set_property(String name, Float64Vector value) {
        set_property_internal(name, value);
    }

    /**
     * Set an array of Jscience Float64Vector objects as a property.
     *
     * @param name  The unique string that identifies the property to be set.
     * @param value The value to set this property as.
     */
    public void set_property(String name, Float64Vector[] value) {
        set_property_internal(name, value);
    }

    /**
     * Set a two-dimensional array of Jscience Float64Vector objects as a property.
     *
     * @param name  The unique string that identifies the property to be set.
     * @param value The value to set this property as.
     */
    public void set_property(String name, Float64Vector[][] value) {
        set_property_internal(name, value);
    }

    /**
     * Set a Jscience Float64Matrix objects as a property.
     *
     * @param name  The unique string that identifies the property to be set.
     * @param value The value to set this property as.
     */
    public void set_property(String name, Float64Matrix value) {
        set_property_internal(name, value);
    }

    /**
     * Set an array of Jscience Float64Matrix objects as a property.
     *
     * @param name  The unique string that identifies the property to be set.
     * @param value The value to set this property as.
     */
    public void set_property(String name, Float64Matrix[] value) {
        set_property_internal(name, value);
    }

    /**
     * Set a Scheme object as a property.
     *
     * @param name  The unique string that identifies the property to be set.
     * @param value The value to set this property as.
     */
    public void set_property(String name, Scheme value) {
        set_property_internal(name, value);
    }

    /**
     * Actually sets the property.
     * <p>
     * The other methods are for convenience and to ensure all objects are copied or immutable.
     *
     * @param name  The unique string that identifies the property to be set.
     * @param value The value to set this property as.
     */
    protected void set_property_internal(String name, Object value) {
        property_values.put(name, value);
    }
}
