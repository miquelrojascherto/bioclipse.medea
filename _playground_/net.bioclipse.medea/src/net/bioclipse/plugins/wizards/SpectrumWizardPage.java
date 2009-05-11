package net.bioclipse.plugins.wizards;



import java.awt.BorderLayout;
import java.awt.Frame;
import java.text.DecimalFormat;
import java.util.List;

import net.bioclipse.plugins.medea.core.Medea;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.jfree.chart.JFreeChart;
import org.xmlcml.cml.base.CMLElements;
import org.xmlcml.cml.element.CMLPeak;
import org.xmlcml.cml.element.CMLPeakList;
import org.xmlcml.cml.element.CMLSpectrum;

import spok.gui.SpectrumChartFactory;
import spok.gui.SpokChartPanel;
/**
 * 
 * @author Miguel Rojas
 */
public class SpectrumWizardPage extends WizardPage {
	
	private Button yes1;
	private Label text1;
	protected boolean loadPerspective = true;

	/**
	 * Constructor for SpectrumWizardPage object.
	 * 
	 */
	public SpectrumWizardPage() {
		super("SpectrumWizardPage");
		setTitle("Prediction of a Mass Spectrum");
		this.setDescription("This wizard shows the predicted Mass Spectrum" +
				" from the MEDEA System given a compound.");
	}
	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		try{
			CMLSpectrum cmlExp = null;
			
			PredictWizard pw = ((PredictWizard)this.getWizard());
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			
			Composite composite = new Composite(parent, SWT.NONE);
			if(pw.getMedeaClass().getProcessType() == Medea.LEARNINGPROCESS)
				composite.setLayout(new GridLayout(5,false));
			else
				composite.setLayout(new GridLayout(4,false));
			
			if(pw.getMedeaClass().getProcessType() == Medea.LEARNINGPROCESS){
			
				Composite graph = new Composite(composite,SWT.EMBEDDED);
				GridData gridData = new GridData(SWT.FILL,SWT.FILL,true,true);
			    gridData.horizontalSpan = 4;
			    graph.setLayoutData(gridData);
			    
			    /*Experimental Spectrum*/
			    Frame  fileTableFrame1 = SWT_AWT.new_Frame(graph);
				fileTableFrame1.setLayout(new BorderLayout());
				JFreeChart chart1 = SpectrumChartFactory.createPeakChart(null,page);
				chart1.setTitle("empty chart");
				SpokChartPanel chartPanel1 = new SpokChartPanel(chart1, "peak", null);
				
				cmlExp = pw.getMedeaClass().getExperimentalSpectrum();
				cmlExp = setPoints(cmlExp);
					

				chartPanel1.setSpectrum(cmlExp, page);
				fileTableFrame1.add(chartPanel1,  BorderLayout.CENTER);
			    
				/*with predicted intensity*/
				Composite graph2 = new Composite(composite,SWT.EMBEDDED);
				GridData gridData2 = new GridData(SWT.FILL,SWT.FILL,true,true);
			    gridData2.horizontalSpan = 4;
			    graph2.setLayoutData(gridData2);
			    Frame  fileTableFrame2 = SWT_AWT.new_Frame(graph2);
				fileTableFrame2.setLayout(new BorderLayout());
				PredictWizard pw2 = ((PredictWizard)this.getWizard());
				
				JFreeChart chart2 = SpectrumChartFactory.createPeakChart(null,page);
				chart2.setTitle("empty chart");
				SpokChartPanel chartPanel2 = new SpokChartPanel(chart2, "peak", null);
				
				CMLSpectrum cmlPred = pw2.getMedeaClass().getPredictedTSpectrum();
				cmlPred = setPoints(cmlPred);
				
				
				chartPanel2.setSpectrum(cmlPred, page);
				fileTableFrame2.add(chartPanel2,  BorderLayout.CENTER);
				
				/*score similarity*/
				Label text_2 = new Label(composite, SWT.LEFT);
				int numEP = cmlExp.getPeakListElements().get(0).getPeakElements().size()-2;
				int numPP = cmlPred.getPeakListElements().getList().get(0).getPeakElements().size()-2;
			    text_2.setText("N° peaks = "+numPP+"/"+numEP);
			    
			}
		    
		    /*with predicted intensity*/
			Composite graph3 = new Composite(composite,SWT.EMBEDDED);
			GridData gridData3 = new GridData(SWT.FILL,SWT.FILL,true,true);
		    gridData3.horizontalSpan = 4;
		    graph3.setLayoutData(gridData3);
		    Frame  fileTableFrame3 = SWT_AWT.new_Frame(graph3);
			fileTableFrame3.setLayout(new BorderLayout());
			PredictWizard pw3 = ((PredictWizard)this.getWizard());
			
			JFreeChart chart3 = SpectrumChartFactory.createPeakChart(null,page);
			chart3.setTitle("empty chart");
			SpokChartPanel chartPanel3 = new SpokChartPanel(chart3, "peak", null);
			
			CMLSpectrum cmlPred = pw3.getMedeaClass().getPredictedSpectrum();
			cmlPred = setPoints(cmlPred);
			
			chartPanel3.setSpectrum(cmlPred, page);
			fileTableFrame3.add(chartPanel3,  BorderLayout.CENTER);
			
			/*score similarity*/
			if(pw.getMedeaClass().getProcessType() == Medea.LEARNINGPROCESS){
				Label text_3 = new Label(composite, SWT.LEFT);
				DecimalFormat df = new DecimalFormat("#.###");
				String formattedMyNumber = df.format(calculationScore(cmlPred,cmlExp));
				
				text_3.setText("Score = "+formattedMyNumber+" %");
			}
			
		    /* 1 - perspective*/
			text1 = new Label(composite, SWT.LEFT);
		    text1.setText("Should the Medea perspective be opened?");
		    Composite yesNo3 = new Composite(composite, SWT.NONE);
		    yesNo3.setLayout(new FillLayout(SWT.VERTICAL));
		    
		    yes1 = new Button(yesNo3, SWT.CHECK);
		    yes1.setSelection(true);
		    yes1.addSelectionListener(new SelectionListener(){
		    	public void widgetSelected(SelectionEvent e) {
		    		if(yes1.getSelection())
		    			loadPerspective = true;
		    		else
		    			loadPerspective = false;
		    	}
		          public void widgetDefaultSelected(SelectionEvent e) {
		          }
		    });

		    setPageComplete(true);
		    setControl(composite);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
			
	}
	/**
	 * 
	 * @return True, if the perspective must be loaded
	 */
	public boolean loadPerspective() {
		return loadPerspective;
	}
	
	/**
	 * return the cmlSpectrum with the points 0 and 150. That makes that all spectra
	 * will have the same scalation
	 * 
	 * @param cmlSpectrum
	 * @return
	 */
	private CMLSpectrum setPoints(CMLSpectrum cmlSpectrum){
		CMLSpectrum cmlExp = cmlSpectrum;
		List<CMLPeakList> peakList = cmlExp.getPeakListElements().getList();
		CMLPeak peak_I = new CMLPeak();
		peak_I.setXValue(0);
		peak_I.setYValue(0);
		CMLPeak peak_F = new CMLPeak();
		peak_F.setXValue(150);
		peak_F.setYValue(0);
		peakList.get(0).addPeak(peak_I);
		peakList.get(0).addPeak(peak_F);
		return cmlSpectrum;
	}
	
	/**
	 * return the score of two differents spectra
	 * 
	 * @param cmlSpectrum1 CMLSpectrum 1: Predicted
	 * @param cmlSpectrum2 CLMSpectrum 2: Experimental
	 * @return The score results
	 */
	private double calculationScore(CMLSpectrum cmlSpectrum1, CMLSpectrum cmlSpectrum2){
		double maxScore = 1000; // Score for optimum fit of exp. with calc. shift
		
		/*1*/
		CMLElements<CMLPeak> peaks = cmlSpectrum1.getPeakListElements().get(0).getPeakElements();
		double[] predPeakPos = new double[peaks.size()];
		double[] predPeakInt = new double[peaks.size()];
		for (int peakNo=0; peakNo<peaks.size(); peakNo++) {
			CMLPeak peak = peaks.get(peakNo);
			predPeakPos[peakNo] = peak.getXValue();
			predPeakInt[peakNo] = peak.getYValue();
		}
		
		/*2*/
		peaks = cmlSpectrum2.getPeakListElements().get(0).getPeakElements();
		double[] msPeakPos = new double[peaks.size()];
		double[] msPeakInt = new double[peaks.size()];
		for (int peakNo=0; peakNo<peaks.size(); peakNo++) {
			CMLPeak peak = peaks.get(peakNo);
			msPeakPos[peakNo] = peak.getXValue();
			msPeakInt[peakNo] = peak.getYValue();
		}
		double scoreSum = maxScore*WCCTool.wcc(msPeakPos, msPeakInt, predPeakPos, predPeakInt, 2);
		
		return scoreSum/1000;
	}
}
	