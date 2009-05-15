package net.bioclipse.reaction.model;

import net.bioclipse.reaction.tools.CounterObjects;

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;

/**
 * 
 * @author Miguel Rojas
 */
public class CompoundObjectModel extends AbstractObjectModel{
	
	private IAtomContainer molecule = null;
	/**
	 * Constructor of the CompoundObjectModel object
	 */
	public CompoundObjectModel(){
		super();
		CounterObjects.setCompound();
		setText("mol"+CounterObjects.getCompoundNumber());
	}
	/**
	 * set the text of the CompoundObjectModel 
	 * @param text The Text
	 */
	public void setText(String text){
		super.setText(text);
		if(molecule != null)
			molecule.setID(text);
	}
	/**
	 * set a IMolecule 
	 * @param molecule The IMolecule object
	 */
	public void setIMolecule(IAtomContainer molecule) {
		this.molecule = molecule;
		
	}
	/**
	 * get the IMolecule
	 * @return The IMolecule object
	 */
	public IAtomContainer getIMolecule(){
	  if(molecule==null)
	        return DefaultChemObjectBuilder.getInstance().newAtomContainer();
		return molecule;
	}
	
}
