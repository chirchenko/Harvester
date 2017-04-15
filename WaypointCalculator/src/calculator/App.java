package calculator;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;

import gui.WindowLogo;
import logginig.AbstractLogger.LogLevel;
import logginig.Logger;
import logginig.PrintStreamLogger;

public class App{
	public static Logger logger = Logger.getLogger(App.class);
	public static int COORDINATE_PRECISION = 6;
	public static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	public final static String APP_ICON_PATH = "res/img/icon64.png";
	public static final String APP_BLANK_MAP = "res/img/blank.png";
	public static final String APP_LOGO_IMAGE = "res/img/logo.jpg";
	public static final String APP_EXPORT_DIR = "export";
	public static final String APP_RES_DIR = "res";
	
	public static final boolean SHOW_POINT_TEXT = true;
	
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
		
		new WindowLogo();					
	}

}
