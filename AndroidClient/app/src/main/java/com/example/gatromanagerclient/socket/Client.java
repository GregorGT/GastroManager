package com.example.gatromanagerclient.socket;

import com.gastromanager.models.MenuDetail;
import com.gastromanager.models.OrderDetailsView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

    Socket socket  = null;
    ObjectOutputStream out = null;
    ObjectInputStream in = null;
    Integer serverPort = 5000;
    static Client client = null;

    public Client() {
        try {
            //send to server
            socket = new Socket("192.168.1.7", serverPort);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

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
            //in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            Object responseFromServer = in.readObject();
            if(responseFromServer instanceof OrderDetailsView) {
                response =  ((OrderDetailsView) responseFromServer).getOrderDetailsView();
            }
            //System.out.println("Client received order from Server: "+response);
            //close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return response;
    }

    public MenuDetail getMenuDetails(String request) {
        Object response = null;
        MenuDetail menuDetail = null;

        try {
            sendTextData(request, out); //request could be orderId / menu
            //in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            //if(in.available() > 0){
                response = in.readObject();
                menuDetail = (response == null) ? null : (MenuDetail) response;
            //}
            //System.out.println("Client received order from Server: "+response);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return menuDetail;
    }


    public void sendTextData(String xmlContent, ObjectOutputStream out) {
        try {
            System.out.println(xmlContent);
            out.writeObject(xmlContent);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {

        Client client = Client.getInstance();
        client.getResponse("1");

    }
}