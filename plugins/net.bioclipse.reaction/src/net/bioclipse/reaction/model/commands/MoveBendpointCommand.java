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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
/**
 * 
 * @author Miguel Rojas
 */
public class MoveBendpointCommand extends Command {
	private AbstractConnectionModel connection;
	private Point oldLocation, newLocation;
	private int index;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		oldLocation = (Point) connection.getBendpoints().get(index);
		connection.replaceBendpoint(index, newLocation);
	}

	public void setConnectionModel(Object model) {
		connection = (AbstractConnectionModel) model;
	}

	public void setIndex(int i) {
		index = i;
	}

	public void setNewLocation(Point point) {
		newLocation = point;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		connection.replaceBendpoint(index, oldLocation);
	}

}