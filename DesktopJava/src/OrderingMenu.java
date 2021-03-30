import java.util.Random;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class OrderingMenu extends Composite {

	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	public GMTreeItem m_gmTree;
	public Tree m_tree;
	public OrderingMenuDropdown m_dropdown;
	public String m_tableText;
	public String m_chairText;
	public String m_waiterText;
	public String m_floorText;
	public DrillDownMenu ordMenuComposite;
	
	
	public void init(OrderingMenu ordMenu) {
		
//		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
//		tbtmView.setControl(composite_1);
//		formToolkit.paintBordersFor(composite_1);
//		composite_1.setLayout(new FormLayout());
		Label lblTable= new Label(ordMenu, SWT.NONE);
		FormData fd_lblTable = new FormData();
		fd_lblTable.left = new FormAttachment(0, 10);
		lblTable.setLayoutData(fd_lblTable);
	    lblTable.setText("Table: ");
		
		Text txtTable = new Text(ordMenu, SWT.BORDER);
		fd_lblTable.bottom = new FormAttachment(txtTable, -6);
//		txtTable.setText("Table");
		FormData fd_txtTable = new FormData();
		fd_txtTable.left = new FormAttachment(0, 10);
		fd_txtTable.top = new FormAttachment(0, 36);
//		fd_txtTable.right = new FormAttachment(0, 60);
		fd_txtTable.width = 40;
		txtTable.setLayoutData(fd_txtTable);
		formToolkit.adapt(txtTable, true, true);
		txtTable.addListener(SWT.Modify, new Listener() {
			@Override 
			public void handleEvent(Event event) {
				try {
					m_tableText = txtTable.getText();
					System.out.println(m_tableText);
				} catch (Exception e) {
					System.out.println("Table text field");
				}
				
				
			}
		});
		
		m_tree = new Tree(ordMenu, SWT.BORDER);

		FormData fd_tree = new FormData();
		fd_tree.bottom = new FormAttachment(100, -213);
		fd_tree.top = new FormAttachment(txtTable, 6);
		fd_tree.left = new FormAttachment(0, 10);
		m_tree.setLayoutData(fd_tree);
		formToolkit.adapt(m_tree);
		formToolkit.paintBordersFor(m_tree);	
		m_tree.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				
				bottomDrillDownShow((GMTreeItem)m_tree.getSelection()[0], ordMenuComposite);
				
			}
			
			
		});
		
		m_gmTree = new GMTreeItem(m_tree, SWT.NONE);
		m_gmTree.setText("Orders");
		m_gmTree.setExpanded(true);
		
		ordMenuComposite = new DrillDownMenu(ordMenu, SWT.None);
		FormData fd_ordMenuComposite = new FormData();
		fd_ordMenuComposite.top = new FormAttachment(m_tree, 6);
		fd_ordMenuComposite.left = new FormAttachment(lblTable, 0, SWT.LEFT);
		fd_ordMenuComposite.bottom = new FormAttachment(100, -10);
		int aaa = 50;
		
		ordMenuComposite.addListener(SWT.MouseEnter, new Listener() {
			@Override 
			public void handleEvent(Event e) {
				
				Color newC = new Color(aaa, 0, aaa);
				ordMenuComposite.setBackground(newC);
				java.awt.Toolkit.getDefaultToolkit().beep();
				
			}
			
			
		});
		
		ordMenuComposite.addListener(SWT.MouseExit, new Listener() {
			
			@Override 
			public void handleEvent(Event e) {
				
				Color newCo = new Color(0,aaa,0);
				ordMenuComposite.setBackground(newCo);
				
			}
			
		});
		
		
		ordMenuComposite.setLayoutData(fd_ordMenuComposite);
		
		Text txtChair = new Text(ordMenu, SWT.BORDER);
		fd_txtTable.right = new FormAttachment(txtChair, -11);
		FormData fd_txtChair = new FormData();
		fd_txtChair.bottom = new FormAttachment(m_tree, -6);
		fd_txtChair.left = new FormAttachment(0, 73);
		txtChair.setLayoutData(fd_txtChair);
//		txtChair.setText("Chair");
		formToolkit.adapt(txtChair, true, true);
		txtChair.addListener(SWT.Modify, new Listener() {
			@Override 
			public void handleEvent(Event event) {
				try {
					m_chairText = txtChair.getText();
					System.out.println(m_chairText);
				} catch (Exception e) {
					System.out.println("Chair text field");
				}
				
				
			}
		});
		
		Text txtWaiter = new Text(ordMenu, SWT.BORDER);
		fd_txtChair.right = new FormAttachment(100, -365);
//		txtWaiter.setText("Waiter");
		FormData fd_txtWaiter = new FormData();
		fd_txtWaiter.right = new FormAttachment(txtChair, 60, SWT.RIGHT);
		fd_txtWaiter.left = new FormAttachment(0, 159);
		txtWaiter.setLayoutData(fd_txtWaiter);
		formToolkit.adapt(txtWaiter, true, true);
		txtWaiter.addListener(SWT.Modify, new Listener() {
			@Override 
			public void handleEvent(Event event) {
				try {
					m_waiterText = txtWaiter.getText();
					System.out.println(m_waiterText);
				} catch (Exception e) {
					System.out.println("Waiter text field");
				}
				
				
			}
		});
		
		Text txtFloor = new Text(ordMenu, SWT.BORDER);
		txtFloor.setText("Floor");
		FormData fd_txtFloor = new FormData();
		fd_txtFloor.left = new FormAttachment(txtWaiter, 9);
		fd_txtFloor.right = new FormAttachment(100, -260);
		txtFloor.setLayoutData(fd_txtFloor);
		formToolkit.adapt(txtFloor, true, true);
		txtFloor.addListener(SWT.Modify, new Listener() {
			@Override 
			public void handleEvent(Event event) {
				try {
					m_floorText = txtFloor.getText();
					System.out.println(m_floorText);
				} catch (Exception e) {
					System.out.println("Floor text field");
				}
				
				
			}
		});
		
		Text txtOrderId = new Text(ordMenu, SWT.BORDER);
		fd_ordMenuComposite.right = new FormAttachment(txtOrderId, 0, SWT.RIGHT);
		fd_tree.right = new FormAttachment(100, -260);
		txtOrderId.setText("Order ID");
		FormData fd_txtOrderId = new FormData();
		fd_txtOrderId.top = new FormAttachment(0, 61);
		fd_txtOrderId.right = new FormAttachment(100, -156);
		fd_txtOrderId.left = new FormAttachment(m_tree, 17);
		txtOrderId.setLayoutData(fd_txtOrderId);
		formToolkit.adapt(txtOrderId, true, true);
		
		Button button = new Button(ordMenu, SWT.NONE);
		FormData fd_button = new FormData();
		fd_button.left = new FormAttachment(m_tree, 17);
		fd_button.top = new FormAttachment(txtOrderId, 31);
		button.setLayoutData(fd_button);
		formToolkit.adapt(button, true, true);
		button.setText("<<");
		
		Button button_1 = new Button(ordMenu, SWT.NONE);
		FormData fd_button_1 = new FormData();
		fd_button_1.left = new FormAttachment(button, 31);
		fd_button_1.top = new FormAttachment(button, 0, SWT.TOP);
		button_1.setLayoutData(fd_button_1);
		formToolkit.adapt(button_1, true, true);
		button_1.setText(">>");
		
		Label lblChair = new Label(ordMenu, SWT.NONE);
		fd_txtChair.top = new FormAttachment(lblChair, 6);
		FormData fd_lblChair = new FormData();
		fd_lblChair.top = new FormAttachment(lblTable, 0, SWT.TOP);
		fd_lblChair.left = new FormAttachment(txtChair, 0, SWT.LEFT);
		lblChair.setLayoutData(fd_lblChair);
	    lblChair.setText("Chair: ");
	    
	    Label lblWaiter = new Label(ordMenu, SWT.NONE);
	    fd_txtWaiter.top = new FormAttachment(lblWaiter, 6);
	    FormData fd_lblWaiter = new FormData();
	    fd_lblWaiter.left = new FormAttachment(lblChair, 54);
	    fd_lblWaiter.top = new FormAttachment(lblTable, 0, SWT.TOP);
	    lblWaiter.setLayoutData(fd_lblWaiter);
	    lblWaiter.setText("Waiter: ");
	    
	    Label lblFloor = new Label(ordMenu, SWT.NONE);
	    fd_txtFloor.top = new FormAttachment(lblFloor, 6);
	    FormData fd_lblFloor = new FormData();
	    fd_lblFloor.left = new FormAttachment(lblWaiter, 24);
	    fd_lblFloor.top = new FormAttachment(lblTable, 0, SWT.TOP);
	    lblFloor.setLayoutData(fd_lblFloor);
	    lblFloor.setText("Floor: ");
		
	    Label lblDDMenu = new Label(ordMenu, SWT.NONE);
	    FormData fd_lblDDMenu = new FormData();
	    fd_lblDDMenu.top = new FormAttachment(lblTable, 0, SWT.TOP);
	    fd_lblDDMenu.right = new FormAttachment(100, -51);
	    lblDDMenu.setLayoutData(fd_lblDDMenu);
	    lblDDMenu.setText("Drill Down Options: ");
	    
	    Button btnSelectOrderId = new Button(ordMenu, SWT.NONE);
	    FormData fd_btnSelectOrderId = new FormData();
	    fd_btnSelectOrderId.left = new FormAttachment(m_tree, 17);
	    fd_btnSelectOrderId.top = new FormAttachment(txtOrderId, 6);
	    btnSelectOrderId.setLayoutData(fd_btnSelectOrderId);
	    formToolkit.adapt(btnSelectOrderId, true, true);
	    btnSelectOrderId.setText("Select Order ID");
	    
	    Button btnNewOrderId = new Button(ordMenu, SWT.NONE);
	    FormData fd_btnNewOrderId = new FormData();
	    fd_btnNewOrderId.right = new FormAttachment(100, -156);
	    fd_btnNewOrderId.left = new FormAttachment(m_tree, 17);
	    fd_btnNewOrderId.top = new FormAttachment(button, 16);
	    btnNewOrderId.setLayoutData(fd_btnNewOrderId);
	    formToolkit.adapt(btnNewOrderId, true, true);
	    btnNewOrderId.setText("New Order ID");
	    btnNewOrderId.addSelectionListener(new SelectionAdapter() {
	    	@Override 
	    	public void widgetSelected(SelectionEvent event) {
	    		
	    		try {
	    			GMTreeItem newitemtable = new GMTreeItem(m_gmTree, SWT.NONE);
	    			newitemtable.setText("Table: " + m_tableText);
	    			GMTreeItem newitemchair = new GMTreeItem(newitemtable, SWT.NONE); 
	    			newitemchair.setText("Chair: " + m_chairText);
	    			GMTreeItem newitemwaiter = new GMTreeItem(newitemchair, SWT.NONE);
	    			newitemwaiter.setText("Waiter: " + m_waiterText);
	    		} catch (Exception e) {
	    			System.out.println("NEW ORDER ID");
	    		}
	    	}
	    	
	    	
	    });
	    
	    Label lblSeparator1 = new Label(ordMenu, SWT.SEPARATOR | SWT.VERTICAL);
	    FormData fd_lblSeparator1 = new FormData();
	    fd_lblSeparator1.left = new FormAttachment(txtOrderId, 3);
	    fd_lblSeparator1.height = 1000;
	    fd_lblSeparator1.top = new FormAttachment(0);
	    lblSeparator1.setLayoutData(fd_lblSeparator1);
	    formToolkit.adapt(lblSeparator1, true, true);

	    m_dropdown = new OrderingMenuDropdown(ordMenu, SWT.FLAT | SWT.LEFT);
	    FormData fd_ordMenuDropdown = new FormData();
	    fd_ordMenuDropdown.top = new FormAttachment(lblDDMenu, 6);
	    fd_ordMenuDropdown.left = new FormAttachment(lblDDMenu, 0, SWT.LEFT);
	    fd_ordMenuDropdown.width = 100;
	    m_dropdown.setLayoutData(fd_ordMenuDropdown);
	    formToolkit.adapt(m_dropdown);
	    formToolkit.paintBordersFor(m_dropdown);
	    m_dropdown.init(m_dropdown, m_gmTree);
	    
	    Text text_7 = new Text(ordMenu, SWT.BORDER);
	    FormData fd_text_7 = new FormData();
	    fd_text_7.top = new FormAttachment(btnSelectOrderId, 0, SWT.TOP);
	    fd_text_7.left = new FormAttachment(lblDDMenu, 0, SWT.LEFT);
	    text_7.setLayoutData(fd_text_7);
	    formToolkit.adapt(text_7, true, true);
	    
	    Label lblMenuID = new Label(ordMenu, SWT.NONE);
	    FormData fd_lblMenuID = new FormData();
	    fd_lblMenuID.bottom = new FormAttachment(txtOrderId, 0, SWT.BOTTOM);
	    fd_lblMenuID.left = new FormAttachment(lblDDMenu, 0, SWT.LEFT);
	    lblMenuID.setLayoutData(fd_lblMenuID);
	    lblMenuID.setText("Menu ID");
	    
	    Button btnSelectMenuId = new Button(ordMenu, SWT.NONE);
	    FormData fd_btnSelectMenuId = new FormData();
	    fd_btnSelectMenuId.right = new FormAttachment(lblDDMenu, 84);
	    fd_btnSelectMenuId.left = new FormAttachment(lblDDMenu, 0, SWT.LEFT);
	    fd_btnSelectMenuId.top = new FormAttachment(text_7, 6);
	    btnSelectMenuId.setLayoutData(fd_btnSelectMenuId);
	    formToolkit.adapt(btnSelectMenuId, true, true);
	    btnSelectMenuId.setText("Select Menu ID");
		
	}
	
	protected void bottomDrillDownShow(TreeItem treeItem, DrillDownMenu composite) {
		
//		System.out.println(treeItem.getText());
//		Rectangle start = new Rectangle(5,5, 20,20);
//		Button mainbtn = new Button(composite, SWT.PUSH);
//		mainbtn.setText(treeItem.getText());
//		mainbtn.setBounds(start.x + 25, start.y, start.width, start.width);
//		start.x = mainbtn.getBounds().x + 25;
//		
//		mainbtn.redraw();
		TreeItem[] Attr = treeItem.getItems();
		if (Attr.length == 2) {
			DrillDownGroup grpShow = new DrillDownGroup(composite, SWT.NONE);
			for (int j = 0; j < Attr.length; j++) {
				
				System.out.println("AAA");
				
			}
		}
		
		
		
		
//		Point treeItemAttr = new Point();
		
		
//		grpShow.setBounds(5, 5, );
		
		
		
		
		
	}

	public void getTreeItems(Node xmlNode, GMTreeItem tree) {
				
		if (xmlNode.getNodeName().contains("#"))
			return;
		if (xmlNode.getNodeName() != "menues" 
				&& xmlNode.getNodeName() != "layout"
				&& xmlNode.getNodeName() != "reservations"
				&& xmlNode.getNodeName() != "settings") {
			GMTreeItem newNode = new GMTreeItem(tree, SWT.NONE);
			NamedNodeMap attributes = xmlNode.getAttributes();
		 
			if(attributes != null) {
				for(int k = 0; k<attributes.getLength(); ++k) {
					String name = attributes.item(k).getNodeName();
					String value = attributes.item(k).getNodeValue();
				
					newNode.m_attributes.put(name,  value);
					newNode.m_attributes.putIfAbsent("uuid", assignUUID());
					//if it's already assigned, do not assign
				}
			}
		 
			if (xmlNode.getNodeName() == "x") {
			 String emptyNode = xmlNode.getTextContent();
			}
		 
			if (xmlNode.getChildNodes().getLength() == 1) {
				if (xmlNode.getFirstChild().hasChildNodes() == false) {
					String nodeName = xmlNode.getFirstChild().getNodeName();
				 if (nodeName == "#text") {
					 newNode.m_value = xmlNode.getFirstChild().getTextContent(); 
				 }
			 }
		 }
		 
		 if (xmlNode.getNodeValue() != null && xmlNode.getNodeValue().length() > 0)
		 newNode.m_value = xmlNode.getNodeValue();
		 
		 newNode.m_xmlname = xmlNode.getNodeName();
		 newNode.setText(newNode.getDisplayString());
		 
		 NodeList children = xmlNode.getChildNodes();
		 
		 for (int i = 0; i < children.getLength(); ++i) { 
			getTreeItems(children.item(i), newNode);
			newNode.setExpanded(true);
		}
		 }
		
	}
	
	String assignUUID() {
		 Random rd = new Random(); // creating Random object
	     String uuid = String.valueOf(rd.nextLong());
	     return uuid;
	}
	
	public OrderingMenu(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}

}
