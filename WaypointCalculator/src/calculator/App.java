package calculator;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;

import datasource.DataSource;
import gui.LogoWindow;
import logginig.Logger;
import logginig.Logger.LogLevel;
import logginig.PrintStreamLogger;

public class App{
	public static Logger logger = Logger.getLogger(App.class);
	public static DataSource ds;
	public static int COORDINATE_PRECISION = 6;
	public static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    
	public static void main(String[] args) throws IOException{	
		String logFileName = "log.txt";
		File logFile = new File(logFileName);
		if(logFile.exists()) logFile.delete();
		logFile = new File(logFileName);		

		Logger.setLogLevel(LogLevel.INFO.getMask() | LogLevel.DEBUG.getMask());
		Logger.subscribe(new PrintStreamLogger(System.out));
		Logger.subscribe(new PrintStreamLogger(new PrintStream(logFile)));
		
		ClassLoader cl = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader)cl).getURLs();

        logger.info("Class-path:");
        for(URL url: urls){
        	logger.info("\t" + url.getFile());
        }
		
		new LogoWindow();					
	}

}
