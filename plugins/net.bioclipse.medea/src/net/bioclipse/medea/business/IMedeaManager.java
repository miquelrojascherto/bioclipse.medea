/*******************************************************************************
 * Copyright (c) 2009  Egon Willighagen <egonw@users.sf.net>
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.org—epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 * 
 * Contact: http://www.bioclipse.net/    
 ******************************************************************************/
package net.bioclipse.medea.business;

import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.TestClasses;
import net.bioclipse.core.TestMethods;
import net.bioclipse.core.domain.IMolecule;
import net.bioclipse.core.domain.ISpectrum;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.reaction.domain.ICDKReactionScheme;

@TestClasses(
    "net.bioclipse.medea.business.test.APITest," +
    "net.bioclipse.medea.business.test.JavaMedeaManagerPluginTest"
)
public interface IMedeaManager extends IBioclipseManager {

    @PublishedMethod(
        params="IMolecule molecule",
        methodSummary="Predicts an EI mass spectrum for the given molecule"
    )
    @TestMethods("testPredictMassSpectrum_IMolecule")
    public ICDKReactionScheme predictMassSpectrum(IMolecule molecule);

    @PublishedMethod(
            params="IMolecule molecule,ISpectrum spectrum,String nameFile",
            methodSummary="Learns an EI mass spectrum for the given molecule and the corresponding spectrum"
        )
    @TestMethods("testLearnMassSpectrum_IMolecule_ISpectrum_String")
	public ICDKReactionScheme learnMassSpectrum(IMolecule molecule, ISpectrum spectrum, String nameFile);

}
