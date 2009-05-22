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
package net.bioclipse.reaction.tools;

/**
 * This class counts the number of the objects which exist
 * 
 * @author Miguel Rojas
 *
 */
public class CounterObjects {
	public static int counterCompounds = 0;
	public static int counterReactions = 0;
	/**
	 * set a new compound in this list.
	 */
	static public void setCompound(){
		counterCompounds++;
	}
	/**
	 * set a new reaction in this list.
	 */
	static public void setReaction(){
		counterReactions++;
	}
	/**
	 * get the number of compounds.
	 */
	static public int getCompoundNumber(){
		return counterCompounds;
	}
	/**
	 * get the number of reactions.
	 */
	static public int getReactionNumber(){
		return counterReactions;
	}
}
