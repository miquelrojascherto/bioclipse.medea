package net.bioclipse.plugins.bc_reaction.tools;

import java.io.File;

import net.bioclipse.plugins.bc_reaction.editparts.CompoundObjectEditPart;
import net.bioclipse.plugins.bc_reaction.model.AbstractObjectModel;

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
		System.out.println("path "+path);
		if(part instanceof CompoundObjectEditPart || path != null){
			File file = new File(path);
			System.out.println("exists "+file.exists());
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
