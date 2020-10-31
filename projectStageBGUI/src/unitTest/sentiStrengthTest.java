package unitTest;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class sentiStrengthTest {

	File fileTest;
	String weights;

	@Before
	public void setUp() throws Exception {
		fileTest = new File("testTexts\\t1.txt");
		weights = "19 3 0 0 0 4 0 0 1 0 2 0 0 0 0 3 0 0 0 0 0 0 0 0 0 ";
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetSentimentFeature() {
		
		fail("Not yet implemented");
	}

	@Test
	public void testGetTextWordsSentiment() {
		fail("Not yet implemented");
	}

	@Test
	public void testSentimentMatrixToString() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateSentimentDataForKNN() {
		fail("Not yet implemented");
	}

}
