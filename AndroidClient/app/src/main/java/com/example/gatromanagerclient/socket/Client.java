package com.example.gatromanagerclient.socket;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

    Socket socket  = null;
    DataOutputStream out = null;
    DataInputStream in = null;
    Integer serverPort = 5000;
    static Client client = null;

    public Client() {
        try {
            //send to server
            socket = new Socket("192.168.1.7", serverPort);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Client getInstance() {
        if(client == null) {
            client = new Client();
        }
        return client;
    }

    public void close() {
        try {
            socket.close();
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public String getResponse(String request) {
        String response = null;

        try {
            sendTextData(request, out); //request could be orderId / menu
            response = in.readUTF();
            //System.out.println("Client received order from Server: "+response);
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }


    public void sendTextData(String xmlContent, DataOutputStream out) {
        try {
            //System.out.println(xmlContent);
            out.writeUTF(xmlContent);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {

        Client client = Client.getInstance();
        client.getResponse("1");

    }
}