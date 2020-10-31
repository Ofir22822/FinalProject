package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import uk.ac.wlv.sentistrength.SentiStrength;

public class ConsoleMain {

	public static void main(String[] args) {
		
		File txt = new File("test2.txt");		
		
		File txtFile = new File("testText\\t1.txt");		
		String result = DictionaryController.createFrequencyFeature(txtFile).replace(' ', ',')+"P";
		
		System.out.println(result.substring(0, 50));
		
		textObject to = new textObject();
		to.setFrequencyFeature(DictionaryController.createFrequencyFeature(txt).replace(' ', ',')+"P");
		
		KNNController.createDictionaryData(to);
		
		KNNController.setKNNData("DictionaryDataKNN.txt");
		System.out.println(KNNController.KNN());
		
		/*
		dbQuerys query = new dbQuerys();
		
		for(int i = 1; i<=401; i++)
		{
			File txtFile = new File("testText\\t"+i+".txt");
		
			String result = DictionaryController.createFrequencyFeature(txtFile);
		
			query.addFrequencyFeature("test_t"+i+".txt", result);
		
			System.out.println("test_t"+i+".txt " + result);
		}
		*/
		
		
		/*
		// use SentiStrength API and initialize
		SentiStrength sentiStrength = new SentiStrength();
		// Create an array of command line parameters to send (not text or file to
		// process)
		String ssthInitialisation[] = { "sentidata", "resources\\SentStrength_Data\\", "scale" };
		String ssthInitialisation3[] = { "sentidata", "resources\\SentStrength_Data\\", "explain" };
		sentiStrength.initialise(ssthInitialisation); // Initialise
		sentiStrength.initialise(ssthInitialisation3); // Initialise
		
		for(int i = 1; i<=401; i++)
		{
		File txtFile = new File("testText\\t"+i+".txt");
		
		String result = SentiStrengthController.getTextWordsSentiment(txtFile);
		
		query.addWordsSentimentFeature("test_t"+i+".txt", result);
		
		//System.out.print(result);
		}
		
		try {
			query.closeConnectToDB();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		*/
		//createTextFiles();
		
		//File txt = new File("resources\\HDPe\\t1.txt");
		//String res = SentiStrengthController.sentimentMatrixToString(SentiStrengthController.getSentimentFeature(txt),25);
		//System.out.print(res);
		
		//KNNController.createDataFile();
		
		/*
		try {
			SentiStrengthController.createSentimentDataForKNN(new textObject());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		List<String> listTxtFiles = textfilesInFolder("testText");
		
		for(String filePath : listTxtFiles) {
			File txtFile = new File(filePath);
			addTextFileToDB(txtFile, "Promotion");
		}
		
		listTxtFiles = textfilesInFolder("HDPe");
		
		for(String filePath : listTxtFiles) {
			File txtFile = new File(filePath);
			addTextFileToDB(txtFile, "Pe");
		}
		*/
	}
	
	public static void createTextFiles() {
        String csvFile = "D:\\temp\\eclipse\\photon3\\projectStageBGUI\\resources\\text0400-TRAIN.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        
        try {

            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] textData = line.split(cvsSplitBy);

                //System.out.println(textData[2]);
                //System.out.println("********************************************************************");
                addTextFileToDB(createTextFile(textData[2]), textData[3]);		//2 - text , 3 - classification
                //System.out.println("********************************************************************");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
	}

	
	public static void createTypeArray() {
        String csvFile = "D:\\temp\\eclipse\\photon3\\projectStageBGUI\\resources\\text_test.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        
        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] country = line.split(cvsSplitBy);

                System.out.println(country[2]);
        }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
	}

	public static File createTextFile(String text) {
		int countText = 1;
		File textFile = null;
		String[] textLine = text.split("\\. "); //split text at dot - .
		File countFile = new File("testText\\counter.txt");
		try {
			//read text counter
			Scanner myReader = new Scanner(countFile);
			countText = Integer.parseInt(myReader.nextLine());
			myReader.close();
			//write text to file "wi.txt"
			FileWriter myWriter = new FileWriter("testText\\t"+countText+".txt");
			
			//write to text counter +1 to number
			countText++;
			FileWriter countWriter = new FileWriter("testText\\counter.txt");
			countWriter.write(""+countText);
			countWriter.close();
			
			//write lines from text in file
			for(String line : textLine)
			{
				//System.out.println(line);
				myWriter.write(line+".\n");
			}
			myWriter.close();
			textFile = new File("testText\\t"+(--countText)+".txt");

		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return textFile;
	}
		
	public static void addTextFileToDB(File txtFile, String Classification) {
		
		String weightSentenceMat[][] = SentiStrengthController.getSentimentFeature(txtFile);
		String sentimentFeature = SentiStrengthController.sentimentMatrixToString(weightSentenceMat, 25);
		String subject = TextRazorController.getTextSubject(txtFile);
		subject = (subject == null) ? "" : subject;
		
		dbQuerys query = new dbQuerys();
		String fileName = txtFile.getPath().substring(txtFile.getPath().lastIndexOf("\\")+1);
		query.addNewText("test_"+fileName, subject, Classification, sentimentFeature);
	}
	
	//get all .txt files from folder in list
	public static List<String> textfilesInFolder(String folderName) {
		try (Stream<Path> walk = Files.walk(Paths.get(folderName))) {

			List<String> result = walk.map(x -> x.toString())
					.filter(f -> f.endsWith(".txt")).collect(Collectors.toList());

			//result.forEach(System.out::println);
			return result;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
