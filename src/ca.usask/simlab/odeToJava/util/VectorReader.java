/* ./util/VectorReader.java
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
 * Read in a vector of numbers from a file.
 */
public class VectorReader {
    /**
     * Read in a vector of Double objects from a file.
     *
     * @param in The input file stream.
     *
     * @return A vector of doubles that have been read.
     *
     * @throws IOException If there was an error opening or reading the files. 
     */
    public java.util.Vector<Double> read_vector(BufferedReader in) throws IOException {
        java.util.Vector<Double> v = new java.util.Vector<Double>();
        
        StandardTokenizer stream = new StandardTokenizer(in, true);
        int type = stream.nextToken();
        while (type != StreamTokenizer.TT_EOF) {
            if (type == StreamTokenizer.TT_WORD) {
                v.add(Double.parseDouble(stream.sval));
            } else {
                throw new NumberFormatException(stream.sval);
            }
            type = stream.nextToken();
        }
        return v;
        
    }
}
