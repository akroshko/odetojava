/* ./util/FileSolutionReader.java
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.jscience.mathematics.vectors.Float64Matrix;
import org.jscience.mathematics.vectors.Float64Vector;

/**
 * This class reads the time vector and the solution matrix from a text file.
 * <p>
 * Each row of the text file consists of the time <i>t</i> followed by the
 * solution vector at time <i>t</i>.
 */
public class FileSolutionReader {
    /**
     * The filename to read from.
     */
    protected String filename;

    /**
     * The class to use for reading.
     */
    protected MatrixReader reader;

    /**
     * The solution values that have been read.
     */
    protected Float64Matrix solution;

    /**
     * The times that have been read.
     */
    protected Float64Vector time;

    /**
     * Construct a solution reader for file
     *
     * @param filename The name of a text file.
     */
    public FileSolutionReader(String filename) {
        reader = new MatrixReader();
        this.filename = filename;
    }

    /**
     * Return the solution in matrix.
     * <p>
     * Each row of the matrix is a solution vector at a particular time.
     *
     * @return The solution values as a matrix.
     */
    public Float64Matrix get_solution() {

        return solution;
    }

    /**
     * Get the time vector.
     *
     * @return A vector of time.
     */
    public Float64Vector get_time() {
        return time;
    }

    /**
     * Read the time vector and the solution from the file.
     *
     * @throws FileNotFoundException                The file does not exist.
     * @throws IOException                          There was an I/O error reading the file.
     */
    public void readSolution() throws FileNotFoundException, IOException {

        double[][] matrixData = reader.read_matrix(new FileInputStream(filename));

        if (matrixData.length == 0) {
            time = null;
            solution = null;
            return;
        }

        double[] time = new double[matrixData.length];
        double[][] solution = new double[matrixData.length][matrixData[0].length - 1];

        for (int i = 0; i < matrixData.length; i++) {

            time[i] = matrixData[i][0];

            for (int j = 1; j < matrixData[0].length; j++) {
                solution[i][j - 1] = matrixData[i][j];
            }
        }
        this.time = Float64Vector.valueOf(time);
        this.solution = Float64Matrix.valueOf(solution);
    }
}
