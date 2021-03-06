package logginig;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractLogger {

private static int logLevel = 4;
	
	final Class<?> clazz;
	static final Set<LogListener> listeners = new HashSet<>();
	
	public enum LogLevel{
		INFO(1), TRACE(2), DEBUG(4);
		private final int mask;

	    LogLevel(int mask)
	    {
	        this.mask = mask;
	    }

	    public int getMask()
	    {
	        return mask;
	    }
	}

	AbstractLogger(Class<?> clazz){
		this.clazz = clazz;
	}
	
	public static void subscribe(LogListener listener){
		listeners.add(listener);
	}
	
	public static void unsubscribe(LogListener listener){
		listeners.remove(listener);
	}
	
	public abstract void info(String message);
	
	public abstract void debug(String message);
	
	public abstract void trace(String message);
	
	public static void setLogLevel(int mask) {
		logLevel = mask;
	}
	
	public static int getLogLevel() {
		return logLevel;
	}
}
