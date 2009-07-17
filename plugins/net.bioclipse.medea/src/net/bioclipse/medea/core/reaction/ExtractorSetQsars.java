package net.bioclipse.medea.core.reaction;

import java.util.ArrayList;
/**
 * Class which extract the qsar from a reactions.
 * 
 * @author Miguel Rojas
 */
public interface ExtractorSetQsars {
	/**
	 * get an ArrayList with all descriptors for a this Reaction
	 * 
	 * @param reactionKp The ReactionKp object
	 * @return An ArrayList with all descriptors results.
	 */
	public ArrayList<Double> getQsars(ReactionKp reactionKp);
	
}
