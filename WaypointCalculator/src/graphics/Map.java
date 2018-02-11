package graphics;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import geometry.Point;
import gui.DisplayPanel;
import logginig.Logger;
import tools.Config;
import tools.GoogleTools;
import tools.IOTools;
import tools.Parameter;

public class Map{
	
	private Point SW, NE;
	private final BufferedImage image;
	private final Logger logging = Logger.getLogger(Map.class);
	
	public final Point center;
	
	/**
	 * Creates map to display polygon.
	 * Map contains SW and NE points and GoogleMap image
	 */
	public Map(Dimention ovf, Dimension canvasSize, DisplayPanel display) {				
		image = IOTools.getMapImage(GoogleTools.URL_PATTERN, ovf, canvasSize, display.zoom
				, Config.getString(Parameter.APP_BLANK_MAP));

		Point center = ovf.getCenter(); 
		if(image != null) {
			double hDistance = image.getHeight() * display.metersInPixel;
			double vDistance = image.getWidth() * display.metersInPixel;
			
			this.SW = center.moveTo(90 * 3, hDistance / 2).moveTo(90 * 2, vDistance / 2);
			this.NE = center.moveTo(90 * 1, hDistance / 2).moveTo(90 * 4, hDistance / 2);
		}else{
			this.SW = ovf.getSquareDiagonal().getA();
			this.NE = ovf.getSquareDiagonal().getB();
		}
		this.center = ovf.getCenter();
		
		logging.info(String.format("Map SW: %s\tNE:%s", SW, NE));
	}

	public BufferedImage getImage() {
		return image;
	}

	public Point getSW() {
		return SW;
	}

	public void setSW(Point SW) {
		this.SW = SW;
	}

	public Point getNE() {
		return NE;
	}
	
	public void setNE(Point NE) {
		this.NE = NE;
	}
	
}
