package tools;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import geometry.Point;
import graphics.Dimention;
import logginig.Logger;

public class GoogleTools {
	public static Logger logger = Logger.getLogger(GoogleTools.class);
	public final static int RADIUS = 6378137;
	final static int GLOBE_WIDTH = 256; // a constant in Google's map projection
	final static int ZOOM_MAX = 21;
	final static String URL_PATTERN = "https://maps.googleapis.com/maps/api/staticmap?center=%s,%s&zoom=%d&size=%dx%d&scale=2&maptype=hybrid&format=jpg";


	public static int getBoundsZoomLevel(Point northeast,Point southwest, int width, int height) {
	    double latFraction = (latRad(northeast.getLatitude()) - latRad(southwest.getLatitude())) / Math.PI;
	    double lngDiff = northeast.getLongitude() - southwest.getLongitude();
	    double lngFraction = ((lngDiff < 0) ? (lngDiff + 360) : lngDiff) / 360;
	    double latZoom = zoom(height, GLOBE_WIDTH, latFraction);
	    double lngZoom = zoom(width, GLOBE_WIDTH, lngFraction);
	    double zoom = Math.min(Math.min(latZoom, lngZoom),ZOOM_MAX);
	    return (int)(zoom);
	}
	private static double latRad(double lat) {
	    double sin = Math.sin(lat * Math.PI / 180);
	    double radX2 = Math.log((1 + sin) / (1 - sin)) / 2;
	    return Math.max(Math.min(radX2, Math.PI), -Math.PI) / 2;
	}
	private static double zoom(double mapPx, double worldPx, double fraction) {
	    final double LN2 = .693147180559945309417;
	    return (Math.log(mapPx / worldPx / fraction) / LN2);
	}	
	
	/**
	 * Returns image of map which fits given polygon on canvas size. Or returns default image 
	 * @param polygon
	 * @param canvasSize
	 * @param defaultPath
	 * @return
	 */
	public static BufferedImage getMapImage(Dimention ovf, Dimension canvasSize, int zoom, String defaultPath) {
		BufferedImage result = new BufferedImage(1240, 1240, BufferedImage.TYPE_INT_RGB);
		BufferedImage img;
		if(ovf == null || canvasSize == null) {				
			img = readImageFromUrl(defaultPath);
			result = (img == null) ? result : img; 
		}else{
			double lat = ovf.getCenter().getLatitude();
	        double lon = ovf.getCenter().getLongitude();
	        
			int size_w = 620;
			int size_h = 620;
        
        	String url = String.format(URL_PATTERN, lat, lon, zoom, size_w, size_h);
        	img = readImageFromUrl(url);
        	logger.info("Loaded map image from: " + url);
        	
        	result = (img == null) ? readImageFromUrl(defaultPath) :  img;	    			
		}			

		return result;
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. 
	 * @throws IOException */
	private static BufferedImage readImageFromUrl(String url){
	    if (url != null) {
	        try {
	        	if(url.startsWith("http")){
	        		return ImageIO.read(new URL(url));
	        	}else{
	        		return ImageIO.read(new File(url));
	        	}				
			} catch (IOException e) {
				System.err.println("Cannot read URL");
				e.printStackTrace();
			}
	    } else {
	        System.err.println("Image URL is null");
	    	return null;
	    }
		return null;
	}
	
//	public static double getMetersPerPixel(int zoom, double latitude){
//		return 156543.03392 * Math.cos(latitude * Math.PI / 180) / Math.pow(2, zoom);
//		
//	}
	
	public static double getMetersPerPixel(int zoom, double latitude){
		double factor = 0.009330692;
		for(int refZoom = 24; refZoom > zoom + 1; refZoom--){
			factor = factor * 2;
		}
		
		return factor * Math.cos(latitude * Math.PI / 180) ;
		
	}
}
