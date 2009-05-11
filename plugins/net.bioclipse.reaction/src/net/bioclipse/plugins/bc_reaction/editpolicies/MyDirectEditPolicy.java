package net.bioclipse.plugins.bc_reaction.editpolicies;

import net.bioclipse.plugins.bc_reaction.model.commands.DirectEditCommand;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

/**
 * 
 * @author Miguel Rojas
 */
public class MyDirectEditPolicy extends DirectEditPolicy{
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.DirectEditPolicy#getDirectEditCommand(org.eclipse.gef.requests.DirectEditRequest)
	 */
	protected Command getDirectEditCommand(DirectEditRequest request) {
		DirectEditCommand command = new DirectEditCommand();
		command.setModel(getHost().getModel());
		command.setText((String)request.getCellEditor().getValue());
		return command;
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.DirectEditPolicy#showCurrentEditValue(org.eclipse.gef.requests.DirectEditRequest)
	 */
	protected void showCurrentEditValue(DirectEditRequest request) {
		
	}

}
