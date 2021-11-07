package com.gastromanager.mainwindow;

import com.gastromanager.socket.Server3;
import com.gastromanager.util.DbUtil;
import com.gastromanager.util.PropertiesUtil;
import com.gastromanager.util.Util;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;


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
    
    
    public  static JTextField serverTextField;
    private JLabel 	   serverLabel;
    
    private JButton resetHumanReadableId;
    private JLabel resetHumanReadableIdLabel;
    private JLabel lastResetTime;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



    public ServerSocketMenu() {
        isServerAlive = false;
        
       this.setLayout(new BorderLayout() );
        
       serverStartStopButton = new JButton("Start Server");
       serverStartStopButton.setBounds(320, 80, 150, 50);
        serverStartStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performServerButtonAction();
            }
        });

        serverStatusLabel = new JLabel("Status: ");
        serverStatusLabel.setBounds(50, 20, 100, 50);
        serverStatusInfoLabel = new JLabel(" Offline ");
        serverStatusInfoLabel.setBounds(100, 20, 100, 50);


        /*checkServerStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverStatusLabel.setText(checkSocketConnectionStatus()? "Online" : "Offline");
            }
        });
*/
        ipAddress = new JLabel("IP:Port");
        ipAddress.setBounds(50,80, 100, 50);
        ipAddressTextField = new JTextField();
        ipAddressTextField.setBounds(100, 80, 100, 50);
        ipAddressTextField.setEditable(false);
        try {
            InetAddress ip = InetAddress.getLocalHost();
            System.out.println("Ip address"+ip.getHostAddress() +" "+ip.getAddress().toString());
            ipAddressTextField.setText(ip.getHostAddress());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        port = new JLabel("Port:");
        port.setBounds(150, 80, 100, 50);
        portTextField = new JTextField();
        portTextField.setText(serverSocketPort.toString());
        portTextField.setBounds(200, 80, 100, 50);
        
        serverLabel = new JLabel("Server name:");
        serverTextField	= new JTextField();
        
        serverLabel.setBounds(50, 250, 100, 50);
        serverTextField.setText("Server name");
        serverTextField.setBounds(150, 250, 100, 50);

        resetHumanReadableIdLabel = new JLabel("Reset human readable ID");
        resetHumanReadableIdLabel.setBounds(400, 250, 200, 50);
        
        resetHumanReadableId = new JButton("Reset");
        resetHumanReadableId.setBounds(440, 300, 100, 50);
        
        
        lastResetTime = new JLabel("Last modified time");
        lastResetTime.setBounds(350, 350, 400, 50);
        lastResetTime.setText("Last modified time: " + PropertiesUtil.getPropertyValue("lastReset"));

        resetHumanReadableId.addActionListener(l -> {
        	PropertiesUtil.setAndSavePropertyValue("lastReset", sdf.format(new Date()));
        	lastResetTime.setText("Last modified time: " + PropertiesUtil.getPropertyValue("lastReset"));	
        });
        
        
        
        
        this.add(lastResetTime);
        this.add(resetHumanReadableId);        
        this.add(resetHumanReadableIdLabel);        

        this.add(serverStartStopButton);
        //this.add(checkServerStatusButton);
        this.add(serverStatusLabel);
        this.add(serverStatusInfoLabel);

        this.add(ipAddress);
        this.add(ipAddressTextField);

        this.add(port);
        this.add(portTextField);
        
        
        
        this.add(serverLabel);
        this.add(serverTextField);

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
