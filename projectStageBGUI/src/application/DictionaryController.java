package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.math3.analysis.function.Abs;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import opennlp.tools.stemmer.PorterStemmer;

import java.util.regex.Pattern;
import weka.core.Stopwords;

public class DictionaryController {

	/**
	 * variable contains dictionary words
	 */
	private static Set<String> dictionaryWords = new HashSet<>();

	/* preprocessing variables */

	private static String RESULT_FNAME = "Output.txt";
	private static HashMap irregularSingulars = new HashMap(100);
	private static HashMap irregularPlurals = new HashMap(100);
	static {
		irregularSingulars.put("ache", "aches");
		irregularSingulars.put("alumna", "alumnae");
		irregularSingulars.put("alumnus", "alumni");
		irregularSingulars.put("axis", "axes");
		irregularSingulars.put("bison", "bison");
		ArrayList busses = new ArrayList(2);
		busses.add("buses");
		busses.add("busses");
		irregularSingulars.put("bus", busses);
		irregularSingulars.put("calf", "calves");
		irregularSingulars.put("caribou", "caribou");
		irregularSingulars.put("child", "children");
		irregularSingulars.put("datum", "data");
		irregularSingulars.put("deer", "deer");
		ArrayList dice = new ArrayList(2);
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
		ArrayList hoof = new ArrayList(2);
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
		ArrayList octopus = new ArrayList(3);
		octopus.add("octopodes");
		octopus.add("octopi");
		octopus.add("octopuses");
		irregularSingulars.put("octopus", octopus);
		irregularSingulars.put("ox", "oxen");
		irregularSingulars.put("plus", "pluses");
		irregularSingulars.put("quail", "quail");
		irregularSingulars.put("reindeer", "reindeer");
		ArrayList scarf = new ArrayList(2);
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
		ArrayList axes = new ArrayList(2);
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

	/* preprocessing helping functions */

	/**
	 * Tests whether the given (presumed) English noun is singular. A word like
	 * "sheep" that can be either singular or plural yields true.
	 */
	public static boolean isSingular(String word) {
		word = word.toLowerCase();
		if (irregularSingulars.containsKey(word))
			return true;
		// If it is not an irregular singular, it must not be an
		// irregular plural (like "children"), and it must not end
		// in -s unless it ends in -ss (like "boss")).
		if (irregularPlurals.containsKey(word))
			return false;
		if (word.length() <= 0)
			return false;
		if (word.charAt(word.length() - 1) != 's')
			return true;
		if (word.length() >= 2 && word.charAt(word.length() - 2) == 's')
			return true; // word ends in -ss
		return false; // word is not irregular, and ends in -s but not -ss
	}

	/**
	 * Returns the singular of a given (presumed) English word. The given word may
	 * be plural or (already) singular.
	 */
	public static String singularOf(String word) {
		word = word.toLowerCase();
		if (isSingular(word))
			return word;
		Object pluralLookup = irregularPlurals.get(word);
		String singular = null;
		if (pluralLookup != null) {
			if (pluralLookup instanceof ArrayList)
				return (String) (((ArrayList) pluralLookup).get(0));
			else
				return (String) pluralLookup;
		}
		int length = word.length();
		if (length <= 1)
			return word;
		char lastLetter = word.charAt(length - 1);
		if (lastLetter != 's')
			return word; // no final -s
		char secondLast = word.charAt(length - 2);
		if (secondLast == '\'')
			return word.substring(0, length - 2);
		// remove -'s
		if (word.equalsIgnoreCase("gas"))
			return word;
		if (secondLast != 'e' || length <= 3)
			return word.substring(0, length - 1); // remove final -s
		// Word ends in -es and has length >= 4:
		char thirdLast = word.charAt(length - 3);
		if (thirdLast == 'i') // -ies => -y
			return word.substring(0, length - 3) + "y";
		if (thirdLast == 'x') // -xes => -x
			return word.substring(0, length - 2);
		if (length <= 4) // e.g. uses => use
			return word.substring(0, length - 1);
		char fourthLast = word.charAt(length - 4);
		if (thirdLast == 'h' && (fourthLast == 'c' || fourthLast == 's'))
			// -ches or -shes => -ch or -sh
			return word.substring(0, length - 2);
		if (thirdLast == 's' && fourthLast == 's') // -sses => -ss
			return word.substring(0, length - 2);
		return word.substring(0, length - 1); // keep the final e.
	} // end singularOf

	public static void loadStopwords() throws IOException {
		List<String> stopwords = Files.readAllLines(Paths.get("english_stopwords.txt"));
	}

	public static String RemoveStopwordsUsingRemoveAll(String original, List<String> stopwords) {
		// String original = "The quick brown fox jumps over the lazy dog";
		ArrayList<String> allWords = Stream.of(original.toLowerCase().split(" "))
				.collect(Collectors.toCollection(ArrayList<String>::new));
		allWords.removeAll(stopwords);

		String result = allWords.stream().collect(Collectors.joining(" "));
		return result;
	}

	/* function to create dictionary file from text files	*/
	
	private static String[] readStopWords() {
		String[] stopWords = null;
		try {
			File WordsFile = new File("english_stopwords.txt");
			Scanner stopWordsFile = new Scanner(WordsFile);
			int numStopWords = 127;// stopWordsFile.nextInt();

			stopWords = new String[numStopWords];

			for (int i = 0; i < numStopWords; i++)
				stopWords[i] = stopWordsFile.nextLine();

			stopWordsFile.close();
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}

		return stopWords;
	}

	/**
	 * add words to dictionary variable
	 * 
	 * @param words
	 */
	private static void addWordsToDictionary(String[] words) {
		dictionaryWords.addAll(Arrays.asList(words));
	}

	/**
	 * create dictionary file , include all words from DictionarytWords variable
	 */
	private static void createDictionaryFile() {
		
		FileFunctions.createTextFile("dictionarytWords.txt", dictionaryWords);
		
		/*
		try {
			PrintWriter outFile = new PrintWriter(new File("dictionarytWords.txt"));

			for (String word : dictionarytWords) {
				outFile.println(word);
			}

			outFile.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	public static void createDictionaryWordsFile() {
		Scanner keyboard = new Scanner(System.in);
		String[] stopWords = readStopWords();

		List<File> textFilesList = FileFunctions.getTextFilesInFolder("testText");
		FileFunctions.combineAllTextFiles(textFilesList);
		
		File allText = new File("allText.txt");
		
		String[] res = createTokenWords(allText, stopWords);
		addWordsToDictionary(res);
		createDictionaryFile();
		
		/*
		File allText = new File("allText.txt");

		// write text in String
		String textString = "";
		for (int i = 1; i <= 401; i++) {
			File dataFile = new File("testText\\t" + i + ".txt");
			Scanner myReader;
			try {
				myReader = new Scanner(dataFile);
				while (myReader.hasNextLine()) {
					textString += myReader.nextLine() + "\n";
				}
				myReader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}

		PrintWriter outFile;
		try {
			outFile = new PrintWriter(allText);
			outFile.println(textString);
			outFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String[] res = createTokenWords(allText, stopWords);
		addWordsToDictionary(res);
		createDictionaryFile();
		*/
	}
	
	/* dictionary */
	
	/**
	 * create tokens of words from text file
	 * 
	 * @param textFilename
	 * @param stopWords
	 * @return
	 */
	public static String[] createTokenWords(File textFilename, String[] stopWords) {
		String word;

		try {
			Scanner textFile = new Scanner(textFilename);
			textFile.useDelimiter(Pattern.compile("[ \n\r\t,.;:?!'\"]+"));

			PrintWriter outFile = new PrintWriter(new File(RESULT_FNAME));
			String fileText = "";
			while (textFile.hasNextLine()) {
				fileText += textFile.nextLine() + " ";
			}
			String res = fileText.replaceAll("[^a-zA-Z ]", "").toLowerCase();
			res = RemoveStopwordsUsingRemoveAll(res, Arrays.asList(stopWords));
			String[] tokens = res.split(" ");
			PorterStemmer porterStemmer = new PorterStemmer();

			for (int i = 0; i < tokens.length; i++) {
				tokens[i] = singularOf(tokens[i]); 			// plural to singular
				tokens[i] = porterStemmer.stem(tokens[i]);	// root word, stemming

				outFile.print(tokens[i] + " "); // print to ouput file
			}

			// System.out.println("Output File " + RESULT_FNAME);
			textFile.close();
			outFile.close();
			return tokens;
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
		return null;
	}

	/**
	 * build frequency matrix form texts and words
	 * 
	 * @param listText        - list of text to check
	 * @param dictionaryWords - words to check frequency of
	 * @return matrix[i][j] = ( text i, word j ) frequency value, if word not in
	 *         text put 1
	 */
	public static Integer[][] buildFrequencyMatrix(List<File> listText, List<String> dictionaryWords) {
		Integer[][] freqMatrix = new Integer[listText.size()][dictionaryWords.size()];

		for (int i = 0; i < listText.size(); i++) {
			String[] textTokens = createTokenWords(listText.get(i), readStopWords());
			for (int j = 0; j < dictionaryWords.size(); j++) {
				freqMatrix[i][j] = Collections.frequency(Arrays.asList(textTokens), dictionaryWords.get(j));
				if (freqMatrix[i][j] == 0)
					freqMatrix[i][j] = 1;

			}
		}
		return freqMatrix;
	}

	/**
	 * read dictionarytWords.txt and return words in string list
	 * 
	 * @return
	 */
	public static List<String> convertDictionaryFileToList() {
		File dataFile = new File("dictionarytWords.txt");
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

	public static void buildDictionary() {
		int count = 1, count2 = 1;
		dbQuerys query = new dbQuerys();
		// 2 matrix
		List<File> pList = new ArrayList<File>();
		List<File> peList = new ArrayList<File>();

		for (int i = 1; i <= 401; i++) {

			File dataFile = new File("testText\\t" + i + ".txt");
			int res = query.getClassification("test_t" + i + ".txt");

			if (res == 1)
				pList.add(dataFile);
			else if (res == 2)
				peList.add(dataFile);
			else
				System.out.print("error: t" + i + ".txt");
		}

		List<String> words = convertDictionaryFileToList();

		Integer[][] pFreqMatrix = buildFrequencyMatrix(pList, words);
		Integer[][] peFreqMatrix = buildFrequencyMatrix(peList, words);

		int maxLengthVector = 0;
		if (pFreqMatrix.length > peFreqMatrix.length)
			maxLengthVector = pFreqMatrix.length;
		else
			maxLengthVector = peFreqMatrix.length;

		double[] pVector = new double[maxLengthVector];
		double[] peVector = new double[maxLengthVector];

		System.out.println("pVector.length: " + pVector.length);
		System.out.println("peVector.length: " + peVector.length);

		for (int i = 0; i < words.size(); i++) {
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

			// spearman & threshold
			SpearmansCorrelation sp = new SpearmansCorrelation();
			double spResult = sp.correlation(pVector, peVector);

			if (!Double.isNaN(spResult)) {
				System.out.println(count + " " + words.get(i) + " - Correlation:" + spResult + "");
				count++;
			}
			// else
			// System.out.println("Correlation: isNaN");

			if (Math.abs(spResult) < 0.1) {
				// add word to Dictionary if needed
				query.addWordsToDictionary(words.get(i));
				count2++;
			}

		}

		System.out.println("count: " + count + "    count2: " + count2);
	}

	public static String createFrequencyFeature(File textFile)
	{
		String tokens[] = createTokenWords(textFile, readStopWords());
		List<String> dictionaryWords = convertDictionaryFileToList();
		
		int FrequencyFeature[] = new int[dictionaryWords.size()];
		
		for (int i = 0; i < dictionaryWords.size(); i++) {
			FrequencyFeature[i] = Collections.frequency(Arrays.asList(tokens), dictionaryWords.get(i));
		}
		
		return intArrayToString(FrequencyFeature);	
	}
	
	public static String intArrayToString(int arr[]) {
		String arrStr = "";
		
		for(int i =0; i<arr.length;i++)
		{
			arrStr += arr[i]+" ";
		}
		
		return arrStr;		
	}
	
	// function create dictionary feature for new text
	// how to use for knn

	public static void main(String[] arg) {

		// File text = new File("t2.txt");
		// List<File> listText = new ArrayList<File>();
		// listText.add(text);

		buildDictionary();

		// List<String> dictionaryWords = convertDictionaryFileToList();

		// buildFrequencyMatrix(listText, dictionaryWords);

		//words file create
		/*
		Scanner keyboard = new Scanner(System.in);
		System.out.print("Input StopWord File: ");
		String[] stopWords = readStopWords();

		System.out.print("Input file from which stopword to be removed: ");

		File allText = new File("allText.txt");

		// write text in String
		String textString = "";
		for (int i = 1; i <= 401; i++) {
			File dataFile = new File("testText\\t"+i+".txt");
			Scanner myReader;
			try {
				myReader = new Scanner(dataFile);
				while (myReader.hasNextLine()) {
					textString += myReader.nextLine() + "\n";
				}
				myReader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}
		
		PrintWriter outFile;
		try {
			outFile = new PrintWriter(allText);
			outFile.println(textString);
			outFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String[] res = createTokenWords(allText, stopWords);
		addWordsToDictionary(res);
		createDictionaryFile();

		*/

	}
}
