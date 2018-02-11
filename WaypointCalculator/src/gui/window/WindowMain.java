package gui.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;

import domains.Fields;
import domains.Machinery;
import domains.PersistentObject;
import domains.Points;
import geometry.Displayable;
import geometry.Point;
import geometry.Polygon;
import gui.ConsolePanel;
import gui.DisplayPanel;
import gui.panel.FieldListPanel;
import gui.panel.MachineListPanel;
import logginig.Logger;
import logic.WaypointFinder;
import tools.Config;
import tools.ExportImport;
import tools.IOTools;
import tools.Parameter;

@SuppressWarnings("serial")
public class WindowMain extends JFrame{
	private final static Logger logger = Logger.getLogger(WindowMain.class);

	public static WindowMain instance;
	
	private FieldListPanel fieldList;
	private MachineListPanel machineList;
	private DisplayPanel display;	
	private ConsolePanel console;

	private WaypointFinder wpf;

	private final WindowAbout aboutFrame = new WindowAbout();

	public WindowMain() throws HeadlessException {
		super();
    	Image icon = IOTools.readImageFromUrl(Config.getString(Parameter.APP_ICON_PATH));
    	setIconImage(icon);

		initUI();
		
		WindowMain.instance = this;
	}

	private void initUI() {
        setTitle("Waypoint Calculator");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        initMenu();       
        JPanel windowContainer = new JPanel(new BorderLayout());	    
        JPanel sidePanel = new JPanel(new BorderLayout());
             
        fieldList = new FieldListPanel("Fields", () -> {
        	
				display.field = fieldList.getSelected();
		    	
		    	Polygon polygon = new Polygon(display.field.points);
				display.setMapForArea(polygon.getDimention());
				
				display.getCanvas().clear();
				
				display.addDisplayObject(DisplayPanel.GROUP_FIELD, (ArrayList<Point>) polygon, new Color(0, 255, 0, 127));
				display.addDisplayObject(DisplayPanel.GROUP_FIELD, (Displayable) polygon, new Color(50, 30, 210, 32));
				
				display.getCanvas().render();
				display.label.setText("Now please select harvester");
				
		});
        
        machineList = new MachineListPanel("Harvesters",() -> {
        	
        	display.machine = machineList.getSelected(); 		
    		
    		logger.info("Invoking building waypoints"); 
    		if(display.field == null){
    			logger.info("Field is not ready");
    			display.label.setText("Please select field[!!!]");
    			return;
    		}

    		wpf = new WaypointFinder(display.field.points, display.machine.workWidth);
    		
    		display.clearDisplayObject(DisplayPanel.GROUP_WP);
    		
    		display.addDisplayObject(DisplayPanel.GROUP_WP, wpf.getWaypoints(), Color.RED);
    		display.addDisplayObject(DisplayPanel.GROUP_WP, wpf.getPath(), Color.YELLOW);		
    		display.render();
    		
    		double distance = wpf.getPath().getTotalDistance();
    		int turns = wpf.getPath().getWaypoints().size();
    		double fuelConsumption = display.machine.fuel;
    		double totalConsumption = fuelConsumption * distance / 1000;
    		String result = String.format("Overal distance: %.2f m, "
						    				+ "Number of turns: %d, "
						    				+ "Fuel consumption: %.2f litres(%.4f l/km)"
						    				, distance, turns, totalConsumption, fuelConsumption);
    		logger.info(result);
    		display.label.setText(result);
    		
        });
        display = new DisplayPanel();    
        console = new ConsolePanel();       

        windowContainer.add(display);
        JSplitPane mainSplitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, windowContainer, sidePanel);
        mainSplitPanel.setResizeWeight(0.9);

        JSplitPane splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, fieldList, machineList);
        JSplitPane splitPanel2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitPanel, console);
        
        splitPanel.setResizeWeight(0.5);
        splitPanel2.setResizeWeight(0.5);
        sidePanel.add(splitPanel2, BorderLayout.CENTER);
        
        this.add(mainSplitPanel, BorderLayout.CENTER);   
        
        Fields.addDataChangedListener(fieldList);
        Machinery.addDataChangedListener(machineList);
	}
	private void runAbout() {
		aboutFrame .display();
	}

	private void runExport() {
		JFileChooser fileChooser = new JFileChooser(
				new File(Config.getString(Parameter.APP_EXPORT_DIR)));
		fileChooser.setDialogTitle("Export to");
		fileChooser.setSelectedFile(
				new File(Config.getString(Parameter.APP_EXPORT_DIR)
						+ "/export.xml"));
		fileChooser.setFileFilter(new FileNameExtensionFilter("XML document", "xml"));
		
		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				ExportImport.exportToXML(fileChooser.getSelectedFile());
				JOptionPane.showMessageDialog(this,
					    "Exported completed");
			} catch (JAXBException e) {
				JOptionPane.showMessageDialog(this,
					    "Error occured while export:\n" + e.getCause().getMessage(),
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void runImport() {
		JFileChooser fileChooser = new JFileChooser(
				new File(Config.getString(Parameter.APP_EXPORT_DIR)));
		
		fileChooser.setDialogTitle("Import from");
		fileChooser.setSelectedFile(
				new File(Config.getString(Parameter.APP_EXPORT_DIR) + "/export.xml"));
		
		fileChooser.setFileFilter(new FileNameExtensionFilter("XML document", "xml"));
		
		logger.info("Import data");
		if (fileChooser.showDialog(this, "Import") == JFileChooser.APPROVE_OPTION) {
			try(InputStream is = new FileInputStream(fileChooser.getSelectedFile())) {
				ExportImport.importFromXML(is, PersistentObject::save);

				Machinery.loadAll();
				Points.loadAll();
				Fields.loadAll();
				
				logger.info("Import successfull");
				JOptionPane.showMessageDialog(this,
					    "Import completed");
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this,
					    "Error occured while import:\n" + e.getCause().getMessage(),
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
			}
		} else {
			logger.info("Canceled import");
		}
	}
	
	private void initMenu(){
		 JMenuBar menubar = new JMenuBar();

       JMenu file = new JMenu("File");
       file.setMnemonic(KeyEvent.VK_F);
       
       JMenu tools = new JMenu("Tools");
       tools.setMnemonic(KeyEvent.VK_T);
       
       JMenu help = new JMenu("Help");
       help.setMnemonic(KeyEvent.VK_H);

       JMenuItem eMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
       eMenuItem.setToolTipText("Exit application");
       eMenuItem.addActionListener((ActionEvent event) -> System.exit(0));
       
       JMenuItem importMenuItem = new JMenuItem("Import", KeyEvent.VK_I);
       importMenuItem.setToolTipText("Import from XML file");
       importMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK));
       importMenuItem.addActionListener((ActionEvent event) -> runImport());
       
       JMenuItem exportMenuItem = new JMenuItem("Export", KeyEvent.VK_E);
       exportMenuItem.setToolTipText("Export to XML file");
       exportMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
       exportMenuItem.addActionListener((ActionEvent event) -> runExport());
       
       JMenuItem aboutMenuItem = new JMenuItem("About", KeyEvent.VK_A);
       aboutMenuItem.setToolTipText("Show information");
       aboutMenuItem.addActionListener((ActionEvent event) -> runAbout());

       file.add(eMenuItem);
       
       tools.add(importMenuItem);
       tools.add(exportMenuItem);
       
       help.add(aboutMenuItem);

       menubar.add(file);
       menubar.add(tools);
       menubar.add(help);

       setJMenuBar(menubar);
	}
}

