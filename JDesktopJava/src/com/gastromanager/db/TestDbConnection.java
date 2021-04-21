package com.gastromanager.db;

import java.sql.*;

public class TestDbConnection {

    public static void connect() {
        Connection connection;
        try {
            connection = DbConnection.getDbConnection().gastroDbConnection;//DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
            PreparedStatement stmt=connection.prepareStatement("select * from orderitem where order_id=?");
            stmt.setInt(1,1);//1 specifies the first parameter in the query
            System.out.println(stmt.executeQuery().findColumn("quantity"));
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TestDbConnection.connect();
    }
}
