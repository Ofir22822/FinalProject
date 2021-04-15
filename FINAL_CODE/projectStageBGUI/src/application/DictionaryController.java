package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.util.regex.Pattern;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

/**
 * class for dictionary related functions
 *
 */
public class DictionaryController {

	public static dbQuerys query;

	/* preprocessing variables */

	protected static StanfordCoreNLP pipeline;
	protected static Properties props = new Properties();
	static {
		props.put("annotators", "tokenize, ssplit, pos, lemma");
		pipeline = new StanfordCoreNLP(props);
	}
	private static HashMap<String, Serializable> irregularSingulars = new HashMap<String, Serializable>(100);
	private static HashMap<String, Serializable> irregularPlurals = new HashMap<String, Serializable>(100);
	static {
		irregularSingulars.put("ache", "aches");
		irregularSingulars.put("alumna", "alumnae");
		irregularSingulars.put("alumnus", "alumni");
		irregularSingulars.put("axis", "axes");
		irregularSingulars.put("bison", "bison");
		ArrayList<String> busses = new ArrayList<String>(2);
		busses.add("buses");
		busses.add("busses");
		irregularSingulars.put("bus", busses);
		irregularSingulars.put("calf", "calves");
		irregularSingulars.put("caribou", "caribou");
		irregularSingulars.put("child", "children");
		irregularSingulars.put("datum", "data");
		irregularSingulars.put("deer", "deer");
		ArrayList<String> dice = new ArrayList<String>(2);
		dice.add("dies");
		dice.add("dice");
		irregularSingulars.put("die", dice);
		irregularSingulars.put("elf", "elves");
		irregularSingulars.put("elk", "elk");
		irregularSingulars.put("fish", "fish");
		irregularSingulars.put("foot", "feet");
		irregularSingulars.put("gentleman", "gentlemen");
		irregularSingulars.put("gentlewoman", "gentlewomen");
		irregularSingulars.put("go", "goes");
		irregularSingulars.put("goose", "geese");
		irregularSingulars.put("grouse", "grouse");
		irregularSingulars.put("half", "halves");
		ArrayList<String> hoof = new ArrayList<String>(2);
		hoof.add("hooves");
		hoof.add("hoofs");
		irregularSingulars.put("hoof", hoof);
		irregularSingulars.put("knife", "knives");
		irregularSingulars.put("leaf", "leaves");
		irregularSingulars.put("life", "lives");
		irregularSingulars.put("louse", "lice");
		irregularSingulars.put("man", "men");
		irregularSingulars.put("money", "monies");
		irregularSingulars.put("moose", "moose");
		irregularSingulars.put("mouse", "mice");
		ArrayList<String> octopus = new ArrayList<String>(3);
		octopus.add("octopodes");
		octopus.add("octopi");
		octopus.add("octopuses");
		irregularSingulars.put("octopus", octopus);
		irregularSingulars.put("ox", "oxen");
		irregularSingulars.put("plus", "pluses");
		irregularSingulars.put("quail", "quail");
		irregularSingulars.put("reindeer", "reindeer");
		ArrayList<String> scarf = new ArrayList<String>(2);
		scarf.add("scarves");
		scarf.add("scarfs");
		irregularSingulars.put("scarf", scarf);
		irregularSingulars.put("self", "selves");
		irregularSingulars.put("sheaf", "sheaves");
		irregularSingulars.put("sheep", "sheep");
		irregularSingulars.put("shelf", "shelves");
		irregularSingulars.put("squid", "squid");
		irregularSingulars.put("thief", "thieves");
		irregularSingulars.put("tooth", "teeth");
		irregularSingulars.put("wharf", "wharves");
		irregularSingulars.put("wife", "wives");
		irregularSingulars.put("wolf", "wolves");
		irregularSingulars.put("woman", "women");

		irregularPlurals.put("aches", "ache");
		irregularPlurals.put("alumnae", "alumna");
		irregularPlurals.put("alumni", "alumnus");
		ArrayList<String> axes = new ArrayList<String>(2);
		axes.add("axe");
		axes.add("axis");
		irregularPlurals.put("axes", axes);
		irregularPlurals.put("bison", "bison");
		irregularPlurals.put("buses", "bus");
		irregularPlurals.put("busses", "bus");
		irregularPlurals.put("brethren", "brother");
		irregularPlurals.put("caches", "cache");
		irregularPlurals.put("calves", "calf");
		irregularPlurals.put("cargoes", "cargo");
		irregularPlurals.put("caribou", "caribou");
		irregularPlurals.put("children", "child");
		irregularPlurals.put("data", "datum");
		irregularPlurals.put("deer", "deer");
		irregularPlurals.put("dice", "die");
		irregularPlurals.put("dies", "die");
		irregularPlurals.put("dominoes", "domino");
		irregularPlurals.put("echoes", "echo");
		irregularPlurals.put("elves", "elf");
		irregularPlurals.put("elk", "elk");
		irregularPlurals.put("embargoes", "embargo");
		irregularPlurals.put("fish", "fish");
		irregularPlurals.put("feet", "foot");
		irregularPlurals.put("gentlemen", "gentleman");
		irregularPlurals.put("gentlewomen", "gentlewoman");
		irregularPlurals.put("geese", "goose");
		irregularPlurals.put("goes", "go");
		irregularPlurals.put("grottoes", "grotto");
		irregularPlurals.put("grouse", "grouse");
		irregularPlurals.put("halves", "half");
		irregularPlurals.put("hooves", "hoof");
		irregularPlurals.put("knives", "knife");
		irregularPlurals.put("leaves", "leaf");
		irregularPlurals.put("lives", "life");
		irregularPlurals.put("lice", "louse");
		irregularPlurals.put("men", "man");
		irregularPlurals.put("monies", "money");
		irregularPlurals.put("moose", "moose");
		irregularPlurals.put("mottoes", "motto");
		irregularPlurals.put("mice", "mouse");
		irregularPlurals.put("octopi", "octopus");
		irregularPlurals.put("octopodes", "octopus");
		irregularPlurals.put("octopuses", "octopus");
		irregularPlurals.put("oxen", "ox");
		irregularPlurals.put("pies", "pie");
		irregularPlurals.put("pluses", "plus");
		irregularPlurals.put("posses", "posse");
		irregularPlurals.put("potatoes", "potato");
		irregularPlurals.put("quail", "quail");
		irregularPlurals.put("reindeer", "reindeer");
		irregularPlurals.put("scarves", "scarf");
		irregularPlurals.put("sheaves", "sheaf");
		irregularPlurals.put("sheep", "sheep");
		irregularPlurals.put("shelves", "shelf");
		irregularPlurals.put("squid", "squid");
		irregularPlurals.put("teeth", "tooth");
		irregularPlurals.put("thieves", "thief");
		irregularPlurals.put("ties", "tie");
		irregularPlurals.put("tomatoes", "tomato");
		irregularPlurals.put("wharves", "wharf");
		irregularPlurals.put("wives", "wife");
		irregularPlurals.put("wolves", "wolf");
		irregularPlurals.put("women", "woman");
	} // end static initialization block

	private static int avg_T = 1;
	private static double corr_start_T = 0.03;
	private static double corr_end_T = 0.1;
	
	/* preprocessing helping functions */


	public static int getAvg_T() {
		return avg_T;
	}
	
	/**
	 * set average parameter
	 * @param avg_T new value
	 */
	public static void setAvg_T(int avg_T) {
		DictionaryController.avg_T = avg_T;
	}

	public static double getCorr_start_T() {
		return corr_start_T;
	}
	
	/***
	 * set correlation threshold start
	 * @param corr_start_T new value
	 */
	public static void setCorr_start_T(double corr_start_T) {
		DictionaryController.corr_start_T = corr_start_T;
	}

	public static double getCorr_end_T() {
		return corr_end_T;
	}
	
	/***
	 * set correlation threshold end
	 * @param corr_end_T new value
	 */
	public static void setCorr_end_T(double corr_end_T) {
		DictionaryController.corr_end_T = corr_end_T;
	}

	private static List<String> lemmatize(String documentText) {
		List<String> lemmas = new LinkedList<String>(); // Create an empty Annotation just with the given text
		Annotation document = new Annotation(documentText); // run all Annotators on this text
		pipeline.annotate(document); // Iterate over all of the sentences found
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for (CoreMap sentence : sentences) { // Iterate over all tokens in a sentence
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				// Retrieve and add the lemma for each word into the
				// list of lemmas
				lemmas.add(token.get(LemmaAnnotation.class));
			}
		}

		return lemmas;
	}

	private static List<String> preprocessingText(String text) {
		String[] stopWords = readStopWords();

		@SuppressWarnings("resource")
		Scanner textFileScanner = new Scanner(text);

		textFileScanner.useDelimiter(Pattern.compile("[ \n\r\t,.;:?!'\"]+"));

		text = text.replaceAll("[^a-zA-Z ]", "").toLowerCase();

		text = removeStopwords(text, Arrays.asList(stopWords));

		List<String> textWordsTokens = lemmatize(text);
		return textWordsTokens;
	}

	/**
	 * remove stopwords form text
	 * 
	 * @param original text as string
	 * @param stopwords list of words to remove
	 * @return
	 */

	private static String removeStopwords(String original, List<String> stopwords) {
		// String original = "The quick brown fox jumps over the lazy dog";
		ArrayList<String> allWords = Stream.of(original.toLowerCase().split(" "))
				.collect(Collectors.toCollection(ArrayList<String>::new));
		allWords.removeAll(stopwords);

		String result = allWords.stream().collect(Collectors.joining(" "));
		return result;
	}

	/**
	 * read english_stopwords.txt file, and put all words in string array
	 * 
	 * @return stopwords in string array
	 */
	private static String[] readStopWords() {
		List<String> stopWords = new ArrayList<String>();
		try {
			File WordsFile = new File("dictionary\\english_stopwords.txt");
			Scanner stopWordsScanner = new Scanner(WordsFile);

			while (stopWordsScanner.hasNextLine())
				stopWords.add(stopWordsScanner.nextLine());

			stopWordsScanner.close();
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}

		return stopWords.toArray(new String[0]);
	}

	/* function to create words token file from text files */

	/**
	 * create all tokens words file from all texts in testText folder
	 */
	private static void createAllTextsWordsTokensFile() {
		List<File> textFilesList = FileFunctions.getTextFilesInFolder("testText");
		textFilesList.remove(0); // remove counter file
		FileFunctions.combineAllTextFiles(textFilesList);

		File allText = new File("allText.txt");
		String allTextString = FileFunctions.convertTextFileToString(allText);

		List<String> allTextsWordsTokens = preprocessingText(allTextString);
		Set<String> dictionaryWords = new HashSet<>();
		dictionaryWords.addAll(allTextsWordsTokens);

		FileFunctions.createTextFile("dictionary\\AllWordsTokens.txt", dictionaryWords);
	}

	/**
	 * read AllWordsTokens.txt and return words tokens in string list
	 * 
	 * @return list with all words tokens 
	 */
	private static List<String> getAllWordsTokens() {
		File dataFile = new File("dictionary\\AllWordsTokens.txt");
		Scanner myReader;
		List<String> dictionryWords = new ArrayList<String>();
		try {
			myReader = new Scanner(dataFile);
			while (myReader.hasNextLine()) {
				dictionryWords.add(myReader.nextLine());
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return dictionryWords;
	}

	private static void createAllWordsTokensFile() {
		// List<File> textFileList = FileFunctions.getTextFilesInFolder("testText");

		List<File> textFileList = new ArrayList<File>();

		List<File> textsList[] = new ArrayList[4]; // FileFunctions.getAllTrainigSetTexts(query);

		textsList[0] = FileFunctions.getTextFilesInFolder("split\\P");
		textsList[1] = FileFunctions.getTextFilesInFolder("split\\PE");
		
		//textsList[0] = FileFunctions.getTextFilesInFolder("textGames\\train\\P");
		//textsList[1] = FileFunctions.getTextFilesInFolder("textGames\\train\\PE");
		
		//textsList[0] = FileFunctions.getTextFilesInFolder("testText\\P");
		//textsList[1] = FileFunctions.getTextFilesInFolder("testText\\PE");

		 //textsList[2] = FileFunctions.getTextFilesInFolder("textGames\\test\\P");
		 //textsList[3] = FileFunctions.getTextFilesInFolder("textGames\\test\\PE");

		 //textsList[0].addAll(textsList[2]);
		 //textsList[1].addAll(textsList[3]);

		textFileList.addAll(textsList[0]);
		textFileList.addAll(textsList[1]);

		// textFileList.remove(0);
		Set<String> tokens = new HashSet<String>();
		List<String> tokensClassification = new ArrayList<String>();

		Set<String> tokensP = new HashSet<String>();
		Set<String> tokensE = new HashSet<String>();

		for (File fileI : textsList[0]) {
			System.out.println(fileI.getPath());
			String fileText = FileFunctions.convertTextFileToString(fileI);
			List<String> newTokens = preprocessingText(fileText);
			tokensP.addAll(newTokens);
			for (String token : newTokens)
				tokensClassification.add("Promotion");
		}

		for (File fileI : textsList[1]) {
			System.out.println(fileI.getPath());
			String fileText = FileFunctions.convertTextFileToString(fileI);
			List<String> newTokens = preprocessingText(fileText);
			tokensE.addAll(newTokens);
			for (String token : newTokens)
				tokensClassification.add("Experiance");
		}

		tokens.addAll(tokensE);
		// tokens.addAll(tokensP);
		FileFunctions.createTextFile("tokensP.txt", tokensP);
		FileFunctions.createTextFile("tokensE.txt", tokensE);
		FileFunctions.createTextFile("tokensTest.txt", tokens);
		FileFunctions.createTextFile("tokensClass.txt", tokensClassification);
	}

	/* dictionary */

	/**
	 * build frequency matrix form texts and words
	 * 
	 * @param listText        - list of text to check
	 * @param dictionaryWords - words to check frequency of
	 * @return matrix[i][j] = ( text i, word j ) frequency value, if word not in
	 *         text put 1
	 */

	/**
	 * build frequency matrix of words using texts
	 * @param listText texts to to calculate words frequency values
	 * @param dictionaryWords words to find frequency of
	 * @return matrix with frequencies of words, columns words, rows texts
	 */
	public static Integer[][] buildFrequencyMatrix(List<File> listText, List<String> dictionaryWords) {
		Integer[][] freqMatrix = new Integer[listText.size()][dictionaryWords.size()];

		for (int i = 0; i < listText.size(); i++) {

			// create tokens from text i in list
			String textI = FileFunctions.convertTextFileToString(listText.get(i));
			List<String> textTokens = preprocessingText(textI);

			for (int j = 0; j < dictionaryWords.size(); j++) {
				// calculate how much word j from dictionary appears in text i
				freqMatrix[i][j] = Collections.frequency(textTokens, dictionaryWords.get(j));
				// if (freqMatrix[i][j] == 0)
				// freqMatrix[i][j] = 1;
			}
		}
		return freqMatrix;
	}

	private static void frequencyAllText() {
		List<String> dictionaryWords = getAllWordsTokens();

		File allText = new File("allText.txt");
		String allTextString = FileFunctions.convertTextFileToString(allText);

		List<String> allWordsToCheck = preprocessingText(allTextString); // preprocessing to words

		HashMap<String, Integer> wordsFrequency = new HashMap<>();

		for (int i = 0; i < dictionaryWords.size(); i++) {
			int frequency = Collections.frequency(Arrays.asList(allWordsToCheck), dictionaryWords.get(i));

			if (frequency > 50)
				wordsFrequency.put(dictionaryWords.get(i), frequency);

		}

		List<String> words = new ArrayList<String>();
		for (String keyI : wordsFrequency.keySet())
			words.add(keyI + " : " + wordsFrequency.get(keyI));

		FileFunctions.createTextFile("dictionary\\newWordsDictionary.txt", words);
	}

	/***
	 * build dictionary using average parameter and correlation
	 */
	public static void frequencyAvg() {

		List<String> listout = new ArrayList<>();

		int count2 = 1;
		String wordsAvg = "";
		// texts list - promotion, personal experinect
		List<File> textsList[] = new ArrayList[4]; // FileFunctions.getAllTrainigSetTexts(query);

		textsList[0] = FileFunctions.getTextFilesInFolder("split\\P");
		textsList[1] = FileFunctions.getTextFilesInFolder("split\\PE");
		
		//textsList[0] = FileFunctions.getTextFilesInFolder("textGames\\train\\P");
		//textsList[1] = FileFunctions.getTextFilesInFolder("textGames\\train\\PE");

		//textsList[0] = FileFunctions.getTextFilesInFolder("testText\\P");
		//textsList[1] = FileFunctions.getTextFilesInFolder("testText\\PE");
		
		 //textsList[2] = FileFunctions.getTextFilesInFolder("textGames\\test\\P");
		 //textsList[3] = FileFunctions.getTextFilesInFolder("textGames\\test\\PE");

		 //textsList[0].addAll(textsList[2]);
		 //textsList[1].addAll(textsList[3]);

		List<File> pTextsList = textsList[0];
		List<File> peTextsList = textsList[1];

		// words tokens from texts
		File tokenFile = new File("tokensTest.txt");
		List<String> wordsTokens = FileFunctions.convertTextFileToList(tokenFile);
		File tokenClass = new File("tokensClass.txt");
		List<String> wordsTokensClass = FileFunctions.convertTextFileToList(tokenClass);

		// calculate frequency
		Integer[][] pFreqMatrix = buildFrequencyMatrix(pTextsList, wordsTokens);
		Integer[][] peFreqMatrix = buildFrequencyMatrix(peTextsList, wordsTokens);

		int maxLengthVector = peFreqMatrix.length;

		double[] pVector = new double[maxLengthVector];
		double[] peVector = new double[maxLengthVector];

		List<String> values = new ArrayList<String>();
		List<String> valuesFreq = new ArrayList<String>();
		
		
		for (int i = 0; i < wordsTokens.size(); i++) {

			String valueI = "";
			String valueIFreq = "";
			valueIFreq = wordsTokens.get(i)+",";
			// get word from 2 matrix (2 vectors) //frequency words
			for (int j = 0; j < maxLengthVector; j++) {
				if (j > pFreqMatrix.length)
					pVector[j] = 0;
				else
					pVector[j] = pFreqMatrix[j][i];

				if (j > peFreqMatrix.length)
					peVector[j] = 0;
				else {
					try {
						peVector[j] = peFreqMatrix[j][i];
					} catch (Exception e) {
						peVector[j] = 0;
					}
				}
			}

			for(double freqI : pVector)
			{
				valueIFreq+= freqI+",";
			}
			
			for(double freqI : peVector)
			{
				valueIFreq+= freqI+",";
			}

			valuesFreq.add(valueIFreq);
			// System.out.println("");

			int sumFreq = 0;
			int sumFreqP = 0;
			int sumFreqPE = 0;

			int sumFreqTP = 0;
			int sumFreqTE = 0;

			for (double value : pVector) {
				sumFreq += value;
				sumFreqP += value;
				if (value > 1)
					sumFreqTP++;
			}
			for (double value : peVector) {
				sumFreq += value;
				sumFreqPE += value;
				if (value > 1)
					sumFreqTE++;
			}

			if (sumFreqTE == 0)
				sumFreqTE = sumFreqPE;
			if (sumFreqTP == 0)
				sumFreqTP = sumFreqP;

			valueI = "" + wordsTokens.get(i) + "," + "" + sumFreqP + "" + "," + sumFreqPE + ",";

			double freqAvgP = (sumFreqP * 1.0) / maxLengthVector;
			double freqAvgPE = (sumFreqPE * 1.0) / maxLengthVector;
			//double freqAvgTP = (sumFreqP * 1.0) / sumFreqTP;
			//double freqAvgTE = (sumFreqPE * 1.0) / sumFreqTE;

			valueI += "" + freqAvgP + "" + "," + freqAvgPE + ",";// + "" + freqAvgTP + "" + "," + freqAvgTE + ",";

			// if(Math.abs(freqAvgTP-freqAvgTE) > 3){
			if (freqAvgPE > avg_T || freqAvgP > avg_T) {
				wordsAvg += wordsTokens.get(i) + "\tfreqAvgP: " + freqAvgP + "\tfreqAvgPE: " + freqAvgPE + "\n";

				// spearman & threshold
				SpearmansCorrelation sp = new SpearmansCorrelation();
				double spResult = sp.correlation(pVector, peVector);
				valueI += "" + spResult + ",";
				if (!Double.isNaN(spResult)) {
					// System.out.println(count + " " + wordsTokens.get(i) + " - Correlation:" +
					// spResult + "");

					if (wordsTokens.get(i).length() >= 2) // word more then 2 letters
					{
						if (Math.abs(spResult) < corr_start_T && Math.abs(spResult) > corr_end_T) {
							wordsAvg += "spResult" + wordsTokens.get(i) + "\tfreqAvgP: " + freqAvgP + "\tfreqAvgPE: "
									+ freqAvgPE + "\n";
							query.addWordsToDictionary(wordsTokens.get(i), sumFreq, spResult, sumFreqP, sumFreqPE);
							count2++;
							valueI += "true,";
						} else {
							listout.add(wordsTokens.get(i));// System.out.println(wordsTokens.get(i));
							valueI += "false,";
						}
					} else {
						listout.add(wordsTokens.get(i));// System.out.println(wordsTokens.get(i));
						valueI += "false,";
					}
				} else {
					wordsAvg += "spResult" + wordsTokens.get(i) + "\tfreqAvgP: " + freqAvgP + "\tfreqAvgPE: "
							+ freqAvgPE + "\n";
					query.addWordsToDictionary(wordsTokens.get(i), sumFreq, -2, sumFreqP, sumFreqPE);
					count2++;
					valueI += "true,";
				}
				wordsAvg += "\n";

				// add word to Dictionary if needed

				values.add(valueI);

			}
			else
			{
				valueI += "no correlation,";
				valueI += "false,";
				values.add(valueI);
			}
		}
		wordsAvg = count2 + "\n" + wordsAvg;
		FileFunctions.createTextFile("wordsAvg.txt", wordsAvg);

		FileFunctions.createTextFile("words_out.txt", listout);

		FileFunctions.createTextFile("words_values.csv", values);

		FileFunctions.createTextFile("words_values_freq.csv", valuesFreq);
	}

	private static void frequencyAvgNoAvg() {

		List<String> listout = new ArrayList<>();

		int count2 = 1;
		String wordsAvg = "";
		// texts list - promotion, personal experinect
		List<File> textsList[] = new ArrayList[4]; // FileFunctions.getAllTrainigSetTexts(query);

		textsList[0] = FileFunctions.getTextFilesInFolder("textGames\\train\\P");
		textsList[1] = FileFunctions.getTextFilesInFolder("textGames\\train\\PE");

		//textsList[0] = FileFunctions.getTextFilesInFolder("testText\\P");
		//textsList[1] = FileFunctions.getTextFilesInFolder("testText\\PE");
		
		// textsList[2] = FileFunctions.getTextFilesInFolder("textGames\\test\\P");
		// textsList[3] = FileFunctions.getTextFilesInFolder("textGames\\test\\PE");

		// textsList[0].addAll(textsList[2]);
		// textsList[1].addAll(textsList[3]);

		List<File> pTextsList = textsList[0];
		List<File> peTextsList = textsList[1];

		// words tokens from texts
		File tokenFile = new File("tokensTest.txt");
		List<String> wordsTokens = FileFunctions.convertTextFileToList(tokenFile);
		File tokenClass = new File("tokensClass.txt");
		List<String> wordsTokensClass = FileFunctions.convertTextFileToList(tokenClass);

		// calculate frequency
		Integer[][] pFreqMatrix = buildFrequencyMatrix(pTextsList, wordsTokens);
		Integer[][] peFreqMatrix = buildFrequencyMatrix(peTextsList, wordsTokens);

		int maxLengthVector = peFreqMatrix.length;

		double[] pVector = new double[maxLengthVector];
		double[] peVector = new double[maxLengthVector];

		List<String> values = new ArrayList<String>();
		List<String> valuesFreq = new ArrayList<String>();
		
		
		for (int i = 0; i < wordsTokens.size(); i++) {

			String valueI = "";
			String valueIFreq = "";
			valueIFreq = wordsTokens.get(i)+",";
			// get word from 2 matrix (2 vectors) //frequency words
			for (int j = 0; j < maxLengthVector; j++) {
				if (j > pFreqMatrix.length)
					pVector[j] = 0;
				else
					pVector[j] = pFreqMatrix[j][i];

				if (j > peFreqMatrix.length)
					peVector[j] = 0;
				else {
					try {
						peVector[j] = peFreqMatrix[j][i];
					} catch (Exception e) {
						peVector[j] = 0;
					}
				}
			}

			for(double freqI : pVector)
			{
				valueIFreq+= freqI+",";
			}
			
			for(double freqI : peVector)
			{
				valueIFreq+= freqI+",";
			}

			valuesFreq.add(valueIFreq);
			// System.out.println("");

			int sumFreq = 0;
			int sumFreqP = 0;
			int sumFreqPE = 0;

			int sumFreqTP = 0;
			int sumFreqTE = 0;

			for (double value : pVector) {
				sumFreq += value;
				sumFreqP += value;
				if (value > 1)
					sumFreqTP++;
			}
			for (double value : peVector) {
				sumFreq += value;
				sumFreqPE += value;
				if (value > 1)
					sumFreqTE++;
			}

			if (sumFreqTE == 0)
				sumFreqTE = sumFreqPE;
			if (sumFreqTP == 0)
				sumFreqTP = sumFreqP;

			valueI = "" + wordsTokens.get(i) + "," + "" + sumFreqP + "" + "," + sumFreqPE + ",";

			double freqAvgP = (sumFreqP * 1.0) / maxLengthVector;
			double freqAvgPE = (sumFreqPE * 1.0) / maxLengthVector;
			//double freqAvgTP = (sumFreqP * 1.0) / sumFreqTP;
			//double freqAvgTE = (sumFreqPE * 1.0) / sumFreqTE;

			valueI += "" + freqAvgP + "" + "," + freqAvgPE + ",";// + "" + freqAvgTP + "" + "," + freqAvgTE + ",";

			// if(Math.abs(freqAvgTP-freqAvgTE) > 3){
			
				wordsAvg += wordsTokens.get(i) + "\tfreqAvgP: " + freqAvgP + "\tfreqAvgPE: " + freqAvgPE + "\n";

				// spearman & threshold
				SpearmansCorrelation sp = new SpearmansCorrelation();
				double spResult = sp.correlation(pVector, peVector);
				valueI += "" + spResult + ",";
				if (!Double.isNaN(spResult)) {
					// System.out.println(count + " " + wordsTokens.get(i) + " - Correlation:" +
					// spResult + "");

					if (wordsTokens.get(i).length() >= 2) // word more then 2 letters
					{
						if (Math.abs(spResult) < 0.1 && Math.abs(spResult) > 0.03) {
							wordsAvg += "spResult" + wordsTokens.get(i) + "\tfreqAvgP: " + freqAvgP + "\tfreqAvgPE: "
									+ freqAvgPE + "\n";
							query.addWordsToDictionary(wordsTokens.get(i), sumFreq, spResult, sumFreqP, sumFreqPE);
							count2++;
							valueI += "true,";
						} else {
							listout.add(wordsTokens.get(i));// System.out.println(wordsTokens.get(i));
							valueI += "false,";
						}
					} else {
						listout.add(wordsTokens.get(i));// System.out.println(wordsTokens.get(i));
						valueI += "false,";
					}
				} else {
					wordsAvg += "spResult" + wordsTokens.get(i) + "\tfreqAvgP: " + freqAvgP + "\tfreqAvgPE: "
							+ freqAvgPE + "\n";
					query.addWordsToDictionary(wordsTokens.get(i), sumFreq, -2, sumFreqP, sumFreqPE);
					count2++;
					valueI += "true,";
				}
				wordsAvg += "\n";

				// add word to Dictionary if needed

				values.add(valueI);

			

		}
		wordsAvg = count2 + "\n" + wordsAvg;
		FileFunctions.createTextFile("wordsAvg.txt", wordsAvg);

		FileFunctions.createTextFile("words_out.txt", listout);

		FileFunctions.createTextFile("words_values.csv", values);

		FileFunctions.createTextFile("words_values_freq.csv", valuesFreq);
	}

	private static void buildDictionary() throws FileNotFoundException {
		int count = 1, count2 = 1;
		dbQuerys query = new dbQuerys();

		// 2 text lists, for personal experience and promotion texts
		List<File> textsList[] = new ArrayList[2]; // FileFunctions.getAllTrainigSetTexts(query);

		textsList[0] = FileFunctions.getTextFilesInFolder("textGames\\train\\P");
		textsList[1] = FileFunctions.getTextFilesInFolder("textGames\\train\\PE");

		List<File> pTextsList = textsList[0];
		List<File> peTextsList = textsList[1];

		/*
		 * System.out.println("1");
		 * 
		 * System.out.println("2"); //List<String> wordsTokens = getAllWordsTokens();
		 * File allText = new File("allText.txt"); String allTextString =
		 * FileFunctions.convertTextFileToString(allText);
		 * 
		 * List<String> wordsTokens = preprocessingText(allTextString);
		 * System.out.println("7");
		 */

		File tokenFile = new File("tokensTest.txt");
		List<String> wordsTokens = FileFunctions.convertTextFileToList(tokenFile);

		Integer[][] pFreqMatrix = buildFrequencyMatrix(pTextsList, wordsTokens);
		System.out.println("8");
		Integer[][] peFreqMatrix = buildFrequencyMatrix(peTextsList, wordsTokens);
		System.out.println("9");

		int maxLengthVector = 0;
		if (pFreqMatrix.length > peFreqMatrix.length)
			maxLengthVector = pFreqMatrix.length;
		else
			maxLengthVector = peFreqMatrix.length;

		double[] pVector = new double[maxLengthVector];
		double[] peVector = new double[maxLengthVector];

		System.out.println("pVector.length: " + pVector.length);
		System.out.println("peVector.length: " + peVector.length);

		for (int i = 0; i < wordsTokens.size(); i++) {
			// get word from 2 matrix (2 vectors)
			for (int j = 0; j < maxLengthVector; j++) {
				if (j > pFreqMatrix.length)
					pVector[j] = 1;
				else
					pVector[j] = pFreqMatrix[j][i];

				if (j > peFreqMatrix.length)
					peVector[j] = 1;
				else {
					try {
						peVector[j] = peFreqMatrix[j][i];
					} catch (Exception e) {
						peVector[j] = 1;
					}
				}
			}

			// System.out.println("");

			int sumFreq = 0;
			int sumFreqP = 0;
			int sumFreqPE = 0;
			for (double value : pVector) {
				sumFreq += value;
				sumFreqP += value;
			}
			for (double value : peVector) {
				sumFreq += value;
				sumFreqPE += value;
			}

			if (sumFreq > 20) {
				// spearman & threshold
				SpearmansCorrelation sp = new SpearmansCorrelation();
				double spResult = sp.correlation(pVector, peVector);

				if (!Double.isNaN(spResult)) {
					System.out.println(count + " " + wordsTokens.get(i) + " - Correlation:" + spResult + "");

					if (wordsTokens.get(i).length() > 2) // word more then 2 letters
					{
						if (Math.abs(spResult) < 0.1 && Math.abs(spResult) > 0.03) {
							query.addWordsToDictionary(wordsTokens.get(i), sumFreq, spResult, sumFreqP, sumFreqPE);
							count2++;
						}
					}

					count++;
				}
				// else
				// System.out.println("Correlation: isNaN");

				// add word to Dictionary if needed

			}
		}

		System.out.println("count: " + count + "    count2: " + count2);
	}

	/***
	 * create frequency feature for text using dictionary words
	 * @param textFile text to create it feature
	 * @return feature string
	 */
	public static String createFrequencyFeature(File textFile) {
		String textString = FileFunctions.convertTextFileToString(textFile);
		List<String> tokens = preprocessingText(textString);
		dbQuerys.createAllDictionaryWordsFile();
		List<String> dictionaryWords = FileFunctions.convertTextFileToList(new File("dictionary\\dictionary.txt"));

		int FrequencyFeature[] = new int[dictionaryWords.size()];

		for (int i = 0; i < dictionaryWords.size(); i++) {
			FrequencyFeature[i] = Collections.frequency(tokens, dictionaryWords.get(i));
			// if (FrequencyFeature[i] == 0)
			// FrequencyFeature[i] = 1;
		}

		return intArrayToString(FrequencyFeature);
	}

	private static String intArrayToString(int arr[]) {
		String arrStr = "";

		for (int i = 0; i < arr.length; i++) {
			arrStr += arr[i] + " ";
		}

		return arrStr;
	}

	// function create dictionary feature for new text
	// how to use for knn

	/* dictionary data for KNN functions */

	/***
	 * create data file for knn
	 * @param txtObject text to check classification of
	 * @param length text length,  long - 1, short - 0
	 */
	public static void createDictionaryData(textObject txtObject, int length) {

		String attr = "@relation dictionary\n\n";

		dbQuerys.createAllDictionaryWordsFile();
		List<String> dictionaryWords = FileFunctions.convertTextFileToList(new File("dictionary\\dictionary.txt"));

		for (int i = 0; i < dictionaryWords.size(); i++) {
			attr += "@attribute \"" + dictionaryWords.get(i) + "\" numeric\n";
		}

		attr += "@attribute classification {P, PE}\n\n@data\n";

		ArrayList<String> FrequencyFeauters = query.getFrequencyFeatures(length);
		FrequencyFeauters.add(0, txtObject.getFrequencyFeature().replace(" ", ",") + "P");

		for (int i = 0; i < FrequencyFeauters.size(); i++) {
			attr += FrequencyFeauters.get(i) + "\n";
		}
		attr.substring(0, attr.length() - 2);
		FileFunctions.createTextFile("dictionary\\DictionaryDataKNN.txt", attr);
	}

	/**
	 * set database connection variable
	 * @param queryNew new connection
	 */
	public static void setDbquery(dbQuerys queryNew) {
		query = queryNew;

	}

	private static void main(String[] arg) {

		query = new dbQuerys();

		createAllWordsTokensFile();
		try {
			// buildDictionary();
			frequencyAvg();
			//frequencyAvgNoAvg();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// frequencyAvg() ;

		// dictionary build
		/*
		 * try { buildDictionary(); } catch (FileNotFoundException e) { 
		 * Auto-generated catch block e.printStackTrace(); }
		 * 
		 * query.createAllDictionaryWordsFile();
		 */
		/*
		 * List<File> list = new ArrayList<File>(); list.add(new File("test.txt"));
		 * 
		 * List<String> words = new ArrayList<String>(); words.add("hello");
		 * words.add("text");
		 * 
		 * Integer[][] matrix = buildFrequencyMatrix(list,words);
		 * 
		 * System.out.println(Arrays.toString(matrix));
		 */
		// createAllWordsTokensFile();
		/*
		
		*/
		// frequencyAllText();

		// File text = new File("t2.txt");
		// List<File> listText = new ArrayList<File>();
		// listText.add(text);

		// buildDictionary();

		// List<String> dictionaryWords = convertDictionaryFileToList();

		// buildFrequencyMatrix(listText, dictionaryWords);

		// words file create
		/*
		 * Scanner keyboard = new Scanner(System.in);
		 * System.out.print("Input StopWord File: "); String[] stopWords =
		 * readStopWords();
		 * 
		 * System.out.print("Input file from which stopword to be removed: ");
		 * 
		 * File allText = new File("allText.txt");
		 * 
		 * // write text in String String textString = ""; for (int i = 1; i <= 401;
		 * i++) { File dataFile = new File("testText\\t"+i+".txt"); Scanner myReader;
		 * try { myReader = new Scanner(dataFile); while (myReader.hasNextLine()) {
		 * textString += myReader.nextLine() + "\n"; } myReader.close(); } catch
		 * (FileNotFoundException e) { e.printStackTrace(); }
		 * 
		 * }
		 * 
		 * PrintWriter outFile; try { outFile = new PrintWriter(allText);
		 * outFile.println(textString); outFile.close(); } catch (FileNotFoundException
		 * e) { e.printStackTrace(); }
		 * 
		 * String[] res = createTokenWords(allText, stopWords);
		 * addWordsToDictionary(res); createDictionaryFile();
		 * 
		 */

	}
}
