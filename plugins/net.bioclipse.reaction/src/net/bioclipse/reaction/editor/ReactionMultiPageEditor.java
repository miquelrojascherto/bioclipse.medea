/*******************************************************************************
 * Copyright (c) 2007-2009  Miguel Rojas, Stefan Kuhn
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.org—epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.reaction.editor;

import java.io.IOException;
import java.util.List;

import net.bioclipse.cdk.business.Activator;
import net.bioclipse.cdk.business.ICDKManager;
import net.bioclipse.cdk.domain.ICDKReaction;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.util.LogUtils;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.openscience.cdk.exception.CDKException;


/**
 * Create a multi-page editor for a reactions.
 * It has 2 pages:
 * <ul>
 * <li>page 0 shows the reacion viewer
 * <li>page 1 contains a nested the text editor.
 * </ul>
 */
public class ReactionMultiPageEditor extends MultiPageEditorPart implements ISelectionListener {


	public static final String EDITOR_ID="net.bioclipse.reaction.editor.ReactionMultiPageEditor";
	private static final Logger logger = Logger.getLogger( ReactionMultiPageEditor.class.toString());
	private TextEditor textEditor;
	private IEditorInput editorInput;
	private ReactionEditor rEditor;
	
	/**
	 * constructor of the ReactionMultiPageEditor object.
	 */
	public ReactionMultiPageEditor() {
		super();
		this.setPartName("ReactionMultiPageEditor");
	}
	
	/**
	 * Creates page 0 of the multi-page editor,
	 * which shows the reaction viewer.
	 */
	void createPage0() {
		try{
			rEditor = new ReactionEditor(editorInput);
			int index = this.addPage(rEditor, getEditorInput());
			setPageText(index, "Viewer");
			this.setActivePage(index);			
		} catch (PartInitException e){
			LogUtils.handleException( e, logger );
		}		
	}
	
	/**
	 * Creates page 1 of the multi-page editor,
	 * which contains a XML editor.
	 * @throws BioclipseException 
	 */
	void createPage1() {
		try{
		  textEditor = new TextEditor(  );		    
			int index = this.addPage((IEditorPart) textEditor, getEditorInput());
			setPageText(index,"Source");
			
		} catch (PartInitException e){
			LogUtils.handleException( e, logger );
		}
	}
	/**
	 * Creates the pages of the multi-page editor.
	 */
	protected void createPages() {
		createPage0();
    createPage1();
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
      this.showBusy( true );
      // Synch from ReactionEditor to texteditor
      updateTextEditor();
      textEditor.doSave( monitor );
  		try {
          rEditor.setDirty(false);
      } catch ( BioclipseException e ) {
          LogUtils.handleException( e, logger );
      }
  		textEditor.doRevertToSaved();		
      firePropertyChange( IEditorPart.PROP_DIRTY );
      this.showBusy( false );
	}
	
	
	private void updateTextEditor() {

        // TODO update text editor from ReactionEditor
        
    }

    /**
	 * Saves the multi-page editor's document as another file.
	 * Also updates the text for page 1's tab, and updates this multi-page editor's input
	 * to correspond to the nested editor's.
	 */
	public void doSaveAs() {
		//TODO
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
	 * Calculates the contents of page 0 when the it is activated.
	 */
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		if(newPageIndex == 1){
		    updateTextEditor();
		}else{
		    updateReactionEditor();
		}
	}

	private void updateReactionEditor() {

        // TODO update reaction editor from text
        
    }

    /*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		//TODO when is this used?
	}
	
  public static List<ICDKReaction> getModelFromEditorInput(IEditorInput input) throws BioclipseException, IOException, CDKException, CoreException{
      Object file = input.getAdapter(IFile.class);
      if (!(file instanceof IFile)) {
          throw new BioclipseException(
                  "Invalid editor input: Does not provide an IFile");
      }
      IFile inputFile = (IFile) file;
      ICDKManager cdkManager = Activator.getDefault().getCDKManager();
      return cdkManager.loadReactions( inputFile, new NullProgressMonitor());
  }
}