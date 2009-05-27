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

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author Miguel Rojas
 */
public class ContentsModel extends AbstractModel {
	public static final String P_CHILDREN ="_children";
	private List<Object> children = new ArrayList<Object>();
	/**
	 * Add a children object
	 * 
	 * @param child The object
	 */
	public void addChild(Object child){
		children.add(child);
		firePropertyChange(P_CHILDREN,null,null);
	}
	/**
	 * Get a List of its children objects.
	 * 
	 * @return A List with the Children objects
	 */
	public List<Object> getChildren(){
		return children;
	}
	/**
	 * Remove the children object.
	 * 
	 * @param child  The children object to remove
	 */
	public void removeChild(Object child){
		children.remove(child);
		firePropertyChange(P_CHILDREN,null,null);
	}
	
}
