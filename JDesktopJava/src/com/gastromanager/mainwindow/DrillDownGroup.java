package com.gastromanager.mainwindow;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class DrillDownGroup extends JPanel {

	public DrillDownGroup newpanel;
	public int buttonIndex = 0;
	
	public void newDrillDown(int width, int height, String name, DrillDownMenu menu) {
		newpanel = new DrillDownGroup();

			newpanel.setLayout(new GridLayout(5, 5));

    	newpanel
    	.setBorder(new TitledBorder(null,
    			name, TitledBorder.LEADING, TitledBorder.TOP, null, null));
    	menu.add(newpanel);
    	newpanel.setBounds(10, 150, width, height);
    	
    	newpanel.setVisible(true);   		
	}
	
	public void newButton(int width, int height, String name, DrillDownGroup grp) {
		DrillDownButton btn = new DrillDownButton();
		
		btn.init(width, height, name, newpanel);
		buttonIndex++;
		System.out.println(buttonIndex + " <--- number of buttons");
		
	}
	
	public DrillDownGroup() {
		// TODO Auto-generated constructor stub
	}

	public DrillDownGroup(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	public DrillDownGroup(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public DrillDownGroup(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

}