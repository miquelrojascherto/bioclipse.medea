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
package net.bioclipse.reaction.editparts;

import java.beans.PropertyChangeListener;

import net.bioclipse.reaction.model.AbstractModel;

import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
/**
 * 
 * @author Miguel Rojas
 */
abstract public class EditPartWithListener extends AbstractGraphicalEditPart implements PropertyChangeListener{
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	public void activate(){
		super.activate();
		((AbstractModel)getModel()).addPropertyChangeListener(this);
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	public void deactivate(){
		super.deactivate();
		((AbstractModel)getModel()).removePropertyChangeListener(this);
	}
}
