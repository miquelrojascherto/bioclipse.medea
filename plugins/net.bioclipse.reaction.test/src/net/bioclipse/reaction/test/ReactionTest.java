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
package net.bioclipse.reaction.test;

import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.reaction.business.ReactionManager;

import org.junit.Test;
/**
 * 
 * @author Miguel Rojas
 */
public class ReactionTest {

	private ReactionManager reactionmanager;
	
	public ReactionTest(){
		reactionmanager = new ReactionManager();		    
	}
    
	public IBioclipseManager getManager() {
        return reactionmanager;
    }

	@Test public void testPredicting() throws Exception {
    	
    }
}
