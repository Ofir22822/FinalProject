import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import com.textrazor.TextRazor;
import com.textrazor.account.AccountManager;
import com.textrazor.account.model.Account;
import com.textrazor.annotations.Custom;
import com.textrazor.annotations.Entity;
import com.textrazor.annotations.AnalyzedText;
import com.textrazor.annotations.ScoredCategory;
import com.textrazor.annotations.Sentence;
import com.textrazor.annotations.Topic;
import com.textrazor.annotations.Word;
import com.textrazor.annotations.Word.Sense;
import com.textrazor.annotations.Word.Suggestion;
import com.textrazor.classifier.ClassifierManager;
import com.textrazor.classifier.model.Category;
import com.textrazor.dictionary.DictionaryManager;
import com.textrazor.dictionary.model.Dictionary;
import com.textrazor.dictionary.model.DictionaryEntry;
import com.textrazor.dictionary.model.PagedAllEntries;


public class textRazorFunctions {

	static String API_KEY = "03f2bb1e5bfe25f7ca0e0a57288fb607b518b35d07f484da300cdc66";
	
	public static String getTextSubject2(String textFileName) throws FileNotFoundException, NetworkException, AnalysisException
	{
		TextRazor client = new TextRazor(API_KEY);
		
		client.addExtractor("topics");
		client.setClassifiers(Arrays.asList("textrazor_newscodes"));
		
		//read text file
		File myObj = new File("D:\\temp\\eclipse\\photon3\\projectStageB\\src\\"+textFileName);
		Scanner myReader;
		myReader = new Scanner(myObj);
		String textString = "";
		while (myReader.hasNextLine()) {
			textString += myReader.nextLine() + " ";}
		
		AnalyzedText response = client.analyze(textString);
		
		if(response.isOk())
		{		
			if(response.getResponse().getCategories() != null)
			{
				System.out.println(textFileName);
				for (ScoredCategory category : response.getResponse().getCategories()) {
					System.out.println(textFileName+": Category--------:" + category.getCategoryId() + " " + category.getLabel() + " " + category.getScore());
			
				}
				System.out.println("");
			}
			else
			{
				System.out.println(textFileName+ " error");
				return null;
			}
		}
		else
			return null;
		return null;
	}
	
	
	
	public static String getTextSubject(String textFileName) throws FileNotFoundException, NetworkException, AnalysisException
	{
		TextRazor client = new TextRazor(API_KEY);
		
		client.addExtractor("topics");
		client.setClassifiers(Arrays.asList("textrazor_newscodes"));
		
		//read text file
		File myObj = new File("D:\\temp\\eclipse\\photon3\\projectStageB\\src\\"+textFileName);
		Scanner myReader;
		myReader = new Scanner(myObj);
		String textString = "";
		while (myReader.hasNextLine()) {
			textString += myReader.nextLine() + " ";}
		
		AnalyzedText response = client.analyze(textString);
		
		if(response.isOk())
		{		
			if(response.getResponse().getCategories() != null)
			{
				return response.getResponse().getCategories().get(0).getLabel();
			}
			else
			{
				System.out.println(textFileName+ " error");
				return null;
			}
		}
		else
			return null;
	}
	
	
	public static void testAnalysis(String apiKey) throws NetworkException, AnalysisException, FileNotFoundException {
		// Sample request, showcasing a couple of TextRazor features
		
		TextRazor client = new TextRazor(apiKey);
		
		//client.addExtractor("topics");
		client.setClassifiers(Arrays.asList("textrazor_newscodes"));
		//client.addExtractor("senses");
		//client.addExtractor("entity_companies");	
		//client.setEnrichmentQueries(Arrays.asList("fbase:/location/location/geolocation>/location/geocode/latitude", "fbase:/location/location/geolocation>/location/geocode/longitude"));
		//String rules = "entity_companies(CompanyEntity) :- entity_type(CompanyEntity, 'Company').";
		//client.setRules(rules);

		//read text file
		File myObj = new File("D:\\temp\\eclipse\\photon3\\projectStageB\\src\\PE1\\t3.txt");
		Scanner myReader;
		myReader = new Scanner(myObj);
		String textString = "";
		while (myReader.hasNextLine()) {
			textString += myReader.nextLine() + " ";}
		
		//System.out.print(textString);
		
		//analyze string text for subject
		AnalyzedText response = client.analyze(textString);
		
		System.out.println(response.isOk());
		
		ScoredCategory category = response.getResponse().getCategories().get(0);
		System.out.println("Category--------:" + category.getCategoryId() + " " + category.getLabel() + " " + category.getScore());
		//for (ScoredCategory category : response.getResponse().getCategories()) {
		//	System.out.println("Category--------:" + category.getCategoryId() + " " + category.getLabel() + " " + category.getScore());
		//}
	
		 
	}
	
	
	public static void main(String[] args) throws NetworkException, AnalysisException, FileNotFoundException {
			
		//testAnalysis(API_KEY);
		
		for(int i =0;i<5;i++)
		{
			String filename = "HDP\\t"+(i+1)+".txt";
			getTextSubject(filename);	
		}
		System.out.println("");
		for(int i =0;i<5;i++)
		{
			String filename = "HDPe\\t"+(i+1)+".txt";
			getTextSubject(filename);
		}
		System.out.println("_______________________");
		for(int i =0;i<5;i++)
		{
			String filename = "p\\0"+(i+1)+".txt";
			getTextSubject(filename);	
		}
		System.out.println("");
		for(int i =0;i<5;i++)
		{
			String filename = "PE1\\t"+(i+1)+".txt";
			getTextSubject(filename);
		}
	}

}
