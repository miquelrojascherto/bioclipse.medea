package net.bioclipse.plugins.medea.core;

import java.util.ArrayList;
import java.util.Iterator;

import org.openscience.cdk.Molecule;
import org.openscience.cdk.interfaces.IMapping;
import org.openscience.cdk.interfaces.IMolecule;

/**
 * Class which is an extension of IMolecule and collects all information 
 * about the fragment obtained in a reaction.
 * 
 * @author Miguel Rojas
 *
 */
public class FragmentMolecule extends Molecule {
	
	private static final long serialVersionUID = 5370937629360742069L;
	
	private ArrayList<Double> probabChildrenA = new ArrayList<Double>();
	private ArrayList<Double> probabA = new ArrayList<Double>();
	private ArrayList<Position> parentA = new ArrayList<Position>();
	private ArrayList<Position> childrenA = new ArrayList<Position>();
	private ArrayList<Iterator> mappingA = new ArrayList<Iterator>();
	private ArrayList<String> processA = new ArrayList<String>();
	private ArrayList<IMolecule> neighbouringA = new ArrayList<IMolecule>();
	private Position identification;

	private double abundance;
	
	/**
	 * Constructor of the MSFragments object 
	 * 
	 * @param molecule The Fragment to store
	 * @param id       The identification of this fragment. It consists on [mass, positionFragmentTree]
	 * @param nameProcess      Name of the process/reaction which is obtained this molecule
	 * @param origin   The position of the predecessor
	 * @param probab   The probability of obtaining this reaction.
	 */
	FragmentMolecule(IMolecule molecule, Position id, String nameProcess, Position parent, double probab){

		add(molecule);
		setID(id.height+"_"+id.width);
		molecule.setID(id.height+"_"+id.width);
		identification = id;
		probabA.add(probab);
		parentA.add(parent);
		
	}
	/**
	 * get the parent of this fragment
	 * @return An ArrayList with all parents
	 * 
	 */
	public ArrayList<Position> getParents(){
		return parentA;
	}
	/**
	 * set the parent of this fragment
	 * @param The FragmentMolecule
	 * 
	 */
	public void setParent(Position parent){
		parentA.add(parent);
	}
	/**
	 * set the children of this fragment
	 * 
	 * @param children    The children of this fragment
	 * @param nameProcess The name which this process is obtained
	 * @param iterable     The mapping of the reaction
	 */
	public void setChildren(Position children, String nameProcess, Iterable<IMapping> iterable, IMolecule neighbouring, double prob){
		childrenA.add(children);
		processA.add(nameProcess);
		mappingA.add((Iterator) iterable);
		neighbouringA.add(neighbouring);
		probabChildrenA.add(prob);
	}
	/**
	 * get the children of this fragment
	 * 
	 * @return  An ArrayList with all children
	 */
	public ArrayList<Position> getChildren(){
		return childrenA;
	}
	/**
	 * get the probabilities of the fragment
	 * 
	 * @return An ArrayList with all probabilities
	 */
	public ArrayList<Double> getChildrenProbabilities(){
		return probabChildrenA;
	}
	/**
	 * set the probabilities of the fragment
	 * 
	 * @param the probability
	 */
	public void setChildrenProbabilities(double prob){
		probabChildrenA.add(prob);
	}
	/**
	 * get the probabilities of the fragment
	 * 
	 * @return An ArrayList with all probabilities
	 */
	public ArrayList<Double> getProbabilities(){
		return probabA;
	}
	/**
	 * set the probabilities of the fragment
	 * 
	 * @param the probability
	 */
	public void setProbabilities(double prob){
		probabA.add(prob);
		
	}
	/**
	 * get the identification of this fragment
	 * 
	 * @return The position # identification
	 */
	public Position getIdP(){
		return identification;
	}
	/**
	 * get the name of the process
	 * 
	 * @return String 
	 */
	public ArrayList<String> getProcess(){
		return processA;
	}
	/**
	 * get the mapping of the reaction 
	 * @return The IMapping
	 */
	public ArrayList<Iterator> getMapping(){
		return mappingA;
	}
	/**
	 * get the the neighbouring fragment of this FragmentMolecule 
	 * @return The IMolecule neighbouring
	 */
	public ArrayList<IMolecule> getNeighbouring(){
		return neighbouringA;
	}
	public void setAbundance(double abun){
		abundance = abun;
	}
	public double getAbundance(){
		return abundance;
	}
}
