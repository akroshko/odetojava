/* ./util/Check.java
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
import org.jscience.mathematics.vectors.Float64Matrix;
import org.jscience.mathematics.vectors.Float64Vector;

/**
 * This class contains utilities for extending the functionality of the classes in org.jscience.mathematics.vectors
 */
public class Check {
    /**
     * This method checks if a vector has a particular dimension.
     * 
     * @param a The vector to check.
     * @param b The particular dimension to verify.
     *
     * @return true if the vector has the particular dimension and false otherwise.
     */     
    public static boolean dimension(Float64Vector a, int b) {
        if (a.getDimension() != b) {
            return false;
        }
        return true;
    }

    /**
     * This method checks if two vectors are equal in dimension.
     * 
     * @param a The first vector to check.
     * @param b The second vector to check.
     *
     * @return true if the vectors are equal in dimension and false otherwise.
     */ 
    public static boolean dimension(Float64Vector a, Float64Vector b) {
        if (a.getDimension() != b.getDimension()) {
            return false;
        }
        return true;
    } 

    /**
     * This method checks if two matrices are equal dimensions.
     * 
     * @param a The first matrix to check.
     * @param b The second matrix to check.
     *
     * @return true if the matrices are equal dimensions and false otherwise.
     */
    public static boolean dimension(Float64Matrix a, Float64Matrix b) {
        if (a.getNumberOfColumns() != b.getNumberOfColumns() || a.getNumberOfRows() != b.getNumberOfRows()) {
            return false;
        }
        return true;
    }

    /**
     * This method checks if a number is positive.
     * 
     * @param a The number to check.
     *
     * @return true if the number is valid and not inf or Nan.
     */      
    public static boolean positive(double a) {
        if (valid(a) && !(a < 0)) {
            return true;
        } else {
            return false;
        }
    } 
    
    /**
     * This method checks if a number is positive.
     * 
     * @param a The number to check.
     *
     * @return true if the number is valid and not inf or Nan.
     */      
    public static boolean positive(Float64 a) {
        return positive(a.doubleValue());
    }
    
    /**
     * This method checks if a number is positive.
     * 
     * @param a The number to check.
     *
     * @return true if the number is valid and not inf or Nan.
     */     
    public static boolean valid(double a) {
        if (Double.isNaN(a) || Double.isInfinite(a)) {
            return false;
        } else {
            return true;
        }
    } 

    /**
     * This method checks if a number is valid.
     * 
     * @param a The number to check.
     *
     * @return true if the number is valid and not inf or Nan.
     */    
    public static boolean valid(Float64 a) {
        return valid(a.doubleValue());
    }
    
    /**
     * This method checks if all components of a vector is valid.
     * 
     * @param a The vector to check.
     *
     * @return true if the number is valid and not inf or Nan.
     */     
    public static boolean valid(Float64Vector a) {
        for (int i = 0; i < a.getDimension(); i++) {
            if (!valid(a.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * This method checks if all components of a matrix is valid.
     * 
     * @param a The matrix to check.
     *
     * @return true if the number is valid and not inf or Nan.
     */     
    public static boolean valid(Float64Matrix a) {
        for (int i = 0; i < a.getNumberOfRows(); i++) {
            for (int j = 0; i < a.getNumberOfColumns(); j++) {
                if (!valid(a.get(i, j))) {
                    return false;
                }
            }
        }
        return true;
    } 
}
