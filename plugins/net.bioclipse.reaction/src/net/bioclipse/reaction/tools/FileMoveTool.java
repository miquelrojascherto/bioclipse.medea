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
package net.bioclipse.reaction.tools;

import java.io.File;

import net.bioclipse.reaction.editparts.CompoundObjectEditPart;
import net.bioclipse.reaction.model.AbstractObjectModel;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.swt.dnd.DragSourceEvent;
/**
 * 
 * @author Miguel Rojas
 */
public class FileMoveTool extends SelectionTool {
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.tools.AbstractTool#handleNativeDragStarted(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public boolean handleNativeDragStarted(DragSourceEvent event){
		EditPart part = getTargetEditPart();
		String path = ((AbstractObjectModel) part.getModel()).getText();
		if(part instanceof CompoundObjectEditPart || path != null){
			File file = new File(path);
			if(!file.exists())
				event.doit = false;
			else
				event.data = new String[]{path};
			return true;
		}
		event.doit = false;
		return super.handleNativeDragStarted(event);
	}
}
