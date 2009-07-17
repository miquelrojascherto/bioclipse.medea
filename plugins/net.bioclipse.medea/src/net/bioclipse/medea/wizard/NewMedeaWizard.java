package net.bioclipse.medea.wizard;

import net.bioclipse.medea.Activator;
import net.bioclipse.medea.business.IMedeaManager;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class NewMedeaWizard extends Wizard implements INewWizard{

	private static Logger logger = Logger.getLogger(NewMedeaWizard.class);
    private NewReactionAcquisitionWizardPage selectFilePage;
	private AddMoleculeWizardPage addMoleculePage;
	private AddSpectrumWizardPage addSpectrumPage;

//	private final PredictionMSJob computation = new PredictionMSJob();

	/**
	 * Constructor of the NewAquisitionWizard object
	 */
	public NewMedeaWizard(){
		super();
	    setWindowTitle("Create a new Prediction job");
		setNeedsProgressMonitor(true);
	}
	/**
	 * Adding the page to the wizard.
	 */
	public void addPages()  
	{  
		addMoleculePage = new AddMoleculeWizardPage();
		addSpectrumPage = new AddSpectrumWizardPage();
		selectFilePage = new NewReactionAcquisitionWizardPage();
		addPage(addMoleculePage);
		addPage(addSpectrumPage);
		addPage(selectFilePage);

	}
	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		
		IResource speResource = addSpectrumPage.getSelectedRes();
		IResource molResource = addMoleculePage.getSelectedRes();
		
		if(molResource == null || speResource == null)
			return false;
		
//			CMLWriter cmlWriter = new CMLWriter();
//			Object parsedRes = molResource.getParsedResource();
//			IChemModel model = null;
//			
//			if(molResource instanceof CMLMolecule){
//				CMLMolecule cmlMol = (CMLMolecule) parsedRes;
//				cmlMol.detach();
//				cmlMol.appendChild(cmlMol);
//			}else if(parsedRes instanceof IChemModel){
//				model = (IChemModel) parsedRes;
//			}else if(parsedRes instanceof IChemFile){
//				model = ((IChemFile)parsedRes).getChemSequence(0).getChemModel(0);
//			}
//			if(model != null){
//				StringWriter sout = new StringWriter();
//				try {
//					cmlWriter.setWriter(sout);
//				} catch (CDKException e) {
//					e.printStackTrace();
//					return false;
//				}
//				ac = model.getMoleculeSet().getAtomContainer(0);
//				mol = model.getBuilder().newMolecule(ac);
//				if(mol != null || mol.getAtomCount() != 0){
//					try {
//						cmlWriter.write(mol);
//					} catch (CDKException e) {
//						e.printStackTrace();
//						return false;
//					}
//				}
//				CMLBuilder builder = new CMLBuilder();
//				CMLElement cmlElement = null;
//				try {
//					cmlElement = (CMLElement)builder.parseString(sout.toString());
//				} catch (ValidityException e) {
//					e.printStackTrace();
//					return false;
//				} catch (ParsingException e) {
//					e.printStackTrace();
//					return false;
//				} catch (IOException e) {
//					e.printStackTrace();
//					return false;
//				}
//				((ParentNode)cml).appendChild(cmlElement.copy());
//				try {
//					cmlWriter.close();
//					sout.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//					return false;
//				}
//			}
//		}else{
//			return false;
//		}
//		if(spectraList.size() == 1){
//			IBioResource spectrumRes = (IBioResource) spectraList.get(0);
//			if(spectrumRes.getPersistedResource().load() && spectrumRes.parseResource()){
//				Object parsedRes = spectrumRes.getParsedResource();
//				if(parsedRes instanceof CMLSpectrum)
//					cmlSpectrum = (CMLSpectrum) parsedRes;
//
//			}else return false;
//		}else{
//			for(int i = 0; i < spectraList.size() ; i++){
//				IBioResource spectrumRes = (IBioResource) spectraList.get(i);
//				if(spectrumRes.getPersistedResource().load() && spectrumRes.parseResource()){
//					Object parsedRes = spectrumRes.getParsedResource();
//					if(parsedRes instanceof CMLSpectrum)
//						cml.appendChild((CMLSpectrum) parsedRes);
//					
//				}
//			}
//		}
		String path = selectFilePage.getPathStr();
		String fileName = selectFilePage.getFileName();
		String path_file = path+"/"+fileName;
		
		
		IMedeaManager manager =	(IMedeaManager)Activator.getDefault().getJavaManager();
	    manager.learnMassSpectrum(null, null, "");
		return true;
	}

	/*
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
//		computation.setSelection(selection);
	}
//	public Job getComputationJob() {
//		return computation;
//	}

}
