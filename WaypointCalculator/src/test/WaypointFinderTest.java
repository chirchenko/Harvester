package test;

import calculator.App;
import domains.Domains;
import domains.Fields;
import geometry.Point;
import logic.WaypointFinder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tools.Config;
import tools.ExportImport;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WaypointFinderTest{
	private static WaypointFinder waypointFinder;
	private static final String fieldPath = "res/tests/logic/field_terezyno.xml";
	private static final String resultPath = "res/tests/logic/waypoints.lst";

	@BeforeClass
	public static void testSetup() throws IOException, JAXBException {
		InputStream is = new FileInputStream(new File(fieldPath));

		Domains domains = ExportImport.importDomains(is);
		Fields.Field field = domains.fields.stream().filter(f -> f.name.equals("Терезино №603")).findFirst().get();

		waypointFinder = new WaypointFinder(field.points, 5.0d);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExceptionIsThrown() {
	}

	@Test
	public void testPoints() throws IOException {
		List<String> target = Files.lines(Paths.get(resultPath)).collect(Collectors.toList());
		List<String> test = waypointFinder.getWaypoints().stream().map(Point::toString).collect(Collectors.toList());
		assertEquals(target.size(), test.size());
		assertTrue(test.containsAll(target));
	}
}
