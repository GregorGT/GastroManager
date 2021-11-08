/*Copyright 2021 GastroRice

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/


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
