package net.bioclipse.plugins.actions;


import java.io.ByteArrayInputStream;

import net.bioclipse.model.SpecMolResource;
import net.bioclipse.views.BioResourceView;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.io.CMLReader;
import org.xmlcml.cml.element.CMLMolecule;
import org.xmlcml.cml.element.CMLSpectrum;
/**
* Action during which the MEDEA process starts. Its funcionality
* is to get the relation between an IMolecule and a Mass Spectrum (CMLSpectrum).
* 
* @author Miguel Rojas
* @see IViewActionDelegate
*/
public class LearnAction implements IViewActionDelegate {
	
	/** The view component */
	private IViewPart view = null;
	/** MEDEA program for Bioclipse*/
	private MedeaSWT medea;
	
	/**
	 * The constructor of the PredictAction object.
	 */
	public LearnAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}
	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI. \n
	 * 
	 * @see IActionDelegate#run(IAction)
	 */
	@SuppressWarnings("static-access")
	public void run(IAction action) {
		SpecMolResource smres = null;
		if (view instanceof BioResourceView) {
			Object first = ((BioResourceView)view).getSelectedResource();
			/* For SpecMolResource */
			if (first instanceof SpecMolResource){
				smres = (SpecMolResource)first;
				smres.getPersistedResource().load();
				smres.parseResource();
				CMLMolecule cmlM = smres.getMolecule();
				String moleculestring = cmlM.toXML();
		        CMLReader reader = new CMLReader(new ByteArrayInputStream(moleculestring.getBytes()));
				try {
					IChemFile file = (IChemFile)reader.read(new ChemFile());
					IChemModel chemmodel = file.getChemSequence(0).getChemModel(0);
					IAtomContainer ac = chemmodel.getMoleculeSet().getAtomContainer(0);
					ac.setID(smres.getName());

					CMLSpectrum cmlS = smres.getCurrentSpectrum();
					/*extract root with name*/
					String pathh = smres.getPath().substring(0, smres.getPath().length()-smres.getExtension().length()-1);
					if(ac != null && cmlS != null)
						callMedaProcess(ac, cmlS, pathh);

				} catch (CDKException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * Selection in the workbench has been changed. 
	 * 
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}
	/**
	 * this method disposeS of any system
	 * resources we previously allocated.
	 * 
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * 
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IViewPart view) {
		this.view = view;
	}
	/**
	 * call and initiate the class Medea
	 * 
	 * @param ac The IAtomContainer
	 * @param cmlSpectrum The CMLSpectrum
	 * @param nameFile A String with the name
	 * 
	 */
	private void callMedaProcess(IAtomContainer ac, CMLSpectrum cmlSpectrum, String nameFile){
		if(medea == null)
			medea = new MedeaSWT(view);
		medea.learningsMSThread(ac, cmlSpectrum, nameFile);
	}
}