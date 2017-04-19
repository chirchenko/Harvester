package domains;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import logginig.Logger;
import sqlutils.DBHelper;

public class Machinery {
	private static Logger logger = Logger.getLogger(Machinery.class);
	public final static String TABLE_NAME = "MACHINERY";
	
	private static List<Machine> machinery = new ArrayList<>();
	private static List<DataChangeListener> listeners = new ArrayList<>();
	
	public static class Machine extends PersistentObject implements ToolTipRecord{
		@XmlTransient
		public int id;
		public String name = "";
		public double workWidth;
		public double fuel;

		private Machine load(ResultSet rs) throws SQLException{
			this.id = rs.getInt("ID");         
			this.name = rs.getString("NAME");
			this.workWidth = rs.getDouble("WORK_WIDTH");
			this.fuel = rs.getDouble("FUEL");
			return this;
		}
		
		@Override
		public void save() throws SQLException{
			int idx = machinery.indexOf(this);
			if(idx == -1){
				this.id = DBHelper.getNextSequence(Machinery.TABLE_NAME);
				machinery.add(this);
			}else{
				this.id = machinery.get(idx).id;
				machinery.set(idx, this);
			}			
		}
		
		@Override
		public void persist() throws SQLException{
			ResultSet rs = DBHelper.executeQuery("SELECT SUM(1) AS EXIST FROM " + TABLE_NAME + " WHERE ID = ?", new Object[]{this.id});
			rs.next();
			
			if(rs.getBoolean("EXIST")){
				DBHelper.executeUpdate("UPDATE MACHINERY SET NAME = ?, WORK_WIDTH = ?, FUEL = ? WHERE ID = ?", new Object[]{ this.name, this.workWidth, this.fuel, this.id });//update
			} else {
				DBHelper.executeUpdate("INSERT INTO MACHINERY (ID, NAME, WORK_WIDTH, FUEL) VALUES (?, ?, ?, ?)", new Object[]{this.id, this.name, this.workWidth, this.fuel });//insert
			}
		}
		
		@Override
		public void delete() throws SQLException {
			
		}		
		
		@Override
		public String validate() throws SQLException {
			ResultSet rs = DBHelper.executeQuery("SELECT SUM(1) AS EXIST FROM " + TABLE_NAME + " WHERE NAME = ?", new Object[]{this.name});
			rs.next();
			if(rs.getBoolean("EXIST")) return "Entity with this name already exeists";
			
			if("".equals(this.name)) return "Name cannot be empty";
			
			if(this.fuel == 0.0) return "Fuel consumption cannot be 0.0";

			if(this.workWidth == 0.0) return "Work width cannot be 0.0";
			return "";
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Machine other = (Machine) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}
		
		@Override
		public String getTooltip() {
			return String.format("<html>"
					+ "Name:\t%s<br>"
					+ "Workwidth:\t%.2f<br>"
					+ "Fuel:\t%.4f"
					+ "</html>" 
					, name, workWidth, fuel);
		}

		@Override
		public String toString() {
			return name + ", width=" + workWidth;
		}
	}
	
	public static void addDataChangedListener(DataChangeListener listener){
		listeners.add(listener);
	}
	
	public static void loadAll() throws SQLException{
		logger.info("Loading Machinery...");
		ResultSet rs = DBHelper.executeQuery(String.format("SELECT ID, NAME, WORK_WIDTH, FUEL FROM %s", TABLE_NAME), null);
		
		machinery.clear();
		while(rs.next()){
			machinery.add(new Machine().load(rs));
		}
		
		logger.info(String.format("\tLoaded %d machines", machinery.size()));
		notifyListeners();	
	}
	
	public static void saveAll() throws SQLException{
		for(Machine o : machinery){
			o.save();
		}
	}
	
	private static void notifyListeners() {
		for(DataChangeListener l : listeners){
			l.dataChanged();
		}
	}

	public static List<Machine> getMachinery() {
		return machinery;
	}
	
}
