package net.bioclipse.plugins.medea.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import net.bioclipse.plugins.medea.core.learning.ExtractorProbability;
import net.bioclipse.plugins.medea.core.reaction.IPReactionsExtraction;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.interfaces.IReactionSet;
import org.openscience.cdk.qsar.descriptors.molecular.IPMolecularDescriptor;
import org.openscience.cdk.reaction.type.CarbonylEliminationReaction;
import org.openscience.cdk.reaction.type.HydrogenRearrangementDeltaReaction;
import org.openscience.cdk.reaction.type.HydrogenRearrangementGammaReaction;
import org.openscience.cdk.reaction.type.RadicalSiteInitiationHReaction;
import org.openscience.cdk.reaction.type.RadicalSiteInitiationReaction;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.tools.HydrogenAdder;
import org.openscience.cdk.tools.LonePairElectronChecker;
import org.openscience.cdk.tools.MFAnalyser;

/**
 * class which fragments the molecule and obtains fragments which are
 * added into the tree-fragments.
 * 
 * @author Miguel Rojas
 */
public class Fragmenter {
	
	FragmentTree fragTree;
	
	boolean printInfo = false;
	
	/** ArrayList which will be added each new obtained fragment. It is the responsible
	 * to manage the order of the fragmentation*/
	ArrayList<FragmentMolecule> setOfFragments = new ArrayList<FragmentMolecule>();
	/**count which say how many fragments are obtained in this moment*/
	private int numberOfFragments = 0;
	
	/** FragmentMolecule which in this moment is being fragmented*/
	private FragmentMolecule fragmentToStudy;

	private ExtractorProbability extractorP;

	public static int processType = 0;
	
	
	/**
	 * constructor of FragmentController object
	 * 
	 * @param process  The Process, Medea.PREDICTPROCESS or Medea.LEARNINGPROCESS
	 * @param molecule  IMolecule to fragment
	 * @param peaksX The peaks of the experimental  spectrum. Used if there
	 * is a learning process.
	 * 
	 * @throws CDKException 
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	public Fragmenter(int process, IMolecule molecule, ArrayList<Double> peaksX) throws CDKException, IOException, ClassNotFoundException{
		processType  = process;
		/*Only used for learning process*/
		if(process == Medea.PREDICTPROCESS || process == Medea.LEARN_PREDPROCESS)
			if(extractorP == null)
				extractorP = new ExtractorProbability();
		
		/* make a check about hydrogens and Pair Electrons */
		try {
			HydrogenAdder hAdder = new HydrogenAdder();
		    hAdder.addExplicitHydrogensToSatisfyValency(molecule);
		    
			LonePairElectronChecker lpChecker = new LonePairElectronChecker();
			lpChecker.newSaturate(molecule);
			
//			printInformation(molecule);/**********************************************************/
			
			/*elimination of coordinates*/
			for(Iterator it =molecule.atoms() ; it.hasNext();)
				((IAtom)it.next()).setPoint2d(null);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (CDKException e) {
			e.printStackTrace();
		}
		
		/* iniziate the FragmentTree*/
		fragTree = new FragmentTree(molecule);
		
		/* making an ionization of the molecule to study*/
		IPMolecularDescriptor ipDescriptor = new IPMolecularDescriptor();
		ipDescriptor.calculate(molecule);
		IReactionSet reactionSetIP = ipDescriptor.getReactionSet();
		Iterator iterator = reactionSetIP.reactions();
		IPReactionsExtraction.setCalculateRelativeProbabilites(reactionSetIP);
		
		/* get mass of the molecule. Important to determine the Position*/
		int massMolecule = Math.round((new MFAnalyser(molecule)).getMass());
		
		int count = 0;
		while(iterator.hasNext()){
			IReaction reaction = (IReaction) iterator.next();
			setOfFragments.add(addNewFragment(reaction.getProducts().getMolecule(0),
					"Ionitzation", 
					new Position(massMolecule,0),
					((Double) reaction.getProperty("IonizationEnergy")).doubleValue(),
					null,
					null));
			count++;
		}
		
		/* process of fragmention from the obtained ionized molecules. Into of 
		 * setOfFragment will be added all new fragment obtained */
		for(int i = 0; i < setOfFragments.size() ; i++){
			numberOfFragments = i;
			String typeOfFragmentation = null;
			fragmentToStudy = setOfFragments.get(i);

			if(printInfo){
			System.out.println("*********************NEW:  "+i+"  ******************************************");
			printInformation(fragmentToStudy);/**********************************************************/
			}
				
			/*apply all reactions*/
			for (int j = 0; j < 5; j++){
				IMoleculeSet fragments = null;
				IReactionSet setOfReactions = null;
				if(j == 0){/*1: radicalSiteInitiation*/
					typeOfFragmentation = "RadicalSiteInitiation";
					
					
					if(printInfo)
					System.out.println("--"+typeOfFragmentation+"--");
					
					try {
						
						IMoleculeSet setOfReactants = molecule.getBuilder().newMoleculeSet();
						cleanFlagReactiveCenter((IMolecule)fragmentToStudy);
						setOfReactants.addMolecule((IMolecule)fragmentToStudy);
				        Object[] params = {Boolean.FALSE};
				        RadicalSiteInitiationReaction type  = new RadicalSiteInitiationReaction();
				        type.setParameters(params);
				        setOfReactions = type.initiate(setOfReactants, null);
				        
				        if(printInfo)
				        System.out.println("numberOfReactions: "+setOfReactions.getReactionCount());
						
				        if(setOfReactions.getReactionCount() == 0)
							continue;
				        
					} catch (CDKException e) {
						e.printStackTrace();
						System.err.println("error in a fragmentation. ReactionType:"+typeOfFragmentation);
						continue;
					}
			        
				}
				if(j == 1){/*1: RadicalSiteInitiationH*/
					typeOfFragmentation = "RadicalSiteInitiationH";
					
					if(printInfo)
					System.out.println("--"+typeOfFragmentation+"--");
					
					try{
					
						IMoleculeSet setOfReactants = molecule.getBuilder().newMoleculeSet();
						cleanFlagReactiveCenter((IMolecule)fragmentToStudy);
						setOfReactants.addMolecule((IMolecule)fragmentToStudy);
				        Object[] params = {Boolean.FALSE};
				        RadicalSiteInitiationHReaction type  = new RadicalSiteInitiationHReaction();
				        type.setParameters(params);
				        setOfReactions = type.initiate(setOfReactants, null);
				        
				        if(printInfo)
				        System.out.println("numberOfReactions: "+setOfReactions.getReactionCount());
						
				        if(setOfReactions.getReactionCount() == 0)
							continue;
				        
					} catch (CDKException e) {
						e.printStackTrace();
						System.err.println("error in a fragmentation. ReactionType:"+typeOfFragmentation);
						continue;
					}
				}
				if(j == 2){/*1: carbonylElemination*/
					typeOfFragmentation = "CarbonylElimination";
					
					if(printInfo)
					System.out.println("--"+typeOfFragmentation+"--");
					
					try{
						
						IMoleculeSet setOfReactants = molecule.getBuilder().newMoleculeSet();
						cleanFlagReactiveCenter((IMolecule)fragmentToStudy);
						setOfReactants.addMolecule((IMolecule)fragmentToStudy);
				        Object[] params = {Boolean.FALSE};
				        CarbonylEliminationReaction type  = new CarbonylEliminationReaction();
				        type.setParameters(params);
				        setOfReactions = type.initiate(setOfReactants, null);
				        
				        if(printInfo)
				        System.out.println("numberOfReactions: "+setOfReactions.getReactionCount());
						
				        if(setOfReactions.getReactionCount() == 0)
							continue;
			        
					} catch (CDKException e) {
						e.printStackTrace();
						System.err.println("error in a fragmentation. ReactionType:"+typeOfFragmentation);
						continue;
					}
				}
				if(j == 3){/*1: HydrogenRearrangementDelta*/
					typeOfFragmentation = "HydrogenRearrangementDelta";
					
					if(printInfo)
					System.out.println("--"+typeOfFragmentation+"--");
					
					try{
						
						IMoleculeSet setOfReactants = molecule.getBuilder().newMoleculeSet();
						cleanFlagReactiveCenter((IMolecule)fragmentToStudy);
						setOfReactants.addMolecule((IMolecule)fragmentToStudy);
				        Object[] params = {Boolean.FALSE};
				        HydrogenRearrangementDeltaReaction type  = new HydrogenRearrangementDeltaReaction();
				        type.setParameters(params);
				        setOfReactions = type.initiate(setOfReactants, null);
				        
				        if(printInfo)
				        System.out.println("numberOfReactions: "+setOfReactions.getReactionCount());
						
				        if(setOfReactions.getReactionCount() == 0)
							continue;
			        
					} catch (CDKException e) {
						e.printStackTrace();
						System.err.println("error in a fragmentation. ReactionType:"+typeOfFragmentation);
						continue;
					}
				}
				if(j == 4){/*1: HydrogenRearrangementDelta*/
					typeOfFragmentation = "HydrogenRearrangementGamma";
					
					if(printInfo)
					System.out.println("--"+typeOfFragmentation+"--");
					
					try{
						
						IMoleculeSet setOfReactants = molecule.getBuilder().newMoleculeSet();
						cleanFlagReactiveCenter((IMolecule)fragmentToStudy);
						setOfReactants.addMolecule((IMolecule)fragmentToStudy);
				        Object[] params = {Boolean.FALSE};
				        HydrogenRearrangementGammaReaction type  = new HydrogenRearrangementGammaReaction();
				        type.setParameters(params);
				        setOfReactions = type.initiate(setOfReactants, null);
				        
				        if(printInfo)
				        System.out.println("numberOfReactions: "+setOfReactions.getReactionCount());
						
				        if(setOfReactions.getReactionCount() == 0)
							continue;
			        
					} catch (CDKException e) {
						e.printStackTrace();
						System.err.println("error in a fragmentation. ReactionType:"+typeOfFragmentation);
						continue;
					}
				}
				
				
				if(setOfReactions.getReactionCount() > 0){
					Iterator sorI = setOfReactions.reactions();
					int counta = 0;
					while(sorI.hasNext()){
						IReaction rr = (IReaction)sorI.next();
						
						if(printInfo)
						System.out.println(counta+" :-------------------------------------------");
						
						fragments = rr.getProducts();
						for (int k = 0; k < fragments.getAtomContainerCount(); k++){
//							System.out.println("   :-------------------------------------------");
							
							int molk = 0;/*I need to add the other fragment*/
							if(k == 0)
								molk = 1;
							else
								molk = 0;
							
							if(printInfo)
							printInformation(fragments.getMolecule(k));/**********************************************************/
							
							if(FragmentController.isAccept(fragments.getMolecule(k))){
								
								if(printInfo)
								System.out.println("is Accepted");
								
								FragmentMolecule fragmentE =FragmentController.exists(fragTree, fragments.getMolecule(k));
								if(fragmentE == null){
									
									if(printInfo)
									System.out.println("no exists");
									
									/*prove probability*/
									double prob = -1;
									
									if(process == Medea.PREDICTPROCESS || process == Medea.LEARN_PREDPROCESS){
										prob = extractorP.getProbability(fragmentToStudy, j, fragments.getMolecule(k), fragments.getMolecule(molk),setOfReactions.getReaction(counta).mappings());
										if(printInfo)
											System.out.println("proba: "+prob);
										if(prob < 0.05)
											continue;
									}
									FragmentMolecule fm = addNewFragment(fragments.getMolecule(k),
											typeOfFragmentation,
											fragmentToStudy.getIdP(),
											prob,
											setOfReactions.getReaction(counta).mappings(),
											fragments.getMolecule(molk));

									boolean isExistingPeak = true;
									if(process == Medea.LEARNINGPROCESS)
										isExistingPeak = FragmentController.isExistingMass(fragments.getMolecule(k), peaksX);

									if(isExistingPeak){
										
										if(printInfo)
										System.out.println("isExistingPeak");
										
										setOfFragments.add(fm);
									}else
										if(printInfo)
											System.out.println("notisExistingPeak");
										
								}else{
									if(typeOfFragmentation.equals("HydrogenRearrangementGamma") || typeOfFragmentation.equals("HydrogenRearrangementDelta")){
										if(FragmentController.isPredecessor(fragTree, fragmentToStudy,fragmentE)){
											
											if(printInfo)
												System.out.println("isPredecesor-Not added");
											
											continue;
										}
									}
									/*prove probability*/
									double prob = -1;

									if(printInfo){
										System.out.println("exists");
									}
									
									
									if(process == Medea.PREDICTPROCESS || process == Medea.LEARN_PREDPROCESS){
										if(printInfo)
											System.out.print("PREDICTPROCESS& LEARN_PREDPROCESS");
										prob = extractorP.getProbability(fragmentToStudy, j, fragments.getMolecule(k), fragments.getMolecule(molk),setOfReactions.getReaction(counta).mappings());
										if(printInfo)
											System.out.println("proba: "+prob);
										
										if(prob < 0.05){
//											System.out.println("not continue, not suficient probability");
											continue;
										}
									}
									addFragment(fragmentE,
											typeOfFragmentation,
											fragmentToStudy.getIdP(),
											prob,
											setOfReactions.getReaction(counta).mappings(),
											fragments.getMolecule(molk));
										
								}
								
							}else
								if(printInfo)
								System.out.println("is not Accepted");

						}
							counta++;
					}
				}
			}
			
			
		}

		if(printInfo)
		System.out.println("Ended: total fragments found: "+setOfFragments.size());
	}
	
	private void printInformation(IMolecule fragmentToStudy) {
		String smiles = (new SmilesGenerator()).createSMILES(fragmentToStudy);
		MFAnalyser mfAnalyser = new MFAnalyser(fragmentToStudy);
		System.out.println("SMILE: " + smiles+", count Atoms: " + fragmentToStudy.getAtomCount()+ ", count Bonds: " + fragmentToStudy.getBondCount() + ", imass: "+ Math.round(mfAnalyser.getMass()) );
		
		Iterator atomsI = fragmentToStudy.atoms();
		int count = 0;
		while(atomsI.hasNext()){
			IAtom atom = (IAtom)atomsI.next();
			int se = fragmentToStudy.getConnectedSingleElectronsCount(atom);
			System.out.println("Atom: "
							+ count
							+ ", Sym: "
							+ atom.getSymbol()
							+ ", AtomAt: "
							+ fragmentToStudy.getConnectedAtomsCount(atom)
							+ ", lpe: "
							+ fragmentToStudy.getConnectedLonePairsCount(atom)
							+ ", sg: " + se
							+ ", Charge: "
							+ atom.getFormalCharge());
			count++;
		}
		
	}
	/**
	 * Adds the new fragment into MSFragments, and this into an ArrayList which contains all
	 * fragments and its conections
	 * 
	 * @param fragment     IMolecule to add.
	 * @param nameProcess  Name of the process which is obtained this molecule.
	 * @param parent       position of the predecessor.
	 * @param probab       The probability of obtaining this reaction.
	 * @param mapping      The mapping of the reaction
	 * @param fragmentNB   IMolecule represents the Neighboring
	 * @return The MSFragments value
	 */
	private FragmentMolecule addNewFragment(IMolecule fragment, String nameProcess, Position parent, double probab, Iterator mapping, IMolecule fragmentNB){
		int mass = Math.round((new MFAnalyser(fragment)).getMass());
		Position id = new Position(mass, fragTree.getFragments(mass).size());
		
		FragmentMolecule msfrag = new FragmentMolecule(fragment, id, nameProcess, parent, probab);
		fragTree.addFragment(msfrag);

		if(!nameProcess.equals("Ionitzation"))
			fragmentToStudy.setChildren(id, nameProcess, mapping, fragmentNB,  probab);
		else
			fragTree.getFragment(new Position(mass, 0)).setChildren(id,nameProcess, mapping, fragmentNB, probab);
		
		return msfrag;

	}
	/**
	 * Adds the new fragment into MSFragments, and this into an ArrayList which contains all
	 * fragments and its conections
	 * 
	 * @param fragment     IMolecule to add.
	 * @param nameProcess  Name of the process which is obtained this molecule.
	 * @param parent       position of the predecessor.
	 * @param probab       The probability of obtaining this reaction.
	 * @param mapping      The mapping of the reaction
	 * @param fragmentNB   IMolecule represents the Neighboring
	 * @return The MSFragments value
	 */
	private void addFragment(FragmentMolecule fragment, String nameProcess, Position parent, double probab, Iterator mapping, IMolecule fragmentNB){
		int mass = Math.round((new MFAnalyser(fragment)).getMass());
		Position id = fragment.getIdP();
		
		fragment.setParent(parent);

		if(!nameProcess.equals("Ionitzation"))
			fragmentToStudy.setChildren(id, nameProcess, mapping, fragmentNB,  probab);
		else
			fragTree.getFragment(new Position(mass, 0)).setChildren(id,nameProcess, mapping, fragmentNB, probab);

	}
	/**
	 * get the FragmentTree object
	 * 
	 * @return The FragmentTree object
	 */
	public FragmentTree getFragmentTree(){
		return fragTree;
	}
	/**
	 * get the number of fragments in this moment
	 * 
	 * @return The number of fragments found
	 */
	public int numberOfFragments(){
		return numberOfFragments;
	}
//	/**
//	 * Get the probabilities of the ionization
//	 * @return An ArrayList with the probabilities
//	 */
//	public ArrayList<Double> getProbabilitiesIP(){
//		return proIp;
//	}
	/**
     * clean the flags CDKConstants.REACTIVE_CENTER from the molecule
     * 
	 * @param mol
	 */
	public void cleanFlagReactiveCenter(IMolecule molecule){
		for(int j = 0 ; j < molecule.getAtomCount(); j++)
			molecule.getAtom(j).setFlag(CDKConstants.REACTIVE_CENTER, false);
		for(int j = 0 ; j < molecule.getBondCount(); j++)
			molecule.getBond(j).setFlag(CDKConstants.REACTIVE_CENTER, false);
	}
}
