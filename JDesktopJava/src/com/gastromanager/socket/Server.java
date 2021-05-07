package com.gastromanager.socket;


import com.gastromanager.util.DbUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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
            socket = serverSocket.accept();

            while(true) {
                //Read data from client
                in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                String orderId = in.readUTF();
                System.out.println("Received request for "+ orderId);

                while(null != orderId) {
                    //send result to client
                    out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                    out.writeUTF(DbUtil.loadOrderItemXmlInfo(orderId).toString());
                    out.close();
                }
                //socket.close();
                in.close();
                //serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Server server = new Server();
    }
}
