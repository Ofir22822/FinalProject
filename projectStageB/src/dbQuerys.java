import java.sql.*;

public class dbQuerys {

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
	
	public static void addNewText(String textName,String subject,String classification, String sentimentFeature)
	{
		Connection con = connectToDB();
		Statement stmt;
		int classficationID = 0;
		if(classification=="Promotion") {
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
	public static void addSentimentFeature(String textName, String feature)
	{
		Connection con = connectToDB();
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
	
	public static void printCourses(Connection con)
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
