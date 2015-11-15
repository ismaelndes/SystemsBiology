package org.sbfc.converter.sbml2dot;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class Context {

	public static String PROPERTIES_FILE_NAME = "sbml2graph.properties";
	
	public static Properties properties = new Properties();
	public static Properties defaultProperties = new Properties();

	public static final String SUFFIX = "suffix";
	public static final String CLONING = "cloning";
	
	
	static {
		defaultProperties.setProperty(SUFFIX, "");
		defaultProperties.setProperty(CLONING, "false");
		
	}
	
	
	public static void loadProperties() {
		
		// Putting the default values
		properties.putAll(defaultProperties);
		
		String propertiesFilePath = PROPERTIES_FILE_NAME;
		
		
		try {
			properties.load(Context.class.getResourceAsStream(propertiesFilePath));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public static void loadProperties(String fileName) {
		properties.putAll(defaultProperties);
		
		try {
			properties.load(new FileInputStream(fileName));
		} catch (Exception e) {
		}
	}
	
	public static void storeProperties() {
		try {
			properties.store(new FileOutputStream(PROPERTIES_FILE_NAME), "SBML2Graph Properties file.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getProperty(String propertyName) {
		String propertyValue = properties.getProperty(propertyName);
		
		System.out.println("Context : getProperty(" + propertyName + ") = " + propertyValue);
		
		return propertyValue;
	}
	
	public static boolean getPropertyAsBoolean(String propertyName) {
		String propertyValue = properties.getProperty(propertyName);
		boolean propertyValueBoolean = false;
		
		System.out.println("Context : getPropertyAsBoolean(" + propertyName + ") = " + propertyValue);
		
		if (propertyValue != null && propertyValue.equals("true")) {
			propertyValueBoolean = true;
		}
		
		return propertyValueBoolean;
	}
	
	
	public static void setProperty(String propertyName, String propertyValue) {
		
		System.out.println("Context : setProperty(" + propertyName + ") = " + propertyValue);
		
		properties.setProperty(propertyName, propertyValue);
	}
	
	public static void setPropertyAsBoolean(String propertyName, boolean propertyValue) {
		
		System.out.println("Context : setProperty(" + propertyName + ") = " + propertyValue);
		
		properties.setProperty(propertyName, Boolean.toString(propertyValue));
	}
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		loadProperties();
		System.out.println(properties.toString());
	}

}
