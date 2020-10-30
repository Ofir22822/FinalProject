package application;
import java.sql.*;
import java.util.ArrayList;

public class dbQuerys {

	private Connection con;
	
	public dbQuerys() {
		this.con = connectToDB();
	}
	
	public static void main(String[] args) 
	{
		try 
		{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {/* handle the error*/}
        
        try 
        {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/finalprojectdb","root","123456");
            //Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.3.68/test","root","Root");
            System.out.println("SQL connection succeed");
            //createTableCourses(conn);
            printCourses(conn);
     	} catch (SQLException ex) 
     	    {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            }
   	}
	
	/**
	 * connect to db, return connection object
	 * @return
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
            //Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.3.68/test","root","Root");
            System.out.println("SQL connection succeed");
            //createTableCourses(conn);
            //printCourses(conn);
     	} catch (SQLException ex) 
     	    {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            }
        return conn;
	}
	
	/**
	 * close connect to db
	 * @return
	 * @throws SQLException 
	 */
	public void closeConnectToDB() throws SQLException
	{
		con.close();
	}
	
	
	/**
	 * add new text to db
	 * @param textName
	 * @param subject
	 * @param classification
	 * @param sentimentFeature
	 */
	public void addNewText(String textName,String subject,String classification, String sentimentFeature)
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
			stmt.executeUpdate("Insert into tbtextobject (`textName`,`subject`,`classificationID`,`sentimentFeature`) VALUES('"+textName+"','"+subject+"','"+classficationID+"','"+sentimentFeature+"')");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * add sentiment feature to existing text in db
	 * @param textName
	 * @param feature
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
	 * add words sentiment feature to existing text in db
	 * @param textName
	 * @param wordsFeature
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
	
	public void addWordsToDictionary(String word)
	{
		Statement stmt;
		try
		{
			stmt = con.createStatement();
			stmt.executeUpdate("Insert into tbdicationary (`wordName`) VALUES('"+word+"');");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/***
	 * get text subject list from db, with no subject duplicate
	 * @return ArrayList<String> with subjects as string
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
	 * get all texts sentiment features from db
	 * @return ArrayList<String> with sentiment feature as string
	 */
	public ArrayList<String> getSentimentFeatures () {
		ArrayList<String> SentimentFeautersList = new ArrayList<>();
		Statement stmt;
		try
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT sentimentFeature, classificationID FROM tbtextobject;");
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
	 * get all texts words sentiment features from db
	 * @return ArrayList<String> with sentiment feature as string
	 */
	public ArrayList<String> getSentimentFeaturesWord () {
		ArrayList<String> SentimentFeautersList = new ArrayList<>();
		Statement stmt;
		try
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT sentimentFeatureWords, classificationID FROM tbtextobject;");
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
	 * get classification of specific text from db
	 * @param textFileName text to check
	 * @return
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
	
	
	private static void printCourses(Connection con)
	{
		Statement stmt;
		try 
		{
			stmt = con.createStatement();
			
			String name = "p";
			int classID = 1;
			for(int i =0;i<10;i++)
			{
				if(i >= 5)
				{
					name = "pe";
					classID = 2;
				}
				//System.out.print("INSERT INTO tbtextobject (textName, subject, classificationID) VALUES('"+name+((i%5)+1)+"', \"travel\", "+classID+");");
			
				//stmt.executeUpdate("INSERT INTO tbtextobject (textName, subject, classificationID) VALUES('"+name+((i%5)+1)+"', \"travel\", "+classID+");");
			}
			
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM tbtextobject;");
			
	 		while(rs.next())
	 		{
				 // Print out the values
	 			textObject obj = new textObject(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4),rs.getString(5),rs.getString(6));
				 System.out.println(obj);
			} 
			rs.close();
			//stmt.executeUpdate("UPDATE course SET semestr=\"W08\" WHERE num=61309");
		} catch (SQLException e) {e.printStackTrace();}
	}
	
}
