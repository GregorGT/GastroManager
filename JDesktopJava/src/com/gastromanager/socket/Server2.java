package com.gastromanager.socket;


import com.gastromanager.models.MenuDetail;
import com.gastromanager.models.OrderDetailsView;
import com.gastromanager.print.PrintServiceImpl;
import com.gastromanager.util.SaxParserForGastromanager;
import com.gastromanager.util.XmlUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

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

    public void sendXMLData(DataOutputStream out) {
        try {
            //C:\Users\Admin\IdeaProjects\GastroManager\JDesktopJava\data\sample_tempalte.xml
            String xmlContent = XmlUtil.readFileToString(
                    "C:\\Users\\Admin\\IdeaProjects\\GastroManager\\JDesktopJava\\data\\sample_tempalte.xml", Charset.defaultCharset());
            System.out.println(xmlContent);
            out.writeUTF(xmlContent);

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
                //if(ois.available() > 0) {
                    String request = (String) ois.readObject();
                    System.out.println("Received request for " + request);

                    if (isNumeric(request)) { //order details id 1
                        //send result to client
                        String response = new PrintServiceImpl().getPrintInfo(request);
                        OrderDetailsView orderDetailsView = new OrderDetailsView();
                        orderDetailsView.setOrderDetailsView(response);
                        oos.writeObject(orderDetailsView);
                    } else {
                        if (request.equals("menu")) { //menu
                            //sendXMLData(dos);
                            sendMenuDetail(oos);
                        }
                    }

                    if (request.equals("Exit")) {
                        System.out.println("Client " + this.s + " sends exit...");
                        System.out.println("Closing this connection.");
                        this.s.close();
                        System.out.println("Connection closed");
                    }
                //}

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
