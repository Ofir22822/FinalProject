package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * class for functions related to files
 *
 */
public class FileFunctions {

	/**
	 * get all *.txt files from folder
	 * 
	 * @param folderName folder with all texts
	 * @return files in file list
	 */
	public static List<File> getTextFilesInFolder(String folderName) {
		List<String> listTxtFilesPaths = null;
		List<File> listTxtFiles = new ArrayList<>();

		try (Stream<Path> walk = Files.walk(Paths.get(folderName))) {

			listTxtFilesPaths = walk.map(x -> x.toString()).filter(f -> f.endsWith(".txt"))
					.collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String filePath : listTxtFilesPaths) {
			File txtFile = new File(filePath);
			listTxtFiles.add(txtFile);
		}

		return listTxtFiles;
	}

	/**
	 * combine all text files to one file with all texts from files
	 * output file allText.txt
	 * @param fileList all texts to combine
	 */
	public static void combineAllTextFiles(List<File> fileList)
	{
		String textFromFiles = "";
		
		for(File fileI : fileList)
		{
			textFromFiles += convertTextFileToString(fileI);
		}
			
		createTextFile("allText.txt", textFromFiles);
	}

	/**
	 * create new text file from string
	 * @param fileName name of new file, with .txt ending
	 * @param text text to write in file
	 */
	public static void createTextFile(String fileName, String text) {
		
		File newFile = new File(fileName);
		PrintWriter outFile;
		try {
			outFile = new PrintWriter(newFile);
			outFile.print(text);
			outFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/***
	 * write to text file
	 * @param fileName file path
	 * @param text text to write in file
	 */
	public static void writeToTextFile(String fileName, String text) {
		
		File newFile = new File(fileName);
		FileWriter outFile;
		try {
			outFile = new FileWriter(newFile, true);
			outFile.write(text);
			outFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * create new text file from lines Collection object
	 * @param fileName name of new file, with .txt ending
	 * @param lines Collection object with type String
	 */
	public static void createTextFile(String fileName, Collection<String> lines) {
		
		File newFile = new File(fileName);
		PrintWriter outFile;
		try {
			outFile = new PrintWriter(newFile);
			for(String line : lines)
				outFile.println(line);
			outFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	

	/***
	 * convert text in file to string
	 * @param textFile file to get text from
	 * @return string with all text from file
	 */
	public static String convertTextFileToString(File textFile) {

		String textString = "";

		Scanner myReader;
		try {
			myReader = new Scanner(new FileInputStream(textFile));
			while (myReader.hasNextLine()) {
				textString += myReader.nextLine() + "\n";
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return textString;
	}

	/***
	 * convert text to list of sentences
	 * @param textFile file to get text from
	 * @return list with all sentences as string
	 */
	public static List<String> convertTextFileToList(File textFile) {

		List<String> listString = new ArrayList<>();

		Scanner myReader;
		try {
			myReader = new Scanner(textFile);
			while (myReader.hasNextLine()) {
				listString.add(myReader.nextLine());
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return listString;
	}

}
