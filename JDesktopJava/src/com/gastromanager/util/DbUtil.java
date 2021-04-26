package com.gastromanager.util;

import com.gastromanager.db.DbConnection;
import com.gastromanager.models.OrderInfo;
import com.gastromanager.models.OrderItem;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
            PreparedStatement stmt=connection.prepareStatement("select * from orderitem where order_id=?");
            stmt.setInt(1,Integer.parseInt(orderId));
            ResultSet result = stmt.executeQuery();
            //System.out.println(stmt.executeQuery().findColumn("quantity"));
            orderItems = loadResults(result);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            orderItems = null;
        }

        return orderItems;
    }

    public static OrderInfo getOrderInfo(String orderId) {
        OrderInfo orderInfo = null;
        String query = "SELECT O.HUMANREADABLE_ID, O.STAFF_ID, O.DATETIME," +
                "L.FLOOR_ID, L.TABLE_ID FROM ORDERS O, LOCATION L\n" +
                "WHERE O.ID = ?\n" +
                "AND L.ID = O.LOCATION_ID";
        try {
            Connection connection = DbConnection.getDbConnection().gastroDbConnection;
            PreparedStatement stmt=connection.prepareStatement(query);
            stmt.setInt(1,Integer.parseInt(orderId));
            ResultSet result = stmt.executeQuery();
            //System.out.println(stmt.executeQuery().findColumn("quantity"));
            if(result.next()) {
                orderInfo = new OrderInfo();
                orderInfo.setHumanReadableId(result.getString("HUMANREADABLE_ID"));
                orderInfo.setStaffId(result.getString("STAFF_ID"));
                DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-DD HH:mm:ss");
                DateTime dt = formatter.parseDateTime(result.getString("DATETIME"));
                orderInfo.setTimestamp(dt.toString(formatter));
                orderInfo.setFloorId(result.getString("FLOOR_ID"));
                orderInfo.setTableId(result.getString("TABLE_ID"));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();

        }
        return orderInfo;
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
                    orderItem.setPrice(result.getDouble("price"));
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
