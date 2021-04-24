package com.gastromanager.mainwindow;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class DrillDownButton extends JButton implements ActionListener, MouseMotionListener {

	protected  Point origPoint;
	protected Cursor draggingCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	protected boolean overlapping = false;
	
	public String name, id, targetID;
	public int height, width, x, y;
	public HashSet<GMTreeItem> elements = new HashSet<GMTreeItem>();
	public GMTreeItem associatedTreeItem;
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}
	
	public DrillDownButton getButton() {
		return this;
	}
	
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getXPos() {
		return this.x;
	}
	public int getYPos() {
		return this.y;
	}
	
	public DrillDownButton(int width, int height, int x, int y, String name, DrillDownGroup grp) {

		this.setName(name);
		this.setText(name);
		this.setSize(height, width);
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(this, popupMenu);
				
		this.setBounds(x,y,width,height);
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.name = name;
		
		JMenuItem mntmMove = new JCheckBoxMenuItem("Move");
		
		MouseMotionListener dragListener = new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				origPoint = e.getPoint();
				DrillDownButton.this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			
			public void mouseDragged(MouseEvent e) {
				int origX = origPoint.x;
				int origY = origPoint.y;
				
				Point relativeToScreen = grp.getLocationOnScreen();
				Point mouseOnScreen = e.getLocationOnScreen();
				Point position 
				= new Point(mouseOnScreen.x - relativeToScreen.x - origX,
							mouseOnScreen.y - relativeToScreen.y - origY);
				
				DrillDownButton.this.setLocation(position);

				DrillDownButton.this.x = position.x;
				DrillDownButton.this.y = position.y;
				// Y, X, WIDTH, HEIGHT
				
				GMTreeItem a = (GMTreeItem) associatedTreeItem.getChildAt(0);
				a.setUserObject("Y: " + String.valueOf(position.y));
				GMTreeItem b = (GMTreeItem) associatedTreeItem.getChildAt(1);
				b.setUserObject("X: " + String.valueOf(position.x));
				
				DefaultTreeModel model =
						(DefaultTreeModel) associatedTreeItem.treeParent.getModel();				
				model.reload(a);
				model.reload(b);
				
				if (overlapping) {
					getParent().setComponentZOrder(DrillDownButton.this, 0);
					repaint();
				}
			}			
		};
				
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mntmMove.isSelected() == true) {
					DrillDownButton.this.addMouseMotionListener(dragListener);
				} else if (mntmMove.isSelected() == false) {
					DrillDownButton.this.removeMouseMotionListener(dragListener);
					DrillDownButton.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					
					System.out.println(DrillDownButton.this.id);
				}
			}
		});
		
		popupMenu.add(mntmMove);
		
		JMenuItem mntmDelete = new JMenuItem("Delete");
		mntmDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				grp.remove(DrillDownButton.this);
				grp.revalidate();
				grp.repaint();
				
				DefaultTreeModel model = (DefaultTreeModel)
				associatedTreeItem.treeParent.getModel();
				
				model.removeNodeFromParent(associatedTreeItem);
			}			
		});
		popupMenu.add(mntmDelete);
		JMenuItem mntmRename = new JMenuItem("Rename");
		mntmRename.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EditDialog d = new EditDialog("Rename");
				d.renameDrillDownButton(DrillDownButton.this);
			}			
		});
		popupMenu.add(mntmRename);
		
		JMenu mntmTarget = new JMenu("Select target menu");
		
		MouseListener hover = new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
							
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				addElementsToTargetMenu(mntmTarget, elements);	
			}

			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
		};
		mntmTarget.addMouseListener(hover);
		
		popupMenu.add(mntmTarget);		
		
		grp.add(this);
		grp.revalidate();
	    grp.repaint();
	    this.setVisible(true);
	    
	}
	
	private void addElementsToTargetMenu(JMenu menuitem, HashSet<GMTreeItem> elem) {

		if (menuitem.getItemCount() == 0) {
		System.out.println(menuitem.getItemCount());
		elem.forEach((e) -> { 
			 JMenuItem newItem = new JMenuItem();
			 newItem.setText(e.getName());
			 newItem.setText(e.getDisplayString());
			 
			 newItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					DrillDownButton.this.targetID = e.getId();
					associatedTreeItem.addAttributes("target", e.getId());
					System.out.println( e.getId() + " ID ADDED TO BUTTON ");
				}
			 });
			 menuitem.add(newItem);
		 });		
		} else if (menuitem.getItemCount() > 0) {
			menuitem.removeAll();
			addElementsToTargetMenu(menuitem, elem);
		}
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	
	public String assignUUID() {
		Random rd = new Random(); // creating Random object
		String uuid = String.valueOf(rd.nextLong());
		return uuid;
	}
	
	public DrillDownButton() {
	}


	public DrillDownButton(Icon icon) {
		super(icon);
		// TODO Auto-generated constructor stub
	}

	public DrillDownButton(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public DrillDownButton(Action a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	public DrillDownButton(String text, Icon icon) {
		super(text, icon);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void addMenuElements(MenuElement menuE) {
		this.elements = menuE.getMenuElements();
	}

	public void setTreeItem(GMTreeItem treeItem) {
		this.associatedTreeItem = treeItem;
	}

}
