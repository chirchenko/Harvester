package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Event;
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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;

import calculator.App;
import domains.Fields;
import domains.Machinery;
import geometry.Displayable;
import geometry.Point;
import geometry.Polygon;
import logginig.Logger;
import logic.WaypointFinder;
import tools.ExportImport;

@SuppressWarnings("serial")
public class WindowMain extends JFrame implements MouseListener{
	protected static Logger logger = Logger.getLogger(WindowMain.class);
	
	final static int windowWidth = 1280;
	final static int windowhHeight = 924;
	
	private GuiFieldsPanel fieldList;
	private GuiMachinaryPanel machineList;
	private GuiDisplayPanel display;	
	private GuiConsolePanel console;

	private WaypointFinder wpf;

	private WindowAbout aboutFrame = new WindowAbout();
	
	public static final String GROUP_FIELD = "field";
	public static final String GROUP_WP = "waypoints";
	public static final String GROUP_SEGMENT = "segment";
	
	public static double workWidth = 0;
	
	public WindowMain() throws HeadlessException {
		super();	
		File iconFile = new File(App.APP_ICON_PATH);
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
	
	public void initMenu(){
		 JMenuBar menubar = new JMenuBar();

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        
        JMenu tools = new JMenu("Tools");
        tools.setMnemonic(KeyEvent.VK_T);
        
        JMenu help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);

        JMenuItem eMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener((ActionEvent event) -> {
            System.exit(0);
        });
        
        JMenuItem importMenuItem = new JMenuItem("Import", KeyEvent.VK_I);
        importMenuItem.setToolTipText("Import from XML file");
        importMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, Event.CTRL_MASK));
        importMenuItem.addActionListener((ActionEvent event) -> {
            runImport();
        });
        
        JMenuItem exportMenuItem = new JMenuItem("Export", KeyEvent.VK_E);
        exportMenuItem.setToolTipText("Export to XML file");
        exportMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Event.CTRL_MASK));
        exportMenuItem.addActionListener((ActionEvent event) -> {
            runExport();
        });
        
        JMenuItem aboutMenuItem = new JMenuItem("About", KeyEvent.VK_A);
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
	}

	public void initUI() {    
        setTitle("Waypoint Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        initMenu();       
        JPanel windowContainer = new JPanel(new BorderLayout());	    
        JPanel sidePanel = new JPanel(new BorderLayout());
             
        fieldList = new GuiFieldsPanel("Fields");
        machineList = new GuiMachinaryPanel("Harvesters");
        display = new GuiDisplayPanel();    
        console = new GuiConsolePanel();
        
        machineList.listEnabled(false);        

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
        
        Fields.addDataChangedListener(fieldList);
        Machinery.addDataChangedListener(machineList);
	}
	
	private void runAbout() {
		aboutFrame .display();
	}

	private void runExport() {
		JFileChooser fileChooser = new JFileChooser(new File("."));
		fileChooser.setDialogTitle("Export to");
		fileChooser.setSelectedFile(new File("export.xml"));
		fileChooser.setFileFilter(new FileNameExtensionFilter("XML document", "xml"));
		
		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				ExportImport.exportXML(fileChooser.getSelectedFile());
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
		JFileChooser fileChooser = new JFileChooser(new File("."));
		fileChooser.setDialogTitle("Import from");
		fileChooser.setSelectedFile(new File("export.xml"));
		fileChooser.setFileFilter(new FileNameExtensionFilter("XML document", "xml"));
		
		logger.info("Import data");
		if (fileChooser.showDialog(this, "Import") == JFileChooser.APPROVE_OPTION) {
			try {
				ExportImport.importXML(fileChooser.getSelectedFile());
			
				machineList.listEnabled(fieldList.getSelected() != null);
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

	private void fieldSelected() {
		display.field = fieldList.getSelected();
		    	
    	Polygon polygon = new Polygon(display.field.points);
		display.setMapForArea(polygon.getDimention());
		
		display.getCanvas().clear();
		
		display.addDisplayObject(GROUP_FIELD, (ArrayList<Point>) polygon, new Color(0, 255, 0, 127));
		display.addDisplayObject(GROUP_FIELD, (Displayable) polygon, new Color(50, 30, 210, 32));
		
		display.getCanvas().render();
		machineList.listEnabled(true);
		
	}
	
	private void harvesterSelected(){
		display.machine = machineList.getSelected(); 		
		
		logger.info("Invoking building waypoints"); 
		if(display.machine == null || display.machine.workWidth == 0 || display.field.points == null){
			logger.info("Datasource not ready"); 
			return;
		}
		logger.info("Datasource ready"); 
		wpf = new WaypointFinder(display.field.points, display.machine.workWidth);
		
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

