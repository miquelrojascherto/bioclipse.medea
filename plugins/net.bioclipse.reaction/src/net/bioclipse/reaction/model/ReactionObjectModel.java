/*******************************************************************************
 * Copyright (c) 2007-2009  Miguel Rojas <miguelrojasch@users.sf.net>, 
 *                          Stefan Kuhn <shk3@users.sf.net>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.org—epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.reaction.model;

import net.bioclipse.reaction.tools.CounterObjects;

import org.openscience.cdk.interfaces.IReaction;

/**
 * 
 * @author Miguel Rojas
 */
public class ReactionObjectModel extends AbstractObjectModel{
	
	private IReaction reaction;
	/**
	 * Constructor of the ReactionObjectModel object
	 */
	public ReactionObjectModel(){
		super();
		CounterObjects.setReaction();
		setText("Reac"+CounterObjects.getReactionNumber());
	}
	/**
	 * set the text of the ReactionObjectModel 
	 * @param text The Text
	 */
	public void setText(String text){
		super.setText(text);
		if(reaction != null)
			reaction.setID(text);
	}
	/**
	 * set a IReaction 
	 * @param reaction The IReaction object
	 */
	public void setIReaction(IReaction reaction) {
		this.reaction = reaction;
		
	}
	/**
	 * get the IReaction
	 * @return The IReaction object
	 */
	public IReaction getIReaction(){
		return reaction;
	}
}
