package domains;

import java.sql.SQLException;

public abstract class PersistentObject implements Cloneable {
	public int id;

	public abstract int getId();

	public abstract void save() throws SQLException;

	public abstract void persist() throws SQLException;

	public abstract void delete() throws SQLException;

	public abstract void dispose();

	public abstract String validate() throws SQLException;
	
	public abstract void loadAll() throws SQLException;
	
//	public abstract PersistentContainer<?> getInstance();
	
//	public abstract List<PersistentObject> getEntities();

	@Override
	public PersistentObject clone() {
		
		try {			
			return (PersistentObject) super.clone();			
		} catch (CloneNotSupportedException ex) {			
			throw new InternalError();			
		}
	}
}
