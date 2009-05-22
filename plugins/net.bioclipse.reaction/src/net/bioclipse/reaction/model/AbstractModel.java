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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
/**
 * 
 * @author Miguel Rojas
 */
abstract public class AbstractModel {
	
private PropertyChangeSupport listeners = new PropertyChangeSupport(this);
	
	public void addPropertyChangeListener(PropertyChangeListener listener){
		listeners.addPropertyChangeListener(listener);
	}
	
	public void firePropertyChange(String propName, Object oldValue, Object newValue){
		listeners.firePropertyChange(propName, oldValue, newValue);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener){
		listeners.removePropertyChangeListener(listener);
	}
	
	public Object getEditableValue() {
		return this; 
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[0];
	}

	public Object getPropertyValue(Object id) {
		return null;
	}

	public boolean isPropertySet(Object id) {
		return false;
	}

	public void resetPropertyValue(Object id) {
	}

	public void setPropertyValue(Object id, Object value) {
	}
}
