package com.gastromanager.mainwindow;

import java.awt.Choice;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;

public class OrderingMenu extends JPanel {

	private JTextField txtFieldTable;
	private JTextField txtFieldChair;
	private JTextField txtFieldWaiter;
	private JTextField tfOrderID;
	private JTextField tfMenuID;
	
	
	public OrderingMenu() { 
	    JLabel lblTable = new JLabel("Table: ");
	    lblTable.setBounds(20, 11, 46, 14);
	    this.add(lblTable);
	    
	    txtFieldTable = new JTextField();
	    txtFieldTable.setBounds(20, 24, 60, 20);
	    this.add(txtFieldTable);
	    txtFieldTable.setColumns(10);
	    
	    JLabel lblChair = new JLabel("Chair: ");
	    lblChair.setBounds(90, 11, 46, 14);
	    this.add(lblChair);
	    
	    txtFieldChair = new JTextField();
	    txtFieldChair.setBounds(90, 24, 60, 20);
	    this.add(txtFieldChair);
	    txtFieldChair.setColumns(10);
	    
	    JLabel lblWaiter = new JLabel("Waiter: ");
	    lblWaiter.setBounds(160, 11, 46, 14);
	    this.add(lblWaiter);
	    
	    txtFieldWaiter = new JTextField();
	    txtFieldWaiter.setBounds(160, 24, 60, 20);
	    this.add(txtFieldWaiter);
	    txtFieldWaiter.setColumns(10);
	    
//	    JTree tree_1 = new JTree();
//	    tree_1.setBounds(10, 55, 236, 276);
//	    this.add(tree_1);
	    
	    JList list = new JList();
	    list.setBounds(10, 55, 236, 276);
	    this.add(list);
	    
	    JLabel lblOrderID = new JLabel("Order ID");
	    lblOrderID.setBounds(256, 56, 68, 14);
	    this.add(lblOrderID);
	    
	    tfOrderID = new JTextField();
	    tfOrderID.setBounds(256, 76, 109, 20);
	    this.add(tfOrderID);
	    tfOrderID.setColumns(10);
	    
	    JButton lblSelectOrderID = new JButton("Select Order ID");
	    lblSelectOrderID.setBounds(256, 101, 109, 23);
	    this.add(lblSelectOrderID);
	    
	    JButton btnPrevious = new JButton("<-");
	    btnPrevious.setBounds(256, 135, 50, 23);
	    this.add(btnPrevious);
	    
	    JButton btnNext = new JButton("->");
	    btnNext.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    	}
	    });
	    btnNext.setBounds(315, 135, 50, 23);
	    this.add(btnNext);
	    
	    JButton btnNewOrderID = new JButton("New Order ID");
	    btnNewOrderID.setBounds(256, 169, 111, 50);
	    this.add(btnNewOrderID);
	    
	    Choice ddChoice = new Choice();
	    ddChoice.setBounds(406, 50, 120, 20);
	    this.add(ddChoice);
	    
	    JLabel lblDrillDownOpts = new JLabel("Drill Down Options: ");
	    lblDrillDownOpts.setBounds(406, 27, 120, 14);
	    this.add(lblDrillDownOpts);
	    
	    tfMenuID = new JTextField();
	    tfMenuID.setBounds(406, 102, 120, 20);
	    this.add(tfMenuID);
	    tfMenuID.setColumns(10);
	    
	    JLabel lblMenuID = new JLabel("Menu ID:");
	    lblMenuID.setBounds(406, 79, 60, 14);
	    this.add(lblMenuID);
	    
	    JButton btnSelectMenuID = new JButton("Select Menu ID");
	    btnSelectMenuID.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    	}
	    });
	    btnSelectMenuID.setBounds(406, 135, 120, 23);
	    this.add(btnSelectMenuID);
	
	}

	public OrderingMenu(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	public OrderingMenu(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public OrderingMenu(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

}
