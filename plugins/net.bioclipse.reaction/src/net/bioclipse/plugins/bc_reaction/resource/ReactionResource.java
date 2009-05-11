package net.bioclipse.plugins.bc_reaction.resource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

import net.bioclipse.dialogs.PerspectiveSwitchQuestionDialog;
import net.bioclipse.model.BioResource;
import net.bioclipse.model.BioResourceType;
import net.bioclipse.model.IBioResource;
import net.bioclipse.model.IPersistedResource;
import net.bioclipse.model.PersistedResource;
import net.bioclipse.plugins.Bc_reactionPlugin;
import net.bioclipse.plugins.bc_reaction.editors.ReactionMultiPageEditor;
import net.bioclipse.plugins.bc_reaction.perspective.ReactionPerspective;
import net.bioclipse.util.BioclipseConsole;
import net.bioclipse.util.BioclipseConstants;

import org.apache.log4j.Logger;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.PlatformUI;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IChemSequence;
import org.openscience.cdk.interfaces.IReactionSet;
import org.openscience.cdk.io.IChemObjectReader;
import org.openscience.cdk.io.IChemObjectWriter;
import org.openscience.cdk.io.ReaderFactory;
import org.openscience.cdk.io.formats.IChemFormat;
import org.openscience.cdk.io.formats.IResourceFormat;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;
import org.openscience.cdk.tools.manipulator.MoleculeSetManipulator;

/**
 * 
 * Extends PersistedResource to load and parse a File into a ReactionResource object
 * 
 * @author Miguel Rojas
 */

public class ReactionResource extends BioResource {

	private static final Logger logger = Logger.getLogger(ReactionResource.class);
	public static final String ID = "bioclipse.ReactionResource";
	public boolean moleculeChanged = false;
	private IResourceFormat chemFormat;

	/**
	 * The constructor of ReactionResource object
	 * 
	 * @param name A String with the name
	 */
	public ReactionResource(String name) {
		super(name);
	}

	/**
	 * The constructor of ReactionResource object
	 * 
	 * @param type The BioResourceType
	 * @param obj  The new Resource object
	 */
	public ReactionResource(BioResourceType type, Object obj) {
		super(type,obj);
	}

	/**
	 * Bypass having a file and write a string directly to persistedResource
	 * @param type       The BioResourceType 
	 * @param resString  The String newResource
	 * @param name       The String name
	 */
	public ReactionResource(BioResourceType type, String resString, String name) {
		super(name);
		if (getPersistedResource()==null){
			persistedResource = PersistedResource.newResource(resString); 
		}
		setDefaultResourceType(type);

	}
	/**
	 * Load the Item Resource
	 * @param type      The BioResourceType
	 * @param nameFile  The Name of the file 
	 * @return          The BioResource
	 */
	public static IBioResource loadItem(BioResourceType type, String nameFile) {
		File aFile = new File(nameFile);
		if (aFile == null)
			return null;
		else
			return new ReactionResource(type, aFile);
	}


	/**
	 * Parse the resourceString into children or parsedResource object
	 * @return true, if it was parsed good.
	 */
	public boolean parseResource(){
		if (!getPersistedResource().isLoaded()) return false;	//Return false if not loaded
		if (isParsed()) return true;	//Return true if already parsed
		if (isTriedParsed()) return false;	//Return false if already tried
		
		setTriedParsed(true);
		
		IChemFile chemFile=null;	//To hold parsed resource
		
		byte[] buffer=getPersistedResource().getInMemoryResource();
		ByteArrayInputStream bs = new ByteArrayInputStream(buffer);
		
		long time = System.currentTimeMillis();
		
		IChemObjectReader reader;
		try {
			reader = new ReaderFactory().createReader(bs);
			if (reader==null){
				setParsed(false);
				return false;
			}
			
			chemFile = new org.openscience.cdk.ChemFile();
			chemFile=(IChemFile)reader.read(chemFile);
			
			//Store the chemFormat used for the reader
			chemFormat = reader.getFormat();
			
			if (chemFile==null || chemFile.getChemSequenceCount()==0){
				setParsed(false);
				return false;
			}
			
		} catch (IOException e) {
			setParsed(false);
			return false;
		} catch (CDKException e) {
			setParsed(false);
			return false;
		} catch (ArrayIndexOutOfBoundsException e) {
			setParsed(false);
			return false;
		} catch (Exception e) {
			setParsed(false);
			return false;
		}
		
		
		//---------------------------------------
		//We have successfully read a chemfile
		//Now, see what it contains
		//---------------------------------------
		try {
			
			long endTime = System.currentTimeMillis();
			BioclipseConsole.writeToConsole("Parsed file in " + (endTime - time) + " ms.");
			
			//If there are many chemModels, put them in separate IBioResources
			List chemModelsList = ChemFileManipulator.getAllChemModels(chemFile);
			IChemModel chemModel = (IChemModel)chemModelsList.get(0);
			
			if(chemModel != null){
				IReactionSet reactionSet = chemModel.getReactionSet();
			if(reactionSet != null )
			if (chemModelsList.size() == 1 && reactionSet.getReactionCount() > 0){
				setParsedResource(chemFile);
				setParsed(true);
				openPerspective();
				return true;
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
			setParsed(false);
			return false;
		}
		
		
		
		setParsed(false);
		return false;
	}

	/**
	 * Open the reactions perspective if it is necessary
	 */
	private void openPerspective() {
		Bc_reactionPlugin plugin = Bc_reactionPlugin.getDefault();
		boolean open = false;
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getPerspective().getId().compareTo(ReactionPerspective.ID_PERSPECTIVE) != 0) {
			if(plugin.getOpenPerspectivePreference() == plugin.TRUE) {
				open = true;
			}
			else if (plugin.getOpenPerspectivePreference() == plugin.FALSE) {
				open = false;
			}
			else {
				open = PerspectiveSwitchQuestionDialog.openQuestion(plugin, PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Open Reactions Perspective", 
						"This file seems to contain a reaction - should the Reactions Perspective be opened?");
			}
			if (open) {
					IPerspectiveDescriptor persp = PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId(ReactionPerspective.ID_PERSPECTIVE);
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().setPerspective(persp);
			}
		}
		
	}

	/**
	 * Make a copy of this object and return it if it can be parsed. 
	 * Used to create new objects with a higher level taht replaces the old on parse 
	 * 
	 * @param type              The BioResourceType
	 * @param resourceObject    The ResourceObject
	 * @param name              The String name
	 * @return The IBioResource object
	 */
	public static IBioResource newResource(BioResourceType type, Object resourceObject, String name) {
		if (resourceObject instanceof IPersistedResource) {
			IPersistedResource persRes = (IPersistedResource) resourceObject;

			//This is the copy
			ReactionResource fResource = new ReactionResource(type, persRes);
			fResource.setName(name);
			if (fResource.parseResource() == true){
				return fResource;
			}
			else{
				System.out.println("PersistedResource:" + fResource.getName() + " could not be parsed into a ReactionResource+");
				return null;
			}

		}	

		if (resourceObject instanceof File) {
			File fFile = (File) resourceObject;

			//This is the copy
			ReactionResource fResource= new ReactionResource(type, fFile);

			if (fResource.parseResource() == true){
				return fResource;
			}
			else{
				System.out.println("File:" + fFile.getName() + " could not be parsed into a CMLFile+");
				return null;
			}
		}

		//This will happen for subResources
		else if (resourceObject instanceof String) {
			ReactionResource cmlfResource = new ReactionResource(type, (String)resourceObject, name);
			if (cmlfResource!=null){
				return cmlfResource;
			}
		}

		System.out.println("ResourceObject neither a File nor String. Discarded.+");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see net.bioclipse.model.BioResource#getEditorIDs()
	 */
	public String[] getEditorIDs(){
		String[] ret = new String[3];
		ret[0] = ReactionMultiPageEditor.EDITOR_ID;
		ret[1] = BioclipseConstants.XML_EDITOR;
		ret[2] = BioclipseConstants.DEFAULT_EDITOR;
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see net.bioclipse.model.BioResource#save()
	 */
	public boolean save() {
		Object parseRes = this.getParsedResource();
		if (parseRes instanceof IChemFile) {
			IChemFile chemFile = (IChemFile) parseRes;
			byte[] byteStream = serialiseChemFileToByteArray(chemFile);
			this.getPersistedResource().setInMemoryResource(byteStream);
			this.getPersistedResource().save();
			this.setParsedResourceDirty(false);
			return true;
		}
		else {
			logger.debug("Could not serialize in ReactionResource, using saveWithoutParse()");
			return super.saveWithoutParse();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.bioclipse.model.BioResource#updateParsedResourceFromString(java.lang.String)
	 */
	public boolean updateParsedResourceFromString(String resString) {
		
		ByteArrayInputStream bs = new ByteArrayInputStream(resString.getBytes());
		IChemObjectReader reader = null;
		try {
			reader = new ReaderFactory().createReader(bs);
			if (reader == null) {
				return false;
			}
			this.setChemFormat(reader.getFormat());
		} catch (IOException e) {
			System.err.println("error in updateParsedResourceFromString: "+e);
			return false;
		}
		
		IChemFile chemFile = new org.openscience.cdk.ChemFile();
		IChemObjectBuilder builder = chemFile.getBuilder();
		try {
			chemFile=(IChemFile)reader.read(chemFile);
			builder = chemFile.getBuilder();
			IChemSequence cseq = builder.newChemSequence();
			cseq.addChemModel(chemFile.getChemSequence(0).getChemModel(0));
			IChemFile cfile = builder.newChemFile();
			cfile.addChemSequence(cseq);
			reader.close();
			this.setParsedResource(cfile);
		} catch (CDKException e) {
			System.err.println("error in updateParsedResourceFromString: "+e);
			return false;
		} catch (IOException e) {
			System.err.println("error in updateParsedResourceFromString: "+e);
			return false;
		}
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.bioclipse.model.BioResource#getParsedResourceAsString()
	 */
	public String getParsedResourceAsString() {
		Object parsedRes = this.getParsedResource();
		if (parsedRes instanceof IChemFile) {
			IChemFile chemFile = (IChemFile) parsedRes;
			byte[] byteStream = serialiseChemFileToByteArray(chemFile);
			return new String(byteStream);
		}
		else {
			return null;
		}
	}

	/**
	 * get if the molecule has changed
	 * @return true, if the IMolecule has changed
	 */
	public boolean isMoleculeChanged() {
		return moleculeChanged;
	}
	/**
	 * set the IMolecule with the changes
	 * @param moleculeChanged The IMolecule with the changes 
	 */
	public void setMoleculeChanged(boolean moleculeChanged) {
		this.moleculeChanged = moleculeChanged;
	}
	
	public String getChemFormatName() {
		if (chemFormat!=null){
			return chemFormat.getFormatName();
		}
		return "";
	}
	
	public IResourceFormat getChemFormat() {
		return chemFormat;
	}
	public void setChemFormat(IResourceFormat chemFormat) {
		this.chemFormat = chemFormat;
	}
	
	private byte[] serialiseChemFileToByteArray(IChemFile chemFile) {
		IResourceFormat format = getChemFormat();
		if (format instanceof IChemFormat) {
			String writerClassName = ((IChemFormat)format).getWriterClassName();
			IChemObjectWriter chemWriter = null;
			Class<?> writer = null;
			try {
				writer = Class.forName(writerClassName);
				try {
					chemWriter = (IChemObjectWriter) writer.newInstance();
				} catch (InstantiationException e) {
					logger.error(e.getMessage(), e);
					//FIXME: Handle the exception!
					
				} catch (IllegalAccessException e) {
					logger.error(e.getMessage(), e);
					//FIXME: Handle the exception!
					
				}
			} catch (ClassNotFoundException e) {
				logger.error(e.getMessage(), e);
				//FIXME: Handle the exception!
				
			}
			StringWriter out = new StringWriter();
			if (writer != null) {
				try {
					chemWriter.setWriter(out);

					//distintion between IMoleculesSet and IReactionSet
					if(chemFile.getChemSequence(0).getChemModel(0).getReactionSet() != null){
						IReactionSet reactionSet = chemFile.getChemSequence(0).getChemModel(0).getReactionSet();
						chemWriter.write(reactionSet);
					}else 
						if(chemFile.getChemSequence(0).getChemModel(0).getMoleculeSet() != null){
							List containersList = MoleculeSetManipulator.getAllAtomContainers((chemFile.getChemSequence(0).getChemModel(0).getMoleculeSet()));
							Iterator iterator = containersList.iterator();
							while(iterator.hasNext()){
								chemWriter.write((IAtomContainer)iterator.next());
							}
						}
					chemWriter.close();
					out.close();
				} catch (CDKException e) {
					logger.error(e.getMessage(), e);
					//FIXME: Handle the exception!
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error(e.getMessage(), e);
				}
			}
			byte[] byteStream = out.toString().getBytes();
			return byteStream;
		} else if (format == null){
			logger.error("Could not save in format. the ChemFormat is null!");
		} else {
			logger.error("Could not save in format: " + format.getFormatName());
		}
		return new byte[0];
	}
	
}
