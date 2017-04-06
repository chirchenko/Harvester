package graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D.Float;

import geometry.Segment;
import gui.JACanvas;

@SuppressWarnings("serial")
public class JGSegment extends Float implements CanvasObject {
	private Color color;
	private int size = 2;
	private JACanvas canvas;
	private Segment segment;
	
	
	public JGSegment(Segment segment, JACanvas canvas, Color color) {
		super();
		this.segment = segment;
		this.canvas = canvas;
		this.color = color;
	}
	
	@Override
	public void show(Graphics g) {
		this.setLine(canvas.getDisplayX(segment.getA().getLongitude())
				, canvas.getDisplayY(segment.getA().getLatitude())
				, canvas.getDisplayX(segment.getB().getLongitude())
				, canvas.getDisplayY(segment.getB().getLatitude()));
	
		 Graphics2D g2 = (Graphics2D) g;
		 g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		 
		 g2.setStroke(new BasicStroke(size));
		 g2.setPaint(color);
		 g2.draw(this);		 
		
	}
}
