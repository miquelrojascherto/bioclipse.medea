package net.bioclipse.reaction.model.commands;

import net.bioclipse.reaction.model.AbstractConnectionModel;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
/**
 * 
 * @author Miguel Rojas
 */
public class DeleteBendpointCommand extends Command {
	private AbstractConnectionModel connection;
	private Point oldLocation;
	private int index;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		oldLocation = (Point) connection.getBendpoints().get(index);
		connection.removeBendpoint(index);
	}

	public void setConnectionModel(Object model) {
		connection = (AbstractConnectionModel) model;
	}

	public void setIndex(int i) {
		index = i;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		connection.addBendpoint(index, oldLocation);
	}
}

