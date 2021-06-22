package com.gastromanager.socket;


import com.gastromanager.models.*;
import com.gastromanager.models.xml.Choice;
import com.gastromanager.models.xml.Item;
import com.gastromanager.models.xml.Option;
import com.gastromanager.util.DbUtil;
import com.gastromanager.util.SaxParserForGastromanager;
import com.gastromanager.util.XmlUtil;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server2 {

    ServerSocket serverSocket = null;
    Socket socket = null;
    Integer serverSocketPort = 5000;
    static MenuDetail menuDetail;

    public Server2() {
        try {
            //Connection to client
            serverSocket = new ServerSocket(serverSocketPort);

            while (true) {
                socket = null;
                try {
                    socket = serverSocket.accept();
                    Thread t = new ClientHandler(socket);
                    t.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Server2(String test) {

    }

    private void sendMenuDetail(ObjectOutputStream out) {
        try {
           /* String xmlContent = XmlUtil.readFileToString(
                    "C:\\Users\\Admin\\IdeaProjects\\GastroManager\\JDesktopJava\\data\\sample_tempalte.xml",
                    Charset.defaultCharset());
            SaxParserForGastromanager parser = SaxParserForGastromanager.getInstance();
            menuDetail = parser.parseXml(xmlContent);*/
            menuDetail = loadMenu();
            System.out.println("Drill down menu types available count " +menuDetail.getDrillDownMenus().getDrillDownMenuTypes().size());
            out.writeObject(menuDetail);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private String createXmlItemInfo(SelectedOrderItem item, Double totalPrice) {
        String xmlContent = null;
        Menu menu = menuDetail.getMenu();
        Map<String, DrillDownMenuItemDetail> menuMap = menu.getItemMap();
        DrillDownMenuItemDetail menuItemDetail = menuMap.get(item.getTarget());
        Map<String, Double> optionPriceMap = new HashMap<>();
        Map<String, Double> choicePriceMap = new HashMap<>();
        Item xmlMainItem = createItem(item, menuItemDetail, totalPrice, optionPriceMap, choicePriceMap);
        StringWriter sw = new StringWriter();
        JAXB.marshal(xmlMainItem, sw);
        String xmlString = sw.toString();
        System.out.println("xml "+xmlString);
        return xmlString;

    }

    private OrderItem buildOrderItemEntry(SelectedOrderItem selectedOrderItem) throws Exception {
        Double orderItemPrice = new Double(0);
        selectedOrderItem.setPrice(orderItemPrice);
        String xmlItemInfo = createXmlItemInfo(selectedOrderItem, orderItemPrice);
        //Order
        Order order = new Order();
        order.setId(Integer.valueOf(selectedOrderItem.getOrderId()));
        order.setAmount(selectedOrderItem.getPrice()); //TODO need to be sum
        order.setDateTime(LocalDateTime.now());
        order.setFloorId(selectedOrderItem.getFloorId());
        order.setTableId(selectedOrderItem.getTableId());
        order.setHumanReadableId(selectedOrderItem.getOrderId());
        order.setDateTime(LocalDateTime.now());


        //Orderitem
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(Integer.valueOf(selectedOrderItem.getOrderId()));
        orderItem.setQuantity(1);
        orderItem.setPrice(selectedOrderItem.getPrice());
        orderItem.setItemId(0);
        orderItem.setRemark("");
        orderItem.setDateTime(LocalDateTime.now());
        orderItem.setPrintStatus(0);
        orderItem.setPayed(0);
        orderItem.setXmlText(xmlItemInfo);
        orderItem.setStatus(0);
        orderItem.setOrder(order);

        return orderItem;
    }

    private Item createItem(SelectedOrderItem item, DrillDownMenuItemDetail itemDetail,  Double totalPrice,
                            Map<String, Double> optionPriceMap, Map<String, Double> choicePriceMap) {
        Item xmlMainItem = null;
        if(itemDetail != null) {
            Map<String, DrillDownMenuItemOptionDetail> itemOptionDetailMap = itemDetail.getOptionsMap();
            xmlMainItem = new Item();
            xmlMainItem.setName(item.getItemName());

            SelectedOrderItemOption selectedOrderItemOption = item.getOption();
            Option option = new Option();
            option.setId(selectedOrderItemOption.getId());
            option.setName(selectedOrderItemOption.getName());
            DrillDownMenuItemOptionDetail optionDetail = itemOptionDetailMap.get(selectedOrderItemOption.getName());
            if (optionDetail != null) {
                totalPrice = new Double(totalPrice + optionDetail.getPrice());
                item.setPrice(Double.valueOf((item.getPrice() == null ? Double.valueOf(0) : item.getPrice() )+ optionDetail.getPrice()));
                option.setPrice(optionDetail.getPrice().toString());
                if(optionDetail.getOverwritePrice()) {
                    optionPriceMap.put(option.getId(), item.getPrice());
                } else {
                    optionPriceMap.put(option.getId(), Double.valueOf((optionPriceMap.get(option.getId()) == null ? Double.valueOf(0) : optionPriceMap.get(option.getId() )
                            + optionDetail.getPrice())));
                }
                DrillDownMenuItemOptionChoiceDetail choiceDetail = optionDetail.getChoice();

                if (choiceDetail != null) {
                    Choice choice = new Choice();
                    choice.setName(choiceDetail.getName());
                    choice.setPrice(choiceDetail.getPrice().toString());
                    totalPrice = new Double(totalPrice + choiceDetail.getPrice() - optionDetail.getPrice());
                    item.setPrice(Double.valueOf(item.getPrice() + choiceDetail.getPrice() - optionDetail.getPrice()));
                    option.setChoice(choice);
                    choicePriceMap.put(choiceDetail.getName(), choiceDetail.getPrice());
                }
            }
            //sub items
            if(item.getSubItems() != null) {
                List<Item> xmlSubItems = new ArrayList<>();
                for (SelectedOrderItem subItem : item.getSubItems()) {
                    Item item1 = createItem(subItem, itemDetail.getSubItems().stream().filter(menuSubItem ->
                            menuSubItem.getMenuItemName().equals(subItem.getItemName())
                    ).findAny().get(), totalPrice, optionPriceMap, choicePriceMap);
                    xmlSubItems.add(item1);
                    //totalPrice = totalPrice + totalPrice;
                    item.setPrice(Double.valueOf(item.getPrice()+ (subItem.getPrice() == null ? Double.valueOf(0):subItem.getPrice())));
                }
                xmlMainItem.setItem(xmlSubItems);
            }

            xmlMainItem.setOption(option);

        }

        return xmlMainItem;
    }

    private MenuDetail  loadMenu() {
        try {
            String xmlContent = XmlUtil.readFileToString(
                    "C:\\Users\\Admin\\IdeaProjects\\GastroManager\\JDesktopJava\\data\\sample_tempalte.xml",
                    Charset.defaultCharset());
            SaxParserForGastromanager parser = SaxParserForGastromanager.getInstance();
            menuDetail = parser.parseXml(xmlContent);
            System.out.println("Drill down menu types available count " +menuDetail.getDrillDownMenus().getDrillDownMenuTypes().size());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return menuDetail;
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void main(String[] args) {

        Server2 server = new Server2();

        //below code is only for test
        /*Server2 server = new Server2("test");
        MenuDetail menuDetail = server.loadMenu();
        SelectedOrderItem orderItem = new SelectedOrderItem();
        orderItem.setItemName("Pizza");
        SelectedOrderItemOption itemOption = new SelectedOrderItemOption();
        itemOption.setId("size");
        itemOption.setName("small");
        SelectedOrderItem subItem = new SelectedOrderItem();
        subItem.setItemName("topping");
        SelectedOrderItemOption subItemOption = new SelectedOrderItemOption();
        subItemOption.setId("toppingammount");
        subItemOption.setName("lots");
        subItem.setOption(subItemOption);
        orderItem.setOption(itemOption);
        List<SelectedOrderItem> subItems = new ArrayList<>();
        subItems.add(subItem);
        orderItem.setSubItems(subItems);
        orderItem.setOrderId("2");
        orderItem.setFloorId("1");
        orderItem.setStaffId("1");


        //Item currItem = server.createItem(orderItem, menuDetail.getMenu().getItemMap());
        //server.createXmlItemInfo(orderItem, new Double(0));
        try {
            OrderItem orderItem1 = server.buildOrderItemEntry(orderItem);
            Integer noOfRowsInserted = DbUtil.insertOrder(orderItem1);
            System.out.println((noOfRowsInserted ==1) ? "Order Inserted": " Order not inserted");
        } catch (Exception e) {
            e.printStackTrace();
        }
*/

    }

    class ClientHandler extends Thread {
        final Socket s;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        public ClientHandler(Socket s) {
            this.s = s;
        }

        @Override
        public void run() {
            System.out.println("Server Run");
            try {
                oos = new ObjectOutputStream(this.s.getOutputStream());
                ois = new ObjectInputStream(this.s.getInputStream());

                Object clientMessage = ois.readObject();
                if(clientMessage instanceof SelectedOrderItem) {
                    SelectedOrderItem orderItem = (SelectedOrderItem) clientMessage;
                    OrderItem dbOrderItem = buildOrderItemEntry(orderItem);
                    Integer noOfRowsInserted = DbUtil.insertOrder(dbOrderItem);
                    System.out.println((noOfRowsInserted ==1) ? "Order Inserted": " Order not inserted");

                } else if(clientMessage instanceof String) {
                    String request = (String) clientMessage;
                    System.out.println("Received request for " + request);

                    if (isNumeric(request)) { //order details id 1
                        //send result to client
                        List<OrderItem> orderItems = DbUtil.getOrderDetails(request);
                        System.out.println(orderItems);
                        ArrayList<String> orderDetails = new ArrayList<>();
                        for(OrderItem orderItem:orderItems) {
                            /*orderDetails.add(orderItem.getItemId() +" "
                                    + orderItem.getQuantity() +"\n");*/
                            orderDetails.add(XmlUtil.formatOrderText(orderItem));
                        }

                        oos.writeObject(orderDetails);
                    } else {
                        if (request.equals("menu")) { //menu
                            sendMenuDetail(oos);
                        } else if(request.equals("newOrderId")) {
                            System.out.println("Next order id "+DbUtil.getNewOrderId());
                            oos.writeObject(DbUtil.getNewOrderId());
                        }
                    }

                    if (request.equals("Exit")) {
                        System.out.println("Client " + this.s + " sends exit...");
                        System.out.println("Closing this connection.");
                        this.s.close();
                        System.out.println("Connection closed");
                    }
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    oos.close();
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
