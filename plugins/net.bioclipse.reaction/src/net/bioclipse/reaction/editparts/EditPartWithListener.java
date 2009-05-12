package net.bioclipse.reaction.editparts;

import java.beans.PropertyChangeListener;

import net.bioclipse.reaction.model.AbstractModel;

import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
/**
 * 
 * @author Miguel Rojas
 */
abstract public class EditPartWithListener extends AbstractGraphicalEditPart implements PropertyChangeListener{
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	public void activate(){
		super.activate();
		((AbstractModel)getModel()).addPropertyChangeListener(this);
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	public void deactivate(){
		super.deactivate();
		((AbstractModel)getModel()).removePropertyChangeListener(this);
	}
}
