package com.gastromanager.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDbConnection {

    public static void connect() {
        Connection connection;
        try {
            String url = "jdbc:sqlite:D:\\Program Files\\sqllite\\sqlite-tools-win32-x86-3350400\\testDB.db";
            connection = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
            Statement statement = connection.createStatement();
            String query = "select * from test";
            System.out.println(statement.execute(query));
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TestDbConnection.connect();
    }
}
