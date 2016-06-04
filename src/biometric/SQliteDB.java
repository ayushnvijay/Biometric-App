package biometric;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;
 
public class SQliteDB
{
	Connection conn;
	Statement stmt;
	public void close(){
		try{
			conn.close();
			stmt.close();
			System.out.println("Database Close");
		}
		catch(Exception e){
			
		}
	}
	public int[] getAvg(String username){
		int[] arr = new int[3];
		String sqlSlectQuery = "SELECT * FROM "+username+"avg";
		try{
			ResultSet rs = stmt.executeQuery(sqlSlectQuery);
			if(rs.next()){
				//before after down
				arr[0] = rs.getInt(1);
				arr[1] = rs.getInt(2);
				arr[2] = rs.getInt(3);
				return arr;
			}
			return null;
		}
		catch(Exception e){
			return null;
		}
	}
	public int[] getData(String userName, String firstKey, String secondKey ){
		int[] arr = new int[3];
		String sqlSlectQuery = "SELECT * FROM "+userName + " WHERE firstLetter="+firstKey+" AND secondLetter="+secondKey;
		try {
			ResultSet rs = stmt.executeQuery(sqlSlectQuery);
			if(rs.next()){
				//before after down
				arr[0] = rs.getInt(4);
				arr[1] = rs.getInt(3);
				arr[2] = rs.getInt(5);
				System.out.println("Got Data from getData");
				return arr;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
	}
	public boolean insertIntoTable(String userName, KeyData data){
		try{
			String firstLetter = data.keyPressedCode+", ";
			String secondLetter = data.nextkeyPressedCode+", ";
			String timeBefore = data.timeBeforeKeyPressed+", ";
			String timeAfter = data.timeAfterKeyPressed+", ";
			String timeDown = data.timeKeyDown+"";
			String sqlCheckIfRecordExists = "SELECT * FROM "+userName+" WHERE firstLetter="+data.keyPressedCode+" AND secondLetter="+data.nextkeyPressedCode;
			ResultSet rs = stmt.executeQuery(sqlCheckIfRecordExists);
			//System.out.println("CHECK QUERY YO");
			if(rs.next()){
				System.out.println("Inserting Data");
				//System.out.println("Results exist");
				//System.out.println(rs.getInt(3));
				int after = (rs.getInt(3) + data.timeAfterKeyPressed)/2;
				int before = (rs.getInt(4) + data.timeBeforeKeyPressed)/2;
				int down = (rs.getInt(5) + data.timeKeyDown)/2;
				String sqlUpdateCommand = "UPDATE "+userName+" SET timeAfter = "+after+", timeBefore = "+before+", keyDownTime = "+down+ " WHERE firstLetter="+data.keyPressedCode+" AND secondLetter="+data.nextkeyPressedCode ;
				stmt.execute(sqlUpdateCommand);
				//System.out.println("UPDATE QUERY YO");
			}
			else{
				String sqlInsertTableQuery = "INSERT INTO "+ userName + " VALUES("+firstLetter+secondLetter+timeAfter+timeBefore+timeDown+")";
				stmt.execute(sqlInsertTableQuery);
				
			}
			return true;
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			return false;
		}
	}
	public boolean insertIntoAvgTable(String userName, int[] arr){
		try{
			
			String sqlCheckIfRecordExists = "SELECT * FROM "+userName+"avg";
			ResultSet rs = stmt.executeQuery(sqlCheckIfRecordExists);
			//System.out.println("CHECK QUERY YO");
			if(rs.next()){
				//System.out.println("Results exist");
				//System.out.println(rs.getInt(3));
				int before = (rs.getInt(1) + arr[0])/2;
				int after = (rs.getInt(2) + arr[1])/2;
				int down = (rs.getInt(3) + arr[2])/2;
				String sqlUpdateCommand = "UPDATE "+userName+"avg SET timeBeforeAvg = "+before+", timeAfterAvg = "+after+", timeDownAvg = "+down ;
				stmt.execute(sqlUpdateCommand);
				//System.out.println("UPDATE QUERY YO");
			}
			else{
				String sqlInsertTableQuery = "INSERT INTO "+ userName+"avg" + " VALUES("+arr[0]+", "+arr[1]+", "+arr[2]+")";
				stmt.execute(sqlInsertTableQuery);
				
			}
			return true;
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			return false;
		}
	}
	public boolean createUserTable(String userName){
		try{
			String sqlCreateTableQuery = "CREATE TABLE "+userName+" (firstLetter INTEGER, secondLetter INTEGER"
					+ " ,timeAfter INTEGER, timeBefore INTEGER, keyDownTime INTEGER)";
			stmt.execute(sqlCreateTableQuery);
			String sqlCreateTableQuery2 = "CREATE TABLE "+userName+"avg (timeBeforeAvg INTEGER, timeAfterAvg INTEGER"
					+ " ,timeDownAvg INTEGER)";
			stmt.execute(sqlCreateTableQuery2);
			System.out.println("Created table in given database...");
			return true;
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			return false;
		}
	}
	public SQliteDB() {
		// TODO Auto-generated constructor stub
		
	}
	public boolean open(){
		try{
        	String sDriverName = "org.sqlite.JDBC";
        	Class.forName(sDriverName);
    	}
    	catch(Exception e){
    		return false;
    	}
        // now we set up a set of fairly basic string variables to use in the body of the code proper
        String sTempDb = "KeyStrokes.db";
        String sJdbc = "jdbc:sqlite";
        String sDbUrl = sJdbc + ":" + sTempDb;
        // which will produce a legitimate Url for SqlLite JDBC :
        // jdbc:sqlite:hello.db
        
 
        // create a database connection
        try{
        	conn = DriverManager.getConnection(sDbUrl);
        	stmt = conn.createStatement();
        	System.out.println("Database Open");
        	return true;
        }
        catch(Exception e){
        	System.out.println(e.getMessage());
        	return false;
        }
	}
	
	
	public static void main (String[] args) throws Exception
    {
        // register the driver 
    	
        /*try {
            Statement stmt = conn.createStatement();
            try {
                stmt.setQueryTimeout(iTimeout);
                stmt.executeUpdate( sMakeTable );
                stmt.executeUpdate( sMakeInsert );
                ResultSet rs = stmt.executeQuery(sMakeSelect);
                try {
                    while(rs.next())
                        {
                            String sResult = rs.getString("response");
                            System.out.println(sResult);
                        }
                } finally {
                    try { rs.close(); } catch (Exception ignore) {}
                }
            } finally {
                try { stmt.close(); } catch (Exception ignore) {}
            }
        } finally {
            try { conn.close(); } catch (Exception ignore) {}
        }*/
    }
 
}