/* ./scheme/ARKButcherTableau.java
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
package ca.usask.simlab.odeToJava.scheme;

import org.jscience.mathematics.vectors.Float64Matrix;
import org.jscience.mathematics.vectors.Float64Vector;
import ca.usask.simlab.odeToJava.interpolant.Interpolant;
import ca.usask.simlab.odeToJava.interpolant.DefaultInterpolant;
import ca.usask.simlab.odeToJava.interpolant.ARKInterpolant;

/**
 * An abstract class that defines a Butcher tableau for a 2-additive
 * Runge-Kutta method.
 * <p>
 * Christopher A. Kennedy, Mark H. Carpenter. "Additive Runge-Kutta schemes for convection-diffusion-reaction equations.". Applied numerical mathematics, vol 44, pg 139-181, 2003.
 */
public abstract class ARKButcherTableau extends Scheme {
    /**
     * The first tableau representing the first additive component of the ODE.
     */
    protected ERKButcherTableau tableau_one;

    /**
     * The second tableau representing the second additive component of the ODE.
     */
    protected ERKButcherTableau tableau_two;

    /**
     * The order of convergence for the main method.
     */
    protected int scheme_order;

    /**
     * The order of convergence for the embedded method.
     */ 
    protected int embedded_order;

    /**
     * If this tableau has the First-Same-As-Last (FSAL) property.
     */
    protected boolean is_FSAL;

    /**
     * Creates a new ARKButcherTableau with the two given tableaux.
     *
     * @param tableau_one The tableau to be used for the first additive component
     *                    of the ODE.
     * @param tableau_two The tableau to be used for the second additive component 
     *                    of the ODE.                                            
     * @param scheme_order The order of convergence of the main method.
     * @param interpolant The interpolant to use for dense output.
     * @param name The name of the method.
     */ 

    public ARKButcherTableau(ERKButcherTableau tableau_one, ERKButcherTableau tableau_two, int scheme_order, Interpolant interpolant, String name) {
        this(tableau_one, tableau_two, scheme_order, 0, interpolant, name);
    }

    /**
     * Creates a new ARKButcherTableau with the given embedded tableaux.
     *
     * @param tableau_one The tableau to be used for the first additive component
     *                    of the ODE.
     * @param tableau_two The tableau to be used for the second additive component 
     *                    of the ODE.                                            
     * @param scheme_order  The order of convergence of the main method.
     * @param interpolant The interpolant to use for dense output.
     * @param name The name of the method.
     */ 
    public ARKButcherTableau(ERKButcherTableau tableau_one, ERKButcherTableau tableau_two, int scheme_order, int embedded_order, Interpolant interpolant, String name) {
        this.tableau_one = tableau_one;
        this.tableau_two = tableau_two;
        this.scheme_order = scheme_order;
        this.embedded_order = embedded_order;
        this.interpolant = interpolant;
        this.name = name;
        is_FSAL = this.tableau_one.is_FSAL() && this.tableau_two.is_FSAL();
    } 

    /**
     * Gets the first tableau.
     *
     * @return The first tableau.
     */
    public ERKButcherTableau get_tableau_one() {
        return tableau_one;
    }

    /**
     * Gets the second tableau.
     *
     * @return The second tableau.
     */ 
    public ERKButcherTableau get_tableau_two() {
        return tableau_two;
    }

    @Override
    public boolean is_single() {
        return false;
    }

    @Override
    public boolean is_additive() {
        return true;
    }

    @Override
    public boolean is_FSAL() {
        return is_FSAL;
    }

    @Override
    public int get_order() {
        return get_scheme_order();
    }

    @Override
    public boolean has_emb() {
        return tableau_one.has_emb() && tableau_two.has_emb();
    }

    @Override
    public int get_scheme_order() {
        return scheme_order;
    }

    @Override
    public int get_emb_order() {
        return embedded_order;
    }
}
