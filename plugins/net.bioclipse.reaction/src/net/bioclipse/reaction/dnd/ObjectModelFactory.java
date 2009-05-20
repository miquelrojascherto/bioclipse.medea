package net.bioclipse.reaction.dnd;

import net.bioclipse.reaction.model.CompoundObjectModel;

import org.eclipse.gef.requests.CreationFactory;

/**
 * Class which sets a new object in the graphic from the palette.
 * 
 * @author Miguel Rojas
 */
public class ObjectModelFactory implements CreationFactory {
	private String path;
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.requests.CreationFactory#getNewObject()
	 */
	public Object getNewObject() {
		CompoundObjectModel model = new CompoundObjectModel();
		model.setText(path);
		return model;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.requests.CreationFactory#getObjectType()
	 */
	public Object getObjectType() {
		return CompoundObjectModel.class;
	}
	
	public void setPath(String s){
		path = s;
	}

}
