/*******************************************************************************
 * Copyright (c) 2009  Miguel Rojas <miguelrojasch@users.sf.net>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * www.eclipse.org—epl-v10.html <http://www.eclipse.org/legal/epl-v10.html>
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.reaction.domain;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.bioclipse.core.domain.props.BioObjectPropertySource;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.tools.manipulator.ReactionSchemeManipulator;

public class CDKReactionSchemePropertySource extends BioObjectPropertySource {

    protected static final String PROPERTY_NUM_REACTIONS = "Number of reactions";

    private final Object cdkPropertiesTable[][] =
    {
        { PROPERTY_NUM_REACTIONS,
            new TextPropertyDescriptor(PROPERTY_NUM_REACTIONS,PROPERTY_NUM_REACTIONS)},
            
    };

    private ArrayList<IPropertyDescriptor> cdkProperties;
    private HashMap<String, Object> cdkValueMap;

    public CDKReactionSchemePropertySource(CDKReactionScheme item) {
        super(item);
        
        cdkProperties = setupProperties(item.getReactionScheme());
        cdkValueMap = getPropertyValues(item);
    }

    /**
     * @param item
     */
    private HashMap<String, Object> getPropertyValues(CDKReactionScheme item) {
        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(
        		PROPERTY_NUM_REACTIONS,
        		ReactionSchemeManipulator.getAllReactions(item.getReactionScheme()).getReactionCount());
        
        // IChemObject.getProperties()
        Map<Object,Object> objectProps = item.getReactionScheme().getProperties();
        for (Object propKey : objectProps.keySet()) {
            String label = ""+propKey;
            valueMap.put(label, ""+objectProps.get(propKey));
        }
        return valueMap;
    }

    private ArrayList<IPropertyDescriptor> setupProperties(IChemObject object) {
        ArrayList<IPropertyDescriptor> cdkProperties =
            new ArrayList<IPropertyDescriptor>();
        // default properties
        for (int i=0;i<cdkPropertiesTable.length;i++) {
            PropertyDescriptor descriptor;
            descriptor = (PropertyDescriptor)cdkPropertiesTable[i][1];
            descriptor.setCategory("General");
            cdkProperties.add(descriptor);
        }
        // IChemObject.getProperties()
        Map<Object,Object> objectProps = object.getProperties();
        for (Object propKey : objectProps.keySet()) {
            PropertyDescriptor descriptor;
            String label = ""+propKey;
            descriptor = new TextPropertyDescriptor(label,label);
            descriptor.setCategory("Reaction Properties");
            cdkProperties.add(descriptor);
        }
        return cdkProperties;
    }

    public IPropertyDescriptor[] getPropertyDescriptors() {
        // Create the property vector.

        IPropertyDescriptor[] propertyDescriptors =
            new IPropertyDescriptor[cdkProperties.size()];
        for (int i=0; i< cdkProperties.size();i++){
            propertyDescriptors[i]=(IPropertyDescriptor) cdkProperties.get(i);
        }

        // Return it.
        return propertyDescriptors;
    }

    public Object getPropertyValue(Object id) {
        if (cdkValueMap.containsKey(id))
            return cdkValueMap.get(id);

        return super.getPropertyValue(id);
    }

    public ArrayList<IPropertyDescriptor> getProperties() {
        return cdkProperties;
    }

    public void setProperties(ArrayList<IPropertyDescriptor> properties) {
        this.cdkProperties = properties;
    }

    public HashMap<String, Object> getValueMap() {
        return cdkValueMap;
    }

    public void setValueMap(HashMap<String, Object> valueMap) {
        this.cdkValueMap = valueMap;
    }
    /*
	 * (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		
	}
}
