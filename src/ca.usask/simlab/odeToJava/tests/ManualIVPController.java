/* ./tests/ManualIVPController.java
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
package ca.usask.simlab.odeToJava.tests;

import java.util.Vector;
import java.util.Arrays;
import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.vectors.Float64Vector;

import ca.usask.simlab.odeToJava.scheme.ERKButcherTableau;
import ca.usask.simlab.odeToJava.scheme.IMEXESDIRKButcherTableau;
import ca.usask.simlab.odeToJava.controller.IVP;
import ca.usask.simlab.odeToJava.controller.IVPController;
import ca.usask.simlab.odeToJava.modules.io.writers.DiskWriter;

import ca.usask.simlab.odeToJava.controller.IVP;
import ca.usask.simlab.odeToJava.controller.IVPController;
import ca.usask.simlab.odeToJava.modules.io.writers.DiskWriter;

import ca.usask.simlab.odeToJava.odes.OrbitArenstorfODE;
import ca.usask.simlab.odeToJava.odes.BurgersMOLODE;

/**
 * This class goes along with the example in the manual to demonstrate
 * the IVPController class.
 */
public class ManualIVPController {
    /**
     * The main method for the tutorial.
     *
     * @param args The standard variable for holding command-line arguments.
     */
    public static void main(String[] args) throws Exception {
        IVPController controller;
        IVP ivp;

        ivp = new IVP(new OrbitArenstorfODE(), Float64.valueOf(0), Float64Vector.valueOf(0.994, 0.0, 0.0, -2.00158510637908252240537862224)); 
        controller = new IVPController(ivp, Float64.valueOf(17.1));
        controller.set_butcher_tableau(ERKButcherTableau.get_DormandPrince54_tableau());
        controller.set_emb_error_control();
        controller.write_at_interval(0.1);
        controller.add_solution_writer(new DiskWriter("output/outputOrbitTutorial.txt")); 
        controller.run();
        
        ivp = new IVP(new BurgersMOLODE(127,Float64.valueOf(1./126.),Float64.valueOf(0.01)), Float64.ZERO, Float64Vector.valueOf(0.000000000000000000e+00, 2.493069173807287153e-02, 4.984588566069715621e-02, 7.473009358642423994e-02, 9.956784659581664754e-02, 1.243437046474851621e-01, 1.490422661761744150e-01, 1.736481776669303312e-01, 1.981461431993975508e-01, 2.225209339563143929e-01, 2.467573976902936173e-01, 2.708404681430051086e-01, 2.947551744109041527e-01, 3.184866502516844333e-01, 3.420201433256687129e-01, 3.653410243663949841e-01, 3.884347962746946825e-01, 4.112871031306115088e-01, 4.338837391175581204e-01, 4.562106573531629627e-01, 4.782539786213181876e-01, 4.999999999999999445e-01, 5.214352033794980024e-01, 5.425462638657593262e-01, 5.633200580636219534e-01, 5.837436722347897344e-01, 6.038044103254773809e-01, 6.234898018587334834e-01, 6.427876096865392519e-01, 6.616858375968593942e-01, 6.801727377709193556e-01, 6.982368180860727414e-01, 7.158668492597183297e-01, 7.330518718298262293e-01, 7.497812029677340950e-01, 7.660444431189780135e-01, 7.818314824680298036e-01, 7.971325072229223929e-01, 8.119380057158565034e-01, 8.262387743159949061e-01, 8.400259231507714031e-01, 8.532908816321554957e-01, 8.660254037844385966e-01, 8.782215733702285476e-01, 8.898718088114685454e-01, 9.009688679024190350e-01, 9.115058523116731370e-01, 9.214762118704076244e-01, 9.308737486442041353e-01, 9.396926207859083169e-01, 9.479273461671317014e-01, 9.555728057861406777e-01, 9.626242469500120302e-01, 9.690772862290779610e-01, 9.749279121818236193e-01, 9.801724878485438275e-01, 9.848077530122080203e-01, 9.888308262251285230e-01, 9.922392066001720634e-01, 9.950307753654014098e-01, 9.972037971811801293e-01, 9.987569212189223444e-01, 9.996891820008162455e-01, 1.000000000000000000e+00, 9.996891820008162455e-01, 9.987569212189223444e-01, 9.972037971811801293e-01, 9.950307753654014098e-01, 9.922392066001720634e-01, 9.888308262251285230e-01, 9.848077530122080203e-01, 9.801724878485438275e-01, 9.749279121818236193e-01, 9.690772862290779610e-01, 9.626242469500121413e-01, 9.555728057861406777e-01, 9.479273461671318124e-01, 9.396926207859084279e-01, 9.308737486442042464e-01, 9.214762118704076244e-01, 9.115058523116732481e-01, 9.009688679024191460e-01, 8.898718088114687674e-01, 8.782215733702286586e-01, 8.660254037844387076e-01, 8.532908816321557177e-01, 8.400259231507715141e-01, 8.262387743159947950e-01, 8.119380057158566144e-01, 7.971325072229226150e-01, 7.818314824680301367e-01, 7.660444431189780135e-01, 7.497812029677344281e-01, 7.330518718298264513e-01, 7.158668492597185518e-01, 6.982368180860728524e-01, 6.801727377709196887e-01, 6.616858375968596162e-01, 6.427876096865394739e-01, 6.234898018587335944e-01, 6.038044103254777140e-01, 5.837436722347901785e-01, 5.633200580636222865e-01, 5.425462638657595482e-01, 5.214352033794981134e-01, 5.000000000000003331e-01, 4.782539786213184652e-01, 4.562106573531631293e-01, 4.338837391175582314e-01, 4.112871031306119529e-01, 3.884347962746946270e-01, 3.653410243663952617e-01, 3.420201433256688794e-01, 3.184866502516849329e-01, 2.947551744109045968e-01, 2.708404681430054417e-01, 2.467573976902938393e-01, 2.225209339563149480e-01, 1.981461431993976063e-01, 1.736481776669306920e-01, 1.490422661761747203e-01, 1.243437046474853425e-01, 9.956784659581717489e-02, 7.473009358642467015e-02, 4.984588566069748233e-02, 2.493069173807309705e-02, 1.224646799147353207e-16));
        controller = new IVPController(ivp, Float64.valueOf(2.0));
        controller.set_butcher_tableau(IMEXESDIRKButcherTableau.get_KC43_tableau());
        controller.set_emb_error_control();
        controller.write_at_interval(0.1);
        controller.add_solution_writer(new DiskWriter("output/outputMOLTutorial.txt")); 
        controller.run();
    }
}
