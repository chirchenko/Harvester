package datasource;

import java.util.List;

import geometry.Point;

public class DataSource{

	private List<Point> formPoints;
	private double workWidth;
	
	
	public DataSource(List<Point> formPoints, double workWidth) {
		super();
		this.formPoints = formPoints;
		this.workWidth = workWidth;
		
		if(formPoints == null || workWidth == 0) 
			throw new IllegalArgumentException("DataSource is not ready");
	}
	public List<Point> getFormPoints() {
		return formPoints;
	}
	public void setFormPoints(List<Point> formPoints) {
		this.formPoints = formPoints;
	}
	@Override
	public String toString() {
		return "DataSourceImpl [formPoints=" + formPoints + ", workWidth=" + workWidth + "]";
	}
	public double getWorkWidth() {
		// TODO Auto-generated method stub
		return this.workWidth;
	}	
	
	
}
