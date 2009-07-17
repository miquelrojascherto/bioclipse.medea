package net.bioclipse.plugins.model;

import net.bioclipse.model.BioResourcePropertySource;
import net.bioclipse.model.IBioResource;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * Provides the properties for a arffItem
 * 
 * @author     Miguel Rojas
 * @created    30. november 2006
 *
 */
public class ARFFResourcePropertySource extends BioResourcePropertySource implements IPropertySource {

	public ARFFResourcePropertySource(IBioResource item) {
		super(item);
	}
}
