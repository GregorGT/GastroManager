package com.gastromanager.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    public static String getPropertyValue(String propertyKey) {
        String value = null;
        try {
            InputStream is =  new FileInputStream(System.getProperty("user.dir")+"\\resources\\gastromanager.properties");
            //InputStream is = PropertiesUtil.class.getClassLoader().getResourceAsStream("gastromanager.properties");
            Properties properties = new Properties();
            properties.load(is);
            value = properties.getProperty(propertyKey);
            System.out.println(value);
            System.out.println(System.getProperty("user.dir")+"\\resources\\gastromanager.properties");
            } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static void main(String[] args) {
        PropertiesUtil.getPropertyValue("dbFolder");
    }

}
