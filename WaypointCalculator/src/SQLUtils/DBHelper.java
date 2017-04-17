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

import javax.xml.bind.JAXBException;

import calculator.App;
import logginig.Logger;
import tools.ExportImport;

public class DBHelper{
	private static Connection connection = null;
	private static Logger logger = Logger.getLogger(DBHelper.class);
	
	private final static String SCRIPT_DIR = "res/scripts";
	private final static String SQL_DRIVER = "org.sqlite.JDBC";
	private final static String SQL_DB_JDBC = "jdbc:sqlite:";
	private final static String SQL_DB_NAME = "database.db";

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
			for(int i = 0; parameters != null && i < parameters.length; i++){
				stmt.setObject(i + 1, parameters[i]);
			}
			res = stmt.executeQuery();
			commit();
			
		} catch (SQLException e) {
			if(stmt == null){
				logger.info("Database is not openned");
			}
			logger.info("Error while executing statement: \n" + sqlText);
			e.printStackTrace();	
			rollback();
			
		}
		return res;
	}

	public static int executeUpdate(String sqlText, Object[] parameters) throws SQLException {
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
				logger.info("Database is not openned");
			}
			logger.info("Error while executing statement: \n" + sqlText);
			logger.info(e);
			throw e;
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
				logger.info("Database is not openned");
			}
			logger.info("Error while executing statement: \n" + sqlText);
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
			logger.info("Place SQLite driver to ../lib folder");
			e.printStackTrace();			
		} catch (SQLException e) {
			if(conn == null){
				logger.info("SQL exception occured while creating connection: %s");
			}			
			e.printStackTrace();
		}
		
		return conn;
	}

	public static String getSqlText(String path) throws IOException {
		File file = new File(SCRIPT_DIR + "/" + path);
		if (!file.exists()){
			throw new FileNotFoundException(String.format("Cannot find file %s at path %s", path, SCRIPT_DIR));
		}

		StringBuilder sb = new StringBuilder();
		try(BufferedReader fr = new  BufferedReader(new FileReader(file))) {
			while(fr.ready()){
				sb.append(fr.readLine() + System.lineSeparator());
			}
		} catch (IOException e) {
			throw new IOException(String.format("Cannot read file %s at path %s", path, SCRIPT_DIR));
		}
		return sb.toString();
	}

	public static int getCurrentSequence(String tableName) throws SQLException {
		int res = -1;
		Connection conn = getConnection();
		try {
			Statement stmt= conn.createStatement();
			ResultSet rs = stmt.executeQuery(String.format("SELECT VAL as SEQ FROM DB_INFO WHERE NAME = '%s_SEQUENCE'", tableName));
			res = rs.getInt("SEQ");
		} catch (SQLException e) {
			logger.info("Cannot get sequence for table " + tableName);
			throw e;
		}
		
		return res;
	}
	
	public static int getNextSequence(String tableName) throws SQLException {
		int res = getCurrentSequence(tableName);
		if(res == -1) throw new SQLException("Impossible sequence value -1");
		res++;
		Connection conn = getConnection();
		try {
			Statement stmt= conn.createStatement();
			stmt.executeUpdate(String.format("UPDATE DB_INFO SET VAL = %d WHERE NAME = '%s_SEQUENCE'", res, tableName));
			commit();
		} catch (SQLException e) {
			logger.debug("Cannot get sequence for table " + tableName);
			rollback();
			throw e;
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

	public static void checkDB() throws SQLException, IOException {
		logger.info("Checking database");
		
		logger.info("Attempting to connect: " + SQL_DB_JDBC + SQL_DB_NAME);
		if(new File(SQL_DB_NAME).exists()){
			logger.info("Database exists. Connecting...");
		}else{
			logger.info("Database does not exists. Creating...");
		}
		if(DBHelper.getConnection() == null){
			throw new SQLException("Failed to create connection");
		}
		
		logger.info("Validating schema");
		if(!isDbInitailized()){
			logger.info("Database is not initialized. Running core script...");
			executePlainUpdate(getSqlText("create-schema.sql"));
			
			logger.info("Importing preset");
			String filePath = App.APP_RES_DIR + "/" + App.APP_EXPORT_DIR + "/initial.xml";
			File file = new File(filePath);
			if(file.exists()){
				try {
					ExportImport.importXML(file);
				} catch (JAXBException e) {
					logger.info("Failed to import preset " + file.getAbsolutePath());
					logger.info(e);
				}
			}else{
				logger.info("no preset found at path: " + file.getAbsolutePath());
			}	
		}
	}	
}