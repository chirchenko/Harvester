package geometry;

class CartesianPoint {
	private final double x;
	private final double y;
	private final double z;
	
	public CartesianPoint(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public CartesianPoint(double r, Point p) {
		double φ = p.getLatitude();
		double θ = p.getLongitude();
		x = r * Math.sin(φ) * Math.cos(θ);
		y = r * Math.sin(φ) * Math.sin(θ); 
		z = r * Math.cos(φ);

	}
}
