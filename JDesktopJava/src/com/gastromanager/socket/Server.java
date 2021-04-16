package com.gastromanager.socket;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    ServerSocket serverSocket = null;
    Socket socket  = null;
    DataInputStream in  = null;

    public Server(int port) {
        try {
            serverSocket =  new ServerSocket(port);
            socket = serverSocket.accept();

            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            String line = "";

            while(!line.equals("Done")) {
                line  = in.readUTF();
                System.out.println(line);

            }

            socket.close();
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        Server server = new Server(5001);

    }
}
