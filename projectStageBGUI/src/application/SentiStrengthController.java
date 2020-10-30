package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import uk.ac.wlv.sentistrength.SentiStrength;

public class SentiStrengthController {

	/***
	 * create sentiment feature matrix weights of sentence in matrix 1,-1 to 5,-5 in
	 * row 0 count of weights in row 1
	 * 
	 * @param filePath
	 * @return sentiment feature as matrix
	 */
	public static String[][] getSentimentFeature(File txtFile) {

		// set sentence weight matrix ()
		String weightSentenceCombine[] = { "1,-1", "1,-2", "1,-3", "1,-4", "1,-5", "2,-1", "2,-2", "2,-3", "2,-4",
				"2,-5", "3,-1", "3,-2", "3,-3", "3,-4", "3,-5", "4,-1", "4,-2", "4,-3", "4,-4", "4,-5", "5,-1", "5,-2",
				"5,-3", "5,-4", "5,-5" };
		String weightSentenceMat[][] = { { "0" }, new String[25] };
		weightSentenceMat[0] = weightSentenceCombine;

		// use SentiStrength API and initialize
		SentiStrength sentiStrength = new SentiStrength();
		// Create an array of command line parameters to send (not text or file to
		// process)
		String ssthInitialisation[] = { "sentidata", "resources\\SentStrength_Data\\", "scale" };
		String ssthInitialisation3[] = { "sentidata", "resources\\SentStrength_Data\\", "explain" };
		sentiStrength.initialise(ssthInitialisation); // Initialise
		sentiStrength.initialise(ssthInitialisation3); // Initialise

		// get values for matrix
		weightSentenceMat = getTextSenteceSentiment(sentiStrength, weightSentenceCombine, txtFile);

		return weightSentenceMat;
	}

	public static String getTextWordsSentiment(File txtFile) {
		
		// use SentiStrength API and initialize
		SentiStrength sentiStrength = new SentiStrength();
		// Create an array of command line parameters to send (not text or file to
		// process)
		String ssthInitialisation[] = { "sentidata", "resources\\SentStrength_Data\\", "scale" };
		String ssthInitialisation3[] = { "sentidata", "resources\\SentStrength_Data\\", "explain" };
		sentiStrength.initialise(ssthInitialisation); // Initialise
		sentiStrength.initialise(ssthInitialisation3); // Initialise
		
		int countSentece = 0, countWords = 0, totalcount = 0;
		int weightWords[] = new int[11];

		// can now calculate sentiment scores quickly without having to initialise again
		try {
			Scanner myReader = new Scanner(txtFile);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				// System.out.println(data);

				String result = sentiStrength.computeSentimentScores(data);

				int count = 0;

				// String resultSentence = result.substring(result.indexOf(".") + 1,
				// result.indexOf("[") - 1);
				// System.out.println(resultSentence);

				String[] words = data.split("\\s+");
				String[] wordsSenti = result.split("\\s+");
				for (int i = 3; i < wordsSenti.length; i++) {
					// System.out.println(wordsSenti[i].indexOf("["));
					if (wordsSenti[i].indexOf("[") != -1) {
						try {
							int weight = Integer.parseInt(wordsSenti[i].substring(wordsSenti[i].indexOf("[") + 1,
									wordsSenti[i].length() - 1));
							weightWords[weight + 5]++;
							totalcount++;
						} catch (Exception e) {

						}
					}
				}

				countWords += words.length;
				countSentece++;

			}
			weightWords[5] = countWords - totalcount;
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		
		String WordsSentimentFeature = "";
		for (int i = 0; i < weightWords.length; i++) {
			WordsSentimentFeature += weightWords[i] + " ";
		}
		return WordsSentimentFeature;

	}

	/**
	 * get text file sentence sentiment values from SentiStrength
	 * 
	 * @param sentiStrength
	 * @param weights
	 * @param txtFile
	 * @return
	 */
	private static String[][] getTextSenteceSentiment(SentiStrength sentiStrength, String[] weights, File txtFile) {
		String[][] resultWeightSentenceMat = { { "0" }, new String[25] };
		resultWeightSentenceMat[0] = weights;
		try {// read text file
			Scanner myReader = new Scanner(txtFile);

			while (myReader.hasNextLine()) { // for each line in text
				String line = myReader.nextLine();
				if (line.length() == 0)
					continue;
				String result = sentiStrength.computeSentimentScores(line); // use sentiStrength API on line, save in
																			// result

				// get only sentence weights from result
				String sentenceWeight = result.substring(result.indexOf("[sentence:") + 1);
				sentenceWeight = sentenceWeight.substring(sentenceWeight.indexOf(":") + 2, sentenceWeight.indexOf("]"));

				int count = 0;
				// count sentence weight combination
				for (int i = 0; i < resultWeightSentenceMat[0].length; i++) {
					// same weight
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

	/**
	 * create sentiment data file to use in knn algorithm
	 * 
	 * @param txtObject - object to check in knn
	 * @throws IOException
	 */
	public static void createSentimentDataForKNN(textObject txtObject) throws IOException {
		createDataFile();
		
		dbQuerys query = new dbQuerys();
		ArrayList<String> SentimentFeautersSentence = query.getSentimentFeatures();
		SentimentFeautersSentence.add(0, txtObject.getSentimentFeatureSentence());

		ArrayList<String> SentimentFeautersWord = query.getSentimentFeaturesWord();
		SentimentFeautersWord.add(0, txtObject.getSentimentFeatureWord());
		
		// System.out.println("\n---------\n
		// arrayList:"+SentimentFeauters.size()+"\n---------\n");

		System.out.println("\n----Sentence-----\n");
		FileWriter FeauterstWriter = new FileWriter("sentimentDataSentence.txt", true);
		for (String line : SentimentFeautersSentence) {
			FeauterstWriter.write(line + "\n");
		}
		FeauterstWriter.close();
		
		System.out.println("\n----Word-----\n");
		FeauterstWriter = new FileWriter("sentimentDataWord.txt", true);
		for (String line : SentimentFeautersWord) {
			FeauterstWriter.write(line + "\n");
		}
		FeauterstWriter.close();

	}

	/**
	 * initialize sentiment data file
	 */
	private static void createDataFile() {
		File dataFile = new File("CopySentence.txt");
		File dataFileWord = new File("CopyWord.txt");
		
		System.out.println(dataFileWord.getAbsolutePath());
		
		// File newDataFile = new File("sentimentData.txt");

		try {
			Scanner myReader = new Scanner(dataFile);
	
			// write text in String
			String textString = "";
			String textStringWord = "";
			
			while (myReader.hasNextLine()) {
				textString += myReader.nextLine() + "\n";
			}
			myReader.close();
			
			myReader = new Scanner(dataFileWord);
			while (myReader.hasNextLine()) {
				textStringWord += myReader.nextLine() + "\n";
			}			
			myReader.close();

			FileWriter FeauterstWriter = new FileWriter("sentimentDataSentence.txt");
			FeauterstWriter.write(textString);
			FeauterstWriter.close();
			
			FeauterstWriter = new FileWriter("sentimentDataWord.txt");
			FeauterstWriter.write(textStringWord);
			
			FeauterstWriter.flush();
			FeauterstWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
