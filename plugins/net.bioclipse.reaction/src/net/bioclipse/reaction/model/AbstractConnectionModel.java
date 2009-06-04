/*******************************************************************************
 * Copyright (c) 2007-2009  Miguel Rojas <miguelrojasch@users.sf.net>, 
 *                          Stefan Kuhn <shk3@users.sf.net>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.org—epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.reaction.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
/**
 * 
 * @author Miguel Rojas
 */
public class AbstractConnectionModel extends AbstractModel{
	private AbstractObjectModel source, target;
	private List<Object> bendpoints = new ArrayList<Object>();
	
	public static final String P_BEND_POINT = "_bend_point";
	
	public void attachSource(){
		if(!source.getModelSourceConnections().contains(this))
			source.addSourceConnection(this);
	}
	
	public void attachTarget(){
		if(!target.getModelTargetConnections().contains(this))
			target.addTargetConnection(this);
	}
	
	public void detachSource(){
		source.removeSourceConnection(this);
	}
	
	public void detachTarget(){
		target.removeTargetConnection(this);
	}
	
	public AbstractObjectModel getSource(){
		return source;
	}
	
	public AbstractObjectModel getTarget(){
		return target;
	}
	
	public void setSource(AbstractObjectModel model){
		source = model;
	}
	
	public void setTarget(AbstractObjectModel model){
		target = model;
	}
	@SuppressWarnings("unchecked")
	public void addBendpoint(int index, Point point){
		bendpoints.add(index,point);
		firePropertyChange(P_BEND_POINT,null,null);
	}
	public List<Object> getBendpoints(){
		return bendpoints;
	}
	public void removeBendpoint(int index){
		bendpoints.remove(index);
		firePropertyChange(P_BEND_POINT,null,null);
	}
	@SuppressWarnings("unchecked")
	public void replaceBendpoint(int index, Point point){
		bendpoints.set(index,point);
		firePropertyChange(P_BEND_POINT,null,null);
	}
}
