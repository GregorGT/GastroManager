package com.gastromanager.mainwindow;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class DrillDownGroup extends JPanel{

	public DrillDownGroup group;
//	public DrillDownButton button;
	public int xCoord, yCoord, height, width;
	public String name, id;
	public GMTreeItem treeItem;
	public int buttonCount;
//	public Set<DrillDownButton> buttons = new HashSet<DrillDownButton>();
	
	public DrillDownGroup(int width, int height, String name, DrillDownMenu ddmenu) {
		
		this.setLayout(null);
//			newpanel.setLayout(new GridLayout(5, 5));
    	this
    	.setBorder(new TitledBorder(null, name,
    			TitledBorder.LEADING, TitledBorder.TOP, null, null));
    	ddmenu.add(this);
    	this.setBounds(10, 150, width, height);
    	this.xCoord = 10;
    	this.yCoord = 150;
    	this.name = name;
    	
    	ddmenu.add(this);
    	this.setVisible(true);   		
	}

	public DrillDownGroup(int width, int height, String name) {
		this.setLayout(null);
		this.setPreferredSize(new Dimension(1000, 1000));
		this.setBorder(new TitledBorder(null, name, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.name = name;
		this.setVisible(true);
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setTreeItem(GMTreeItem item) {
		this.treeItem = item;
	}
	
	public DrillDownGroup() {
	}

	public void clear() {
		this.group = null;
	}


}
