package net.bioclipse.plugins.bc_reaction.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
/**
 * The editor part of the ArrowConnectionObject
 * 
 * @author Miguel Rojas
 */
public class ArrowConnectionEditPart extends MyAbstractConnectionEditPart{
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure(){
		PolylineConnection connection = new PolylineConnection();
		connection.setTargetDecoration(new PolygonDecoration());
		return connection;
	}
}
