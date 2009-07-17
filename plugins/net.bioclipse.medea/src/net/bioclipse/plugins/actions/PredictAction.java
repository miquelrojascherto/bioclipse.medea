package net.bioclipse.plugins.actions;


import net.bioclipse.plugins.wizards.PredictWizard;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemFile;
/**
* Action during which the MEDEA process starts. From a compound, it predictes 
* its mass spectrum. The accepted resource of the compound are CDKResource and
* CMLResource.
* 
* @author Miguel Rojas
* @see IViewActionDelegate
*/
public class PredictAction implements IViewActionDelegate {
	
	/** The view component */
	private IViewPart view = null;
	/** MEDEA program for Bioclipse*/
	private MedeaSWT medea;
	
	/**
	 * The constructor of the PredictAction object.
	 */
	public PredictAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		view=(IViewPart) targetPart;
	}
	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * 
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
//		PredictWizard predwiz = new PredictWizard(view);
//		WizardDialog wd=new WizardDialog(new Shell(),predwiz);
//		wd.open();		

//		if (view instanceof BioResourceView) {
//			Object first = ((BioResourceView)view).getSelectedResource();
//			IChemFile cf = null;
//			String name = null;
//			/* For CDKResource */
//			if (first instanceof CDKResource){
//				CDKResource cdkres = (CDKResource)first;
//				cdkres.getPersistedResource().load();
//				cdkres.parseResource();
//				cf= (IChemFile)cdkres.getParsedResource();
//				name = cdkres.getName();
//			}
//			/* For CMLResource */
//			else if (first instanceof CMLResource){
//				CMLResource cmlres = (CMLResource)first;
//				cmlres.getPersistedResource().load();
//				cmlres.parseResource();
//				cf= (IChemFile)cmlres.getParsedResource();
//				name = cmlres.getName();
//			}
//			if(cf != null){
//				IAtomContainer ac = cf.getChemSequence(0).getChemModel(0).getMoleculeSet().getAtomContainer(0);
//				ac.setID(name);
//				if(ac != null)
//					callMedaProcess(ac);
//				
//			}
//		}
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
	 */
	private void callMedaProcess(IAtomContainer ac){
		if(medea == null)
			medea = new MedeaSWT(view);
		medea.predictMSThread(ac);
	}
}