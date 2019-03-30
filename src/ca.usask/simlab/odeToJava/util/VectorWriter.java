/* ./util/VectorWriter.java
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

import java.io.OutputStream;
import java.io.PrintWriter;

import org.jscience.mathematics.vectors.Float64Vector;

/**
 * Write a vector of numbers to a file.
 */
public class VectorWriter {
    /**
     * Write an array of doubles to a file.
     * 
     * @param a   The array of doubles.
     * @param out The file stream to write to.
     */
    public void write_array(double[] a, OutputStream out) {
        PrintWriter pw = new PrintWriter(out);
        this.write_array(a, pw);
    }
    
    /**
     * Write out a Float64Vector object to a file.
     * 
     * @param v   The Float64Vector to write.
     * @param out The file stream to write to.
     */
    public void write_vector(Float64Vector v, OutputStream out) {
        double[] a = new double[v.getDimension()];
        for (int i = 0; i < v.getDimension(); i++) {
            a[i] = v.getDimension();
        }
        this.write_array(a, out);
    }
    
    /**
     * Write an array to a PrinterWriter object. 
     *
     * @param a  The array of doubles.
     * @param pw The PrinterWriter object to write to.
     */
    public void write_array(double a[], PrintWriter pw) {
        for (int i = 0; i < a.length; i++) {
            pw.print(a[i]);
            if (i < a.length - 1) {
                pw.print('\t');
            }
        }
        pw.flush();
    }
}
