/*Copyright 2021 GastroRice

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/


package com.gastromanager.socket;


import com.gastromanager.print.PrintServiceImpl;
import com.gastromanager.util.PublicVariables;
import com.gastromanager.util.XmlUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class Server {

    ServerSocket serverSocket = null;
    Socket socket = null;
    Integer serverSocketPort = 5000;

    public Server() {
        try {
            //Connection to client
            serverSocket = new ServerSocket(serverSocketPort);

            while (true) {
                socket = null;
                try {
                    socket = serverSocket.accept();
                    Thread t = new ClientHandler(socket);
                    t.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendXMLData(DataOutputStream out) {
        try {
            PublicVariables publicVariables = PublicVariables.getInstance();
            String xmlContent = XmlUtil.writeTreeIntoString(publicVariables.getTree());
//            String xmlContent = XmlUtil.readFileToString(
//                    "C:\\Users\\Admin\\IdeaProjects\\GastroManager\\JDesktopJava\\data\\sample_tempalte.xml",
//                    Charset.defaultCharset());
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
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
    }

    class ClientHandler extends Thread {
        final Socket s;

        public ClientHandler(Socket s) {
            this.s = s;
        }

        @Override
        public void run() {
            try {
                DataInputStream dis = new DataInputStream(this.s.getInputStream());
                String request = dis.readUTF();
                DataOutputStream dos = new DataOutputStream(this.s.getOutputStream());
                System.out.println("Received request for " + request);

                if (isNumeric(request)) {
                    //send result to client
                    String response = new PrintServiceImpl().getPrintInfo(request, "", null, 0);
                    dos.writeUTF(response);
                } else {
                    if (request.equals("menu")) {
                        sendXMLData(dos);
                    }
                }

                if (request.equals("Exit")) {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                }
                dos.close();
                dis.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
