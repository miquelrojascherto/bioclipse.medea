package net.bioclipse.plugins.medea.core;

import java.util.ArrayList;
import java.util.Iterator;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.isomorphism.UniversalIsomorphismTester;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainer;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainerCreator;
import org.openscience.cdk.tools.MFAnalyser;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

/**
 * class which make a controll of the fragments obtained: 
 * 
 * @author Miguel Rojas
 */
public class FragmentController {
	/**
	 * class which makes a controll of the fragments obtained.\n
	 * - When it is a learning process if the mass exists in the spectrum. \n
	 * 
	 * @param molecule  The IMolecule 
	 * @param peaksX    An ArrayList with all X peaks
	 * 
	 * @return True, if pass the requisites.
	 */
	public static boolean isExistingMass(IMolecule molecule, ArrayList<Double> peaksX){
		if(peaksX != null){
			int mass = Math.round((new MFAnalyser(molecule)).getMass());
			if(peaksX.contains(new Double(mass))){
				return true;
			}
		}
		
		return false;
	}
	/**
	 * class which makes a controll of the fragments obtained.\n
	 * - doesn't have formalCharge == 1 (not visible for the mass spectrometer). \n
	 * - doesn't have total formalCharge != 0 (not visible for the mass spectrometer). \n
	 * 
	 * @param molecule  The IMolecule 
	 * 
	 * @return True, if pass the requisites.
	 */
	public static boolean isAccept(IMolecule molecule) {
		
		if(AtomContainerManipulator.getTotalPositiveFormalCharge(molecule) != 1)
			return false;
		if(AtomContainerManipulator.getTotalFormalCharge(molecule) == 0)
			return false;
		
		
		return true;
	}
	
	/**
	 * class which makes a controll of the fragments obtained. It shows if the fragment was already
	 * obtained before. It makes a is isomorphism search.
	 * 
	 * @param fragTree  The FragmentTree object.
	 * @param molecule  The IMolecule to study.
	 * @return fragmentMolecule, the fragment wich is isomorph.
	 */
	public static FragmentMolecule exists(FragmentTree fragTree, IMolecule molecule){

		int mass = Math.round((new MFAnalyser(molecule)).getMass());
		FragmentTreeSub listFragments = fragTree.getFragments(mass);
		QueryAtomContainer qAC = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(molecule);
		int numAtoms = molecule.getAtomCount();
		Iterator iterator = listFragments.iterator();
		while(iterator.hasNext()){
			FragmentMolecule fragment = (FragmentMolecule)iterator.next();
			
			/*Compare if they have the same number of Atoms*/
			if(numAtoms == fragment.getAtomCount()){
				/*Compare if they are isomorphism*/
				try {
					if(UniversalIsomorphismTester.isIsomorph(fragment,qAC)){
						return fragment;
					}
				} catch (CDKException e1) {
					System.err.println(e1);
				}
			}
		}
		return null;
	}
	/**
	 * controll if is the predecessor. For rearrangements.
	 * 
	 * @param fragTree The FramgentTree
	 * @param molecule IMolecule to study
	 * @return  True, if the molecule is the same from its predecessor
	 */
	public static boolean isPredecessor(FragmentTree fragTree, IMolecule molecule, FragmentMolecule precedessor) {
		ArrayList<Position> childrenPosition = precedessor.getChildren();
		QueryAtomContainer qAC = QueryAtomContainerCreator.createSymbolAndChargeQueryContainer(molecule);
		for(int i = 0; i < childrenPosition.size(); i++){
			try {
				if(UniversalIsomorphismTester.isIsomorph(fragTree.getFragment(childrenPosition.get(i)),qAC)){
					return true;
				}
			} catch (CDKException e1) {
				System.err.println(e1);
			}
		}
		return false;
	}
}
