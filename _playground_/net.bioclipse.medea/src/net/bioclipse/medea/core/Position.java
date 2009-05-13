package net.bioclipse.medea.core;

import java.awt.Dimension;

/**
 * This class is an extension of Dimension. It gives sense
 * for difine the position which occupys a fragment into
 * the FragmentTree: \n
 * The with means mass. \n
 * the hight means position.
 */
public class Position extends Dimension {
	
	private static final long serialVersionUID = -5652121983277951434L;

	/**
	 * Constructor of the Position object
	 * 
	 * @param mass   The Mass
	 * @param pos    The Position
	 */
	public Position(int mass,int pos){
		super(mass,pos);
	}
	/**
	 * set the position Mass. Makes reference the position into the ArrayList-FragmentTree.
	 */
	public void setWidth(int mass){
		setWidth(mass);
	}
	/**
	 * set the position Position. Make reference to the position into the ArrayList-FragmentTreeSub
	 */
	public void setHeight(int pos){
		setHeight(pos);
	}
	/**
	 * get the position Mass. Makes reference the position into the ArrayList-FragmentTree.
	 */
	public double getWidth(){
		return super.getWidth();
	}
	/**
	 * get the position Position. Make reference to the position into the ArrayList-FragmentTreeSub
	 */
	public double getHeight(){
		return super.getHeight();
	}
}
