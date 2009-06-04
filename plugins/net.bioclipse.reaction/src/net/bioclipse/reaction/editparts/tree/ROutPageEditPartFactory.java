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

import net.bioclipse.reaction.model.AbstractObjectModel;
import net.bioclipse.reaction.model.CompoundObjectModel;
import net.bioclipse.reaction.model.ContentsModel;
import net.bioclipse.reaction.model.ReactionObjectModel;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
/**
 * Factory that maps model elements to TreeEditParts.
 * TreeEditParts are used in the outline view.
 *
 * @author Miguel Rojas
 */
public class ROutPageEditPartFactory implements EditPartFactory {
	/**
	 * 
	 */
	protected ContentsModel modelManager;
	
	/**
	 * 
	 * 
	 * @param modelmanager 
	 */
	public ROutPageEditPartFactory(ContentsModel modelmanager){
		this.modelManager=modelmanager;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart part = null;

		if (model instanceof ContentsModel){
			part = new ContentsROutPageEditPart();
		}else if (model instanceof AbstractObjectModel){
			AbstractObjectModel objectModel = (AbstractObjectModel) model;
			if(objectModel instanceof ReactionObjectModel)
				part = new ReactionROutPageEditPart();
			else if(objectModel instanceof CompoundObjectModel)
				part = new CompoundROutPageEditPart();
		}
		if (part != null)
			part.setModel(model);
		
		return part;
	}

}
