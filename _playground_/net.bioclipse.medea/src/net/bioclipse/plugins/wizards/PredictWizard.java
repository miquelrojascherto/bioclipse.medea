package net.bioclipse.plugins.wizards;


import java.io.File;
import java.lang.reflect.InvocationTargetException;

import net.bioclipse.model.BioResource;
import net.bioclipse.model.IBioResource;
import net.bioclipse.model.SpectrumResource;
import net.bioclipse.plugins.actions.MedeaSWT;
import net.bioclipse.plugins.bc_reaction.resource.ReactionResource;
import net.bioclipse.plugins.medea.core.Medea;
import net.bioclipse.plugins.perspective.MedeaPerspective;
import net.bioclipse.util.folderUtils;
import net.bioclipse.views.BioResourceView;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.io.formats.IChemFormat;
import org.xmlcml.cml.element.CMLSpectrum;

/**
 * 
 * @author Miguel Rojas
 */
public class PredictWizard extends Wizard implements INewWizard{
	
	private SpectrumWizardPage spectrumWizard;
	private SaveSpectrumWizardPage saveSpectrumWizard;
	private SaveTreeReactionWizardPage saveTreeWizard;
	private MedeaSWT medea;

	/**
	 * Constructor for PredictWizard object.
	 * @param medea The Medea object
	 */
	public PredictWizard(MedeaSWT medea) {
		super();
		setNeedsProgressMonitor(true);
		this.medea = medea;
	}
	
	/**
	 * Adding the page to the wizard.
	 */
	public void addPages() {
		spectrumWizard = new SpectrumWizardPage();
		addPage(spectrumWizard);
		saveSpectrumWizard = new SaveSpectrumWizardPage();
		addPage(saveSpectrumWizard);
		saveTreeWizard = new SaveTreeReactionWizardPage();
		addPage(saveTreeWizard);
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		
		/*save spectrum*/
		if(saveSpectrumWizard.saveSpectrum()){
			/*Get filename from wizard page*/
			String fileName = saveSpectrumWizard.getCompleteFileName();
			File file = new File(fileName);
			String filename = saveSpectrumWizard.getFileName() + saveSpectrumWizard.getExtension();
			
			/*Get folder to install in from wizard page*/
			BioResource parentFolder = saveSpectrumWizard.getSelectedFolder();
			if (file.exists()) {
	
				boolean result = true;
				result = MessageDialog.openConfirm(this.getShell(),
						"File already exists - Overwrite?",
						"There exists already a file " + file.getAbsolutePath()
								+ "\nShould it be overwritten?");
				if(!result) {
					return false;
				}else
					file.delete();
			}
			
			/*Create the new SpectrumResource as a child of the folder*/
			IBioResource newRes = new SpectrumResource(filename);//We will get a CMLResource here
			newRes.addToResourceTypes(folderUtils.getTypeByName(SpectrumResource.ID));
			parentFolder.addBioResourceAsChildren(newRes);
			
			CMLSpectrum spectrum = getMedeaClass().getPredictedSpectrum();
			if (spectrum != null) {
				newRes.setParsedResource(spectrum);
				newRes.save();
				BioResourceView.executeDoubleClick(newRes);
			}
		}		

		/*save reaction*/
		if(saveTreeWizard.saveReaction()){
			/*Get filename from wizard page*/
			String fileName2 = saveTreeWizard.getCompleteFileName();
			File file2 = new File(fileName2);
			String filename2 = saveTreeWizard.getFileName() + saveTreeWizard.getExtension();
			
			/*Get folder to install in from wizard page*/
			IBioResource parentFolder2 = saveTreeWizard.getSelectedFolder();
			if (file2.exists()) {
				boolean result2 = true;
				result2 = MessageDialog.openConfirm(this.getShell(),
						"File already exists - Overwrite?",
						"There exists already a file " + file2.getAbsolutePath()
								+ "\nShould it be overwritten?");
				if (!result2) {
					return false;
				}else
					file2.delete();
			}
	
			/*Create the new IReactionSet as a child of the folder*/
			ReactionResource newRes2 = new ReactionResource(filename2);
			newRes2.addToResourceTypes(folderUtils.getTypeByName(ReactionResource.ID));
			parentFolder2.addBioResourceAsChildren(newRes2);
			
			IChemFile chemFile = getMedeaClass().getChemFileReaction();
			if (chemFile != null) {
				String className = "org.openscience.cdk.io.formats.CMLFormat";
				try {
					IChemFormat chemFormat = (IChemFormat)Class.forName(className).getDeclaredMethod("getInstance", new Class[0]).invoke(Class.forName(className), new Class[0]);
					newRes2.setChemFormat(chemFormat);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				
				newRes2.setParsedResource(chemFile);
				newRes2.save();
				BioResourceView.executeDoubleClick(newRes2);
			}
			
		}
		
		/*open Perspective medea*/
		if(spectrumWizard.loadPerspective())
			openPerspective();
		
		
		return true;
	}
	/**
	 * get the IViewPart object
	 * 
	 * @return The IViewPart
	 */
	public IViewPart getIViewPart() {
		return medea.getIViewPart();
	}
	/**
	 * get the IAtomContainer will is predicted its mass spectrum
	 * 
	 * @return The AtomContainer to predict its mass spectrum
	 */
	public IAtomContainer getAtomContainer(){
		return medea.getAtomContainer();
	}
	/**
	 * get Medea class
	 * 
	 * @return The Medea object
	 */
	public Medea getMedeaClass(){
		return medea;
	}
	/**
	 * Initializes this creation wizard using the passed workbench and object selection. 
	 * This method is called after the no argument constructor and before other methods
	 * are called.
	 * 
	 *  @param workbench the current workbench
	 *  @param selection the current object selection
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		
	}
	
	/**
	 * Open the reactions perspective if it is necessary
	 */
	private void openPerspective() {
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getPerspective().getId().compareTo(MedeaPerspective.ID_PERSPECTIVE) != 0) {
			IPerspectiveDescriptor persp = PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId(MedeaPerspective.ID_PERSPECTIVE);
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().setPerspective(persp);
		}
		
	}
}