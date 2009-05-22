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
package net.bioclipse.reaction.dnd;

import net.bioclipse.reaction.model.CompoundObjectModel;

import org.eclipse.gef.requests.CreationFactory;

/**
 * Class which sets a new object in the graphic from the palette.
 * 
 * @author Miguel Rojas
 */
public class ObjectModelFactory implements CreationFactory {
	private String path;
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.requests.CreationFactory#getNewObject()
	 */
	public Object getNewObject() {
		CompoundObjectModel model = new CompoundObjectModel();
		model.setText(path);
		return model;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.requests.CreationFactory#getObjectType()
	 */
	public Object getObjectType() {
		return CompoundObjectModel.class;
	}
	
	public void setPath(String s){
		path = s;
	}

}
