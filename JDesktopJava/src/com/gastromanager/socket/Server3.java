package com.gastromanager.socket;


import com.gastromanager.models.*;
import com.gastromanager.service.MenuService;
import com.gastromanager.service.OrderService;
import com.gastromanager.service.PaymentService;
import com.gastromanager.service.impl.MenuServiceImpl;
import com.gastromanager.service.impl.OrderServiceImpl;
import com.gastromanager.service.impl.PaymentServiceImpl;
import com.gastromanager.util.DbUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server3 extends Thread{

    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private MenuDetail menuDetail;
    private Boolean isRunning = false;
    private Integer serverSocketPort;
    private OrderService orderService;
    private PaymentService paymentService;
    private MenuService menuService;


    public Server3(Integer serverSocketPort) {

        this.serverSocketPort = serverSocketPort;
        orderService = new OrderServiceImpl();
        paymentService = new PaymentServiceImpl();
        menuService = new MenuServiceImpl();

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
            menuDetail = menuService.loadMenu();
            menuService.loadQuickMenuMap(menuDetail);
            System.out.println("Drill down menu types available count " +menuDetail.getDrillDownMenus().getDrillDownMenuTypes().size());
            out.writeObject(menuDetail);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                    orderService.addOrderItem((SelectedOrderItem) clientMessage);

                } else if(clientMessage instanceof OrderItemInfo) {
                    orderService.removeOrderItem((OrderItemInfo) clientMessage);

                } else if(clientMessage instanceof OrderDetailQuery) {
                    //send result to client
                    oos.writeObject(orderService.retrieveOrderItems((OrderDetailQuery) clientMessage));

                } else if(clientMessage instanceof SignOffOrderInfo) {
                    //send result to client
                    oos.writeObject(orderService.signOffOrder((SignOffOrderInfo) clientMessage));

                } else if(clientMessage instanceof HumanReadableIdQuery) {
                    //send result to client
                    HumanReadableIdQuery humanReadableIdQuery = (HumanReadableIdQuery) clientMessage;
                    oos.writeObject(DbUtil.getStartingHumanReadableOrderId(humanReadableIdQuery.getFloorId(),
                            humanReadableIdQuery.getTableId()));

                } else if(clientMessage instanceof OrderListQuery) {
                    //send result to client
                    OrderListQuery orderListQuery = (OrderListQuery) clientMessage;
                    oos.writeObject(paymentService.retrieveOrders(orderListQuery));
                    paymentService = null;

                } else if(clientMessage instanceof OrderItemTransactionInfo) {
                    //send result to client
                    OrderItemTransactionInfo orderItemTransactionInfo = (OrderItemTransactionInfo) clientMessage;
                    oos.writeObject(paymentService.processTransactionInfo(orderItemTransactionInfo));
                    paymentService = null;

                } else if(clientMessage instanceof String) {
                    String request = (String) clientMessage;
                    System.out.println("Received request for " + request);

                    if (request.equals("menu")) { //menu
                        sendMenuDetail(oos);
                        orderService.setMenuDetail(menuDetail);
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
