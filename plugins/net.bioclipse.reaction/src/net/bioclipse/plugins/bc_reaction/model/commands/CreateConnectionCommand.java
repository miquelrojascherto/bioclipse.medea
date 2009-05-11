package net.bioclipse.plugins.bc_reaction.model.commands;

import net.bioclipse.plugins.bc_reaction.model.AbstractConnectionModel;
import net.bioclipse.plugins.bc_reaction.model.AbstractObjectModel;

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
