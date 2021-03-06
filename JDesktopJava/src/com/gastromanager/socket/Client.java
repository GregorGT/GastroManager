package com.gastromanager.socket;

import com.gastromanager.util.XmlUtil;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;

public class Client {

    Socket socket  = null;
    DataOutputStream out = null;
    DataInputStream in = null;
    String serverIp = "127.0.0.1";
    Integer serverPort = 5000;

    public Client() {
        try {

            //send to server
            socket = new Socket(serverIp, serverPort);
            out = new DataOutputStream(socket.getOutputStream());

            //sendXMLData(null, out);
            sendTextData("1", out);

            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            String orderDetails = in.readUTF();
            String[] orderDetailElements = orderDetails.split(",");
            System.out.println("Client received order from Server: "+orderDetails);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendXMLData(String xmlContent, DataOutputStream out) {
        try {
            xmlContent = XmlUtil.readFileToString(
                    "C:\\Users\\Admin\\IdeaProjects\\GastroManager\\DesktopJava\\data\\sample_tempalte.xml", Charset.defaultCharset());
            //System.out.println(xmlContent);
            out.writeUTF(xmlContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        Client client = new Client();

    }
}