import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Scanner;

import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;

import uk.ac.wlv.sentistrength.*;

public class SentiStrengthApp {

	public static void main(String[] args) throws FileNotFoundException, NetworkException, AnalysisException {

		String sentimentFeature = "";
		String sentimentFeature2 = "";
		String sentimentFeature3 = "";
		String subject = "";
		for (int i = 0; i < 5; i++) {
			String filename = "HDP\\t" + (i + 1) + ".txt";
			String filename2 = "HDPe\\t" + (i + 1) + ".txt";
			String filename3 = "ThGPe\\t" + (i + 1) + ".txt";

			try {
				File myObj = new File("D:\\temp\\eclipse\\photon3\\projectStageB\\src\\" + filename);
				System.out.println("t" + (i + 1) + " HDP");
				String weightSentenceMat[][] = getSentimentFeature(filename);
				sentimentFeature = matrixToString(weightSentenceMat, 25);
				subject = textRazorFunctions.getTextSubject(filename);
				subject = (subject == null) ? "" : subject;
				dbQuerys.addNewText("t" + (i + 1), subject, "Promotion", sentimentFeature);
			} catch (Exception e) {

			}

			try {
				File myObj = new File("D:\\temp\\eclipse\\photon3\\projectStageB\\src\\" + filename2);
				System.out.println("t" + (i + 1) + " HDPe");
				String weightSentenceMat2[][] = getSentimentFeature(filename2);
				sentimentFeature2 = matrixToString(weightSentenceMat2, 25);
				subject = textRazorFunctions.getTextSubject(filename2);
				subject = (subject == null) ? "" : subject;
				dbQuerys.addNewText("t" + (i + 1), subject, "Personal", sentimentFeature2);
			} catch (Exception e) {

			}

			try {
				File myObj = new File("D:\\temp\\eclipse\\photon3\\projectStageB\\src\\" + filename3);
				System.out.println("t" + (i + 1) + " ThGPe");
				String weightSentenceMat3[][] = getSentimentFeature(filename3);
				sentimentFeature3 = matrixToString(weightSentenceMat3, 25);
				subject = textRazorFunctions.getTextSubject(filename3);
				subject = (subject == null) ? "" : subject;
				dbQuerys.addNewText("t" + (i + 1), subject, "Personal", sentimentFeature3);
			} catch (Exception e) {

			}

			/*
			 * for(int j =0;j<25;j++) { if(weightSentenceMat[1][j] !=null) sentimentFeature
			 * += weightSentenceMat[1][j] +" "; else sentimentFeature += "0 ";
			 * //System.out.print(weightSentenceMat[1][j] + " "); }
			 * 
			 * System.out.println(sentimentFeature);
			 */

			sentimentFeature = "";
			sentimentFeature2 = "";
			sentimentFeature3 = "";

		}
		/*
		 * System.out.println(""); for(int i =0;i<5;i++) { String filename =
		 * "pe\\t"+(i+1)+".txt"; String weightSentenceMat[][] =
		 * getSentimentFeature(filename);
		 * 
		 * for(int j =0;j<25;j++) { if(weightSentenceMat[1][j] !=null) sentimentFeature
		 * += weightSentenceMat[1][j] +" "; else sentimentFeature += "0 ";
		 * //System.out.print(weightSentenceMat[1][j] + " "); }
		 * 
		 * System.out.println(sentimentFeature);
		 * dbQuerys.addSentimentFeature("pe"+(i+1), sentimentFeature);
		 * 
		 * sentimentFeature = ""; }
		 */

	}

	public static String matrixToString(String Mat[][], int n) {
		String sentimentFeature = "";
		for (int j = 0; j < n; j++) {
			if (Mat[1][j] != null)
				sentimentFeature += Mat[1][j] + " ";
			else
				sentimentFeature += "0 ";
		}
		return sentimentFeature;
	}

	public static String[][] getSentimentFeature(String textFileName) {

		File myObj = new File("D:\\temp\\eclipse\\photon3\\projectStageB\\src\\" + textFileName);
		String weightSentenceCombine[] = { "1,-1", "1,-2", "1,-3", "1,-4", "1,-5", "2,-1", "2,-2", "2,-3", "2,-4",
				"2,-5", "3,-1", "3,-2", "3,-3", "3,-4", "3,-5", "4,-1", "4,-2", "4,-3", "4,-4", "4,-5", "5,-1", "5,-2",
				"5,-3", "5,-4", "5,-5" };
		String weightSentenceMat[][] = { { "0" }, new String[25] };
		weightSentenceMat[0] = weightSentenceCombine;

		SentiStrength sentiStrength = new SentiStrength();
		// Create an array of command line parameters to send (not text or file to
		// process)
		String ssthInitialisation[] = { "sentidata",
				"D:\\\\temp\\\\eclipse\\\\photon\\\\finalproject\\\\src\\\\SentStrength_Data\\\\", "scale" };
		String ssthInitialisation3[] = { "sentidata",
				"D:\\\\temp\\\\eclipse\\\\photon\\\\finalproject\\\\src\\\\SentStrength_Data\\\\", "explain" };
		sentiStrength.initialise(ssthInitialisation); // Initialise
		sentiStrength.initialise(ssthInitialisation3); // Initialise

		Scanner myReader;

		try {
			myReader = new Scanner(myObj);

			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();

				String result = sentiStrength.computeSentimentScores(data);
				String result2 = result;

				result2 = result2.substring(result2.indexOf("[sentence:") + 1);
				result2 = result2.substring(result2.indexOf(":") + 2, result2.indexOf("]"));

				// System.out.println(result2);
				int count = 0;
				// count sentence weight combination
				for (int i = 0; i < weightSentenceMat[0].length; i++) {
					// same weight
					if (result2.compareTo(weightSentenceMat[0][i]) == 0) {
						if (weightSentenceMat[1][i] != null)
							count = Integer.parseInt(weightSentenceMat[1][i]) + 1;
						else
							count = 1;
						weightSentenceMat[1][i] = "" + count;
						// System.out.println(weightSentenceMat[1][i]);
					}
				}
			}
			myReader.close();

		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
			return null;
		}

		return weightSentenceMat;

	}

	public static void main2(String[] args) {
		/*
		 * //Method 1: one-off classification (inefficient for multiple classifications)
		 * //Create an array of command line parameters, including text or file to
		 * process
		 * 
		 * String ssthInitialisationAndText[] = {"sentidata",
		 * "D:\\temp\\eclipse\\photon\\finalproject\\src\\SentStrength_Data\\", "
		 * text", myObj.toString(), "explain"};
		 * SentiStrength.main(ssthInitialisationAndText);
		 */

		// read text
		File myObj = new File("D:\\temp\\eclipse\\photon\\finalproject\\src\\pe\\t5.txt");
		Scanner myReader;
		int countSentece = 0, countWords = 0, totalcount = 0;
		int weightWords[] = new int[11];
		int weightSentence[] = new int[9];

		int weightSentencePositive[] = new int[5];
		int weightSentenceNegative[] = new int[5];

		String weightSentenceCombine[] = { "1,-1", "1,-2", "1,-3", "1,-4", "1,-5", "2,-1", "2,-2", "2,-3", "2,-4",
				"2,-5", "3,-1", "3,-2", "3,-3", "3,-4", "3,-5", "4,-1", "4,-2", "4,-3", "4,-4", "4,-5", "5,-1", "5,-2",
				"5,-3", "5,-4", "5,-5" };
		String weightSentenceMat[][] = { { "0" }, new String[25] };
		weightSentenceMat[0] = weightSentenceCombine;

		// Method 2: One initialisation and repeated classifications
		SentiStrength sentiStrength = new SentiStrength();
		// Create an array of command line parameters to send (not text or file to
		// process)
		String ssthInitialisation[] = { "sentidata",
				"D:\\\\temp\\\\eclipse\\\\photon\\\\finalproject\\\\src\\\\SentStrength_Data\\\\", "scale" };
		String ssthInitialisation3[] = { "sentidata",
				"D:\\\\temp\\\\eclipse\\\\photon\\\\finalproject\\\\src\\\\SentStrength_Data\\\\", "explain" };
		sentiStrength.initialise(ssthInitialisation); // Initialise
		sentiStrength.initialise(ssthInitialisation3); // Initialise

		// can now calculate sentiment scores quickly without having to initialise again
		try {
			myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				// System.out.println(data);

				String result = sentiStrength.computeSentimentScores(data);
				String result2 = result;

				result = result.substring(0, result.indexOf(".") + 1);
				result2 = result2.substring(result2.indexOf(".") + 1);
				result2 = result2.substring(result2.indexOf(":") + 2, result2.indexOf("]"));

				// System.out.println(result);
				System.out.println(result2);
				int count = 0;
				// count sentence weight combination
				for (int i = 0; i < weightSentenceMat[0].length; i++) {
					// same weight
					if (result2.compareTo(weightSentenceMat[0][i]) == 0) {
						if (weightSentenceMat[1][i] != null)
							count = Integer.parseInt(weightSentenceMat[1][i]) + 1;
						else
							count = 1;
						weightSentenceMat[1][i] = "" + count;
						// System.out.println(weightSentenceMat[1][i]);
					}
				}

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
							// System.out.println(wordsSenti[i].substring(wordsSenti[i].indexOf("[")+1,
							// wordsSenti[i].indexOf("]")));
							totalcount++;
						} catch (Exception e) {

						}
					}
				}

				countWords += words.length;
				countSentece++;

				int weight = Integer.parseInt(wordsSenti[2]);
				weightSentence[weight + 4]++;

				int index = Integer.parseInt(wordsSenti[0]) - 1;
				weightSentencePositive[index]++;

				index = -Integer.parseInt(wordsSenti[1]) - 1;
				weightSentenceNegative[index]++;

			}
			weightWords[5] = countWords - totalcount;
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		System.out.println(" ");
		System.out.println("countSentece: " + countSentece);

		// print

		for (int i = 0; i < weightSentenceMat[0].length; i++) {
			System.out.println(weightSentenceMat[0][i] + " : " + weightSentenceMat[1][i]);
		}

		for (int i = 0; i < weightSentencePositive.length; i++) {
			System.out.print((i + 1) + "|");
		}
		System.out.println(" ");
		for (int i = 0; i < weightSentencePositive.length; i++) {
			System.out.print(weightSentencePositive[i] + "\t");
		}
		System.out.println(" ");

		for (int i = 0; i < weightSentenceNegative.length; i++) {
			System.out.print(-(i + 1) + "|");
		}
		System.out.println(" ");
		for (int i = 0; i < weightSentenceNegative.length; i++) {
			System.out.print(weightSentenceNegative[i] + "\t");
		}

		/*
		 * for (int i = 0; i < weightSentence.length; i++) { System.out.print((i - 4) +
		 * "|"); } System.out.println(" "); for (int i = 0; i < weightSentence.length;
		 * i++) { System.out.print(weightSentence[i] + "\t"); }
		 * 
		 * System.out.println("\n\n"+"countWords: " + countWords); for (int i = 0; i <
		 * weightWords.length; i++) { System.out.print((i - 5) + "|"); }
		 * System.out.println(" "); for (int i = 0; i < weightWords.length; i++) {
		 * System.out.print(weightWords[i] + "\t"); }
		 * 
		 * System.out.println(sentiStrength.computeSentimentScores("I hate frogs."));
		 * System.out.println(sentiStrength.computeSentimentScores("I love dogs."));
		 */
	}

}
