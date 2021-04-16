package com.gastromanager.mainwindow;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class DrillDownGroup extends JPanel{

	public DrillDownGroup group;
	public int xCoord, yCoord;
	
	public void init(int width, int height, String name, DrillDownMenu ddmenu) {
		group = new DrillDownGroup();
		
		group.setLayout(null);
//			newpanel.setLayout(new GridLayout(5, 5));

    	group
    	.setBorder(new TitledBorder(null, name,
    			TitledBorder.LEADING, TitledBorder.TOP, null, null));
    	ddmenu.add(group);
    	group.setBounds(10, 150, width, height);
    	this.xCoord = 10;
    	this.yCoord = 150;
    	ddmenu.add(group);
    	group.setVisible(true);   		
	}
	
	public void newButton(int width, int height, int x, int y, String name, DrillDownGroup drillDownGroup) {
		DrillDownButton btn = new DrillDownButton();
		btn.init(width, height, x, y, name, group);		
	}
	
	public DrillDownGroup() {
		
	}

	public void clear() {
		this.group = null;
		
//		buttonIndex = 0;
	}


}
