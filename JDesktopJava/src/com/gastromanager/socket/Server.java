package com.gastromanager.socket;


import com.gastromanager.print.PrintServiceImpl;
import com.gastromanager.util.XmlUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {

    ServerSocket serverSocket = null;
    Socket socket = null;
    DataInputStream in = null;
    DataOutputStream out = null;
    Integer serverSocketPort = 5000;
    public Server() {
        try {
            //Connection to client
            serverSocket = new ServerSocket(serverSocketPort);


            while(true) {
                socket = null;
                try{
                    socket = serverSocket.accept();
                    in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                    out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                    Thread t = new ClientHandler(socket, in, out);
                    t.start();
                } catch (Exception e) {
                    socket.close();
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendXMLData(DataOutputStream out) {
        try {
            String xmlContent = XmlUtil.readFileToString(
                    "C:\\Users\\Admin\\IdeaProjects\\GastroManager\\DesktopJava\\data\\sample_tempalte.xml", Charset.defaultCharset());
            System.out.println(xmlContent);
            out.writeUTF(xmlContent);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
    }
    // ClientHandler class
    class ClientHandler extends Thread {
        final DataInputStream dis;
        final DataOutputStream dos;
        final Socket s;

        public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
            this.s = s;
            this.dis = dis;
            this.dos = dos;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    //String orderId = in.readUTF();
                    String request = in.readUTF().trim();
                    System.out.println("Received request for " + request);

                    if (isNumeric(request)) {
                        while (null != request) {
                            //send result to client
                            //out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                            //out.writeUTF(DbUtil.loadOrderItemXmlInfo(orderId).toString());
                            out.writeUTF(new PrintServiceImpl().getPrintInfo(request));
                            out.close();
                        }
                    } else {
                        if (request.equals("menu")) {
                            sendXMLData(out);
                            out.close();
                        }
                    }

                    if (request.equals("Exit")) {
                        System.out.println("Client " + this.s + " sends exit...");
                        System.out.println("Closing this connection.");
                        this.s.close();
                        System.out.println("Connection closed");
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                // closing resources
                this.dis.close();
                this.dos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
