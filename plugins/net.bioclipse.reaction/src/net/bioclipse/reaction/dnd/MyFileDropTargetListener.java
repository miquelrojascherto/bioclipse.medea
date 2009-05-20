package net.bioclipse.reaction.dnd;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.dnd.FileTransfer;

/**
 * 
 * @author Miguel Rojas
 */
public class MyFileDropTargetListener extends AbstractTransferDropTargetListener{
	
	private ObjectModelFactory factory = new ObjectModelFactory();
	
	/**
	 * Constructor of MyFileDropTargetListener object
	 * 
	 * @param viewer the EditPartViewer value
	 */
	public MyFileDropTargetListener(EditPartViewer viewer){
		super(viewer,FileTransfer.getInstance());
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#createTargetRequest()
	 */
	protected Request createTargetRequest(){
		CreateRequest request = new CreateRequest();
		request.setFactory(factory);
		return request;
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#handleDrop()
	 */
	protected void handleDrop(){
		String path = ((String[]) getCurrentEvent().data)[0];
		factory.setPath(path);
		super.handleDrop();
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#updateTargetRequest()
	 */
	protected void updateTargetRequest() {
		((CreateRequest)getTargetRequest()).setLocation(getDropLocation());
		
	}

}
