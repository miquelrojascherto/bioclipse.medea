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
import org.eclipse.ui.views.properties.IPropertySource;
import org.openscience.cdk.interfaces.IReactionScheme;
import org.openscience.cdk.libio.cml.Convertor;
import org.xmlcml.cml.element.CMLCml;

/**
 * The CDKReaction wraps an IReaction
 *
 */
public class CDKReactionScheme extends BioObject implements ICDKReactionScheme {

    private IReactionScheme reaction;
    // cached properties
    private String cachedSMILES;

    private static Preferences prefs;
    /*
     * Needed by Spring
     */
    public CDKReactionScheme(){
    	super();
        if (prefs == null && Activator.getDefault() != null) {
            prefs = Activator.getDefault().getPluginPreferences();
        }
    }
    
    public CDKReactionScheme(IReactionScheme reaction) {
    	this();
        this.reaction=reaction;
    }

//    public String getSMILES(net.bioclipse.core.domain.IReaction.Property urgency) throws BioclipseException {
//
//        //TODO: wrap in job?
//        if (urgency == net.bioclipse.core.domain.IReaction.Property.USE_CACHED) return cachedSMILES;
//
//        if (cachedSMILES != null &&
//            urgency == net.bioclipse.core.domain.IReaction.Property.USE_CACHED_OR_CALCULATED) {
//            return cachedSMILES;
//        }
//
//        if (getReactionScheme() == null)
//            throw new BioclipseException("Unable to calculate SMILES: Reaction scheme is empty");
//
//        if (!(getReactionScheme() instanceof IReactionScheme))
//            throw new BioclipseException("Unable to calculate SMILES: Not a reaction.");
//
//        SmilesGenerator generator = new SmilesGenerator();
//        String cachedSMILES = "";
//        try {
//        	IReactionSet reactionList = ReactionSchemeManipulator.getAllReactions(reaction);
//        	for(IReaction react:reactionList.reactions())
//        		cachedSMILES += generator.createSMILES(react)+"\n";
//        } catch ( CDKException e ) {
//            throw new BioclipseException(e.getMessage());
//        }
//
//        return cachedSMILES;
//    }

    public IReactionScheme getReactionScheme() {
        return reaction;
    }


    public String getCML() throws BioclipseException {

        if (getReactionScheme()==null) throw new BioclipseException("No reaction scheme to " +
        "get CML from!");

        Convertor convertor = new Convertor(true, null);
        CMLCml cmlReact = convertor.cdkReactionSchemeToCMLReactionSchemeAndMoleculeList( getReactionScheme());
        return cmlReact.toXML();
    }
    
    public Object getAdapter( Class adapter ) {
        
        if (adapter == IReactionScheme.class){
            return this;
        }
        
        if (adapter.isAssignableFrom(IReactionScheme.class)) {
            return this.getReactionScheme();
        }
        
        if (adapter.isAssignableFrom(IPropertySource.class)) {
            return new CDKReactionSchemePropertySource(this);
        }
        
        return super.getAdapter( adapter );
    }
}
