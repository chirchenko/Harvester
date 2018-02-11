package logginig;

import logginig.AbstractLogger.LogLevel;

public interface LogListener {
	void update(LogLevel level, Class<?> clazz, String message);
}
