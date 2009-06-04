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
import net.bioclipse.reaction.domain.CDKReaction;
import net.bioclipse.reaction.domain.CDKReactionPropertySource;
import net.bioclipse.reaction.domain.ICDKReactionScheme;
import net.bioclipse.reaction.editparts.CompoundObjectEditPart;
import net.bioclipse.reaction.editparts.REditPartFactory;
import net.bioclipse.reaction.editparts.ReactionObjectEditPart;
import net.bioclipse.reaction.layout.HierarchicLayer;
import net.bioclipse.reaction.model.AbstractConnectionModel;
import net.bioclipse.reaction.model.AbstractObjectModel;
import net.bioclipse.reaction.model.ArrowConnectionModel;
import net.bioclipse.reaction.model.CompoundObjectModel;
import net.bioclipse.reaction.model.ContentsModel;
import net.bioclipse.reaction.model.LineConnectionModel;
import net.bioclipse.reaction.model.ReactionObjectModel;
import net.bioclipse.reaction.tools.FileMoveToolEntry;
import net.bioclipse.reaction.view.ReactionOutLinePage;
import net.bioclipse.reaction.wizards.FormWizardContextMenuProvider;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
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
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.interfaces.IReactionSet;
import org.openscience.cdk.tools.manipulator.ReactionSchemeManipulator;

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
	private REditPartFactory epf;
	private boolean hasPositions;
	private KeyHandler keyHandler;
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
			epf.setEJP(jcpEW);
			jcpEW.setReaction( model.getReactionScheme().getReaction(0));
	        updateContent( model );
        } catch ( Exception e ) {
			LogUtils.handleException( e, logger );
		}
	}
	
	public void updateContent(ICDKReactionScheme model){
		contentsModel = createContentsModel(model);
		if(!hasPositions){
			new HierarchicLayer(contentsModel);
		}
		viewer.setContents(contentsModel);
	}

	private ContentsModel createContentsModel(ICDKReactionScheme model) {
		contentsModel = new ContentsModel();
		int countReactions = 0;
		
    	int countcompounds = 0;
		IReactionSet reactions = ReactionSchemeManipulator.getAllReactions(model.getReactionScheme());
		for(IReaction reaction:reactions.reactions()){
			/*reaction*/
			ReactionObjectModel reactionObject = new ReactionObjectModel();
			reactionObject.addJCP(jcpEW);
			String textID = reaction.getID();
			if(textID == null)
				textID = "Reac"+countReactions;

			reactionObject.setText(textID);
			String pos_X = (String)reaction.getProperty("reactPlug:x");
			if(pos_X != null){
				hasPositions = true;
				String pos_Y = (String)reaction.getProperty("reactPlug:y");
				int i_x = (int)Double.parseDouble(pos_X);
				int i_y = (int)Double.parseDouble(pos_Y);
				createBox(reactionObject,i_x,i_y);
			}
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
					pos_X = (String)reactant.getProperty("reactPlug:x");
					if(pos_X != null){
						hasPositions = true;
						String pos_Y = (String)reactant.getProperty("reactPlug:y");
						int i_x = (int)Double.parseDouble(pos_X);
						int i_y = (int)Double.parseDouble(pos_Y);
						createBox(reactantObject,i_x,i_y);
					}
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
					pos_X = (String)product.getProperty("reactPlug:x");
					if(pos_X != null){
						hasPositions = true;
						String pos_Y = (String)product.getProperty("reactPlug:y");
						int i_x = (int)Double.parseDouble(pos_X);
						int i_y = (int)Double.parseDouble(pos_Y);
						createBox(productObject,i_x,i_y);
					}
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
	private void createBox(AbstractObjectModel object, int pos_X, int pos_Y) {
		object.setConstraint(new Rectangle(pos_X,pos_Y,-1,-1));
		
		
	}
	/**
	 * get the object if it exists
	 * @param contentsModel The ContentsModel
	 * @param ref           The id
	 * @return              The AbstractObjectModel. 
	 */
	private AbstractObjectModel getObject(ContentsModel contentsModel, String ref) {
		for(Iterator<Object> iterator = contentsModel.getChildren().iterator(); iterator.hasNext();) {
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
		
		epf = new REditPartFactory();
		viewer.setEditPartFactory(epf);
		
		keyHandler = new KeyHandler();
		keyHandler.put(KeyStroke.getPressed(SWT.DEL,127,0),getActionRegistry().getAction(ActionFactory.DELETE.getId()));
		keyHandler.put(KeyStroke.getPressed(SWT.F2,0),getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
		keyHandler.put( KeyStroke.getPressed('+', SWT.KEYPAD_ADD, 0), getActionRegistry().getAction(GEFActionConstants.ZOOM_IN)); 
		keyHandler.put( KeyStroke.getPressed('-', SWT.KEYPAD_SUBTRACT, 0), getActionRegistry().getAction(GEFActionConstants.ZOOM_OUT));
		viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.NONE), MouseWheelZoomHandler.SINGLETON);
		
		getGraphicalViewer().setKeyHandler(new GraphicalViewerKeyHandler(getGraphicalViewer()).setParent(keyHandler));
		
		viewer.setContextMenu(new FormWizardContextMenuProvider(viewer, getActionRegistry()));  
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

        System.out.println("getAdapter  redi"+adapter);
		if ( IContentOutlinePage.class.equals( adapter ) ) {
            if ( fOutlinePage == null ) {
                fOutlinePage = new ReactionOutLinePage(this);
            }
            return fOutlinePage;
        }
		if ( IChemObject.class.equals( adapter ) ) {
    			StructuredSelection sele = (StructuredSelection)this.getEditorGraphicalViewer().getSelection();
    			if(sele.getFirstElement() instanceof CompoundObjectEditPart){
        			Object model = ((CompoundObjectEditPart)sele.getFirstElement()).getModel();
        			if(model instanceof CompoundObjectModel)
    				return ((CompoundObjectModel)model).getIMolecule();
    			}else if(sele.getFirstElement() instanceof ReactionObjectEditPart){
    				Object model = ((ReactionObjectEditPart)sele.getFirstElement()).getModel();
        			if(model instanceof ReactionObjectModel)
            			return ((ReactionObjectModel)model).getIReaction();
    			}
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
		for(Iterator<Object> iterator = contentsModel.getChildren().iterator(); iterator.hasNext();) {
			AbstractObjectModel object = (AbstractObjectModel)iterator.next();
			if(object instanceof ReactionObjectModel){
				ReactionObjectModel reactionOM = (ReactionObjectModel)object;
				List<Object> listS = reactionOM.getModelTargetConnections();
				IReaction reaction = DefaultChemObjectBuilder.getInstance().newReaction();
				for(Iterator<Object> iter = listS.iterator(); iter.hasNext();){
					AbstractConnectionModel con = (AbstractConnectionModel)iter.next();
					CompoundObjectModel reactantOM = (CompoundObjectModel)con.getSource();
					Rectangle reactangle = reactantOM.getConstraint();
					IMolecule reactant = DefaultChemObjectBuilder.getInstance().newMolecule(reactantOM.getIMolecule());
					if(reactant == null){
						reactant = DefaultChemObjectBuilder.getInstance().newMolecule();
					}
					reactant.setProperty("reactPlug:x", (new Double(reactangle.x)).toString());
					reactant.setProperty("reactPlug:y", (new Double(reactangle.y)).toString());
					reactant.setID(reactantOM.getText());
					reaction.addReactant(reactant);
				}
				List<Object> listT = reactionOM.getModelSourceConnections();
				for(Iterator<Object> iter = listT.iterator(); iter.hasNext();){
					AbstractConnectionModel con = (AbstractConnectionModel)iter.next();
					CompoundObjectModel productOM = (CompoundObjectModel)con.getTarget();
					Rectangle reactangle = productOM.getConstraint();
					IMolecule product = DefaultChemObjectBuilder.getInstance().newMolecule(productOM.getIMolecule());
					if(product == null){
						product = DefaultChemObjectBuilder.getInstance().newMolecule();
					}
					product.setProperty("reactPlug:x", (new Double(reactangle.x)).toString());
					product.setProperty("reactPlug:y", (new Double(reactangle.y)).toString());
					product.setID(productOM.getText());
					reaction.addProduct(product);
				}
				reaction.setID(reactionOM.getText());
				Rectangle reactangle = reactionOM.getConstraint();
				reaction.setProperty("reactPlug:x", (new Double(reactangle.x)).toString());
				reaction.setProperty("reactPlug:y", (new Double(reactangle.y)).toString());
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
	public KeyHandler getKeyHandler() {
		return keyHandler;
	}

}
