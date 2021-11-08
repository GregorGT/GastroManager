/*Copyright 2021 GastroRice

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/


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
