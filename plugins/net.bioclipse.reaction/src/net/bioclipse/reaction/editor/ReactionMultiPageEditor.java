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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import net.bioclipse.cdk.business.Activator;
import net.bioclipse.cdk.business.ICDKManager;
import net.bioclipse.cdk.domain.ICDKReaction;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.util.LogUtils;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.io.CMLWriter;
import org.openscience.cdk.io.DefaultChemObjectWriter;
import org.openscience.cdk.io.MDLRXNWriter;
import org.openscience.cdk.io.formats.CMLFormat;
import org.openscience.cdk.io.formats.IChemFormat;
import org.openscience.cdk.io.formats.MDLRXNFormat;


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
	protected String filetype;
	
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
      Object file = getEditorInput().getAdapter(IFile.class);
      if (!(file instanceof IFile)) {
          throw new RuntimeException(
                  "Invalid editor input: Does not provide an IFile");
      }
      IFile inputFile = (IFile) file;
      try {
        filetype=Activator.getDefault().getJavaCDKManager().determineFormat( inputFile.getFullPath().toOSString() );
      } catch ( Exception e ) {
          throw new RuntimeException(
          "Invalid editor input: Cannot determine file format");
      }
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
      try {
          updateTextEditor();
          textEditor.doSave( monitor );
          rEditor.setDirty(false);
      } catch ( Exception e ) {
          LogUtils.handleException( e, logger );
      }
  		textEditor.doRevertToSaved();		
      firePropertyChange( IEditorPart.PROP_DIRTY );
      this.showBusy( false );
	}
	
	
	private void updateTextEditor() throws CDKException {
	    IChemModel chemmodel=rEditor.getChemModelFromContentsModel();
      StringWriter writer = new StringWriter();
      DefaultChemObjectWriter cdkwriter=null;
	    if(filetype.equals( "Chemical Markup Language" )){
	        cdkwriter = new CMLWriter(writer);
	        cdkwriter.write( chemmodel );
	    }else if(filetype.equals( "MDL Reaction format" )){
	        cdkwriter = new MDLRXNWriter(writer);
	        cdkwriter.write( chemmodel.getReactionSet() );
	    }else{
	        LogUtils.handleException( new BioclipseException("Unknown format in editor - totally lost here"), logger );
	    }
	    try {
          cdkwriter.close();
          textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).set(writer.toString());
      } catch ( IOException e ) {
          LogUtils.handleException( e, logger );
      }
    }

    /**
     * Saves the multi-page editor's document as another file.
	   **/
	public void doSaveAs() {
      IProgressMonitor monitor = new NullProgressMonitor();
        boolean correctfiletype = false;
        IFile target = null;
        int ticks = 10000;
        while ( !correctfiletype ) {
            SaveAsDialog saveasdialog =
                    new SaveAsDialog( this.getSite().getShell() );
            Object file = getEditorInput().getAdapter(IFile.class);
            saveasdialog.setOriginalFile( (IFile) file );
            int result = saveasdialog.open();
            if ( result == SaveAsDialog.CANCEL ) {
                correctfiletype = true;
                target = null;
            } else {
                target =
                        ResourcesPlugin.getWorkspace().getRoot()
                                .getFile( saveasdialog.getResult() );
                String filetype = saveasdialog.getResult().getFileExtension();
                if ( filetype == null )
                    filetype = "none";
                if ( filetype.equals( "cml" ) || filetype.equals( "rxn" ) ) {
                    correctfiletype = true;
                    monitor.beginTask( "Writing file", ticks );
                    try {
                        if ( filetype.equals( "cml" ) )
                            this.filetype =
                                    CMLFormat.getInstance().getFormatName();
                        else
                            this.filetype =
                                    MDLRXNFormat.getInstance().getFormatName();
                        updateTextEditor();
                        byte[] newtext =
                                textEditor
                                        .getDocumentProvider()
                                        .getDocument(
                                                      textEditor
                                                              .getEditorInput() )
                                        .get().getBytes();
                        if ( target.exists() )
                            target
                                    .setContents(
                                                  new ByteArrayInputStream(
                                                                            newtext ),
                                                  0, new NullProgressMonitor() );
                        else
                            target.create( new ByteArrayInputStream( newtext ),
                                           true, new NullProgressMonitor() );
                        rEditor.setDirty(false);
                        textEditor.setInput( new FileEditorInput(target) );
                    } catch ( Exception ex ) {
                        MessageDialog.openError( this.getSite().getShell(),
                                                 "Error saving",
                                                 "Saving failed due to: "
                                                         + ex.getMessage() );
                        correctfiletype = false;
                    }
                } else {
                    MessageDialog
                            .openError(
                                        this.getSite().getShell(),
                                        "No valid file type!",
                                        "Valid file types are "
                                                + "rxn"
                                                + " and "
                                                + "cml"
                                                + ". The file extension must be one of these!" );
                }
            }
        }
        monitor.worked( ticks );
	}

	/**
	 * The <code>ReactionMultiPageEditor</code> implementation of this method
	 * checks that the input is an instance of <code>BioResourceEditorInput</code>.
	 */
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		super.init(site, editorInput);
		this.editorInput = editorInput;
		setPartName( editorInput.getName() );
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
	    try{
    		super.pageChange(newPageIndex);
    		if(newPageIndex == 1){
    		    updateTextEditor();
    		}else{
    		    updateReactionEditor();
    		}
	    }catch(Exception ex){
	        LogUtils.handleException( ex, logger );
	    }
	}

	private void updateReactionEditor() {
	    if(textEditor==null)
	        return;
      ICDKManager cdkManager = Activator.getDefault().getJavaCDKManager();
      byte[] newtext =
          textEditor
                  .getDocumentProvider()
                  .getDocument(
                                textEditor
                                        .getEditorInput() )
                  .get().getBytes();
      try {
        List<ICDKReaction> model = cdkManager.loadReactions( new ByteArrayInputStream(newtext), (IChemFormat) (filetype.equals( "Chemical Markup Language" ) ? CMLFormat.getInstance() : MDLRXNFormat.getInstance()));
        rEditor.updateContent( model );
      } catch ( Exception e ) {
          LogUtils.handleException( e, logger );
      }
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
      ICDKManager cdkManager = Activator.getDefault().getJavaCDKManager();
      return cdkManager.loadReactions( inputFile );
  }
}
