package net.bioclipse.plugins.actions;

import net.bioclipse.plugins.medea.core.Medea;
import net.bioclipse.plugins.wizards.PredictWizard;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.xmlcml.cml.element.CMLSpectrum;

/** 
 * Principal class of the MEDEA (the mass spectrum simulation) for Bioclipse.
 * It is necessary an IViewPart which will be used for initiate a wizard.
 * <pre>
 *  MedeaSWT medea = MedeaSWT.getInstance(IViewPart view);
 *  medea.predictMS(ac);
 * </pre>
 * 
 * @author Miguel Rojas
 * @see Medea
 *
 */
public class MedeaSWT extends Medea{
	private IViewPart view;
//	private IAtomContainer ac;
	/**
	 * Constructor of MedeaSWT object for Bioclipse.
	 * Default IViewPart = null, therefore won't be shown the wizard
	 * with the results.
	 */
	public MedeaSWT(){
		super();
	}
	/**
	 * Constructor of MedeaSWT object for Bioclipse.
	 *
	 * @param view The IViewPart necessary for the wizard which will appear once the calculation is finished
	 */
	public MedeaSWT(IViewPart vie){
		super();
		view = vie;
	}
	/**
	 * Run the process which learns the correlation between the mass spectrum (CMLSpectrum)
	 * and a IMolecule
	 * 
	 * @param ac The IMoleucle
	 * @param cmlSpectrum The CMLSpectrum
	 * @param nameFile The name of the file which will contain the information of the prediction
	 */
	public void learningsMSThread(IAtomContainer acNew, CMLSpectrum cmlSpectrum, String nameFile){
		learningMS(acNew, cmlSpectrum, nameFile);
		showWizard();
	}
	/**
	 * Run the process which will be simulated the mass spectru of a molecule.
	 * 
	 * @param acNew The IAtomContainer to simulate its mass spectrum
	 */
	public void predictMSThread(IAtomContainer acNew) {
//		this.ac = acNew;
//		IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress() {
//	          public void run(IProgressMonitor monitor)
//	            throws InvocationTargetException, InterruptedException {
//	            monitor.beginTask("Number of fragments found ", 10);
//	            final Thread applicationThread = new Thread("applicationThread") {
//	  		      public void run() {
	  		    	  predictMS(acNew);
//	  		      }
//	            };
//	            applicationThread.start();
//	            for(int i=0; i < 100; i++) {
//	            	if(i == 0)
//	            		Thread.sleep(2000);
//		            monitor.setTaskName("Number of fragments found = "+getController().numberOfFragments());
//	              if(monitor.isCanceled()) {
//	                monitor.done();
//	                return;
//	              }
//	              
//	              monitor.worked(1);
//	              Thread.sleep(500); // 0.5s.
//	              
//	              if(isTaskFinalized()){
//	            	  monitor.done();
//	            	  return;
//	              }
//	            }
//	            monitor.done();
//	          }
//	        };
//	        
//	        ProgressMonitorDialog dialog = new ProgressMonitorDialog(new Shell());
//	        try {
//	          dialog.run(true, true, runnableWithProgress);
//	        } catch (InvocationTargetException e) {
//	        	System.err.println("error1 InvocationTargetException");
//	          e.printStackTrace();
//	        } catch (InterruptedException e) {
//	        	System.err.println("error2 InterruptedException");
//	          e.printStackTrace();
//	        }
//	        
//
//	  		/*only will be iniziated if has been added the IView object*/
//	        if(isTaskFinalized())
	        	if(view != null)
	        		showWizard();
	}
	/**
	 * show a Wizard with the predicted mass spectrum.
	 *
	 */
	public void showWizard(){
		PredictWizard predwiz = new PredictWizard(this);
		WizardDialog wd = new WizardDialog( new Shell(), predwiz );
		wd.open();
	}
	
	/**
	 * get the IViewPart object
	 * 
	 * @return The IViewPart
	 */
	public IViewPart getIViewPart() {
		return view;
	}
}
