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

import net.bioclipse.reaction.model.AbstractConnectionModel;
import net.bioclipse.reaction.model.AbstractObjectModel;

import org.eclipse.gef.commands.Command;
/**
 * 
 * @author Miguel Rojas
 */
public class CreateConnectionCommand extends Command {
	private AbstractObjectModel source, target;
	private AbstractConnectionModel connection;
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute(){
		if(source == null || target == null)
			return false;
		if(source.equals(target))
			return false;
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute(){
		connection.attachSource();
		connection.attachTarget();
	}
	
	public void setConnection(Object model){
		connection = (AbstractConnectionModel) model;
	}
	
	public void setSource(Object model){
		source = (AbstractObjectModel) model;
		connection.setSource(source);
	}
	public void setTarget(Object model){
		target = (AbstractObjectModel) model;
		connection.setTarget(target);
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo(){
		connection.detachSource();
		connection.detachTarget();
	}
	
}
