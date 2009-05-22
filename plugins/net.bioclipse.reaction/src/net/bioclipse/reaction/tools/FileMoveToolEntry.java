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

import org.eclipse.gef.SharedImages;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.ToolEntry;
/**
 * 
 * @author Miguel Rojas
 */
public class FileMoveToolEntry extends ToolEntry {
	
	/**
	 * Constructor of the FileMoveToolEntry object
	 *
	 */
	public FileMoveToolEntry() {
		super("FileMoveTool", 
			  "FileMoveTool", 
			  SharedImages.DESC_SELECTION_TOOL_16, 
			  SharedImages.DESC_SELECTION_TOOL_24);
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.palette.ToolEntry#createTool()
	 */
	public Tool createTool(){
		return new FileMoveTool();
	}

}
