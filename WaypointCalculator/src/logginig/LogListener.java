package logginig;

import logginig.Logger.LogLevel;

public interface LogListener {
	public void update(LogLevel level, Class<?> clazz, String message);
}
