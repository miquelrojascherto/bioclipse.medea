package net.bioclipse.medea.core.prediction;

import java.util.ArrayList;

import net.bioclipse.medea.core.FragmentMolecule;
import net.bioclipse.medea.core.FragmentTree;
import net.bioclipse.medea.core.Position;

import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

public class ExtractorAbundance {

	private static int massI;
	private static FragmentMolecule fm;
	private static FragmentTree fragmentTree;
	/**
	 * Constructor of the ExtractorProbability object
	 * 
	 * @param process The process which is working
	 */
	public ExtractorAbundance(){
	}

	public static void setAbundace(FragmentTree tree, ArrayList<Double> probabilitiesIP) {
		
		fragmentTree = tree;
		
		double probParent = 0.0;
		double prob = 0.0;
		double factor = 3.4;
		
		IMolecule molecule = fragmentTree.getMolecule();
		
//		MFAnalyser mfAnalyser = new MFAnalyser(molecule);
		IMolecularFormula formula = MolecularFormulaManipulator.getMolecularFormula(molecule);
		massI = (int) MolecularFormulaManipulator.getTotalExactMass(formula);
		
		fm = fragmentTree.getFragment(new Position(massI,0));
		fm.setAbundance(-1.0);
		ArrayList<FragmentMolecule> al = new ArrayList<FragmentMolecule>();
		al.add(fm);
		int count = 0;
		for(int i = 0 ; i < al.size(); i++){
//			System.out.println("i: "+i+", "+al.size());
			FragmentMolecule fragmentParent = al.get(i); 
			ArrayList<Position> children_D = fragmentParent.getChildren();
			for(int j = 0 ; j < children_D.size(); j++){
				FragmentMolecule fragment = fragmentTree.getFragment(children_D.get(j));
//				System.out.println("name: "+al.get(i).getProcess().get(j));
//				System.out.println("smiles: "+(new SmilesGenerator()).createSMILES(fragment));
//				System.out.println("prob: "+al.get(i).getChildrenProbabilities().get(j));
				prob = al.get(i).getChildrenProbabilities().get(j);
				count++;
				double probTotal  = 0.0;
				/*probability parent*/
				if(al.get(i).getProcess().get(j).equals("Ionitzation")){
					if(fragment.getChildren().size() > 0)
						probParent = 1.0;
					else
						probParent = 0.0;
				}else{
					/*father*/
					double probNeight  = 0.0;
					
					probParent = fragmentParent.getProbabilities().get(0);
					/*probability neightbour*/
					if(children_D.size() > 1)
					for(int k = 0 ; k < children_D.size(); k++){
							double abb = al.get(i).getChildrenProbabilities().get(k);
							probNeight += abb;
//							System.out.println("aaBB: "+abb);
					}
					
					if(probNeight != 0.0)
						probTotal = probNeight;
				}
				if(probTotal == 0)
					probTotal = 1;
					
				
				double abundance = probParent*prob*(1-Math.exp(-factor))/(probTotal);
//				System.out.println("abundance: "+abundance+"= pP("+probParent+")*p("+prob+")* / prbT("+probTotal);
				ArrayList probb = fragment.getProbabilities();
				probb.set(0, abundance);
				fragment.setAbundance(fragment.getAbundance()+abundance);
				
				if(count < 50)
					al.add(fragment);
			}
		}
//		System.out.println("Substract+++++++++++++++");
		subtractDescendents();
//		System.out.println("SetABund+++++++++++++++");
		extractAbundanceRelative();
		
	}
	/**
	 * Substract descendents
	 *
	 */
	private static void subtractDescendents() {
		ArrayList<FragmentMolecule> al = new ArrayList<FragmentMolecule>();
		al.add(fm);
		
		for(int i = 0 ; i < al.size(); i++){
//			System.out.println("--, "+i);
			FragmentMolecule fragmentParent = al.get(i);
			double abundance = fragmentParent.getAbundance();
			ArrayList<Position> children_D = fragmentParent.getChildren();
			double abundanceTChildren = 0.0;
//			System.out.println("="+fragmentParent.getAbundance());
//			System.out.println("children ="+children_D.size());
			for(int j = 0 ; j < children_D.size(); j++){
				FragmentMolecule fragment = fragmentTree.getFragment(children_D.get(j));
//				System.out.println("smiles: "+(new SmilesGenerator()).createSMILES(fragment));
//				
				if(fragmentParent.getAbundance() == -1){
					if(al.size() < 50)
						al.add(fragment);
					continue;
				}
				/*it is not possible that abundanceTChildren < abundance*/
				if(fragment.getAbundance() > abundance){
//					System.out.println("remove: "+fragment.getAbundance()+"=<>"+abundance);

					if(i != 0)
						fragment.setAbundance(abundance);
				}
				
				abundanceTChildren += fragment.getAbundance();
				
//				System.out.println(j+"="+fragment.getAbundance());
				if(al.size() < 50)
					al.add(fragment);
			}
			double abundanceT = abundance - abundanceTChildren;
//			System.out.println(abundanceT+"="+abundance+" - "+abundanceTChildren);
			if(abundanceT < 0){
//				System.out.println("setAbundanceT == 0");
				abundanceT = 0;
			}
			fragmentParent.setAbundance(abundanceT);

		}
	}

	/**
	 * extract the relative abundance
	 *
	 */
	private static void extractAbundanceRelative() {
		/*extract maximum abundance*/
		double maxAbundance = 0.0;
		ArrayList<FragmentMolecule> al = new ArrayList<FragmentMolecule>();
		al.add(fm);
		for(int i = 0 ; i < al.size(); i++){
			FragmentMolecule fragmentParent = al.get(i); 
			ArrayList<Position> children_D = fragmentParent.getChildren();
//			System.out.println("="+fragmentParent.getAbundance());
//			System.out.println("children ="+children_D.size());
			for(int j = 0 ; j < children_D.size(); j++){
				FragmentMolecule fragment = fragmentTree.getFragment(children_D.get(j));
//				System.out.println("smiles: "+(new SmilesGenerator()).createSMILES(fragment));
//				System.out.println((new SmilesGenerator()).createSMILES(fragment)+"-ff: "+fragment.getAbundance());
				if(fragment.getAbundance() > maxAbundance)
					maxAbundance = fragment.getAbundance();
				if(al.size() < 50)
					al.add(fragment);
			}
		}
//		System.out.println("maxAbund: "+maxAbundance);
		
		al = new ArrayList<FragmentMolecule>();
		fm.setAbundance(0.0);
		al.add(fm);
		for(int i = 0 ; i < al.size(); i++){
//			System.out.println("--, "+i);
			FragmentMolecule fragmentParent = al.get(i); 
			ArrayList<Position> children_D = fragmentParent.getChildren();
			for(int j = 0 ; j < children_D.size(); j++){
				FragmentMolecule fragment = fragmentTree.getFragment(children_D.get(j));
				if(!al.contains(fragment)){	
				double aa = fragment.getAbundance()/maxAbundance*100;
//				System.out.println("smiles: "+(new SmilesGenerator()).createSMILES(fragment));
//				System.out.println(aa+"="+fragment.getAbundance()+"/"+maxAbundance+"*100");
					if(aa < 0)
						fragment.setAbundance(0.0);
					else 
						fragment.setAbundance(aa);
					if(al.size() < 50)
						al.add(fragment);
				}
//				else
//					System.out.println("already contined");
			}
		}
		
	}
	
}
