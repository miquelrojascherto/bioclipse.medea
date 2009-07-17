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
package net.bioclipse.medea.business.test;

import net.bioclipse.cdk.business.Activator;

import org.junit.BeforeClass;

public class JavaScriptMedeaManagerPluginTest
    extends AbstractMedeaManagerPluginTest {

    @BeforeClass 
    public static void setupCDKManagerPluginTest() throws Exception {
        cdk = Activator.getDefault().getJavaScriptCDKManager();
        medeamanager = net.bioclipse.medea.Activator.getDefault()
            .getJavaScriptManager();
        sp = net.bioclipse.spectrum.Activator.getDefault()
            .getJavaScriptSpectrumManager();
    }

}
