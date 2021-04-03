import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class EditDialog extends JDialog {

	public void openRename(DrillDownButton btn) {
		
		JDialog d = new JDialog();
		d.setLayout(null);
//		d.setBounds(X, Y, WIDTH, HEIGHT);
		
		JLabel lblValue = new JLabel("New value: ");
		lblValue.setBounds(10, 10, 70, 14);
		d.add(lblValue);
		
		JTextField tfValue = new JTextField();
		tfValue.setBounds(80, 8, 120, 20);
		d.add(tfValue);
		tfValue.setColumns(10);
		
		JButton btnOk = new JButton("OK");
		btnOk.setBounds(20, 36, 55, 22);
		btnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btn.setText(tfValue.getText());
				btn.repaint();
				d.dispose();
			}
			
		});
		d.add(btnOk);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(90, 36, 90, 22);
		d.add(btnCancel);
		
		d.setDefaultCloseOperation(EditDialog.DISPOSE_ON_CLOSE);
		d.setBounds(100, 100, 250, 100);
		d.setVisible(true);		
	}
	
	
	public void returnNewVal() {
		
	}
	
	
	public EditDialog() {
		
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
