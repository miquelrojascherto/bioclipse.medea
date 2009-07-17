package net.bioclipse.medea.core.reaction;

import java.util.ArrayList;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.interfaces.IReactionSet;

/**
 * class which extract all possible reaction of a ionization and obtains their probabilities.
 * 
 * @author Miguel Rojas
 *
 */
public class IPReactionsExtraction {
	
	/** SetOfMolecules that contains all ionized molecules*/
	private IMoleculeSet setOfMolecules;
	/** List that contains the energy for each ionized molecules
	 * the size will be the same like molList*/
	private ArrayList<Double> ipList = new ArrayList<Double>();
	/** List that contains the probabilities for each ionized molecules
	 * the size will be the same like molList*/
	private ArrayList<Double> eList = new ArrayList<Double>();
	/**
	 * Constructor of the EnergyIP object
	 * 
	 * @param molecule The IMolecule to ionizate
	 */
//	public IPReactionsExtraction(IMolecule molecule){
//		setOfMolecules = molecule.getBuilder().newMoleculeSet();
//		
//		/* obtain the ionization fragments*/
//		IReactionSet setOfReactions1 = null;
//		IReactionSet setOfReactions2 = null;
//		try {
//			IReactionProcess reactionType = new ElectronImpactPDBReaction();
//			Object[] params = {Boolean.FALSE};
//			reactionType.setParameters(params);
//			molecule = cleanReactiveCenters(molecule);
//			IMoleculeSet reactants1 = molecule.getBuilder().newMoleculeSet();
//			IMolecule moleculeCloned;
//			try {
//				moleculeCloned = (IMolecule) molecule.clone();
//			} catch (CloneNotSupportedException e) {
//				throw new CDKException("Could not clone IMolecule!", e);
//			}
//			reactants1.addAtomContainer(moleculeCloned);
//			setOfReactions1 = reactionType.initiate(reactants1, null);
//			
//			reactionType = new ElectronImpactNBEReaction();
//			reactionType.setParameters(params);
//			molecule = cleanReactiveCenters(molecule);
//			try {
//				moleculeCloned = (IMolecule) molecule.clone();
//			} catch (CloneNotSupportedException e) {
//				throw new CDKException("Could not clone IMolecule!", e);
//			}
//			IMoleculeSet reactants2 = molecule.getBuilder().newMoleculeSet();
//			reactants2.addAtomContainer(moleculeCloned);
//			setOfReactions2 = reactionType.initiate(reactants2, null);
//		} catch (CDKException e) {
//			e.printStackTrace();
//		}
//		
//		/* obtain the ionization potential energy*/
//		IPAtomicDescriptor descriptor;
//		Object[] params = new Object[2];
//
//		System.out.println("n1: "+setOfReactions1.getReactionCount());
//		if(setOfReactions1 != null)
//			for(int i = 0; i < setOfReactions1.getReactionCount() ; i++){
//				IAtomContainer reactant = setOfReactions1.getReaction(i).getReactants().getAtomContainer(0);
//				IMoleculeSet products = setOfReactions1.getReaction(i).getProducts();
//				IChemObject object = setOfReactions1.getReaction(i).getMapping(0).getChemObject(0);
//				if(object instanceof IBond){
//				IBond bond =  (IBond) object;
//				for(int k = 0; k < products.getAtomContainerCount() ; k++){
//					
//					IAtomContainer ac = products.getAtomContainer(k);
////					System.out.println("smilesR: "+(new SmilesGenerator(reactant.getBuilder())).createSMILES((IMolecule)reactant));
//					
////					for(int j = 0; j < ac.getBondCount() ; j++){
////						if(bond.getFlag(CDKConstants.REACTIVE_CENTER)){
//							try {
//								params[0] = new Integer(reactant.getBondNumber(bond));
//								params[1] = IPAtomicDescriptor.BondTarget;
//								descriptor = new IPAtomicDescriptor();
//								params[1] = IPAtomicDescriptor.BondTarget;
//						        descriptor.setParameters(params);
//						        double result= ((DoubleResult)descriptor.calculate(reactant).getValue()).doubleValue();
//						        /*if(result != )*/{
//									setOfMolecules.addAtomContainer(ac);
//									ipList.add(new Double(result));
//									/* TODO set probability into reaction.setProperty*/
//								}
//							} catch (CDKException e) {
//								e.printStackTrace();
//							}
//							break;
//								
////						}
////					}
//				}
//				}
//			}
//		System.out.println("n2: "+setOfReactions2.getReactionCount());
//		if(setOfReactions2 != null)
//			for(int i = 0; i < setOfReactions2.getReactionCount() ; i++){
//				IAtomContainer reactant = setOfReactions2.getReaction(i).getReactants().getAtomContainer(0);
//				IMoleculeSet products = setOfReactions2.getReaction(i).getProducts();
//				for(int k = 0; k < products.getAtomContainerCount() ; k++){
//					IAtomContainer ac = products.getAtomContainer(k);
//					for(int j = 0; j < ac.getAtomCount() ; j++){
//						IAtom atom = ac.getAtom(j);
//						if(atom.getFlag(CDKConstants.REACTIVE_CENTER)){
//							params[0] = new Integer(ac.getAtomNumber(atom));
//							params[1] = IPAtomicDescriptor.AtomicTarget;
//							try {
//								descriptor = new IPAtomicDescriptor();
//								descriptor.setParameters(params);
//						        double result= ((DoubleResult)descriptor.calculate(reactant).getValue()).doubleValue();
//								/*if(result != )*/{
//									setOfMolecules.addAtomContainer(ac);
//									ipList.add(new Double(result));
//									/* TODO set probability into reaction.setProperty*/
//								}
//							} catch (CDKException e) {
//								e.printStackTrace();
//							}
//							break;
//								
//						}
//					}
//				}
//			}
//		
//		if(ipList.size() > 0)
//			calculateRelativeProbabilites();
//		
//
//	}
	/**
	 * calculate and set the relative probabilities from the energy
	 */
	public static void setCalculateRelativeProbabilites(IReactionSet reactionSet) {
		double sumT = 0.0;
//		Iterator iterator = reactionSet.reactions();
		for(IReaction reaction:reactionSet.reactions()){
//			IReaction reaction = (IReaction) iterator.next();
			sumT += ((Double) reaction.getProperty("IonizationEnergy")).doubleValue();
		}
//		for(int i = 0; i < ipList.size() ; i++)
//			sumT += ipList.get(i).doubleValue();

		for(IReaction reaction:reactionSet.reactions()){
//			IReaction reaction = (IReaction) iterator.next();
			double result = ((Double) reaction.getProperty("IonizationEnergy")).doubleValue();
			result = result/sumT;
//			System.out.println("result: "+result);
			reaction.setProperty("IonizationEnergy", result);
		}
//		for(int i = 0; i < ipList.size() ; i++){
//			eList.add(ipList.get(i).doubleValue()/sumT*100);
//		}
	}
	/**
	 * calculate the relative probabilities from the energy
	 */
	private void calculateRelativeProbabilites() {
		double sumT = 0.0;
		for(int i = 0; i < ipList.size() ; i++)
			sumT += ipList.get(i).doubleValue();
		for(int i = 0; i < ipList.size() ; i++){
			eList.add(ipList.get(i).doubleValue()/sumT*100);
		}
	}

	/**
	 * clean atomContainer from reactive centers flags
	 * 
	 * @param molecule  The IMolecule to clean from flags
	 * @return          Cleaned molecule
	 */
	private IMolecule cleanReactiveCenters(IMolecule ac) {
//		IBond[] bonds = ac.getBonds();
//		Iterator<IBond> bonds = ac.bonds();
		for(IBond bond:ac.bonds())
			bond.setFlag(CDKConstants.REACTIVE_CENTER,false);
		
//		Iterator atomsI = ac.atoms();
		for(IAtom atom:ac.atoms()){
//			IAtom atom = (IAtom) atomsI.next();
			atom.setFlag(CDKConstants.REACTIVE_CENTER,false);
		}
		return ac;
	}
	/**
	 * get an Iterator which contains all ionized molecules
	 * 
	 * @return The an Iterator with all molecules
	 * @see probabilities
	 */
	public Iterable<IAtomContainer> ionizedMolecules(){
		return setOfMolecules.molecules();
	}
	/**
	 * get a ArrayList which contains the contains the probabilities for each ionized molecules
	 * 
	 * @return The probability values
	 * @see ionizedMolecules
	 */
	public ArrayList<Double> probabilities(){
		return eList;
	}

}
