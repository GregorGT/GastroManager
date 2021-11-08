/*Copyright 2021 GastroRice

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/


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
