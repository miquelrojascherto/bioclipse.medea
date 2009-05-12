package net.bioclipse.reaction.editor;

import java.util.List;

import net.bioclipse.cdk.domain.ICDKReaction;
import net.bioclipse.cdk.jchempaint.widgets.JChemPaintEditorWidget;
import net.bioclipse.core.business.BioclipseException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IReactionSet;


/**
 * Composite which set the JChemPaint
 *   
 * @author Miguel Rojas
 */
public class ReactMolDrawingComposite extends Composite{
	private boolean ret;
	private JChemPaintEditorWidget drawingPanel;
	private ReactionEditor page;

	/**
	 * Constructor of the ReactMolDrawingComposite object
	 * 
	 * @param parent  The Composita
	 * @param style   The style
	 * @param editor  The ReactionEditor
	 * @throws BioclipseException 
	 */
	public ReactMolDrawingComposite(Composite parent, int style, ReactionEditor editor) throws BioclipseException {
		super(parent, style);
		this.page = editor;
		this.init(editor);
	}

	/**
	 * initializes the composite itself (creates drawingPanel and jcpModel..)
	 * 
	 * @param page The ReactionEditor 
	 * @throws BioclipseException 
	 */
	private void init(ReactionEditor page) throws BioclipseException {
		this.setLayout(new FillLayout());	
		drawingPanel = new JChemPaintEditorWidget(this,SWT.NONE);
		List<ICDKReaction> model = null;
		model = page.getModelFromEditorInput();
		drawingPanel.setReaction(model.get( 0 ).getReaction());
//		jcpModel.activate();
		//registerSpecMolController();
		//jcpModel.getRendererModel().addCDKChangeListener(page);	
	}
	
	
	/**
	 * get the drawingPanel
	 * 
	 * @return DrawingPanel drawingPanel
	 */
	public JChemPaintEditorWidget getDrawingPanel() {
		return drawingPanel;
	}

	/* (non-Javadoc)
	 * @see bc_specmol.listener.SpecMolListener#selectionChanged(bc_specmol.editors.AssignmentController)
	 */
//	public void selectionChanged(IChemModel chemModel) {
//		System.out.println("selectionChanged - reactionMolDrawing");
//		jcpModel.setChemModel(chemModel);
//		jcpModel.resetIsModified();
//		registerSpecMolController();
//		drawingPanel.repaint();
//		
//	}

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
	public void loadNewMolecule(IMolecule newModel) {
	    drawingPanel.setAtomContainer( newModel );
		this.setSize(getSize().x,getSize().y-1);
		//registerSpecMolController();
	}

  /**
   * load a new ChemModel into the drawing panel
   * 
   * @param IChemModel model2
   * @param HashMap    coordenates
   */
  public void loadNewReactionSet(IReactionSet newModel) {
      drawingPanel.setReactionSet( newModel );
    this.setSize(getSize().x,getSize().y-1);
    //registerSpecMolController();
  }
  
  /**
	 * register the SpecMolController2D to the drawingPanel
	 */
//	private void registerSpecMolController() {
//		Controller2DModel contrModel = new Controller2DModel();
//		contrModel.setDrawMode(Controller2DModel.LASSO);
//		contrModel.setMovingAllowed(false);
////		SimpleController2D contr = new SpecMolController2D(jcpModel.getChemModel(), jcpModel.getRendererModel(), contrModel, this);
////		contr.addCDKChangeListener(jcpModel);
////		drawingPanel.addMouseListener(contr);
////		drawingPanel.addMouseMotionListener(contr);
//		
////		TODO should not be neccessary - abstractController should work without undoRedoHandler as well...
//		JCPBioclipseUndoRedoHandler undoRedoHandler=new JCPBioclipseUndoRedoHandler();
//		undoRedoHandler.setDrawingPanel(drawingPanel);
//		undoRedoHandler.setJcpm(jcpModel);
//		undoRedoHandler.setUndoContext(new UndoContext());
////		contr.setUndoRedoHandler(undoRedoHandler);
//	}

}
