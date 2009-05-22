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
package net.bioclipse.reaction.dnd;

import net.bioclipse.reaction.model.AbstractObjectModel;
import net.bioclipse.reaction.tools.FileMoveTool;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.AbstractTransferDragSourceListener;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.FileTransfer;

/**
 * 
 * @author Miguel Rojas
 */
public class MyFileDragSourceListener extends AbstractTransferDragSourceListener {

	/**
	 * Constructor of the MyFileDragSourceListener object
	 * 
	 * @param viewer The EditPartViewer
	 */
	public MyFileDragSourceListener(EditPartViewer viewer) {
		super(viewer, FileTransfer.getInstance());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.dnd.AbstractTransferDragSourceListener#dragStart(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public void dragStart(DragSourceEvent event) {
		if(!(getViewer().getEditDomain().getActiveTool() instanceof FileMoveTool))
			event.doit = false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragSetData(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public void dragSetData(DragSourceEvent event) {
		AbstractObjectModel model =	(AbstractObjectModel) ((EditPart) getViewer().getSelectedEditParts().get(0)).getModel();
		event.data = new String[] { model.getText()};
	}
}
