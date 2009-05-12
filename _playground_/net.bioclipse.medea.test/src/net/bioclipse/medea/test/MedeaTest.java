/*******************************************************************************
 * Copyright (c) 2008 The Bioclipse Project and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ola Spjuth
 *     Jonathan Alvarsson
 *
 ******************************************************************************/
package net.bioclipse.medea.test;

import java.io.InputStream;

import net.bioclipse.plugins.medea.core.Medea;

import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;
import org.xmlcml.cml.element.CMLSpectrum;

public class MedeaTest {

    @Test public void testGenerating() throws Exception {
    	InputStream ins = MedeaTest.class.getResourceAsStream("/testFiles/30460-92-5-2d.mol");
        MDLV2000Reader reader = new MDLV2000Reader(ins);		
		IChemFile chemFile = new org.openscience.cdk.ChemFile();
        try {
            chemFile=(IChemFile)reader.read(chemFile);
        } catch (CDKException e) {
        	e.printStackTrace();
        }
        
        IAtomContainer container = ChemFileManipulator.getAllAtomContainers(chemFile).get(0);
        
        Medea medea = new Medea();
        medea.predictMS(container);
        CMLSpectrum cml = medea.getPredictedSpectrum();
        
    }

}
