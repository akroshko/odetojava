/* ./util/StandardTokenizer.java
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;

/**
 * Tokenize a stream from a reader consisting of arrays of numbers.
 */
public class StandardTokenizer extends StreamTokenizer {
    
    /**
     * A default constructor.
     *
     * @param in     The input file stream.
     * @param vector Indicate whether a vector of arrays is being tokenized.
     */
    public StandardTokenizer(InputStream in, boolean vector) {
        this(new BufferedReader(new InputStreamReader(in)), vector);
    }
    
    /**
     * A default constructor.
     *
     * @param reader The reader whose stream needs to be tokenized.
     * @param vector Indicate whether a vector of arrays is being tokenized.
     */
    public StandardTokenizer(Reader reader, boolean vector) {
        super(reader);
        commentChar('#');
        commentChar('%');
        parseNumbers();
        slashSlashComments(true);
        slashStarComments(true);
        
        // The modifications below allow us to read exponential numbers.
        // (Thian-Peng Ter)
        // Modify it to turn off recognition of numbers
        ordinaryChars('0', '9');
        ordinaryChars('-', '-');
        ordinaryChars('+', '+');
        
        // Modify again so these chars are recognized as word
        wordChars('0', '9');
        wordChars('-', '-');
        wordChars('+', '+');
        
        if (vector) {
            eolIsSignificant(false);
        } else {
            eolIsSignificant(true);
        }
    }
    
}
