package net.bioclipse.medea.core;

import net.bioclipse.medea.core.reaction.ExtractorSetReactions;
import net.bioclipse.reaction.domain.CDKReactionScheme;
import net.bioclipse.reaction.domain.ICDKReactionScheme;

import org.openscience.cdk.interfaces.IReactionScheme;
import org.openscience.cdk.interfaces.IReactionSet;
import org.openscience.cdk.tools.manipulator.ReactionSchemeManipulator;

/**
 * Class which creates a ChemModel that contains a reaction list from the FragmentTree.
 * 
 * @author Miguel Rojas
 */
public class CMLReactionCreator {
	/** IChemFile which contains a IReactionSet*/
	private ICDKReactionScheme cdkRSch;
	/**
	 * Constructor of the CMLReactionCreator object
	 * 
	 * @param fragmentTree The FragmentTree object
	 */
	CMLReactionCreator(FragmentTree fragmentTree){
		IReactionSet reactionSet = (new ExtractorSetReactions(fragmentTree)).extract();
		IReactionScheme reactionScheme = ReactionSchemeManipulator.createReactionScheme(reactionSet);
		cdkRSch = new CDKReactionScheme(reactionScheme);
//		IMoleculeSet moleculeSet = ReactionSetManipulator.getAllMolecules(reactionSet);
//		for(Iterator iter = moleculeSet.molecules(); iter.hasNext();){
//			HashMap coordinates = new HashMap();
//			IMolecule mol = (IMolecule)iter.next();
//			String smiles = (new SmilesGenerator()).createSMILES(mol);
//			MFAnalyser mfAnalyser = new MFAnalyser(mol);
//			System.out.println("SMILE: " + smiles+", count Atoms: " + mol.getAtomCount()+ ", count Bonds: " + mol.getBondCount() + ", imass: "+ Math.round(mfAnalyser.getMass()) );
//			if(!GeometryTools.has2DCoordinates(mol)){
//				StructureDiagramGenerator sdg = new StructureDiagramGenerator();
//				sdg.setMolecule(mol);
//				try {
//					sdg.generateCoordinates();
//					mol = sdg.getMolecule();
//					GeometryTools.translateAllPositive(mol,coordinates);
//					Iterator it2 = mol.atoms();
//					while(it2.hasNext()){
//						IAtom atom=(IAtom)it2.next();
//						atom.setPoint2d((Point2d)coordinates.get(atom));
//					}
//					
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		
//		IChemModel chemModel = reactionSet.getBuilder().newChemModel();
//		chemModel.setReactionSet(reactionSet);
//		IChemSequence seq = reactionSet.getBuilder().newChemSequence();
//		seq.addChemModel(chemModel);
//		chemFile = reactionSet.getBuilder().newChemFile();
//		chemFile.addChemSequence(seq);
		
	}
	/**
	 * get the ChemFile that contains the IReactionSet
	 *
	 * @return The ChemFile object
	 */
	public ICDKReactionScheme getReactionScheme() {
		return cdkRSch;
	}
}
