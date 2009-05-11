package net.bioclipse.plugins.bc_reaction.model;

import net.bioclipse.plugins.bc_reaction.tools.CounterObjects;

import org.openscience.cdk.interfaces.IReaction;

/**
 * 
 * @author Miguel Rojas
 */
public class ReactionObjectModel extends AbstractObjectModel{
	
	private IReaction reaction;
	/**
	 * Constructor of the ReactionObjectModel object
	 */
	public ReactionObjectModel(){
		super();
		CounterObjects.setReaction();
		setText("Reac"+CounterObjects.getReactionNumber());
	}
	/**
	 * set the text of the ReactionObjectModel 
	 * @param text The Text
	 */
	public void setText(String text){
		super.setText(text);
		if(reaction != null)
			reaction.setID(text);
	}
	/**
	 * set a IReaction 
	 * @param reaction The IReaction object
	 */
	public void setIReaction(IReaction reaction) {
		this.reaction = reaction;
		
	}
	/**
	 * get the IReaction
	 * @return The IReaction object
	 */
	public IReaction getIReaction(){
		return reaction;
	}
}
