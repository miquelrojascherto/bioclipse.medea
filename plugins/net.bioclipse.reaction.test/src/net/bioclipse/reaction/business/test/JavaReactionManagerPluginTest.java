/*******************************************************************************
 * Copyright (c) 2009  Miguel Rojas <miguelrojasch@users.sf.net>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.org—epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.reaction.business.test;


import net.bioclipse.reaction.business.Activator;

import org.junit.BeforeClass;
/**
 * 
 * @author Miguel Rojas
 */
public class JavaReactionManagerPluginTest extends AbstractReactionManagerPluginTest {

    @BeforeClass 
    public static void setupCDKManagerPluginTest() throws Exception {
        cdk = net.bioclipse.cdk.business.Activator.getDefault().getJavaCDKManager();
        reactionmanager = Activator.getDefault().getJavaManager();
    }

}
