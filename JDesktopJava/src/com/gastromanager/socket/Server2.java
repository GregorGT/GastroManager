package com.gastromanager.socket;


import com.gastromanager.models.MenuDetail;
import com.gastromanager.models.OrderItem;
import com.gastromanager.util.DbUtil;
import com.gastromanager.util.SaxParserForGastromanager;
import com.gastromanager.util.XmlUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Server2 {

    ServerSocket serverSocket = null;
    Socket socket = null;
    Integer serverSocketPort = 5000;

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

    private void sendMenuDetail(ObjectOutputStream out) {
        try {
            String xmlContent = XmlUtil.readFileToString(
                    "C:\\Users\\Admin\\IdeaProjects\\GastroManager\\JDesktopJava\\data\\sample_tempalte.xml",
                    Charset.defaultCharset());
            SaxParserForGastromanager parser = SaxParserForGastromanager.getInstance();
            MenuDetail menuDetail = parser.parseXml(xmlContent);
            System.out.println("Drill down menu types available count " +menuDetail.getDrillDownMenus().getDrillDownMenuTypes().size());
            out.writeObject(menuDetail);
        } catch (IOException e) {
            e.printStackTrace();
        }


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
                if(clientMessage instanceof OrderItem) {
                    OrderItem orderItem = (OrderItem) clientMessage;

                } else if(clientMessage instanceof String) {
                    String request = (String) clientMessage;
                    System.out.println("Received request for " + request);

                    if (isNumeric(request)) { //order details id 1
                        //send result to client
                        List<OrderItem> orderItems = DbUtil.getOrderDetails(request);
                        System.out.println(orderItems);
                        ArrayList<String> orderDetails = new ArrayList<>();
                        for(OrderItem orderItem:orderItems) {
                            orderDetails.add(orderItem.getItemId() +" "
                                    + orderItem.getQuantity() +"\n");
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
