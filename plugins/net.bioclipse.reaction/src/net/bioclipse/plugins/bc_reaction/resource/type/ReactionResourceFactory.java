/*******************************************************************************
 * Copyright (c) 2005 Bioclipse Project
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ola Spjuth - core API and implementation
 *******************************************************************************/
package net.bioclipse.plugins.bc_reaction.resource.type;

import net.bioclipse.model.BioResourceFactory;
import net.bioclipse.model.BioResourceType;
import net.bioclipse.model.IBioResource;
import net.bioclipse.plugins.bc_reaction.resource.ReactionResource;

/**
 * A factory that instantiates Folders for the File Viewer
 * 
 * @author Miguel Rojas
 */
public class ReactionResourceFactory extends BioResourceFactory{
	
	/*
	 * (non-Javadoc)
	 * @see net.bioclipse.model.BioResourceFactory#newItem(net.bioclipse.model.BioResourceType, java.lang.Object)
	 */
	public IBioResource newItem(BioResourceType type,Object obj) {
		return new ReactionResource(type, obj);
	}
	/*
	 * (non-Javadoc)
	 * @see net.bioclipse.model.BioResourceFactory#loadItem(net.bioclipse.model.BioResourceType, java.lang.String)
	 */
	public IBioResource loadItem(BioResourceType type,String info) {
		return ReactionResource.loadItem(type, info);
    }
	/*
	 * (non-Javadoc)
	 * @see net.bioclipse.model.BioResourceFactory#newResource(net.bioclipse.model.BioResourceType, java.lang.Object, java.lang.String)
	 */
	public IBioResource newResource(BioResourceType type, Object resourceObject, String name) {
		return ReactionResource.newResource(type, resourceObject, name);
	}

}
