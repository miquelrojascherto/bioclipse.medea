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

import net.bioclipse.reaction.model.AbstractObjectModel;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.widgets.Text;
/**
 * 
 * @author Miguel Rojas
 */
public class RDirectEditManager extends DirectEditManager{

	private AbstractObjectModel reactionModel;

	/**
	 * Constructor of the MyDirectEditManager object
	 * 
	 * @param source      The GraphicalEditPart
	 * @param editorType  The Class
	 * @param locator     The CellEditorLocator
	 */
	public RDirectEditManager(GraphicalEditPart source, Class editorType, CellEditorLocator locator) {
		super(source, editorType, locator);
		reactionModel = (AbstractObjectModel)source.getModel();
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.tools.DirectEditManager#initCellEditor()
	 */
	protected void initCellEditor() {
		getCellEditor().setValue(reactionModel.getText());
		Text text = (Text)getCellEditor().getControl();
		text.selectAll();
		
	}

}
