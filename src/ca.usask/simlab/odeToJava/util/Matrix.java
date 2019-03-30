/* ./util/Matrix.java
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
import org.jscience.mathematics.vectors.DimensionException;
import org.jscience.mathematics.vectors.Float64Matrix;
import org.jscience.mathematics.vectors.Float64Vector;

/**
 * This class provides matrix functions that are not provided by JScience in
 * order to support ODEToJava.  
 */
public class Matrix {
    /**
     * Finds the absolute values of all the entries of a vector.
     * 
     * @param a The vector to find the absolute values of.
     *
     * @return  The resultant vector with the absolute value taken of all
     *          entries.  
     */
    public static Float64Vector abs(Float64Vector a) {
        double[] array = toDouble(a);
        for (int i = 0; i < array.length; i++) {
            array[i] = Math.abs(array[i]);
        }
        return Float64Vector.valueOf(array);
    }  

    /**
     * Finds the absolute values of all the entries of a matrix.
     * 
     * @param a The matrix to find the absolute values of.
     *
     * @return  The resultant matrix with the absolute value taken of all
     *          entries. 
     */
    public static Float64Matrix abs(Float64Matrix a) {
        double[][] array = toDouble(a);
        for (double[] element : array) {
            for (int j = 0; j < element.length; j++) {
                element[j] = Math.abs(element[j]);
            }
        }
        return Float64Matrix.valueOf(array);
    }

    /**
     * Does component-wise division of two vectors.
     * 
     * @param a The dividend vector.
     * @param b The divisor vector.
     *
     * @return  The quotient vector resulting from a component-wise division.
     */
    public static Float64Vector divide(Float64Vector a, Float64Vector b) {
        double[] array = new double[b.getDimension()];
        if (!Check.dimension(a, b)) {
            throw new DimensionException();
        } 
        
        for (int i = 0; i < b.getDimension(); i++) {
            array[i] = a.getValue(i) / b.getValue(i);
        }
        return Float64Vector.valueOf(array);
    }
    
    /**
     * Does component-wise division of two vectors.
     * 
     * @param a The dividend vector.
     * @param b The divisor vector.
     *
     * @return  The quotient vector resulting from a component-wise division.
     */
    public static Float64Matrix divide(Float64Matrix a, Float64Matrix b) {
        double[][] array = new double[b.getNumberOfRows()][b.getNumberOfColumns()];
        if (!Check.dimension(a, b)) {
            throw new DimensionException();
        } 
        
        for (int i = 0; i < b.getNumberOfRows(); i++) {
            for (int j = 0; j < b.getNumberOfColumns(); j++) {
                array[i][j] = a.get(i, j).doubleValue() / b.get(i, j).doubleValue();
            }
        }
        return Float64Matrix.valueOf(array);
    } 

    /**
     * Method creates an identity matrix of a given size.
     *
     * @param n The size of the resultant identity matrix. 
     *
     * @return The resultant identity matrix of particular size.
     */
    public static Float64Matrix eye(int n) {
        double[][] array = new double[n][n];
        for (int i = 0; i < n; i++) {
            array[i][i] = 1;
        }
        return Float64Matrix.valueOf(array);
    } 

    /**
     * Creates an array of a given size where every element is a
     * given value.
     * 
     * @param a The value to fill the resultant vector with.
     * @param n The size of the resultant vector.
     *
     * @return The vector of the specified size filled with the specified value. 
     */
    public static Float64Vector fill(Float64 a, int n) {
        double[] array = new double[n];
        for (int i = 0; i < n; i++) {
            array[i] = a.doubleValue();
        }
        return Float64Vector.valueOf(array);
    }
    
    /**
     * Creates an array of a given size where every element is a
     * given value.
     * 
     * @param a The value to fill the resultant vector with.
     * @param m The number of rows of the resultant matrix.
     * @param n The number of columns of the resultant matrix.
     *
     * @return The matrix of the specified size filled with the specified value.  
     */
    public static Float64Matrix fill(Float64 a, int m, int n) {
        double[][] array = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                array[i][j] = a.doubleValue();
            }
        }
        return Float64Matrix.valueOf(array);
    }
    
    /**
     * Find the maximum magnitude of the components of a individual vector.
     * 
     * @param a The vector to find the maximum component of.
     *
     * @return The maximum magnitude of the components of the vector.
     */      
    public static double max(Float64Vector a) {
        double max = 0;
        for (int i = 0; i < a.getDimension(); i++) {
            if (Check.valid(a.get(i))) {
                max = Math.max(Math.abs(a.getValue(i)), max);
            }
        }
        return max;
    }    

    /**
     * Find the maximum magnitude of the components of a individual matrix.
     * 
     * @param a The matrix to find the maximum component of.
     *
     * @return The maximum magnitude of the components of the matrix.
     */  
    public static double max(Float64Matrix a) {      
        double max = 0;
        for (int i = 0; i < a.getNumberOfRows(); i++) {
            for (int j = 0; j < a.getNumberOfColumns(); j++) {
                if (Check.valid(a.get(i, j))) {
                    max = Math.max(a.get(i, j).abs().doubleValue(), max);
                }
            }
        }
        return max;
    }  

    /**
     * Finds the maximum value of two values
     * 
     * @param a The first value to use in the maximum operation.
     * @param b The second value to use in the maximum operation.
     *
     * @return The maximum of the two values.
     */
    public static Float64 max(Float64 a, Float64 b) {
        return Float64.valueOf(Math.max(a.doubleValue(), b.doubleValue()));
    } 

    /**
     * Compares each entry of a matrix with a number and returns
     * a vector containing component-wise maximum with that number.
     * 
     * @param a The vector to perform the maximum operation on.
     * @param b The number to perform the maximum operation on.
     *
     * @return The resultant vector containing the component-wise maximums.
     */
    public static Float64Vector max(Float64Vector a, double b) {
        double[] array = new double[a.getDimension()];
       
        for (int i = 0; i < a.getDimension(); i++) {
            array[i] = Math.max(a.getValue(i), b);
        }
        return Float64Vector.valueOf(array);
    }
 
    /**
     * Compares two vectors of the same size and returns a vector
     * with the component-wise maximum values
     *
     * @param a The first vector to use in the maximum operation.
     * @param b The second vector to use in the maximum operation.
     * 
     * @return The resultant vector with the component-wise maximum of the two
     *         original vectors. 
     */
    public static Float64Vector max(Float64Vector a, Float64Vector b) {
        double[] array = new double[a.getDimension()];
        // check vector sizes
        if (!Check.dimension(a, b)) {
            throw new DimensionException();
        }
        
        for (int i = 0; i < a.getDimension(); i++) {
            array[i] = Math.max(a.getValue(i), b.getValue(i));
        }
        
        return Float64Vector.valueOf(array);
    } 

    /**
     * Compares two matrices of the same size and returns a matrix
     * with the component-wise maximum values
     *
     * @param a The first matrix to use in the maximum operation.
     * @param b The second matrix to use in the maximum operation.
     * 
     * @return The resultant matrix with the component-wise maximum of the two
     *         original matrices.
     */
    public static Float64Matrix max(Float64Matrix a, Float64Matrix b) {
        double[][] array = new double[a.getNumberOfRows()][a.getNumberOfColumns()];
        // check matrix sizes
        if (!Check.dimension(a, b)) {
            throw new DimensionException();
        }
        
        for (int i = 0; i < a.getNumberOfRows(); i++) {
            for (int j = 0; i < a.getNumberOfColumns(); j++) {
                array[i][j] = Math.max(a.get(i, j).doubleValue(), b.get(i, j).doubleValue());
            }
        }
        return Float64Matrix.valueOf(array);
    } 
    
    /**
     * Finds the minimum value of two values.
     * 
     * @param a The first value to use in the minimum operation.
     * @param b The second value to use in the minimum operation.
     *
     * @return The minimum of the two values.
     */
    public static Float64 min(Float64 a, Float64 b) {
        return Float64.valueOf(Math.min(a.doubleValue(), b.doubleValue()));
    }

    /**
     * Does component-wise exponentiation of a vector by a scalar.
     * 
     * @param b The base vector.
     * @param e The exponent.
     *
     * @return The component-wise exponentiation of the base vector.
     */
    public static Float64Vector pow(Float64Vector b, double e) {
        double[] array = new double[b.getDimension()];
       
        for (int i = 0; i < b.getDimension(); i++) {
            array[i] = Math.pow(b.getValue(i), e);
        }
        return Float64Vector.valueOf(array);
    }
    
    /**
     * Does component-wise exponentiation of a matrix by a scalar.
     * 
     * @param b The base matrix.
     * @param e The exponent.
     *
     * @return The component-wise exponentiation of the base matrix.
     */ 
    public static Float64Matrix pow(Float64Matrix b, double e) {
        double[][] array = new double[b.getNumberOfRows()][b.getNumberOfColumns()];
       
        for (int i = 0; i < b.getNumberOfRows(); i++) {
            for (int j = 0; j < b.getNumberOfColumns(); j++) {
                array[i][j] = Math.pow(b.get(i, j).doubleValue(), e);
            }
        }
        return Float64Matrix.valueOf(array);
    } 


    /**
     * Calculates the root-mean-square of the values in a vector.
     * 
     * @param a The vector to perform the root-mean-square on.
     *
     * @return The calculated root-mean-square from the vector.
     */
    public static Float64 rms(Float64Vector a) {
        double accum = 0;
        for (int i = 0; i < a.getDimension(); i++) {
            accum += a.getValue(i) * a.getValue(i);
        }
        accum /= a.getDimension();
        return Float64.valueOf(Math.sqrt(accum));
    }  
    
    /**
     * Calculates the root-mean-square of the values in a matrix.
     * 
     * @param a The matrix to perform the root-mean-square on.
     *
     * @return The calculated root-mean-square from the matrix. 
     */
    public static double rms(Float64Matrix a) {
        double accum = 0;
        for (int i = 0; i < a.getNumberOfRows(); i++) {
            for (int j = 0; j < a.getNumberOfColumns(); j++) {
                accum += a.get(i, j).doubleValue() * a.get(i, j).doubleValue();
            }
        }
        accum /= a.getNumberOfRows() * a.getNumberOfColumns();
        return Math.sqrt(accum);
    }

    /**
     * Does a component-wise multiplication of two vectors.
     * 
     * @param a The first vector to multiply.
     * @param b The second vector to multiply.
     *
     * @return The component-wise product of the two vectors.
     */
    public static Float64Vector times(Float64Vector a, Float64Vector b) {
        double[] array = new double[b.getDimension()];
        if (!Check.dimension(a, b)) {
            throw new DimensionException();
        }
        for (int i = 0; i < b.getDimension(); i++) {
            array[i] = a.getValue(i) * b.getValue(i);
        }
        return Float64Vector.valueOf(array);
    }
    
    /**
     * Does component-wise multiplication of two matrices.
     * 
     * @param a The first matrix to multiply.  
     * @param b The second matrix to multiply. 
     *
     * @return The component-wise product of the two matrices.
     */
    public static Float64Matrix times(Float64Matrix a, Float64Matrix b) {
        double[][] array = new double[b.getNumberOfRows()][b.getNumberOfColumns()];
        if (!Check.dimension(a, b)) {
            throw new DimensionException();
        } 
        
        for (int i = 0; i < b.getNumberOfRows(); i++) {
            for (int j = 0; j < b.getNumberOfColumns(); j++) {
                array[i][j] = a.get(i, j).doubleValue() * b.get(i, j).doubleValue();
            }
        }
        return Float64Matrix.valueOf(array);
    }

    /**
     * Converts the JScience Float64Vector class into an array of doubles.
     * 
     * @param a The vector to convert into an array of doubles.
     *
     * @return The vector as an array of doubles.
     */
    public static double[] toDouble(Float64Vector a) {
        double[] array = new double[a.getDimension()];
        
        for (int i = 0; i < a.getDimension(); i++) {
            array[i] = a.getValue(i);
        }
        return array;
    }
    
    /**
     * Convert the JScience Float64Matrix class into an array of
     * doubles.
     * 
     * @param a The matrix to convert into an array of doubles.
     *
     * @return double[][] The matrix as an array of doubles.
     */
    public static double[][] toDouble(Float64Matrix a) {
        double[][] array = new double[a.getNumberOfRows()][a.getNumberOfColumns()];
        
        for (int i = 0; i < a.getNumberOfRows(); i++) {
            for (int j = 0; j < a.getNumberOfColumns(); j++) {
                array[i][j] = a.get(i, j).doubleValue();
            }
        }
        return array;
    } 
}
