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
package net.bioclipse.reaction.business.test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import junit.framework.Assert;
import net.bioclipse.cdk.business.ICDKManager;
import net.bioclipse.core.MockIFile;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.reaction.business.IReactionManager;
import net.bioclipse.reaction.domain.ICDKReaction;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.junit.Test;
import org.openscience.cdk.io.formats.IChemFormat;
import org.openscience.cdk.io.formats.MDLRXNFormat;

/**
 * 
 * @author Miguel Rojas
 */
public abstract class AbstractReactionManagerPluginTest {
	//Needed to run these tests on some systems. If it breaks them on 
    //other systems we need to do some sort of checking before 
    //setting them...
    static {
        System.setProperty( "javax.xml.parsers.SAXParserFactory", 
                            "com.sun.org.apache.xerces.internal." 
                                + "jaxp.SAXParserFactoryImpl" );
        System.setProperty( "javax.xml.parsers.DocumentBuilderFactory", 
                            "com.sun.org.apache.xerces.internal."
                                + "jaxp.DocumentBuilderFactoryImpl" );
    }
    protected static ICDKManager   cdk;
    protected static IReactionManager reactionmanager;


  @Test public void testLoadReaction_InputStream_IProgressMonitor_IChemFormat() throws Exception,
																					  BioclipseException, 
																					  CoreException, 
																					  URISyntaxException {
      URI uri = getClass().getResource("/testFiles/0002.stg01.rxn").toURI();
      URL url=FileLocator.toFileURL(uri.toURL());
      String path=url.getFile();
      IFile file = new MockIFile( path );
      ICDKReaction reaction = reactionmanager.loadReactions( file.getContents(),(IChemFormat)MDLRXNFormat.getInstance()).get( 0 );

      Assert.assertNotNull(reaction);
      Assert.assertSame(1, reaction.getReaction().getReactantCount());
      Assert.assertSame(1, reaction.getReaction().getProductCount());

  }
  
  @Test public void testLoadReaction_IFile_IProgressMonitor() throws Exception,
																	  BioclipseException, 
																	  CoreException, 
																	  URISyntaxException {
	  
      URI uri = getClass().getResource("/testFiles/reaction.1.cml").toURI();
      URL url=FileLocator.toFileURL(uri.toURL());
      String path=url.getFile();
      IFile file = new MockIFile( path );
      ICDKReaction reaction = reactionmanager.loadReactions( file ).get( 0 );

      Assert.assertNotNull(reaction);
      Assert.assertSame(1, reaction.getReaction().getReactantCount());
      Assert.assertSame(1, reaction.getReaction().getProductCount());
      
  }
    
}
