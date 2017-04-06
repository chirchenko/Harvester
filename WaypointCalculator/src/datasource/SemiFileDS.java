package datasource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import geometry.Point;
import logginig.Logger;

public class SemiFileDS{
	private static Logger logger = Logger.getLogger(SemiFileDS.class);
	
	private static String separator = ",";

	public static List<Point> readFile(File path) throws IOException {
		List<Point> formPoints = new ArrayList<>();
		try(Scanner sc = new Scanner(path)){			
			String line = "-1";
			
			logger .info(String.format("\nGetting form points"));
			while(sc.hasNextLine()){
				line = sc.nextLine();
				if("".equals(line)) continue;
				logger.info(String.format("Reading line: %s", line));
				String[] pair = line.split(separator);
				Point p = new Point(Double.parseDouble(pair[0]), Double.parseDouble(pair[1]));
				logger.info(String.format("  Adding point to list: %s", p));
				formPoints.add(p);
			}
			
		} catch (IOException e) {
			System.err.println(String.format("Error occured: %s", e.getMessage()));
			throw e; 
		}catch (NoSuchElementException | NumberFormatException e) {
			System.err.println(String.format("Source file is not correctly formated: %s", e.getMessage()));
			throw e; 
		}
		logger.info("\n");
		logger.info("");
		logger.info("#######################################################");
		return formPoints;
	}
	
}
