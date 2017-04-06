package logginig;

import java.util.HashSet;
import java.util.Set;

public class Logger {
	private static int logLevel = 4;
	
	private Class<?> clazz;
	private static Set<LogListener> listeners = new HashSet<>();
	
	public static enum LogLevel{
		INFO(1), TRACE(2), DEBUG(4);
		private final int mask;

	    private LogLevel(int mask)
	    {
	        this.mask = mask;
	    }

	    public int getMask()
	    {
	        return mask;
	    }
	}

	private Logger(Class<?> clazz){
		this.clazz = clazz;
	}
	
	public static Logger getLogger(Class<?> clazz){
		return new Logger(clazz);
	}
	
	public static void subscribe(LogListener listener){
		listeners.add(listener);
	}
	
	public static void unsubscribe(LogListener listener){
		listeners.remove(listener);
	}
	
	public void info(String message){
		for(LogListener ll : listeners){
			ll.update(LogLevel.INFO, clazz, message);
		}
	}
	
	public void debug(String message){
		for(LogListener ll : listeners){
			ll.update(LogLevel.DEBUG, clazz, message);
		}
	}
	
	public void trace(String message){
		for(LogListener ll : listeners){
			ll.update(LogLevel.TRACE, clazz, message);
		}
	}
	
	public static void setLogLevel(int mask) {
		logLevel = mask;
	}
	
	public static int getLogLevel() {
		return logLevel;
	}
	
}
