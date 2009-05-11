package net.bioclipse.plugins.bc_reaction.editpolicies;

import net.bioclipse.plugins.bc_reaction.model.commands.CreateConnectionCommand;
import net.bioclipse.plugins.bc_reaction.model.commands.ReconnectConnectionCommand;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

/**
 * 
 * @author Miguel Rojas
 */
public class MyGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy{
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getConnectionCompleteCommand(org.eclipse.gef.requests.CreateConnectionRequest)
	 */
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		CreateConnectionCommand command =
			(CreateConnectionCommand)request.getStartCommand();
		command.setTarget(getHost().getModel());
		return command;
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getConnectionCreateCommand(org.eclipse.gef.requests.CreateConnectionRequest)
	 */
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		CreateConnectionCommand command = new CreateConnectionCommand();
		command.setConnection(request.getNewObject());
		command.setSource(getHost().getModel());
		
		request.setStartCommand(command);
		return command;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectTargetCommand(org.eclipse.gef.requests.ReconnectRequest)
	 */
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		ReconnectConnectionCommand command = new ReconnectConnectionCommand();
		command.setConnectionModel(request.getConnectionEditPart().getModel());
		command.setNewTarget(getHost().getModel());
		return command;
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectSourceCommand(org.eclipse.gef.requests.ReconnectRequest)
	 */
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		ReconnectConnectionCommand command = new ReconnectConnectionCommand();
		command.setConnectionModel(request.getConnectionEditPart().getModel());
		command.setNewSource(getHost().getModel());
		return command;
	}

}
