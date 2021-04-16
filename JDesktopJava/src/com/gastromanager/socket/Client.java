package com.gastromanager.socket;

import java.io.*;
import java.net.Socket;

public class Client {

    Socket socket  = null;
    BufferedReader bufferedReader = null;
    DataOutputStream out = null;

    public Client(String address, int port) {
        try {

            socket = new Socket(address, port);

            bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            out = new DataOutputStream(socket.getOutputStream());
            String line = "";

            while(!line.equals("Done")) {
                //line = in.readLine();
                line  = bufferedReader.readLine();
                //System.out.println(line);
                out.writeUTF(line);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bufferedReader.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {

        Client client = new Client("127.0.0.1", 5001);

    }
}
