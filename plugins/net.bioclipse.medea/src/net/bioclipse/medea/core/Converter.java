package net.bioclipse.medea.core;

import java.util.ArrayList;
import java.util.List;

import net.bioclipse.reaction.domain.ICDKReactionScheme;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import org.openscience.cdk.tools.manipulator.ReactionSchemeManipulator;
import org.xmlcml.cml.element.CMLPeak;
import org.xmlcml.cml.element.CMLPeakList;
import org.xmlcml.cml.element.CMLSpectrum;

public class Converter {
	public static CMLSpectrum toSpectrum(ICDKReactionScheme reactionScheme){
		CMLSpectrum cmlSpectrum = new CMLSpectrum();
		cmlSpectrum.setType("massSpectrum");
		CMLPeakList peakList = new CMLPeakList();
		
		List<Double> listPeaks = new ArrayList<Double>();
		IMoleculeSet reactionList = ReactionSchemeManipulator.getAllMolecules(reactionScheme.getReactionScheme());
		CDKHydrogenAdder cdkH = CDKHydrogenAdder.getInstance(reactionList.getBuilder());
		for(IAtomContainer ac:reactionList.atomContainers()){
			double abundance = 100.0;
			try {
				AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(ac);
	            cdkH.addImplicitHydrogens(ac);
				AtomContainerManipulator.convertImplicitToExplicitHydrogens(ac);
	    		
			} catch (CDKException e) {
				e.printStackTrace();
			}
			double mass = MolecularFormulaManipulator.getMajorIsotopeMass(MolecularFormulaManipulator.getMolecularFormula(ac));
			System.out.println(MolecularFormulaManipulator.getHillString(MolecularFormulaManipulator.getMolecularFormula(ac)) +" "+mass);
			if(listPeaks.contains(mass))
				continue;
			else
				listPeaks.add(mass);
			
			CMLPeak peak = new CMLPeak();
			peak.setXUnits("jcampdx: m/z");
			peak.setYUnits("jcampdx: RELATIVE ABUNDANCE");
			peak.setXValue(mass);
			peak.setYValue(abundance);
			peakList.addPeak(peak);
			
		}
		cmlSpectrum.addPeakList(peakList);
		return cmlSpectrum;
	}
}
