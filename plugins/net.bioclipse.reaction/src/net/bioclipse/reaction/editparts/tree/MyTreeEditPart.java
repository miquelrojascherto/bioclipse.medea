package net.bioclipse.reaction.editparts.tree;

import java.beans.PropertyChangeListener;

import net.bioclipse.reaction.model.AbstractModel;

import org.eclipse.gef.editparts.AbstractTreeEditPart;

/**
 * 
 * @author Miguel Rojas
 */
public abstract class MyTreeEditPart extends AbstractTreeEditPart implements PropertyChangeListener {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#activate()
	 */
	public void activate() {
		super.activate();
		((AbstractModel) getModel()).addPropertyChangeListener(this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#deactivate()
	 */
	public void deactivate() {
		((AbstractModel) getModel()).removePropertyChangeListener(this);
		super.deactivate();
	}
}
