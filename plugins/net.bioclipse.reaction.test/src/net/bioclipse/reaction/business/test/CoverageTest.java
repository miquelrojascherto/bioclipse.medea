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

import net.bioclipse.core.tests.coverage.AbstractCoverageTest;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.reaction.business.IReactionManager;
import net.bioclipse.reaction.business.ReactionManager;

/**
 * JUnit tests for checking if the tested Manager is properly tested.
 * 
 * @author Miguel Rojas
 */
public class CoverageTest extends AbstractCoverageTest {
    
    private static ReactionManager manager = new ReactionManager();

    public IBioclipseManager getManager() {
        return manager;
    }

    public Class<? extends IBioclipseManager> getManagerInterface() {
        return IReactionManager.class;
    }
}
