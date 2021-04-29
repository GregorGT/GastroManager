package com.gastromanager.db;

import com.gastromanager.util.PropertiesUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static DbConnection dbConnection = null;
    public Connection gastroDbConnection;
    private static String OS = null;
    
    private DbConnection(boolean linux) {
    	
    	String slash = "";
    	if (linux==true) {
    		slash = "/";
    	} else {
    		slash = "\\";
    	}
		try {
            String url = "jdbc:sqlite:"+ System.getProperty("user.dir")+ slash
                    + PropertiesUtil.getPropertyValue("dbFolder")+slash
                    + PropertiesUtil.getPropertyValue("dbName");
            gastroDbConnection = DriverManager.getConnection(url);
            System.out.println("Connection to Gastro Database has been established.");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
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

    public static DbConnection getDbConnection() {
    	 
    	if(null == dbConnection) {
    	     dbConnection = new DbConnection(!isWindows());
    	 }
    
        return dbConnection;
    }

}
