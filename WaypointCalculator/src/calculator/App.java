package calculator;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JWindow;

import datasource.DataSource;
import gui.LogoWindow;
import gui.MainWindow;
import logginig.Logger;
import logginig.Logger.LogLevel;
import logginig.StdOutLogger;
import logic.WaypointFinder;

public class App{
	public static Logger logger = Logger.getLogger(App.class);
	public static DataSource ds;
	//public static JACanvas canvas;
	public static int COORDINATE_PRECISION = 6;
	public static WaypointFinder wpf;
	public static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    
	public static void main(String[] args) throws IOException{		

		Logger.setLogLevel(LogLevel.INFO.getMask() | LogLevel.DEBUG.getMask());
		Logger.subscribe(new StdOutLogger());
		
		init();
		//showIface();				
	}

	private static void init() {
		int logoWidth = dim.width*2/5;
		int logoHeight = dim.height*2/5;
		
		LogoWindow logo = new LogoWindow(logoWidth, logoHeight);
		logo.setLocation((dim.width/2) - logoWidth/2, (dim.height/2) - logoHeight/2);
		EventQueue.invokeLater(() -> {
			logo.setVisible(true);
        });
		
	}
	
	private static void showIface() {
		MainWindow mw= new MainWindow();
		mw.setSize(dim.width*3/4, dim.height*3/4);
		mw.setLocationByPlatform(true);
		EventQueue.invokeLater(() -> {
			mw.setVisible(true);
        });
	}

}
