package net.bioclipse.reaction.model.commands;

import net.bioclipse.reaction.model.AbstractObjectModel;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
/**
 * 
 * @author Miguel Rojas
 */
public class ChangeConstraintCommand extends Command{
	private AbstractObjectModel reactionModel;
	private Rectangle constraint;
	private Rectangle oldConstraint;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute(){
		reactionModel.setConstraint(constraint);
	}
	public void setConstraint(Rectangle rect){
		constraint = rect;
	}
	public void setModel(Object model){
		reactionModel = (AbstractObjectModel) model;
		oldConstraint = reactionModel.getConstraint();
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo(){
		reactionModel.setConstraint(oldConstraint);
	}
}
