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

import net.bioclipse.cdk.business.Activator;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.BioObject;

import org.eclipse.core.runtime.Preferences;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.libio.cml.Convertor;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.xmlcml.cml.element.CMLReaction;

/**
 * The CDKReaction wraps an IReaction
 *
 */
public class CDKReaction extends BioObject implements ICDKReaction {

    private IReaction reaction;
    // cached properties
    private String cachedSMILES;

    private static Preferences prefs;
    /*
     * Needed by Spring
     */
    public CDKReaction(){
        super();
        if (prefs == null && Activator.getDefault() != null) {
            prefs = Activator.getDefault().getPluginPreferences();
        }
    }
    
    public CDKReaction(IReaction reaction) {
    	this();
        this.reaction=reaction;
    }

    public String getSMILES(net.bioclipse.core.domain.IReaction.Property urgency) throws BioclipseException {

        //TODO: wrap in job?
        if (urgency == net.bioclipse.core.domain.IReaction.Property.USE_CACHED) return cachedSMILES;

        if (cachedSMILES != null &&
            urgency == net.bioclipse.core.domain.IReaction.Property.USE_CACHED_OR_CALCULATED) {
            return cachedSMILES;
        }

        if (getReaction() == null)
            throw new BioclipseException("Unable to calculate SMILES: Reaction is empty");

        if (!(getReaction() instanceof IReaction))
            throw new BioclipseException("Unable to calculate SMILES: Not a reaction.");

        // Create the SMILES
        SmilesGenerator generator = new SmilesGenerator();
        
        try {
			cachedSMILES = generator.createSMILES(getReaction());
		} catch (CDKException e) {
			e.printStackTrace();
		}

        return cachedSMILES;
    }

    public IReaction getReaction() {
        return reaction;
    }


    public String getCML() throws BioclipseException {

        if (getReaction()==null) throw new BioclipseException("No molecule to " +
        "get CML from!");

        Convertor convertor = new Convertor(true, null);
        CMLReaction cmlMol = convertor.cdkReactionToCMLReaction( getReaction());
        return cmlMol.toXML();
    }
    
    public Object getAdapter( Class adapter ) {
        
        if (adapter == IReaction.class){
            return this;
        }
        
        if (adapter.isAssignableFrom(IReaction.class)) {
            return this.getReaction();
        }
//        if (adapter.isAssignableFrom(IPropertySource.class)) {
//            return new CDKReactionPropertySource(this);
//        }
        return super.getAdapter( adapter );
    }
}
