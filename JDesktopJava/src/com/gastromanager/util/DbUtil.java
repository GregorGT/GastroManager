package com.gastromanager.util;

import com.gastromanager.db.DbConnection;
import com.gastromanager.models.OrderItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbUtil {
    public static List<OrderItem> getOrderDetails(String orderId) {
        List<OrderItem> orderItems = null;
        try {
            Connection connection = DbConnection.getDbConnection().gastroDbConnection;
            System.out.println("Connection to SQLite has been established.");
            PreparedStatement stmt=connection.prepareStatement("select * from orderitem where order_id=?");
            stmt.setInt(1,Integer.parseInt(orderId));
            ResultSet result = stmt.executeQuery();
            System.out.println(stmt.executeQuery().findColumn("quantity"));
            orderItems = loadResults(result);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            orderItems = null;
        }

        return orderItems;
    }

    private static List<OrderItem> loadResults(ResultSet result) {
            List<OrderItem> orderItems = null;
            try {
                while(result.next()) {
                    if(null == orderItems) {
                        orderItems = new ArrayList<>();
                    }
                    OrderItem orderItem = new OrderItem();
                    orderItem.setItemId(result.getInt("item_id"));
                    orderItem.setRemark(result.getString("remark"));
                    orderItem.setXml(XmlUtil.loadXMLFromString(result.getString("xml")));
                    orderItems.add(orderItem);
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        return orderItems;
    }

    public static void main(String[] args) {
        List<OrderItem> orderItems = DbUtil.getOrderDetails("1");
        System.out.println(orderItems.get(0).getXml().getElementsByTagName("item").getLength());
    }
}
