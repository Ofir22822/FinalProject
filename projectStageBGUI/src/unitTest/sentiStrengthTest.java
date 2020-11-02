package unitTest;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import application.FileFunctions;
import application.SentiStrengthController;
import application.dbQuerys;
import application.textObject;

public class sentiStrengthTest {

	File fileTest;
	String fileWeights;
	String fileWordsWeights;
	dbQuerys query;
	textObject to;
	
	@Before
	public void setUp() throws Exception {
		fileTest = new File("testText\\t1.txt");
		fileWeights = "19 3 0 0 0 4 0 0 1 0 2 0 0 0 0 3 0 0 0 0 0 0 0 0 0 ";
		fileWordsWeights = "0 1 0 4 0 1133 0 8 9 5 0 ";
		query = new dbQuerys();
		to = new textObject();
		to.setSentimentFeatureSentence("19 3 0 0 0 4 0 0 1 0 2 0 0 0 0 3 0 0 0 0 0 0 0 0 0 ");
		to.setSentimentFeatureWord("0 1 0 4 0 1133 0 8 9 5 0 ");
	}

	@After
	public void tearDown() throws Exception {
	}

	/*
	@Test
	public void testGetSentimentFeatureSentences() {
		
		String resultMatrix[][] = SentiStrengthController.getSentimentFeatureSentences(fileTest);
		String result = SentiStrengthController.sentimentMatrixToString(resultMatrix, 25);
		assertTrue(fileWeights.equals(result));
	}

	@Test
	public void testGetSentimentFeatureWords() {
		String result = SentiStrengthController.getSentimentFeatureWords(fileTest);
		assertTrue(fileWordsWeights.equals(result));
	}

	@Test
	public void testSentimentMatrixToString() {
		//fail("Not yet implemented");
	}
*/
	
	@Test
	public void testCreateSentimentDataForKNN() {
		//SentiStrengthController.initializeSentimentKNNDataFile();
	}

	
	
}
