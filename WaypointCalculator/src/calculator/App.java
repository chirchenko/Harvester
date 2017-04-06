package calculator;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.IOException;

import datasource.DataSource;
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
    
	public static void main(String[] args) throws IOException{		

		Logger.setLogLevel(LogLevel.INFO.getMask() | LogLevel.DEBUG.getMask());
		Logger.subscribe(new StdOutLogger());
		//ds = new SemiFileDS(new File("ds.txt"));

		//canvas = new JACanvas();
		
		//wpf = new WaypointFinder(ds);
		
		//System.out.println(wpf.getWaypoints());
		
		MainWindow mw= new MainWindow();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		mw.setSize(screenSize.width*3/4, screenSize.height*3/4);
		mw.setLocationByPlatform(true);
		EventQueue.invokeLater(() -> {
			mw.setVisible(true);
        });
	}
}
