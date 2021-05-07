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
            orderItems = loadResults(result);
            stmt.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            orderItems = null;
        }

        return orderItems;
    }

    public static OrderInfo getOrderInfo(String orderId) {
        OrderInfo orderInfo = null;
        String query = "SELECT O.DATETIME, O.HUMANREADABLE_ID, O.STAFF_ID, L.FLOOR_ID, L.TABLE_ID, S.LASTNAME, S.FIRSTNAME, F.NAME AS FLOOR_NAME, T.NAME AS TABLE_NAME \n" +
                "FROM ORDERS O, LOCATION L, STAFF S, FLOOR F, TABLEDETAILS T\n" +
                "WHERE O.ID = ?\n" +
                "AND L.ID = O.LOCATION_ID\n" +
                "AND O.STAFF_ID = S.ID\n" +
                "AND L.FLOOR_ID = F.ID\n" +
                "AND L.TABLE_ID = T.ID";
        try {
            Connection connection = DbConnection.getDbConnection().gastroDbConnection;
            PreparedStatement stmt=connection.prepareStatement(query);
            stmt.setInt(1,Integer.parseInt(orderId));
            ResultSet result = stmt.executeQuery();
            if(result.next()) {
                orderInfo = new OrderInfo();
                orderInfo.setHumanReadableId(result.getString("HUMANREADABLE_ID"));
                orderInfo.setStaffId(result.getString("STAFF_ID"));
                DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-DD HH:mm:ss");
                DateTime dt = formatter.parseDateTime(result.getString("DATETIME"));
                orderInfo.setTimestamp(dt.toString(formatter));
                orderInfo.setFloorId(result.getString("FLOOR_ID"));
                orderInfo.setTableId(result.getString("TABLE_ID"));
                orderInfo.setFloorName((result.getString("FLOOR_NAME") != null ? result.getString("FLOOR_NAME"): ""));
                orderInfo.setStaffName((result.getString("LASTNAME") != null ?
                        result.getString("LASTNAME"): "") + "," +
                        (result.getString("FIRSTNAME") != null ? result.getString("FIRSTNAME"): ""));
                orderInfo.setTableName((result.getString("TABLE_NAME") != null ? result.getString("TABLE_NAME"): ""));
            }
            stmt.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();

        }
        return orderInfo;
    }

    public static List<String> loadOrderItemXmlInfo(String orderId) {
        List<String> orderItemXmlInfo = null;
        try {
            String query = "SELECT XML FROM ORDERITEM WHERE ORDER_ID= ?";
            Connection connection = DbConnection.getDbConnection().gastroDbConnection;
            PreparedStatement stmt=connection.prepareStatement(query);
            stmt.setInt(1,Integer.parseInt(orderId));
            ResultSet result = stmt.executeQuery();
            while(result.next()) {
                if(null== orderItemXmlInfo) {
                    orderItemXmlInfo = new ArrayList<>();
                }
                orderItemXmlInfo.add(result.getString("XML"));
            }
            stmt.close();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return orderItemXmlInfo;
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
                    orderItem.setQuantity(result.getInt("quantity"));
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
        //List<OrderItem> orderItems = DbUtil.getOrderDetails("1");
        //System.out.println(orderItems.get(0).getXml().getElementsByTagName("item").getLength());
        //DbUtil.getOrderInfo("1");
        System.out.println(DbUtil.loadOrderItemXmlInfo("1"));
    }
}
