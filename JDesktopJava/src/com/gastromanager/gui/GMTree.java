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
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class GMTree extends JTree {
	public boolean loaded = false;
	
	public void init(GMTree tree, DrillDownMenu menu, DefaultTreeModel model, GMTreeItem root) { //GMTreeItem root, 
		
		JPopupMenu treeContextMenu = new JPopupMenu();
	    
	    JMenuItem mntmTranslate = new JMenuItem("Edit name");
	    mntmTranslate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}	    	
	    });
	    treeContextMenu.add(mntmTranslate);
	    
	    JMenuItem mntmSelectTarget = new JMenuItem("Show on menu editor");
	    mntmSelectTarget.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showOnEditor(tree.getSelectionPath(), menu, root);				
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
	    mntmSetPrice.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
	    	
	    });
	    
	    tree.addMouseListener(new MouseListener() {
	    	@Override
	    	public void mouseClicked(MouseEvent e) {
	    		
	    		if (SwingUtilities.isLeftMouseButton(e)) {
	    			int row = tree.getClosestRowForLocation(e.getX(), e.getY());
	    	        tree.setSelectionRow(row);
	    	        TreePath treePath = tree.getSelectionPath();
	    	        GMTreeItem newItem = new GMTreeItem(treePath);
	    	        
//	    	        System.out.println(" a -> " + newItem.m_name + newItem.m_value);
	    	        
	    	        System.out.println(tree.getSelectionPath().toString());
	    	        if (tree.getSelectionPath().toString().contains("drilldownmenus")) {
	    	        	
	    	        }
	    		}
	    		
	    		if (SwingUtilities.isRightMouseButton(e)) {
	    	        int row = tree.getClosestRowForLocation(e.getX(), e.getY());
	    	        tree.setSelectionRow(row);
	    	        
	    	        if (tree.getSelectionPath().toString().contains("drilldownmenus")) {
	    	        	System.out.println(tree.getSelectionPath());
	    	        	treeContextMenu.show(e.getComponent(), e.getX(), e.getY());
	    	        }
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
	
	public void showOnEditor(TreePath treePath, DrillDownMenu menu, GMTreeItem root) {
		
		GMTreeItem firstParent = (GMTreeItem) treePath.getLastPathComponent();
		int children = firstParent.getChildCount();
		System.out.println(children);
		
		Vector<String> ddValues = new Vector<String>();
		ddValues.add(firstParent.toString());
		
		for (int j= 0; j < children; j++) {
			if (firstParent.getChildAt(j).toString().contains("Height")) {
				String height = firstParent.getChildAt(j).toString().substring(8);
				ddValues.add(height);
			} else if (firstParent.getChildAt(j).toString().contains("Width")) {
				String width = firstParent.getChildAt(j).toString().substring(7);
				ddValues.add(width);
			}
			else if (!firstParent.getChildAt(j).toString().contains("Width") 
					 && !firstParent.getChildAt(j).toString().contains("Height")) {
				
//				menu.drillDownGroup.newButton(j, j, children, j, TOOL_TIP_TEXT_KEY, null);
				//button to editor
				//add button and coordinates x and y  
			}
			
		}

		
		String sNewName = ddValues.elementAt(0);
		int nNewHeight = Integer.parseInt(ddValues.elementAt(1)); 
		int nNewWidth = Integer.parseInt(ddValues.elementAt(2));
		int x; 
		int y;
		menu.remove(menu.drillDownGroup);
		menu.revalidate();
		menu.repaint();
		menu.drillDownGroup.init(nNewWidth, nNewHeight, sNewName, menu);
		
		for (int i = 0; i < ddValues.size(); i++) {
			
		}
		
//		menu.toTree(root, model, sNewName, "drilldownmenus", nNewHeight, nNewWidth);
		
	}
	
	public void buttonToTree(DrillDownGroup grp, int height, int width, int x, int y) {
		
		
		
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
