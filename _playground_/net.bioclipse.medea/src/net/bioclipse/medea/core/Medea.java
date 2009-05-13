package net.bioclipse.medea.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.bioclipse.medea.core.learning.ExtractorProbability;
import net.bioclipse.medea.core.prediction.ExtractorAbundance;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.interfaces.IMolecule;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.element.CMLPeak;
import org.xmlcml.cml.element.CMLSpectrum;

import spok.utils.SpectrumUtils;

/** 
 * Principal class of the MEDEA (the mass spectrum simulation)
 * <pre>
 *  Medea medea = new Medea();
 *  medea.predictMS(ac);
 * </pre>
 * 
 * @author Miguel Rojas
 *
 */
public class Medea {
	
	/** AtomContainer parsed and which is simulated its mass spectrum */
	private IAtomContainer ac;
	/** CMLSpectrum which contains the final simulated spectrum*/
	private CMLSpectrum cmlSpectrum = null;
	/** CMLSpectrum which contains the final simulated spectrum without the
	 * the calculation of the abundance for each peak*/
	private CMLSpectrum cmlTSpectrum = null;
	/** CMLSpectrum which contains the final experimental spectrum*/
	private CMLSpectrum cmlExpSpectrum = null;
	/** IChemFile that contains the IReactionSet*/
	private IChemFile chemFileReaction = null;
	
	private boolean taskFinalized = false;
	private Fragmenter fController;
	private ArrayList<Double> peaksX;
	private double[] peaksY;
	private String nameFile;

	/*process to realize*/
	private int process;
	public static final int PREDICTPROCESS = 0;
	public static final int LEARNINGPROCESS = 1;
	public static final int LEARN_PREDPROCESS = 2;
	/**
	 * Constructor of Medea object. 
	 *
	 */
	public Medea(){	}
	
	/**
	 * get the atomContainer which is predicted its mass spectrum
	 * 
	 * @return The IAtomContainer which is predicted its mass spectrum
	 */
	public IAtomContainer getAtomContainer(){
		return ac;
	}
	/**
	 * Run the process of learing from a IAtomContainer and its experimental.
	 * The only difference with the method of predictMS is that it has to be added
	 * experimental spectrum.
	 * 
	 * @param acNew The IAtomConatiner
	 * @param cmlSpectrum the CMLSpectrum
	 */
	public void learningMS(IAtomContainer acNew, CMLSpectrum cmlSpectrum, String nameFile){
		if(acNew != null && cmlSpectrum != null){
			this.nameFile = nameFile;
			cmlExpSpectrum = cmlSpectrum;
			/* creates a ArrayList with all peaks X and Y*/
			List<CMLElement> peaks = SpectrumUtils.getPeakElements(cmlSpectrum);
			int numP = peaks.size()-1;
			Double massI = new Double(((CMLPeak)peaks.get(numP)).getXValue());
			peaksX = new ArrayList<Double>();
			peaksY = new double[massI.intValue()+10];/* the spectrum will be bigger +10 m/e*/
			Iterator it = peaks.iterator();
			while(it.hasNext()){
				CMLPeak peak = (CMLPeak) it.next();
				if(peak.getXValue() != 0.0){
					peaksX.add(peak.getXValue());
					System.out.println(peak.getXValue());
					peaksY[(int)peak.getXValue()] = peak.getYValue();
				}else
					peaksY[(int)peak.getXValue()] = -1.0;
			}
			
			try {
				predictMS(acNew,peaksX,peaksY);
			} catch (CDKException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Run the process which will be simulated the mass spectrum of a molecule.
	 * 
	 * @param acNew The IAtomContainer to predict its mass spectrum
	 */
	public void predictMS(IAtomContainer acNew){
		try {
			predictMS(acNew, null, null);
		} catch (CDKException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Run the process which will be simulated the mass spectrum of a molecule.
	 * 
	 * @param acNew  The IAtomContainer to predict its mass spectrum
	 * @param peaksX An ArrayList with the peaks X
	 * @param peaksY An Array with the peaks Y
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 * @throws CDKException 
	 */
	@SuppressWarnings("static-access")
	private void predictMS(IAtomContainer acNew, ArrayList<Double> peaksX, double[] peaksY) throws CDKException, IOException, ClassNotFoundException{
		if(ac != acNew){
			this.ac = acNew;
			
			if(peaksX != null)
				process = LEARNINGPROCESS;
			else{
				process = PREDICTPROCESS;
			}
			
			if(ac instanceof IMolecule){
				fController = new Fragmenter(process,(IMolecule)ac,peaksX);
					
				FragmentTree fragmentTree = fController.getFragmentTree();
				if(process == LEARNINGPROCESS){
					ExtractorProbability.setProbabilities(fragmentTree,peaksY,nameFile);
					CMLSpectrumCreator spectrumCreator = new CMLSpectrumCreator(LEARNINGPROCESS, fragmentTree);
					cmlTSpectrum = spectrumCreator.getCMLSpectrum();
					
					
					/* made 2 times the process comparing the results */
					fController = new Fragmenter(LEARN_PREDPROCESS,(IMolecule)ac,null);
					fragmentTree = fController.getFragmentTree();
				}
//				else
					ExtractorAbundance.setAbundace(fragmentTree,null);

				CMLSpectrumCreator spectrumCreator = new CMLSpectrumCreator(PREDICTPROCESS, fragmentTree);
				cmlSpectrum = spectrumCreator.getCMLSpectrum();
				
				CMLReactionCreator reactionCreator = new CMLReactionCreator(fragmentTree);
				chemFileReaction = reactionCreator.getChemFile();
			}
		}
		taskFinalized  = true;
	}
	
	/**
	 * get the predicted spectrum
	 * 
	 * @return The predicted CMLSpectrum
	 */
	public CMLSpectrum getPredictedSpectrum(){
		return cmlSpectrum;
	}
	/**
	 * get the predicted spectrum but without the calculation of their abundance of 
	 * each peak. This method makes only sense in the LEARNINGPROCESS.
	 * 
	 * @return The predicted CMLSpectrum
	 */
	public CMLSpectrum getPredictedTSpectrum(){
		return cmlTSpectrum;
	}
	/**
	 * get the experimetal spectrum. If it is in LEARNINGPROCESS but will be null.
	 * 
	 * @return The experimental CMLSpectrum
	 */
	public CMLSpectrum getExperimentalSpectrum(){
		return cmlExpSpectrum;
	}

	/**
	 * get the ChemFile that contains the IReactionSet
	 *
	 * @return The IChemFile object
	 */
	public IChemFile getChemFileReaction() {
		return chemFileReaction;
	}
	/**
	 * get if task is finalized.
	 * 
	 * @return True, if the task if finalized
	 */
	public boolean isTaskFinalized(){
		return taskFinalized;
	}
	/**
	 * get the FragmentController
	 * 
	 * @return The Fragmenter
	 */
	public Fragmenter getController(){
		return fController;
	}
	/**
	 * get the process type(LEARNINGPROCESS or PREDICTPROCESS)
	 * @return
	 */
	public int getProcessType(){
		return process;
	}
}
