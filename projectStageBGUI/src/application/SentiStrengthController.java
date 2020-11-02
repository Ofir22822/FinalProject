package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import uk.ac.wlv.sentistrength.SentiStrength;

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
	 * @param txtFile
	 * @return words sentiment feature as string
	 */
	public static String getSentimentFeatureWords(File txtFile) {
		
		int countWords = 0, countWordsWithWeight = 0;
		int weightWords[] = new int[11];

		try {
			//read each line from file
			Scanner myReader = new Scanner(txtFile);
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
	 * @param txtFile
	 * @return
	 */
	public static String[][] getSentimentFeatureSentences(File txtFile) {

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
	 * @param Mat - matrix
	 * @param n   - number of columns
	 * @return matrix as string
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

	/*sentiment data for KNN functions*/
	
	/**
	 * create sentiment data file to use in knn algorithm
	 * 
	 * @param txtObject - object to check in knn
	 * @throws IOException
	 */
	public static void createSentimentKNNData(textObject txtObject){
		initializeSentimentKNNDataFile();
		
		ArrayList<String> SentimentFeautersSentence = query.getSentimentFeatures();
		SentimentFeautersSentence.add(0, txtObject.getSentimentFeatureSentence());

		ArrayList<String> SentimentFeautersWord = query.getSentimentFeaturesWord();
		SentimentFeautersWord.add(0, txtObject.getSentimentFeatureWord());

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
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * initialize sentiment data file
	 */
	private static void initializeSentimentKNNDataFile() {
		File dataFileSentence = new File("sentiment\\initSentenceData.txt");
		File dataFileWord = new File("sentiment\\initWordData.txt");

		// write text in String
		String initSentenceData = FileFunctions.convertTextFileToString(dataFileSentence);
		String initWordData = FileFunctions.convertTextFileToString(dataFileWord);

		FileFunctions.createTextFile("sentiment\\sentimentDataKNNSentence.txt", initSentenceData);
		FileFunctions.createTextFile("sentiment\\sentimentDataKNNWord.txt", initWordData);

	}

	public static void setDbquery(dbQuerys queryNew) {
		query = queryNew;
	}
}
