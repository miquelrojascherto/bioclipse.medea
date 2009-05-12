package net.bioclipse.plugins.medea.core;

import java.util.ArrayList;

import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

/**
 * Class which contains all information of the fragmention process and groups.
 * Each fragment obtained will be saved into a sub ArrayList depending of its
 * mass.
 * 
 * @author Miguel Rojas
 */
public class FragmentTree extends ArrayList<FragmentTreeSub> {
	
	private static final long serialVersionUID = -1756390877174302389L;
	
	/** molecule to study for the process of fragmentation */
	private IMolecule molecule;

	/**
	 * Constructor of the FragmentTree object
	 * @param molecule An IMolecule to study
	 */
	FragmentTree(IMolecule molecule){
		this.molecule = molecule;
		
		/* the Sub-ArrayList is limited for mass */
		int mass = getMass(molecule);
		for (int i = 0; i < mass+10; i++){
			FragmentTreeSub fts = new FragmentTreeSub();
			add(fts);
		}
		/* it is also added the molecule ifself*/
		addFragment(new FragmentMolecule(molecule,
				new Position(mass,0),
				null,
				new Position(mass, -1),
				0));
	}
	/**
	 * Adds a new FragmentMolecule to the FragmentTree.
	 * 
	 * @param msfragment The FragmentMolecule object
	 */
	public void addFragment(FragmentMolecule msfragment){
		int mass = getMass(msfragment);
		FragmentTreeSub fts = get(mass);
		fts.addFragment(msfragment);
	}
	/**
	 * get the fragments for a determined mass
	 * 
	 * @param mass The mass which belongs the FragmentTreeSub
	 * @return The FragmentTreeSub
	 */
	public FragmentTreeSub getFragments(int mass){
		return get(mass);
	}
	/**
	 * get the fragment of a specific mass and position
	 * 
	 * @param id Position of the fragment
	 * @return   The FragmentMolecule
	 */
	public FragmentMolecule getFragment(Position id){
		return get((int)id.getWidth()).get((int)id.getHeight());
	}
	/**
	 * get the molecular mass of a specific molecule or fragment
	 * 
	 * @param mol  IMolecule or fragment to value
	 * @return     The Mass value
	 */
	private int getMass(IMolecule mol){	
		IMolecularFormula formula = MolecularFormulaManipulator.getMolecularFormula(mol);
		return (int)MolecularFormulaManipulator.getTotalExactMass(formula);
	}
	/**
	 * get the molecule to predict 
	 * 
	 * @return The IMolecule
	 */
	public IMolecule getMolecule(){
		return molecule;
	}
}
