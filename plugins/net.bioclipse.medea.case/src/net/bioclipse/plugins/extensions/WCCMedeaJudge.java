package net.bioclipse.plugins.extensions;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import net.bioclipse.core.util.LogUtils;
import net.bioclipse.medea.core.Medea;
import net.bioclipse.seneca.judge.IJudge;
import net.bioclipse.seneca.judge.Judge;
import net.bioclipse.seneca.judge.JudgeResult;
import net.bioclipse.seneca.judge.MissingInformationException;
import net.bioclipse.seneca.judge.WCCNMRShiftDBJudge;
import net.bioclipse.spectrum.domain.IJumboSpectrum;
import net.bioclipse.spectrum.editor.SpectrumEditor;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;
import nu.xom.ParsingException;
import nu.xom.XPathContext;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
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

  private static Logger logger = Logger.getLogger(WCCNMRShiftDBJudge.class);

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
		if (!"MS".equals(cmlSpect.getType()) && !"massSpectrum".equals(cmlSpect.getType())) {
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

    public IJudge createJudge(IPath data) throws MissingInformationException {
        WCCMedeaJudge judge = new WCCMedeaJudge();
        judge.setData( data );
        CMLBuilder builder = new CMLBuilder();
        try {
            Document doc =  builder.buildEnsureCML(ResourcesPlugin.getWorkspace().getRoot().getFile( judge.getData()).getContents());
            SpectrumUtils.namespaceThemAll( doc.getRootElement().getChildElements() );
            doc.getRootElement().setNamespaceURI(CMLUtil.CML_NS);
            Element element = builder.parseString(doc.toXML());
            if(element instanceof CMLCml)
                judge.configure((CMLCml)element);
            else if(element instanceof CMLSpectrum){
                CMLCml cmlcml=new CMLCml();
                cmlcml.appendChild( element );
                judge.configure(cmlcml);
            }
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
            Element element = builder.parseString(doc.toXML());
            if(element instanceof CMLCml)
                configure((CMLCml)element);
            else if(element instanceof CMLSpectrum){
                CMLCml cmlcml=new CMLCml();
                cmlcml.appendChild( element );
                configure(cmlcml);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public IFile setData( ISelection selection, IFile sjsFile ) {
        IStructuredSelection ssel = (IStructuredSelection) selection;
        if(ssel.size()>1){
            MessageBox mb = new MessageBox(new Shell(), SWT.ICON_WARNING);
            mb.setText("Multiple Files");
            mb.setMessage("Only one file can be dropped on here!");
            mb.open();
            return null;
        }else{
            if (ssel.getFirstElement() instanceof IFile) {
                IFile file = (IFile) ssel.getFirstElement();
                IContentTypeManager contentTypeManager = Platform.getContentTypeManager();
                InputStream stream;
                try {
                    stream = file.getContents();
                    IContentType contentType = contentTypeManager.findContentTypeFor(stream, file.getName());
                    if(contentType.getId().equals( "net.bioclipse.contenttypes.jcampdx" ) ||  contentType.getId().equals( "net.bioclipse.contenttypes.cml.singleSpectrum")){
                        IJumboSpectrum spectrum=net.bioclipse.spectrum
                            .Activator.getDefault()
                            .getJavaSpectrumManager()
                            .loadSpectrum( file );
                        //if the file is somewhere else , we make a new file
                        IFile newFile;
                        if(file.getParent()!=sjsFile.getParent()){
                            IContainer folder = sjsFile.getParent();
                            newFile=folder.getFile( new Path(file.getName().substring( 0, file.getName().length()-1-file.getFileExtension().length() )+".cml") );
                            net.bioclipse.spectrum.Activator
                                .getDefault().getJavaSpectrumManager()
                                .saveSpectrum(
                                    spectrum, newFile,
                                    SpectrumEditor.CML_TYPE
                                );
                        }else{
                            newFile = file;
                        }
                        return newFile;
                    }else{
                        MessageBox mb = new MessageBox(new Shell(), SWT.ICON_WARNING);
                        mb.setText("Not a spectrum file");
                        mb.setMessage("Only a spectrum file (JCAMP or CML) can be dropped on here!");
                        mb.open();
                        return null;
                    }
                } catch ( Exception e ) {
                    LogUtils.handleException( e, logger );
                    return null;
                }
            }else{
                MessageBox mb = new MessageBox(new Shell(), SWT.ICON_WARNING);
                mb.setText("Not a file");
                mb.setMessage("Only a file (not directory etc.) can be dropped on here!");
                mb.open();
                return null;
            }
        }
    }
}
