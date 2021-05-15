package com.gastromanager.mainwindow;

import java.awt.Color;
import java.awt.Panel;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PaymentMenu  extends Panel{
	
	private JTextArea txtArea1;
	private JTextArea txtArea2;
	
	private JTextField txtFieldFloor;
	private JTextField txtFieldTableID;
	private JTextField txtFieldOrderID;
	
	public PaymentMenu() {
	    txtArea1 = new JTextArea();
		txtArea1.setBounds(50, 220, 250, 300);
		txtArea1.setLineWrap(true);
		txtArea1.setSelectionColor(Color.green);
		this.add(txtArea1);
		
		txtArea2 = new JTextArea();
		txtArea2.setBounds(390, 220, 250, 300);
		txtArea2.setLineWrap(true);
		this.add(txtArea2);
		
	    txtFieldFloor = new JTextField("Floor");
		txtFieldFloor.setBounds(50, 30, 150, 30);
		this.add(txtFieldFloor);
		
	    txtFieldTableID = new JTextField("Table ID");
		txtFieldTableID.setBounds(50, 100, 150, 30);
		this.add(txtFieldTableID);
		
		txtFieldOrderID = new JTextField("Display(Order)ID");
		txtFieldOrderID.setBounds(220, 30, 150, 30);
		this.add(txtFieldOrderID);
		
		//Edit Buttons for the floor
		JButton selectButton1 = new JButton("Select");
		selectButton1.setBounds(50, 135, 150, 30);
		this.add(selectButton1);
				
		JButton selectButton2 = new JButton("Select");
		selectButton2.setBounds(220, 65, 150, 30);
		this.add(selectButton2);
				
				
		JButton prevButton = new JButton("<<");
		prevButton.setBounds(50, 60, 70, 30);
		this.add(prevButton);
				
		JButton nextButton = new JButton(">>");
		nextButton.setBounds(130, 60, 70, 30);
		this.add(nextButton);
		
		//edit buttons table ID
		JButton prevButton2 = new JButton("<<");
		prevButton2.setBounds(50, 165, 70, 30);
		this.add(prevButton2);
				
		JButton nextButton2 = new JButton(">>");
		nextButton2.setBounds(130, 165, 70, 30);
				
		//Display(order)ID edit buttons
		JButton prevButton3 = new JButton("<<");
		prevButton3.setBounds(220, 95, 70, 30);
		this.add(prevButton3);
				
		JButton nextButton3 = new JButton("<<");
		nextButton3.setBounds(300, 95, 70, 30);
		this.add(nextButton3);
		//main buttons
		JButton addButton = new JButton("Add");
		addButton.setBounds(305, 295, 80, 30);
		this.add(addButton);
				
		JButton removButton = new JButton("Remove");
		removButton.setBounds(305, 345, 80, 30);
		this.add(removButton);
				
		JButton undoButton = new JButton("Undo");
		undoButton.setBounds(135, 530, 80, 30);
		this.add(undoButton);
				
		JButton payedButton = new JButton("Payed");
		payedButton.setBounds(480, 530, 80, 30);
		this.add(payedButton);
		
	}

	public void setLayout(Object object) {
		// TODO Auto-generated method stub
		
	}

}
