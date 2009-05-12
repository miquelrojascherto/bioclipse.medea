package net.bioclipse.reaction.model;

import java.util.ArrayList;
import java.util.List;

import net.bioclipse.reaction.editor.ReactMolDrawingComposite;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
/**
 * 
 * @author Miguel Rojas
 */
public class AbstractObjectModel extends AbstractModel  {
	public static final String P_CONSTRAINT = "_constraint";
	public static final String P_TEXT = "_text";
	
	private String text;
	private Rectangle constraint;
	private ReactMolDrawingComposite jcp;
	
	public static final String P_SOURCE_CONNECTION = "_source_connection";
	public static final String P_TARGET_CONNECTION = "_target_connection";
	
	private List sourceConnections = new ArrayList();
	private List targetConnections = new ArrayList();
	/**
	 * Constructor of the AbstractObjectModel object
	 * 
	 * @param composite The Composite JCP
	 */
	AbstractObjectModel(){
		super();
	}
	/**
	 * get the JCP composite
	 * 
	 * @return the ReactMolDrawingComposite object
	 */
	public ReactMolDrawingComposite getJCP(){
		return jcp;
	}
	/**
	 * add the ReactMolDrawingComposite object
	 * 
	 * @param jcp the ReactMolDrawingComposite
	 */
	public void addJCP(ReactMolDrawingComposite jcp){
		this.jcp = jcp;
	}
	/**
	 * get the the of the AbstractObjectModel
	 * @return The text
	 */
	public String getText(){
		return text;
	}
	/**
	 * set the text of the AbstractObjectModel 
	 * @param text The Text
	 */
	public void setText(String text){
		this.text = text;
		firePropertyChange(P_TEXT,null,text);
	}
	/**
	 * move the AbstractObjectModel
	 * 
	 * @return The Rectangle
	 */
	public Rectangle getConstraint(){
		return constraint;
	}
	/**
	 * set the AbstractObjectModel
	 * @param rect The Rectangle
	 */
	public void setConstraint(Rectangle rect){
		constraint = rect;
		firePropertyChange(P_CONSTRAINT ,null,constraint);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.bioclipse.plugins.bc_reaction.model.AbstractModel#getPropertyDescriptors()
	 */
	public IPropertyDescriptor[] getPropertyDescriptors(){
		IPropertyDescriptor[] descriptors = new IPropertyDescriptor[]{
				new TextPropertyDescriptor(P_TEXT,"text")};
		
		return descriptors;
	}
	/*
	 * (non-Javadoc)
	 * @see net.bioclipse.plugins.bc_reaction.model.AbstractModel#getPropertyValue(java.lang.Object)
	 */
	public Object getPropertyValue(Object id){
		if(id.equals(P_TEXT)){
			return text;
		}
		return null;
	}
	/*
	 * (non-Javadoc)
	 * @see net.bioclipse.plugins.bc_reaction.model.AbstractModel#isPropertySet(java.lang.Object)
	 */
	public boolean isPropertySet(Object id){
		if(id.equals(P_TEXT)){
			return true;
		}
		else
			return false;
	}
	/*
	 * (non-Javadoc)
	 * @see net.bioclipse.plugins.bc_reaction.model.AbstractModel#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value){
		if(id.equals(P_TEXT)){
			setText((String)value);
		}
	}
	/**
	 * add the Source connection
	 * @param connx The Object
	 */
	@SuppressWarnings("unchecked")
	public void addSourceConnection(Object connx){
		sourceConnections.add(connx);
		firePropertyChange(P_SOURCE_CONNECTION,null,null);
	}
	/**
	 * add the Target connection
	 * @param connx The OBject
	 */
	@SuppressWarnings("unchecked")
	public void addTargetConnection(Object connx){
		targetConnections.add(connx);
		firePropertyChange(P_TARGET_CONNECTION,null,null);
	}
	
	public List getModelSourceConnections(){
		return sourceConnections;
	}
	
	public List getModelTargetConnections(){
		return targetConnections;
	}
	/**
	 * remove the Source connection
	 * 
	 * @param connx The Object to remove
	 */
	public void removeSourceConnection(Object connx){
		sourceConnections.remove(connx);
		firePropertyChange(P_SOURCE_CONNECTION,null,null);
	}
	/**
	 * remove the Target connection
	 * 
	 * @param connx The Object to remove
	 */
	public void removeTargetConnection(Object connx){
		targetConnections.remove(connx);
		firePropertyChange(P_TARGET_CONNECTION,null,null);
	}
}