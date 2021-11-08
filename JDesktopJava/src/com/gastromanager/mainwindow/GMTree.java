/*Copyright 2021 GastroRice

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/


package com.gastromanager.mainwindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class GMTree extends JTree {
	public boolean loaded = false;
	public GMTreeItem rootItem;
	private int height, width, btnHeight, btnWidth, btnX, btnY;
	private String drilldownName, btnName, btnTarget, btnID;
	private LayoutMenu layoutMenu;

	public void init(GMTree tree, DrillDownMenu menu, GMTreeItem root) { 

		JPopupMenu treeContextMenu = new JPopupMenu();

		JMenuItem mntmTranslate = new JMenuItem("Edit name");
		mntmTranslate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EditDialog d = new EditDialog("name");			
				//d.renameTreeItem(tree.getSelectionPath());

			}	    	
		});
		treeContextMenu.add(mntmTranslate);

		JMenuItem mntmSelectTarget = new JMenuItem("Show on menu editor");
		mntmSelectTarget.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GMTreeItem selectedItem = (GMTreeItem) tree.getSelectionPath().getLastPathComponent();
				showOnEditor(selectedItem, menu);				
			}	    	
		});
		treeContextMenu.add(mntmSelectTarget);


		JMenuItem mntmDelete = new JMenuItem("Delete");
		mntmDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectToDelete(tree);
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

				if (SwingUtilities.isRightMouseButton(e)) {
					int row = tree.getClosestRowForLocation(e.getX(), e.getY());
					tree.setSelectionRow(row);

					if (tree.getSelectionPath().getLastPathComponent()
							.toString() != "drilldownmenus" &&
							tree.getSelectionPath().getLastPathComponent()
							.toString() != "menues" &&
							tree.getSelectionPath().getLastPathComponent()
							.toString() != "layout" &&
							tree.getSelectionPath().getLastPathComponent()
							.toString() != "reservations" &&
							tree.getSelectionPath().getLastPathComponent()
							.toString() != "settings") {
						
						treeContextMenu.show(e.getComponent(), e.getX(), e.getY());
						
//						GMTreeItem ex = (GMTreeItem) tree.getLastSelectedPathComponent();
//						System.out.println(ex.getAttribute("name"));
//						System.out.println(ex.getTree().toString());
//						System.out.println(ex.getXmlName());						
					}
					//in here it's possible to add different popup
					//menues for all the items
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

	public void selectToDelete(GMTree tree) {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

		TreePath[] paths = tree.getSelectionPaths();
		if (paths != null) {
			for (TreePath path : paths) {
				GMTreeItem node = (GMTreeItem) path.getLastPathComponent();
				if (node.getParent() != null && node.toString() != "root") {
					if (node.getXmlName().equals("floor")) {
						GMTreeItem parent = (GMTreeItem) node.getParent();
//						System.out.println(node.getName());
						model.removeNodeFromParent(node);
		    			node.removeFromParent();
		    			parent.children.remove(node);
		    			deleteFloor(node.getName());
		    			layoutMenu.getScrollPane().setViewportView(null);
					}
				}
			}
		}
	}
	
	private void deleteFloor(String name) {
		Map<Floor, ImageDrawing> floors = layoutMenu.getAllFloors();
		for (Map.Entry<Floor, ImageDrawing> f: floors.entrySet()) {
			if (f.getKey().getTitle().equals(name)) {
				f.getKey().setToDelete(true);
				return;
			}
		}
	}

	public void showOnEditor(GMTreeItem selectedItem, DrillDownMenu menu) {

		HashMap<String, String> attributes = selectedItem.getAttributes();

		Enumeration children = selectedItem.children();
		
		if (menu.drillDownGroup != null) {
			menu.remove(menu.drillDownGroup);	
			menu.drillDownGroup = null;
			menu.revalidate();
			menu.repaint();						
			showOnEditor(selectedItem, menu);
		} else if (menu.drillDownGroup == null) {
			
			attributes.forEach((k,v) -> {
				switch (k) {
				case "width" : 
					this.width = Integer.parseInt(v);
					break;
					
				case "height" : 
					this.height = Integer.parseInt(v);
					break;
					
				case "name" : 
					this.drilldownName = v;
					break;			
				}
			});
			
			menu.drillDownGroup = 
					new DrillDownGroup(width, height, drilldownName, menu);
			menu.revalidate();
			menu.repaint();
		}
		
		menu.drillDownGroup.removeAll();
		menu.drillDownGroup.revalidate();
		menu.drillDownGroup.repaint();
		
		if(children != null) {
			while (children.hasMoreElements()) {
				GMTreeItem child = (GMTreeItem) children.nextElement();
				if (child.getXmlName() == "button") {
					btnName = ""; btnID = ""; btnTarget = "";
					btnWidth = 0; btnHeight = 0; btnX = 0; btnY = 0;
					
					HashMap<String, String> attrs = child.getAttributes();
					
					attrs.forEach((k,v) -> {
						
						switch (k) {
						case "name" :
							btnName = v;
							break;
						case "width" : 
							btnWidth = Integer.parseInt(v);
							break;
						case "height" :
							btnHeight = Integer.parseInt(v);
							break;
						case "x-position" :
							btnX = Integer.parseInt(v);
							break;
						case "y-position" : 
							btnY = Integer.parseInt(v);
							break;
						case "target" : 
							btnTarget = v;
							break;
						case "id" :
							btnID = v;
							break;
						}
					});
				
					DrillDownButton newBtn =
							new DrillDownButton(btnWidth, btnHeight, 10, 10, btnName, menu.drillDownGroup);
					newBtn.associatedTreeItem = child;
					newBtn.id = btnID;
					newBtn.targetID = btnTarget;
					newBtn.setLocation(btnX, btnY);
			}		
		  }
		}
			
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

	
	public LayoutMenu getLayoutMenu() {
		return layoutMenu;
	}
	public void setLayoutMenu(LayoutMenu layoutMenu) {
		this.layoutMenu = layoutMenu;
	}
}
