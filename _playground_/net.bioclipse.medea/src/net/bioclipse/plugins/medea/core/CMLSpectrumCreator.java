package net.bioclipse.plugins.medea.core;

import java.util.ArrayList;

import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.tools.MFAnalyser;
import org.xmlcml.cml.element.CMLPeak;
import org.xmlcml.cml.element.CMLPeakList;
import org.xmlcml.cml.element.CMLSpectrum;

/**
 * Class which searchs abundance of each peaks (sum of the fragment's 
 * abundance) and creates a CMLSpectrum from the FragmentTree.
 * 
 * @author Miguel Rojas
 */
class CMLSpectrumCreator {
	
	private CMLSpectrum cmlSpectrum;
	/**
	 * Constructor of the CMLSpectrumCreator object
	 * 
	 * @param process The process
	 * @param fragmentTree The FragmentTree object
	 */
	CMLSpectrumCreator(int process, FragmentTree fragmentTree){
		/*obtain the mass of the arrayList which contains the ionized fragments*/
		IMolecule molecule = fragmentTree.getMolecule();
		MFAnalyser mfAnalyser = new MFAnalyser(molecule);
		int massI = Math.round(mfAnalyser.getMass());
		
		cmlSpectrum = new CMLSpectrum();
		cmlSpectrum.setType("massSpectrum");
		CMLPeakList peakList = new CMLPeakList();
		
		FragmentTreeSub fts = fragmentTree.getFragments(massI);
		
		for(int i = 0; i <= massI ; i++){
			fts = fragmentTree.getFragments(i);

			if(fts.size() > 0){
				double abundance = 0.0;
				if(process == Medea.LEARNINGPROCESS)
					abundance = 100.0;
				else{
					/* only will be summed the ionized fragments and not the rearrangements*/
					int numbIonized = 0;
					if(i == massI ){
						ArrayList<String> list = fts.get(0).getProcess();
						for(int k = 0 ; k < list.size(); k++)
							if(list.get(k).equals("Ionitzation"))
								numbIonized++;
							
					}

					/*sum the abundance of all fragments with have the same m/c */
					for(int j = 0 ; j < fts.size(); j++){
						if(i == massI )
							if(j < numbIonized)
								break;
						abundance = abundance + fts.get(j).getAbundance();
					}
				}
				if(abundance == 0.0)
					continue;
				
				CMLPeak peak = new CMLPeak();
				peak.setXUnits("jcampdx: m/z");
				peak.setYUnits("jcampdx: RELATIVE ABUNDANCE");
				peak.setXValue(i);
				peak.setYValue(abundance*10);
				peakList.addPeak(peak);
			}
		}

		cmlSpectrum.addPeakList(peakList);
	}
	/**
	 * get the simulated CMLSpectrum
	 *
	 * @return The CMLSpectrum object
	 */
	CMLSpectrum getCMLSpectrum() {
		return cmlSpectrum;
	}
}
