package tools;

import calculator.App;
import logginig.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class Config {
	private static final Logger logger = Logger.getLogger(App.class);

	private static final String path = "app.properties";
	private static final Properties properties;

	static {
		properties = new Properties();
		try {
			properties.load(App.class.getClassLoader().getResourceAsStream(path));
		} catch (IOException e) {
			logger.info("Unable to load configuration file: " + new File(path).getAbsolutePath());
			logger.info(e);
			System.exit(1);
		}
	}

	public static String getString(Parameter key){
		return properties.getProperty(key.getKey(), key.getDefaultValue());
	}

	public static Boolean getBoolean(Parameter key){
		return Boolean.valueOf(getString(key));
	}

}
