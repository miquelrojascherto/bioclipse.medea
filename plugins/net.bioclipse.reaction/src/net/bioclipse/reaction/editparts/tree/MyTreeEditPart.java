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
package net.bioclipse.reaction.editparts.tree;

import java.beans.PropertyChangeListener;

import net.bioclipse.reaction.model.AbstractModel;

import org.eclipse.gef.editparts.AbstractTreeEditPart;

/**
 * 
 * @author Miguel Rojas
 */
public abstract class MyTreeEditPart extends AbstractTreeEditPart implements PropertyChangeListener {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#activate()
	 */
	public void activate() {
		super.activate();
		((AbstractModel) getModel()).addPropertyChangeListener(this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#deactivate()
	 */
	public void deactivate() {
		((AbstractModel) getModel()).removePropertyChangeListener(this);
		super.deactivate();
	}
}
