package com.gastromanager.mainwindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.HashSet;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class MenuElement {
	
	public int numberOfElements = 0;
	public HashSet<GMTreeItem> elements = new HashSet<GMTreeItem>();
	private JScrollPane treeScroll;
	
	public void showSelectionList(DrillDownGroup parent, DrillDownButton btn, GMTreeItem treeItem) {
		
		//on close [x] put back the tree items in the original tree
		
		JFrame itemSelectFrame = new JFrame();
		itemSelectFrame.setResizable(false);
		itemSelectFrame.setBounds(300, 300, 300 ,300);
		itemSelectFrame.setLayout(null);
		
		GMTreeItem roote = new GMTreeItem("All menu items");
		
		GMTree tree = new GMTree(roote);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				GMTreeItem epin = (GMTreeItem) tree.getSelectionPath().getLastPathComponent();
				System.out.println(epin.id);
			}
			
		});

		treeScroll = new JScrollPane(tree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		treeScroll.setBounds(10, 11, 280, 200);
		
		DefaultTreeModel epin = (DefaultTreeModel) tree.getModel();
		
		this.elements.forEach((item) -> {
			GMTreeItem newItem = item;
					
			epin.insertNodeInto(newItem, roote, 0);
			epin.reload();
		});
		
		itemSelectFrame.add(treeScroll);
		
		JButton btnSelectItem = new JButton("Select Item");
		btnSelectItem.setBounds(10, 222, 140, 23);
		
		ActionListener selectClick = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GMTreeItem selectedItem = (GMTreeItem) tree.getSelectionPath().getLastPathComponent();
				btn.setTargetId(selectedItem.id);
				btn.associatedTreeItem.m_attributes.remove("target");
				btn.associatedTreeItem.m_attributes.put("target", selectedItem.id);
				System.out.println(btn.targetID  + " TARGET ID ");
				returnItemsToTree(treeItem);
				itemSelectFrame.dispose();
			}
		};
		
		btnSelectItem.addActionListener(selectClick);
		
		itemSelectFrame.add(btnSelectItem);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(150, 222, 91, 23);
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				returnItemsToTree(treeItem);
				itemSelectFrame.dispose();
			}			
		});
		itemSelectFrame.add(btnCancel);
		
		itemSelectFrame.setVisible(true);
		itemSelectFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}
	
	public void returnItemsToTree(GMTreeItem item) {
		
		
		GMTreeItem newTreeItem = item.getTree().rootItem;
		DefaultTreeModel newModel = (DefaultTreeModel) newTreeItem.getTree().getModel();
		
		Enumeration<?> enumerChildren = newTreeItem.children();
		while (enumerChildren.hasMoreElements()) {
			GMTreeItem epin = (GMTreeItem) enumerChildren.nextElement();
			if (epin.getUserObject() == "menues") {
				this.elements.forEach((nItem) -> {
					GMTreeItem menuItem = nItem;
					newModel.insertNodeInto(nItem, epin, 0);
					newModel.reload(epin);
				});
			}
		}
		
	}
	
	public void addMenuElement(GMTreeItem item) {
		this.elements.add(item);
		this.numberOfElements++;
		System.out.println("number of menu elements: " + numberOfElements);
	}
	
	public HashSet<GMTreeItem> getMenuElements() {
		return this.elements;
	}
	
	public MenuElement() {
	}
	
	
}
