package net.bioclipse.reaction.editpolicies;

import net.bioclipse.reaction.model.AbstractObjectModel;
import net.bioclipse.reaction.model.commands.ChangeConstraintCommand;
import net.bioclipse.reaction.model.commands.CreateCommand;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

/**
 * 
 * @author Miguel Rojas
 */
public class MyXYLayoutEditPolicy extends XYLayoutEditPolicy {
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createAddCommand(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	protected Command createAddCommand(EditPart child, Object constraint) {
		// TODO Auto-generated method stub
		return null;
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createChangeConstraintCommand(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
		ChangeConstraintCommand command = new ChangeConstraintCommand();
		command.setModel(child.getModel());
		command.setConstraint((Rectangle)constraint);
		
		return command;
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
	 */
	protected Command getCreateCommand(CreateRequest request) {
		CreateCommand command = new CreateCommand();
		
		Rectangle constraint = (Rectangle)getConstraintFor(request);
		AbstractObjectModel model = (AbstractObjectModel)request.getNewObject();
		model.setConstraint(constraint);
		
		command.setContentsModel(getHost().getModel());
		command.setReactionModel(model);
		return command;
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getDeleteDependantCommand(org.eclipse.gef.Request)
	 */
	protected Command getDeleteDependantCommand(Request request) {
		// TODO Auto-generated method stub
		return null;
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.EditPolicy#getCommand(org.eclipse.gef.Request)
	 */
//	public Command getCommand(Request request){
//		return super.getCommand(request);
//	}

	

}
