package com.gastromanager.mainwindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class LayoutMenu extends JPanel {
	
	private String absolutePathToImage;
	private int windowWidth = 0;
	private int windowHeight = 0;
	private JPanel panelImage = null;
	private JScrollPane scrollPane = null;	
    private JLabel labelImage = null;  
    private ImageIcon icon = null;

    
	public LayoutMenu() {

		JButton newFloor = new JButton("<html>New<br/>floor</html>");
		JButton newTable = new JButton("<html>New<br/>table</html>");
		JButton newChair = new JButton("<html>New<br/>chair</html>");

		newFloor.setPreferredSize(new Dimension(100, 100));
		newTable.setPreferredSize(new Dimension(100, 100));
		newChair.setPreferredSize(new Dimension(100, 100));
		
		
		this.add(newFloor);
		this.add(newTable);
		this.add(newChair);


		JPanel imageSelectPanel = new JPanel();
		
		TitledBorder border = new TitledBorder("Image layout");
		JLabel selectImage = new JLabel("Select image for the floor's layout");
		JTextField filePathString = new JTextField();
		JButton browseImageButton = new JButton("Browse");
	
		browseImageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				JFrame frameForImageSelection = new JFrame();
				JFileChooser chooser = new JFileChooser();
			    FileNameExtensionFilter filter = new FileNameExtensionFilter("jpeg", "jpg", "png");
			    chooser.setFileFilter(filter);
			    int returnVal = chooser.showOpenDialog(frameForImageSelection);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			       System.out.println("You chose to open this file: " +
			       		chooser.getSelectedFile().getAbsolutePath());
			       absolutePathToImage = chooser.getSelectedFile().getAbsolutePath();
			       filePathString.setText(chooser.getSelectedFile().getAbsolutePath());
			       displayImage();
			    }
			    if (returnVal == JFileChooser.CANCEL_OPTION) {
			    	System.out.println("Canceled");
			    }
			}
		});
		
		imageSelectPanel.setBorder(border);
		filePathString.setPreferredSize(new Dimension(530, 30));
		imageSelectPanel.add(selectImage);	
		imageSelectPanel.add(filePathString);
		imageSelectPanel.setPreferredSize(new Dimension(730, 90));
		imageSelectPanel.add(browseImageButton);
		this.add(imageSelectPanel);
		
		this.addComponentListener(new ComponentAdapter() 
		{  
		    public void componentResized(ComponentEvent evt) {
		    	Component c = (Component)evt.getSource();
			    	
//		    	System.out.println("State: "+windowWidth+" "+windowHeight);
		    	
		        windowWidth = c.getWidth();
		        windowHeight = c.getHeight();
		        filePathString.setPreferredSize(new Dimension(windowWidth - 200, 30));
		        imageSelectPanel.setPreferredSize(new Dimension(windowWidth - 30, 90));
		        if (scrollPane != null) {
		        	scrollPane.setPreferredSize(new Dimension(windowWidth - 30, windowHeight-200));
		        }
		    }
		});
		
	}
	
	private void displayImage() {
		TitledBorder tb = new TitledBorder("Restaurant layout");
		if (panelImage != null) {
			this.remove(scrollPane);
		}
        
		icon = new ImageIcon(absolutePathToImage);

		labelImage = new JLabel(icon);
        panelImage = new JPanel();
        panelImage.add(labelImage);
        panelImage.setBorder(tb);
        
        scrollPane = new JScrollPane(panelImage,
                     JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                     JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        scrollPane.setPreferredSize(new Dimension(windowWidth - 30, windowHeight-200));
        
        this.add(scrollPane);
	}



}
