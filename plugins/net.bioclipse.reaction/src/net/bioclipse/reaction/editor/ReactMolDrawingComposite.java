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
		drawingPanel.setReaction( model.get( 0 ).getReaction());
	}
	
	
	/**
	 * get the drawingPanel
	 * 
	 * @return DrawingPanel drawingPanel
	 */
	public JChemPaintEditorWidget getDrawingPanel() {
		return drawingPanel;
	}


	/**
	 * get the assignment editor page 
	 * 
	 * @return ReactionEditor page
	 */
	public ReactionEditor getPage() {
		return page;
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
  }
}
