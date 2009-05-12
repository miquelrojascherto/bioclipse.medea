package net.bioclipse.reaction.wizards;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

import nu.xom.ParentNode;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.interfaces.IMolecule;
/**
 * 
 * @author Miguel Rojas
 */
public class NewAquisitionWizard extends Wizard{

	private AddMoleculeWizardPage addMoleculePage;
	private IMolecule molecule;

	/**
	 * Constructor of the NewAquisitionWizard object
	 */
	public NewAquisitionWizard(){
		super();
		setNeedsProgressMonitor(true);
	}
	/**
	 * Adding the page to the wizard.
	 */
	public void addPages(){  
		addMoleculePage = new AddMoleculeWizardPage();
		addPage(addMoleculePage);

	}
	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	@SuppressWarnings("deprecation")
	public boolean performFinish() {

		IAtomContainer ac = null;
		IMolecule mol = null;
		CMLCml cml = new CMLCml();
		
		
		IBioResource molResource = addMoleculePage.getSelectedRes();
		CMLMolecule cmlMolecule = null;
		if(molResource == null){
			return false;
		}else if(!molResource.getPersistedResource().load()){
			return false;
		}
		if(molResource.parseResource()){
			CMLWriter cmlWriter = new CMLWriter();
			Object parsedRes = molResource.getParsedResource();
			IChemModel model = null;
			if(parsedRes instanceof CMLMolecule){
				cmlMolecule = (CMLMolecule) parsedRes;
				cmlMolecule.detach();
				((ParentNode)cml).appendChild(cmlMolecule.copy());
				
				String moleculestring = cmlMolecule.toXML();
		        CMLReader reader = new CMLReader(new ByteArrayInputStream(moleculestring.getBytes()));
		        IChemFile file;
				try {
					file = (IChemFile)reader.read(new org.openscience.cdk.ChemFile());
					mol = file.getChemSequence(0).getChemModel(0).getMoleculeSet().getMolecule(0);
				} catch (CDKException e) {
					e.printStackTrace();
				}
				
				
			}else if(parsedRes instanceof IChemModel){
				model = (IChemModel) parsedRes;
			}else if(parsedRes instanceof IChemFile){
				model = ((IChemFile)parsedRes).getChemSequence(0).getChemModel(0);
			}
			if(model != null){
				StringWriter sout = new StringWriter();
				try {
					cmlWriter.setWriter(sout);
				} catch (CDKException e) {
					e.printStackTrace();
					return false;
				}
				ac = model.getMoleculeSet().getAtomContainer(0);
				mol = model.getBuilder().newMolecule(ac);
				if(mol != null || mol.getAtomCount() != 0){
					try {
						cmlWriter.write(mol);
					} catch (CDKException e) {
						e.printStackTrace();
						return false;
					}
				}
				CMLBuilder builder = new CMLBuilder();
				CMLElement cmlElement = null;
				try {
					cmlElement = (CMLElement)builder.parseString(sout.toString());
				} catch (ValidityException e) {
					e.printStackTrace();
					return false;
				} catch (ParsingException e) {
					e.printStackTrace();
					return false;
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
				cmlElement.detach();
				((ParentNode)cml).appendChild(cmlElement.copy());
				try {
					cmlWriter.close();
					sout.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}else{
			return false;
		}
		this.molecule = mol;
		return true;
	}

	/*
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}
	/**
	 * get the IMolecule
	 * 
	 * @return The IMolecule object
	 */
	public IMolecule getIMolecule(){
		return molecule;
	}

}
