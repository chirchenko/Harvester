package graphics;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import geometry.Point;
import gui.JADisplay;
import logginig.Logger;
import tools.GoogleTools;

public class Map{
	
	private Point SW, NE;
	private BufferedImage image;
	Logger logging = Logger.getLogger(Map.class);
	
	public Point center;

//	/**
//	 * Creates blank map
//	 */
//	public Map() {
//		this.SW = null;
//		this.NE = null;
//		
//		defaultImage = createImageIcon(DEFAULTIMAGEPATH, "Default Image");
//	}
	
	/**
	 * Creates map to display polygon.
	 * Map contains SW and NE points and GoogleMap image
	 * @param polygon
	 */
	public Map(Dimention ovf, Dimension canvasSize, JADisplay display) {				
		image = GoogleTools.getMapImage(ovf, canvasSize, display.zoom, "/img/blank.png");		

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
