package com.gastromanager.mainwindow;
import java.awt.Color;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class DrillDownMenu extends JPanel {

	private JTextField txtDDHeight;
	private JTextField txtDDWidth;
	private JTextField txtDDName;
	public String ddName;
	private JTextField txtButtonHeight;
	private JTextField txtButtonWidth;
	private JTextField txtButtonName;
	public DrillDownGroup drillDownGroup;
	
	public void init(DrillDownMenu ddmenu, GMTreeItem treeitem, DefaultTreeModel model, GMTree tree) {		
				
		JPanel panelDrillDown = new JPanel();
		panelDrillDown
		.setBorder(new TitledBorder(null, "Drill Down", TitledBorder.LEADING,
					TitledBorder.TOP, null, null));
		panelDrillDown.setBounds(6, 0, 368, 71);
		this.add(panelDrillDown);
		panelDrillDown.setLayout(null);
		
		JLabel lblDDHeight = new JLabel("Height");
		lblDDHeight.setBounds(6, 20, 46, 14);
		panelDrillDown.add(lblDDHeight);
		
		txtDDHeight = new JTextField();
		txtDDHeight.setBounds(46, 17, 60, 20);
		panelDrillDown.add(txtDDHeight);
		txtDDHeight.setColumns(10);
		
		JLabel lblDDWidth = new JLabel("Width");
		lblDDWidth.setBounds(116, 20, 46, 14);
		panelDrillDown.add(lblDDWidth);
		
		txtDDWidth = new JTextField();
		txtDDWidth.setBounds(151, 17, 60, 20);
		panelDrillDown.add(txtDDWidth);
		txtDDWidth.setColumns(10);
		
		JLabel lblDDName = new JLabel("Name");
		lblDDName.setBounds(6, 48, 34, 14);
		panelDrillDown.add(lblDDName);
		
		txtDDName = new JTextField();
		txtDDName.setBounds(46, 45, 165, 20);
		panelDrillDown.add(txtDDName);
		txtDDName.setColumns(10);
		
		JButton btnAddDrillDown = new JButton("Add Drill Down Menu");
		btnAddDrillDown.setBounds(221, 16, 141, 46);
		
		
		ActionListener addDrillDownButton = new ActionListener() {
		    
		    public void actionPerformed(ActionEvent e) {       	
		    	
		    	if (drillDownGroup == null) {
		    	drillDownGroup = new DrillDownGroup();
		    	String ddheight = txtDDHeight.getText();
		    	int nDDHeight = Integer.parseInt(ddheight);
		    	String ddwidth = txtDDWidth.getText();
		    	int nDDWidth = Integer.parseInt(ddwidth);
		    	ddName = txtDDName.getText();

		    	drillDownGroup.init(nDDWidth, nDDHeight, ddName, ddmenu);	
		    	toTree(treeitem, model, ddName, "drilldownmenus", nDDHeight, nDDWidth);
		    	
		    	} else if (drillDownGroup != null) {
		    		ddmenu.remove(drillDownGroup.group);
		    		ddmenu.revalidate();
		    		ddmenu.repaint();
		    		drillDownGroup = new DrillDownGroup();
		    		
		        	String ddheight = txtDDHeight.getText();
		        	int nDDHeight = Integer.parseInt(ddheight);
		        	String ddwidth = txtDDWidth.getText();
		        	int nDDWidth = Integer.parseInt(ddwidth);
		        	ddName = txtDDName.getText();
		        	System.out.println(drillDownGroup.toString());
		        	drillDownGroup.init(nDDWidth, nDDHeight, ddName, ddmenu);
		        	toTree(treeitem, model, ddName, "drilldownmenus", nDDHeight, nDDWidth);
		    		
		    	}
		    }
		};
		
			
		
		btnAddDrillDown.addActionListener(addDrillDownButton);
		panelDrillDown.add(btnAddDrillDown);
		
		
		JPanel panelButtons = new JPanel();
		panelButtons.setLayout(null);
		panelButtons
		.setBorder(new TitledBorder(null, "Button", TitledBorder.LEADING,
					TitledBorder.TOP, null, null));
		panelButtons.setBounds(6, 73, 368, 71);
		this.add(panelButtons);
		
		JLabel lblButtonHeight = new JLabel("Height");
		lblButtonHeight.setBounds(6, 20, 46, 14);
		panelButtons.add(lblButtonHeight);
		
		txtButtonHeight = new JTextField();
		txtButtonHeight.setColumns(10);
		txtButtonHeight.setBounds(46, 17, 60, 20);
		panelButtons.add(txtButtonHeight);
		
		JLabel lblButtonWidth = new JLabel("Width");
		lblButtonWidth.setBounds(116, 20, 46, 14);
		panelButtons.add(lblButtonWidth);
		
		txtButtonWidth = new JTextField();
		txtButtonWidth.setColumns(10);
		txtButtonWidth.setBounds(151, 17, 60, 20);
		panelButtons.add(txtButtonWidth);
		
		JLabel lblButtonName = new JLabel("Name");
		lblButtonName.setBounds(6, 48, 34, 14);
		panelButtons.add(lblButtonName);
		
		txtButtonName = new JTextField();
		txtButtonName.setColumns(10);
		txtButtonName.setBounds(46, 45, 165, 20);
		panelButtons.add(txtButtonName);
		
		JButton btnAddButton = new JButton("Add Button");
		btnAddButton.setBounds(221, 16, 141, 46);
		panelButtons.add(btnAddButton);
		
		ActionListener addButton = new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				String btnheight = txtButtonHeight.getText();
            	int nBtnHeight = Integer.parseInt(btnheight);
            	String btnwidth = txtButtonWidth.getText();
            	int nBtnWidth = Integer.parseInt(btnwidth);
            	String btnName = txtButtonName.getText();
            	drillDownGroup.newButton(nBtnWidth, nBtnHeight, 10, 20, btnName, drillDownGroup);
            	toTree(treeitem, model, btnName, ddName, nBtnHeight, nBtnWidth);
            	
			}
		};
		
		
		btnAddButton.addActionListener(addButton);	
		
		
		JButton btnClear = new JButton("Clear");
		ActionListener clearFromEditor = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ddmenu.remove(drillDownGroup.group);
				ddmenu.revalidate();
				ddmenu.repaint();
			}
		};

		
		btnClear.addActionListener(clearFromEditor);
		btnClear.setBounds(400, 16, 100, 60);
		this.add(btnClear);
	}

		
	public void toTree(GMTreeItem treeItem, DefaultTreeModel model, String newName, String parent, int height, int width) {	

		Enumeration enum1 = treeItem.children();
	
		if (treeItem.toString() == parent) {
			GMTreeItem newItem = new GMTreeItem();
			newItem.setUserObject(newName);
			GMTreeItem heightItem = new GMTreeItem();
			heightItem.setUserObject("Height :" + String.valueOf(height));
			GMTreeItem widthItem = new GMTreeItem();
			widthItem.setUserObject("Width :" + String.valueOf(width));
			
			model.insertNodeInto(newItem, treeItem, 0);
			model.insertNodeInto(heightItem, newItem, 0);
			model.insertNodeInto(widthItem, newItem, 0);
		}
		
		if(enum1 != null) {
			while (enum1.hasMoreElements()) {
				toTree((GMTreeItem)enum1.nextElement(), model, newName, parent, height, width);
			}
		}
	}
	
		
	public DrillDownMenu() {
		// TODO Auto-generated constructor stub
	}

	public DrillDownMenu(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	public DrillDownMenu(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public DrillDownMenu(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}


	public void addActionListeners(DrillDownMenu menu) {

		
	}
}
