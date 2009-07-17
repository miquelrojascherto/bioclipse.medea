package net.bioclipse.medea.core.learning;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import net.bioclipse.medea.core.reaction.ExtractorSetQsarsCE;
import net.bioclipse.medea.core.reaction.ExtractorSetQsarsHR;
import net.bioclipse.medea.core.reaction.ExtractorSetQsarsRSH;
import net.bioclipse.medea.core.reaction.ExtractorSetQsarsRSI;
import net.bioclipse.medea.core.reaction.ReactionKp;

public class AdministratorFilesWriter {
	
	public static HashMap<String, Double> hashL_P = null;
	
	private ExtractorSetQsarsRSI eSQ_RSI;
	private ExtractorSetQsarsRSH eSQ_RSH;
	private ExtractorSetQsarsCE eSQ_CE;
	private ExtractorSetQsarsHR eSQ_HR;
	/**RadicalSiteInitiation reaction*/
	private FileWriter out1 = null;
	/**RadicalSiteInitiationH reaction*/
	private FileWriter out2 = null;
	/**CarbonylElimination reaction*/
	private FileWriter out3 = null;
	/**HydrogenRearrangementGamma reaction*/
	private FileWriter out4 = null;
	/**HydrogenRearrangementDelta reaction*/
	private FileWriter out5 = null;
	private String nameFile;
	String[] classe = {"0_00","0_01","0_02","0_03","0_04","0_05","0_06","0_07","0_08","0_09",
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
	/**
	 * Constructor of the AdministratorFiles
	 *
	 */
	public AdministratorFilesWriter(String nameFile){
		this.nameFile = nameFile;
		hashL_P = new HashMap<String, Double>();
		eSQ_RSI = new ExtractorSetQsarsRSI();
		eSQ_RSH = new ExtractorSetQsarsRSH();
		eSQ_CE = new ExtractorSetQsarsCE();
		eSQ_HR = new ExtractorSetQsarsHR();
	}
	/**
	 * creates the files 
	 * 
	 * @param reaction
	 * @param probability
	 * @throws IOException 
	 */
	public void addReaction(ReactionKp reaction) throws IOException {
		ArrayList<Double> results = null;
		if(reaction.getNameReaction().equals("RadicalSiteInitiation")){
			if(out1 == null){
				out1 = new FileWriter(new File(nameFile+"-rsi.arff"));
				out1.write("@relation  "+reaction.getNameReaction()+" \n");
				out1.write("@attribute PartialSigmaChargeDescriptor_PB numeric \n");
				out1.write("@attribute PartialPiChargeDescriptor_PB numeric \n");
				out1.write("@attribute EffectiveAtomPolarizabilityDescriptor_PB numeric \n");
				out1.write("@attribute PartialSigmaChargeDescriptor_PA numeric \n");
				out1.write("@attribute PartialPiChargeDescriptor_PA numeric \n");
				out1.write("@attribute EffectiveAtomPolarizabilityDescriptor_PA numeric \n");
				out1.write("@attribute StructureResonanceCount_PA numeric \n");
				out1.write("@attribute NumberCarbons_PA numeric \n");
				out1.write("@attribute SigmaElectronegativityDescriptor_R numeric \n");
				out1.write("@attribute LonePairCount_R numeric \n");
				out1.write("@attribute ConnectedAtomsCount_R numeric \n");
				out1.write("@attribute OrderBond_R numeric \n");

				
				out1.write("@attribute class {");
				for(int i = 0 ; i < classe.length ; i++){
					if(i != 0)out1.write(",");
					out1.write(classe[i]);
				}
				out1.write("} \n");
				out1.write("@data \n");
			} 
			
			results = eSQ_RSI.getQsars(reaction);
			for(int i = 0 ; i < results.size() ; i++){
				if(results.get(i) != null){
//					System.out.println("re: "+results.get(i));
					out1.write(extractDeci(results.get(i))+", ");
				}
				else
					out1.write("null");
			}

			out1.write(extractClass(reaction.getProbability())+", ");
			out1.write("\n");
			
		} else if(reaction.getNameReaction().equals("RadicalSiteInitiationH")){
			if(out2 == null){
				out2 = new FileWriter(new File(nameFile+"-rsh.arff"));
				out2.write("@relation  "+reaction.getNameReaction()+" \n");
				out2.write("@attribute PartialSigmaChargeDescriptor_R numeric \n");
				out2.write("@attribute PartialPiChargeDescriptor_R numeric \n");
				out2.write("@attribute EffectiveAtomPolarizabilityDescriptor_R numeric \n");
				
				out2.write("@attribute class {");
				for(int i = 0 ; i < classe.length ; i++){
					if(i != 0)out2.write(",");
					out2.write(classe[i]);
				}
				out2.write("} \n");
				out2.write("@data \n");
			}
			results = eSQ_RSH.getQsars(reaction);
			for(int i = 0 ; i < results.size() ; i++){
				if(results.get(i) != null)
					out2.write(extractDeci(results.get(i))+", ");
				else
					out2.write("null");
			}

			out2.write(extractClass(reaction.getProbability())+", ");
			out2.write("\n");
		} else if(reaction.getNameReaction().equals("CarbonylElimination")){
			if(out3 == null){
				out3 = new FileWriter(new File(nameFile+"-cee.arff"));
				out3.write("@relation  "+reaction.getNameReaction()+" \n");
				out3.write("@attribute PartialSigmaChargeDescriptor_PA numeric \n");
				out3.write("@attribute PartialPiChargeDescriptor_PA numeric \n");
				out3.write("@attribute EffectiveAtomPolarizabilityDescriptor_PA numeric \n");
				
				out3.write("@attribute class {");
				for(int i = 0 ; i < classe.length ; i++){
					if(i != 0)out3.write(",");
					out3.write(classe[i]);
				}
				out3.write("} \n");
				out3.write("@data \n");
			}
			results = eSQ_CE.getQsars(reaction);
			for(int i = 0 ; i < results.size() ; i++){
				if(results.get(i) != null)
					out3.write(extractDeci(results.get(i))+", ");
				else
					out3.write("null");
			}

			out3.write(extractClass(reaction.getProbability())+", ");
			out3.write("\n");
		}
		else if(reaction.getNameReaction().equals("HydrogenRearrangementGamma")){
			if(out4 == null){
				out4 = new FileWriter(new File(nameFile+"-hrg.arff"));
				out4.write("@relation  "+reaction.getNameReaction()+" \n");
				out4.write("@attribute PartialSigmaChargeDescriptor_RA numeric \n");
				out4.write("@attribute PartialPiChargeDescriptor_RA numeric \n");
				out4.write("@attribute EffectiveAtomPolarizabilityDescriptor_RA numeric \n");
				out4.write("@attribute PartialSigmaChargeDescriptor_PA numeric \n");
				out4.write("@attribute PartialPiChargeDescriptor_PA numeric \n");
				out4.write("@attribute EffectiveAtomPolarizabilityDescriptor_PA numeric \n");
				
				out4.write("@attribute class {");
				for(int i = 0 ; i < classe.length ; i++){
					if(i != 0)out4.write(",");
					out4.write(classe[i]);
				}
				out4.write("} \n");
				out4.write("@data \n");
			}
			results = eSQ_HR.getQsars(reaction);
			for(int i = 0 ; i < results.size() ; i++){
				if(results.get(i) != null)
					out4.write(extractDeci(results.get(i))+", ");
				else
					out4.write("null");
			}

			out4.write(extractClass(reaction.getProbability())+", ");
			out4.write("\n");
		}
		else if(reaction.getNameReaction().equals("HydrogenRearrangementDelta")){
			if(out5 == null){
				out5 = new FileWriter(new File(nameFile+"-hrd.arff"));
				out5.write("@relation  "+reaction.getNameReaction()+" \n");
				out5.write("@attribute PartialSigmaChargeDescriptor_RA numeric \n");
				out5.write("@attribute PartialPiChargeDescriptor_RA numeric \n");
				out5.write("@attribute EffectiveAtomPolarizabilityDescriptor_RA numeric \n");
				out5.write("@attribute PartialSigmaChargeDescriptor_PA numeric \n");
				out5.write("@attribute PartialPiChargeDescriptor_PA numeric \n");
				out5.write("@attribute EffectiveAtomPolarizabilityDescriptor_PA numeric \n");
				
				out5.write("@attribute class {");
				for(int i = 0 ; i < classe.length ; i++){
					if(i != 0)out5.write(",");
					out5.write(classe[i]);
				}
				out5.write("} \n");
				out5.write("@data \n");
			}
			results = eSQ_HR.getQsars(reaction);
			for(int i = 0 ; i < results.size() ; i++){
				if(results.get(i) != null)
					out5.write(extractDeci(results.get(i))+", ");
				else
					out5.write("null");
			}

			out5.write(extractClass(reaction.getProbability())+", ");
			out5.write("\n");
		}
		
		if(results != null){
			hashL_P.put(results.toString(),new Double(reaction.getProbability()));
		}
	}
	/**
	 * define the format of the double 
	 * @param double1 Value for rounding
	 * @return String with only 3 zeros
	 */
	private String extractDeci(Double double1) {
		DecimalFormat cf = new DecimalFormat("00.0000");
		String result = cf.format(double1);
		if(double1 >= 0)
			result =" "+result;
		return result;
	}
	/**
	 * close the FileWrite
	 * @throws IOException 
	 * 
	 */
	public void close() throws IOException {
		if(out1 != null)
			out1.close();
		if(out2 != null)
			out2.close();
		if(out3 != null)
			out3.close();
		if(out4 != null)
			out4.close();
		if(out5 != null)
			out5.close();
	}
	/**
	 * extract the class which belong
	 * 
	 * @param value The Double value
	 * @return String class
	 */
	private String extractClass(double value){
		String result = null;
		double interval1 = 0.00;
		double interval2 = 0.01;
		
		for(int i = 0; i < classe.length ; i++){
			if((interval1 <= value) && (value < interval2))
				return classe[i];
			else{
				interval1 = interval1 + 0.01;
				interval2 = interval2 + 0.01;
			}
		}
		return result;
	}
}
