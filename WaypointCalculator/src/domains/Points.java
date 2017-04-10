package domains;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import SQLUtils.DBHelper;
import logginig.Logger;

public class Points {

	final static String TABLE_NAME = "POINTS";
	private static List<Point> points;
	
	private static Logger logger = Logger.getLogger(Points.class);
	
	public static class Point{
		public int id;
		public int  fieldId;
		public int seq;
		public double lat;
		public double lon;		
		
		private Point load(ResultSet rs) throws SQLException{
			this.id = rs.getInt("ID");         
			this.fieldId = rs.getInt("FIELD_ID");
			this.seq = rs.getInt("SEQ");
			this.lat = rs.getDouble("LAT");
			this.lon = rs.getDouble("LON");
			return this;
		}
		
		private void save(){
			if(this.id == 0){
				this.id = DBHelper.getNextSequence(Points.TABLE_NAME);
			}			
		}

		@Override
		public String toString() {
			return String.format("#%d [%f; %f]", seq, lat, lon);
		}
		
		
	}
	
	public static void loadAll(){
		points = new ArrayList<>();
		ResultSet rs = DBHelper.executeQuery(String.format("SELECT ID, FIELD_ID, SEQ, LAT, LON FROM %s", TABLE_NAME), null);
		try {
			while(rs.next()){
				points.add(new Point().load(rs));
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
	}
	
	public static void saveAll() throws SQLException{
		for(Point o: points){
			List<Point> res =  points.stream().filter(t -> t.id == o.id).collect(Collectors.toList());
			
			if(res.isEmpty()){
				o.save();
				DBHelper.executeUpdate(String.format("INSERT INTO %s (ID, FIELD_ID, SEQ, LAT, LON) VALUES (?, ?, ?, ?, ?)", TABLE_NAME), new Object[]{o.id, o.fieldId, o.seq, o.lat, o.lon });//insert
			} else {
				DBHelper.executeUpdate(String.format("UPDATE %s SET FIELD_ID = ?, SEQ = ?, LAT = ?, LON = ? WHERE ID = ?", TABLE_NAME), new Object[]{ o.fieldId, o.seq, o.lat, o.lon, o.id });//update
			}
		}
	}
	
	public static List<geometry.Point> getPoints(int fieldId) {
		return points.stream()
				.filter(t -> t.fieldId == fieldId)
				.map(y -> new geometry.Point(y.lat, y.lon))
				.collect(Collectors.toList());		
	}
}           		
