package unitTest;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import application.DictionaryController;
import application.FileFunctions;
import application.dbQuerys;

public class DictionaryControllerTest {

	File WordsFile;
	List<String> WordsFileList;
	dbQuerys query;
	
	@Before
	public void setUp() throws Exception {
		WordsFile = new File("dictionary\\dictionarytWordsTest.txt");
		//WordsFileList = FileFunctions.convertTextFileToList(WordsFile);
		query = new dbQuerys();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetAllTrainigSetTexts_isSameListAsBefore() throws IOException {
		List<File> textsList[] = FileFunctions.getAllTrainigSetTexts(query);
		List<File> pList = textsList[0];
		List<File> peList = textsList[1];
		
		List<File> pList2 = new ArrayList<File>();
		List<File> peList2 = new ArrayList<File>();
		
		for (int i = 1; i <= 401; i++) {
			
			File dataFile = new File("testText\\t"+i+".txt");
			int res = query.getClassification("test_t"+i+".txt");

			if(res == 1)
				pList2.add(dataFile);
			else if(res == 2)
				peList2.add(dataFile);
			else
				System.out.print("error: t"+i+".txt");
		}
		
		assertTrue(pList2.equals(pList));
		assertTrue(peList2.equals(peList));
	}

}
