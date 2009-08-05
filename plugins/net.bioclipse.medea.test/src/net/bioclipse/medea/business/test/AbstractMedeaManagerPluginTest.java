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

import java.net.URI;
import java.net.URL;

import junit.framework.Assert;
import net.bioclipse.cdk.business.ICDKManager;
import net.bioclipse.cdk.domain.ICDKMolecule;
import net.bioclipse.core.MockIFile;
import net.bioclipse.core.domain.ISpectrum;
import net.bioclipse.medea.business.IMedeaManager;
import net.bioclipse.reaction.domain.ICDKReactionScheme;
import net.bioclipse.spectrum.business.ISpectrumManager;

import org.eclipse.core.runtime.FileLocator;
import org.junit.Test;

public class AbstractMedeaManagerPluginTest {

    protected static ICDKManager   cdk;
    protected static IMedeaManager medeamanager;
    protected static ISpectrumManager sp;

    @Test
    public void testPredictMassSpectrum_IMolecule() throws Exception {
        URI uri = getClass().getResource("/testFiles/30460-92-5-2d.mol").toURI();
        URL url = FileLocator.toFileURL(uri.toURL());
        String pathMolecule = url.getFile();
    	ICDKMolecule molecule = cdk.loadMolecule( new MockIFile(pathMolecule));
    	ICDKReactionScheme scheme = medeamanager.predictMassSpectrum(molecule);
        Assert.assertNotNull(scheme);
        Assert.assertNotNull(scheme.getReactionScheme());
     	Assert.assertNotSame(0,scheme.getReactionScheme().getReactionCount());
               
    }
    
    @Test
    public void testLearnMassSpectrum_IMolecule_ISpectrum_String()
        throws Exception {
        URI uri = getClass().getResource("/testFiles/30460-92-5.cml").toURI();
        URL url = FileLocator.toFileURL(uri.toURL());
        String pathSpectrum = url.getFile();
    	ISpectrum spectrum = sp.loadSpectrum(pathSpectrum );
    	
        uri = getClass().getResource("/testFiles/30460-92-5-2d.mol").toURI();
        url = FileLocator.toFileURL(uri.toURL());
        String pathMolecule = url.getFile();
    	ICDKMolecule molecule = cdk.loadMolecule(new MockIFile(pathMolecule));

    	ICDKReactionScheme scheme = medeamanager.learnMassSpectrum(molecule,spectrum,null);
        Assert.assertNotNull(scheme);
        Assert.assertNotNull(scheme.getReactionScheme());
        Assert.assertNotSame(0,scheme.getReactionScheme().getReactionCount());
    }
    
}
