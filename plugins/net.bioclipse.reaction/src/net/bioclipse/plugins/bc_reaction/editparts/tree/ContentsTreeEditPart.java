package net.bioclipse.plugins.bc_reaction.editparts.tree;

import java.beans.PropertyChangeEvent;
import java.util.List;

import net.bioclipse.plugins.bc_reaction.model.ContentsModel;

/**
 * 
 * @author Miguel Rojas
 */
public class ContentsTreeEditPart extends MyTreeEditPart {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren() {
		return ((ContentsModel) getModel()).getChildren();
	}

	/*
	 * (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ContentsModel.P_CHILDREN))
			refreshChildren();
	}
}
