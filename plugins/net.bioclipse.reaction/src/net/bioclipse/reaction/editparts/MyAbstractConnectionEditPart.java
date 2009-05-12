package net.bioclipse.reaction.editparts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import net.bioclipse.reaction.editpolicies.MyBendpointEditPolicy;
import net.bioclipse.reaction.editpolicies.MyConnectionEditPolicy;
import net.bioclipse.reaction.editpolicies.MyConnectionEndpointEditPolicy;
import net.bioclipse.reaction.model.AbstractConnectionModel;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
/**
 * 
 * @author Miguel Rojas
 */
public class MyAbstractConnectionEditPart extends AbstractConnectionEditPart implements PropertyChangeListener{
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure(){
		PolylineConnection connection = new PolylineConnection();
		connection.setConnectionRouter(new BendpointConnectionRouter());
		return connection;
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ROLE,new MyConnectionEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,new MyConnectionEndpointEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new MyBendpointEditPolicy());
		
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	public void activate(){
		super.activate();
		((AbstractConnectionModel) getModel()).addPropertyChangeListener(this);
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	public void deactivate(){
		((AbstractConnectionModel) getModel()).removePropertyChangeListener(this);
		super.deactivate();
		
	}
	/*
	 * (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals(AbstractConnectionModel.P_BEND_POINT))
			refreshBendpoints();
		
	}
	
	@SuppressWarnings("unchecked")
	private void refreshBendpoints() {
		List bendpoints = ((AbstractConnectionModel)getModel()).getBendpoints();
		List constraint = new ArrayList();
		
		for(int i = 0 ; i < bendpoints.size(); i++)
			constraint.add(new AbsoluteBendpoint((Point)bendpoints.get(i)));
		
		getConnectionFigure().setRoutingConstraint(constraint);
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals(){
		refreshBendpoints();
	}
	
}
