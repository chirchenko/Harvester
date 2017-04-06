package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import gui.JACanvas;

@SuppressWarnings("serial")
public class JGPoint extends Point implements CanvasObject {
	private Color color;
	private int size = 5;
	private JACanvas canvas;
	private geometry.Point point;

	public JGPoint(geometry.Point point, JACanvas canvas, Color color) {
		super();
		this.point = point;
		this.color = color;
		this.canvas = canvas;
	}
	
	public void show(Graphics g){
		int x = canvas.getDisplayX(point.getLongitude());
		int y = canvas.getDisplayY(point.getLatitude());
		this.setLocation(new Point(x, y));
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setPaint(color);
		g2.fill(new Ellipse2D.Double(this.getX() - size/2, this.getY() - size/2, size, size));
		
		if(false){
			g2.setPaint(Color.WHITE);
			int indentX = 10, indentY = -10;
			g2.drawString(point.toString(), x + indentX, y + indentY);
		}
		
	}
}
