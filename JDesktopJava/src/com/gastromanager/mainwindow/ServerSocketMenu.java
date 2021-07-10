package com.gastromanager.mainwindow;

import com.gastromanager.socket.Server3;
import com.gastromanager.util.PropertiesUtil;
import com.gastromanager.util.Util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class ServerSocketMenu extends JPanel {

    private final JButton serverStartStopButton;
    private final JLabel serverStatusLabel;
    private final JLabel serverStatusInfoLabel;
    private static Integer serverSocketPort =
            Integer.parseInt(PropertiesUtil.getPropertyValue("serverSocketPort"));
    private Server3 socketServer;
    private Boolean isServerAlive;

    private JLabel ipAddress;
    private JLabel port;
    private JTextField ipAddressTextField;
    private JTextField portTextField;



    public ServerSocketMenu() {
        isServerAlive = false;
       serverStartStopButton = new JButton("Start Server");
        serverStartStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performServerButtonAction();
            }
        });

        serverStatusLabel = new JLabel("Status: ");
        serverStatusInfoLabel = new JLabel(" Offline ");

        JButton checkServerStatusButton = new JButton("Check Server Status");

        /*checkServerStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverStatusLabel.setText(checkSocketConnectionStatus()? "Online" : "Offline");
            }
        });
*/
        ipAddress = new JLabel("IP:");
        ipAddressTextField = new JTextField();
        ipAddressTextField.setEditable(false);
        try {
            InetAddress ip = InetAddress.getLocalHost();
            System.out.println("Ip address"+ip.getHostAddress() +" "+ip.getAddress().toString());
            ipAddressTextField.setText(ip.getHostAddress());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        port = new JLabel("Port:");
        portTextField = new JTextField();
        portTextField.setText(serverSocketPort.toString());


        this.add(serverStartStopButton);
        //this.add(checkServerStatusButton);
        this.add(serverStatusLabel);
        this.add(serverStatusInfoLabel);

        this.add(ipAddress);
        this.add(ipAddressTextField);

        this.add(port);
        this.add(portTextField);


    }

   private void performServerButtonAction() {
       isServerAlive = checkSocketConnectionStatus();
       System.out.println(" isServerRunning "+ isServerAlive);
       if(serverStartStopButton.getText().contains("Start")) {
           if(socketServer == null && !isServerAlive) {
               serverSocketPort = portTextField.getText() != null && Util.isNumeric(portTextField.getText().trim()) ?
                       Integer.parseInt(portTextField.getText()) : serverSocketPort;
               socketServer = new Server3(serverSocketPort);
               socketServer.startServer();
           }
           serverStartStopButton.setText("Stop");
           serverStatusInfoLabel.setText(" Online ");
           System.out.println("Server online !!");
       } else {
           socketServer.stopServer();
           socketServer = null;
           serverStartStopButton.setText("Start");
           serverStatusInfoLabel.setText(" Offline ");
           System.out.println("Server offline !!");
           isServerAlive = checkSocketConnectionStatus();
           System.out.println(" Inside stop server : isServerRunning "+ isServerAlive);
       }
   }

    /*class ServerSocketHandler extends Thread {
        @Override
        public void run() {
            Server3 sockerServer = new Server3(serverSocketPort);
            sockerServer.start();

        }
    }
*/
    private Boolean checkSocketConnectionStatus() {
        /*Boolean isAlive = false;
        SocketAddress socketAddress = new InetSocketAddress("192.168.1.7", serverSocketPort);
        Socket socket = new Socket();
        int timeout = 2000;
        try {
            socket.connect(socketAddress);
            socket.close();
            isAlive = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isAlive;*/
        return socketServer != null && socketServer.isServerRunning();
    }


}
