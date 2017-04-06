package datasource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import geometry.Point;
import geometry.Segment;
import logginig.Logger;

public class FileDS{
	private static Logger logger = Logger.getLogger(FileDS.class);
	
	private static String separator = ",";

	public static Segment base;
	public static int devidor;	
	
	public static List<Point> readFile(File file) throws IOException {
		List<Point> formPoints = new ArrayList<>();
		
		try(Scanner sc = new Scanner(file)){			
			String line = "-1";
			
			logger.info(String.format("\nGetting form points"));
			while(true){
				line = sc.nextLine();
				if("".equals(line)) break;
				logger.info(String.format("Reading line: %s", line));
				String[] pair = line.split(separator);
				Point p = new Point(Double.parseDouble(pair[0]), Double.parseDouble(pair[1]));
				logger.info(String.format("  Adding point to list: %s", p));
				
				formPoints.add(p);
			}

			logger.info(String.format("\nGetting base segment"));		
			
			line = sc.nextLine();
			logger.info(String.format("Reading line: %s", line));
			Point a = new Point(Double.parseDouble(line.split(separator)[0]),Double.parseDouble(line.split(separator)[1]));			
			logger.info(String.format("Setting point A: %s", a));
			
			line = sc.nextLine();
			logger.info(String.format("Reading line: %s", line));
			Point b = new Point(Double.parseDouble(line.split(separator)[0]),Double.parseDouble(line.split(separator)[1]));
			logger.info(String.format("Setting point B: %s",b));
			
			base = new Segment(a, b);
			logger.info(String.format("Base segment is: %s",base));
			
			logger.info(String.format("\nGetting devidor"));
			
			if(!sc.nextLine().equals("")) throw new NoSuchElementException();
			line = sc.nextLine();
			devidor = Integer.parseInt(line);					
			logger.info(String.format("Devidor is: %d", devidor));
			
		} catch (IOException e) {
			System.err.println(String.format("Error occured: %s", e.getMessage()));
			throw e; 
		}catch (NoSuchElementException | NumberFormatException e) {
			System.err.println(String.format("Source file is not correctly formated: %s", e.getMessage()));
			throw e; 
		}
		logger.info("\n");
		logger .info("#######################################################");
		return formPoints;
	}

}
