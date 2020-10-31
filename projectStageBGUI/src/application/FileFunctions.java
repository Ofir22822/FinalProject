package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileFunctions {

	/**
	 * get all *.txt files from folder
	 * 
	 * @param folderName
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
	 * output file - allText.txt
	 * @param fileList
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
	 * @param fileName - name of new file, with .txt ending
	 * @param text - text to write in file
	 */
	public static void createTextFile(String fileName, String text) {
		
		File newFile = new File(fileName);
		PrintWriter outFile;
		try {
			outFile = new PrintWriter(newFile);
			outFile.println(text);
			outFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * create new text file from lines Collection object
	 * @param fileName - name of new file, with .txt ending
	 * @param lines - Collection object with type String
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
	
	/**
	 * convert text in file to string
	 * @return string with all text from file
	 */
	public static String convertTextFileToString(File textFile) {

		String textString = "";

		Scanner myReader;
		try {
			myReader = new Scanner(textFile);
			while (myReader.hasNextLine()) {
				textString += myReader.nextLine() + "\n";
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return textString;
	}

}
