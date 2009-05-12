package net.bioclipse.reaction.editparts.tree;

import net.bioclipse.reaction.model.AbstractObjectModel;
import net.bioclipse.reaction.model.ContentsModel;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
/**
 * 
 * @author Miguel Rojas
 */
public class TreeEditPartFactory implements EditPartFactory {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart part = null;

		if (model instanceof ContentsModel)
			part = new ContentsTreeEditPart();
		else if (model instanceof AbstractObjectModel)
			part = new ReactionTreeEditPart();

		if (part != null)
			part.setModel(model);

		return part;
	}

}
