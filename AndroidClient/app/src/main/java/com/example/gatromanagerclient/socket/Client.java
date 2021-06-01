package com.example.gatromanagerclient.socket;

import com.gastromanager.models.MenuDetail;
import com.gastromanager.models.OrderDetailsView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

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

    public String getOrderInfo(String request) {
        StringBuilder responseBuilder = null;

        try {
            sendTextData(request, out); //request could be orderId
            ArrayList<String> orderItems = (ArrayList<String>) in.readObject();
            for(String item : orderItems) {
                if(responseBuilder == null) {
                    responseBuilder = new StringBuilder();
                }
                responseBuilder.append(item);
                System.out.println(item);

            }
            //in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            /*byte[] bytes = new byte[1000]; //
            socket.getInputStream().read(bytes);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            in = new ObjectInputStream(byteArrayInputStream);*/
            // read from the stream
            /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] content = new byte[ 2048 ];
            int bytesRead = -1;
            while( ( bytesRead = in.read( content ) ) != -1 ) {
                baos.write( content, 0, bytesRead );
            }
            ByteArrayInputStream bais = new ByteArrayInputStream( baos.toByteArray() );
            in = new ObjectInputStream(bais);
            List<OrderItem> orderItemList  = (List<OrderItem>) in.readObject();
            for(OrderItem item : orderItemList) {
                responseBuilder.append(item.getItemId() +" "
                        + item.getQuantity() +"\n");

            }*/

            /*if(responseFromServer instanceof Object[]) {
                Object[] responseObjects = (Object[]) responseFromServer;
                for(int i=0; i < responseObjects.length; i++) {
                    Object responseObject = responseObjects[i];
                    if(responseObject instanceof OrderItem) {
                        OrderItem orderItem = (OrderItem) responseObject;
                        responseBuilder.append(orderItem.getItemId() +" "
                                + orderItem.getQuantity() +"\n");

                    }
                }

                *//*List<Object> orderDetailList = (List) responseFromServer;
                for(Object orderItemObject: orderDetailList) {
                    if(orderItemObject instanceof OrderItem) {
                        OrderItem currentItem = (OrderItem) orderItemObject;
                        responseBuilder.append(currentItem.getItemId() +" "
                        + currentItem.getQuantity() +"\n");
                    }
                }*//*
            }*/
            //System.out.println("Client received order from Server: "+response);
            //close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return (responseBuilder == null) ? null :responseBuilder.toString();
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

    public Integer getNewOrderId(String newOrderId) {
        Object response = null;
        Integer newGenOrderId = null;
        try {
            sendTextData(newOrderId, out); //request could be orderId / menu
            //in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            //if(in.available() > 0){
            response = in.readObject();
            newGenOrderId = (response == null) ? null : (Integer) response;
            //}
            //System.out.println("Client received order from Server: "+response);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return newGenOrderId;
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


    public void sendTextData(String textContent, ObjectOutputStream out) {
        try {
            System.out.println(textContent);
            out.writeObject(textContent);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {

        Client client = Client.getInstance();
        client.getResponse("1");

    }
}