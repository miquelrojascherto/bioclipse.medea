package net.bioclipse.medea.core;

import java.util.ArrayList;

/**
 * class which groups all fragments which have the same mass.
 * 
 * @author Miguel Rojas
 *
 */
public class FragmentTreeSub extends ArrayList<FragmentMolecule> {

	private static final long serialVersionUID = 4796607875236555456L;

	/**
	 * Adds an MSFragment
	 * 
	 * @param msfragment The FragmentMolecule object
	 */
	public void addFragment(FragmentMolecule msfragment){
		add(msfragment);
	}
}
