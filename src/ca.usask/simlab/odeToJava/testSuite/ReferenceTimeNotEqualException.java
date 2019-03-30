/* ./testSuite/ReferenceTimeNotEqualException.java
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
package ca.usask.simlab.odeToJava.testSuite;

/**
 * The exception that reference times are not equal.
 */ 
public class ReferenceTimeNotEqualException extends Exception {
    /**
     * An exception without an error message.
     */ 
    public ReferenceTimeNotEqualException() {
        super();
    }
    
    /**
     * An exception with an error message.
     *
     * @param message The string giving the error message.
     */ 
    public ReferenceTimeNotEqualException(String message) {
        super(message);
    }
    
    /**
     * An exception with an error message and a cause.
     *
     * @param message The string giving the error message.
     * @param cause Gives the cause of the error.
     */ 
    public ReferenceTimeNotEqualException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * An exception with a cause.
     *
     * @param cause Gives the cause of the error.
     */ 
    public ReferenceTimeNotEqualException(Throwable cause) {
        super(cause);
    }
}
