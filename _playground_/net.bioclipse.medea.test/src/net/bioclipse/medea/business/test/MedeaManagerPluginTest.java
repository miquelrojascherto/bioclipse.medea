/*******************************************************************************
 * Copyright (c) 2007-2008 The Bioclipse Project and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.org�epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 * 
 * Contributors:
 *     shk3
 *     
 *******************************************************************************/
package net.bioclipse.medea.business.test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import junit.framework.Assert;
import net.bioclipse.cdk.business.CDKManager;
import net.bioclipse.cdk.domain.ICDKMolecule;
import net.bioclipse.core.MockIFile;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.ISpectrum;
import net.bioclipse.core.tests.AbstractManagerTest;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.medea.business.IMedeaManager;
import net.bioclipse.medea.business.MedeaManager;
import net.bioclipse.spectrum.business.ISpectrumManager;
import net.bioclipse.spectrum.business.SpectrumManager;
import net.bioclipse.spectrum.domain.IJumboSpectrum;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Test;

public class MedeaManagerPluginTest extends AbstractManagerTest{

    IMedeaManager medeamanager;

    //Do not use SPRING OSGI for this manager
    //since we are only testing the implementations of the manager methods
    public MedeaManagerPluginTest() {
        medeamanager = new MedeaManager();
    }
    
    public IBioclipseManager getManager() {
        return medeamanager;
    }

    @Test
    public void testPredictMassSpectrum_IMolecule() throws IOException, 
                                          BioclipseException, 
                                          CoreException, URISyntaxException {

        URI uri = getClass().getResource("/testFiles/30460-92-5-2d.mol").toURI();
        URL url = FileLocator.toFileURL(uri.toURL());
        String pathMolecule = url.getFile();
        CDKManager cdk = new CDKManager();
    	ICDKMolecule molecule = cdk.loadMolecule( new MockIFile(pathMolecule), new NullProgressMonitor() );
    	ISpectrum spectrum = medeamanager.predictMassSpectrum(molecule);
     	Assert.assertEquals(1,((IJumboSpectrum)spectrum).getJumboObject().getPeakListElements().size());
               
    }
    
    @Test
    public void testLearnMassSpectrum_IMolecule_ISpectrum_String() throws URISyntaxException, IOException, BioclipseException, CoreException{
        URI uri = getClass().getResource("/testFiles/30460-92-5.cml").toURI();
        URL url = FileLocator.toFileURL(uri.toURL());
        String pathSpectrum = url.getFile();
        ISpectrumManager sp = new SpectrumManager();
    	ISpectrum spectrum = sp.loadSpectrum(pathSpectrum );
    	
        uri = getClass().getResource("/testFiles/30460-92-5-2d.mol").toURI();
        url = FileLocator.toFileURL(uri.toURL());
        String pathMolecule = url.getFile();
        CDKManager cdk = new CDKManager();
    	ICDKMolecule molecule = cdk.loadMolecule( new MockIFile(pathMolecule), new NullProgressMonitor() );
    	
        IFile target=new MockIFile();
        medeamanager.learnMassSpectrum(molecule,spectrum,"");
    }
    
}
