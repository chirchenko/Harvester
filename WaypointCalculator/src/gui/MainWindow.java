package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.BevelBorder;

import geometry.Displayable;
import geometry.Point;
import geometry.Polygon;
import logginig.Logger;
import logic.WaypointFinder;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements MouseListener{
	protected static Logger logger = Logger.getLogger(MainWindow.class);
	
	final static int windowWidth = 1280;
	final static int windowhHeight = 924;
	
	private JAFieldList fieldList;
	private JAMachinaryList machineList;
	private JADisplay display;	
	private JAConsole console;

	private WaypointFinder wpf;
	
	public static final String GROUP_FIELD = "field";
	public static final String GROUP_WP = "waypoints";
	public static final String GROUP_SEGMENT = "segment";
	
	public static double workWidth = 0;
	
	public MainWindow() throws HeadlessException {
		super();	
		File iconFile = new File("resources/img/icon64.png");
        if(iconFile.exists()){
        	Image icon;
			try {
				icon = ImageIO.read(iconFile);
	        	setIconImage(icon);    
			} catch (IOException e1) {
				e1.printStackTrace();
			}    	        
        }
        
		initUI();	
	}

	public void initUI() {    
        setTitle("Waypoint Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JMenuBar menubar = new JMenuBar();

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        
        JMenu tools = new JMenu("Tools");
        file.setMnemonic(KeyEvent.VK_T);
        
        JMenu help = new JMenu("Help");
        file.setMnemonic(KeyEvent.VK_H);

        JMenuItem eMenuItem = new JMenuItem("Exit");
        eMenuItem.setMnemonic(KeyEvent.VK_X);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener((ActionEvent event) -> {
            System.exit(0);
        });
        
        JMenuItem importMenuItem = new JMenuItem("Import");
        importMenuItem.setMnemonic(KeyEvent.VK_I);
        importMenuItem.setToolTipText("Import from XML file");
        importMenuItem.addActionListener((ActionEvent event) -> {
            runImport();
        });
        
        JMenuItem exportMenuItem = new JMenuItem("Export");
        exportMenuItem.setMnemonic(KeyEvent.VK_E);
        exportMenuItem.setToolTipText("Export to XML file");
        exportMenuItem.addActionListener((ActionEvent event) -> {
            runExport();
        });
        
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.setMnemonic(KeyEvent.VK_A);
        aboutMenuItem.setToolTipText("Show information");
        aboutMenuItem.addActionListener((ActionEvent event) -> {
            runAbout();
        });

        file.add(eMenuItem);
        
        tools.add(importMenuItem);
        tools.add(exportMenuItem);
        
        help.add(aboutMenuItem);

        menubar.add(file);
        menubar.add(tools);
        menubar.add(help);

        setJMenuBar(menubar);
        
        JPanel windowContainer = new JPanel(new BorderLayout());	    
        JPanel sidePanel = new JPanel(new BorderLayout());
        
        windowContainer.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        sidePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
                        
        fieldList = new JAFieldList();
        machineList = new JAMachinaryList();
        display = new JADisplay();    
        console = new JAConsole();
        
        enableComponents(machineList, false);       

        windowContainer.add(display);
        JSplitPane mainSplitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, windowContainer, sidePanel);
        mainSplitPanel.setResizeWeight(0.9);

        JSplitPane splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, fieldList, machineList);
        JSplitPane splitPanel2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitPanel, console);
        
        splitPanel.setResizeWeight(0.5);
        splitPanel2.setResizeWeight(0.5);
        sidePanel.add(splitPanel2, BorderLayout.CENTER);
        
        this.add(mainSplitPanel, BorderLayout.CENTER);   
        
        fieldList.displayList.addMouseListener(this);        
        machineList.displayList.addMouseListener(this);
	}
	
	private void runAbout() {
		// TODO Auto-generated method stub
		
	}

	private void runExport() {
		// TODO Auto-generated method stub
		
	}

	private void runImport() {
		// TODO Auto-generated method stub
		
	}

	private void fieldSelected() {
		display.field = fieldList.getSelected();
		    	
    	Polygon polygon = new Polygon(display.field.fieldPoints);
		display.setMapForArea(polygon.getDimention());
		
		display.getCanvas().clear();
		
		display.addDisplayObject(GROUP_FIELD, (ArrayList<Point>) polygon, new Color(0, 255, 0, 127));
		display.addDisplayObject(GROUP_FIELD, (Displayable) polygon, new Color(50, 30, 210, 32));
		
		display.getCanvas().render();
		enableComponents(machineList, true);
		display.label.setText("Please select harvester");
		
	}
	
	private void harvesterSelected(){
		display.machine = machineList.getSelected(); 		
		
		logger.info("Invoking building waypoints"); 
		if(display.machine.workWidth == 0 || display.field.fieldPoints == null){
			logger.info("Datasource not ready"); 
			return;
		}
		logger.info("Datasource ready"); 
		wpf = new WaypointFinder(display.field.fieldPoints, display.machine.workWidth);
		
		display.clearDisplayObject(GROUP_WP);
		
		display.addDisplayObject(GROUP_WP, wpf.getWaypoints(), Color.RED);
		display.addDisplayObject(GROUP_WP, wpf.getPath(), Color.YELLOW);		
		display.render();
		
		double distance = wpf.getPath().getTotalDistance();
		int turns = wpf.getPath().getWaypoints().size();
		double fuelConsumption = display.machine.fuel;
		double totalConsumption = fuelConsumption * distance / 1000;
		String result = String.format("Overal distance: %.2f m, Number of turns: %d, Fuel consumption: %.2f litres(%.4f l/km)", distance, turns, totalConsumption, fuelConsumption);
		logger.info(result);
		display.label.setText(result);
	}
	
	@Override
	public void mouseClicked(MouseEvent e){
        if(e.getClickCount()==2 && !e.isConsumed()){
        	e.consume();
        	Object source = e.getSource();
        	if(source == fieldList.displayList){
        		fieldSelected();
        	} else if(source == machineList.displayList){
        		harvesterSelected();
        	}
        	
        }
    }
	
	public static void enableComponents(Container container, boolean enable) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            component.setEnabled(enable);
            if (component instanceof Container) {
                enableComponents((Container)component, enable);
            }
        }
    }

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}

