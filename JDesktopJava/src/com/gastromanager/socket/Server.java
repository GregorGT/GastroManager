package com.gastromanager.socket;

import com.gastromanager.util.XmlUtil;
import org.w3c.dom.Document;

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
            String xmlContent = in.readUTF();
            Document doc = XmlUtil.loadXMLFromString(xmlContent);
            System.out.println(doc.getElementsByTagName("item").getLength());
            doc = null;
            xmlContent = null;
            socket.close();
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Server server = new Server(5001);
    }
}
