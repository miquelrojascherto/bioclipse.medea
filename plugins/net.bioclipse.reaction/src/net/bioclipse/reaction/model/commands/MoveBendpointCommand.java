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