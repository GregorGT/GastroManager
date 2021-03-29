import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;


public class RClickMenu extends Menu {
	
	//private EditDialog dialog;
	private String valueToEdit;
	
	
	public void openTreeMenu(Menu menu, Tree tree,  EditDialog dialog) {
		
		MenuItem[] items = menu.getItems();
		for (int i = 0; i < items.length; ++i) {
			items[i].dispose();
		}
		
		MenuItem newItem = new MenuItem(menu, SWT.NONE);
		newItem.setText("Menu for " + tree.getSelection()[0].getText());
		MenuItem mntmEditValue = new MenuItem(menu, SWT.NONE);
		mntmEditValue.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				EditDialog d = new EditDialog(menu.getShell());
				d.open((GMTreeItem) tree.getSelection()[0], "id");
			}
		});
		mntmEditValue.setText("Edit Value");
		
		MenuItem mntmEditName = new MenuItem(menu, SWT.NONE);
		mntmEditName.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dialog.open((GMTreeItem) tree.getSelection()[0], "name");
				tree.redraw();
			}
		});
		mntmEditName.setText("Edit Name");
		
//		MenuItem mntmExpandAll = new MenuItem(menu, SWT.NONE);
//		mntmExpandAll.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				
//				//expandAll(root);
//			}
//		});
//		mntmExpandAll.setText("Expand All");
		
		MenuItem mntmDeleteNode = new MenuItem(menu, SWT.NONE);
		mntmDeleteNode.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				//deleteSelectedNode();
				tree.getSelection()[0].dispose();
			}
		});
		mntmDeleteNode.setText("Delete Node");
		
		
		MenuItem mntmAddNewNode = new MenuItem(menu, SWT.NONE);
		mntmAddNewNode.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			}
		});
		mntmAddNewNode.setText("Add new Node");
		
		MenuItem mntmSetPrice = new MenuItem(menu, SWT.NONE);
		mntmSetPrice.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				EditDialog d = new EditDialog(menu.getShell());
				d.open((GMTreeItem) tree.getSelection()[0], "price");
			}
		});
		mntmSetPrice.setText("Set Price");
		
		//return valueToEdit;
		
	}
	
	public void openDrillDownMenu(RClickMenu menu, DrillDownGroup grp) {
				
		MenuItem[] items = menu.getItems();
		for (int i = 0; i < items.length; i++) {
			items[i].dispose();
		}
		
		activeItem(grp);
		
//		MenuItem mntmActive = new MenuItem(menu, SWT.CHECK);
//		mntmActive.addSelectionListener(new SelectionAdapter() {
//			private int selectedNum = 2;
//			@Override 
//			public void widgetSelected(SelectionEvent e) {
//				if (selectedNum % 2 == 0) {
//				Color selectedColor = new Color(0,255,0);
//				grp.setBackground(selectedColor);
////				grp.setEnabled(true);
//				System.out.println("a");
//				}
//				else if (selectedNum % 2 != 0) {
//					Color unselectedColor = new Color(100,0,0);
//					grp.setBackground(unselectedColor);
//				}
//				selectedNum++;
//			}
//		});
//		mntmActive.setText("Active");
		
		MenuItem mntmTranslate = new MenuItem(menu, SWT.CHECK);
		mntmTranslate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			}
		});
		mntmTranslate.setText("Translate");
		
		MenuItem mntmResize = new MenuItem(menu, SWT.CHECK);
		mntmResize.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			}
		});
		mntmResize.setText("Resize");
		
		MenuItem mntmRemove = new MenuItem(menu, SWT.CHECK);
		mntmRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			}
		});
		mntmRemove.setText("Remove");
		
	
		
	}
	
	public void activeItem(DrillDownGroup grp) {
		
			
		
	}
	

	public RClickMenu(Control parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}

	public RClickMenu(Menu parentMenu) {
		super(parentMenu);
		// TODO Auto-generated constructor stub
	}

	public RClickMenu(MenuItem parentItem) {
		super(parentItem);
		// TODO Auto-generated constructor stub
	}

	public RClickMenu(Decorations parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}
	
	protected void checkSubclass() {
	    //  allow subclass
	}

}
