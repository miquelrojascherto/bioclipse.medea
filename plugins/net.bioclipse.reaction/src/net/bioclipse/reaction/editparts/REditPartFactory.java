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
package net.bioclipse.reaction.editparts;

import net.bioclipse.cdk.jchempaint.widgets.JChemPaintEditorWidget;
import net.bioclipse.reaction.model.AbstractObjectModel;
import net.bioclipse.reaction.model.ArrowConnectionModel;
import net.bioclipse.reaction.model.CompoundObjectModel;
import net.bioclipse.reaction.model.ContentsModel;
import net.bioclipse.reaction.model.LineConnectionModel;
import net.bioclipse.reaction.model.ReactionObjectModel;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
/**
 * 
 * @author Miguel Rojas
 */
public class REditPartFactory implements EditPartFactory {
	
	private JChemPaintEditorWidget jcpe;
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object model){
		EditPart part = null;

		if(model instanceof ContentsModel)
			part = new ContentsEditPart();
		else if(model instanceof ReactionObjectModel){
			part = new ReactionObjectEditPart();
		}else if(model instanceof CompoundObjectModel){
			part = new CompoundObjectEditPart();
			((AbstractObjectModel)model).addJCP(jcpe);
		}else if (model instanceof LineConnectionModel){
			part = new LineConnectionEditPart();
		}else if(model instanceof ArrowConnectionModel){
			part = new ArrowConnectionEditPart();
		}
		part.setModel(model);
		return part;
	}
	/**
	 * 
	 */
	public void setEJP(JChemPaintEditorWidget jcpe){
		this.jcpe = jcpe;
	}
}
