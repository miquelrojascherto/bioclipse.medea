package net.bioclipse.plugins.bc_reaction.tools;

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
