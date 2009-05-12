package net.bioclipse.plugins.medea.core.reaction;

import java.util.ArrayList;
import java.util.Iterator;

import net.bioclipse.plugins.medea.core.FragmentMolecule;
import net.bioclipse.plugins.medea.core.FragmentTree;
import net.bioclipse.plugins.medea.core.Position;

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IMapping;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IReactionSet;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

public class ExtractorSetReactions {
	
	private FragmentTree fragmentTree;
	
	/**
	 * Constructor of the ExtractorSetReactions object
	 * 
	 * @param fragmentTree
	 */
	public ExtractorSetReactions(FragmentTree fragmentTree){
		this.fragmentTree = fragmentTree;
	}
	/**
	 * Extract from a FragmentTree all reactions which contains
	 * 
	 * @return A set of reactions
	 */
	public IReactionSet extract() {
		IReactionSet setOfReactions = DefaultChemObjectBuilder.getInstance().newReactionSet();
		
		IMolecule molecule = fragmentTree.getMolecule();
		IMolecularFormula formula = MolecularFormulaManipulator.getMolecularFormula(molecule);
		int massI = (int) MolecularFormulaManipulator.getTotalExactMass(formula);
		
		FragmentMolecule fm = fragmentTree.getFragment(new Position(massI,0));
		ArrayList<FragmentMolecule> al = new ArrayList<FragmentMolecule>();
		al.add(fm);
		int count = 0;
		for(int i = 0 ; i < al.size(); i++){
			
			ArrayList<Position> children_D = al.get(i).getChildren();
			for(int j = 0 ; j < children_D.size(); j++){
				ReactionKp reaction = new ReactionKp();
				reaction.setID(al.get(i).getProcess().get(j));
				reaction.addReactant(al.get(i));
				reaction.addProduct(fragmentTree.getFragment(children_D.get(j)));
				
//				String smiles0 = (new SmilesGenerator()).createSMILES((IMolecule)al.get(i));
//				System.out.print(count+", "+smiles0);
				count++;
				if(al.get(i).getProcess().get(j).equals("Ionitzation")){
					reaction.setProperty("IonizationEnergy",al.get(i).getChildrenProbabilities().get(j));
				}
				
				if(al.get(i).getNeighbouring().get(j) != null)
					reaction.addProduct(al.get(i).getNeighbouring().get(j));
				if(al.get(i).getMapping().get(j) != null){
					Iterator mappingI = al.get(i).getMapping().get(j);
					while(mappingI.hasNext())
						reaction.addMapping((IMapping) mappingI.next());
				}
					
				reaction.setNameReaction(al.get(i).getProcess().get(j));
				setOfReactions.addReaction(reaction);
				if(count < 50)
					al.add(fragmentTree.getFragment(children_D.get(j)));
				String smiles = (new SmilesGenerator()).createSMILES(fragmentTree.getFragment(children_D.get(j)));
				String smiles2 = null;
//				if(al.get(i).getNeighbouring().get(j) != null)
//					smiles2 = (new SmilesGenerator()).createSMILES((IMolecule)al.get(i).getNeighbouring().get(j));
//				System.out.println(" "+al.get(i).getProcess().get(j)+" => s: "+smiles+", "+smiles2);
			}
		}
//		System.out.println("end");
		
		return setOfReactions;
	}
}
