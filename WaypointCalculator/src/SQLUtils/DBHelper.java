package SQLUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import logginig.Logger;

public class DBHelper{
	private static Connection connection = null;
	private static Logger logger = Logger.getLogger(DBHelper.class);

	private final static String SQL_DRIVER = "org.sqlite.JDBC";
	private final static String SQL_DB_JDBC = "jdbc:sqlite:";
	private final static String SQL_DB_NAME = "database.db";

	public static void main( String args[] )
	{
		try {
			if(!isDbInitailized()){
				System.out.println("Running DB initialization: " + executePlainUpdate(getSqlText("create-schema.sql")));
			}
			List<Object> parameters = new ArrayList<>();
	
			parameters.add("���� �456");
			parameters.add("15");
			executeUpdate("INSERT INTO FIELDS(NAME, AGE) VALUES(?, ?)", parameters.toArray());
			
		}catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return;
		}
	}

	private static boolean isDbInitailized() {
		Connection c = DBHelper.getConnection();		
		try {
			c.createStatement().executeUpdate("SELECT COUNT(1) FROM DB_INFO");
		} catch (SQLException e) {
			return false;
		}		
		return true;
	}
	
	public static ResultSet executeQuery(String sqlText, Object[] parameters){
		PreparedStatement stmt = null;
		Connection c = DBHelper.getConnection();
		ResultSet res = null;
		if(c == null) return null;
		try {
			stmt = c.prepareStatement(sqlText);
			for(int i = 0; i < parameters.length; i++){
				stmt.setObject(i + 1, parameters[i]);
			}
			res = stmt.executeQuery();
			commit();
			
		} catch (SQLException e) {
			if(stmt == null){
				System.err.println("Database is not openned");
			}
			System.err.println("Error while executing statement: \n" + sqlText);
			e.printStackTrace();	
			rollback();
			
		}
		return res;
	}

	public static int executeUpdate(String sqlText, Object[] parameters) {
		PreparedStatement stmt = null;
		Connection c = DBHelper.getConnection();
		int res = 0;
		if(c == null) return 0;
		try {
			stmt = c.prepareStatement(sqlText);
			if(parameters != null){
				for(int i = 0; i < parameters.length; i++){
					stmt.setObject(i + 1, parameters[i]);
				}
			}
			res = stmt.executeUpdate();
			commit();
		} catch (SQLException e) {
			if(stmt == null){
				System.err.println("Database is not openned");
			}
			System.err.println("Error while executing statement: \n" + sqlText);
			e.printStackTrace();	
			rollback();			
			
		}
		return res;
	}
	
	public static int executePlainUpdate(String sqlText) {
		Statement stmt = null;
		Connection c = DBHelper.getConnection();
		int res = 0;
		if(c == null) return 0;
		try {
			stmt = c.createStatement();
			
			res = stmt.executeUpdate(sqlText);
			commit();
		} catch (SQLException e) {
			if(stmt == null){
				System.err.println("Database is not openned");
			}
			System.err.println("Error while executing statement: \n" + sqlText);
			e.printStackTrace();	
			rollback();
			
		}
		
		return res;
	}

	private static Connection getConnection() {
		if(connection == null){
			connection = createConnection();
		}
		return connection;
	}

	private static Connection createConnection() {
		Connection conn = null;
		try {
			Class.forName(SQL_DRIVER);
			
			conn = DriverManager.getConnection(SQL_DB_JDBC + SQL_DB_NAME);		
			conn.setAutoCommit(false);
		} catch (ClassNotFoundException e) {
			System.err.println("Place SQLite driver to ../lib folder");
			e.printStackTrace();			
		} catch (SQLException e) {
			if(conn == null){
				System.err.println("SQL exception occured while creating connection: %s");
			}			
			e.printStackTrace();
		}
		
		return conn;
	}

	public static String getSqlText(String path) throws IOException {
		String directoryPath= "sql-scripts/";
		File file = new File(directoryPath + path);
		if (!file.exists()){
			throw new FileNotFoundException(String.format("Cannot find file %s at path %s", path, directoryPath));
		}

		StringBuilder sb = new StringBuilder();
		try(BufferedReader fr = new  BufferedReader(new FileReader(file))) {
			while(fr.ready()){
				sb.append(fr.readLine() + System.lineSeparator());
			}
		} catch (IOException e) {
			throw new IOException(String.format("Cannot read file %s at path %s", path, directoryPath));
		}
		return sb.toString();
	}

	public static int getCurrentSequence(String tableName) {
		int res = -1;
		Connection conn = getConnection();
		try {
			Statement stmt= conn.createStatement();
			ResultSet rs = stmt.executeQuery(String.format("SELECT VAL as SEQ FROM DB_INFO WHERE NAME = '%s_SEQUENCE'", tableName));
			res = rs.getInt("SEQ");			
		} catch (SQLException e) {
			logger.debug("Cannot get sequence for table " + tableName);
		}
		
		return res;
	}
	
	public static int getNextSequence(String tableName) {
		int res = getCurrentSequence(tableName);
		if(res == -1) return -1;
		res++;
		Connection conn = getConnection();
		try {
			Statement stmt= conn.createStatement();
			stmt.executeUpdate(String.format("UPDATE DB_INFO SET VAL = %d WHERE NAME = '%s_SEQUENCE'", res, tableName));
			commit();
		} catch (SQLException e) {
			logger.debug("Cannot get sequence for table " + tableName);
		}
		
		return res;
	}
	
	private static void rollback(){
		try {
			connection.rollback();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	private static void commit(){
		try {
			connection.commit();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
}