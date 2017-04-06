package logginig;

import java.io.PrintStream;

import logginig.Logger.LogLevel;

public class StdOutLogger implements LogListener {
	private int pad = 25;
	private PrintStream ps = System.out;
		
	@Override
	public void update(LogLevel level, Class<?> clazz, String message) {
		if ((level.getMask() & Logger.getLogLevel()) > 0){
		    log(level, clazz, message);
		}
	}
	
	public void log(LogLevel level, Class<?> clazz, String message){
		if(message == null) return;
		String source = (clazz == null) ? String.format("") : clazz.getSimpleName();
		
		if(message.length() > 1 && "\n".equals(message.substring(0, 1))){
			message = message.substring(1, message.length());
			ps .println(String.format("%1$-" + pad + "s", source));
		}
		ps.println(String.format("%1$-5s%2$-" + pad + "s",level, source) + message);
	}


}
