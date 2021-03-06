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

import net.bioclipse.cdk.domain.CDKMolecule;
import net.bioclipse.cdk.domain.CDKMoleculePropertySource;
import net.bioclipse.reaction.domain.CDKReactionPropertySource;
import net.bioclipse.reaction.editpolicies.RComponentEditPolicy;
import net.bioclipse.reaction.model.AbstractModel;
import net.bioclipse.reaction.model.AbstractObjectModel;
import net.bioclipse.reaction.model.CompoundObjectModel;
import net.bioclipse.reaction.model.ReactionObjectModel;

import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.views.properties.IPropertySource;
/**
 * 
 * @author Miguel Rojas
 */
public class ReactionROutPageEditPart extends ROutPageEditPart{
	private Image imageReaction;
	/**
	 * Constructor of the ReactionTreeEditPart object
	 */
	public ReactionROutPageEditPart(){
		super();
		imageReaction = new Image(Display.getCurrent(), getClass().getResourceAsStream("/icons/reactionLineOut.gif"));
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractTreeEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new RComponentEditPolicy());
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
		else if(evt.getPropertyName().equals(AbstractObjectModel.P_SOURCE_CONNECTION))
			refreshVisuals();
		
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractTreeEditPart#getImage()
	 */
	protected Image getImage(AbstractObjectModel model) {
		return imageReaction;
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter ) {
		if (IPropertySource.class.equals(adapter)) {
			AbstractModel abstractObject = (AbstractObjectModel)this.getModel();
			if(abstractObject instanceof CompoundObjectModel){
				CDKMolecule cdkMol= new CDKMolecule(((CompoundObjectModel)abstractObject).getIMolecule() );
				return new CDKMoleculePropertySource(cdkMol);
			}else if(abstractObject instanceof ReactionObjectModel){
//				CDKReaction cdkReact= new CDKReaction(((ReactionObjectModel)abstractObject).getIReaction() );
	            return new CDKReactionPropertySource((ReactionObjectModel)abstractObject);
			}
		}
		return super.getAdapter(adapter);
	}
}
