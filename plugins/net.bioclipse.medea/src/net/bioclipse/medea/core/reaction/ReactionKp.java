package net.bioclipse.medea.core.reaction;

import org.openscience.cdk.Reaction;

/**
 * Concept of a reaction but containg more information. Necessary for Medea.
 *
 * @author Miguel Rojas
 */
public class ReactionKp extends Reaction{
	
	private static final long serialVersionUID = 8819089270062641456L;

	private double probability;
	private String nameR;
	
	/**
	 * Constructor of the ReactionKp object
	 * @see Reaction
	 */
	public ReactionKp() {
		super();
		
	}
	/**
	 * TODO - it should add into the setProperty(Object, Object) 
	 * get the probability
	 * 
	 * @return The probability of the reaction
	 */
	public double getProbability(){
		return probability;
	}
	/**
	 * TODO - it should add into the setProperty(Object, Object)
	 * set the probability
	 * 
	 * @param probability  The probability value
	 */
	public void setProbability(double probability){
		this.probability = probability;
	}
	/**
	 * set the process name of the reaction
	 * 
	 * @param string
	 */
	public void setNameReaction(String name) {
		nameR = name;
	}
	/**
	 * get the process name of the reaction
	 * 
	 * @return
	 */
	public String getNameReaction(){
		return nameR;
	}
}
