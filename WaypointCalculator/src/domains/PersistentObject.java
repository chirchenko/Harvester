package domains;

import java.sql.SQLException;

public abstract class PersistentObject {
	public int id;
	
	public abstract void save() throws SQLException;
	public abstract void persist() throws SQLException;
	public abstract void delete() throws SQLException;
	public abstract String validate() throws SQLException;
}
