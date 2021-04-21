package com.gastromanager.mainwindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainTabWindow extends JPanel{
	
	public MainTabWindow() {
		
		super(new GridLayout(1, 1));
		
	    JTabbedPane tabbedPane =new JTabbedPane();  
	    
	    JComponent panel1 = makeTextPanel("Tab #1");	    
	    tabbedPane.addTab("Tab 1", panel1);
	    tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
	    
	    JComponent panel2 = makeTextPanel("Tab #2");	    
	    tabbedPane.addTab("Tab 2", panel2);
	    tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
	    
	    JComponent panel3 = makeTextPanel("Tab #3");	    
	    tabbedPane.addTab("Tab 3", panel3);
	    tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
	    
	    JComponent panel4 = makeTextArea();	
	    panel4.setPreferredSize(new Dimension(750,650));
	    tabbedPane.addTab("Payment", panel4);
	    tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);
	    
	    //add the tabbedpane to this panel	    
	    add(tabbedPane);
	    
	    //the following code enables the use of scrolling tabs
	    tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	    

	}
	
	protected JComponent makeTextPanel(String text) {
		JPanel panel = new JPanel(false);
		JLabel filler = new JLabel(text);
		filler.setHorizontalAlignment(JLabel.CENTER);
		panel.setLayout(new GridLayout(1, 1));
		panel.add(filler);
		return panel;
	}
	protected JComponent makeTextArea() {
		JPanel panel = new JPanel(false);
		JTextArea txtArea1 = new JTextArea();
		txtArea1.setBounds(50, 220, 250, 300);
		txtArea1.setLineWrap(true);
		txtArea1.setSelectionColor(Color.green);
		
		JTextArea txtArea2 = new JTextArea();
		txtArea2.setBounds(390, 220, 250, 300);
		txtArea2.setLineWrap(true);
		
		JTextField txtField1 = new JTextField("Floor");
		txtField1.setBounds(50, 30, 150, 30);
		
		JTextField txtField2 = new JTextField("Table ID");
		txtField2.setBounds(50, 100, 150, 30);
		
		JTextField txtField4 = new JTextField("Display(Order)ID");
		txtField4.setBounds(220, 30, 150, 30);
		
		//Edit Buttons for the floor
		JButton selectButton1 = new JButton("Select");
		selectButton1.setBounds(50, 135, 150, 30);
		
		JButton selectButton2 = new JButton("Select");
		selectButton2.setBounds(220, 65, 150, 30);
		
		
		JButton prevButton = new JButton("prev");
		prevButton.setBounds(50, 60, 70, 30);
		
		JButton nextButton = new JButton("next");
		nextButton.setBounds(130, 60, 70, 30);
		
		//edit buttons table ID
		JButton prevButton2 = new JButton("prev");
		prevButton2.setBounds(50, 165, 70, 30);
		
		JButton nextButton2 = new JButton("next");
		nextButton2.setBounds(130, 165, 70, 30);
		
		//Display(order)ID edit buttons
		JButton prevButton3 = new JButton("prev");
		prevButton3.setBounds(220, 95, 70, 30);
		
		JButton nextButton3 = new JButton("next");
		nextButton3.setBounds(300, 95, 70, 30);
		
		//main buttons
		JButton addButton = new JButton("Add");
		addButton.setBounds(305, 295, 80, 30);
		
		JButton removButton = new JButton("Remove");
		removButton.setBounds(305, 345, 80, 30);
		
		JButton undoButton = new JButton("Undo");
		undoButton.setBounds(135, 530, 80, 30);
		
		JButton payedButton = new JButton("Payed");
		payedButton.setBounds(480, 530, 80, 30);
		
		panel.setLayout(null);
		panel.add(txtArea1);
		panel.add(txtArea2);
		panel.add(addButton);
		panel.add(removButton);
		panel.add(undoButton);
		panel.add(payedButton);
		panel.add(prevButton);
		panel.add(prevButton2);
		panel.add(prevButton3);
		panel.add(nextButton);
		panel.add(nextButton2);
		panel.add(nextButton3);
		panel.add(txtField1);
		panel.add(txtField2);
		panel.add(selectButton1);
		panel.add(selectButton2);
		panel.add(txtField4);
		return panel;
		
	}
		
	/**
	 * Create the GUI and show it. For thread safety,
	 * this method should be invoked 
	 * from event dispatch method
	 * 
	 */
	public static void createAndShowGUI() {
		//create and setup the window
		JFrame frame = new JFrame("TAbbedPaneWindow");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//add content to the window
		frame.add(new MainTabWindow(), BorderLayout.CENTER);
		
		//display the window
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		//schedule a job for the event dispatch thread:
		//creating and showing this application's GUI
		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				//turn off the metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI();
			}
		});
	}

}
