package net.bioclipse.plugins.bc_reaction.editparts;

import net.bioclipse.plugins.bc_reaction.model.AbstractObjectModel;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.widgets.Text;
/**
 * 
 * @author Miguel Rojas
 */
public class MyDirectEditManager extends DirectEditManager{

	private AbstractObjectModel reactionModel;

	/**
	 * Constructor of the MyDirectEditManager object
	 * 
	 * @param source      The GraphicalEditPart
	 * @param editorType  The Class
	 * @param locator     The CellEditorLocator
	 */
	public MyDirectEditManager(GraphicalEditPart source, Class editorType, CellEditorLocator locator) {
		super(source, editorType, locator);
		reactionModel = (AbstractObjectModel)source.getModel();
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.tools.DirectEditManager#initCellEditor()
	 */
	protected void initCellEditor() {
		getCellEditor().setValue(reactionModel.getText());
		Text text = (Text)getCellEditor().getControl();
		text.selectAll();
		
	}

}
