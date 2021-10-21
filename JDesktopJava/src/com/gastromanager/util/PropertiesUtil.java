package com.gastromanager.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    public static Properties properties;
    private static String OS = null;

    public static String getPropertyValue(String propertyKey) {
    	String slash = "";
    	if (!isWindows()) {
    		slash = "/";
    	} else {
    		slash = "\\";
    	}
    	
        String value = null;
        try {
            if(properties == null) {
                InputStream is = new FileInputStream(System.getProperty("user.dir") + slash + "resources"+slash+"gastromanager.properties");
                //InputStream is = PropertiesUtil.class.getClassLoader().getResourceAsStream("gastromanager.properties");
                properties = new Properties();
                properties.load(is);
            }
            value = properties.getProperty(propertyKey);
            } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }
    
    public static void setPropertyValue(String propertyKey, String newPropertyValue) {
    	String slash = "";
    	if (!isWindows()) {
    		slash = "/";
    	} else {
    		slash = "\\";
    	}
        String value = null;
        try {
            if(properties == null) {
                InputStream is = new FileInputStream(System.getProperty("user.dir") + slash + "resources"+slash+"gastromanager.properties");
                properties = new Properties();
                properties.load(is);
            }

            properties.setProperty(propertyKey, newPropertyValue);
            } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void setAndSavePropertyValue(String propertyKey, String newPropertyValue) {
    	setPropertyValue(propertyKey, newPropertyValue);
    	try {
			properties.store(new FileOutputStream("resources/gastromanager.properties"), null);
		} catch (IOException e) {
			e.printStackTrace();
		} 
    }
    
    
    
    public static String getOsName()
    {
       if(OS == null) { OS = System.getProperty("os.name"); }
       return OS;
    }
    public static boolean isWindows()
    {
       return getOsName().startsWith("Windows");
    }

    public static void main(String[] args) {
        PropertiesUtil.getPropertyValue("dbFolder");
    }

}
