package net.bioclipse.plugins.bc_reaction.model.commands;

import java.util.ArrayList;
import java.util.List;

import net.bioclipse.plugins.bc_reaction.model.AbstractConnectionModel;
import net.bioclipse.plugins.bc_reaction.model.AbstractObjectModel;
import net.bioclipse.plugins.bc_reaction.model.ContentsModel;

import org.eclipse.gef.commands.Command;
/**
 * 
 * @author Miguel Rojas
 */
public class DeleteCommand extends Command{
	private ContentsModel contentsModel;
	private AbstractObjectModel reactionModel;
	
	private List sourceConnections = new ArrayList();
	private List targetConnections = new ArrayList();
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@SuppressWarnings("unchecked")
	public void execute(){
		sourceConnections.addAll(reactionModel.getModelSourceConnections());
		targetConnections.addAll(reactionModel.getModelTargetConnections());

		for (int i = 0; i < sourceConnections.size(); i++) {
			AbstractConnectionModel model = (AbstractConnectionModel) sourceConnections.get(i);
			model.detachSource();
			model.detachTarget();
		}

		for (int i = 0; i < targetConnections.size(); i++) {
			AbstractConnectionModel model =
				(AbstractConnectionModel) targetConnections.get(i);
			model.detachSource();
			model.detachTarget();
		}
		contentsModel.removeChild(reactionModel);
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
		contentsModel.addChild(reactionModel);
		for (int i = 0; i < sourceConnections.size(); i++) {
			AbstractConnectionModel model =
				(AbstractConnectionModel) sourceConnections.get(i);
			model.attachSource();
			model.attachTarget();
		}
		for (int i = 0; i < targetConnections.size(); i++) {
			AbstractConnectionModel model =
				(AbstractConnectionModel) targetConnections.get(i);
			model.attachSource();
			model.attachTarget();
		}

		sourceConnections.clear();
		targetConnections.clear();
	}
}
