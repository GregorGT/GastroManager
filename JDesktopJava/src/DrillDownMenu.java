import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.tree.TreeNode;

import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class DrillDownMenu extends JPanel {

	private JTextField txtDDHeight;
	private JTextField txtDDWidth;
	private JTextField txtDDName;
	private JTextField txtButtonHeight;
	private JTextField txtButtonWidth;
	private JTextField txtButtonName;
	private DrillDownGroup drillDownGroup;
//	private DrillDownButton ddButton;
	
	public void init(DrillDownMenu ddmenu, GMTreeItem treeitem) {
		
		JPanel panelDrillDown = new JPanel();
		panelDrillDown
		.setBorder(new TitledBorder(null, "Drill Down", TitledBorder.LEADING,
					TitledBorder.TOP, null, null));
		panelDrillDown.setBounds(6, 0, 368, 71);
		ddmenu.add(panelDrillDown);
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
		panelDrillDown.add(btnAddDrillDown);
		btnAddDrillDown.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
            	
            	drillDownGroup = new DrillDownGroup();
            	String ddheight = txtDDHeight.getText();
            	int nDDHeight = Integer.parseInt(ddheight);
            	String ddwidth = txtDDWidth.getText();
            	int nDDWidth = Integer.parseInt(ddwidth);
            	String ddName = txtDDName.getText();
            	
            	drillDownGroup.newDrillDown(nDDWidth, nDDHeight, ddName, ddmenu);
            	toTree(treeitem); // , ddName, "drilldownmenus", nDDHeight, nDDWidth);
            	
            	 	
            }
            
		});
		
		JPanel panelButtons = new JPanel();
		panelButtons.setLayout(null);
		panelButtons
		.setBorder(new TitledBorder(null, "Button", TitledBorder.LEADING,
					TitledBorder.TOP, null, null));
		panelButtons.setBounds(6, 73, 368, 71);
		ddmenu.add(panelButtons);
		
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
		btnAddButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				String btnheight = txtButtonHeight.getText();
            	int nBtnHeight = Integer.parseInt(btnheight);
            	String btnwidth = txtButtonWidth.getText();
            	int nBtnWidth = Integer.parseInt(btnwidth);
            	String btnName = txtButtonName.getText();
            	
            	drillDownGroup.newButton(nBtnWidth, nBtnHeight, btnName, drillDownGroup);
				
			}
			
		});
		
		
		
	}
	
	public void toTree(GMTreeItem treeItem) { //, String newName, String parent, int height, int width) {
		
		Enumeration epin = treeItem.children();
		
		if(epin != null) {
			while (epin.hasMoreElements()) {
				System.out.println(epin.nextElement());
				toTree((GMTreeItem) epin.nextElement());
			}
		}
		
		
//		for (Enumeration<TreeNode> epin = treeItem.preorderEnumeration(); epin.hasMoreElements();) {
//			
//			
//			
//			
//		}
		
//		for (Enumeration<TreeNode> penis = treeItem.preorderEnumeration(); penis.hasMoreElements();)
//		      if (penis.nextElement().toString() == "drilldownmenus") {
		    	 
//		    	  System.out.println("Found drilldown menu at: " + penis.nextElement().getIndex(treeItem));
		    	  
		    	  
//		      }
		
//		System.out.println(epin.nextElement());
		
		
//		Iterator<TreeNode> it = node.asIterator();
		
		
//		for (Enumeration<TreeNode> node = v.elements(); e.hasMoreElements();)
//		       System.out.println(e.nextElement());
//				
	}
	
	
//public void toTree(GMTreeItem treeItem, String newName, String parent, int height, int width) {
//		
//		TreeItem node[] = treeItem.getItems();
//		for (int i = 0; i < node.length; ++i) {
//		
//		if (node[i] instanceof GMTreeItem) {
//			GMTreeItem newItem = (GMTreeItem) node[i];
//			if (newItem.getText() == parent) {
//				GMTreeItem newtreeitem = new GMTreeItem(node[i], SWT.NONE);
//				newtreeitem.setText(newName);
//				GMTreeItem newitemheight = new GMTreeItem(newtreeitem, SWT.NONE);
//				newitemheight.setText("height = " + height);
//				GMTreeItem itemdrillDownWidth = new GMTreeItem(newtreeitem, SWT.NONE);
//				itemdrillDownWidth.setText("width = " + width);
//				newtreeitem.m_xmlname = "drilldownmenu";
//				newtreeitem.m_attributes.putIfAbsent("name", newName);
//				newtreeitem.m_attributes.putIfAbsent("height", Integer.toString(height));
//				newtreeitem.m_attributes.putIfAbsent("width", Integer.toString(width));
//			}
//			toTree(newItem, newName, parent, height, width);
//		}
//	}
//		
//		
//	}
	
	
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
