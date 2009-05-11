package net.bioclipse.plugins.bc_reaction.tools;

/**
 * This class counts the number of the objects which exist
 * 
 * @author Miguel Rojas
 *
 */
public class CounterObjects {
	public static int counterCompounds = 0;
	public static int counterReactions = 0;
	/**
	 * set a new compound in this list.
	 */
	static public void setCompound(){
		counterCompounds++;
	}
	/**
	 * set a new reaction in this list.
	 */
	static public void setReaction(){
		counterReactions++;
	}
	/**
	 * get the number of compounds.
	 */
	static public int getCompoundNumber(){
		return counterCompounds;
	}
	/**
	 * get the number of reactions.
	 */
	static public int getReactionNumber(){
		return counterReactions;
	}
}
