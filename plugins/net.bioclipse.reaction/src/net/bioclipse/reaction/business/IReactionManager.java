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
package net.bioclipse.reaction.business;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.Recorded;
import net.bioclipse.core.TestClasses;
import net.bioclipse.core.TestMethods;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IReaction;
import net.bioclipse.core.domain.IReactionScheme;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.reaction.domain.ICDKReaction;
import net.bioclipse.reaction.domain.ICDKReactionScheme;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.content.IContentType;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.io.formats.IChemFormat;

@PublishedClass( "Contains Reaction related methods")
@TestClasses(
    "net.bioclipse.reaction.business.test.APITest," +
    "net.bioclipse.reaction.business.test.AbstractReactionManagerPluginTest"
)
public interface IReactionManager extends IBioclipseManager {

    /**
     * Loads an assigned reaction scheme from file.
     *
     * @param path The path to the file
     * @return loaded reaction scheme
     * @throws IOException
     * @throws BioclipseException
     * @throws CoreException 
     */
    @Recorded
    @PublishedMethod( params = "String path", 
                      methodSummary = "Loads an assigned reaction scheme from file. ")
    @TestMethods("testLoadReactionScheme_String")
    public ICDKReactionScheme loadReactionScheme( String path )
        throws IOException, BioclipseException, CoreException;

    /**
     * Load assigned reaction scheme from an <code>IFile</code>.
     * 
     * @param file to be loaded
     * @return loaded assigned reaction scheme
     * @throws IOException
     * @throws BioclipseException
     * @throws CoreException 
     */
    @PublishedMethod( params = "IFile file", 
            methodSummary = "Loads an assigned reaction scheme from file. ")
    @Recorded
    @TestMethods("testLoadReactionScheme_IFile")
    public ICDKReactionScheme loadReactionScheme( IFile file )throws IOException,
															    BioclipseException,
															    CDKException,
															    CoreException;
    
    @Recorded
    @PublishedMethod(
         params="InputStream instream, IChemFormat format",
         methodSummary="Reads a stream into a list of ICDKReactionScheme.")
    @TestMethods("testLoadReactionScheme_InputStream_IProgressMonitor_IChemFormat")
    public ICDKReactionScheme loadReactionScheme( InputStream instream,
            IChemFormat format )
            throws BioclipseException,
            CDKException,
            IOException;

    @Recorded
    @PublishedMethod( 
        params = "String path",
        methodSummary = "Determines the file format if the file, if chemical reaction.")
    @TestMethods("testDetermineFormat")
    public String determineFormat(String path) 
                  throws IOException, CoreException;

    @Recorded
    @PublishedMethod(
         params = "IContentType type",
         methodSummary = "Determines the IChemFormat equivalent of the given" +
                         "content type" )
    public IChemFormat determineFormat(IContentType type);

    /**
     * Save a reaction scheme in same format as loaded to same filename, if exists
     * @param reaction The reaction to save
     * @throws IllegalStateException
     */
    @Recorded
    @PublishedMethod(params = "IReactionScheme reaction",
            methodSummary="Saves reaction scheme to file, if read from file.")
    @TestMethods("testSaveReaction_IReactionScheme")
    public void saveReactionScheme(IReactionScheme reaction)
    	          throws BioclipseException, CDKException, CoreException;

    /**
     * Save a reaction scheme in same format as loaded to same filename, if exists
     * @param reaction The reaction to save
     * @param overwrite If set to true, overwrite if file exists
     * @throws IllegalStateException
     */
    @Recorded
    @PublishedMethod(
        params = "IReactionScheme reaction, boolean overwrite",
        methodSummary = "saves reaction scheme to a file, if previously read from file. " +
            		        "Overwrite determines if existing file shall be " +
            		        "overwritten." )
    @TestMethods("testSaveReaction_IReactionScheme_boolean")
    public void saveReaction(IReactionScheme reaction, boolean overwrite)
    	          throws BioclipseException, CDKException, CoreException;

    /**
     * Save a reaction scheme in same format as loaded
     * @param reaction The reaction to save
     * @param filename Where to save, relative to workspace root
     * @throws IllegalStateException
     */
    @Recorded
    @PublishedMethod(
        params = "IReactionScheme reaction, String filename",
        methodSummary = "saves mol to a file (filename must be a relative to " +
        		            "workspace root and folder must exist), filetype " +
        		            "must be one of the constants given by " +
        		            "getPossibleFiletypes" )
    @TestMethods("testSaveReaction_IReactionScheme_String")
    public void saveReaction(IReactionScheme reaction, String filename)
    	          throws BioclipseException, CDKException, CoreException;

    /**
     * Save a reaction scheme in same format as loaded
     * @param reaction The reaction to save
     * @param filename Where to save, relative to workspace root
     * @param overwrite If set to true, overwrite if file exists
     * @throws IllegalStateException
     */
    @Recorded
    @PublishedMethod(
        params = "IReactionScheme reaction, String filename, boolean overwrite",
        methodSummary = "saves reaction scheme to a file (filename must be a relative " +
        		            "to workspace root and folder must exist), with a " +
        		            "given filename and overwrites determined by the " +
        		            "given boolean. File type is taken from the mol " +
        		            "object, if available. If not, then the file " +
        		            "extension is used to make a somewhat educated guess." )
    @TestMethods("testSaveReaction_IReactionScheme_String_boolean")
    public void saveReaction(IReactionScheme reaction, String filename, boolean overwrite)
    	          throws BioclipseException, CDKException, CoreException;

    /**
     * Save a reaction in same format as loaded
     * @param reaction The reaction scheme to save
     * @param file Where to save
     * @param overwrite If set to true, overwrite if file exists
     * @throws IllegalStateException
     */
    @Recorded
    @TestMethods("testSaveReaction_IReactionScheme_IFile_boolean")
    public void saveMolecule(IReactionScheme reaction, IFile file, boolean overwrite)
    	          throws BioclipseException, CDKException, CoreException;

    /**
     * @param reaction The reaction scheme to save
     * @param filename Where to save, relative to workspace root
     * @param filetype Which format to save (for formats, see constants)
     * @throws IllegalStateException
     */
    @Recorded
    @PublishedMethod(
        params = "IReactionScheme reaction, String filename, IChemFormat filetype",
        methodSummary = "saves reaction scheme to a file (filename must be a relative to " +
        		            "workspace root and folder must exist), filetype " +
        		            "must be a IChemFormat" )
    @TestMethods("testSaveReaction_IReactionScheme_String_IChemFormat")
    public void saveReaction( IReactionScheme reaction, 
                              String filename, 
                              IChemFormat filetype )
    	          throws BioclipseException, CDKException, CoreException;

    /**
     * @param reaction The reaction scheme to save
     * @param filename Where to save, relative to workspace root
     * @param filetype Which format to save (for formats, see constants)
     * @param overwrite if true and file exists, overwrite
     * @throws IllegalStateException
     */
    @Recorded
    @PublishedMethod(
        params = "IReactionScheme reaction, String filename, " +
        		     "IChemFormat filetype, boolean overwrite",
        methodSummary = "saves reaction scheme to a file (filename must be a relative to " +
        		            "workspace root and folder must exist), filetype " +
        		            "must be a IChemFormat. If overwrite=true then file " +
        		            "will be overwritten if exists." )
    @TestMethods("testSaveReaction_IReactionScheme_String_IChemFormat_boolean")
    public void saveReaction( IReactionScheme reaction, 
                              String filename, 
                              IChemFormat filetype, 
                              boolean overwrite )
    	          throws BioclipseException, CDKException, CoreException;

    /**
     * Creates a reaction scheme from a CML String
     * 
     * @param reaction
     * @return
     * @throws BioclipseException if input is null or parse fails
     * @throws IOException if file cannot be read
     */
    @PublishedMethod ( params = "String cml",
                       methodSummary = "Creates a reaction scheme from a " +
                                       "CML String" )
    @Recorded
    @TestMethods("testFromCml_String")
    public ICDKReactionScheme fromCml( String cml ) 
                        throws BioclipseException, IOException;

    /**
     * Creates a cdk reaction from an IReaction
     *
     * @param reaction
     * @return
     * @throws BioclipseException
     */
    @PublishedMethod ( 
        params = "IReaction reaction",
        methodSummary = "Creates a cdk reaction from a reaction" )
    @Recorded
    @TestMethods("testCreate_IReaction")
    public ICDKReaction create( IReaction reaction ) throws BioclipseException;

    @Recorded
    @PublishedMethod(
        params = "ICDKReaction reaction, String filename",
        methodSummary = "Saves a reaction in the MDL molfile RXN reaction format " +
        		            "(filename must be a relative to workspace root and " +
    								    "folder must exist)" )
    @TestMethods("testSaveMDLMolfile_ICDKReaction_String")
    public void saveMDLMolfile(ICDKReaction reaction, String filename) 
                throws InvocationTargetException, 
                       BioclipseException, 
                       CDKException, 
                       CoreException;

    @Recorded
    @PublishedMethod(
        params = "ICDKReaction reaction, String filename",
        methodSummary = "Saves a reaction in the Chemical Markup Language " +
        		            "format (filename must be a relative to workspace " +
        		            "root and folder must exist). Example of file " +
        		            "String: \"/Virtual/bla.cml\"" )
    @TestMethods("testSaveCML_ICDKReaction_String")
    public void saveCML(ICDKReaction cml, String filename) 
                throws InvocationTargetException, 
                       BioclipseException, 
                       CDKException, 
                       CoreException;
    
    @Recorded
    @PublishedMethod(
         params="IFile file",
         methodSummary="Reads a file into a list of ICDKReactions.")
    @TestMethods("testLoadReaction_IFile_IProgressMonitor")
    public List<ICDKReaction> loadReactions( IFile file )
        throws IOException,
        BioclipseException,
        CDKException,
        CoreException;

    @Recorded
    @PublishedMethod(
         params="InputStream instream, IChemFormat format",
         methodSummary="Reads a stream into a list of ICDKReaction.")
    @TestMethods("testLoadReaction_InputStream_IProgressMonitor_IChemFormat")
    public List<ICDKReaction> loadReactions( InputStream instream,
            IChemFormat format )
            throws BioclipseException,
            CDKException,
            IOException;

    
}