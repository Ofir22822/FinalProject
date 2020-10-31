package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;

import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;
import weka.core.Instance;
import weka.core.Instances;

public class KNNController {
	
	private static BufferedReader datafile;
	
	public static double KNN() {
		double class1 = -1.0;
		Instances data;
		try {	
			data = new Instances(datafile);
			data.setClassIndex(data.numAttributes() - 1);
			//do not use first and second
			Instance first = data.instance(0);
			data.delete(0);
	 
			Classifier ibk = new IBk(3);		
			ibk.buildClassifier(data);
	 
			class1 = ibk.classifyInstance(first);
			datafile.close();
			//createDataFile();		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return class1;
	}
	
	public static void setKNNData(String txtName) {
		datafile = readDataFile(txtName);
	}
	
	public static void setKNNData(String txtName, textObject txtObject){
		try {
			SentiStrengthController.createSentimentDataForKNN(txtObject);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		datafile = readDataFile(txtName);
	}
	
	public static BufferedReader readDataFile(String filename) {
		BufferedReader inputReader = null;
 
		try {
			inputReader = new BufferedReader(new FileReader(filename));
			
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
		}
 
		return inputReader;
	}

	public static void createDictionaryData(textObject txtObject) {
		
		dbQuerys query = new dbQuerys();
		
		String attr = "@relation dictionary\n\n";
		List<String> dictionaryWords = DictionaryController.convertDictionaryFileToList();
		
		for(int i = 0; i < dictionaryWords.size();i++) {
			attr += "@attribute \""+dictionaryWords.get(i)+"\" numeric\n";
		}
		
		attr += "@attribute classification {P, PE}\n\n@data\n";
		
		ArrayList<String> FrequencyFeauters = query.getFrequencyFeatures();
		FrequencyFeauters.add(0, txtObject.getFrequencyFeature());	
		
		for(int i = 0; i < FrequencyFeauters.size();i++) {
			attr += FrequencyFeauters.get(i) + "\n";
		}
		
		FileFunctions.createTextFile("DictionaryDataKNN.txt", attr);
	}
	
}
