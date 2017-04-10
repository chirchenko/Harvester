package domains;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import SQLUtils.DBHelper;
import logginig.Logger;

public class Machinery {
	
	final static String TABLE_NAME = "MACHINERY";
	private static List<Machine> machinery = new ArrayList<>();
	
	private static Logger logger = Logger.getLogger(Machinery.class);
	
	static{
//		machinery.add(new Machine("John Deere T660", 5));
//		machinery.add(new Machine("Claas Lexion 480", 5.3));
//		machinery.add(new Machine("Єнісей 1200", 4));
//		machinery.add(new Machine("Славутич КЗС 9", 3));
//		machinery.add(new Machine("Єнісей 950-1", 3.5));
			
	}
	
	public static class Machine{
		private int id;
		private String name;
		private double workWidth;
		private double fuel;

		private Machine load(ResultSet rs) throws SQLException{
			this.id = rs.getInt("ID");         
			this.name = rs.getString("NAME");
			this.workWidth = rs.getDouble("WORK_WIDTH");
			this.fuel = rs.getDouble("FUEL");
			return this;
		}
		
		@Override
		public String toString() {
			return name + ", width=" + workWidth;
		}		
	}
	
	public static void loadAll(){
		machinery = new ArrayList<>();
		ResultSet rs = DBHelper.executeQuery(String.format("SELECT ID, NAME, WORK_WIDTH, FUEL FROM %s", TABLE_NAME), null);
		try {
			while(rs.next()){
				machinery.add(new Machine().load(rs));
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
	}
	
	public static void saveAll() throws SQLException{
		for(Machine o : machinery){
			List<Machine> res =  machinery.stream().filter(t -> t.id == o.id).collect(Collectors.toList());
			
			if(res.isEmpty()){
				DBHelper.executeUpdate(String.format("INSERT INTO %s (ID, NAME, WORK_WIDTH, FUEL) VALUES (?, ?, ?, ?, ?)", TABLE_NAME), new Object[]{o.id, o.name, o.workWidth, o.fuel });//insert
			} else {
				DBHelper.executeUpdate(String.format("UPDATE %s SET NAME = ?, WORK_WIDTH = ?, FUEL = ? WHERE ID = ?", TABLE_NAME), new Object[]{ o.name, o.workWidth, o.fuel, o.id });//update
			}
		}
	}
	public static List<Machine> getMachinery() {
		return machinery;
	}
	
}
