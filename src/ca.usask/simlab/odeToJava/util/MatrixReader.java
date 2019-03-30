/* ./util/MatrixReader.java
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;

/**
 * Read a matrix of numbers from a file.
 */
public class MatrixReader {
    VectorReader vectorReader = new VectorReader();

    /**
     * Read in a file as a 2D array of doubles.
     *
     * @param in The input file stream.
     *
     * @return The 2D array of doubles.
     *
     * @throws IOException If there was an error opening or reading the files.
     */
    public double[][] read_matrix(InputStream in) throws IOException {
        java.util.Vector<java.util.Vector<Double>> v = this.read_vectors(in);
        double[][] a = new double[v.size()][];
        for (int i = 0; i < v.size(); i++) {
            java.util.Vector<Double> row = v.get(i);
            a[i] = new double[row.size()];
            for (int j = 0; j < row.size(); j++) {
                a[i][j] = row.get(j).doubleValue();
            }
        }
        return a;
    }

    /**
     * Read in a file as an array of doubles.
     *
     * @param in The input file stream.
     *
     * @return The array of doubles.
     *
     * @throws IOException If there was an error opening or reading the files.
     */
    public java.util.Vector<java.util.Vector<Double>> read_vectors(BufferedReader in) throws IOException {
        java.util.Vector<java.util.Vector<Double>> main = new java.util.Vector<java.util.Vector<Double>>();
        java.util.Vector<Double> row = new java.util.Vector<Double>();
        int size = 0;

        StandardTokenizer stream = new StandardTokenizer(in, false);
        int type = stream.nextToken();
        while (type != StreamTokenizer.TT_EOF) {
            // Changed from TT_NUM to TT_WORD so that
            // we can detect exponential numbers (Thian-Peng Ter)
            if (type == StreamTokenizer.TT_WORD) {
                if (row == null) {
                    row = new java.util.Vector<Double>();
                }
                row.add(Double.parseDouble(stream.sval));
            } else if (type == StreamTokenizer.TT_EOL) {
                if (size == 0) {
                    size = row.size();
                } else {
                    if (size != row.size()) {
                        throw null;
                    }
                }
                main.add(row);
                row = null;
            } else {
                throw new NumberFormatException(stream.sval);
            }
            type = stream.nextToken();
        }
        if (row != null) {
            if (size == 0) {
                size = row.size();
            } else {
                if (size != row.size()) {
                    throw null;
                }
            }
            main.add(row);
        }

        return main;
    }

    /**
     * Read in a file as an array of doubles.
     *
     * @param in The input file stream.
     *
     * @return A Vector of Double objects.
     * @throws IOException If there was an error opening or reading the files.
     */
    public java.util.Vector<java.util.Vector<Double>> read_vectors(InputStream in) throws IOException {
        return this.read_vectors(new BufferedReader(new InputStreamReader(in)));
    }
}
