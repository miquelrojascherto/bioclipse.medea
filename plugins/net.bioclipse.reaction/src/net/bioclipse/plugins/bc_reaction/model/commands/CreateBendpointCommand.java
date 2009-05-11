package net.bioclipse.plugins.bc_reaction.model.commands;

import net.bioclipse.plugins.bc_reaction.model.AbstractConnectionModel;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
/**
 * 
 * @author Miguel Rojas
 */
public class CreateBendpointCommand extends Command {
	private AbstractConnectionModel connection;
	private Point location; 
	private int index; 

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		connection.addBendpoint(index, location);
	}

	public void setConnection(Object model) {
		connection = (AbstractConnectionModel) model;
	}

	public void setIndex(int i) {
		index = i;
	}

	public void setLocation(Point point) {
		location = point;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		connection.removeBendpoint(index);
	}
}

