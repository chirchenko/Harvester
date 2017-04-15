package domains;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlTransient;

import SQLUtils.DBHelper;
import domains.Points.Point;
import logginig.Logger;

public class Fields {
	private static Logger logger = Logger.getLogger(Fields.class);
	public final static String TABLE_NAME = "FIELDS";

	private static List<Field> fields;
	private static List<DataChangeListener> listeners = new ArrayList<>();
	
	public static class Field{
		
		@XmlTransient
		public int id;
		public String name;
		public List<Point> points;
		
		private Field load(ResultSet rs) throws SQLException{
			this.id = rs.getInt("ID");			
			this.name = rs.getString("NAME");
			this.points = Points.getPoints(this.id);
			return this;
		}
		
		public void save() throws SQLException{
			int idx = fields.indexOf(this);
			if(idx == -1){
				this.id = DBHelper.getNextSequence(Machinery.TABLE_NAME);
				fields.add(this);
			}else{
				this.id = fields.get(idx).id;
				fields.set(idx, this);
			}		
		}			

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Field other = (Field) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return name;
		}
	}
	
	public static void addDataChangedListener(DataChangeListener listener){
		listeners.add(listener);
	}
	
	public static boolean loadAll(){

		logger.info("Loading Fields...");
		fields = new ArrayList<>();
		ResultSet rs = DBHelper.executeQuery(String.format("SELECT ID, NAME FROM %s", TABLE_NAME), null);
		try {
			while(rs.next()){
				fields.add(new Field().load(rs));
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
			return false;	
		}
		logger.info(String.format("\tLoaded %d fields", fields.size()));
		notifyListeners();
		return true;		
	}
	
	public static void saveAll() throws SQLException{
		for(Field f : fields){
			List<Field> res =  fields.stream().filter(t -> t.id == f.id).collect(Collectors.toList());
			
			if(res.isEmpty()){
				f.save();
				DBHelper.executeUpdate(String.format("INSERT INTO %s (ID, NAME) VALUES (?, ?, ?)", TABLE_NAME), new Object[]{f.id, f.name });//insert
			} else {
				DBHelper.executeUpdate(String.format("UPDATE %s SET NAME = ? WHERE ID = ?", TABLE_NAME), new Object[]{ f.name, f.id });//update
			}
		}
	}
	
	private static void notifyListeners() {
		for(DataChangeListener l : listeners){
			l.dataChanged();
		}
	}

	public static List<Field> getFields() {
		return fields;
	}	
}
