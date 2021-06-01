package com.gastromanager.mainwindow;

import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class DrillDownMenu extends JPanel {

	public JTextField txtDDHeight;
	public JTextField txtDDWidth;
	public JTextField txtDDName;
	public String ddName;
	public JTextField txtButtonHeight;
	public JTextField txtButtonWidth;
	public JTextField txtButtonName;
	public DrillDownGroup drillDownGroup;
	
	public DrillDownMenu(GMTreeItem treeitem, DefaultTreeModel model, GMTree tree, MenuElement menuE) {		
		
//		this.add(scrollPane);
//		
//		public JScrollPane scrollPane = new JScrollPane(drillDownGroup, 
//				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
//				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		
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
					String ddheight = txtDDHeight.getText();
					int nDDHeight = Integer.parseInt(ddheight);
					String ddwidth = txtDDWidth.getText();
					int nDDWidth = Integer.parseInt(ddwidth);
					ddName = txtDDName.getText();
					
					drillDownGroup = new DrillDownGroup(nDDWidth, nDDHeight, ddName, DrillDownMenu.this);
					componentToTree(treeitem, model, "drilldownmenus", drillDownGroup);

				} else if (drillDownGroup != null) {
					DrillDownMenu.this.remove(drillDownGroup);
					DrillDownMenu.this.revalidate();
					DrillDownMenu.this.repaint();

					String ddheight = txtDDHeight.getText();
					int nDDHeight = Integer.parseInt(ddheight);
					String ddwidth = txtDDWidth.getText();
					int nDDWidth = Integer.parseInt(ddwidth);
					ddName = txtDDName.getText();
//					System.out.println(drillDownGroup.toString());

					drillDownGroup = new DrillDownGroup(nDDWidth, nDDHeight, ddName, DrillDownMenu.this);
					componentToTree(treeitem, model, "drilldownmenus", drillDownGroup);

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

				DrillDownButton newBtn = new DrillDownButton(nBtnWidth, nBtnHeight, 10, 20, btnName, drillDownGroup);
				newBtn.addMenuElements(menuE);
				drillDownGroup.add(newBtn);
				
				componentToTree(treeitem, model, drillDownGroup.name, newBtn);
				
			}
		};

		btnAddButton.addActionListener(addButton);	

		JButton btnClear = new JButton("Clear");
		ActionListener clearFromEditor = new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				DrillDownMenu.this.remove(drillDownGroup);
				DrillDownMenu.this.revalidate();
				DrillDownMenu.this.repaint();
			}
		};

		btnClear.addActionListener(clearFromEditor);
		btnClear.setBounds(400, 16, 100, 60);
		this.add(btnClear);

	}
	
	public DrillDownMenu(GMTree tree, GMTreeItem item, OrderingMenu parent) {
		this.setBounds(0, 340,
						Integer.parseInt(item.getAttribute("width")) , 
						Integer.parseInt(item.getAttribute("height")));
		this.setVisible(true);
		parent.add(this);
		
		
	}
	
	public void componentToTree(GMTreeItem treeItem, DefaultTreeModel model, String parent, Component comp) {
		Enumeration enumer = treeItem.children();
		if (treeItem.toString() == parent) {
			switch (parent) {
		
			case "drilldownmenus" : 
				GMTreeItem newItem = new GMTreeItem(comp.getName());
				newItem.setName(comp.getName());
				newItem.setXmlName("drilldownmenu");
				newItem.addAttributes("name", comp.getName());
				newItem.addMenuElements(treeItem.menuElement);
				newItem.treeParent = treeItem.getTree();
//				System.out.println(treeItem.getTree().toString());
				String associatedID = assignUUID();
				newItem.setBtnAssociatedId(associatedID);
				((DrillDownGroup) comp).setId(associatedID);
				GMTreeItem heightItem = new GMTreeItem("Height :" + String.valueOf(comp.getSize().height));
				newItem.addAttributes("height", String.valueOf(comp.getSize().height));
				GMTreeItem widthItem = new GMTreeItem("Width :" + String.valueOf(comp.getSize().width));
				newItem.addAttributes("width", String.valueOf(comp.getSize().width));
				
				((DrillDownGroup) comp).setTreeItem(newItem);
				model.insertNodeInto(newItem, treeItem, 0);
				model.insertNodeInto(heightItem, newItem, 0);
				model.insertNodeInto(widthItem, newItem, 0);
				
				break;
		
			default :
				
				GMTreeItem nItem = new GMTreeItem(comp.getName());
				nItem.setName(comp.getName());
				nItem.setXmlName("button");
				nItem.addAttributes("name", comp.getName());
				nItem.addMenuElements(treeItem.menuElement);
				String associatedID2 = assignUUID();
				nItem.setBtnAssociatedId(associatedID2);
				
				((DrillDownButton) comp).setId(associatedID2);
				nItem.addAttributes("id", associatedID2);
				((DrillDownButton) comp).setTreeItem(nItem);
				nItem.setTree(treeItem.getTree());
				
				GMTreeItem hItem = new GMTreeItem("Height:" + String.valueOf(comp.getHeight()));
				nItem.addAttributes("height", String.valueOf(comp.getHeight()));
				GMTreeItem wItem = new GMTreeItem("Width:" + String.valueOf(comp.getWidth()));
				nItem.addAttributes("width", String.valueOf(comp.getWidth()));
				GMTreeItem xPItem = new GMTreeItem("X: " + String.valueOf(((DrillDownButton) comp).getXPos()));
				nItem.addAttributes("x-position", String.valueOf(((DrillDownButton) comp).getXPos()));
				GMTreeItem yPItem = new GMTreeItem("Y: " + String.valueOf(((DrillDownButton) comp).getYPos()));
				nItem.addAttributes("y-position", String.valueOf(((DrillDownButton) comp).getYPos()));

				model.insertNodeInto(nItem, treeItem, 0);
				model.insertNodeInto(hItem, nItem, 0);
				model.insertNodeInto(wItem, nItem, 1);
				model.insertNodeInto(xPItem, nItem, 2);
				model.insertNodeInto(yPItem, nItem, 3);
				
//				((DrillDownButton) comp).setTreeItem(nItem);
				
				break;
			}
		}
		
		if(enumer != null) {
			while (enumer.hasMoreElements()) {
				componentToTree((GMTreeItem)enumer.nextElement(), model, parent , comp);
			}
		}
	}

	public String assignUUID() {
		Random rd = new Random(); // creating Random object
		String uuid = String.valueOf(rd.nextLong());
		return uuid;
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
}
