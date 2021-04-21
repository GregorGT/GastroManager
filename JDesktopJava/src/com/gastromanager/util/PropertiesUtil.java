package com.gastromanager.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    public static Properties properties;

    public static String getPropertyValue(String propertyKey) {
        String value = null;
        try {
            if(properties == null) {
                InputStream is = new FileInputStream(System.getProperty("user.dir") + "\\resources\\gastromanager.properties");
                //InputStream is = PropertiesUtil.class.getClassLoader().getResourceAsStream("gastromanager.properties");
                properties = new Properties();
                properties.load(is);
            }
            value = properties.getProperty(propertyKey);
            //System.out.println(value);
            } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static void main(String[] args) {
        PropertiesUtil.getPropertyValue("dbFolder");
    }

}
