package net.bioclipse.plugins.bc_reaction.model.commands;

import net.bioclipse.plugins.bc_reaction.model.AbstractConnectionModel;

import org.eclipse.gef.commands.Command;
/**
 * 
 * @author Miguel Rojas
 */
public class DeleteConnectionCommand extends Command {
	private AbstractConnectionModel connection;
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		connection.detachSource();
		connection.detachTarget();
	}

	public void setConnectionModel(Object model) {
		connection = (AbstractConnectionModel) model;
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		connection.attachSource();
		connection.attachTarget();
	}
}

