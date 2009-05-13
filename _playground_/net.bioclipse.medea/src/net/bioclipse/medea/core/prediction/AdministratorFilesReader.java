package net.bioclipse.medea.core.prediction;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import net.bioclipse.medea.core.Fragmenter;
import net.bioclipse.medea.core.Medea;
import net.bioclipse.medea.core.learning.AdministratorFilesWriter;
import net.bioclipse.medea.core.reaction.ExtractorSetQsarsCE;
import net.bioclipse.medea.core.reaction.ExtractorSetQsarsHR;
import net.bioclipse.medea.core.reaction.ExtractorSetQsarsRSH;
import net.bioclipse.medea.core.reaction.ExtractorSetQsarsRSI;
import net.bioclipse.medea.core.reaction.ReactionKp;

import org.openscience.cdk.qsar.model.QSARModelException;
import org.openscience.chemojava.qsar.model.weka.J48WModel;

public class AdministratorFilesReader {
	
	String[] classAttrib = {"0_00","0_01","0_02","0_03","0_04","0_05","0_06","0_07","0_08","0_09",
			"0_10","0_11","0_12","0_13","0_14","0_15","0_16","0_17","0_18","0_19",
			"0_20","0_21","0_22","0_23","0_24","0_25","0_26","0_27","0_28","0_29",
			"0_30","0_31","0_32","0_33","0_34","0_35","0_36","0_37","0_38","0_39",
			"0_40","0_41","0_42","0_43","0_44","0_45","0_46","0_47","0_48","0_49",
			"0_50","0_51","0_52","0_53","0_54","0_55","0_56","0_57","0_58","0_59",
			"0_60","0_61","0_62","0_63","0_64","0_65","0_66","0_67","0_68","0_69",
			"0_70","0_71","0_72","0_73","0_74","0_75","0_76","0_77","0_78","0_79",
			"0_80","0_81","0_82","0_83","0_84","0_85","0_86","0_87","0_88","0_89",
			"0_90","0_91","0_92","0_93","0_94","0_95","0_96","0_97","0_98","0_99",
	};
	private ExtractorSetQsarsRSI extractorRSI;
	private ExtractorSetQsarsRSH extractorRSH;
	private ExtractorSetQsarsCE extractorCE;
	private ExtractorSetQsarsHR extractorHR;
	private HashMap<String, Double> hash;
	/**
	 * Constructor of the AdministratorFiles
	 *
	 */
	public AdministratorFilesReader() {
		extractorRSI = new ExtractorSetQsarsRSI();
		extractorRSH = new ExtractorSetQsarsRSH();
		extractorCE = new ExtractorSetQsarsCE();
		extractorHR = new ExtractorSetQsarsHR();
		hash = new HashMap<String, Double>();
		double value = 0.005;
		for(int i = 0 ; i < classAttrib.length ; i++){
			hash.put(classAttrib[i],new Double(value));
			value += 0.01;
		}
	}

	public BufferedInputStream getRsiFile() {
	    return new BufferedInputStream(
	        this.getClass().getClassLoader().getResourceAsStream(
	            "/data/total-rsi.arff"
	        )
	    );
	}
	
    public BufferedInputStream getRshFile() {
        return new BufferedInputStream(
            this.getClass().getClassLoader().getResourceAsStream(
                "/data/total-rsh.arff"
            )
        );
    }
    
    public BufferedInputStream getCeeFile() {
        return new BufferedInputStream(
            this.getClass().getClassLoader().getResourceAsStream(
                "/data/total-cee.arff"
            )
        );
    }
    
    public BufferedInputStream getHrgFile() {
        return new BufferedInputStream(
            this.getClass().getClassLoader().getResourceAsStream(
                "/data/total-hrg.arff"
            )
        );
    }
    
    public BufferedInputStream getHrdFile() {
        return new BufferedInputStream(
            this.getClass().getClassLoader().getResourceAsStream(
                "/data/total-hrd.arff"
            )
        );
    }
    
	/**
	 * creates the files 
	 * 
	 * @param reaction
	 * @param probability
	 * @throws IOException 
	 */
	public double getProbability(ReactionKp reaction){
		Double[][] results = null;
		ArrayList<Double> resultQ = null;
		InputStream table = null;
		System.out.println("getProbabilityName: "+reaction.getNameReaction());
		if(reaction.getNameReaction().equals("RadicalSiteInitiationReaction")){
		    table = getRsiFile();
		    resultQ = extractorRSI.getQsars(reaction);
		    results = new Double[1][resultQ.size()];

		    for(int i = 0 ; i < resultQ.size(); i++){
		        results[0][i] = resultQ.get(i);
		        //						System.out.println(i+", "+resultQ.get(i));
		    }
		}else if(reaction.getNameReaction().equals("RadicalSiteInitiationHReaction")){
		    table = getRshFile();
		    resultQ = extractorRSH.getQsars(reaction);
		    results = new Double[1][resultQ.size()];
		    for(int i = 0 ; i < resultQ.size(); i++){
		        results[0][i] = resultQ.get(i);
		        //						System.out.println(i+", "+resultQ.get(i));
		    }
		}else if(reaction.getNameReaction().equals("CarbonylEliminationReaction")){
		    table = getCeeFile();
		    resultQ = extractorCE.getQsars(reaction);
		    results = new Double[1][resultQ.size()];
		    for(int i = 0 ; i < resultQ.size(); i++){
		        results[0][i] = resultQ.get(i);
		        //						System.out.println(i+", "+resultQ.get(i));
		    }
		}else if(reaction.getNameReaction().equals("RadicalSiteHrDeltaReaction")){
		    table = getHrdFile();
		    resultQ = extractorHR.getQsars(reaction);
		    results = new Double[1][resultQ.size()];
		    for(int i = 0 ; i < resultQ.size(); i++){
		        results[0][i] = resultQ.get(i);
		        //						System.out.println(i+", "+resultQ.get(i));
		    }
		}else if(reaction.getNameReaction().equals("RadicalSiteHrGammaReaction")){
		    table = getHrgFile();
		    resultQ = extractorHR.getQsars(reaction);
		    results = new Double[1][resultQ.size()];
		    for(int i = 0 ; i < resultQ.size(); i++){
		        results[0][i] = resultQ.get(i);
		        //						System.out.println(i+", "+resultQ.get(i));
		    }
		} else {
		    System.out.println("**** MAJOR FAILURE ****");
		}
		
		if(results != null){
			if(Fragmenter.processType == Medea.LEARN_PREDPROCESS){
				double resultP = 0.0;
				if(AdministratorFilesWriter.hashL_P.containsKey(resultQ.toString()))
					resultP = ((Double)AdministratorFilesWriter.hashL_P.get(resultQ.toString())).doubleValue();
				return resultP;
			}else{
	//			System.out.println("path: "+path);
				J48WModel j48 = new J48WModel();
				String[] options = new String[4];
				options[0] = "-C";
				options[1] = "0.25";
				options[2] = "-M";
				options[3] = "2";
				try {
					j48.setOptions(options);
					j48.build(table);
		    		j48.setParameters(results);
		            j48.predict();
		    		String[] result = (String[])j48.getPredictPredicted();
	//	    		System.out.println("result["+result[0]);
		    		return ((Double)hash.get(result[0])).doubleValue();
		    		
				} catch (QSARModelException e) {
					e.printStackTrace();
				}
			}
		}

		return 0.0;
	}
}
