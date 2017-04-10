package domains;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import SQLUtils.DBHelper;
import domains.Points.Point;
import logginig.Logger;

public class Fields {
	final static String TABLE_NAME = "FIELDS";
	
	private static List<Field> fields;
	
	private static Logger logger = Logger.getLogger(Fields.class);
	
	static{
		
		//sequence = DBHelper.getCurrentSequence(TABLE_NAME);
		
//		List<Point> pointList = new ArrayList<>();
//		pointList.add(Point.parsePoint("49.856666, 30.122131"));
//		pointList.add(Point.parsePoint("49.855485, 30.121552"));
//		pointList.add(Point.parsePoint("49.856100, 30.117736"));
//		pointList.add(Point.parsePoint("49.856143, 30.116169"));
//		pointList.add(Point.parsePoint("49.856625, 30.116229"));
//		pointList.add(Point.parsePoint("49.856646, 30.115896"));
//		pointList.add(Point.parsePoint("49.857538, 30.115832"));		
//		fields.add(new Field(1, "Терезино №603", pointList));
//		
//		pointList = new ArrayList<>();
//		pointList.add(Point.parsePoint("49.851887, 30.120772"));
//		pointList.add(Point.parsePoint("49.848890, 30.137644"));
//		pointList.add(Point.parsePoint("49.854177, 30.139869"));
//		pointList.add(Point.parsePoint("49.856543, 30.122645"));		
//		fields.add(new Field(2, "Терезино №598", pointList));
//		
//		pointList = new ArrayList<>();
//		pointList.add(Point.parsePoint("50.082687, 30.039135"));
//		pointList.add(Point.parsePoint("50.080071, 30.040690"));
//		pointList.add(Point.parsePoint("50.076592, 30.027692"));
//		pointList.add(Point.parsePoint("50.078366, 30.024730"));
//		pointList.add(Point.parsePoint("50.080584, 30.032966"));
//		pointList.add(Point.parsePoint("50.081290, 30.033961"));		
//		fields.add(new Field(3, "Велика Снітинка №153", pointList));
			
	}	
	
	public static class Field{
		private int id;
		private String name;
		private int fieldPoints;
		
		public List<Point> getFieldPoints() {
			return Points.getPoints(this.id);
		}
		
		@Override
		public String toString() {
			return name;
		}
		
		private Field load(ResultSet rs) throws SQLException{
			this.id = rs.getInt("ID");
			this.name = rs.getString("NAME");
			this.id = rs.getInt("POINTS");
			return this;
		}
	}
	
	public static void loadAll(){
		fields = new ArrayList<>();
		ResultSet rs = DBHelper.executeQuery(String.format("SELECT ID, NAME, POINTS FROM %s", TABLE_NAME), null);
		try {
			while(rs.next()){
				fields.add(new Field().load(rs));
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
	}
	
	public static void saveAll() throws SQLException{
		for(Field f : fields){
			List<Field> res =  fields.stream().filter(t -> t.id == f.id).collect(Collectors.toList());
			
			if(res.isEmpty()){
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
