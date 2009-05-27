/*******************************************************************************
 * Copyright (c) 2007-2009  Miguel Rojas <miguelrojasch@users.sf.net>, 
 *                          Stefan Kuhn <shk3@users.sf.net>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.org—epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.reaction.editparts.tree;

import java.beans.PropertyChangeEvent;
import java.util.List;

import net.bioclipse.reaction.editpolicies.MyComponentEditPolicy;
import net.bioclipse.reaction.model.AbstractObjectModel;
import net.bioclipse.reaction.model.CompoundObjectModel;
import net.bioclipse.reaction.model.ReactionObjectModel;

import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
/**
 * 
 * @author Miguel Rojas
 */
public class ReactionTreeEditPart extends MyTreeEditPart {
	private Image imageReaction;
	private Image imageR;
	private Image imageP;
	private Image imageRP;
	/**
	 * Constructor of the ReactionTreeEditPart object
	 */
	public ReactionTreeEditPart(){
		super();
		imageReaction = new Image(Display.getCurrent(), getClass().getResourceAsStream("/icons/reactionLineOut.gif"));
		imageR = new Image(Display.getCurrent(), getClass().getResourceAsStream("/icons/reactant.gif"));
		imageP = new Image(Display.getCurrent(), getClass().getResourceAsStream("/icons/product.gif"));
		imageRP = new Image(Display.getCurrent(), getClass().getResourceAsStream("/icons/reactProduct.gif"));
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractTreeEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new MyComponentEditPolicy());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractTreeEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		AbstractObjectModel model = (AbstractObjectModel) getModel();
		setWidgetText(model.getText());
		setWidgetImage(getImage(model));
	}

	/*
	 * (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(AbstractObjectModel.P_TEXT))
			refreshVisuals();
		else if(evt.getPropertyName().equals(AbstractObjectModel.P_TARGET_CONNECTION))
			refreshVisuals();
		
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractTreeEditPart#getImage()
	 */
	protected Image getImage(AbstractObjectModel model) {
		Image image = null;

		if(model instanceof ReactionObjectModel){
	    	return imageReaction;
		}else{
			CompoundObjectModel compoundObject = (CompoundObjectModel)model;
			List<Object> list1 = compoundObject.getModelSourceConnections();
			List<Object> list2 = compoundObject.getModelTargetConnections();

			if(list1.size() == 0 && list2.size() != 0)
				return imageP;
			
			if(list2.size() == 0 && list1.size() != 0)
				return imageR;
			
			if(list2.size() != 0 && list1.size() != 0)
				return imageRP;
			
		}
	    
		return image;
	}
}
