import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EditDialog extends JDialog {

	public EditDialog(JFrame parent, String title) {
		super(parent, title, true);
		if (parent != null) {
		      Dimension parentSize = parent.getSize(); 
		      Point p = parent.getLocation(); 
		      setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
		    }
		 JPanel messagePane = new JPanel();
		    messagePane.add(new JLabel("Value" ));
		    getContentPane().add(messagePane);
		    JPanel buttonPane = new JPanel();
		    JButton button = new JButton("OK"); 
		    buttonPane.add(button); 
		    button.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					  setVisible(false); 
					    dispose(); 
					
				}
			});
		    getContentPane().add(buttonPane, BorderLayout.SOUTH);
		    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		    pack(); 
		    setVisible(true);
		
	}
	

	public EditDialog(Frame owner) {
		super(owner);
		// TODO Auto-generated constructor stub
	}

	public EditDialog(Dialog owner) {
		super(owner);
		// TODO Auto-generated constructor stub
	}

	public EditDialog(Window owner) {
		super(owner);
		// TODO Auto-generated constructor stub
	}

	public EditDialog(Frame owner, boolean modal) {
		super(owner, modal);
		// TODO Auto-generated constructor stub
	}

	public EditDialog(Frame owner, String title) {
		super(owner, title);
		// TODO Auto-generated constructor stub
	}

	public EditDialog(Dialog owner, boolean modal) {
		super(owner, modal);
		// TODO Auto-generated constructor stub
	}

	public EditDialog(Dialog owner, String title) {
		super(owner, title);
		// TODO Auto-generated constructor stub
	}

	public EditDialog(Window owner, ModalityType modalityType) {
		super(owner, modalityType);
		// TODO Auto-generated constructor stub
	}

	public EditDialog(Window owner, String title) {
		super(owner, title);
		// TODO Auto-generated constructor stub
	}

	public EditDialog(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
		// TODO Auto-generated constructor stub
	}

	public EditDialog(Dialog owner, String title, boolean modal) {
		super(owner, title, modal);
		// TODO Auto-generated constructor stub
	}

	public EditDialog(Window owner, String title, ModalityType modalityType) {
		super(owner, title, modalityType);
		// TODO Auto-generated constructor stub
	}

	public EditDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		// TODO Auto-generated constructor stub
	}

	public EditDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		// TODO Auto-generated constructor stub
	}

	public EditDialog(Window owner, String title, ModalityType modalityType, GraphicsConfiguration gc) {
		super(owner, title, modalityType, gc);
		// TODO Auto-generated constructor stub
	}

}
