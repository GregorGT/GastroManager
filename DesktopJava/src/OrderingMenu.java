import java.util.Random;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class OrderingMenu extends Composite {

	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	public GMTreeItem gmTree;
	public Tree tree;
	public OrderingMenuDropdown dropdown;
	
	public void init(OrderingMenu ordMenu) {
	
	Label lblTable= new Label(ordMenu, SWT.NONE);
	FormData fd_lblTable = new FormData();
	fd_lblTable.left = new FormAttachment(0, 10);
	lblTable.setLayoutData(fd_lblTable);
    lblTable.setText("Table: ");
	
	Text txtTable = new Text(ordMenu, SWT.BORDER);
	txtTable.setText("Table");
	FormData fd_txtTable = new FormData();
	fd_lblTable.bottom = new FormAttachment(txtTable, -6);
	fd_txtTable.left = new FormAttachment(0, 10);
	fd_txtTable.top = new FormAttachment(0, 36);
//	fd_txtTable.right = new FormAttachment(0, 60);
	fd_txtTable.width = 40;
	txtTable.setLayoutData(fd_txtTable);
	formToolkit.adapt(txtTable, true, true);
	
	tree = new Tree(ordMenu, SWT.BORDER);

	FormData fd_tree = new FormData();
	fd_tree.left = new FormAttachment(0, 10);
	fd_tree.top = new FormAttachment(txtTable, 6);
	fd_tree.bottom = new FormAttachment(100, -213);
	tree.setLayoutData(fd_tree);
	formToolkit.adapt(tree);
	formToolkit.paintBordersFor(tree);	
	tree.addListener(SWT.MenuDetect, new Listener() {
		@Override
		public void handleEvent(Event arg0) {
			
			//add menu and edit dialog for menu
			
		}
		
		
	});
	
	gmTree = new GMTreeItem(tree, SWT.NONE);
	gmTree.setText("root");
	gmTree.setExpanded(true);
	
	Text txtChair = new Text(ordMenu, SWT.BORDER);
	FormData fd_txtChair = new FormData();
	fd_txtChair.left = new FormAttachment(txtTable, 11);
	fd_txtChair.bottom = new FormAttachment(txtTable, 19); //, SWT.BOTTOM);
	fd_txtChair.top = new FormAttachment(txtTable, 0, SWT.TOP);
	txtChair.setLayoutData(fd_txtChair);
	txtChair.setText("Chair");
	formToolkit.adapt(txtChair, true, true);
	
	Text txtWaiter = new Text(ordMenu, SWT.BORDER);
	fd_txtChair.right = new FormAttachment(100, -365);
	txtWaiter.setText("Waiter");
	FormData fd_txtWaiter = new FormData();
	fd_txtWaiter.right = new FormAttachment(txtChair, 60, SWT.RIGHT);
	fd_txtWaiter.top = new FormAttachment(txtTable, 0, SWT.TOP);
	fd_txtWaiter.left = new FormAttachment(txtChair, 6);
	txtWaiter.setLayoutData(fd_txtWaiter);
	formToolkit.adapt(txtWaiter, true, true);
	
	Text txtFloor = new Text(ordMenu, SWT.BORDER);
	txtFloor.setText("Floor");
	FormData fd_txtFloor = new FormData();
	fd_txtFloor.top = new FormAttachment(txtTable, 0, SWT.TOP);
	fd_txtFloor.right = new FormAttachment(100, -260);
	txtFloor.setLayoutData(fd_txtFloor);
	formToolkit.adapt(txtFloor, true, true);
	
	Text txtOrderId = new Text(ordMenu, SWT.BORDER);
	fd_tree.right = new FormAttachment(100, -260);
	txtOrderId.setText("Order ID");
	FormData fd_txtOrderId = new FormData();
	fd_txtOrderId.top = new FormAttachment(0, 61);
	fd_txtOrderId.right = new FormAttachment(100, -156);
	fd_txtOrderId.left = new FormAttachment(tree, 17);
	txtOrderId.setLayoutData(fd_txtOrderId);
	formToolkit.adapt(txtOrderId, true, true);
	
	Button button = new Button(ordMenu, SWT.NONE);
	FormData fd_button = new FormData();
	fd_button.left = new FormAttachment(tree, 17);
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
	FormData fd_lblChair = new FormData();
	fd_lblChair.top = new FormAttachment(lblTable, 0, SWT.TOP);
	fd_lblChair.left = new FormAttachment(txtChair, 0, SWT.LEFT);
	lblChair.setLayoutData(fd_lblChair);
    lblChair.setText("Chair: ");
    
    Label lblWaiter = new Label(ordMenu, SWT.NONE);
    FormData fd_lblWaiter = new FormData();
    fd_lblWaiter.top = new FormAttachment(lblTable, 0, SWT.TOP);
    fd_lblWaiter.left = new FormAttachment(txtWaiter, 0, SWT.LEFT);
    lblWaiter.setLayoutData(fd_lblWaiter);
    lblWaiter.setText("Waiter: ");
    
    Label lblFloor = new Label(ordMenu, SWT.NONE);
    FormData fd_lblFloor = new FormData();
    fd_lblFloor.top = new FormAttachment(lblTable, 0, SWT.TOP);
    fd_lblFloor.left = new FormAttachment(txtFloor, 0, SWT.LEFT);
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
    fd_btnSelectOrderId.left = new FormAttachment(tree, 17);
    fd_btnSelectOrderId.top = new FormAttachment(txtOrderId, 6);
    btnSelectOrderId.setLayoutData(fd_btnSelectOrderId);
    formToolkit.adapt(btnSelectOrderId, true, true);
    btnSelectOrderId.setText("Select Order ID");
    
    Button btnNewOrderId = new Button(ordMenu, SWT.NONE);
    FormData fd_btnNewOrderId = new FormData();
    fd_btnNewOrderId.right = new FormAttachment(100, -156);
    fd_btnNewOrderId.left = new FormAttachment(tree, 17);
    fd_btnNewOrderId.top = new FormAttachment(button, 16);
    btnNewOrderId.setLayoutData(fd_btnNewOrderId);
    formToolkit.adapt(btnNewOrderId, true, true);
    btnNewOrderId.setText("New Order ID");
    
    Label lblSeparator1 = new Label(ordMenu, SWT.SEPARATOR | SWT.VERTICAL);
    FormData fd_lblSeparator1 = new FormData();
    fd_lblSeparator1.left = new FormAttachment(txtOrderId, 3);
    fd_lblSeparator1.height = 1000;
    fd_lblSeparator1.top = new FormAttachment(0);
    lblSeparator1.setLayoutData(fd_lblSeparator1);
    formToolkit.adapt(lblSeparator1, true, true);

    dropdown = new OrderingMenuDropdown(ordMenu, SWT.FLAT | SWT.LEFT);
    FormData fd_ordMenuDropdown = new FormData();
    fd_ordMenuDropdown.top = new FormAttachment(txtTable, 0, SWT.TOP);
    fd_ordMenuDropdown.left = new FormAttachment(lblDDMenu, 0, SWT.LEFT);
    fd_ordMenuDropdown.width = 100;
    dropdown.setLayoutData(fd_ordMenuDropdown);
    formToolkit.adapt(dropdown);
    formToolkit.paintBordersFor(dropdown);
    dropdown.init(dropdown);
    
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
	
	public void getTreeItems(Node xmlNode, GMTreeItem tree) {
				
		if (xmlNode.getNodeName().contains("#"))
			return;
		if (xmlNode.getNodeName() != "menues") {
			GMTreeItem newNode = new GMTreeItem(tree, SWT.NONE);
			NamedNodeMap attributes = xmlNode.getAttributes();
		 
			if(attributes != null) {
				for(int k = 0; k<attributes.getLength(); ++k) {
					String name = attributes.item(k).getNodeName();
					String value = attributes.item(k).getNodeValue();
				
					newNode.m_attributes.put(name,  value);
					newNode.m_attributes.putIfAbsent("uuid", assignUUID()); //if it's already assigned, do not assign
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
