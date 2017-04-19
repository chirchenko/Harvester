package domains;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlTransient;

import logginig.Logger;
import sqlutils.DBHelper;

public class Points {
	private static Logger logger = Logger.getLogger(Points.class);
	public final static String TABLE_NAME = "POINTS";
	
	private static List<Point> points = new ArrayList<>();;
	
	public static class Point extends PersistentObject {
		
		@XmlTransient
		public int id;
		@XmlTransient
		public int  fieldId;
		@XmlTransient
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
		
		@Override
		public void save() throws SQLException{
			int idx = points.indexOf(this);
			if(idx == -1){
				this.id = DBHelper.getNextSequence(Points.TABLE_NAME);
				points.add(this);
			}else{
				this.id = points.get(idx).id;
				points.set(idx, this);
			}		
		}
		
		@Override
		public void persist() throws SQLException {
			ResultSet rs = DBHelper.executeQuery("SELECT SUM(1) AS EXIST FROM " + TABLE_NAME + " WHERE ID = ?", new Object[]{this.id});
			rs.next();
			
			if(rs.getBoolean("EXIST")){
				DBHelper.executeUpdate(String.format("UPDATE %s SET FIELD_ID = ?, SEQ = ?, LAT = ?, LON = ? WHERE ID = ?", TABLE_NAME), new Object[]{ this.fieldId, this.seq, this.lat, this.lon, this.id });//update
			} else {
				DBHelper.executeUpdate(String.format("INSERT INTO %s (ID, FIELD_ID, SEQ, LAT, LON) VALUES (?, ?, ?, ?, ?)", TABLE_NAME), new Object[]{this.id, this.fieldId, this.seq, this.lat, this.lon });//insert
			}
		}

		@Override
		public void delete() throws SQLException {
			// TODO Auto-generated method stub
			
		}		

		@Override
		public String validate() throws SQLException {
			if(this.lat < -90 || this.lat > 90 
					|| this.lon < -180 || this.lon > 180) return "Invalid coordinates\n-180 < lat < 180, -90 < lon < 90";
	
			ResultSet rs = DBHelper.executeQuery("SELECT SUM(1) AS EXIST FROM " + TABLE_NAME + " WHERE FIELD_ID = ? AND LAT = ? AND LON = ?", new Object[]{this.fieldId, this.lat, this.lon});
			rs.next();
			if(rs.getBoolean("EXIST")) return "There is already such point for this field";
			
			return "";
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Point other = (Point) obj;
			if (fieldId != other.fieldId)
				return false;
			if (Double.doubleToLongBits(lat) != Double.doubleToLongBits(other.lat))
				return false;
			if (Double.doubleToLongBits(lon) != Double.doubleToLongBits(other.lon))
				return false;
			return true;
		}
		
		@Override
		public String toString() {
			return String.format("[%f; %f]", lat, lon);
		}		
	}
	
	public static void loadAll() throws SQLException{
		logger.info("Loading " + "Points" + "...");
		ResultSet rs = DBHelper.executeQuery("SELECT ID, FIELD_ID, SEQ, LAT, LON FROM POINTS", null);
		
		points.clear();
		while(rs.next()){
			points.add(new Point().load(rs));
		}
		logger.info(String.format("\tLoaded %d points", points.size()));
	}
	
	public static void saveAll() throws SQLException{
		for(Point o : points){
			o.save();
		}
	}
	
	public static List<Point> getPoints(int fieldId){
		return points.stream()
				.filter(t -> t.fieldId == fieldId)
				.collect(Collectors.toList());
	}
}           		
