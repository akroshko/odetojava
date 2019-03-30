/* ./util/Etc.java
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
package ca.usask.simlab.odeToJava.util;

import org.jscience.mathematics.numbers.Float64;

/**
 * Miscellaneous utilities for ODEToJava
 */
public class Etc {
    /**
     * Get 64-bit floating point machine roundoff.
     * 
     * @return The value of machine roundoff.
     */
    public static Float64 get_epsilon() {
        // default for most IEEE 754 64bit machines
        return Float64.valueOf(2.220446049250313 * 1.0E-16);
    }
    
    /**
     * Check that numbers are strictly positive and are not infinity or NaN.
     * 
     * @param check The number to check.
     */
    public static void checkPositive(Float64 check) {
        if (check.isNaN() || check.isInfinite() || check.doubleValue() <= 0) {
            throw new IllegalArgumentException();
        }
    }
}
