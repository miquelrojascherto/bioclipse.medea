package net.bioclipse.plugins.bc_reaction.model;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author Miguel Rojas
 */
public class ContentsModel extends AbstractModel {
	public static final String P_CHILDREN ="_children";
	private List<Object> children = new ArrayList<Object>();
	/**
	 * add a children object
	 * @param child The object
	 */
	public void addChild(Object child){
		children.add(child);
		firePropertyChange(P_CHILDREN,null,null);
	}
	/**
	 * get a List of its children objects
	 * @return A List with the Children objects
	 */
	public List getChildren(){
		return children;
	}
	/**
	 * remove the children object
	 * @param child  The children object to remove
	 */
	public void removeChild(Object child){
		children.remove(child);
		firePropertyChange(P_CHILDREN,null,null);
	}
	
}
