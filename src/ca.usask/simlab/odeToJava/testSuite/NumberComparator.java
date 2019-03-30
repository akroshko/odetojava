/* ./testSuite/NumberComparator.java
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

import java.text.DecimalFormat;

import org.jscience.mathematics.vectors.Float64Matrix;
import org.jscience.mathematics.vectors.Float64Vector;

import ca.usask.simlab.odeToJava.util.Check;
import ca.usask.simlab.odeToJava.util.Matrix;


/**
 * Compare the numbers against the reference numbers by computing absolute error,
 * relative error, or number of significant figures that is accurate.
 */
public class NumberComparator {
    /*
     * Indicate which significant figures to obtain.
     */
    public enum SIGFIG {
        /*
         * Indicate the minimum number significant figures.
         */
        MINIMUM,
        /*
         * Indicate the average number significant figures.
         */ 
        AVERAGE
    }
    
    /**
     * Find the absolute error between a number and a reference numbers.
     * 
     * @param number           The number to do the comparison with.
     * @param reference_number The reference to compare the number to.
     *
     * @return The absolute error of the two numbers.
     */
    public static double absoluteError(double number, double reference_number) {
        return Math.abs(number - reference_number);
    }
    

    /**
     * Find the absolute error between a vector and a reference vector.
     * 
     * @param numbers           The vector to do the comparison with.
     * @param reference_numbers The reference to compare the vector with.
     *
     * @return The absolute error between the two vectors.
     */
    public static double absoluteError(Float64Vector numbers, Float64Vector reference_numbers) {
        return Matrix.max(numbers.minus(reference_numbers));
    }
 
    /**
     * Find the maximum absolute error between a matrix and a reference matrix.
     * 
     * @param numbers           The matrix to do the comparison with.
     * @param reference_numbers The reference to compare the matrix to.
     *
     * @return The absolute error between the two matrices.
     */
    public static double absoluteError(Float64Matrix numbers, Float64Matrix reference_numbers) {
        return Matrix.max(numbers.minus(reference_numbers));
    }
   
    /**
     * Find the relative error between a number and a reference numbers.
     * 
     * @param number           The number to do the comparison with.
     * @param reference_number The reference to compare the number to.
     *
     * @return The relative error of the two numbers.
     */ 
    public static double relativeError(double number, double reference_number) {
        return Math.abs((number - reference_number) / reference_number);
    }
    
    /**
     * Find the relative error between a vector and a reference vector.
     * 
     * @param numbers           The vector to do the comparison with.
     * @param reference_numbers The reference to compare the vector with.
     *
     * @return The relative error between the two vectors.
     */ 
    public static double relativeError(double[] numbers, double[] reference_numbers) {
        return Matrix.max(Float64Vector.valueOf(numbers).minus(Float64Vector.valueOf(reference_numbers)));
    }
    
    /**
     * Find the relative error between a vector and a reference vector.
     * 
     * @param numbers           The vector to do the comparison with.
     * @param reference_numbers The reference to compare the vector with.
     *
     * @return The relative error between the two vectors.
     */  
    public static double relativeError(double[][] numbers, double[][] reference_numbers) {
        return Matrix.max(Float64Matrix.valueOf(numbers).minus(Float64Matrix.valueOf(reference_numbers)));
    }
    
    /**
     * Find the relative error between a vector and a reference vector.
     * 
     * @param numbers           The vector to do the comparison with.
     * @param reference_numbers The reference to compare the vector with.
     *
     * @return The relative error between the two vectors.
     */ 
    public static double relativeError(Float64Vector numbers, Float64Vector reference_numbers) {
        return Matrix.max(Matrix.divide(numbers.minus(reference_numbers), reference_numbers));
    } 


    /**
     * Find the relative error between a matrix and a reference matrix.
     * 
     * @param numbers           The matrix to do the comparison with.
     * @param reference_numbers The reference to compare the matrix to.
     *
     * @return The relative error between the two matrices.
     */ 
    public static double relativeError(Float64Matrix numbers, Float64Matrix reference_numbers) {
        return Matrix.max(Matrix.divide(numbers.minus(reference_numbers), reference_numbers));
    }

    /**
     * Find the number of significant figures that number matches a reference.
     * 
     * @param number           The number to find the matching significant figures for.
     * @param reference_number The reference number to compare to for finding the matching
     *                         significant figures.
     * @param threshold        The maximum number of significant figures.
     *
     * @return The number of significant figures that the number matches the reference number.
     */
    public static int numSigFigs(double number, double reference_number, int threshold) {
        
        final int DEFAULT_THRESHOLD = 256;
        final String SIGFIGSUFFIX = "E0";
        final String SIGFIGPREFIX = "0.";
        int numSigFigs = 0;
        int sigFigsThreshold = DEFAULT_THRESHOLD;
        String sigFigPattern = "";
        
        if (threshold > 1) {
            sigFigsThreshold = threshold;
        }
        
        while (numSigFigs < sigFigsThreshold) {
            DecimalFormat form = new DecimalFormat(SIGFIGPREFIX + sigFigPattern + SIGFIGSUFFIX);
            String s1 = form.format(number);
            String s2 = form.format(reference_number);
            if (!s1.equals(s2)) {
                return numSigFigs;
            }
            
            numSigFigs++;
            sigFigPattern = "0" + sigFigPattern;
        }
        
        return numSigFigs;
    }
    
    /**
     * Find the number of significant figures that number a matrix is accurate to.
     * 
     * @param numbers           The matrix to find the matching significant figures for.
     * @param reference_numbers The reference matrix to compare to for finding the matching
     * @param threshold         The maximum number of significant figures.
     * @param type              SIGFIG.AVERAGE or SIGFIG.MINIMUM.
     *
     * @return The number of significant figures that match the reference matrix.
     */
    public static int numSigFigs(Float64Matrix numbers, Float64Matrix reference_numbers, int threshold, SIGFIG type) {
        int minSigFigs = 0;
        int sumSigFigs = 0;
        
        for (int i = 0; i < reference_numbers.getNumberOfRows(); i++) {
            for (int j = 0; j < reference_numbers.getNumberOfColumns(); j++) {
                int numSigFigs = NumberComparator.numSigFigs(numbers.get(i, j).doubleValue(), reference_numbers.get(i, j).doubleValue(), threshold);
                sumSigFigs += numSigFigs;
                if (i == 0 && j == 0) {
                    minSigFigs = numSigFigs;
                } else if (numSigFigs < minSigFigs) {
                    minSigFigs = numSigFigs;
                }
            }
        }
        
        if (type == NumberComparator.SIGFIG.MINIMUM) {
            return minSigFigs;
        } else if (type == NumberComparator.SIGFIG.AVERAGE) {
            return sumSigFigs / (reference_numbers.getNumberOfColumns() * reference_numbers.getNumberOfRows());
        } else {
            throw new IllegalArgumentException("type must be SIGFIG.MINIMUM or SIGFIG.AVERAGE");
        }
    }
    
    /**
     * Find the number of significant figures that number a vector is accurate to.
     * 
     * @param numbers           The vector to find the matching significant figures for.
     * @param reference_numbers The reference vector to compare to for finding the matching
     *                          significant figures.
     * @param threshold         The maximum number of significant figures.
     * @param type              SIGFIG.AVERAGE or SIGFIG.MINIMUM.
     *
     * @return The number of significant figures that match the reference vector.
     */ 
    public static int numSigFigs(Float64Vector numbers, Float64Vector reference_numbers, int threshold, SIGFIG type) {
        int minSigFigs = 0;
        int sumSigFigs = 0;
        
        for (int i = 0; i < reference_numbers.getDimension(); i++) {
            int numSigFigs = NumberComparator.numSigFigs(numbers.getValue(i), reference_numbers.getValue(i), threshold);
            
            sumSigFigs += numSigFigs;
            
            if (i == 0) {
                minSigFigs = numSigFigs;
            } else if (numSigFigs < minSigFigs) {
                minSigFigs = numSigFigs;
            }
        }
        
        if (type == NumberComparator.SIGFIG.MINIMUM) {
            return minSigFigs;
        } else if (type == NumberComparator.SIGFIG.AVERAGE) {
            return sumSigFigs / reference_numbers.getDimension();
        } else {
            throw new IllegalArgumentException("type must be SIGFIG.MINIMUM or SIGFIG.AVERAGE");
        }
    }
}
