import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class DrillDownButton extends JButton {

	private DrillDownButton Button;
	
	public void init(int width, int height, String name, DrillDownGroup grp) {
		
		Button = new DrillDownButton(name);
		Button.setSize(height, width);
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(Button, popupMenu);
		
		JMenuItem mntmMove = new JMenuItem("Move");
		mntmMove.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				// move the button around 
				
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
		
		Button.setBounds(10,20,width,height);
		Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				java.awt.Toolkit.getDefaultToolkit().beep();
			}
		});
		grp.add(Button);
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
		// TODO Auto-generated constructor stub
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

}
