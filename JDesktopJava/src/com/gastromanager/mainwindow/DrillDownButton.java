package com.gastromanager.mainwindow;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

public class DrillDownButton extends JButton implements ActionListener, MouseMotionListener {

	
	private DrillDownButton Button;
	protected Point origPoint;
//	private Point whereToDrop;
	protected Cursor draggingCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
//	private boolean draggable = true;
	protected boolean overlapping = false;
	private int width, height, xCoord, yCoord;
	private String name;
	protected boolean readyToMove = false;
	
	
	public void init(int width, int height, int x, int y, String name, DrillDownGroup grp) {

		Button = new DrillDownButton(name);
		Button.setSize(height, width);
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(Button, popupMenu);
				
		Button.setBounds(x,y,width,height);
//		this.height = height;
//		this.width = width;
//		this.xCoord = x;
//		this.yCoord = y;
		
		JMenuItem mntmMove = new JCheckBoxMenuItem("Move");
		
		MouseMotionListener dragListener = new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				origPoint = e.getPoint();
				Button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			
			public void mouseDragged(MouseEvent e) {
				int origX = origPoint.x;
				int origY = origPoint.y;
				
				Point relativeToScreen = grp.getLocationOnScreen();
				Point mouseOnScreen = e.getLocationOnScreen();
				Point position 
				= new Point(mouseOnScreen.x - relativeToScreen.x - origX,
							mouseOnScreen.y - relativeToScreen.y - origY);
				
				Button.setLocation(position);
				Button.xCoord = position.x;
				Button.yCoord = position.y;
				
				if (overlapping) {
					getParent().setComponentZOrder(Button, 0);
					repaint();
				}
			}
			
		};
				
		Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mntmMove.isSelected() == true) {
//					moveButton(Button, grp);
					Button.addMouseMotionListener(dragListener);
				} else if (mntmMove.isSelected() == false) {
					Button.removeMouseMotionListener(dragListener);
					Button.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});
		
		popupMenu.add(mntmMove);
		
		JMenuItem mntmDelete = new JMenuItem("Delete");
		mntmDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				grp.remove(Button);
				grp.revalidate();
				grp.repaint();
				
			}			
		});
		popupMenu.add(mntmDelete);
		JMenuItem mntmRename = new JMenuItem("Rename");
		mntmRename.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EditDialog d = new EditDialog();
				d.openRename(Button);
			}			
		});
		popupMenu.add(mntmRename);
		
		JMenuItem mntmTarget = new JMenuItem("Select target menu");
		mntmTarget.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				
				
				
			}			
		});
		popupMenu.add(mntmTarget);
		
		
		grp.add(Button);
		grp.revalidate();
	    grp.repaint();
	    Button.setVisible(true);
	    
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

}
