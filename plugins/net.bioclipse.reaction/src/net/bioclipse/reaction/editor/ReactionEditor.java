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
package net.bioclipse.reaction.editor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import net.bioclipse.cdk.jchempaint.widgets.JChemPaintEditorWidget;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.util.LogUtils;
import net.bioclipse.reaction.dnd.MyFileDragSourceListener;
import net.bioclipse.reaction.dnd.MyFileDropTargetListener;
import net.bioclipse.reaction.domain.ICDKReaction;
import net.bioclipse.reaction.domain.ICDKReactionScheme;
import net.bioclipse.reaction.editparts.MyEditPartFactory;
import net.bioclipse.reaction.layout.HierarchicLayer;
import net.bioclipse.reaction.model.AbstractConnectionModel;
import net.bioclipse.reaction.model.AbstractObjectModel;
import net.bioclipse.reaction.model.ArrowConnectionModel;
import net.bioclipse.reaction.model.CompoundObjectModel;
import net.bioclipse.reaction.model.ContentsModel;
import net.bioclipse.reaction.model.LineConnectionModel;
import net.bioclipse.reaction.model.ReactionObjectModel;
import net.bioclipse.reaction.tools.FileMoveToolEntry;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.interfaces.IReactionSet;

/**
 * An editor page showing a reaction as a tree and a detailed view.
 * 
 * @author Miguel Rojas
 */
public class ReactionEditor extends GraphicalEditorWithPalette{// implements ICDKChangeListener,IJCPEditorPart, BioResourceChangeListener {
	private IEditorInput editorInput;
	private JChemPaintEditorWidget jcpEW;
	private SashForm form;
	private ContentsModel contentsModel;
	private GraphicalViewer viewer;
	private Object fOutlinePage;
	private static final Logger logger = Logger.getLogger( ReactionEditor.class.toString());
	
	/**
	 * Constructor of the ReactionEditor object
	 * 
	 * @param editorInput 
	 */
	public ReactionEditor(IEditorInput editorInput){
		this.editorInput = editorInput;
		setEditDomain(new DefaultEditDomain(this));		
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithPalette#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		form = new SashForm(parent,SWT.VERTICAL);
		form.setLayout(new FillLayout());
		super.createPartControl(form);
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#initializeGraphicalViewer()
	 */
	protected void initializeGraphicalViewer() {
		viewer = getGraphicalViewer();

		viewer.addDropTargetListener(new MyFileDropTargetListener(viewer));
		viewer.addDragSourceListener(new MyFileDragSourceListener(viewer));
		try {
			ICDKReactionScheme model = this.getModelFromEditorInput();
			jcpEW = new JChemPaintEditorWidget(form,SWT.PUSH);
			jcpEW.setReaction( model.getReactionScheme().getReaction(0));
	        updateContent( model );
        } catch ( Exception e ) {
			LogUtils.handleException( e, logger );
		}
	}
	
	public void updateContent(ICDKReactionScheme model){
		contentsModel = createContentsModel(model);
		@SuppressWarnings("unused")
		HierarchicLayer hLayer = new HierarchicLayer(contentsModel);
		viewer.setContents(contentsModel);
	}

	private ContentsModel createContentsModel(ICDKReactionScheme model) {
		contentsModel = new ContentsModel();
		int countReactions = 0;
		int countcompounds = 0;
//		for(Iterator<ICDKReaction> iteratorReactions = model.iterator(); iteratorReactions.hasNext();) {
//		IReaction reaction = iteratorReactions.next().getReaction();
		for(IReaction reaction:model.getReactionScheme().reactions()){
			/*reaction*/
			ReactionObjectModel reactionObject = new ReactionObjectModel();
			reactionObject.addJCP(jcpEW);
			String textID = reaction.getID();
			if(textID == null)
				textID = "Reac"+countReactions;

			reactionObject.setText(textID);
			reactionObject.setIReaction(reaction);
			contentsModel.addChild(reactionObject);
			countReactions ++;
			/*reactant*/
			for(IAtomContainer reactant : reaction.getReactants().molecules()) {
				String name = reactant.getID();
				if(name == null){
					name = "mol"+countcompounds;
					reactant.setID(name);
				}
				CompoundObjectModel reactantObject;
				AbstractObjectModel objectExisting = getObject(contentsModel, name);
				
				if(objectExisting != null)
					reactantObject = (CompoundObjectModel)objectExisting;
				else{
					reactantObject = new CompoundObjectModel();
					reactantObject.addJCP(jcpEW);
					reactantObject.setText(name);
					reactantObject.setIMolecule(reactant);
					contentsModel.addChild(reactantObject);
				}
			
				ArrowConnectionModel connection = new ArrowConnectionModel();
				connection.setSource(reactantObject);
				connection.setTarget(reactionObject);
				connection.attachSource();
				connection.attachTarget();

				countcompounds++;
			}

			/*product*/
			for(IAtomContainer product :  reaction.getProducts().molecules()) {
				String name = product.getID();
				if(name == null){
					name = "mol"+countcompounds;
					product.setID(name);
				}
				CompoundObjectModel productObject;
				AbstractObjectModel objectExisting = getObject(contentsModel, name);
				
				if(objectExisting != null)
					productObject = (CompoundObjectModel)objectExisting;
				else{
					productObject = new CompoundObjectModel();
					productObject.addJCP(jcpEW);
					productObject.setText(name);
					productObject.setIMolecule(product);
					contentsModel.addChild(productObject);
				}
				ArrowConnectionModel connection = new ArrowConnectionModel();
				connection.setSource(reactionObject);
				connection.setTarget(productObject);
				connection.attachSource();
				connection.attachTarget();
				
				countcompounds++;
				
			}
		}
		return contentsModel;
	}
	/**
	 * get the object if it exists
	 * @param contentsModel The ContentsModel
	 * @param ref           The id
	 * @return              The AbstractObjectModel. 
	 */
	private AbstractObjectModel getObject(ContentsModel contentsModel, String ref) {
		for(Iterator iterator = contentsModel.getChildren().iterator(); iterator.hasNext();) {
			AbstractObjectModel object = (AbstractObjectModel)iterator.next();
			if(object.getText().equals(ref)){
				return object;
			}
		}
		return null;
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {
  }
	
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
	 */
	public void doSaveAs() {
	
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		return false;
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#gotoMarker(org.eclipse.core.resource.IMarker)
	 */
	public void gotoMarker(IMarker marker){
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	protected void configureGraphicalViewer(){
		super.configureGraphicalViewer();
		
		GraphicalViewer viewer = getGraphicalViewer();
		
		ScalableRootEditPart rootEditPart = new ScalableRootEditPart();
		viewer.setRootEditPart(rootEditPart);

		ZoomManager manager = rootEditPart.getZoomManager();
		
		double[] zoomLevels = new double[] {
		  0.25,0.5,0.75,1.0,1.5,2.0,2.5,3.0,4.0,5.0,10.0,20.0
		};
		manager.setZoomLevels(zoomLevels);
    
		ArrayList zoomContributions = new ArrayList();
		zoomContributions.add(ZoomManager.FIT_ALL);
		zoomContributions.add(ZoomManager.FIT_HEIGHT);
		zoomContributions.add(ZoomManager.FIT_WIDTH);
		manager.setZoomLevelContributions(zoomContributions);

		IAction action = new ZoomInAction(manager);
		getActionRegistry().registerAction(action);
		
		action = new ZoomOutAction(manager);
		getActionRegistry().registerAction(action);
		
		viewer.setEditPartFactory(new MyEditPartFactory());
		
		KeyHandler keyHandler = new KeyHandler();
		keyHandler.put(KeyStroke.getPressed(SWT.DEL,127,0),getActionRegistry().getAction(GEFActionConstants.DELETE));
		keyHandler.put(KeyStroke.getPressed(SWT.F2,0),getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
		
		getGraphicalViewer().setKeyHandler(new GraphicalViewerKeyHandler(getGraphicalViewer()).setParent(keyHandler));
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithPalette#getPaletteRoot()
	 */
	protected PaletteRoot getPaletteRoot(){
		
			
		PaletteRoot root = new PaletteRoot();
		PaletteGroup toolGroup = new PaletteGroup("c[");
		
		ToolEntry tool = new SelectionToolEntry();
		toolGroup.add(tool);
		root.setDefaultEntry(tool);
		
		toolGroup.add(new MarqueeToolEntry());
		toolGroup.add(new FileMoveToolEntry());
		
		PaletteDrawer drawer = new PaletteDrawer("i");
		
		ImageDescriptor descriptor = ImageDescriptor.createFromFile(ReactionEditor.class,"/icons/reactionFigure.gif");
		CreationToolEntry creationEntry = new CreationToolEntry(
				"Reaction",
				"Reaction Figure",
				new SimpleFactory(ReactionObjectModel.class),
				descriptor,
				descriptor);
		drawer.add(creationEntry);
		
		descriptor = ImageDescriptor.createFromFile(ReactionEditor.class,"/icons/compoundFigure.gif");
		creationEntry = new CreationToolEntry(
				"Compound",
				"Compound Figure",
				new SimpleFactory(CompoundObjectModel.class),
				descriptor,
				descriptor);
		drawer.add(creationEntry);
		
		descriptor = ImageDescriptor.createFromFile(ReactionEditor.class,"/icons/newConnection.gif");
		ConnectionCreationToolEntry connxCreationEntry = new ConnectionCreationToolEntry(
				"reversible",
				"reversible Connection",
				new SimpleFactory(LineConnectionModel.class),
				descriptor,
				descriptor);
		drawer.add(connxCreationEntry);
		
		descriptor = ImageDescriptor.createFromFile(ReactionEditor.class,"/icons/arrowConnection.gif");
		ConnectionCreationToolEntry arrowConnxCreationEntry = new ConnectionCreationToolEntry(
				"arrow",
				"arrow Connection",
				new SimpleFactory(ArrowConnectionModel.class),
				descriptor,
				descriptor);
		drawer.add(arrowConnxCreationEntry);
		
		root.add(toolGroup);
		root.add(drawer);
		
		return root;
		
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection selection){
		if(part.getSite().getWorkbenchWindow().getActivePage() == null)
			return;
		super.selectionChanged(part,selection);
		
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#createActions()
	 */
	@SuppressWarnings("unchecked")
	protected void createActions(){
		super.createActions();
		ActionRegistry registry = getActionRegistry();
		
		IAction action = new DirectEditAction((IWorkbenchPart)this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter ) {
		if (adapter == ZoomManager.class)
			return ((ScalableRootEditPart) getGraphicalViewer().getRootEditPart()).getZoomManager();
		
		if ( IContentOutlinePage.class.equals( adapter ) ) {
            if ( fOutlinePage == null ) {
                fOutlinePage = new ReactionOutLinePage(this);
            }
            return fOutlinePage;
        }
		return super.getAdapter(adapter);
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.commands.CommandStackListener#commandStackChanged(java.util.EventObject)
	 */
	public void commandStackChanged(EventObject event) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(event);
	}
	/**
	 * Get the IChemModel from the parsedResource
	 * @return The IChemModel necessary for ReactionEditor
	 */
	public ICDKReactionScheme getModelFromEditorInput() throws BioclipseException, IOException, CDKException, CoreException{
	    return ReactionMultiPageEditor.getModelFromEditorInput( this.editorInput);	    
	}

	/**
	 * update the IChemModel from ContentsModel
	 * 
	 * @return The IChemModel object
	 */
	public IChemModel getChemModelFromContentsModel() {
		IChemModel newChemModel = DefaultChemObjectBuilder.getInstance().newChemModel();
		IReactionSet reactionSet = DefaultChemObjectBuilder.getInstance().newReactionSet();
		newChemModel.setReactionSet(reactionSet);
		for(Iterator iterator = contentsModel.getChildren().iterator(); iterator.hasNext();) {
			AbstractObjectModel object = (AbstractObjectModel)iterator.next();
			if(object instanceof ReactionObjectModel){
				ReactionObjectModel reactionOM = (ReactionObjectModel)object;
				List listS = reactionOM.getModelTargetConnections();
				IReaction reaction = DefaultChemObjectBuilder.getInstance().newReaction();
				for(Iterator iter = listS.iterator(); iter.hasNext();){
					AbstractConnectionModel con = (AbstractConnectionModel)iter.next();
					CompoundObjectModel reactantOM = (CompoundObjectModel)con.getSource();
					IMolecule reactant = DefaultChemObjectBuilder.getInstance().newMolecule(reactantOM.getIMolecule());
					if(reactant == null){
						reactant = DefaultChemObjectBuilder.getInstance().newMolecule();
						reactant.setID(reactantOM.getText());
					}
					reaction.addReactant(reactant);
				}
				List listT = reactionOM.getModelSourceConnections();
				for(Iterator iter = listT.iterator(); iter.hasNext();){
					AbstractConnectionModel con = (AbstractConnectionModel)iter.next();
					CompoundObjectModel productOM = (CompoundObjectModel)con.getTarget();
					IMolecule product = DefaultChemObjectBuilder.getInstance().newMolecule(productOM.getIMolecule());
					if(product == null){
						product = DefaultChemObjectBuilder.getInstance().newMolecule();
						product.setID(productOM.getText());
					}
					reaction.addProduct(product);
				}
				reaction.setID(reactionOM.getText());
				reactionSet.addReaction(reaction);
				
			}
		}
		return newChemModel;
	}

	/*
	 * 
	 */
	public void setDirty(boolean isDirty) throws BioclipseException {
		if(!isDirty)
		    getCommandStack().markSaveLocation();
		firePropertyChange( IEditorPart.PROP_DIRTY );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#isDirty()
	 */
	public boolean isDirty(){
		return getCommandStack().isDirty();
	}
	/**
	 * make a repaint of the Gui
	 * 
	 * @param chemModel
	 */
	public void updateViewer(ICDKReactionScheme chemModel) {
		ContentsModel contentsM = createContentsModel(chemModel);
		@SuppressWarnings("unused")
		HierarchicLayer hLayer = new HierarchicLayer(contentsM);
		viewer.setContents(contentsM);
		
	}
	
	public ActionRegistry getEditorActionRegistry(){
		return getActionRegistry();
	}
	
	public DefaultEditDomain getEditorEditDomain(){
		return getEditDomain();
	}

	public SelectionSynchronizer getEditorSelectionSynchronizer(){
		return getSelectionSynchronizer();
	}

	public GraphicalViewer getEditorGraphicalViewer(){
		return getGraphicalViewer();
	}
	
	public ContentsModel getContentsModel() {
		return contentsModel;
	}

}
