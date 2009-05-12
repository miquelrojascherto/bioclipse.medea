package net.bioclipse.reaction.editpolicies;

import net.bioclipse.reaction.model.commands.DeleteCommand;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
/**
 * 
 * @author Miguel Rojas
 */
public class MyComponentEditPolicy extends ComponentEditPolicy{
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.ComponentEditPolicy#createDeleteCommand(org.eclipse.gef.requests.GroupRequest)
	 */
	protected Command createDeleteCommand(GroupRequest deletRequest){
		DeleteCommand command = new DeleteCommand();
		command.setContentsModel(getHost().getParent().getModel());
		command.setReactionModel(getHost().getModel());
		return command;
	}
}
