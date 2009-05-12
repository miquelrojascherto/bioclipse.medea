package net.bioclipse.reaction.editor;

import java.util.List;

import net.bioclipse.cdk.business.Activator;
import net.bioclipse.cdk.business.ICDKManager;
import net.bioclipse.cdk.domain.ICDKReaction;
import net.bioclipse.core.business.BioclipseException;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IChemSequence;


/**
 * Create a multi-page editor for a reactions.
 * It has 2 pages:
 * <ul>
 * <li>page 0 shows the reacion viewer
 * <li>page 1 contains a nested the text editor.
 * </ul>
 * 
 * @author Miguel Rojas
 */
public class ReactionMultiPageEditor extends MultiPageEditorPart implements ISelectionListener {


	public static final String EDITOR_ID="net.bioclipse.reaction.editor.ReactionMultiPageEditor";
	private static final Logger logger = Logger.getLogger( ReactionMultiPageEditor.class.toString());
	private TextEditor textEditor;
	private IEditorInput editorInput;
	private ReactionEditor rEditor;
	
	@SuppressWarnings("unused")
	private ReactionMultiPageEditorContributor contributor;
	
	/**
	 * constructor of the ReactionMultiPageEditor object.
	 */
	public ReactionMultiPageEditor() {
		super();
		this.setPartName("ReactionMultiPageEditor");
	}
	
	/**
	 * Creates page 0 of the multi-page editor,
	 * which shows the reacion viewer.
	 */
	void createPage0() {
		try{
			rEditor = new ReactionEditor(editorInput);
			int index = this.addPage(rEditor, getEditorInput());
			setPageText(index, "Viewer");
			this.setActivePage(index);
			
		} catch (PartInitException e){
			e.printStackTrace();			
		}		
	}
	/**
	 * Creates page 1 of the multi-page editor,
	 * which contains a XML editor.
	 * @throws BioclipseException 
	 */
	void createPage1() throws BioclipseException {
	    List<ICDKReaction> resource = ReactionMultiPageEditor.getModelFromEditorInput( getEditorInput() );
	    textEditor = new TextEditor(  );
		
		try{

			int index = this.addPage((IEditorPart) textEditor, getEditorInput());
			setPageText(index,"Source");
			
		} catch (PartInitException e){
			e.printStackTrace();
		}
	}
	/**
	 * Creates the pages of the multi-page editor.
	 */
	protected void createPages() {
		createPage0();
		try {
            createPage1();
        } catch ( BioclipseException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	/**
	 * The <code>ReactionMultiPageEditor</code> implementation of this 
	 * <code>IWorkbenchPart</code> method disposes all nested editors.
	 * Subclasses may extend.
	 */
	public void dispose() {
		super.dispose();
	}
	/**
	 * Saves the multi-page editor's document.
	 */
	public void doSave(IProgressMonitor monitor) {
		IEditorPart editor = this.getActiveEditor();
		editor.doSave(monitor);
		try {
            rEditor.setDirty(false);
        } catch ( BioclipseException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		textEditor.doRevertToSaved();		
//		xmlEditor.setInput(getEditorInput());
	}
	/**
	 * Saves the multi-page editor's document as another file.
	 * Also updates the text for page 1's tab, and updates this multi-page editor's input
	 * to correspond to the nested editor's.
	 */
	public void doSaveAs() {
		
	}
	/* (non-Javadoc)
	 * Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {

	}
	/**
	 * The <code>ReactionMultiPageEditor</code> implementation of this method
	 * checks that the input is an instance of <code>BioResourceEditorInput</code>.
	 */
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		super.init(site, editorInput);
		this.editorInput = editorInput;
		
	}
	/* (non-Javadoc)
	 * Method declared on IEditorPart.
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}
	
	/**
	 * set the ReactionMultiPageEditorContributor object
	 * 
	 * @param con The ReactionMultiPageEditorContributor
	 */
	public void setContributor(ReactionMultiPageEditorContributor con){
		this.contributor = con;
	}
	
	/**
	 * Calculates the contents of page 0 when the it is activated.
	 */
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
//		if (this.getPageCount() > 1) {
//			if(newPageIndex == 1){
//				ReactionEditor reactionEditor = (ReactionEditor)getEditor(0);
//				IChemModel chemModel = reactionEditor.getChemModel();
//				if(chemModel != null) {
//					IChemObjectBuilder builder = chemModel.getBuilder();
//					IChemSequence cs = builder.newChemSequence();
//					cs.addChemModel(chemModel);
//					IChemFile cf = builder.newChemFile();
//					cf.addChemSequence(cs);
//					
//					ReactionResource rs = (ReactionResource)((BioResourceEditorInput)getEditorInput()).getBioResource();
//					rs.setParsedResource(cf);
//				}
//				if (!reactionEditor.isDirty())
//					textEditor.doRevertToSaved();
//
//				textEditor.setInput(getEditorInput());
//			}
//			if(newPageIndex==0){
//				if (textEditor == null)
//					return;
//				
//				IDocument doc = textEditor.getDocumentProvider().getDocument(getEditorInput());
//				ReactionResource rs = (ReactionResource)((BioResourceEditorInput)getEditorInput()).getBioResource();
//				rs.updateParsedResourceFromString(doc.get());
//				if (textEditor.isDirty())
//					rEditor.setDirty(true);
//				else
//					rEditor.setDirty(false);
				
//				ReactionEditor reactionEditor = (ReactionEditor)getEditor(0);
//				reactionEditor.updateViewer(reactionEditor.getModelFromEditorInputReaction());
//				if (reactionEditor.getJCPModel() != null){
//					reactionEditor.getJcpComposite().setSize(reactionEditor.getJcpComposite().getSize().x,reactionEditor.getJcpComposite().getSize().y-1);
//				}
		
//			}
//			
//			
//		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		System.out.println("selectionChanged");
	}
	
  public static List<ICDKReaction> getModelFromEditorInput(IEditorInput input) throws BioclipseException{

      Object file = input.getAdapter(IFile.class);
      if (!(file instanceof IFile)) {
          throw new BioclipseException(
                  "Invalid editor input: Does not provide an IFile");
      }

      IFile inputFile = (IFile) file;
      
      try {
            ICDKManager cdkManager = Activator.getDefault().getCDKManager();
            return cdkManager.loadReactions( inputFile, new NullProgressMonitor());
      } catch (Exception e) {
          e.printStackTrace();
      }
      
      return null;
  }

	

}
