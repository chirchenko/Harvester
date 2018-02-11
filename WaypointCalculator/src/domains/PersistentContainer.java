package domains;

import java.util.ArrayList;
import java.util.List;

public abstract class PersistentContainer<T extends PersistentObject> {
    static PersistentContainer<? extends PersistentObject> instance;

    protected List<PersistentObject> entityList = new ArrayList<>();
    private static final List<DataChangeListener> listeners = new ArrayList<>();

//    public void saveAll() throws SQLException {
//	    entityList.stream().forEach(p -> {
//		    try{
//			    p.save();
//		    } catch (SQLException e){
//			    throw new RuntimeException(e);
//		    }
//	    });
//}


    public static void addDataChangedListener(DataChangeListener paramDataChangeListener) {
        listeners.add(paramDataChangeListener);
    }

    static void notifyListeners() {
    	listeners.forEach(DataChangeListener::dataChanged);
    }
}
