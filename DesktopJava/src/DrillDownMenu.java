import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class DrillDownMenu extends Composite {
	
	private int nHeight, nWidth;
	private String buttonName;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	public String drillDownMenuName;
	public int nDrillDownWidth;
	public int nDrillDownHeight;
	protected GMTreeItem trtmRoot;
	public DrillDownGroup DDGroup;
	public DrillDownButton btn;
	
	public void init(Composite composite, GMTreeItem treeItem) {		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		FormData fd_composite_1 = new FormData();
		fd_composite_1.top = new FormAttachment(0);
		fd_composite_1.left = new FormAttachment(0);
		composite_1.setLayoutData(fd_composite_1);
		formToolkit.adapt(composite_1);
		formToolkit.paintBordersFor(composite_1);
		
		Text ddTextHeight = new Text(composite_1, SWT.READ_ONLY | SWT.CENTER);
		ddTextHeight.setBounds(0, 0, 50, 18);
		ddTextHeight.setText("Height");
		formToolkit.adapt(ddTextHeight, true, true);
		
		Text ddTextWidth = new Text(composite_1, SWT.READ_ONLY | SWT.CENTER);
		ddTextWidth.setBounds(100, 0, 50, 18);
		ddTextWidth.setText("Width");
		formToolkit.adapt(ddTextWidth, true, true);
		
		Text ddTextName = new Text(composite_1, SWT.READ_ONLY | SWT.CENTER);
		ddTextName.setBounds(0, 20, 50, 20);
		ddTextName.setText("Text");
		formToolkit.adapt(ddTextName, true, true);
		
		Button btnAddDrillDown = new Button(composite_1, SWT.CENTER);
		btnAddDrillDown.setBounds(202, 0, 98, 42);
		btnAddDrillDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {				
					DDGroup = new DrillDownGroup(composite, SWT.NONE);
					DDGroup.addNewDrillDownGroup(nDrillDownHeight, nDrillDownWidth, drillDownMenuName, composite);
					toTree(treeItem, drillDownMenuName, "drilldownmenus", nDrillDownHeight, nDrillDownWidth);
				} catch (Exception a) {
					System.out.println("Empty drill down text fields");
				}
			}
		});
		btnAddDrillDown.setText("Add Drill Down Menu");
		formToolkit.adapt(btnAddDrillDown, true, true);
		
		Text ddTextFieldWidth = new Text(composite_1, SWT.BORDER);
		ddTextFieldWidth.setBounds(150, 0, 50, 18);
		ddTextFieldWidth.addListener(SWT.Modify, new Listener() {
			 public void handleEvent(Event event) {
				try {
				String drillDownMenuWidth = ddTextFieldWidth.getText();
				nDrillDownWidth = Integer.parseInt(drillDownMenuWidth);
				} catch (Exception e) {
					
				}
			}
		});
		formToolkit.adapt(ddTextFieldWidth, true, true);
		
		Text ddTextFieldHeight = new Text(composite_1, SWT.BORDER);
		ddTextFieldHeight.setBounds(50, 0, 50, 18);
		ddTextFieldHeight.addListener(SWT.Modify, new Listener() {
			 public void handleEvent(Event event) {
				try {
				String drillDownMenuHeight = ddTextFieldHeight.getText();
				nDrillDownHeight = Integer.parseInt(drillDownMenuHeight);
				} catch (Exception e) {
					
				}
			}
		});
		formToolkit.adapt(ddTextFieldHeight, true, true);
		
		Text ddTextFieldName = new Text(composite_1, SWT.BORDER);
		ddTextFieldName.setBounds(50, 18, 150, 20);
		ddTextFieldName.addListener(SWT.Modify, new Listener() {
			 public void handleEvent(Event event) {
				try {
				drillDownMenuName = ddTextFieldName.getText();
				} catch (Exception e) {
					
				}
			}
		});
		formToolkit.adapt(ddTextFieldName, true, true);
		
		Text text = new Text(composite, SWT.READ_ONLY | SWT.CENTER);
		FormData fd_text = new FormData();
		fd_text.bottom = new FormAttachment(0, 66);
		fd_text.right = new FormAttachment(0, 50);
		fd_text.top = new FormAttachment(0, 48);
		fd_text.left = new FormAttachment(0);
		text.setLayoutData(fd_text);
		text.setText("Height");
		formToolkit.adapt(text, true, true);
		
		Text text_1 = new Text(composite, SWT.READ_ONLY | SWT.CENTER);
		FormData fd_text_1 = new FormData();
		fd_text_1.bottom = new FormAttachment(0, 66);
		fd_text_1.right = new FormAttachment(0, 150);
		fd_text_1.top = new FormAttachment(0, 48);
		fd_text_1.left = new FormAttachment(0, 100);
		text_1.setLayoutData(fd_text_1);
		text_1.setText("Width");
		formToolkit.adapt(text_1, true, true);
		
		Text text_2 = new Text(composite, SWT.READ_ONLY | SWT.CENTER);
		FormData fd_text_2 = new FormData();
		fd_text_2.bottom = new FormAttachment(0, 88);
		fd_text_2.right = new FormAttachment(0, 50);
		fd_text_2.top = new FormAttachment(0, 68);
		fd_text_2.left = new FormAttachment(0);
		text_2.setLayoutData(fd_text_2);
		text_2.setText("Text");
		formToolkit.adapt(text_2, true, true);
		
		Text text_3 = new Text(composite, SWT.BORDER);
		FormData fd_text_3 = new FormData();
		fd_text_3.bottom = new FormAttachment(0, 66);
		fd_text_3.right = new FormAttachment(0, 100);
		fd_text_3.top = new FormAttachment(0, 48);
		fd_text_3.left = new FormAttachment(0, 50);
		text_3.setLayoutData(fd_text_3);
		formToolkit.adapt(text_3, true, true);
		text_3.addListener(SWT.Modify, new Listener() {
			
					public void handleEvent(Event event) {
				        try {
						String btnHeight = text_3.getText();
						nHeight = Integer.parseInt(btnHeight);
				        	System.out.println("text_3");
						} catch (Exception e){
//						
						  }
					    }
				 });
		
		Text text_4 = new Text(composite, SWT.BORDER);
		FormData fd_text_4 = new FormData();
		fd_text_4.bottom = new FormAttachment(0, 66);
		fd_text_4.right = new FormAttachment(0, 200);
		fd_text_4.top = new FormAttachment(0, 48);
		fd_text_4.left = new FormAttachment(0, 150);
		text_4.setLayoutData(fd_text_4);
		formToolkit.adapt(text_4, true, true);
		text_4.addListener(SWT.Modify, new Listener() {
			
			public void handleEvent(Event event) {
		        try {
				String btnHeight = text_4.getText();
				nWidth = Integer.parseInt(btnHeight);
		        	System.out.println("text_4");
				} catch (Exception e){
//				
				  }
			    }
		 });
		
		Text text_5 = new Text(composite, SWT.BORDER);
		FormData fd_text_5 = new FormData();
		fd_text_5.bottom = new FormAttachment(0, 88);
		fd_text_5.right = new FormAttachment(0, 200);
		fd_text_5.top = new FormAttachment(0, 68);
		fd_text_5.left = new FormAttachment(0, 50);
		text_5.setLayoutData(fd_text_5);
		formToolkit.adapt(text_5, true, true);
		text_5.addListener(SWT.Modify, new Listener() {
			
			public void handleEvent(Event event) {
		        try {
				buttonName = text_5.getText();
				} catch (Exception e){
				
				}
			}
		 });
		
		Button btnAddButton = new Button(composite, SWT.CENTER);
		FormData fd_btnAddButton = new FormData();
		fd_btnAddButton.bottom = new FormAttachment(0, 90);
		fd_btnAddButton.right = new FormAttachment(0, 300);
		fd_btnAddButton.top = new FormAttachment(0, 48);
		fd_btnAddButton.left = new FormAttachment(0, 200);
		btnAddButton.setLayoutData(fd_btnAddButton);
		btnAddButton.setText("Add Button");
		formToolkit.adapt(btnAddButton, true, true);
		btnAddButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {				
					DDGroup.addButtonToDrillDown(nHeight, nWidth, buttonName);
					toTree(treeItem, buttonName,  drillDownMenuName, nHeight, nWidth);
//					composite.layout(true);
				} catch (Exception a) {
					System.out.println("Empty button text fields");
				}
			}
		});
		
		
		
		
	}
	
	
	
	public void toTree(GMTreeItem treeItem, String newName, String parent, int height, int width) {
		
		TreeItem node[] = treeItem.getItems();
		for (int i = 0; i < node.length; ++i) {
			
			if (node[i] instanceof GMTreeItem) {
				GMTreeItem newItem = (GMTreeItem) node[i];
				if (newItem.getText() == parent) {
					GMTreeItem newtreeitem = new GMTreeItem(node[i], SWT.NONE);
					newtreeitem.setText(newName);
					GMTreeItem newitemheight = new GMTreeItem(newtreeitem, SWT.NONE);
					newitemheight.setText("height = " + height);
					GMTreeItem itemdrillDownWidth = new GMTreeItem(newtreeitem, SWT.NONE);
					itemdrillDownWidth.setText("width = " + width);
					newtreeitem.m_xmlname = "drilldownmenu";
					newtreeitem.m_attributes.putIfAbsent("name", newName);
					newtreeitem.m_attributes.putIfAbsent("height", Integer.toString(height));
					newtreeitem.m_attributes.putIfAbsent("width", Integer.toString(width));
				}
				toTree(newItem, newName, parent, height, width);
			}
		}
		
		
	}
	
	
	public DrillDownMenu(Composite parent, int style) {
		super(parent, style);
	}

}
