package domains;

import java.util.ArrayList;
import java.util.List;

public class Machinery {
	
	private static List<Machine> machinery = new ArrayList<>();
	
	static{
		machinery.add(new Machine("John Deere T660", 5));
		machinery.add(new Machine("Claas Lexion 480", 5.3));
		machinery.add(new Machine("Єнісей 1200", 4));
		machinery.add(new Machine("Славутич КЗС 9", 3));
		machinery.add(new Machine("Єнісей 950-1", 3.5));
			
	}
	
	public static class Machine{
		private String name;
		private double workItemWidth;
		public Machine(String name, double workItemWidth) {
			this.name = name;
			this.workItemWidth = workItemWidth;
		}
		public String getName() {
			return name;
		}
		public double getWorkItemWidth() {
			return workItemWidth;
		}
		@Override
		public String toString() {
			return name + ", ширина=" + workItemWidth;
		}
		
		
	}

	public static List<Machine> getMachinery() {
		return machinery;
	}
	
}
