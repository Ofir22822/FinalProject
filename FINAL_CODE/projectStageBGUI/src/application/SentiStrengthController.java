package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import uk.ac.wlv.sentistrength.SentiStrength;

/**
 * class for sentiment (using SentiStrength) related functions
 *
 */
public class SentiStrengthController {

	public static dbQuerys query;
	// set sentence weight matrix ()
	private static String[][] resultWeightSentenceMat = { { "0" }, new String[25] };
	static {	
		String weightSentenceCombine[] = { "1,-1", "1,-2", "1,-3", "1,-4", "1,-5", "2,-1", "2,-2", "2,-3", "2,-4",
				"2,-5", "3,-1", "3,-2", "3,-3", "3,-4", "3,-5", "4,-1", "4,-2", "4,-3", "4,-4", "4,-5", "5,-1", "5,-2",
				"5,-3", "5,-4", "5,-5" };
		resultWeightSentenceMat[0] = weightSentenceCombine;
	}
	private static SentiStrength sentiStrength = new SentiStrength();
	static {
		// Create an array of command line parameters to send (not text or file to process)
		String ssthInitialisation[] = { "sentidata", "resources\\SentStrength_Data\\", "scale" };
		String ssthInitialisation3[] = { "sentidata", "resources\\SentStrength_Data\\", "explain" };
		sentiStrength.initialise(ssthInitialisation); // Initialise
		sentiStrength.initialise(ssthInitialisation3); // Initialise
	}

	/*sentiment features functions*/
	
	/**
	 * create sentiment feature for words
	 * @param txtFile text to create feature of
	 * @return words sentiment feature as string
	 */
	public static String getSentimentFeatureWords(File txtFile) {
		
		int countWords = 0, countWordsWithWeight = 0;
		int weightWords[] = new int[11];

		try {
			//read each line from file
			Scanner myReader = new Scanner(new FileInputStream(txtFile));
			while (myReader.hasNextLine()) {
				
				//apply sentiStrength analysis to line
				String line = myReader.nextLine();
				String ssResult = sentiStrength.computeSentimentScores(line);

				//split line to words, and split sentiStrength result to words
				String[] words = line.split("\\s+");				
				String[] wordsSenti = ssResult.split("\\s+");
				
				countWords += words.length; 	//add line words count to total words count
				
				// i = 3, jump sentence weights
				for (int i = 3; i < wordsSenti.length; i++) {
					//if word has weight
					if (wordsSenti[i].indexOf("[") != -1) {
						try {
							String wordWeight = wordsSenti[i].substring(wordsSenti[i].indexOf("[") + 1, wordsSenti[i].length() - 1);
							int weight = Integer.parseInt(wordWeight);
							weightWords[weight + 5]++;
							countWordsWithWeight++;	//add to weight words count
						} catch (Exception e) {
							//skip on parse error
						}
					}
				}
			}
			weightWords[5] = countWords - countWordsWithWeight;		//calculate (neutral)0 weight words count
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		
		//create feature string
		String WordsSentimentFeature = "";
		for (int i = 0; i < weightWords.length; i++) {
			WordsSentimentFeature += weightWords[i] + " ";
		}
		return WordsSentimentFeature;

	}

	/**
	 * get text file sentence sentiment values from SentiStrength
	 * create sentiment feature for sentence in matrix
	 * row 0 : 1,-1 to 5,-5
	 * row 1 : count of weights
	 * 
	 * @param txtFile text to create feature of
	 * @return matrix with values
	 */
	public static String[][] getSentimentFeatureSentences(File txtFile) {

		Arrays.fill(resultWeightSentenceMat[1], null);	// !!!!!! fill matrix[1] with null
		
		try {// read text file
			Scanner myReader = new Scanner(txtFile);

			while (myReader.hasNextLine()) {
				// for each line in text
				String line = myReader.nextLine();
				if (line.length() == 0)		//skip empty lines
					continue;
				// use sentiStrength API on line, save in result
				String result = sentiStrength.computeSentimentScores(line); 

				// get only sentence weights from result
				String sentenceWeight = result.substring(result.indexOf("[sentence:") + 1);
				sentenceWeight = sentenceWeight.substring(sentenceWeight.indexOf(":") + 2, sentenceWeight.indexOf("]"));
				
				int count = 0;
				// count sentence weight combination
				for (int i = 0; i < resultWeightSentenceMat[0].length; i++) {
					// same weight, add count
					if (sentenceWeight.compareTo(resultWeightSentenceMat[0][i]) == 0) {
						if (resultWeightSentenceMat[1][i] != null)
							count = Integer.parseInt(resultWeightSentenceMat[1][i]) + 1;
						else
							count = 1;
						resultWeightSentenceMat[1][i] = "" + count;
					}
				}
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
			return null;
		}
		return resultWeightSentenceMat;
	}

	/***
	 * convert sentiment matrix to string if cell=null, put 0
	 * 
	 * @param Mat matrix
	 * @param n number of columns
	 * @return matrix as string, sentence feature
	 */
	public static String sentimentMatrixToString(String Mat[][], int n) {
		String sentimentFeature = "";
		for (int j = 0; j < n; j++) {
			if (Mat[1][j] != null)
				sentimentFeature += Mat[1][j] + " ";
			else
				sentimentFeature += "0 ";
		}
		return sentimentFeature;
	}

	private static String featureWordNormalization(String sentimentFeature) {
		int weightWords[] = new int[11];

		String[] FeatureWeightsValues = sentimentFeature.split(",");
		String featureNorm = "";
		
		for(int i = 0; i< 11; i++)
		{
			if(i==2||i==3||i==7||i==8||i==10)
			{
				weightWords[i] = Integer.parseInt(FeatureWeightsValues[i]);
				featureNorm += weightWords[i]+",";
			}
			else
			{
				weightWords[i] = 0;
				featureNorm += weightWords[i]+",";
			}
			
		}
		
		featureNorm+=FeatureWeightsValues[11];
		return featureNorm;
	}
	
	private static String featureSentenceNormalization(String sentimentFeature) {
		double weightWords[] = new double[25];
		double countSentence= 0;
		String[] FeatureWeightsValues = sentimentFeature.split(",");
		String featureNorm = "";
		
		for(int i = 0; i< 25; i++)
		{
			if((i > 2 && i < 8)||( i > 11 && i < 15 ))
				weightWords[i] = Double.parseDouble(FeatureWeightsValues[i]);		
			else
				weightWords[0]=0;
			
			countSentence += weightWords[i];
		}
			
		for(int i = 0; i< 25; i++)
		{		
			featureNorm += Math.round((weightWords[i]/countSentence) * 10000.0) / 10000.0+","; //weightWords[i]+",";//
			
		}
				
		featureNorm += FeatureWeightsValues[25];
		
		/*
		FeatureWeightsValues[0] = "0";
		for(int i = 0; i< 25; i++)
		{	
			featureNorm += FeatureWeightsValues[i]+",";
		}
		featureNorm += FeatureWeightsValues[25];
		*/
		return featureNorm;
	}
	
	
	/*sentiment data for KNN functions*/
	
	/**
	 * create sentiment data file to use in knn algorithm
	 * 
	 * @param txtObject object to check in knn
	 * @param length 1 long text 0 short
	 */
	public static void createSentimentKNNData(textObject txtObject, int length){
		initializeSentimentKNNDataFile();
		
		ArrayList<String> SentimentFeautersSentence = query.getSentimentFeatures(length);
		SentimentFeautersSentence.add(0, txtObject.getSentimentFeatureSentence());

		for(int i =0; i< SentimentFeautersSentence.size();i++)
		{
			String featureI = SentimentFeautersSentence.get(i);
			//featureI = SentiStrengthController.featureSentenceNormalization(featureI);
			SentimentFeautersSentence.set(i, featureI);
		}
		
		ArrayList<String> SentimentFeautersWord = query.getSentimentFeaturesWord(length);
		SentimentFeautersWord.add(0, txtObject.getSentimentFeatureWord());

		for(int i =0; i< SentimentFeautersWord.size();i++)
		{
			String featureI = SentimentFeautersWord.get(i);
			//featureI = SentiStrengthController.featureWordNormalization(featureI);
			SentimentFeautersWord.set(i, featureI);
		}
		
		ArrayList<String> SentimentWordRank = new ArrayList<>();
		for(int i =0; i< SentimentFeautersWord.size();i++)
		{
			String featureI = SentimentFeautersWord.get(i);

			String[] numbers = featureI.split(",");
			String featureRank = "";
			for(int j = 0; j < numbers.length-1; j++)
			{
				int num = Integer.parseInt(numbers[j]);
				if(num >= 1 && num < 10)
					featureRank +="1,";
				else if(num != 0)
					featureRank +="2,";
				else
					featureRank +="0,";			
			}
			featureRank += numbers[numbers.length-1];	
			SentimentWordRank.add(featureRank);
		}
		
		ArrayList<String> SentimentSentenceRank = new ArrayList<>();
		for(int i =0; i< SentimentFeautersSentence.size();i++)
		{
			String featureI = SentimentFeautersSentence.get(i);

			String[] numbers = featureI.split(",");
			String featureRank = "";
			for(int j = 0; j < numbers.length-1; j++)
			{
				int num = Integer.parseInt(numbers[j]);
				if(num >= 1 && num < 10)
					featureRank +="1,";
				else if(num != 0)
					featureRank +="2,";
				else
					featureRank +="0,";			
			}
			featureRank += numbers[numbers.length-1];	
			SentimentSentenceRank.add(featureRank);
		}
		
		FileWriter FeauterstWriter;
		try {
			FeauterstWriter = new FileWriter("sentiment\\sentimentDataKNNSentence.txt", true);
			for (String line : SentimentFeautersSentence) {
				FeauterstWriter.write(line + "\n");
			}
			FeauterstWriter.close();
			
			FeauterstWriter = new FileWriter("sentiment\\sentimentDataKNNWord.txt", true);
			for (String line : SentimentFeautersWord) {
				FeauterstWriter.write(line + "\n");
			}
			FeauterstWriter.close();
			
			FeauterstWriter = new FileWriter("sentiment\\sentimentDataKNNWordRank.txt", true);
			for (String line : SentimentWordRank) {
				FeauterstWriter.write(line + "\n");
			}
			FeauterstWriter.close();
			
			FeauterstWriter = new FileWriter("sentiment\\sentimentDataKNNSentenceRank.txt", true);
			for (String line : SentimentSentenceRank) {
				FeauterstWriter.write(line + "\n");
			}
			FeauterstWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		mergeKNNdata(txtObject, length);
		
	}

	private static void createSentimentKNNDataNormalize(textObject txtObject, int length){
		initializeSentimentKNNDataFileNormalize();
		
		ArrayList<String> SentimentFeautersSentence = query.getSentimentFeatures(length);
		SentimentFeautersSentence.add(0, txtObject.getSentimentFeatureSentence());

		for(int i =0; i< SentimentFeautersSentence.size();i++)
		{
			String featureI = SentimentFeautersSentence.get(i);
			featureI = SentiStrengthController.featureSentenceNormalization(featureI);
			SentimentFeautersSentence.set(i, featureI);
		}
		
		ArrayList<String> SentimentFeautersWord = query.getSentimentFeaturesWord(length);
		SentimentFeautersWord.add(0, txtObject.getSentimentFeatureWord());

		for(int i =0; i< SentimentFeautersWord.size();i++)
		{
			String featureI = SentimentFeautersWord.get(i);
			featureI = SentiStrengthController.featureWordNormalization(featureI);
			SentimentFeautersWord.set(i, featureI);
		}
		
		FileWriter FeauterstWriter;
		try {
			FeauterstWriter = new FileWriter("sentiment\\sentimentDataKNNSentenceNormalize.txt", true);
			for (String line : SentimentFeautersSentence) {
				FeauterstWriter.write(line + "\n");
			}
			FeauterstWriter.close();
			
			FeauterstWriter = new FileWriter("sentiment\\sentimentDataKNNWordNormalize.txt", true);
			for (String line : SentimentFeautersWord) {
				FeauterstWriter.write(line + "\n");
			}
			FeauterstWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		mergeKNNdataNormalize(txtObject, length);
		
	}

	
	/**
	 * initialize sentiment data file
	 */
	private static void initializeSentimentKNNDataFile() {
		File dataFileSentence = new File("sentiment\\initSentenceData.txt");
		File dataFileWord = new File("sentiment\\initWordData.txt");
		File dataFileBoth = new File("sentiment\\þþinitBothData.txt");
		
		// write text in String
		String initSentenceData = FileFunctions.convertTextFileToString(dataFileSentence);
		String initWordData = FileFunctions.convertTextFileToString(dataFileWord);
		String initBothData = FileFunctions.convertTextFileToString(dataFileBoth);
		
		FileFunctions.createTextFile("sentiment\\sentimentDataKNNSentence.txt", initSentenceData);
		FileFunctions.createTextFile("sentiment\\sentimentDataKNNWord.txt", initWordData);
		FileFunctions.createTextFile("sentiment\\sentimentDataKNNWordRank.txt", initWordData);
		FileFunctions.createTextFile("sentiment\\sentimentDataKNNSentenceRank.txt", initSentenceData);
		FileFunctions.createTextFile("sentiment\\sentimentDataKNNboth.txt", initBothData);
	}

	private static void initializeSentimentKNNDataFileNormalize() {
		File dataFileSentence = new File("sentiment\\initSentenceData.txt");
		File dataFileWord = new File("sentiment\\initWordData.txt");
		File dataFileBoth = new File("sentiment\\þþinitBothData.txt");
		
		// write text in String
		String initSentenceData = FileFunctions.convertTextFileToString(dataFileSentence);
		String initWordData = FileFunctions.convertTextFileToString(dataFileWord);
		String initBothData = FileFunctions.convertTextFileToString(dataFileBoth);
		
		FileFunctions.createTextFile("sentiment\\sentimentDataKNNSentenceNormalize.txt", initSentenceData);
		FileFunctions.createTextFile("sentiment\\sentimentDataKNNWordNormalize.txt", initWordData);
		FileFunctions.createTextFile("sentiment\\sentimentDataKNNbothNormalize.txt", initBothData);
	}

	/**
	 * set database connection variable
	 * @param queryNew new connection
	 */
	public static void setDbquery(dbQuerys queryNew) {
		query = queryNew;
	}

	/**
	 * Create file data for knn with both word and sentence features
	 * @param txtObject text to check classification of
	 * @param length 1 long text 0 short
	 */
	public static void mergeKNNdata(textObject txtObject, int length) {
		
		//Feauters Sentence from database
		ArrayList<String> SentimentFeautersBoth = new ArrayList<>();
		ArrayList<String> SentimentFeautersSentence = query.getSentimentFeatures(length);
		
		String featureWord = "";
		String feature = "";
		
		feature = txtObject.getSentimentFeatureSentence();//SentiStrengthController.featureSentenceNormalization(txtObject.getSentimentFeatureSentence());

		if(feature.contains("PE"))
			feature = feature.substring(0, feature.length()-2);
		else
			feature = feature.substring(0, feature.length()-1);

		featureWord = SentiStrengthController.featureWordNormalization(txtObject.getSentimentFeatureWord());
		feature = feature + featureWord;

		SentimentFeautersBoth.add(0, feature);

		//normalize features
		for(int i = 1; i< SentimentFeautersSentence.size();i++)
		{
			String featureI = SentimentFeautersSentence.get(i);
			//featureI = SentiStrengthController.featureSentenceNormalization(featureI);
			SentimentFeautersSentence.set(i, featureI);
		}
		
		SentimentFeautersBoth.addAll(SentimentFeautersSentence);
		
		//Feauters words from database
		ArrayList<String> SentimentFeautersWord = query.getSentimentFeaturesWord(length);

		//normalize features
		for(int i =0; i< SentimentFeautersWord.size();i++)
		{
			String featureI = SentimentFeautersWord.get(i);
			//featureI = SentiStrengthController.featureWordNormalization(featureI);
			SentimentFeautersWord.set(i, featureI);
		}
		
		SentimentFeautersBoth.addAll(SentimentFeautersWord);
		
		FileWriter FeauterstWriter;
		try {
			String featureI = "";
			FeauterstWriter = new FileWriter("sentiment\\sentimentDataKNNboth.txt", true);
			FeauterstWriter.write(SentimentFeautersBoth.get(0) + "\n");
			int i = 0;
			for (i = 1; i < SentimentFeautersSentence.size();i++) {
				if(SentimentFeautersSentence.get(i).contains("PE"))
					featureI = SentimentFeautersSentence.get(i).substring(0, SentimentFeautersSentence.get(i).length()-2) + SentimentFeautersWord.get(i);
				else
					featureI = SentimentFeautersSentence.get(i).substring(0, SentimentFeautersSentence.get(i).length()-1) + SentimentFeautersWord.get(i);
				FeauterstWriter.write(featureI + "\n");
			}
			FeauterstWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void mergeKNNdataNormalize(textObject txtObject, int length) {
		
		//Feauters Sentence from database
		ArrayList<String> SentimentFeautersBoth = new ArrayList<>();
		ArrayList<String> SentimentFeautersSentence = query.getSentimentFeatures(length);
		ArrayList<String> SentimentFeautersWord = query.getSentimentFeaturesWord(length);
		
		String featureWord = "";
		String feature = "";
		
		feature = SentiStrengthController.featureSentenceNormalization(txtObject.getSentimentFeatureSentence());

		if(feature.contains("PE"))
			feature = feature.substring(0, feature.length()-2);
		else
			feature = feature.substring(0, feature.length()-1);

		featureWord = SentiStrengthController.featureWordNormalization(txtObject.getSentimentFeatureWord());
		feature = feature + featureWord;

		SentimentFeautersBoth.add(0, feature);

		//normalize features sentence
		for(int i = 1; i< SentimentFeautersSentence.size();i++)
		{
			String featureI = SentimentFeautersSentence.get(i);
			featureI = SentiStrengthController.featureSentenceNormalization(featureI);
			SentimentFeautersSentence.set(i, featureI);
		}//word
		for(int i =0; i< SentimentFeautersWord.size();i++)
		{
			String featureI = SentimentFeautersWord.get(i);
			featureI = SentiStrengthController.featureWordNormalization(featureI);
			SentimentFeautersWord.set(i, featureI);
		}
		//combine
		for(int i =0; i< SentimentFeautersSentence.size();i++)
		{
			feature = SentimentFeautersSentence.get(i);

			if(feature.contains("PE"))
				feature = feature.substring(0, feature.length()-2);
			else
				feature = feature.substring(0, feature.length()-1);

			featureWord = SentimentFeautersWord.get(i);
			feature = feature + featureWord;
			
			SentimentFeautersBoth.add(feature);
		}
	
		
		FileWriter FeauterstWriter;
		try {
			FeauterstWriter = new FileWriter("sentiment\\sentimentDataKNNbothNormalize.txt", true);
			FeauterstWriter.write(SentimentFeautersBoth.get(0) + "\n");
			int i = 0;
			for (i = 1; i < SentimentFeautersBoth.size();i++) {
				FeauterstWriter.write(SentimentFeautersBoth.get(i) + "\n");
			}
			FeauterstWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
