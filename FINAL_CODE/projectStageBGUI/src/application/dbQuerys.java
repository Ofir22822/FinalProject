package application;
import java.sql.*;
import java.util.ArrayList;

/**
 * class for database related functions
 *
 */
public class dbQuerys {

	private static Connection con;
	
	public dbQuerys() {
		this.con = connectToDB();
	}
	
	/*connection open/close functions*/
	
	/**
	 * connect to database, return connection object
	 * @return connection object
	 */
	public static Connection connectToDB()
	{
		Connection conn = null;
		try 
		{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {/* handle the error*/}
        
        try 
        {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/finalprojectdb","root","123456");
            System.out.println("SQL connection succeed");
     	} catch (SQLException ex) 
     	    {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            }
        return conn;
	}
	
	/**
	 * close connect to database
	 * @throws SQLException 
	 */
	public void closeConnectToDB() throws SQLException
	{
		con.close();
	}
	
	/*adding new items to database functions*/
	
	/***
	 * add new text to database, using all textObject
	 * @param textName text file name
	 * @param txtObj object with text features and data
	 */
	public void addNewText(String textName,textObject txtObj)
	{		
		Statement stmt;
		
		try
		{
			stmt = con.createStatement();
			stmt.executeUpdate("Insert into tbtextobject (`textName`,`subject`,`classificationID`,`sentimentFeature`,`sentimentFeatureWords`,`frequencyFeature`) VALUES('"+textName+"','"+txtObj.getSubject()+"','"+txtObj.getClassificationID()+"','"+txtObj.getSentimentFeatureSentence()+"','"+txtObj.getSentimentFeatureWord()+"','"+txtObj.getFrequencyFeature()+"');");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * add new short text to database, using all parameters
	 * @param textName text file name
	 * @param subject subject of text
	 * @param classification promotion or personal experience
	 * @param sentimentFeature words feature of sentiment
	 * @param sentimentFeatureSentence sentence feature of sentiment
	 * @param freqFeature frequency feature 
	 */
	public void addNewText(String textName,String subject,String classification, String sentimentFeature, String sentimentFeatureSentence, String freqFeature)
	{		
		Statement stmt;
		int classficationID = 0;
		if(classification.compareTo("Promotion")==0) {
			classficationID = 1;
		}else
			classficationID = 2;
		
		try
		{
			stmt = con.createStatement();
			stmt.executeUpdate("Insert into tbtextobject (`textName`,`subject`,`classificationID`,`sentimentFeature`,`sentimentFeatureWords`,`frequencyFeature`) VALUES('"+textName+"','"+subject+"','"+classficationID+"','"+sentimentFeatureSentence+"','"+sentimentFeature+"','"+freqFeature+"');");

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * add new long text to database, using all parameters
	 * @param length 1 long text 0 short
	 * @param textName text file name
	 * @param subject subject of text
	 * @param classification promotion or personal experience
	 * @param sentimentFeature words feature of sentiment
	 * @param sentimentFeatureSentence sentence feature of sentiment
	 * @param freqFeature frequency feature 
	 */
	public void addNewTextLong(int length, String textName,String subject,String classification, String sentimentFeature, String sentimentFeatureSentence, String freqFeature)
	{		
		Statement stmt;
		int classficationID = 0;
		if(classification.compareTo("Promotion")==0) {
			classficationID = 1;
		}else
			classficationID = 2;
		
		try
		{
			stmt = con.createStatement();
			stmt.executeUpdate("Insert into tbtextobject (`textName`,`subject`,`classificationID`,`sentimentFeature`,`sentimentFeatureWords`,`frequencyFeature`,`length`) VALUES('"+textName+"','"+subject+"','"+classficationID+"','"+sentimentFeatureSentence+"','"+sentimentFeature+"','"+freqFeature+"',"+length+");");

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
	
	/**
	 * add sentiment feature to existing text in database
	 * @param textName text file name
	 * @param feature sentence sentiment feature
	 */
	public void addSentimentFeature(String textName, String feature)
	{
		Statement stmt;
		try
		{
			stmt = con.createStatement();
			stmt.executeUpdate("UPDATE tbtextobject SET sentimentFeature='"+feature+"' WHERE textName='"+textName+"'");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * add words sentiment feature to existing text in database
	 * @param textName text file name
	 * @param wordsFeature words sentiment feature
	 */
	public void addWordsSentimentFeature(String textName, String wordsFeature)
	{
		Statement stmt;
		try
		{
			stmt = con.createStatement();
			stmt.executeUpdate("UPDATE tbtextobject SET sentimentFeatureWords='"+wordsFeature+"' WHERE textName='"+textName+"'");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/***
	 * add word to database dictionary
	 * @param word value string
	 * @param frequencysum sum of frequencyP + frequencyPE
	 * @param correlation word correlation result
	 * @param frequencyP sum from promotion type text
	 * @param frequencyPE sum from personal experience type text
	 */
	public void addWordsToDictionary(String word, int frequencysum, double correlation, int frequencyP, int frequencyPE)
	{
		String classification = "";
		Statement stmt;
		try
		{
			stmt = con.createStatement();
			stmt.executeUpdate("Insert into tbdicationary (`wordName`,`frequency`, `correlation`, `frequencyP`, `frequencyPE`, `classification`) VALUES('"+word+"',"+frequencysum+","+correlation+","+frequencyP+","+frequencyPE+",'"+classification+"');");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * add sentence sentiment feature to specific text in database
	 * @param textName text name to add feature to
	 * @param feature new feature
	 */
	public void addFrequencyFeature(String textName, String feature)
	{
		Statement stmt;
		try
		{
			stmt = con.createStatement();
			stmt.executeUpdate("UPDATE tbtextobject SET frequencyFeature='"+feature+"' WHERE textName='"+textName+"'");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*get list of items functions*/
	
	/***
	 * get text subject list from database, with no subject duplicate
	 * @return list with subjects as string
	 */
	public ArrayList<String> getSubjectsList() {
		ArrayList<String> subjectList = new ArrayList<>();
		Statement stmt;
		try
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT DISTINCT subject FROM tbtextobject;");
	 		while(rs.next())
	 		{
				 // add subjects to list
	 			subjectList.add(rs.getString(1));
			} 
			rs.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return subjectList;
	}
	
	/**
	 * get all texts sentiment features from database
	 * @param length 1 long text 0 short
	 * @return list with sentiment feature as string
	 */
	public ArrayList<String> getSentimentFeatures (int length) {
		ArrayList<String> SentimentFeautersList = new ArrayList<>();
		Statement stmt;
		try
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT sentimentFeature, classificationID FROM tbtextobject where length="+length+";");
	 		while(rs.next())
	 		{
				 // add subjects to list
	 			if(rs.getInt(2)==1)
	 				SentimentFeautersList.add(rs.getString(1).replace(' ', ',')+"P");
	 			if(rs.getInt(2)==2)
	 				SentimentFeautersList.add(rs.getString(1).replace(' ', ',')+"PE");
			} 
			rs.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return SentimentFeautersList;
	}
	
	/**
	 * get all texts words sentiment features from database
	 * @param length 1 long text 0 short
	 * @return list with sentiment feature as string
	 */
	public ArrayList<String> getSentimentFeaturesWord (int length) {
		ArrayList<String> SentimentFeautersList = new ArrayList<>();
		Statement stmt;
		try
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT sentimentFeatureWords, classificationID FROM tbtextobject where length="+length+";");
	 		while(rs.next())
	 		{
				 // add subjects to list
	 			if(rs.getInt(2)==1)
	 				SentimentFeautersList.add(rs.getString(1).replace(' ', ',')+"P");
	 			if(rs.getInt(2)==2)
	 				SentimentFeautersList.add(rs.getString(1).replace(' ', ',')+"PE");
			} 
			rs.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return SentimentFeautersList;
	}
	
	/**
	 * get all texts frequency features from database
	 * @param length 1 long text 0 short
	 * @return list with sentiment feature as string
	 */
	public ArrayList<String> getFrequencyFeatures (int length) {
		ArrayList<String> FrequencyFeautersList = new ArrayList<>();
		Statement stmt;
		try
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT frequencyFeature, classificationID FROM tbtextobject where length="+length+";");
	 		while(rs.next())
	 		{
				 // add subjects to list
	 			if(rs.getInt(2)==1)
	 				FrequencyFeautersList.add(rs.getString(1).replace(' ', ',')+"P");
	 			if(rs.getInt(2)==2)
	 				FrequencyFeautersList.add(rs.getString(1).replace(' ', ',')+"PE");
			} 
			rs.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return FrequencyFeautersList;
	}

	
	/*other functions*/
	
	/**
	 * create file containing all word from database of dictionary
	 */
	public static void createAllDictionaryWordsFile() {
		ArrayList<String> wordsList = new ArrayList<>();
		Statement stmt;
		
		try
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT wordName FROM tbdicationary;");
	 		while(rs.next())
	 		{
	 			wordsList.add(rs.getString(1));
			} 
			rs.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		FileFunctions.createTextFile("dictionary\\dictionary.txt", wordsList);
	}

	/**
	 * get classification of specific text from database
	 * @param textFileName text to check
	 * @return 1 if promotion 2 if personal experience
	 */
	public int getClassification(String textFileName)
	{
		Statement stmt;
		int result = 0;	
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT classificationID FROM tbtextobject WHERE textName='"+textFileName+"';");
			rs.next();
			result = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
 				
		return result;
	}

}
