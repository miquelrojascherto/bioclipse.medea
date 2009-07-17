package net.bioclipse.medea.core.reaction;

import java.util.ArrayList;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.interfaces.IMapping;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.ISingleElectron;
import org.openscience.cdk.qsar.descriptors.atomic.EffectiveAtomPolarizabilityDescriptor;
import org.openscience.cdk.qsar.descriptors.atomic.PartialPiChargeDescriptor;
import org.openscience.cdk.qsar.descriptors.atomic.PartialSigmaChargeDescriptor;
import org.openscience.cdk.qsar.descriptors.atomic.SigmaElectronegativityDescriptor;
import org.openscience.cdk.qsar.result.DoubleResult;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.tools.StructureResonanceGenerator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
/**
 * Class which extract the qsar from a reactions for RadicalSiteInitiation.
 * 
 * @author Miguel Rojas
 */
public class ExtractorSetQsarsRSI implements ExtractorSetQsars{
	private PartialSigmaChargeDescriptor descriptor0;
	private SigmaElectronegativityDescriptor descriptor1;
	private PartialPiChargeDescriptor descriptor2;
	private EffectiveAtomPolarizabilityDescriptor descriptor3;
//	private ResonancePositiveChargeDescriptor descriptor4;
	/**
	 * Extractor of the ExtractorSetQsars object
	 */
	public ExtractorSetQsarsRSI(){
		descriptor0 = new PartialSigmaChargeDescriptor();
		descriptor1  = new SigmaElectronegativityDescriptor();
		descriptor2 = new PartialPiChargeDescriptor();
		descriptor3 = new EffectiveAtomPolarizabilityDescriptor();
//		descriptor4 = new ResonancePositiveChargeDescriptor();
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
		
		IMolecule product = null; /**/
		IMolecule neighbourd = null;/*molecule with a single electron*/
		
		if(reactionKp.getProducts().getAtomContainerCount() == 1){
			
			product = reactionKp.getProducts().getMolecule(0);
			neighbourd = reactionKp.getProducts().getMolecule(0);
			
		}else{
			IMolecule mol0 = reactionKp.getProducts().getMolecule(0);
			IMolecule mol1 = reactionKp.getProducts().getMolecule(1);
	
			
			for(int i = 0 ; i < mol1.getAtomCount() ; i++ ){
				if(mol1.getConnectedSingleElectronsList(mol1.getAtom(i)).size() != 0 ){
					product = mol0;
					neighbourd = mol1;
					break;
				}
			}
			if(product == null)
				for(int i = 0 ; i < mol0.getAtomCount() ; i++ ){
					if(mol0.getConnectedSingleElectronsList(mol0.getAtom(i)).size() != 0 ){
						product = mol1;
						neighbourd = mol0;
						break;
					}
				}
		}
			
		if(reactionKp.mappings() != null ){
			
//			printInformation(neighbourd);
			ArrayList<Double> results3 = applyDescritorsProductB(neighbourd,reactionKp.mappings());
			resultsQsars.addAll(results3);

//			printInformation(product);
			ArrayList<Double> results2 = applyDescritorsProductA(product,reactionKp.mappings());
			resultsQsars.addAll(results2);

//			printInformation(reactant);
			ArrayList<Double> results1 = applyDescritorsReactant(reactant, reactionKp. mappings());
			resultsQsars.addAll(results1);


		}
		return resultsQsars;
	}
	/**
	 * obtain an ArrayList of descriptors for the reactant.
	 * 
	 * @param reactant The IMolecule(reactant)
	 * @param mapping  The Iterator with mappings
	 */
	private ArrayList<Double> applyDescritorsReactant(IMolecule reactant, Iterable<IMapping> iterable) {
		ArrayList<Double> results = new ArrayList<Double>();
		Integer[] object1 = {new Integer(6)};
		int count = 0;
		IAtom atom2 = null;
		IAtom atom3 = null;
		for(IMapping mapping:iterable){
//			IMapping mapping = (IMapping)iterable.next();
			IChemObject object = mapping.getChemObject(0);/* 0 is reactant, 1 is product, mapping*/
			if(object instanceof IAtom){
				if(reactant.contains((IAtom)object)){
				
				IAtom aap = (IAtom)object;
					if(reactant.getConnectedSingleElectronsList(aap).size() > 0){/*it should be the first mapping*/
						try {
							descriptor1.setParameters(object1);
							results.add(((DoubleResult)descriptor1.calculate(aap, reactant).getValue()).doubleValue());
							results.add(new Double(reactant.getConnectedLonePairsCount(aap)));
							results.add(new Double(reactant.getConnectedAtomsCount(aap)));
						} catch (CDKException e) {
							e.printStackTrace();
						}
						
					}
				}
			}
//			else if(object instanceof IBond){
//				if(reactant.contains((IBond)object)){
//					IBond bb = (IBond)object;
//					try {
//					    DoubleArrayResult dar = ((DoubleArrayResult)descriptor4.calculate(bb, reactant).getValue());
//					    double r = (dar.get(0)+dar.get(1))/2;
//					    results.add(r);
//					} catch (CDKException e) {
//						e.printStackTrace();
//					}
//					results.add(bb.getOrder());
//				}
//			}
			count++;
		}
		
		return results;
	}


	/**
	 * obtain an ArrayList of descriptors for the product.
	 * 
	 * @param product The IMolecule(productA)
	 * @param mapping  The Iterator with mappings
	 */
	private ArrayList<Double> applyDescritorsProductA(IMolecule product, Iterable<IMapping> iterable) {
		ArrayList<Double> results = new ArrayList<Double>();
		Integer[] object1 = {new Integer(6)};
		Object[] object2 = {new Integer(6),new Boolean(false)};
		int count = 0;
		for(IMapping mapping:iterable){/* second is the atom  [A1*]-A2-A3* => A1=A2 + [A3*]*/
			
//			IMapping mapping = (IMapping)iterable.next();
			if(count == 0){
				count++;
				continue;
			}
			
			IChemObject object = mapping.getChemObject(1);/* 0 is reactant, 1 is product, mapping*/
			if(object instanceof IAtom){
				IAtom aap = (IAtom)object;
				if(product.contains(aap)){
						try {
							descriptor0.setParameters(object1);
							double result =((DoubleResult)descriptor0.calculate(aap, product).getValue()).doubleValue();
							results.add(result);
							
							descriptor2 = new PartialPiChargeDescriptor();
							descriptor2.setParameters(object2);
							result =((DoubleResult)descriptor2.calculate(aap, product).getValue()).doubleValue();
							results.add(result);

							result =((DoubleResult)descriptor3.calculate(aap, product).getValue()).doubleValue();
							results.add(result);
							int numberC = 0;
							for(IAtom a:product.atoms()){
//								IAtom a = (IAtom)iter.next();
								if(a.getSymbol().equals("C"))
									numberC++;
							}
							if(numberC > 3)
								results.add(1.0);
							else
								results.add(0.0);

							
							break;
							
						} catch (CDKException e) {
							e.printStackTrace();
						}
				}

			}
		}
		return results;
	}

	/**
	 * obtain an ArrayList of descriptors for the product.
	 * 
	 * @param product The IMolecule(productB)
	 * @param mapping  The Iterator with mappings
	 */
	private ArrayList<Double> applyDescritorsProductB(IMolecule neighbour, Iterable<IMapping> iterable) {
		ArrayList<Double> results = new ArrayList<Double>();
		Integer[] object1 = {new Integer(6)};
		Object[] object2 = {new Integer(6),new Boolean(false)};
		for(IMapping mapping:iterable){
//			IMapping mapping = (IMapping)iterable.next();
			IChemObject object = mapping.getChemObject(1);/* 0 is reactant, 1 is product, mapping*/
			if(object instanceof IAtom){
				IAtom aap = (IAtom)object;
				if(neighbour.contains(aap)){
					if(neighbour.getConnectedSingleElectronsList(aap).size() > 0){
						try {
							descriptor0.setParameters(object1);
							results.add(((DoubleResult)descriptor0.calculate(aap, neighbour).getValue()).doubleValue());
							
							descriptor2 = new PartialPiChargeDescriptor();
							descriptor2.setParameters(object2);
//							results.add(((DoubleResult)descriptor2.calculate(aap, neighbour).getValue()).doubleValue());

							results.add(((DoubleResult)descriptor3.calculate(aap, neighbour).getValue()).doubleValue());
							
							StructureResonanceGenerator gR = new StructureResonanceGenerator();/*according G. should be integrated the breaking bonding*/
							
							IAtomContainerSet iSet = gR.getContainers(neighbour);
							double 	rr = 0.0;
							if(iSet != null)
								rr = (new Double(iSet.getAtomContainerCount())).doubleValue();
							results.add(rr);
							
							break;
						} catch (CDKException e) {
							e.printStackTrace();
						}
					}
//					break;
				}
	
			}
		}
		return results;
	}

	private void printInformation(IMolecule fragmentToStudy) {
		System.out.println("print infom");
		String smiles = (new SmilesGenerator()).createSMILES(fragmentToStudy);
		IMolecularFormula formula = MolecularFormulaManipulator.getMolecularFormula(fragmentToStudy);
		
		System.out.println("SMILE: " + smiles+", count Atoms: " + fragmentToStudy.getAtomCount()+ ", count Bonds: " + fragmentToStudy.getBondCount() + ", imass: "+ MolecularFormulaManipulator.getTotalExactMass(formula));
		
//		Iterator atoms = fragmentToStudy.atoms();
		int count = 0;
		for(IAtom atom:fragmentToStudy.atoms()){
//			IAtom atom = (IAtom)atoms.next();
			ISingleElectron[] se = (ISingleElectron[])fragmentToStudy.getConnectedSingleElectronsList(atom).toArray();
			System.out.println("Atom: "
							+ count
							+ ", Sym: "
							+ atom.getSymbol()
							+ ", AtomAt: "
							+ fragmentToStudy.getConnectedAtomsCount(atom)
							+ ", lpe: "
							+ fragmentToStudy.getConnectedLonePairsCount(atom)
							+ ", sg: " + se.length
							+ ", Charge: "
							+ atom.getFormalCharge());
			count++;
		}
		
	}
}
