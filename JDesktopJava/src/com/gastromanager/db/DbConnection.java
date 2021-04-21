package com.gastromanager.db;

import com.gastromanager.util.PropertiesUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static DbConnection dbConnection = null;
    public Connection gastroDbConnection;

    private DbConnection() {
        try {
            String url = "jdbc:sqlite:"+ System.getProperty("user.dir")+ "\\"
                    + PropertiesUtil.getPropertyValue("dbFolder")+"\\"
                    + PropertiesUtil.getPropertyValue("dbName");
            gastroDbConnection = DriverManager.getConnection(url);
            System.out.println("Connection to Gastro Database has been established.");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static DbConnection getDbConnection() {
        if(null == dbConnection) {
            dbConnection = new DbConnection();
        }

        return dbConnection;
    }
}
