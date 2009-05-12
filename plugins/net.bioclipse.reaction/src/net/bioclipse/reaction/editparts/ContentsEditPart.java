package net.bioclipse.reaction.editparts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import net.bioclipse.reaction.editpolicies.MyXYLayoutEditPolicy;
import net.bioclipse.reaction.model.ContentsModel;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.gef.EditPolicy;
/**
 * 
 * @author Miguel Rojas
 */
public class ContentsEditPart extends EditPartWithListener{
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		Layer figure = new Layer();
		figure.setLayoutManager(new XYLayout());
		return figure;
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new MyXYLayoutEditPolicy());
		
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren(){
		return ((ContentsModel)getModel()).getChildren();
	}
	/*
	 * (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt){
		if(evt.getPropertyName().equals(ContentsModel.P_CHILDREN))
			refreshChildren();
		
	}

}
