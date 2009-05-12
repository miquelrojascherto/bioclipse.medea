package net.bioclipse.reaction.model.commands;

import net.bioclipse.reaction.model.AbstractObjectModel;
import net.bioclipse.reaction.model.ContentsModel;

import org.eclipse.gef.commands.Command;
/**
 * 
 * @author Miguel Rojas
 */
public class CreateCommand extends Command{
	private ContentsModel contentsModel;
	private AbstractObjectModel reactionModel;
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute(){
		contentsModel.addChild(reactionModel);
	}
	public void setContentsModel(Object model){
		contentsModel = (ContentsModel)model;
		
	}
	public void setReactionModel(Object model){
		reactionModel = (AbstractObjectModel)model;
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo(){
		contentsModel.removeChild(reactionModel);
	}
}
