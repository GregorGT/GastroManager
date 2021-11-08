/*Copyright 2021 GastroRice

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/


package com.gastromanager.mainwindow;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.tree.TreePath;

public class EditDialog extends JDialog {

	JTextField tfValue;
	
	public void renameDrillDownButton(DrillDownButton btn) {
		btn.setText(tfValue.getText());
		btn.repaint();
	}
	
	public void renameTreeItem(TreePath treePath, String string) {
		GMTreeItem lole = new GMTreeItem(treePath);
		lole.setName(string);
	}
	
	public void returnNewVal() {
		
	}
	
	
	public EditDialog(String string) {
		JDialog d = new JDialog();
		d.setLayout(null);
//		d.setBounds(X, Y, WIDTH, HEIGHT);
		
		JLabel lblValue = new JLabel("New " + string + ": ");
		lblValue.setBounds(10, 10, 70, 14);
		d.add(lblValue);
		
		tfValue = new JTextField();
		tfValue.setBounds(80, 8, 120, 20);
		d.add(tfValue);
		tfValue.setColumns(10);
		
		JButton btnOk = new JButton("OK");
		btnOk.setBounds(20, 36, 55, 22);
		btnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
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
