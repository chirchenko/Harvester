package domains;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import SQLUtils.DBHelper;
import geometry.Point;

public class Fields {
	final static String TABLE_NAME = "FIELDS";
	
	private static List<Field> fields = new ArrayList<>();
	
	static{
		
		//sequence = DBHelper.getCurrentSequence(TABLE_NAME);
		
		List<Point> pointList = new ArrayList<>();
		pointList.add(Point.parsePoint("49.856666, 30.122131"));
		pointList.add(Point.parsePoint("49.855485, 30.121552"));
		pointList.add(Point.parsePoint("49.856100, 30.117736"));
		pointList.add(Point.parsePoint("49.856143, 30.116169"));
		pointList.add(Point.parsePoint("49.856625, 30.116229"));
		pointList.add(Point.parsePoint("49.856646, 30.115896"));
		pointList.add(Point.parsePoint("49.857538, 30.115832"));		
		fields.add(new Field(1, "Терезино №603", pointList));
		
		pointList = new ArrayList<>();
		pointList.add(Point.parsePoint("49.851887, 30.120772"));
		pointList.add(Point.parsePoint("49.848890, 30.137644"));
		pointList.add(Point.parsePoint("49.854177, 30.139869"));
		pointList.add(Point.parsePoint("49.856543, 30.122645"));		
		fields.add(new Field(2, "Терезино №598", pointList));
		
		pointList = new ArrayList<>();
		pointList.add(Point.parsePoint("50.082687, 30.039135"));
		pointList.add(Point.parsePoint("50.080071, 30.040690"));
		pointList.add(Point.parsePoint("50.076592, 30.027692"));
		pointList.add(Point.parsePoint("50.078366, 30.024730"));
		pointList.add(Point.parsePoint("50.080584, 30.032966"));
		pointList.add(Point.parsePoint("50.081290, 30.033961"));		
		fields.add(new Field(3, "Велика Снітинка №153", pointList));
			
	}
	
	public static class Field{
		private int id;
		private String name;
		private List<Point> fieldPoints;
		
		public Field(int id, String name, List<Point> fieldPoints) {
			this.id = id;
			this.name = name;
			this.fieldPoints = fieldPoints;
		}		
		public int getId() {
			return id;
		}
		public String getName() {
			return name;
		}
		public List<Point> getFieldPoints() {
			return fieldPoints;
		}
		@Override
		public String toString() {
			return name;
		}
		
		public void load(ResultSet rs){
			this.id = rs.getInt("ID");
			this.id = rs.getInt("ID");
			this.id = rs.getInt("ID");
			
		}
		
	}

	public static List<Field> getFields() {
		return fields;
	}
	
	public static load(ResultSet rs){
		
		
	}
}
