package net.bioclipse.plugins.bc_reaction.editors;

import java.util.HashMap;

import org.eclipse.core.commands.operations.UndoContext;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.openscience.cdk.applications.jchempaint.DrawingPanel;
import org.openscience.cdk.applications.jchempaint.JCPBioclipseUndoRedoHandler;
import org.openscience.cdk.applications.jchempaint.JChemPaintModel;
import org.openscience.cdk.controller.Controller2DModel;
import org.openscience.cdk.interfaces.IChemModel;


/**
 * Composite which set the JChemPaint
 *   
 * @author Miguel Rojas
 */
public class ReactMolDrawingComposite extends Composite{
	private boolean ret;
	private DrawingPanel drawingPanel;
	private JChemPaintModel jcpModel;
	private ReactionEditor page;

	/**
	 * Constructor of the ReactMolDrawingComposite object
	 * 
	 * @param parent  The Composita
	 * @param style   The style
	 * @param editor  The ReactionEditor
	 */
	public ReactMolDrawingComposite(Composite parent, int style, ReactionEditor editor) {
		super(parent, style);
		this.page = editor;
		this.init(editor);
	}

	/**
	 * initializes the composite itself (creates drawingPanel and jcpModel..)
	 * 
	 * @param page The ReactionEditor 
	 */
	private void init(ReactionEditor page) {
		this.setLayout(new FillLayout());	
		drawingPanel = new DrawingPanel(this.getDisplay());
		IChemModel model = null;
		model = page.getModelFromEditorInputMolecule();
		jcpModel = new JChemPaintModel(model);
		drawingPanel.setJChemPaintModel(jcpModel);
//		jcpModel.activate();
		registerSpecMolController();
		jcpModel.getRendererModel().addCDKChangeListener(page);	
	}
	
	
	/**
	 * get the drawingPanel
	 * 
	 * @return DrawingPanel drawingPanel
	 */
	public DrawingPanel getDrawingPanel() {
		return drawingPanel;
	}

	/**
	 * get the JChemPaintModel
	 * 
	 * @return JChemPaintModel jcpModel
	 */
	public JChemPaintModel getJcpModel() {
		return jcpModel;
	}

	/* (non-Javadoc)
	 * @see bc_specmol.listener.SpecMolListener#selectionChanged(bc_specmol.editors.AssignmentController)
	 */
	public void selectionChanged(IChemModel chemModel) {
		System.out.println("selectionChanged - reactionMolDrawing");
		jcpModel.setChemModel(chemModel);
		jcpModel.resetIsModified();
		registerSpecMolController();
		drawingPanel.repaint();
		
	}

	/**
	 * get the assignment editor page 
	 * 
	 * @return ReactionEditor page
	 */
	public ReactionEditor getPage() {
		return page;
	}
	/**
	 * deselect all selected atoms/bonds
	 */
	public void unselect() {
//		jcpModel.getRendererModel().setExternalSelectedPart(jcpModel.getChemModel().getBuilder().newAtomContainer());		
//		if (page.getAssignmentController().getSelectedSubstructure() != null) {
//			page.getAssignmentController().getSelectedSubstructure().removeAllElements();
//		}
	}

	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Composite#setFocus()
	 */
	public boolean setFocus() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				ret = ReactMolDrawingComposite.super.setFocus();
			}
			
		});
		return ret;
	}

	/**
	 * load a new ChemModel into the drawing panel
	 * 
	 * @param IChemModel model2
	 * @param HashMap    coordenates
	 */
	public void loadNewChemModel(IChemModel newModel, HashMap coordenates) {
		jcpModel.setChemModel(newModel);
		jcpModel.resetIsModified();
		if(coordenates != null)
			jcpModel.getRendererModel().setRenderingCoordinates(coordenates);
		this.setSize(getSize().x,getSize().y-1);
		registerSpecMolController();
		drawingPanel.repaint();
	}

	/**
	 * register the SpecMolController2D to the drawingPanel
	 */
	private void registerSpecMolController() {
		Controller2DModel contrModel = new Controller2DModel();
		contrModel.setDrawMode(Controller2DModel.LASSO);
		contrModel.setMovingAllowed(false);
//		SimpleController2D contr = new SpecMolController2D(jcpModel.getChemModel(), jcpModel.getRendererModel(), contrModel, this);
//		contr.addCDKChangeListener(jcpModel);
//		drawingPanel.addMouseListener(contr);
//		drawingPanel.addMouseMotionListener(contr);
		
//		TODO should not be neccessary - abstractController should work without undoRedoHandler as well...
		JCPBioclipseUndoRedoHandler undoRedoHandler=new JCPBioclipseUndoRedoHandler();
		undoRedoHandler.setDrawingPanel(drawingPanel);
		undoRedoHandler.setJcpm(jcpModel);
		undoRedoHandler.setUndoContext(new UndoContext());
//		contr.setUndoRedoHandler(undoRedoHandler);
	}

}
