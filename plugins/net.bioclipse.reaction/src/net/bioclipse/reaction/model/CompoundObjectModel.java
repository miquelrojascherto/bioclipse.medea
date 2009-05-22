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

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * 
 * @author Miguel Rojas
 */
public class CompoundObjectModel extends AbstractObjectModel{
	
	private IAtomContainer molecule = null;
	/**
	 * Constructor of the CompoundObjectModel object
	 */
	public CompoundObjectModel(){
		super();
		CounterObjects.setCompound();
		setText("mol"+CounterObjects.getCompoundNumber());
	}
	/**
	 * set the text of the CompoundObjectModel 
	 * @param text The Text
	 */
	public void setText(String text){
		super.setText(text);
		if(molecule != null)
			molecule.setID(text);
	}
	/**
	 * set a IMolecule 
	 * @param molecule The IMolecule object
	 */
	public void setIMolecule(IAtomContainer molecule) {
		this.molecule = molecule;
		
	}
	/**
	 * get the IMolecule
	 * @return The IMolecule object
	 */
	public IAtomContainer getIMolecule(){
	  if(molecule==null)
	        return DefaultChemObjectBuilder.getInstance().newAtomContainer();
		return molecule;
	}
	
}
