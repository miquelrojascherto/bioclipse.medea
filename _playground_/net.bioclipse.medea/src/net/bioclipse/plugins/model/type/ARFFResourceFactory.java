package net.bioclipse.plugins.model.type;

import net.bioclipse.model.BioResourceFactory;
import net.bioclipse.model.BioResourceType;
import net.bioclipse.model.IBioResource;
import net.bioclipse.plugins.model.ARFFResource;

/**
 * A factory that instantiates Folders for the File Viewer
 * 
 * @author     Miguel Rojas
 * @created    30. november 2006
 *
 */
public class ARFFResourceFactory extends BioResourceFactory
{
   public IBioResource newItem(
      BioResourceType type,
      Object obj) {

      return new ARFFResource(type, obj);
   }

   public IBioResource loadItem(BioResourceType type,String info) {
      return ARFFResource.loadItem(type, info);
   }
   
   public IBioResource newResource(BioResourceType type, Object resourceObject, String name) {
	      return ARFFResource.newResource(type, resourceObject, name);
	}

}
