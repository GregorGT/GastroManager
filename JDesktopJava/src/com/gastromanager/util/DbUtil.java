package com.gastromanager.util;

import com.gastromanager.db.DbConnection;
import com.gastromanager.models.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;


import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DbUtil {

    public static DateTimeFormatter DATE_TME_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
    public static List<OrderItem> getOrderDetails(OrderDetailQuery orderDetailQuery, Boolean queryForPrint) {
    	List<OrderItem> orderItems = null;
        try {
            Connection connection = DbConnection.getDbConnection().gastroDbConnection;
            Integer locationId = getLocationId(orderDetailQuery.getFloorId(), orderDetailQuery.getTableId());
            if(locationId != null) {
                Integer orderId = getOrderId(orderDetailQuery, locationId);
                if(orderId != null) {
                    StringBuilder queryBuilder = new StringBuilder();
                    queryBuilder.append("SELECT * from ORDERITEM WHERE" +
                                " ORDER_ID = '"+ orderId +"'" +
                                 " AND DATE(DATETIME) = DATE('NOW', 'LOCALTIME')");
                    if(queryForPrint) {
                        queryBuilder.append(" AND PRINT_STATUS = 0");
                    }
                    Statement stmt=connection.createStatement();

                    ResultSet result = stmt.executeQuery(queryBuilder.toString());

                    orderItems = loadResults(result);
                    if (orderItems == null || orderItems.size() == 0) {
                        System.out.println("No order items found for orderId " + orderId + " today");
                    }
                    stmt.close();
                } else {
                    System.out.println("Order not found for humanreadableId: "+orderDetailQuery.getHumanreadableId() +" location: "+locationId);
                }
            } else {
                System.out.println("Location not found for floor: "+orderDetailQuery.getFloorId() +" and table: "+orderDetailQuery.getTableId());
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            orderItems = null;
        }

        return orderItems;
    }

    public static List<OrderItem> getOrderDetails(String orderId, Boolean queryForPrint) {
        List<OrderItem> orderItems = null;
        try {
            Connection connection = DbConnection.getDbConnection().gastroDbConnection;
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT * from ORDERITEM WHERE" +
                    " ORDER_ID = '"+ orderId +"'" +
                    " AND DATE(DATETIME) = DATE('NOW', 'LOCALTIME')");
            if(queryForPrint) {
                queryBuilder.append(" AND PRINT_STATUS = 0");
            }

            PreparedStatement stmt=connection.prepareStatement(queryBuilder.toString());
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

    public static Boolean updatePrintedOrderItems(OrderDetailQuery orderDetailQuery, Boolean queryForPrint) {
        Boolean printStatusUpdated = false;
        try {
            Connection connection = DbConnection.getDbConnection().gastroDbConnection;
            Integer locationId = getLocationId(orderDetailQuery.getFloorId(), orderDetailQuery.getTableId());
            if(locationId != null) {
                Integer orderId = getOrderId(orderDetailQuery, locationId);
                if(orderId != null) {
                    StringBuilder queryBuilder = new StringBuilder();

                    queryBuilder.append("UPDATE ORDERITEM SET PRINT_STATUS = 1 WHERE" + " ORDER_ID = '")
                            .append(orderId)
                            .append("'")
                            .append(" AND DATE(DATETIME) = DATE('NOW', 'LOCALTIME')");
                    if(queryForPrint) {
                        queryBuilder.append(" AND PRINT_STATUS = 0");
                    }
                    Statement stmt=connection.createStatement();
                    Integer rowsUpdated = stmt.executeUpdate(queryBuilder.toString());

                    if(rowsUpdated > 0) {
                        printStatusUpdated = true;
                    }
                    stmt.close();
                } else {
                    System.out.println("Order not found for humanreadableId: "+orderDetailQuery.getHumanreadableId() +" location: "+locationId);
                }
            } else {
                System.out.println("Location not found for floor: "+orderDetailQuery.getFloorId() +" and table: "+orderDetailQuery.getTableId());
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return printStatusUpdated;
    }

    public static OrderInfo getOrderInfo(String orderId) {
        OrderInfo orderInfo = null;
        String query = "SELECT O.DATETIME, O.HUMANREADABLE_ID, O.STAFF_ID, L.FLOOR_ID, L.TABLE_ID, S.LASTNAME, S.FIRSTNAME, F.NAME AS FLOOR_NAME, T.NAME AS TABLE_NAME \n" +
                "FROM ORDERS O, LOCATION L, STAFF S, FLOOR F, TABLEDETAILS T\n" +
                "WHERE O.HUMANREADABLE_ID = ?\n" +
                "AND DATE(O.DATETIME) = DATE('NOW','LOCALTIME')\n" +
                "AND L.ID = O.LOCATION_ID\n" +
                //"AND O.STAFF_ID = S.ID\n" +
                "AND L.FLOOR_ID = F.ID\n" +
                "AND L.TABLE_ID = T.TABLE_ID";
        try {
            Connection connection = DbConnection.getDbConnection().gastroDbConnection;
            PreparedStatement stmt=connection.prepareStatement(query);
            stmt.setInt(1,Integer.parseInt(orderId));
            ResultSet result = stmt.executeQuery();
            if(result.next()) {
                orderInfo = new OrderInfo();
                orderInfo.setHumanReadableId(result.getString("HUMANREADABLE_ID"));
                orderInfo.setStaffId(result.getString("STAFF_ID"));

                org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-DD HH:mm:ss");
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

    public static Integer checkAndAddOrderId(OrderItem orderItem) {
        //String query = "select * from orders where id = '"+orderItem.getOrderId() +"'";
        //String query = "select * from orders where id = '"+orderId +"'";
        //Connection connection = DbConnection.getDbConnection().gastroDbConnection;
        Order order = orderItem.getOrder();
        Integer orderId = getOrderId(order.getHumanReadableId(), order.getFloorId(), order.getTableId());
        if (orderId == null) {
            orderId  = getNewOrderId();
            try {
                Connection connection = DbConnection.getDbConnection().gastroDbConnection;
                Statement statement = connection.createStatement();

                String insertOrder = " INSERT INTO orders (\n" +
                        "                       id,\n" +
                        "                       amount,\n" +
                        "                       menu_id,\n" +
                        "                       staff_id,\n" +
                        "                       location_id,\n" +
                        "                       transaction_id,\n" +
                        "                       member_id,\n" +
                        "                       humanreadable_id,\n" +
                        "                       datetime\n" +
                        "                   )\n" +
                        "                   VALUES (\n" +
                        "                       '" + orderId + "',\n" +
                        "                       '" + orderItem.getPrice() + "',\n" +
                        "                       '" + orderItem.getOrder().getMenuId() + "',\n" +
                        "                       '" + orderItem.getOrder().getStaffId() + "',\n" +
                        "                       '" + getLocationId(order.getFloorId(),
                        order.getTableId()) + "',\n" +
                        "                       '" + order.getTransactionId() + "',\n" +
                        "                       '" + order.getMemberId() + "',\n" +
                        "                       '" + order.getHumanReadableId() + "',\n" +
                        "                       '" + translateToSqlDate(order.getDateTime()) + "');";
                System.out.println("insert into order " + insertOrder);
                statement.executeUpdate(insertOrder);

                //insert into reservation
                String insertReservation = "INSERT INTO reservations (\n" +
                        "                             id,\n" +
                        "                             capacity,\n" +
                        "                             created_by,\n" +
                        "                             start_date,\n" +
                        "                             floor_id,\n" +
                        "                             table_id,\n" +
                        "                             end_date\n" +
                        "                         )\n" +
                        "                         VALUES (\n" +
                        "                             '" + getNewReservationId() + "',\n" +
                        "                             '0',\n" +
                        "                             'created_by',\n" +
                        "                             '" + translateToSqlDate(LocalDateTime.now()) + "',\n" +
                        "                             '" + order.getFloorId() + "',\n" +
                        "                             '" + order.getTableId() + "',\n" +
                        "                             '" + translateToSqlDate(LocalDateTime.now().plusHours(1)) + "')";
                System.out.println("insert into reservation " + insertReservation);
                statement.executeUpdate(insertReservation);

            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return orderId;
    }

    private static String translateToSqlDate(LocalDateTime localDateTime) {
        return localDateTime.format(DATE_TME_FORMATTER);
    }

    private static Integer getLocationId(String floorId, String tableId) {
        Integer locationId  = null;
        if(floorId != null && tableId != null) {
            String query = "select id from location where floor_id='" + floorId + "' and table_id='" + tableId + "'";
            try {
                Connection connection = DbConnection.getDbConnection().gastroDbConnection;
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    locationId = resultSet.getInt("id");
                    break;
                }
                statement.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return locationId;
    }

    private static Integer getOrderId(OrderDetailQuery orderDetailQuery, Integer locationId) {
        Integer orderId  = null;
        if(locationId != null && orderDetailQuery != null) {
            Connection connection = DbConnection.getDbConnection().gastroDbConnection;
            try {
                PreparedStatement stmt=connection.prepareStatement(
                        "SELECT ID from ORDERS WHERE \n" +
                                "HUMANREADABLE_ID = ?\n" +
                                " AND LOCATION_ID = ?" +
                                " AND DATE(DATETIME) = DATE('NOW', 'LOCALTIME')");
                stmt.setInt(1,Integer.parseInt(orderDetailQuery.getHumanreadableId()));
                stmt.setInt(2,locationId);
                ResultSet resultOrder = stmt.executeQuery();
                while(resultOrder.next()) {
                    orderId = resultOrder.getInt("ID");
                    break;
                }
                stmt.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }

        }
        return orderId;
    }

    private static Integer getOrderId(String humanReadableId, String floorId, String tableId) {
        Integer orderId  = null;
        Integer locationId = getLocationId(floorId, tableId);
        if(locationId != null && humanReadableId != null) {
            Connection connection = DbConnection.getDbConnection().gastroDbConnection;
            try {
                PreparedStatement stmt=connection.prepareStatement(
                        "SELECT ID from ORDERS WHERE \n" +
                                "HUMANREADABLE_ID = ?\n" +
                                " AND LOCATION_ID = ?" +
                                " AND DATE(DATETIME) = DATE('NOW', 'LOCALTIME')");
                stmt.setInt(1,Integer.parseInt(humanReadableId));
                stmt.setInt(2,locationId);
                ResultSet resultOrder = stmt.executeQuery();
                while(resultOrder.next()) {
                    orderId = resultOrder.getInt("ID");
                    break;
                }
                stmt.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }

        }
        return orderId;
    }

    public static Integer insertOrder(OrderItem orderItem) {
        Integer noOfRowsInserted = 0;
        try {

            Integer orderId = checkAndAddOrderId(orderItem);
            Integer newOrderItemId = getNewOrderItemId();
            System.out.println("new order item id "+newOrderItemId);
            String insertOrderQuery = "INSERT INTO orderitem (\n" +
                    "                          order_id,\n" +
                    "                          item_id,\n" +
                    "                          quantity,\n" +
                    "                          remark,\n" +
                    "                          xml,\n" +
                    "                          price,\n" +
                    "                          print_status,\n" +
                    "                          payed,\n" +
                    "                          datetime,\n" +
                    "                          status,\n" +
                    "                          id,\n" +
                    "                          transaction_id,\n" +
                    "                          price_one_unit\n" +
                    "                      )\n" +
                    "                      VALUES (\n" +
                    "                          '"+ orderId+"',\n" +
                    "                          '"+ orderItem.getItemId()+"',\n" +
                    "                          '1',\n" +
                    "                          'remark',\n" +
                    "                          '"+ orderItem.getXmlText()+"',\n" +
                    "                          '"+ orderItem.getPrice()+"',\n" +
                    "                          '"+ orderItem.getPrintStatus()+"',\n" +
                    "                          '"+ orderItem.getPayed()+"',\n" +
                    "                          '"+ translateToSqlDate(orderItem.getDateTime())+"',\n" +
                    "                          '"+ orderItem.getStatus()+"',\n" +
                    "                          '"+newOrderItemId+"',\n" +
                    "                          'null',\n" +
                    "                          '"+ orderItem.getPrice()/orderItem.getQuantity()+"'\n" +
                    "                      )";
            System.out.println("Executing \n "+insertOrderQuery);
            Connection connection = DbConnection.getDbConnection().gastroDbConnection;
            Statement statement = connection.createStatement();
            noOfRowsInserted = statement.executeUpdate(insertOrderQuery);
            statement.close();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return noOfRowsInserted;
    }

    private static List<OrderItem> loadResults(ResultSet result) {
            List<OrderItem> orderItems = null;
            try {
                while(result.next()) {
                    if(null == orderItems) {
                        orderItems = new ArrayList<>();
                    }
                    OrderItem orderItem = new OrderItem();
                    orderItem.setItemId(result.getLong("item_id"));
                    orderItem.setRemark(result.getString("remark"));
                    orderItem.setXml(XmlUtil.loadXMLFromString(result.getString("xml")));
                    orderItem.setXmlText(result.getString("xml"));
                    orderItem.setPrice(result.getDouble("price"));
                    orderItem.setQuantity(result.getInt("quantity"));
                    orderItem.setOrderId(result.getInt("order_id"));
                    orderItem.setPrintStatus(result.getInt("print_status"));

                    orderItem.setDateTime(LocalDateTime.parse(result.getString("datetime"),
                            DATE_TME_FORMATTER));
                    orderItems.add(orderItem);
                }
                if (orderItems == null) {
                    orderItems = new ArrayList<>();
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        return orderItems;
    }

    public static Boolean removeOrderItem(OrderItemInfo orderItemInfo) {
        Boolean isItemRemoved = false;
        try {
            String query = "DELETE FROM ORDERITEM" +
                    " WHERE ORDER_ID='"+orderItemInfo.getOrderId()+"'"+
                    " AND ITEM_ID ='"+orderItemInfo.getItemId()+"'" +
                    " AND DATETIME = '"+translateToSqlDate(orderItemInfo.getDateTime())+"'"+
                    " AND DATE(DATETIME) = DATE('NOW', 'LOCALTIME')";
            System.out.println(query);
            Connection connection = DbConnection.getDbConnection().gastroDbConnection;
            PreparedStatement stmt=connection.prepareStatement(query);
            Integer rowsDeleted = stmt.executeUpdate();
            if(rowsDeleted > 0) {
                isItemRemoved = true;
            }
            stmt.close();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return isItemRemoved;
    }

    public static Integer getNewHumanReadableOrderId() {
        Integer nextOrderId = null;
        try {
            String query = "SELECT HUMANREADABLE_ID AS MAX_ID, MAX(DATETIME) FROM ORDERS "
                    + "WHERE  DATE(DATETIME)  = DATE('NOW', 'LOCALTIME') "
                    +"ORDER BY DATETIME DESC";

            Connection connection = DbConnection.getDbConnection().gastroDbConnection;
            PreparedStatement stmt=connection.prepareStatement(query);
            ResultSet result = stmt.executeQuery();
            if(result.next()) {
                nextOrderId = result.getInt("MAX_ID") + 1;
            } else {
                nextOrderId = 1;
            }
            stmt.close();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return nextOrderId;
    }

    public static Integer getStartingHumanReadableOrderId(Integer floorId, Integer tableId) {
        Integer nextOrderId = null;
        try {
            String query = "SELECT MAX(O.HUMANREADABLE_ID) AS ID FROM ORDERS O, LOCATION L \n" +
                    "WHERE L.FLOOR_ID = ?\n" +
                    "AND L.TABLE_ID = ?\n" +
                    "AND L.ID = O.LOCATION_ID\n" +
                    "AND DATE(O.DATETIME) = DATE('NOW', 'LOCALTIME')";

            Connection connection = DbConnection.getDbConnection().gastroDbConnection;
            PreparedStatement stmt=connection.prepareStatement(query);
            stmt.setInt(1,floorId);
            stmt.setInt(2,tableId);
            ResultSet result = stmt.executeQuery();
            if(result.next()) {
                nextOrderId = result.getInt("ID");
            } else {
                nextOrderId = 1;
            }
            stmt.close();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return nextOrderId;
    }

    public static Integer getNewReservationId() {
        Integer nextReservationId = null;
        try {
            String query = "SELECT MAX(ID) AS MAX_ID FROM RESERVATIONS";
            Connection connection = DbConnection.getDbConnection().gastroDbConnection;
            PreparedStatement stmt=connection.prepareStatement(query);
            ResultSet result = stmt.executeQuery();
            if(result.next()) {
                nextReservationId = result.getInt("MAX_ID") + 1;
            } else {
                nextReservationId = 1;
            }
            stmt.close();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return nextReservationId;
    }

    public static Integer getNewOrderId() {
        Integer nextOrderId = null;
        try {
            String query = "SELECT MAX(ID) AS MAX_ID FROM ORDERS";
            Connection connection = DbConnection.getDbConnection().gastroDbConnection;
            PreparedStatement stmt=connection.prepareStatement(query);
            ResultSet result = stmt.executeQuery();
            if(result.next()) {
                nextOrderId = result.getInt("MAX_ID") + 1;
            } else {
                nextOrderId = 1;
            }
            stmt.close();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return nextOrderId;
    }

    public static Integer getNewOrderItemId() {
        Integer nextOrderItemId = null;
        try {
            String query = "SELECT MAX(ID) AS MAX_ID FROM ORDERITEM";
            Connection connection = DbConnection.getDbConnection().gastroDbConnection;
            PreparedStatement stmt=connection.prepareStatement(query);
            ResultSet result = stmt.executeQuery();
            if(result.next()) {
                nextOrderItemId = result.getInt("MAX_ID") + 1;
            } else {
                nextOrderItemId = 1;
            }
            stmt.close();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return nextOrderItemId;
    }

    public static void main(String[] args) {
        //List<OrderItem> orderItems = DbUtil.getOrderDetails("1");
        //System.out.println(orderItems.get(0).getXml().getElementsByTagName("item").getLength());
        //DbUtil.getOrderInfo("1");
        System.out.println(DbUtil.loadOrderItemXmlInfo("1"));
    }
}
