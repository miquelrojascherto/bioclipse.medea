package net.bioclipse.reaction.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Text;
/**
 * 
 * @author Miguel Rojas
 */
public class MyCellEditorLocator implements CellEditorLocator {
	private IFigure figure;
	
	/**
	 * Constructor of the MyCellEditorLocator object
	 * @param f The IFigure
	 */
	public MyCellEditorLocator(IFigure f){
		figure = f;
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.tools.CellEditorLocator#relocate(org.eclipse.jface.viewers.CellEditor)
	 */
	public void relocate(CellEditor celleditor) {
		Text text = (Text) celleditor.getControl();
//		Point pref = text.computeSize(-1,-1);
		Rectangle rect = figure.getBounds().getCopy();
		figure.translateToAbsolute(rect);
		text.setBounds(rect.x,rect.y,rect.width,rect.height);
		
	}

}
