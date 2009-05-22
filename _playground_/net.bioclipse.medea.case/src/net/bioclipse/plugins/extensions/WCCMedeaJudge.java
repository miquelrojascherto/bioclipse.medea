package net.bioclipse.plugins.extensions;

import java.io.IOException;
import java.io.Serializable;

import net.bioclipse.medea.core.Medea;
import net.bioclipse.seneca.judge.IJudge;
import net.bioclipse.seneca.judge.Judge;
import net.bioclipse.seneca.judge.JudgeResult;
import net.bioclipse.seneca.judge.MissingInformationException;
import nu.xom.Document;
import nu.xom.Nodes;
import nu.xom.ParsingException;
import nu.xom.XPathContext;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.xmlcml.cml.base.CMLBuilder;
import org.xmlcml.cml.base.CMLElement;
import org.xmlcml.cml.base.CMLElements;
import org.xmlcml.cml.base.CMLUtil;
import org.xmlcml.cml.element.CMLCml;
import org.xmlcml.cml.element.CMLPeak;
import org.xmlcml.cml.element.CMLSpectrum;

import spok.utils.SpectrumUtils;

public class WCCMedeaJudge extends Judge implements IJudge, Serializable, Cloneable {

	public double maxScore = 1000; // Score for optimum fit of exp. with calc. shift
	private final static Medea medeaTool = new Medea();
	
	private double[] msPeakInt; 
	private double[] msPeakPos; 

	private int count = 0;
	
	public WCCMedeaJudge() {
		super("WCC Medea MS spectrum Judge");
		count = 0;
	}

	private static final long serialVersionUID = 2735700647109322487L;

	public void calcMaxScore() {
		// nothing to do, 1000 is fine
	}

	public boolean[][][] getAssignment()
	{
		return null;
	}

	public void configure(CMLElement input) throws MissingInformationException {
		if (!(input instanceof CMLCml)) {
			throw new MissingInformationException("Root element must be <cml>!");
		}
		CMLCml root = (CMLCml)input;
		
		String CML_NAMESPACE = "http://www.xml-cml.org/schema";
		XPathContext context = new XPathContext("cml", CML_NAMESPACE);
		Nodes result = root.query("./cml:spectrum", context);
		if (result.size() != 1) {
			throw new MissingInformationException("Expected one and only one <spectrum> element.");
		}
		
		CMLSpectrum cmlSpect = (CMLSpectrum)result.get(0);
		if (!"MS".equals(cmlSpect.getType())) {
			throw new MissingInformationException("Spectrum is not of type MS.");
		}
		if (cmlSpect.getPeakListElements() == null) {
			throw new MissingInformationException("No peaks are defined!");
		}
		CMLElements<CMLPeak> peaks = cmlSpect.getPeakListElements().get(0).getPeakElements();
		msPeakPos = new double[peaks.size()];
		msPeakInt = new double[peaks.size()];
		for (int peakNo=0; peakNo<peaks.size(); peakNo++) {
			CMLPeak peak = peaks.get(peakNo);
			msPeakPos[peakNo] = peak.getXValue();
			msPeakInt[peakNo] = peak.getYValue();
		}
	}

	public JudgeResult evaluate(IAtomContainer ac) throws Exception {
		IMolecule mol = ac.getBuilder().newMolecule(ac);
		System.out.print(count+" smi: " + new SmilesGenerator().createSMILES(mol));
		count++;
		// FIXME: need to do atom type perception first.
		CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(ac.getBuilder());
		adder.addImplicitHydrogens(mol);
		try {
			medeaTool.predictMS(mol);
			System.out.println();
		} catch (NullPointerException exception) {
			//ok, probably a ring system ;)
			System.out.println(" .. failed");
			return new JudgeResult((long)maxScore,0l, 0l, "NPE happened: " + exception.getMessage());
		}
		CMLSpectrum spectrum = medeaTool.getPredictedSpectrum();
		CMLElements<CMLPeak> peaks = spectrum.getPeakListElements().get(0).getPeakElements();
		double[] predPeakPos = new double[peaks.size()];
		double[] predPeakInt = new double[peaks.size()];
		for (int peakNo=0; peakNo<peaks.size(); peakNo++) {
			CMLPeak peak = peaks.get(peakNo);
			predPeakPos[peakNo] = peak.getXValue();
			predPeakInt[peakNo] = peak.getYValue();
		}
		
		double scoreSum = maxScore*net.bioclipse.spectrum.Activator
		    .getDefault().getJavaSpectrumManager().calculateSimilarityWCC(
		        msPeakPos, msPeakInt, predPeakPos, predPeakInt, 2
		     );
		String message = "Score: " + scoreSum  + "/" + maxScore;
		return new JudgeResult((long)maxScore,(long)scoreSum, 0l, message);
	}

	public void init() {
		// nothing to do
	}

    public IJudge createJudge(String data) throws MissingInformationException {
        IJudge judge = new WCCMedeaJudge();
        judge.setData( data );
        CMLBuilder builder = new CMLBuilder();
        try {
            Document doc =  builder.buildEnsureCML(ResourcesPlugin.getWorkspace().getRoot().getFile( new Path(judge.getData())).getContents());
            SpectrumUtils.namespaceThemAll( doc.getRootElement().getChildElements() );
            doc.getRootElement().setNamespaceURI(CMLUtil.CML_NS);
            CMLElement cmlElement = (CMLCml) builder.parseString(doc.toXML());
            judge.configure(cmlElement);
        } catch (IOException e) {
            throw new MissingInformationException("Could not read the cmlString.");
        } catch (ParsingException e) {
            throw new MissingInformationException(
                    "Could not parse the cmlString; " + e.getMessage()
            );
        } catch ( CoreException e ) {
            throw new MissingInformationException(e.getMessage());
        }
        judge.setEnabled(super.getEnabled());
        return judge;
    }

    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean checkJudge( String data ) {
        CMLBuilder builder = new CMLBuilder();
        try {
            Document doc =  builder.buildEnsureCML(ResourcesPlugin.getWorkspace().getRoot().getFile( new Path(data)).getContents());
            SpectrumUtils.namespaceThemAll( doc.getRootElement().getChildElements() );
            doc.getRootElement().setNamespaceURI(CMLUtil.CML_NS);
            CMLElement cmlElement = (CMLCml) builder.parseString(doc.toXML());
            configure(cmlElement);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
