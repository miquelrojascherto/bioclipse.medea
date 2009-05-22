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
package net.bioclipse.reaction.model.commands;

import net.bioclipse.reaction.model.AbstractObjectModel;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
/**
 * 
 * @author Miguel Rojas
 */
public class ChangeConstraintCommand extends Command{
	private AbstractObjectModel reactionModel;
	private Rectangle constraint;
	private Rectangle oldConstraint;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute(){
		reactionModel.setConstraint(constraint);
	}
	public void setConstraint(Rectangle rect){
		constraint = rect;
	}
	public void setModel(Object model){
		reactionModel = (AbstractObjectModel) model;
		oldConstraint = reactionModel.getConstraint();
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo(){
		reactionModel.setConstraint(oldConstraint);
	}
}
