package net.bioclipse.plugins.model;

import java.io.File;

import net.bioclipse.model.BioResource;
import net.bioclipse.model.BioResourceType;
import net.bioclipse.model.IBioResource;
import net.bioclipse.model.IPersistedResource;
import net.bioclipse.model.PersistedResource;
import net.bioclipse.util.BioclipseConstants;

import org.apache.log4j.Logger;

/**
 * 
 * @author     Miguel Rojas
 * @created    30. november 2006
 *
 */
public class ARFFResource extends BioResource{
	/** Logger for this class */
	private static final Logger logger = Logger.getLogger(ARFFResource.class);
	
	public static final String ID = "bioclipse.ARFFResource";
	
	public ARFFResource(String name) {
		super(name);
	}

	public ARFFResource(BioResourceType type, Object obj) {
		super(type,obj);
	}

	/**
	 * Bypass having a file and write a string directly to persistedResource
	 * @param type
	 * @param resString
	 * @param name
	 */
	public ARFFResource(BioResourceType type, String resString, String name) {
		super(name);
		if (getPersistedResource()==null){
			persistedResource = PersistedResource.newResource(resString); 
		}
		setDefaultResourceType(type);

	}
	/**
	 * Return the list of editors in order of preference
	 */
	public String[] getEditorIDs()
	{
		String [] ret = new String[2];
		ret[0] = BioclipseConstants.XML_EDITOR;
		ret[1] = BioclipseConstants.DEFAULT_EDITOR;
		return ret;
	}
	/**
	 * Make a copy of this object and return it if it can be parsed. 
	 * Used to create new objects with a higher level that replaces the old on parse 
	 */
	public static IBioResource newResource(BioResourceType type, Object resourceObject, String name){
		//This should preferably factor out FileBuffer and String so no need for them here
		
		if (resourceObject instanceof IPersistedResource) {
			IPersistedResource persRes = (IPersistedResource) resourceObject;

			//This is the copy
			ARFFResource fResource= new ARFFResource(type, persRes);
			fResource.setName(name);
			if (fResource.parseResource() == true){
				return fResource;
			}
			else{
				System.out.println("My-PersistedResource:" + fResource.getName() + " could not be parsed into a CDKResource");
				int a=0;
				return null;
			}

		}	

		//This is wrong.
		//TODO



		if (resourceObject instanceof File) {
			File fFile = (File) resourceObject;

			//This is the copy
			ARFFResource fResource= new ARFFResource(type, fFile);

			if (fResource.parseResource()==true){
				return fResource;
			}
			else{
				System.out.println("File:" + fFile.getName() + " could not be parsed into a CMLFile");
				return null;
			}
		}

		//This will happen for subResources
		else if (resourceObject instanceof String) {
			ARFFResource cmlfResource= new ARFFResource(type, (String)resourceObject, name);
			if (cmlfResource!=null){
				return cmlfResource;
			}
		}

		System.out.println("ResourceObject neither a File nor String. Discarded.");
		return null;
	}
}

