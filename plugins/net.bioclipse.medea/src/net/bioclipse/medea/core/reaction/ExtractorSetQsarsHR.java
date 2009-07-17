package net.bioclipse.medea.core.reaction;

import java.util.ArrayList;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IMapping;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.qsar.descriptors.atomic.EffectiveAtomPolarizabilityDescriptor;
import org.openscience.cdk.qsar.descriptors.atomic.PartialPiChargeDescriptor;
import org.openscience.cdk.qsar.descriptors.atomic.PartialSigmaChargeDescriptor;
import org.openscience.cdk.qsar.result.DoubleResult;
/**
 * Class which extract the qsar from a reactions for CarbonylElimination.
 * 
 * @author Miguel Rojas
 */
public class ExtractorSetQsarsHR implements ExtractorSetQsars{
	private PartialSigmaChargeDescriptor descriptor0;
	private PartialPiChargeDescriptor descriptor2;
	private EffectiveAtomPolarizabilityDescriptor descriptor3;
	/**
	 * Extractor of the ExtractorSetQsars object
	 */
	public ExtractorSetQsarsHR(){
		descriptor0 = new PartialSigmaChargeDescriptor();
		descriptor2 = new PartialPiChargeDescriptor();
		descriptor3 = new EffectiveAtomPolarizabilityDescriptor();
	}
	/**
	 * get an ArrayList with all descriptors for a this Reaction
	 * 
	 * @param reactionKp The ReactionKp object
	 * @return An ArrayList with all descriptors results.
	 */
	public ArrayList<Double> getQsars(ReactionKp reactionKp){
		ArrayList<Double> resultsQsars = new ArrayList<Double>();
		
		
		IMolecule reactant = reactionKp.getReactants().getMolecule(0);
		IMolecule product = reactionKp.getProducts().getMolecule(0);
		
			
		if(reactionKp.mappings() != null ){
			
//			System.out.println("A");
//			printInformation(product);

			ArrayList<Double> results1 = applyDescritorsProductA(reactant,reactionKp.mappings());
			resultsQsars.addAll(results1);
			
			ArrayList<Double> results2 = applyDescritorsProductA(product,reactionKp.mappings());
			resultsQsars.addAll(results2);

		}

		return resultsQsars;
	}

	/**
	 * obtain an ArrayList of descriptors for the product.
	 * 
	 * @param product The IMolecule(productA)
	 * @param mapping  The Iterator with mappings
	 */
	private ArrayList<Double> applyDescritorsProductA(IMolecule molecule, Iterable<IMapping> iterable) {
		ArrayList<Double> results = new ArrayList<Double>();
		Integer[] object1 = {new Integer(6)};
		Object[] object2 = {new Integer(6),new Boolean(false)};
		int count = 0;
		/*problems with mapping, not valid if a product come from two different reactants.*/
//		while(mappingI.hasNext()){/* second is the atom  [A1*]-A2-A3* => A1=A2 + [A3*]*/
		for(IAtom aap:molecule.atoms()){
//			IMapping mapping = (IMapping)mappingI.next();
			
//			if(count == 2){
//				IChemObject object = mapping.getChemObject(1);/* 0 is reactant, 1 is product, mapping*/
//				if(object instanceof IAtom){
//					IAtom aap = (IAtom)object;
//			IAtom aap = (IAtom)iterator.next();
			if(molecule.getConnectedSingleElectronsCount(aap) != 0){
//					System.out.println("i_: "+product.getAtomNumber(aap)+", "+aap.getID()+", "+aap);
							try {
								descriptor0.setParameters(object1);
								double result =((DoubleResult)descriptor0.calculate(aap, molecule).getValue()).doubleValue();
//								System.out.println("r0: "+result);
								results.add(result);
								
								descriptor2 = new PartialPiChargeDescriptor();
								descriptor2.setParameters(object2);
								result =((DoubleResult)descriptor2.calculate(aap, molecule).getValue()).doubleValue();
//								System.out.println("r1: "+result);
								results.add(result);
	
								result =((DoubleResult)descriptor3.calculate(aap, molecule).getValue()).doubleValue();
//								System.out.println("r2: "+result);
								results.add(result);
	
							} catch (CDKException e) {
								e.printStackTrace();
							}
							break;
	
				}
			}
//			count++;
//		}
		return results;
	}

//	private void printInformation(IMolecule fragmentToStudy) {
//		System.out.println("print infom");
//		String smiles = (new SmilesGenerator(fragmentToStudy.getBuilder())).createSMILES(fragmentToStudy);
//		MFAnalyser mfAnalyser = new MFAnalyser(fragmentToStudy);
//		System.out.println("SMILE: " + smiles+", count Atoms: " + fragmentToStudy.getAtomCount()+ ", count Bonds: " + fragmentToStudy.getBondCount() + ", imass: "+ Math.round(mfAnalyser.getMass()) );
//		
//		Iterator atoms = fragmentToStudy.atoms();
//		int count = 0;
//		while(atoms.hasNext()){
//			IAtom atom = (IAtom)atoms.next();
//			ISingleElectron[] se = fragmentToStudy.getSingleElectron(atom);
//			System.out.println("Atom: "
//							+ count
//							+ ", Sym: "
//							+ atom.getSymbol()
//							+ ", AtomAt: "
//							+ fragmentToStudy.getConnectedAtomsCount(atom)
//							+ ", lpe: "
//							+ fragmentToStudy.getLonePairCount(atom)
//							+ ", sg: " + se.length
//							+ ", Charge: "
//							+ atom.getFormalCharge());
//			count++;
//		}
//		
//	}
}
