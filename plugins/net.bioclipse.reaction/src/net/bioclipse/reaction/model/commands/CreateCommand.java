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
import net.bioclipse.reaction.model.ContentsModel;

import org.eclipse.gef.commands.Command;
/**
 * 
 * @author Miguel Rojas
 */
public class CreateCommand extends Command{
	private ContentsModel contentsModel;
	private AbstractObjectModel reactionModel;
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute(){
		contentsModel.addChild(reactionModel);
	}
	public void setContentsModel(Object model){
		contentsModel = (ContentsModel)model;
		
	}
	public void setReactionModel(Object model){
		reactionModel = (AbstractObjectModel)model;
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo(){
		contentsModel.removeChild(reactionModel);
	}
}
