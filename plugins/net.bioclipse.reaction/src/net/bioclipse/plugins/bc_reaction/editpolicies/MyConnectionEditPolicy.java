package net.bioclipse.plugins.bc_reaction.editpolicies;

import net.bioclipse.plugins.bc_reaction.model.commands.DeleteConnectionCommand;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

/**
 * 
 * @author Miguel Rojas
 */
public class MyConnectionEditPolicy extends ConnectionEditPolicy {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.ConnectionEditPolicy#getDeleteCommand(org.eclipse.gef.requests.GroupRequest)
	 */
	protected Command getDeleteCommand(GroupRequest request) {
		DeleteConnectionCommand command = new DeleteConnectionCommand();
		command.setConnectionModel(getHost().getModel());
		return command;
	}
}
