package com.gastromanager.socket;

import com.gastromanager.util.XmlUtil;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class Client {

    Socket socket  = null;
    BufferedReader bufferedReader = null;
    DataOutputStream out = null;


    public Client(String address, int port) {
        try {

            socket = new Socket(address, port);
            out = new DataOutputStream(socket.getOutputStream());

            sendXMLData(null, out);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            out.close();
            socket.close();
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

    public static void main(String[] args) {

        Client client = new Client("127.0.0.1", 5001);

    }
}
