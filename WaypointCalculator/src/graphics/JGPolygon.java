package graphics;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.List;

import geometry.Point;
import gui.JACanvas;

@SuppressWarnings("serial")
public class JGPolygon extends Polygon implements CanvasObject {
	private Color color;
	private JACanvas canvas;
	private geometry.Polygon polygon;
	
	public JGPolygon(List<Point> list, JACanvas canvas, Color color) {
		super();
		this.color = color;
		this.polygon = new geometry.Polygon(list);
		this.canvas = canvas;		 
	}

	@Override
	public void show(Graphics g) {
		this.reset();
		for(Point p : polygon){
			 this.addPoint(canvas.getDisplayX(p.getLongitude()), canvas.getDisplayY(p.getLatitude()));
		 }
		
		 Graphics2D g2 = (Graphics2D) g;
		 g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		 g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f));
		 g2.setPaint(color);
		 
		 g2.fill(this);
		 g2.setPaint(new Color(color.getRed(), color.getGreen(), color.getBlue(), 255));
		 g2.draw(this);
	}
	

}
