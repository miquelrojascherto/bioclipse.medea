package net.bioclipse.reaction.model.commands;

import net.bioclipse.reaction.model.AbstractObjectModel;

import org.eclipse.gef.commands.Command;
/**
 * 
 * @author Miguel Rojas
 */
public class DirectEditCommand extends Command{
	private String oldText, newText;
	private AbstractObjectModel reactionModel;
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute(){
		oldText = reactionModel.getText();
		reactionModel.setText(newText);
	}
	public void setModel(Object model){
		reactionModel = (AbstractObjectModel) model;
	}
	public void setText(String text){
		newText = text;
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo(){
		reactionModel.setText(oldText);
	}
}
