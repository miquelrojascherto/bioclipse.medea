package net.bioclipse.medea.core.learning;

import java.io.IOException;
import java.util.ArrayList;

import net.bioclipse.medea.core.FragmentMolecule;
import net.bioclipse.medea.core.FragmentTree;
import net.bioclipse.medea.core.Position;
import net.bioclipse.medea.core.prediction.AdministratorFilesReader;
import net.bioclipse.medea.core.reaction.ExtractorSetReactions;
import net.bioclipse.medea.core.reaction.ReactionKp;

import org.openscience.cdk.interfaces.IMapping;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.interfaces.IReactionSet;

public class ExtractorProbability {

	private static FragmentTree fragmentTree;
	private static double[] peaksXY;
	
	private AdministratorFilesReader admin;
	/**
	 * Constructor of the ExtractorProbability object
	 * 
	 * @param process The process which is working
	 */
	@SuppressWarnings("deprecation")
	public ExtractorProbability() {
//		String owlFile="/data/";
//		
//        Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
//        System.out.println(bundle);
//		URL url= bundle.getEntry(owlFile);
//		
//		System.out.println(url.getPath());
//
//		
//		try {
//			URL furl = FileLocator.toFileURL(url);
//			System.out.println("fileURL: " + furl);
//			File file = FileLocator.getBundleFile(bundle);
//			System.out.println("file: " + file.getCanonicalPath());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		String pp ="";
//		try {
//			pp = Platform.asLocalURL(url).getPath().toString()+"data";
//			System.out.println("pata: "+pp);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		admin = new AdministratorFilesReader();
	}
	/**
	 * Set the probability 
	 * @param fragmentTree The FragmentTree
	 * @param p The peaks X
	 * @param pro 
	 */
	public static void setProbabilities(FragmentTree fTree, double[] p,String nameFile) {
		fragmentTree = fTree;
		peaksXY = p;
//		prob = pro;

		IReactionSet reactions = (new ExtractorSetReactions(fragmentTree)).extract();
		AdministratorFilesWriter add = new AdministratorFilesWriter(nameFile);

		for(int i = 0 ; i < reactions.getReactionCount() ; i++){
			double probability = 0.0;
//			System.out.println("num Reaction: "+i+", from: "+reactions.getReactionCount());
			IReaction reaction = reactions.getReaction(i);
//			System.out.println(i+"+++++++++++++++++++++++++++++++++++++++++++++++++++++numbR ");
//			if(i < prob.size()){/*probility of the IP already found*/
//				massI = (int)((FragmentMolecule)reaction.getReactants().getAtomContainer(0)).getId().getWidth();
//				probability = prob.get(i);
//			}else{/*the rest*/
				probability = getProbability((ReactionKp) reaction);
				if(reaction instanceof ReactionKp){
					ReactionKp reactionKp = (ReactionKp) reaction;
					reactionKp.setProbability(probability);
					
//					for(int ip = 0 ; ip < reactionKp.getReactantCount(); ip++)
//						((FragmentMolecule)reactionKp.getReactants().getAtomContainer(ip)).setProbabilities(probability);
//					for(int ip = 0 ; ip < reactionKp.getProductCount(); ip++)
//						((FragmentMolecule)reactionKp.getProducts().getAtomContainer(ip)).setChildrenProbabilities(probability);
					
					try {
//						System.out.println("setRea:");
						for(int jj = 0; jj < 1; jj++)
							add.addReaction(reactionKp);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
//			}
//			System.out.println("probability: "+probability);
		}
		try {
			add.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * get the probability of this reaction 
	 * 
	 * @param reaction The IReaction
	 * @return  The probability
	 */
	private static double getProbability(ReactionKp reaction) {
//		String smiles2 = (new SmilesGenerator()).createSMILES((IMolecule)reaction.getProducts().getMolecule(0));
//		String smiles1 = (new SmilesGenerator()).createSMILES((IMolecule)reaction.getReactants().getMolecule(0));
//		System.out.println(smiles1+" => "+smiles2);
		double probability = 0.0;
		FragmentMolecule reactant = (FragmentMolecule)reaction.getReactants().getAtomContainer(0);
		FragmentMolecule product = (FragmentMolecule)reaction.getProducts().getAtomContainer(0);
		
		int reactantMASS = (int)reactant.getIdP().getWidth();
		double reactantABUND = peaksXY[reactantMASS];
		int productMASS = (int)product.getIdP().getWidth();
		double productABUND = peaksXY[productMASS];
		if(productABUND == -1.0)/*if is obtained a structure which is not found in the spectrum*/
			return 0.0;
//		double totalABUND = 0.0;
//		
//		int numberBrother = fragmentTree.getFragments((int)reactant.getIdP().getWidth()).size();
		
		/* the ionization already contains the correct probability*/
		if(reaction.getNameReaction().equals("Ionitzation")){
//			System.out.println(": "+reaction.getProperty("IonizationEnergy"));
			return ((Double) reaction.getProperty("IonizationEnergy")).doubleValue();
		}
		/*divide for all with the same masse*/
//		if(massI == (int)reactant.getId().getWidth()){
//			numberBrother += -1;
//			int pos = (int)reactant.getId().getHeight();
//			reactantABUND = reactantABUND*(/100);
//		}
		double neighABUND = 0.0;
		ArrayList<Position> children = reactant.getChildren();
		for(int i = 0 ; i< children.size() ; i++){
			FragmentMolecule child = fragmentTree.getFragment(children.get(i));
			int sizeParents = child.getParents().size();
			neighABUND += peaksXY[(int) child.getIdP().getWidth()]/sizeParents;
//			System.out.print(", "+peaksXY[(int) child.getId().getWidth()]);
		}
		
//		System.out.println("{ numberBrother:"+numberBrother);
		if(reactantABUND == 0.0)
			reactantABUND = 0.01; /* FALSH */
		
//		System.out.println("parents: "+product.getParents().size());
		productABUND = (productABUND/product.getParents().size());
		double result1 = (reactantABUND+(neighABUND-productABUND))/(neighABUND+reactantABUND);
//		System.out.println("result1= "+result1 +"= "+reactantABUND+"+"+(neighABUND-productABUND)+"/("+neighABUND+"+"+reactantABUND+")");
		double result2 = productABUND/(neighABUND);
		probability = 1-Math.pow(result1,result2);
//		System.out.println(probability +"= 1-("+result1+"^"+result2+")");
		
		return probability;
	}
	/**
	 * get the probability for this reaction
	 * @param iterable 
	 * 
	 * @param fragmentToStudy
	 * @param molecule
	 * @param molecule2
	 * @return
	 */
	public double getProbability( String nameClass, FragmentMolecule reactant, IMolecule productA, IMolecule productB, Iterable<IMapping> iterable) {
//		String name = "";
		double probability = 0.0;
		ReactionKp reaction = new ReactionKp();
		reaction.addReactant(reactant);
		reaction.addProduct(productA);
		if(productB != null)
			reaction.addProduct(productB);
		for(IMapping mapping:iterable)
			reaction.addMapping(mapping);
		
//		if(nameR == 0)
//			name = "RadicalSiteInitiation";
//		if(nameR == 1)
//			name = "RadicalSiteInitiationH";
//		if(nameR == 2)
//			name = "CarbonylElimination";
//		if(nameR == 3)
//			name = "HydrogenRearrangementDelta";
//		if(nameR == 4)
//			name = "HydrogenRearrangementGamma";
		
		reaction.setNameReaction(nameClass);

		probability = admin.getProbability(reaction);
		
		return probability;
	}
	
}
