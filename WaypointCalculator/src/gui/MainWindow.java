package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import geometry.Displayable;
import geometry.Point;
import geometry.Polygon;
import logginig.Logger;
import logic.WaypointFinder;

@SuppressWarnings("serial")
public class MainWindow  extends JFrame {
	protected static Logger logger = Logger.getLogger(MainWindow.class);
	
	final static int windowWidth = 1280;
	final static int windowhHeight = 924;
	
	private JAFieldList fieldList;
	private JAMachinaryList machineList;
//	private JAInteger sections;
	private JADisplay display;	

	private WaypointFinder wpf;
	
	public static final String GROUP_FIELD = "field";
	public static final String GROUP_WP = "waypoints";
	public static final String GROUP_SEGMENT = "segment";
	
	public static double workWidth = 0;
	public static List<Point> fieldPoints = null;
	
	public MainWindow() throws HeadlessException {
		super();	
		initUI();	
	}

	public void initUI() {    
        setTitle("Waypoint Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JPanel generalPanel = new JPanel(new BorderLayout());	    
        JPanel rightPanel = new JPanel();
        
        JPanel plcHldrDisp = new JPanel(new BorderLayout());    
        JPanel plcHldrConsole = new JPanel(new BorderLayout());    
        
        fieldList = new JAFieldList();
        machineList = new JAMachinaryList();
//        sections = new JAInteger("Work width");
        display = new JADisplay();    
        
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(300, 300));
        rightPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        generalPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    
        plcHldrDisp.add(display);        
        plcHldrConsole.add(new JAConsole());

        
        rightPanel.add(fieldList);
        rightPanel.add(machineList);
//        rightPanel.add(sections);
        rightPanel.add(plcHldrConsole);        
        generalPanel.add(plcHldrDisp);
        
        JButton flip = new JButton("Switch veiw");  
        rightPanel.add(flip);
        
        JButton calculate = new JButton("Go");
        rightPanel.add(calculate);

        this.add(generalPanel, BorderLayout.CENTER);
        this.add(rightPanel, BorderLayout.EAST);
        
        calculate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
//				workWidth = sections.getSections();
				
				logger.info("Invoking building waypoints"); 
				if(workWidth == 0 || fieldPoints == null){
					logger.info("Datasource not ready"); 
					return;
				}
				logger.info("Datasource ready"); 
				wpf = new WaypointFinder(fieldPoints);
				
				display.clearDisplayObject(GROUP_WP);

				display.addDisplayObject(GROUP_WP, wpf.ovf, Color.RED);
				
				display.addDisplayObject(GROUP_WP, wpf.getWaypoints(), Color.RED);
				display.addDisplayObject(GROUP_WP, wpf.getPath(), Color.YELLOW);
				
				display.render();
			}
		});
        
        fieldList.displayList.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount()==2 && !e.isConsumed()){
                	e.consume();
                	
                	fieldPoints = fieldList.getSelected().getFieldPoints();
                	
                	Polygon polygon = new Polygon(fieldPoints);
					display.setMapForArea(polygon.getDimention());
					
					display.getCanvas().clear();
					
					display.addDisplayObject(GROUP_FIELD, (ArrayList<Point>) polygon, new Color(0, 255, 0, 127));
					display.addDisplayObject(GROUP_FIELD, (Displayable) polygon, new Color(50, 30, 210, 32));
					
					display.getCanvas().render();
                }
            }
        });
        
        machineList.displayList.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
            	workWidth = machineList.getSelected().getWorkItemWidth();   
            }
        });
        
//        pointList.getLoadPointsButton().addActionListener(new ActionListener(){  
//        	@Override
//	    	public void actionPerformed(ActionEvent e){
//	    		JFileChooser fc = new JFileChooser(new File("."));
//    	        int returnVal = fc.showOpenDialog(getParent());
//
//    	        if (returnVal == JFileChooser.APPROVE_OPTION) {
//    	        	
//    	        	logger.info("Opening: " + fc.getSelectedFile().getName() + ".");
//    	        	
//    	        	Polygon polygon;
//    	        	try {
//    	        		polygon = new Polygon(SemiFileDS.readFile(fc.getSelectedFile()));
//					} catch (IOException e1) {
//						logger.info(e1.getMessage());
//						return;
//					}       	        	

//    	        } else {
//    	        	logger.info("Open command cancelled by user." + "\n");
//    	        }
//		    }
//		 });	
        
		flip.addActionListener(new ActionListener() {					
			@Override
			public void actionPerformed(ActionEvent e) {
				swipeWindows(plcHldrDisp, plcHldrConsole);
				((JButton) e.getSource()).getParent().getParent().revalidate();
				((JButton) e.getSource()).getParent().getParent().repaint();
				display.getCanvas().render();
			}
		});
	}

	private void swipeWindows(JPanel plcHldrDisp, JPanel plcHldrConsole) {
		if(plcHldrDisp.getComponentCount() == 1 && plcHldrConsole.getComponentCount() == 1){
			JPanel component = (JPanel) plcHldrConsole.getComponents()[0];
			JPanel componentOther = (JPanel) plcHldrDisp.getComponents()[0];
			plcHldrConsole.removeAll();
			plcHldrConsole.add(componentOther);
			componentOther.setPreferredSize(new Dimension(componentOther.getParent().getPreferredSize().width - 10, 200));
			
			plcHldrDisp.removeAll();
			plcHldrDisp.add(component);
			component.setPreferredSize(new Dimension(component.getParent().getPreferredSize().width, component.getParent().getPreferredSize().height));
		}
	}
}

