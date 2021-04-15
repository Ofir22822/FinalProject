package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import com.textrazor.TextRazor;
import com.textrazor.annotations.AnalyzedText;

public class TextRazorController {

	static String API_KEY = "03f2bb1e5bfe25f7ca0e0a57288fb607b518b35d07f484da300cdc66";
	public static dbQuerys query;
	
	/***
	 * get text subject using textRazor API
	 * @param filePath
	 * @return subject as string
	 */
	public static String getTextSubject(File txtFile) {
		
		// use TextRazor API and initialize
		TextRazor client = new TextRazor(API_KEY);
		client.addExtractor("topics");
		client.setClassifiers(Arrays.asList("textrazor_newscodes"));
		AnalyzedText response;
		
		// write text in String
		String textString = FileFunctions.convertTextFileToString(txtFile);
		
		try {			
			// analyze in TextRazor
			response = client.analyze(textString);

			// check response from TextRazor
			if (response.isOk()) {
				if (response.getResponse().getCategories() != null)
					return response.getResponse().getCategories().get(0).getLabel();
				else {
					System.out.println(txtFile.getPath() + " error");
					return null;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * check if text subject exist in training set
	 * @param subject to check
	 * @return true if exist
	 */
	public static boolean checkExistSubject(String subject) {
		ArrayList<String> subjectList = query.getSubjectsList();
		return subjectList.contains(subject);
	}
	
	public static void setDbquery(dbQuerys queryNew) {
		query = queryNew;
	}

}
