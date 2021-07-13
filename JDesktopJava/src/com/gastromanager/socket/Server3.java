package com.gastromanager.socket;


import com.gastromanager.models.*;
import com.gastromanager.models.xml.Choice;
import com.gastromanager.models.xml.Item;
import com.gastromanager.models.xml.Option;
import com.gastromanager.print.PrintService;
import com.gastromanager.print.PrintServiceImpl;
import com.gastromanager.service.PaymentService;
import com.gastromanager.service.impl.PaymentServiceImpl;
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
import java.util.*;

public class Server3 extends Thread{

    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private MenuDetail menuDetail;
    private Boolean isRunning = false;
    private Integer serverSocketPort;


    public Server3(Integer serverSocketPort) {
        this.serverSocketPort = serverSocketPort;
    }

    public void startServer()
    {
        try
        {
            serverSocket = new ServerSocket(serverSocketPort);
            this.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Boolean isServerRunning() {
        return isRunning;
    }

    public void stopServer() {
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverSocket = null;
        this.interrupt();
    }

    @Override
    public void run() {
        isRunning = true;
        while(isRunning) {
            socket = null;
            try {
                if(isRunning) {
                    socket = serverSocket.accept();
                }
                Thread t = new ClientHandler(socket);
                t.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /*public Server3(String test) {

    }*/

    private void sendMenuDetail(ObjectOutputStream out) {
        try {
            menuDetail = loadMenu();
            loadQuickMenuMap(menuDetail);
            System.out.println("Drill down menu types available count " +menuDetail.getDrillDownMenus().getDrillDownMenuTypes().size());
            out.writeObject(menuDetail);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private String createXmlItemInfo(SelectedOrderItem item, OrderPrice orderPrice, OrderItem orderItem) {
        String xmlContent = null;
        Menu menu = menuDetail.getMenu();
        Map<String, DrillDownMenuItemDetail> menuMap = menu.getItemMap();
        DrillDownMenuItemDetail menuItemDetail = menuMap.get(item.getTarget());
        Map<String, Double> optionPriceMap = new HashMap<>();
        Map<String, Double> choicePriceMap = new HashMap<>();
        Item xmlMainItem = createItem(item, menuItemDetail, orderPrice, optionPriceMap, choicePriceMap, orderItem);
        StringWriter sw = new StringWriter();
        JAXB.marshal(xmlMainItem, sw);
        String xmlString = sw.toString();
        System.out.println("xml "+xmlString);
        return xmlString;

    }

    private OrderItem buildOrderItemEntry(SelectedOrderItem selectedOrderItem) throws Exception {
        OrderPrice orderPrice = new OrderPrice((double) 0, false);
        OrderItem orderItem = new OrderItem();
        String xmlItemInfo = createXmlItemInfo(selectedOrderItem, orderPrice, orderItem);
        System.out.println("Price choice: "+orderPrice.getChoicePrice() +
                "price: "+orderPrice.getPrice());
        selectedOrderItem.setPrice(
                (orderPrice.getPrice() != null ? orderPrice.getPrice() : (double) 0) +
                        (orderPrice.getChoicePrice() != null ? orderPrice.getChoicePrice() : (double) 0));
        //Order
        Order order = new Order();
        order.setId(Integer.valueOf(selectedOrderItem.getOrderId()));
        //order.setAmount(selectedOrderItem.getPrice()); //TODO need to be sum
        order.setAmount(selectedOrderItem.getPrice());
        order.setDateTime(LocalDateTime.now());
        order.setFloorId(selectedOrderItem.getFloorId());
        order.setTableId(selectedOrderItem.getTableId());
        order.setHumanReadableId(selectedOrderItem.getOrderId());
        order.setDateTime(LocalDateTime.now());


        //Orderitem
        //OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(Integer.valueOf(selectedOrderItem.getOrderId()));
        orderItem.setQuantity(1);
        //orderItem.setPrice(selectedOrderItem.getPrice());
        orderItem.setPrice(selectedOrderItem.getPrice());
        //orderItem.setItemId(0);
        orderItem.setRemark("");
        orderItem.setDateTime(LocalDateTime.now());
        orderItem.setPrintStatus(0);
        orderItem.setPayed(0);
        orderItem.setXmlText(xmlItemInfo);
        orderItem.setStatus(0);
        orderItem.setOrder(order);

        return orderItem;
    }

    private Item createItem(SelectedOrderItem item, DrillDownMenuItemDetail itemDetail,  OrderPrice orderPrice,
                            Map<String, Double> optionPriceMap, Map<String, Double> choicePriceMap, OrderItem orderItem) {
        Item xmlMainItem = null;
        if(itemDetail != null) {
            Map<String, DrillDownMenuItemOptionDetail> itemOptionDetailMap = itemDetail.getOptionsMap();
            xmlMainItem = new Item();
            xmlMainItem.setName(item.getItemName());
            if(itemDetail.getPrice() != null) {
                xmlMainItem.setPrice(itemDetail.getPrice().toString());
            }
            DrillDownMenuItemOptionDetail optionDetail = null;
            SelectedOrderItemOption selectedOrderItemOption = item.getOption();
            if(selectedOrderItemOption != null) {
                Option option = new Option();
                option.setId(selectedOrderItemOption.getId());
                option.setName(selectedOrderItemOption.getName());
                optionDetail = (itemOptionDetailMap == null) ? null : itemOptionDetailMap.get(selectedOrderItemOption.getName());
                if (optionDetail != null) {
                    if (optionDetail.getOverwritePrice()) {
                        option.setOverwritePrice(optionDetail.getPrice().toString());
                        optionPriceMap.put(option.getId(), item.getPrice());
                        orderPrice.setPrice(optionDetail.getPrice());
                        System.out.println(" Price set to in overwrite "+orderPrice.getPrice() );
                        orderPrice.setOverwritePrice(true);
                    } else {
                        option.setPrice(optionDetail.getPrice().toString());
                        //If already added we need to update by subtracting the earlier price
                        Double currentOptionPrice = optionPriceMap.get(option.getId()+option.getName());
                        System.out.println("Existing price "+currentOptionPrice);
                        Double newPrice = (double) 0;
                        if(currentOptionPrice == null) {
                            newPrice = optionDetail.getPrice();
                        } else {
                            newPrice = optionDetail.getPrice() - currentOptionPrice;
                        }
                        //update
                        optionPriceMap.put(option.getId()+option.getName(), optionDetail.getPrice());

                        if(!orderPrice.getOverwritePrice()) {
                            orderPrice.setPrice(orderPrice.getPrice() + newPrice);
                            System.out.println(" Price set to  "+orderPrice.getPrice() );
                        }
                    }
                    DrillDownMenuItemOptionChoiceDetail choiceDetail = optionDetail.getChoice();

                    if (choiceDetail != null) {
                        Choice choice = new Choice();
                        choice.setName(choiceDetail.getName());
                        choice.setPrice(choiceDetail.getPrice().toString());
                        orderPrice.setChoicePrice(choiceDetail.getPrice()); //choiceDetail.getPrice() - optionDetail.getPrice()
                        System.out.println(" Price set to in choice "+orderPrice.getChoicePrice() + " optionPrice "+optionDetail.getPrice() );
                        //item.setPrice(Double.valueOf(item.getPrice() + choiceDetail.getPrice() - optionDetail.getPrice()));
                        option.setChoice(choice);
                        choicePriceMap.put(choiceDetail.getName(), choiceDetail.getPrice());
                    }
                }
                xmlMainItem.setOption(option);
            }
            //sub items
            if(item.getSubItems() != null) {
                List<Item> xmlSubItems = new ArrayList<>();
                for (SelectedOrderItem subItem : item.getSubItems()) {
                    Item item1 = createItem(subItem, itemDetail.getSubItems().stream().filter(menuSubItem ->
                            menuSubItem.getMenuItemName().equals(subItem.getItemName())
                            //menuSubItem.getUuid().equals(subItem.getTarget())
                    ).findAny().get(), orderPrice, optionPriceMap, choicePriceMap, orderItem);
                    xmlSubItems.add(item1);
                    //totalPrice = totalPrice + totalPrice;
                    //item.setPrice(Double.valueOf(item.getPrice()+ (subItem.getPrice() == null ? Double.valueOf(0):subItem.getPrice())));
                }
                xmlMainItem.setItem(xmlSubItems);
            } else {
                if(optionDetail != null) {
                    orderItem.setItemId(optionDetail.getUuid() == null ? null : Long.valueOf(optionDetail.getUuid()));
                } else {
                    System.out.println(" orderItem "+itemDetail.getMenuItemName()+ " price " +orderPrice.getPrice());
                    if(orderPrice.getPrice() != null && orderPrice.getPrice() == 0.0 && itemDetail.getPrice() != null) {
                        System.out.println("price set from item "+itemDetail.getPrice());
                        orderPrice.setPrice(orderPrice.getPrice() + itemDetail.getPrice());
                    }
                    orderItem.setItemId(itemDetail.getUuid() == null ? null : Long.valueOf(itemDetail.getUuid()));
                }

            }

        }

        return xmlMainItem;
    }

    private MenuDetail loadMenu() {
        try {
            String xmlContent = XmlUtil.readFileToString(
                    "C:\\Users\\Admin\\AndroidStudioProjects\\GastroManager\\JDesktopJava\\data\\sample_tempalte.xml",
                    Charset.defaultCharset());
            SaxParserForGastromanager parser = SaxParserForGastromanager.getInstance();
            menuDetail = parser.parseXml(xmlContent);
            System.out.println("Drill down menu types available count " +menuDetail.getDrillDownMenus().getDrillDownMenuTypes().size());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return menuDetail;
    }

    private void loadQuickMenuMap(MenuDetail menuDetail) {
        Menu menu = menuDetail.getMenu();
        Stack<DrillDownMenuItemDetail> drillDownMenuItemDetailStack = null;
        for(Map.Entry<String, DrillDownMenuItemDetail> orderItemEntry : menu.getMainItemMap().entrySet()) {
            DrillDownMenuItemDetail parent = orderItemEntry.getValue();
            System.out.println("item name "+parent.getMenuItemName());
            buildQuickMenuItem(parent, drillDownMenuItemDetailStack, menu);
        }
    }

    private void buildQuickMenuItem(DrillDownMenuItemDetail parent,
                                    Stack<DrillDownMenuItemDetail> drillDownMenuItemDetailStack, Menu menu) {
        for(DrillDownMenuItemDetail subItem : parent.getSubItems()) {
            System.out.println("subitem "+subItem.getMenuItemName());
            if((subItem.getSubItems() == null || subItem.getSubItems().size() == 0) &&
                    subItem.getOptionsMap() != null) {
                addQuickMenuEntry(subItem, parent, menu, drillDownMenuItemDetailStack);
            } else {
                if(subItem.getOptionsMap() != null || subItem.getSubItems() != null) {
                    if (drillDownMenuItemDetailStack == null) {
                        drillDownMenuItemDetailStack = new Stack<>();
                    }
                    drillDownMenuItemDetailStack.push(parent);
                    drillDownMenuItemDetailStack.push(subItem);
                    buildQuickMenuItem(subItem, drillDownMenuItemDetailStack, menu);
                } else { //no subitem and options
                    buildQuickMenuEntry(subItem, parent, menu, drillDownMenuItemDetailStack);
                }
            }
        }
    }

    private void buildQuickMenuEntry(DrillDownMenuItemDetail subItem, DrillDownMenuItemDetail parent,  Menu menu,
                                     Stack<DrillDownMenuItemDetail> drillDownMenuItemDetailStack) {
        if(drillDownMenuItemDetailStack == null) {
            //main Item
            SelectedOrderItem selectedOrderItem = buildParentChildOrderSelection(subItem, parent, null);
            menu.getQuickMenuIdRefMap().put(subItem.getMenuId(), selectedOrderItem);
            System.out.println("added entry for " + subItem.getMenuItemName() + "menu id = " + subItem.getMenuId());
        } else {
            Stack<DrillDownMenuItemDetail> localDrillDownMenuItemStack =
                    (Stack<DrillDownMenuItemDetail>) drillDownMenuItemDetailStack.clone();
            SelectedOrderItem parentItem = null;
            Boolean isNestedItem = false;
            SelectedOrderItem previousParent = null;
            while (!localDrillDownMenuItemStack.empty()) {
                DrillDownMenuItemDetail currentParentMenuItem = localDrillDownMenuItemStack.pop();
                parentItem = buildParentChildOrderSelection(subItem, currentParentMenuItem, parentItem);
                /*if(isNestedItem) {
                    parentItem = buildParentChildOrderSelection(previousParent, currentParentMenuItem);
                } else {
                    parentItem = buildParentChildOrderSelection(subItem, currentParentMenuItem);
                }
                previousParent = parentItem;
                isNestedItem = true;*/
            }
            menu.getQuickMenuIdRefMap().put(subItem.getMenuId(), parentItem);
            System.out.println("added entry for " + subItem.getMenuItemName() + "menu id = " + subItem.getMenuId());

        }
    }

    private SelectedOrderItem buildParentChildOrderSelection(DrillDownMenuItemDetail subItem, DrillDownMenuItemDetail parent,
                                                             SelectedOrderItem child) {
        SelectedOrderItem selectedOrderItem = new SelectedOrderItem();
        selectedOrderItem.setItemName(parent.getMenuItemName());
        selectedOrderItem.setTarget(parent.getUuid());
        selectedOrderItem.setPrice(subItem.getPrice());
        SelectedOrderItem selectedOrderSubItem = null;
        if(child != null) {
            selectedOrderSubItem = child;
        } else {
            selectedOrderSubItem = new SelectedOrderItem();
            selectedOrderSubItem.setItemName(subItem.getMenuItemName());
            selectedOrderSubItem.setTarget(subItem.getUuid());

        }
        List<SelectedOrderItem> selectedOrderItemList = new ArrayList<>();
        selectedOrderItemList.add(selectedOrderSubItem);
        selectedOrderItem.setSubItems(selectedOrderItemList);

        return selectedOrderItem;
    }



    private void addQuickMenuEntry(DrillDownMenuItemDetail subItem, DrillDownMenuItemDetail parent,  Menu menu,
                                   Stack<DrillDownMenuItemDetail> drillDownMenuItemDetailStack) {

        for (Map.Entry<String, DrillDownMenuItemOptionDetail> orderItemOptionEntry :
                subItem.getOptionsMap().entrySet()) {
            DrillDownMenuItemOptionDetail optionDetail = orderItemOptionEntry.getValue();
            Stack<DrillDownMenuItemDetail> localDrillDownMenuItemStack = (drillDownMenuItemDetailStack != null ?
                    (Stack<DrillDownMenuItemDetail>) drillDownMenuItemDetailStack.clone(): null);

            if (optionDetail.getMenuId() != null) {
                //main Item
                SelectedOrderItem selectedOrderItem = new SelectedOrderItem();
                selectedOrderItem.setItemName(parent.getMenuItemName());
                selectedOrderItem.setTarget(parent.getUuid());
                selectedOrderItem.setPrice(optionDetail.getPrice());
                //add option to parent
                DrillDownMenuItemOptionDetail menuItemOptionDetail = (parent.getOptionsMap() != null) ? parent.getOptionsMap().get(optionDetail.getName())
                        : null;
                if(menuItemOptionDetail != null) {
                    SelectedOrderItemOption selectedOrderItemOption = new SelectedOrderItemOption();
                    selectedOrderItemOption.setName(menuItemOptionDetail.getName());
                    selectedOrderItemOption.setId(menuItemOptionDetail.getId());
                    selectedOrderItem.setOption(selectedOrderItemOption);
                }

                //sub item
                SelectedOrderItem selectedOrderSubItem = new SelectedOrderItem();
                selectedOrderSubItem.setItemName(subItem.getMenuItemName());
                selectedOrderSubItem.setTarget(subItem.getUuid());

                SelectedOrderItemOption selectedOrderItemOption = new SelectedOrderItemOption();
                selectedOrderItemOption.setName(optionDetail.getName());
                selectedOrderItemOption.setId(optionDetail.getId());
                //selectedOrderItem.setOption(selectedOrderItemOption);
                selectedOrderSubItem.setOption(selectedOrderItemOption);
                List<SelectedOrderItem> subItems = new ArrayList<>();
                subItems.add(selectedOrderSubItem);
                selectedOrderItem.setSubItems(subItems);
                if (menu.getQuickMenuIdRefMap() == null) {
                    menu.setQuickMenuIdRefMap(new HashMap<>());
                }
                if(localDrillDownMenuItemStack != null) {
                    while (!localDrillDownMenuItemStack.empty()) {
                        DrillDownMenuItemDetail drillDownMenuItemDetail = localDrillDownMenuItemStack.pop();
                        Map<String, SelectedOrderItemOption> optionsMap = null;
                        Map<String, DrillDownMenuItemOptionDetail> menuItemOptionDetailMap = drillDownMenuItemDetail.getOptionsMap();
                        if(menuItemOptionDetailMap != null) {
                            for (Map.Entry<String, DrillDownMenuItemOptionDetail> optionDetailEntry : menuItemOptionDetailMap.entrySet()) {
                                DrillDownMenuItemOptionDetail drillDownMenuItemOptionDetail = optionDetailEntry.getValue();
                                if (!drillDownMenuItemOptionDetail.getId().equals(selectedOrderItemOption.getId())) {
                                    SelectedOrderItemOption currItemOption = new SelectedOrderItemOption();
                                    currItemOption.setId(drillDownMenuItemOptionDetail.getId());
                                    currItemOption.setName(drillDownMenuItemOptionDetail.getName());
                                    if (optionsMap == null) {
                                        optionsMap = new HashMap<>();
                                    }
                                    optionsMap.put(currItemOption.getName(), currItemOption);
                                } else {
                                    break;
                                }
                            }
                        }
                        if(drillDownMenuItemDetail.getMenuItemName().equals(parent.getMenuItemName())) {
                            selectedOrderItem.setAllOptions(optionsMap);
                        } else {
                            SelectedOrderItem currParentSelectedOrderItem = new SelectedOrderItem();
                            currParentSelectedOrderItem.setItemName(drillDownMenuItemDetail.getMenuItemName());
                            currParentSelectedOrderItem.setTarget(drillDownMenuItemDetail.getUuid());
                            //currParentSelectedOrderItem.setAllOptions(optionsMap);
                            currParentSelectedOrderItem.setOption(selectedOrderItemOption);
                            List<SelectedOrderItem> currSubItems = new ArrayList<>();
                            currSubItems.add(selectedOrderItem);
                            currParentSelectedOrderItem.setSubItems(currSubItems);
                            selectedOrderItem = currParentSelectedOrderItem;
                        }
                    }
                }
                menu.getQuickMenuIdRefMap().put(optionDetail.getMenuId(), selectedOrderItem);
                System.out.println("added entry for " + optionDetail.getMenuId());
            }
        }
    }

    private SelectedOrderItem buildMenuIdSelectionItem(DrillDownMenuItemDetail subItem, DrillDownMenuItemDetail parent,
                                                       DrillDownMenuItemOptionDetail optionDetail) {
        //main Item
        SelectedOrderItem selectedOrderItem = new SelectedOrderItem();
        selectedOrderItem.setItemName(parent.getMenuItemName());
        selectedOrderItem.setTarget(parent.getUuid());
        selectedOrderItem.setPrice(optionDetail.getPrice());

        //sub item
        SelectedOrderItem selectedOrderSubItem = new SelectedOrderItem();
        selectedOrderSubItem.setItemName(subItem.getMenuItemName());
        selectedOrderSubItem.setTarget(subItem.getUuid());

        SelectedOrderItemOption selectedOrderItemOption = new SelectedOrderItemOption();
        selectedOrderItemOption.setName(optionDetail.getName());
        selectedOrderItemOption.setId(optionDetail.getId());
        //selectedOrderItem.setOption(selectedOrderItemOption);
        selectedOrderSubItem.setOption(selectedOrderItemOption);
        List<SelectedOrderItem> subItems = new ArrayList<>();
        subItems.add(selectedOrderSubItem);
        selectedOrderItem.setSubItems(subItems);

        return selectedOrderItem;
    }



    public static void main(String[] args) {

        Server3 server = new Server3(5000);

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

                } else if(clientMessage instanceof OrderItemInfo) {
                    OrderItemInfo orderItem = (OrderItemInfo) clientMessage;
                    Boolean isItemDeleted = DbUtil.removeOrderItem(orderItem);

                } else if(clientMessage instanceof OrderDetailQuery) {
                    //send result to client
                    List<OrderItem> orderItems = DbUtil.getOrderDetails((OrderDetailQuery) clientMessage, false);
                    ArrayList<OrderItemInfo> orderItemArrayList =  null;
                    if(orderItems != null) {
                        orderItemArrayList = new ArrayList<>();
                        for(OrderItem orderItem: orderItems) {
                            OrderItemInfo orderItemInfo = new OrderItemInfo(orderItem);
                            orderItemArrayList.add(orderItemInfo);
                        }

                        System.out.println("order items count "+orderItemArrayList.size());
                        oos.writeObject(orderItemArrayList);

                    } else {
                        System.out.println("No order items found");
                    }

                } else if(clientMessage instanceof SignOffOrderInfo) {
                    //send result to client
                    SignOffOrderInfo signOffOrderInfo = (SignOffOrderInfo) clientMessage;
                    PrintService printService = new PrintServiceImpl();
                    Boolean isPrinted = printService.print(signOffOrderInfo.getOrderDetailQuery());
                    if(isPrinted) {

                    }
                    oos.writeObject(isPrinted);

                } else if(clientMessage instanceof HumanReadableIdQuery) {
                    //send result to client
                    HumanReadableIdQuery humanReadableIdQuery = (HumanReadableIdQuery) clientMessage;
                    oos.writeObject(DbUtil.getStartingHumanReadableOrderId(humanReadableIdQuery.getFloorId(),
                            humanReadableIdQuery.getTableId()));

                } else if(clientMessage instanceof OrderListQuery) {
                    //send result to client
                    OrderListQuery orderListQuery = (OrderListQuery) clientMessage;
                    PaymentService paymentService = new PaymentServiceImpl();
                    oos.writeObject(paymentService.retrieveOrders(orderListQuery));
                    paymentService = null;

                } else if(clientMessage instanceof OrderItemTransactionInfo) {
                    //send result to client
                    OrderItemTransactionInfo orderItemTransactionInfo = (OrderItemTransactionInfo) clientMessage;
                    PaymentService paymentService = new PaymentServiceImpl();
                    oos.writeObject(paymentService.processTransactionInfo(orderItemTransactionInfo));
                    paymentService = null;

                } else if(clientMessage instanceof String) {
                    String request = (String) clientMessage;
                    System.out.println("Received request for " + request);

                    if (request.equals("menu")) { //menu
                        sendMenuDetail(oos);
                    } else if(request.equals("newOrderId")) {
                        System.out.println("Next order id "+DbUtil.getNewHumanReadableOrderId());
                        oos.writeObject(DbUtil.getNewHumanReadableOrderId());
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
