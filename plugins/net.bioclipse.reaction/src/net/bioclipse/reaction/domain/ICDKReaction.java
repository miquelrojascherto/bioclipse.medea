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

import org.openscience.cdk.interfaces.IReaction;


/**
 * An intertace for CDKReaction
 *
 */
public interface ICDKReaction extends net.bioclipse.core.domain.IReaction{

    public IReaction getReaction();
}
