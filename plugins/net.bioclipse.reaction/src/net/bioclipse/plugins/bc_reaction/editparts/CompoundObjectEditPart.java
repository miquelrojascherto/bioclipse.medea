package net.bioclipse.plugins.bc_reaction.editparts;

import net.bioclipse.plugins.bc_reaction.model.AbstractObjectModel;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;

/**
 * The editor part of the CompoundObject
 * 
 * @author Miguel Rojas
 */
public class CompoundObjectEditPart extends MyAbstractObjectEditPart{
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		AbstractObjectModel model = (AbstractObjectModel)getModel();
		
		Label label = new Label();
		label.setText(model.getText());
		label.setBorder(new CompoundBorder(new LineBorder(),new MarginBorder(3)));
		label.setBackgroundColor(ColorConstants.orange);
		label.setOpaque(true);
		return label;
	}
}
