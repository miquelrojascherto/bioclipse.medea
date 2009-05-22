package net.bioclipse.reaction.wizards;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.interfaces.IChemSequence;
import org.openscience.cdk.interfaces.IReactionSet;
import org.openscience.cdk.io.formats.IChemFormat;
/**
 * 
 * @author Miguel Rojas
 */
public class NewReactionWizard extends Wizard implements INewWizard{

	AddNameFileWizardPage addNameFilePage;
	/**
	 * Constructor of the NewReactionWizard object
	 */
	public NewReactionWizard(){
		super();
		setNeedsProgressMonitor(true);
	}
	/**
	 * Adding the page to the wizard.
	 */
	public void addPages(){  
		addNameFilePage = new AddNameFileWizardPage();
		addPage(addNameFilePage);

	}
	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		String fileName = addNameFilePage.getCompleteFileName();
		File file = new File(fileName);
		String filename = addNameFilePage.getFileName() + ".rmr";
		
		/*Get folder to install in from wizard page*/
		BioResource parentFolder = addNameFilePage.getSelectedFolder();
		if (file.exists()) {

			boolean result = true;
			result = MessageDialog.openConfirm(this.getShell(),
					"File already exists - Overwrite?",
					"There exists already a file " + file.getAbsolutePath()
							+ "\nShould it be overwritten?");
			if (!result) {
				return false;
			}else
				file.delete();
		}
		
		/*Create the new ReactionResource as a child of the folder*/
		ReactionResource newRes = new ReactionResource(filename);//We will get a CMLResource here
		newRes.addToResourceTypes(folderUtils.getTypeByName(ReactionResource.ID));
		parentFolder.addBioResourceAsChildren(newRes);
		
		String className = "org.openscience.cdk.io.formats.CMLFormat";
		try {
			IChemFormat chemFormat = (IChemFormat)Class.forName(className).getDeclaredMethod("getInstance", new Class[0]).invoke(Class.forName(className), new Class[0]);
			newRes.setChemFormat(chemFormat);
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

		IReactionSet reactionSet = DefaultChemObjectBuilder.getInstance().newReactionSet();
		IChemModel chemModel = reactionSet.getBuilder().newChemModel();
		chemModel.setReactionSet(reactionSet);
		IChemSequence seq = reactionSet.getBuilder().newChemSequence();
		seq.addChemModel(chemModel);
		IChemFile chemFile = reactionSet.getBuilder().newChemFile();
		chemFile.addChemSequence(seq);
		
		newRes.setParsedResource(chemFile);
		newRes.save();
			
		BioResourceView.executeDoubleClick(newRes);

		return true;
	}

	/*
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

}
