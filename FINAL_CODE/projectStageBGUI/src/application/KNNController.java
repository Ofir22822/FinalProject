package application;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;
import weka.core.Instance;
import weka.core.Instances;

/**
 * class for knn algorithm related functions
 *
 */
public class KNNController {
	
	static int k = 1;
	
	public static dbQuerys query;
	private static BufferedReader datafile;

	private static int word_rank_T = 10;
	private static int sentence_rank_T = 30;
	
	/**
	 * run knn classification algorithm
	 * @param numK number of neighbors for classifier
	 * @return result of knn, 0 promotion 1 personal experience -1 error
	 */
	public static double KNN(int numK) {
		k = numK;
		return KNN();
	}
	
	/**
	 * run knn classification algorithm
	 * @return result of knn, 0 promotion 1 personal experience -1 error
	 */
	public static double KNN() {
		double class1 = -1.0;
		Instances data;
		try {	
			data = new Instances(datafile);
			data.setClassIndex(data.numAttributes() - 1);
			//do not use first and second
			Instance first = data.instance(0);
			data.delete(0);
	 
			Classifier ibk = new IBk(k);		
			ibk.buildClassifier(data);
	 
			class1 = ibk.classifyInstance(first);
			datafile.close();
			//createDataFile();		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return class1;
	}
	
	/**
	 * set data for classification of knn
	 * @param txtName text to check classification of
	 */
	public static void setKNNData(String txtName) {
		datafile = readDataFile(txtName);
	}
	
	private static BufferedReader readDataFile(String filename) {
		BufferedReader inputReader = null;
 
		try {
			inputReader = new BufferedReader(new FileReader(filename));
			
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
		}
 
		return inputReader;
	}

	/**
	 * set database connection variable
	 * @param queryNew new connection
	 */
	public static void setDbquery(dbQuerys queryNew) {
		query = queryNew;
	}
	
	/***
	 * used for testing of features
	 * create data for classification using words sentiment and dictionary
	 * feature sentiment multiply to match dictionary length
	 * @param txtObject text to check classification of
	 * @param length 1 long text 0 short
	 */
	public static void mergeFeaturesData(textObject txtObject, int length)
	{
		dbQuerys.createAllDictionaryWordsFile();
		List<String> dictionaryWords = FileFunctions.convertTextFileToList(new File("dictionary\\dictionary.txt"));
		
		
		//word part for data file
		File dataFileWord = new File("sentiment\\þþinitWordDataMerge.txt");
		
		// write text in String
		String initWordData = FileFunctions.convertTextFileToString(dataFileWord);

		int mul = dictionaryWords.size()/11;
		
		String mulData = "";
		for(int j = 0; j < mul ; j++)
			mulData += initWordData;
		
		initWordData = "@relation wordANDdictionary\n\n" + mulData;
		
		//word sentiment
		ArrayList<String> SentimentFeautersWord = query.getSentimentFeaturesWord(length);
		SentimentFeautersWord.add(0, txtObject.getSentimentFeatureWord());
		
		for(int i =0; i< SentimentFeautersWord.size();i++)
		{
			String featureI = SentimentFeautersWord.get(i);
			if(featureI.contains("PE"))
				featureI = featureI.substring(0, featureI.length()-2);
			else
				featureI = featureI.substring(0, featureI.length()-1);
			
			SentimentFeautersWord.set(i, featureI);
		}
		
		//File merge = new File("KNN\\DataKNN-words-dictionary.txt");
		//FileFunctions.writeToTextFile("KNN\\DataKNN-words-dictionary.txt", initWordData);
		
		String attr = "";

		for(int i = 0; i < dictionaryWords.size();i++) {
			attr += "@attribute \""+dictionaryWords.get(i)+"\" numeric\n";
		}
		
		attr += "@attribute classification {P, PE}\n\n@data\n";
		
		//FileFunctions.writeToTextFile("KNN\\DataKNN-words-dictionary.txt", attr);
		
		
		///dictionary
		ArrayList<String> FrequencyFeauters = query.getFrequencyFeatures(length);
		FrequencyFeauters.add(0, txtObject.getFrequencyFeature().replace(" ", ",")+"P");	
		
		String features = "";
		for(int i = 0; i < FrequencyFeauters.size();i++) {
			
			mulData = "";

			for(int j = 0; j < mul ; j++)
			{
				mulData += SentimentFeautersWord.get(i);
				if(i == 0)
					mulData += ",";
			}
			
			features += mulData+FrequencyFeauters.get(i) + "\n";
		}
		features.substring(0, features.length()-2);
		
		//FileFunctions.writeToTextFile("KNN\\DataKNN-words-dictionary.txt", features);
		FileFunctions.createTextFile("KNN\\DataKNN-words-dictionary.txt", initWordData + attr + features);
	}
	
	/**
	 * used for testing of features
	 * create data for classification using words sentiment and dictionary
	 * feature sentiment not multiply
	 * @param txtObject text to check classification of
	 * @param length 1 long text 0 short
	 */
	public static void mergeFeaturesData_noMulti(textObject txtObject, int length)
	{
		dbQuerys.createAllDictionaryWordsFile();
		List<String> dictionaryWords = FileFunctions.convertTextFileToList(new File("dictionary\\dictionary.txt"));
		
		
		//word part for data file
		File dataFileWord = new File("sentiment\\þþinitWordDataMerge.txt");
		
		// write text in String
		String initWordData = FileFunctions.convertTextFileToString(dataFileWord);
		
		initWordData = "@relation wordANDdictionary\n\n" + initWordData;
		
		//word sentiment
		ArrayList<String> SentimentFeautersWord = query.getSentimentFeaturesWord(length);
		SentimentFeautersWord.add(0, txtObject.getSentimentFeatureWord()+",");
		
		for(int i =0; i< SentimentFeautersWord.size();i++)
		{
			String featureI = SentimentFeautersWord.get(i);
			if(featureI.contains("PE"))
				featureI = featureI.substring(0, featureI.length()-2);
			else
				featureI = featureI.substring(0, featureI.length()-1);
			
			SentimentFeautersWord.set(i, featureI);
		}
		
		//File merge = new File("KNN\\DataKNN-words-dictionary.txt");
		//FileFunctions.writeToTextFile("KNN\\DataKNN-words-dictionary.txt", initWordData);
		
		String attr = "";

		for(int i = 0; i < dictionaryWords.size();i++) {
			attr += "@attribute \""+dictionaryWords.get(i)+"\" numeric\n";
		}
		
		attr += "@attribute classification {P, PE}\n\n@data\n";
		
		//FileFunctions.writeToTextFile("KNN\\DataKNN-words-dictionary.txt", attr);
		
		
		///dictionary
		ArrayList<String> FrequencyFeauters = query.getFrequencyFeatures(length);
		FrequencyFeauters.add(0, txtObject.getFrequencyFeature().replace(" ", ",")+"P");	
		
		String features = "";
		for(int i = 0; i < FrequencyFeauters.size();i++) {
			
			features += SentimentFeautersWord.get(i) + FrequencyFeauters.get(i) + "\n";
		}
		features.substring(0, features.length()-2);
		
		//FileFunctions.writeToTextFile("KNN\\DataKNN-words-dictionary.txt", features);
		FileFunctions.createTextFile("KNN\\DataKNN-words-dictionary-nomulti.txt", initWordData + attr + features);
	}
	
	/**
	 * used for testing of features
	 * create data for classification using sentiment and dictionary
	 * feature are normalized using rank
	 * @param txtObject text to check classification of
	 * @param length 1 long text 0 short
	 */
	public static void mergeFeaturesData_Rank(textObject txtObject, int length)
	{
		dbQuerys.createAllDictionaryWordsFile();
		List<String> dictionaryWords = FileFunctions.convertTextFileToList(new File("dictionary\\dictionary.txt"));
		
		
		//word part for data file
		File dataFileSentence = new File("sentiment\\þþþþþþþþinitDataMergeWord.txt");
		
		// write text in String
		String initWordData = FileFunctions.convertTextFileToString(dataFileSentence);
		
		initWordData = "@relation wordANDdictionary\n\n" + initWordData;
		
		//word sentiment
		ArrayList<String> SentimentFeautersWord = query.getSentimentFeaturesWord(length);
		SentimentFeautersWord.add(0, txtObject.getSentimentFeatureWord());
		
		for(int i =0; i< SentimentFeautersWord.size();i++)
		{
			String featureI = SentimentFeautersWord.get(i);
			if(featureI.contains("PE"))
				featureI = featureI.substring(0, featureI.length()-2);
			else
				featureI = featureI.substring(0, featureI.length()-1);

			String[] numbers = featureI.split(",");
			String featureRank = "";
			for(int j = 0; j < numbers.length; j++)
			{
				int num = Integer.parseInt(numbers[j]);
				if(num >= 1 && num < word_rank_T)
					featureRank +="1,";
				else if(num != 0)
					featureRank +="2,";
				else
					featureRank +="0,";			
			}
			
			SentimentFeautersWord.set(i, featureRank);
		}
		
		String attr = "";

		for(int i = 0; i < dictionaryWords.size();i++) {
			attr += "@attribute \""+dictionaryWords.get(i)+"\" numeric\n";
		}
		
		attr += "@attribute classification {P, PE}\n\n@data\n";
		
		
		///dictionary
		ArrayList<String> FrequencyFeauters = query.getFrequencyFeatures(length);
		FrequencyFeauters.add(0, txtObject.getFrequencyFeature().replace(" ", ",")+"P");	
		
		String features = "";
		for(int i = 0; i < FrequencyFeauters.size();i++) {
			
			String[] numbers = FrequencyFeauters.get(i).split(",");
			String featureRank = "";
			for(int j = 0; j < numbers.length-1; j++)
			{
				int num = Integer.parseInt(numbers[j]);
				if(num >= 1 && num < word_rank_T)
					featureRank +="1,";
				else if(num != 0)
					featureRank +="2,";
				else
					featureRank +="0,";			
			}
			featureRank += numbers[numbers.length-1];			
			features += SentimentFeautersWord.get(i)+ featureRank+ "\n";
		}
		features.substring(0, features.length()-2);
		
		//FileFunctions.writeToTextFile("KNN\\DataKNN-words-dictionary.txt", features);
		FileFunctions.createTextFile("KNN\\DataKNN-words-dictionary-rank.txt", initWordData + attr + features);
	}

	/**
	 * used for testing of features
	 * create data for classification using sentence sentiment and dictionary
	 * feature sentiment multiply to match dictionary length
	 * @param txtObject text to check classification of
	 * @param length 1 long text 0 short
	 */
	public static void mergeFeaturesDataSentence(textObject txtObject, int length)
	{
		dbQuerys.createAllDictionaryWordsFile();
		List<String> dictionaryWords = FileFunctions.convertTextFileToList(new File("dictionary\\dictionary.txt"));
		
		
		//word part for data file
		File dataFileSentence = new File("þþþþþþinitSentence.txt");
		
		// write text in String
		String initWordData = FileFunctions.convertTextFileToString(dataFileSentence);

		int mul = dictionaryWords.size()/11;
		
		String mulData = "";
		for(int j = 0; j < mul ; j++)
			mulData += initWordData;
		
		initWordData = "@relation wordANDdictionary\n\n" + mulData;
		
		//word sentiment
		ArrayList<String> SentimentFeautersSentence = query.getSentimentFeatures(length);
		SentimentFeautersSentence.add(0, txtObject.getSentimentFeatureSentence());
		
		for(int i =0; i< SentimentFeautersSentence.size();i++)
		{
			String featureI = SentimentFeautersSentence.get(i);
			if(featureI.contains("PE"))
				featureI = featureI.substring(0, featureI.length()-2);
			else
				featureI = featureI.substring(0, featureI.length()-1);
			
			SentimentFeautersSentence.set(i, featureI);
		}
		
		//File merge = new File("KNN\\DataKNN-words-dictionary.txt");
		//FileFunctions.writeToTextFile("KNN\\DataKNN-words-dictionary.txt", initWordData);
		
		String attr = "";

		for(int i = 0; i < dictionaryWords.size();i++) {
			attr += "@attribute \""+dictionaryWords.get(i)+"\" numeric\n";
		}
		
		attr += "@attribute classification {P, PE}\n\n@data\n";
		
		//FileFunctions.writeToTextFile("KNN\\DataKNN-words-dictionary.txt", attr);
		
		
		///dictionary
		ArrayList<String> FrequencyFeauters = query.getFrequencyFeatures(length);
		FrequencyFeauters.add(0, txtObject.getFrequencyFeature().replace(" ", ",")+"P");	
		
		String features = "";
		for(int i = 0; i < FrequencyFeauters.size();i++) {
			
			mulData = "";

			for(int j = 0; j < mul ; j++)
			{
				mulData += SentimentFeautersSentence.get(i);
				if(i == 0)
					mulData += ",";
			}
			
			features += mulData+FrequencyFeauters.get(i) + "\n";
		}
		features.substring(0, features.length()-2);
		
		//FileFunctions.writeToTextFile("KNN\\DataKNN-words-dictionary.txt", features);
		FileFunctions.createTextFile("KNN\\DataKNN-Sentence-dictionary.txt", initWordData + attr + features);
	}
	
	/**
	 * used for testing of features
	 * create data for classification using sentence sentiment and dictionary
	 * feature are normalized using rank
	 * @param txtObject text to check classification of
	 * @param length 1 long text 0 short
	 */
	public static void mergeFeaturesData_RankSentence(textObject txtObject, int length)
	{
		dbQuerys.createAllDictionaryWordsFile();
		List<String> dictionaryWords = FileFunctions.convertTextFileToList(new File("dictionary\\dictionary.txt"));
		
		
		//word part for data file
		File dataFileWord = new File("þþþþþþinitSentence.txt");
		
		// write text in String
		String initWordData = FileFunctions.convertTextFileToString(dataFileWord);
		
		initWordData = "@relation wordANDdictionary\n\n" + initWordData;
		
		//word sentiment
		ArrayList<String> SentimentFeautersSentence = query.getSentimentFeatures(length);
		SentimentFeautersSentence.add(0, txtObject.getSentimentFeatureSentence());
		
		for(int i =0; i< SentimentFeautersSentence.size();i++)
		{
			String featureI = SentimentFeautersSentence.get(i);
			if(featureI.contains("PE"))
				featureI = featureI.substring(0, featureI.length()-2);
			else
				featureI = featureI.substring(0, featureI.length()-1);

			String[] numbers = featureI.split(",");
			String featureRank = "";
			for(int j = 0; j < numbers.length; j++)
			{
				int num = Integer.parseInt(numbers[j]);
				if(num >= 1 && num < sentence_rank_T)
					featureRank +="1,";
				else if(num != 0)
					featureRank +="2,";
				else
					featureRank +="0,";			
			}
			
			SentimentFeautersSentence.set(i, featureRank);
		}
		
		String attr = "";

		for(int i = 0; i < dictionaryWords.size();i++) {
			attr += "@attribute \""+dictionaryWords.get(i)+"\" numeric\n";
		}
		
		attr += "@attribute classification {P, PE}\n\n@data\n";
		
		
		///dictionary
		ArrayList<String> FrequencyFeauters = query.getFrequencyFeatures(length);
		FrequencyFeauters.add(0, txtObject.getFrequencyFeature().replace(" ", ",")+"P");	
		
		String features = "";
		for(int i = 0; i < FrequencyFeauters.size();i++) {
			
			String[] numbers = FrequencyFeauters.get(i).split(",");
			String featureRank = "";
			for(int j = 0; j < numbers.length-1; j++)
			{
				int num = Integer.parseInt(numbers[j]);
				if(num >= 1 && num < sentence_rank_T)
					featureRank +="1,";
				else if(num != 0)
					featureRank +="2,";
				else
					featureRank +="0,";			
			}
			featureRank += numbers[numbers.length-1];			
			features += SentimentFeautersSentence.get(i)+ featureRank+ "\n";
		}
		features.substring(0, features.length()-2);
		
		//FileFunctions.writeToTextFile("KNN\\DataKNN-words-dictionary.txt", features);
		FileFunctions.createTextFile("KNN\\DataKNN-Sentence-dictionary-rank.txt", initWordData + attr + features);
	}
	
	/**
	 * used for testing of features
	 * create data for classification using word sentiment and dictionary
	 * feature are normalized using rank
	 * @param txtObject text to check classification of
	 * @param length 1 long text 0 short
	 */	
	public static void mergeFeaturesData_RankWord(textObject txtObject, int length)
	{
		dbQuerys.createAllDictionaryWordsFile();
		List<String> dictionaryWords = FileFunctions.convertTextFileToList(new File("dictionary\\dictionary.txt"));
		
		
		//word part for data file
		File dataFileWord = new File("þþþþþþinitWord.txt");
		
		// write text in String
		String initWordData = FileFunctions.convertTextFileToString(dataFileWord);
		
		initWordData = "@relation wordANDdictionary\n\n" + initWordData;
		
		//word sentiment
		ArrayList<String> SentimentFeautersSentence = query.getSentimentFeatures(length);
		SentimentFeautersSentence.add(0, txtObject.getSentimentFeatureSentence());
		
		for(int i =0; i< SentimentFeautersSentence.size();i++)
		{
			String featureI = SentimentFeautersSentence.get(i);
			if(featureI.contains("PE"))
				featureI = featureI.substring(0, featureI.length()-2);
			else
				featureI = featureI.substring(0, featureI.length()-1);

			String[] numbers = featureI.split(",");
			String featureRank = "";
			for(int j = 0; j < numbers.length; j++)
			{
				int num = Integer.parseInt(numbers[j]);
				if(num >= 1 && num < word_rank_T)
					featureRank +="1,";
				else if(num != 0)
					featureRank +="2,";
				else
					featureRank +="0,";			
			}
			
			SentimentFeautersSentence.set(i, featureRank);
		}
		
		String attr = "";

		for(int i = 0; i < dictionaryWords.size();i++) {
			attr += "@attribute \""+dictionaryWords.get(i)+"\" numeric\n";
		}
		
		attr += "@attribute classification {P, PE}\n\n@data\n";
		
		
		///dictionary
		ArrayList<String> FrequencyFeauters = query.getFrequencyFeatures(length);
		FrequencyFeauters.add(0, txtObject.getFrequencyFeature().replace(" ", ",")+"P");	
		
		String features = "";
		for(int i = 0; i < FrequencyFeauters.size();i++) {
			
			String[] numbers = FrequencyFeauters.get(i).split(",");
			String featureRank = "";
			for(int j = 0; j < numbers.length-1; j++)
			{
				int num = Integer.parseInt(numbers[j]);
				if(num >= 1 && num < word_rank_T)
					featureRank +="1,";
				else if(num != 0)
					featureRank +="2,";
				else
					featureRank +="0,";			
			}
			featureRank += numbers[numbers.length-1];			
			features += SentimentFeautersSentence.get(i)+ featureRank+ "\n";
		}
		features.substring(0, features.length()-2);
		
		//FileFunctions.writeToTextFile("KNN\\DataKNN-words-dictionary.txt", features);
		FileFunctions.createTextFile("KNN\\DataKNN-Sentence-dictionary-rank.txt", initWordData + attr + features);
	}
	
	public static int getWord_rank_T() {
		return word_rank_T;
	}
	
	/**
	 * set rank parameter value
	 * @param word_rank_T new value
	 */
	public static void setWord_rank_T(int word_rank_T) {
		KNNController.word_rank_T = word_rank_T;
	}

	public static int getSentence_rank_T() {
		return sentence_rank_T;
	}

	/**
	 * set rank parameter value
	 * @param sentence_rank_T new value
	 */
	public static void setSentence_rank_T(int sentence_rank_T) {
		KNNController.sentence_rank_T = sentence_rank_T;
	}
	
}
