package net.bioclipse.plugins.bc_reaction.resource;

import net.bioclipse.model.BioResourcePropertySource;
import net.bioclipse.model.IBioResource;

import org.eclipse.ui.views.properties.IPropertySource;

/**
 * Provides the properties for a ReactionItem
 * 
 * @author     Miguel Rojas
 */
public class ReactionResourcePropertySource extends BioResourcePropertySource implements IPropertySource {
	/**
	 * Constructor of the ReactionResourcePropertySource object
	 * 
	 * @param item The IBioResource
	 */
	public ReactionResourcePropertySource(IBioResource item) {
		super(item);
	}	
}
