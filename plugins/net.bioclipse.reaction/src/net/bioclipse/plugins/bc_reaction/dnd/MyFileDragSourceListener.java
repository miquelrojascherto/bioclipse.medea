package net.bioclipse.plugins.bc_reaction.dnd;

import net.bioclipse.plugins.bc_reaction.model.AbstractObjectModel;
import net.bioclipse.plugins.bc_reaction.tools.FileMoveTool;

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
