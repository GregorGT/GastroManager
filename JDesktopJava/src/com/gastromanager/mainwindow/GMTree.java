package com.gastromanager.mainwindow;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

public class GMTree extends JTree {

	public void init(GMTree tree) {
		JPopupMenu treeContextMenu = new JPopupMenu();
	    
	    JMenuItem mntmTranslate = new JMenuItem("Translate");
	    mntmTranslate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}	    	
	    });
	    treeContextMenu.add(mntmTranslate);
	    
	    JMenuItem mntmSelectTarget = new JMenuItem("Select target menu");
	    mntmSelectTarget.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}	    	
	    });
	    treeContextMenu.add(mntmSelectTarget);
	    
	    
	    JMenuItem mntmDelete = new JMenuItem("Delete");
	    mntmDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}	    	
	    });
	    treeContextMenu.add(mntmDelete);
	    
	    
	    JMenuItem mntmSetPrice = new JMenuItem("Set Price");
	    treeContextMenu.add(mntmSetPrice);
	    
	    JMenuItem mntmChangeName = new JMenuItem("Change Name");
	    treeContextMenu.add(mntmChangeName);
	    
	    tree.addMouseListener(new MouseListener() {
	    	@Override
	    	public void mouseClicked(MouseEvent e) {
	    	    
	    		if (SwingUtilities.isLeftMouseButton(e)) {
	    			int row = tree.getClosestRowForLocation(e.getX(), e.getY());
	    	        tree.setSelectionRow(row);
	    	        System.out.println(tree.getSelectionPath().toString());
	    	        if (tree.getSelectionPath().toString().contains("drilldownmenus")) {
	    	        }
	    		}
	    		
	    		if (SwingUtilities.isRightMouseButton(e)) {
	    	        int row = tree.getClosestRowForLocation(e.getX(), e.getY());
	    	        tree.setSelectionRow(row);
//	    	        if (tree.getLastSelectedPathComponent().toString() == "drilldownmenus");
//	    	        treeContextMenu.openContextMenu(treeContextMenu, tree, "drilldownmenus");
//	    	        treeContextMenu.show(e.getComponent(), e.getX(), e.getY());
	    	        if (tree.getSelectionPath().toString().contains("drilldownmenus")) {
	    	        	System.out.println(tree.getSelectionPath());
	    	        	treeContextMenu.show(e.getComponent(), e.getX(), e.getY());
//	    	        	treeContextMenu.openContextMenu(treeContextMenu, tree, "drilldownmenus");
	    	        }
//	    	        System.out.println(tree.getSelectionPath());
//	    	        System.out.println(tree.getLastSelectedPathComponent());
	    	        
	    	    }
	    	}

			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
	    	
	    });
	}
	public GMTree() {
		// TODO Auto-generated constructor stub
	}

	public GMTree(Object[] value) {
		super(value);
		// TODO Auto-generated constructor stub
	}

	public GMTree(Vector<?> value) {
		super(value);
		// TODO Auto-generated constructor stub
	}

	public GMTree(Hashtable<?, ?> value) {
		super(value);
		// TODO Auto-generated constructor stub
	}

	public GMTree(TreeNode root) {
		super(root);
		// TODO Auto-generated constructor stub
	}

	public GMTree(TreeModel newModel) {
		super(newModel);
		
	}

	public GMTree(TreeNode root, boolean asksAllowsChildren) {
		super(root, asksAllowsChildren);
		// TODO Auto-generated constructor stub
	}

}
