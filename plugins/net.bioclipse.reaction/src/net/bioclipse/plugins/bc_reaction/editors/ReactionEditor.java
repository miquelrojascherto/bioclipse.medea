package net.bioclipse.plugins.bc_reaction.editors;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import net.bioclipse.editors.BioResourceEditorInput;
import net.bioclipse.model.BioResource;
import net.bioclipse.model.BioResourceChangeListener;
import net.bioclipse.plugins.Bc_reactionPlugin;
import net.bioclipse.plugins.bc_jchempaint.editors.IJCPEditorPart;
import net.bioclipse.plugins.bc_reaction.dnd.MyFileDragSourceListener;
import net.bioclipse.plugins.bc_reaction.dnd.MyFileDropTargetListener;
import net.bioclipse.plugins.bc_reaction.editparts.MyEditPartFactory;
import net.bioclipse.plugins.bc_reaction.editparts.tree.TreeEditPartFactory;
import net.bioclipse.plugins.bc_reaction.layout.HierarchicLayer;
import net.bioclipse.plugins.bc_reaction.model.AbstractConnectionModel;
import net.bioclipse.plugins.bc_reaction.model.AbstractObjectModel;
import net.bioclipse.plugins.bc_reaction.model.ArrowConnectionModel;
import net.bioclipse.plugins.bc_reaction.model.CompoundObjectModel;
import net.bioclipse.plugins.bc_reaction.model.ContentsModel;
import net.bioclipse.plugins.bc_reaction.model.LineConnectionModel;
import net.bioclipse.plugins.bc_reaction.model.ReactionObjectModel;
import net.bioclipse.plugins.bc_reaction.tools.FileMoveToolEntry;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.LayerConstants;
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
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemSequence;
import org.openscience.cdk.applications.jchempaint.DrawingPanel;
import org.openscience.cdk.applications.jchempaint.JCPControlListener;
import org.openscience.cdk.applications.jchempaint.JCPScrollBar;
import org.openscience.cdk.applications.jchempaint.JChemPaintModel;
import org.openscience.cdk.event.ICDKChangeListener;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.interfaces.IReactionSet;
/**
 * 
 * @author Miguel Rojas 
 */
public class ReactionEditor extends GraphicalEditorWithPalette implements ICDKChangeListener,IJCPEditorPart, BioResourceChangeListener {
	private IEditorInput editorInput;
	private ReactMolDrawingComposite child1;
	private JCPScrollBar jcpScrollBar;
	private SashForm form;
	private IChemModel chemModel;
	@SuppressWarnings("unused")
	private boolean isDirty;
	private ContentsModel contentsModel;
	private GraphicalViewer viewer;
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
//		child1 = new ReactMolDrawingComposite(form, SWT.EMBEDDED | SWT.H_SCROLL | SWT.V_SCROLL, this);	
		
		java.awt.Frame jcpFrame = SWT_AWT.new_Frame(child1);
		jcpFrame.add(child1.getDrawingPanel());
		jcpScrollBar = new JCPScrollBar(this, true, true);
		ControlListener controlListener = new JCPControlListener(this);
		child1.addControlListener(controlListener);
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#initializeGraphicalViewer()
	 */
	protected void initializeGraphicalViewer() {
		viewer = getGraphicalViewer();

		viewer.addDropTargetListener(new MyFileDropTargetListener(viewer));
		viewer.addDragSourceListener(new MyFileDragSourceListener(viewer));
		
		
		child1 = new ReactMolDrawingComposite(form, SWT.EMBEDDED | SWT.H_SCROLL | SWT.V_SCROLL, this);	
		
		
		IChemModel model = this.getModelFromEditorInputReaction();
		
		contentsModel = createContentsModel(model);
		
		@SuppressWarnings("unused")
		HierarchicLayer hLayer = new HierarchicLayer(contentsModel,Bc_reactionPlugin.getDefault().getLayoutPreference());
		viewer.setContents(contentsModel);

	}

	private ContentsModel createContentsModel(IChemModel model) {

		
		contentsModel = new ContentsModel();
		int countReactions = 0;
		int countcompounds = 0;
		for(Iterator iteratorReactions = model.getReactionSet().reactions(); iteratorReactions.hasNext();) {
			IReaction reaction = (IReaction)iteratorReactions.next();
			/*reaction*/
			ReactionObjectModel reactionObject = new ReactionObjectModel();
			reactionObject.addJCP(child1);
			String textID = reaction.getID();
			if(textID == null)
				textID = "Reac"+countReactions;

			reactionObject.setText(textID);
			reactionObject.setIReaction(reaction);
			contentsModel.addChild(reactionObject);
			countReactions ++;
			/*reactant*/
			for(Iterator iteratoreactant = reaction.getReactants().molecules(); iteratoreactant.hasNext();) {
				IMolecule reactant = (IMolecule)iteratoreactant.next();
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
					reactantObject.addJCP(child1);
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
			for(Iterator iteratoproduct = reaction.getProducts().molecules(); iteratoproduct.hasNext();) {
				IMolecule product = (IMolecule)iteratoproduct.next();
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
					productObject.addJCP(child1);
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
		BioResourceEditorInput brinp= (BioResourceEditorInput) editorInput;
		IChemModel chemModel = getChemModel();
		ChemSequence seq = new ChemSequence();
		seq.addChemModel(chemModel);
		ChemFile chemFile = new ChemFile();
		chemFile.addChemSequence(seq);
		//set the BioResouces parsedRes object to this ChemFile
		brinp.getBioResource().setParsedResource(chemFile);
		//then call save() of the BioResource 
		brinp.getBioResource().save();
		this.setDirty(false);
		
		getCommandStack().markSaveLocation();
		
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
		
		/*contribution for different Layouts*/
//		action = new AlignmentAction((IWorkbenchPart) this,
//				PositionConstants.LEFT);
//		registry.registerAction(action);
//		getSelectionActions().add(action.getId());
//
//		action = new AlignmentAction((IWorkbenchPart) this,
//				PositionConstants.CENTER);
//		registry.registerAction(action);
//		getSelectionActions().add(action.getId());
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class type) {
		if (type == ZoomManager.class)
			return ((ScalableRootEditPart) getGraphicalViewer().getRootEditPart()).getZoomManager();

		if (type == IContentOutlinePage.class) {
			return new MyContentOutlinePage();
		}
		return super.getAdapter(type);
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
	public IChemModel getModelFromEditorInputReaction(){
		Object parsedRes = ((BioResource)((BioResourceEditorInput)this.editorInput).getBioResource()).getParsedResource();
		if (parsedRes instanceof IChemModel) {
			chemModel = (IChemModel) parsedRes;
			return chemModel;
		}
		else if (parsedRes instanceof IChemFile) {
			chemModel = ((IChemFile)parsedRes).getChemSequence(0).getChemModel(0);
			return chemModel;
		}
		else {
			return null;
		}
	}
	/**
	 * Get the IChemModel from the parsedResource
	 * 
	 * @return The IChemModel necessary for JCP
	 */
	protected IChemModel getModelFromEditorInputMolecule(){
		Object parsedRes = ((BioResource)((BioResourceEditorInput)this.editorInput).getBioResource()).getParsedResource();
		IChemModel modelJCP;
		if (parsedRes instanceof IChemModel) {
			modelJCP = (IChemModel) parsedRes;
			if(modelJCP.getMoleculeSet() == null)
				return modelJCP;
			IMoleculeSet moleculeSet = modelJCP.getBuilder().newMoleculeSet();
			IMolecule mol = modelJCP.getReactionSet().getReaction(0).getReactants().getMolecule(0);
			moleculeSet.addMolecule(mol);
			modelJCP.setMoleculeSet(moleculeSet);
			return modelJCP;
		}
		else if (parsedRes instanceof IChemFile) {
			modelJCP = ((IChemFile)parsedRes).getChemSequence(0).getChemModel(0);
			if(modelJCP.getMoleculeSet() == null){
				IChemModel newModel = modelJCP.getBuilder().newChemModel();
				return newModel;
			}
			IMoleculeSet moleculeSet = modelJCP.getBuilder().newMoleculeSet();
			IMolecule mol = modelJCP.getReactionSet().getReaction(0).getReactants().getMolecule(0);
			moleculeSet.addMolecule(mol);
			modelJCP.setMoleculeSet(moleculeSet);
			return modelJCP;
		}
		else {
			return null;
		}
	}
	public void stateChanged(EventObject arg0) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * get the DrawingPanel object
	 * 
	 * @return The DrawingPanel object
	 */
	public DrawingPanel getDrawingPanel() {
		return child1.getDrawingPanel();
	}
	/**
	 * get the JChemPaintModel object
	 * 
	 * @return The JChemPaintModel object
	 */
	public JChemPaintModel getJCPModel() {
		return child1.getJcpModel();
	}
	/**
	 * get the IChemModel object
	 * 
	 * @return The IChemModel object
	 */
	public IChemModel getChemModel() {
		return updateChemModelFromContentsModel();
	}
	/**
	 * update the IChemModel from ContentsModel
	 * 
	 * @return The IChemModel object
	 */
	private IChemModel updateChemModelFromContentsModel() {
		IChemModel newChemModel = chemModel.getBuilder().newChemModel();
		IReactionSet reactionSet = chemModel.getBuilder().newReactionSet();
		newChemModel.setReactionSet(reactionSet);
		for(Iterator iterator = contentsModel.getChildren().iterator(); iterator.hasNext();) {
			AbstractObjectModel object = (AbstractObjectModel)iterator.next();
			if(object instanceof ReactionObjectModel){
				ReactionObjectModel reactionOM = (ReactionObjectModel)object;
				List listS = reactionOM.getModelTargetConnections();
				IReaction reaction = chemModel.getBuilder().newReaction();
				for(Iterator iter = listS.iterator(); iter.hasNext();){
					AbstractConnectionModel con = (AbstractConnectionModel)iter.next();
					CompoundObjectModel reactantOM = (CompoundObjectModel)con.getSource();
					IMolecule reactant = reactantOM.getIMolecule();
					if(reactant == null){
						reactant = chemModel.getBuilder().newMolecule();
						reactant.setID(reactantOM.getText());
					}
					reaction.addReactant(reactant);
				}
				List listT = reactionOM.getModelSourceConnections();
				for(Iterator iter = listT.iterator(); iter.hasNext();){
					AbstractConnectionModel con = (AbstractConnectionModel)iter.next();
					CompoundObjectModel productOM = (CompoundObjectModel)con.getTarget();
					IMolecule product = productOM.getIMolecule();
					if(product == null){
						product = chemModel.getBuilder().newMolecule();
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
	/**
	 * get the Composite object
	 * 
	 * @return The Composite object
	 */
	public Composite getJcpComposite() {
		return child1;
	}
	/**
	 * get the JCPScrollBar object
	 * 
	 * @return The JCPScrollBar object
	 */
	public JCPScrollBar getJcpScrollBar() {
		return jcpScrollBar;
	}
	public void resourceChanged(BioResource resource) {
		// TODO Auto-generated method stub
		
	}
	/*
	 * 
	 */
	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
		if(isDirty)
			fireSetDirtyChanged();
	}
	
	private void fireSetDirtyChanged() {
		updateViewer(this.getModelFromEditorInputReaction());
		
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
	public void updateViewer(IChemModel chemModel) {
		ContentsModel contentsM = createContentsModel(chemModel);
		@SuppressWarnings("unused")
		HierarchicLayer hLayer = new HierarchicLayer(contentsM,0);
		viewer.setContents(contentsM);
		
	}
	
	/**
	 * A ContentOutlinePage which contains also zoom-viewer
	 * 
	 * @author Miguel Rojas
	 */
	class MyContentOutlinePage extends ContentOutlinePage {

		private SashForm sash;
		private ScrollableThumbnail thumbnail;
		private DisposeListener disposeListener;

		/**
		 * Constructor of the MyContentOutlinePage object
		 */
		public MyContentOutlinePage() {
			super(new TreeViewer());
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.ui.part.Page#init(org.eclipse.ui.part.IPageSite)
		 */
		@SuppressWarnings("deprecation")
		public void init(IPageSite pageSite) {
			super.init(pageSite);
			ActionRegistry registry = getActionRegistry();
			IActionBars bars = pageSite.getActionBars();

			// String id = ActionFactory.UNDO.getId();
			String id = IWorkbenchActionConstants.UNDO;
			bars.setGlobalActionHandler(id, registry.getAction(id));

			id = IWorkbenchActionConstants.REDO;
			bars.setGlobalActionHandler(id, registry.getAction(id));

			id = IWorkbenchActionConstants.DELETE;
			bars.setGlobalActionHandler(id, registry.getAction(id));
			bars.updateActionBars();
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.gef.ui.parts.ContentOutlinePage#createControl(org.eclipse.swt.widgets.Composite)
		 */
		public void createControl(Composite parent) {
			sash = new SashForm(parent, SWT.VERTICAL);

			getViewer().createControl(sash);

			getViewer().setEditDomain(getEditDomain());
			getViewer().setEditPartFactory(new TreeEditPartFactory());
			getViewer().setContents(contentsModel);
			getSelectionSynchronizer().addViewer(getViewer());

			Canvas canvas = new Canvas(sash, SWT.BORDER);
			LightweightSystem lws = new LightweightSystem(canvas);

			thumbnail = new ScrollableThumbnail(
					(Viewport) ((ScalableRootEditPart) getGraphicalViewer()
							.getRootEditPart()).getFigure());
			thumbnail.setSource(((ScalableRootEditPart) getGraphicalViewer()
					.getRootEditPart())
					.getLayer(LayerConstants.PRINTABLE_LAYERS));

			lws.setContents(thumbnail);

			disposeListener = new DisposeListener() {
				public void widgetDisposed(DisposeEvent e) {
					if (thumbnail != null) {
						thumbnail.deactivate();
						thumbnail = null;
					}
				}
			};
			getGraphicalViewer().getControl().addDisposeListener(
					disposeListener);

		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.gef.ui.parts.ContentOutlinePage#getControl()
		 */
		public Control getControl() {
			return sash;
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.ui.part.Page#dispose()
		 */
		public void dispose() {
			// SelectionSynchronizer �reeViewer�
			getSelectionSynchronizer().removeViewer(getViewer());

			if (getGraphicalViewer().getControl() != null
					&& !getGraphicalViewer().getControl().isDisposed())
				getGraphicalViewer().getControl().removeDisposeListener(
						disposeListener);

			super.dispose();
		}

	}
}
