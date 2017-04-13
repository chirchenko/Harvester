package domains;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import SQLUtils.DBHelper;
import geometry.Point;
import logginig.Logger;

public class Fields {
	final static String TABLE_NAME = "FIELDS";
	
	private static List<Field> fields;	
	private static Logger logger = Logger.getLogger(Fields.class);
		
	public static class Field{
		public int id;
		public String name;
		public List<Point> fieldPoints;
				
		private Field load(ResultSet rs) throws SQLException{
			this.id = rs.getInt("ID");
			byte[] b = rs.getBytes("NAME");
						 
//			for(String charset : Charset.availableCharsets().keySet()){
//				logger.info(String.format("Charset: %s:\t\t\t%s", charset, new String(b , Charset.availableCharsets().get(charset))));
//			}
			
			try {
				this.name = new String(b, "UTF-8");
				logger.info(this.name);
			} catch (UnsupportedEncodingException e) {				
				e.printStackTrace();
			}
			this.fieldPoints = Points.getPoints(rs.getInt("POINTS"));
			return this;
		}
		
		private void save(){
			if(this.id == 0){
				this.id = DBHelper.getNextSequence(Fields.TABLE_NAME);
			}			
		}			
		
		@Override
		public String toString() {
			return name;
		}
		
	}
	
	public static boolean loadAll(){

		logger.info("Loading Fields...");
		fields = new ArrayList<>();
		ResultSet rs = DBHelper.executeQuery(String.format("SELECT ID, NAME, POINTS FROM %s", TABLE_NAME), null);
		try {
			while(rs.next()){
				fields.add(new Field().load(rs));
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
			return false;	
		}
		logger.info(String.format("\tLoaded %d fields", fields.size()));
		return true;		
	}
	
	public static void saveAll() throws SQLException{
		for(Field f : fields){
			List<Field> res =  fields.stream().filter(t -> t.id == f.id).collect(Collectors.toList());
			
			if(res.isEmpty()){
				f.save();
				DBHelper.executeUpdate(String.format("INSERT INTO %s (ID, NAME, POINTS) VALUES (?, ?, ?)", TABLE_NAME), new Object[]{f.id, f.name, f.fieldPoints });//insert
			} else {
				DBHelper.executeUpdate(String.format("UPDATE %s SET NAME = ?, POINTS = ? WHERE ID = ?", TABLE_NAME), new Object[]{ f.name, f.fieldPoints, f.id });//update
			}
		}
	}

	public static List<Field> getFields() {
		return fields;
	}
	
	
}
