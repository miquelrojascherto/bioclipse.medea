package net.bioclipse.reaction.editparts;

import net.bioclipse.reaction.model.ArrowConnectionModel;
import net.bioclipse.reaction.model.CompoundObjectModel;
import net.bioclipse.reaction.model.ContentsModel;
import net.bioclipse.reaction.model.LineConnectionModel;
import net.bioclipse.reaction.model.ReactionObjectModel;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
/**
 * 
 * @author Miguel Rojas
 */
public class MyEditPartFactory implements EditPartFactory {
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object model){
		EditPart part = null;

		if(model instanceof ContentsModel)
			part = new ContentsEditPart();
		else if(model instanceof ReactionObjectModel)
			part = new ReactionObjectEditPart();
		else if(model instanceof CompoundObjectModel)
			part = new CompoundObjectEditPart();
		else if (model instanceof LineConnectionModel)
			  part = new LineConnectionEditPart();
		else if(model instanceof ArrowConnectionModel)
			part = new ArrowConnectionEditPart();
		
		part.setModel(model);
		return part;
	}
}
