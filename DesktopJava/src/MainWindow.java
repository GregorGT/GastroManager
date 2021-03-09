import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.internal.databinding.conversion.IntegerToStringConverter;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.FileDialog;
import java.awt.Component;
import java.awt.MouseInfo;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

//import javax.swing.tree.DefaultTreeModel;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.*;
import org.xml.sax.*;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import java.util.function.Consumer;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;

public class MainWindow {
	private DataBindingContext m_bindingContext;

	protected Shell shell;
	private SashForm mainSashForm;
	private TabFolder tabFolder;
	private TabItem tbtmView;
	private TabItem tbtmNewItem;
	protected Document selected;
	private Composite parentComposite;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtHeight;
	private Text txtWidth;
	private Button btnButton;
	public int nHeight, nWidth, buttonCount, nDrillDownHeight, nDrillDownWidth, drillDownPosX = 0, drillDownPosY = 100;
	private Text txtHeight_1;
	private Text txtWidth_1;
	private Text txtText;
	private Text text_1;
	public String buttonName, drillDownMenuName;
	private Composite composite;
	private Composite composite_2;
	private Text text;
	private Text text_2;
	private Text text_3;
	private Text text_4;
	private Text text_5;
	private Text text_6;
	private Button btnAddDrillDown;
	private Composite composite_1;
	private MenuItem mntmExpandAll;
	private Tree root;
	private Group currentActive;
	private TabItem tbtmOrdering;
	private Composite composite_3;
	private Text txtTable;
	private Text txtChair;
	private Text txtWaiter;
	private Text txtFloor;
	private Text txtOrderId;
	private Button btnSelectOrderId;
	private Button btnNewOrderId;
	private Label lblAa;

	private Label lblSeparator1;
	private Text text_7;
	private Button btnSelectMenuId;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.open();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static Document loadXMLFromString(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

	public static String readFileToString(String path, Charset encoding) throws IOException {
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			return new String(encoded, encoding);
		}
		
	void treeBuild(GMTreeItem rootNode, Node xmlNode) {
		if (xmlNode.getNodeName().contains("#"))
		return;
	 
		GMTreeItem newNode = new GMTreeItem(rootNode, SWT.NONE);
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
		 treeBuild(newNode, children.item(i));
	 	}
	}
	
	void parseXmlDocument(Document doc, GMTreeItem root) {
		try {
		    	treeBuild(root, doc.getFirstChild());
		} catch (Exception e) {
		    	e.printStackTrace();
		    }
	}
			
	String writeTreeIntoString(Tree treeIn, GMTreeItem treeItem) {
		
		TreeItem node[] = treeItem.getItems();
		String result = "";
		
		result += "<" + treeItem.m_xmlname + " ";		
		
		Iterator it = treeItem.m_attributes.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
	        result += " " + pair.getKey() + "=" + "\"" + pair.getValue() + "\"";
	    }
				    
	    if (node.length > 0 || treeItem.m_value.length() > 0) {
	    	result += ">"; 
	    	} else {
	    	result += "/>";
	    	}
	    if (node.length == 0 && treeItem.m_value.length() > 0) {
	    	result += treeItem.m_value;
	    }
	    
		for (int i = 0; i < node.length; ++i) {
			
			if (node[i] instanceof GMTreeItem) {
				GMTreeItem newItem = (GMTreeItem) node[i];
				result += writeTreeIntoString(treeIn, newItem);
				}
			}			
		
		if (node.length > 0 || treeItem.m_value.length() > 0) {
			result += "</" + treeItem.m_xmlname + ">";
		}
		return result;
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
//		Display shellDisplay = new Display();
		shell = new Shell();
		shell.setSize(800, 750);
		shell.setText("GastroManager");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmLoadFile = new MenuItem(menu, SWT.CASCADE);
		mntmLoadFile.setText("File");
		
		Menu menu_1 = new Menu(mntmLoadFile);
		mntmLoadFile.setMenu(menu_1);
		
		parentComposite = new Composite(shell, SWT.NONE);
		parentComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		mainSashForm = new SashForm(parentComposite, SWT.NONE);
		
		root = new Tree(mainSashForm, SWT.BORDER);
		
		GMTreeItem trtmRoot = new GMTreeItem(root, SWT.NONE);
		trtmRoot.setText("root");
		trtmRoot.addListener(SWT.MouseDoubleClick, new Listener() {
			 public void handleEvent(Event event) {
				try {
				
				} catch (Exception e) {
					System.out.println("!!");
				}
			}
		});
		
		//Menu menu_3 = new Menu(root);
		RClickMenu treeMenu = new RClickMenu(root);
		root.setMenu(treeMenu);
		treeMenu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
//				GMTreeItem selItem = (GMTreeItem) trtmRoot.getItem(0);
				EditDialog d = new EditDialog(shell);
				treeMenu.openTreeMenu( treeMenu, root, d);
			}
		});
		
		
		tabFolder = new TabFolder(mainSashForm, SWT.NONE);
		
		tbtmView = new TabItem(tabFolder, SWT.NONE);
		tbtmView.setText("View");
		
		tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("Layout");
		
		TabItem tbtmDrillDownMenu = new TabItem(tabFolder, SWT.NONE);
		tbtmDrillDownMenu.setText("Drill Down Menu");
	
		composite = new Composite(tabFolder, SWT.NONE);
		tbtmDrillDownMenu.setControl(composite);
		formToolkit.adapt(composite);
		formToolkit.paintBordersFor(composite);
		composite.setLayout(null);		
		
		composite_2 = new Composite(composite, SWT.NONE);
		composite_2.setBounds(0, 41, 300, 42);
		formToolkit.adapt(composite_2);
		formToolkit.paintBordersFor(composite_2);
		
		RClickMenu drillDownMenu = new RClickMenu(composite);
		MenuItem ddmenuItem = new MenuItem(drillDownMenu, SWT.None);
		composite.setMenu(drillDownMenu);
		drillDownMenu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
				if(currentActive != null) {
				EditDialog d = new EditDialog(shell);
				drillDownMenu.openDrillDownMenu(drillDownMenu, d, currentActive);
				}
			}
		});
		
		text = new Text(composite_2, SWT.READ_ONLY | SWT.CENTER);
		text.setText("Height");
		text.setBounds(0, 0, 50, 18);
		formToolkit.adapt(text, true, true);
		
		text_2 = new Text(composite_2, SWT.READ_ONLY | SWT.CENTER);
		text_2.setText("Width");
		text_2.setBounds(100, 0, 50, 18);
		formToolkit.adapt(text_2, true, true);
		
		text_3 = new Text(composite_2, SWT.READ_ONLY | SWT.CENTER);
		text_3.setText("Text");
		text_3.setBounds(0, 20, 50, 20);
		formToolkit.adapt(text_3, true, true);
		
		txtHeight = new Text(composite_2, SWT.BORDER);
		txtHeight.setBounds(50, 0, 50, 18);
		txtHeight.addListener(SWT.Modify, new Listener() {
		      public void handleEvent(Event event) {
		        try {
				String btnHeight = txtHeight.getText();
				nHeight = Integer.parseInt(btnHeight);
				} catch (Exception e){
				
				  }
			    }
		 });

		formToolkit.adapt(txtHeight, true, true);
		
		txtWidth = new Text(composite_2, SWT.BORDER);
		txtWidth.setBounds(150, 0, 50, 18);
		txtWidth.addListener(SWT.Modify, new Listener() {
			 public void handleEvent(Event event) {
				try {
					String btnWidth = txtWidth.getText();
					nWidth = Integer.parseInt(btnWidth);
				} catch (Exception e) {
					
				}
			}
		});
		formToolkit.adapt(txtWidth, true, true);
		
		text_1 = new Text(composite_2, SWT.BORDER);
		text_1.setBounds(50, 20, 150, 20);
		text_1.addListener(SWT.Modify, new Listener() {
			 public void handleEvent(Event event) {
				try {
					buttonName = text_1.getText();
				} catch (Exception e) {
					
				}
			}
		});
		formToolkit.adapt(text_1, true, true);
		
		Button btnAddButton = new Button(composite_2, SWT.CENTER);
		btnAddButton.setBounds(200, 0, 100, 42);
		btnAddButton.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
				addNewButton(nHeight, nWidth, buttonCount, buttonName);
				putNewItemIntoTree(trtmRoot, buttonName, currentActive.getText(), nHeight, nWidth);
				
				buttonCount++;
				composite.layout(true);
			}
		});
		
		formToolkit.adapt(btnAddButton, true, true);
		btnAddButton.setText("Add Button");
		
		composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setBounds(0, 0, 300, 42);
		formToolkit.adapt(composite_1);
		formToolkit.paintBordersFor(composite_1);
		
		txtHeight_1 = new Text(composite_1, SWT.READ_ONLY | SWT.CENTER);
		txtHeight_1.setBounds(0, 0, 50, 18);
		txtHeight_1.setText("Height");
		formToolkit.adapt(txtHeight_1, true, true);
		
		txtWidth_1 = new Text(composite_1, SWT.READ_ONLY | SWT.CENTER);
		txtWidth_1.setBounds(100, 0, 50, 18);
		txtWidth_1.setText("Width");
		formToolkit.adapt(txtWidth_1, true, true);
		
		txtText = new Text(composite_1, SWT.READ_ONLY | SWT.CENTER);
		txtText.setBounds(0, 20, 50, 20);
		txtText.setText("Text");
		formToolkit.adapt(txtText, true, true);
		
		btnAddDrillDown = new Button(composite_1, SWT.CENTER);
		btnAddDrillDown.setBounds(202, 0, 98, 42);
		btnAddDrillDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				//Add Drill Down menu to tree and represent as button on composite
				addNewDrillDown(nDrillDownHeight, nDrillDownWidth, drillDownMenuName);
				//trtmRoot.m_attributes.put("height", nDrillDownHeight);
				
				putNewItemIntoTree(trtmRoot, drillDownMenuName, "drilldownmenus", nDrillDownHeight, nDrillDownWidth);
				
				System.out.println(trtmRoot.m_attributes.get("name"));
				composite_2.redraw();
			}
		});
		btnAddDrillDown.setText("Add Drill Down Menu");
		formToolkit.adapt(btnAddDrillDown, true, true);
		
		text_5 = new Text(composite_1, SWT.BORDER);
		text_5.setBounds(150, 0, 50, 18);
		text_5.addListener(SWT.Modify, new Listener() {
			 public void handleEvent(Event event) {
				try {
				String drillDownMenuWidth = text_5.getText();
				nDrillDownWidth = Integer.parseInt(drillDownMenuWidth);
				} catch (Exception e) {
					
				}
			}
		});
		formToolkit.adapt(text_5, true, true);
		
		text_4 = new Text(composite_1, SWT.BORDER);
		text_4.setBounds(50, 0, 50, 18);
		text_4.addListener(SWT.Modify, new Listener() {
			 public void handleEvent(Event event) {
				try {
				String drillDownMenuHeight = text_4.getText();
				nDrillDownHeight = Integer.parseInt(drillDownMenuHeight);
				} catch (Exception e) {
					
				}
			}
		});
		formToolkit.adapt(text_4, true, true);
		
		text_6 = new Text(composite_1, SWT.BORDER);
		text_6.setBounds(50, 18, 150, 20);
		text_6.addListener(SWT.Modify, new Listener() {
			 public void handleEvent(Event event) {
				try {
				drillDownMenuName = text_6.getText();
				} catch (Exception e) {
					
				}
			}
		});
		formToolkit.adapt(text_6, true, true);
		
//		Menu menu_4 = new Menu(composite);
//		composite.setMenu(menu_4);
		
		Button btnClearAll = new Button(composite, SWT.NONE);
		btnClearAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				//delete all buttons/groups
				deleteAllGroupsandButtons();
				
			}
		});
		btnClearAll.setBounds(312, 0, 64, 83);
		formToolkit.adapt(btnClearAll, true, true);
		btnClearAll.setText("Clear All");
				
		tbtmOrdering = new TabItem(tabFolder, SWT.NONE);
		tbtmOrdering.setText("Ordering");
		
		composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmOrdering.setControl(composite_3);
		formToolkit.paintBordersFor(composite_3);
		composite_3.setLayout(new FormLayout());
		Label lblTable= new Label(composite_3, SWT.NONE);
		FormData fd_lblTable = new FormData();
		fd_lblTable.left = new FormAttachment(0, 10);
		lblTable.setLayoutData(fd_lblTable);
	    lblTable.setText("Table: ");
		
		txtTable = new Text(composite_3, SWT.BORDER);
		fd_lblTable.bottom = new FormAttachment(txtTable, -6);
		fd_lblTable.bottom = new FormAttachment(txtTable, -6);
		txtTable.setEditable(false);
		txtTable.setText("Table");
		FormData fd_txtTable = new FormData();
		fd_txtTable.left = new FormAttachment(0, 10);
		fd_txtTable.top = new FormAttachment(0, 36);
		txtTable.setLayoutData(fd_txtTable);
		formToolkit.adapt(txtTable, true, true);
		
		Tree tree = new Tree(composite_3, SWT.BORDER);
		fd_txtTable.bottom = new FormAttachment(100, -623);
		FormData fd_tree = new FormData();
		fd_tree.left = new FormAttachment(0, 10);
		fd_tree.top = new FormAttachment(txtTable, 6);
		fd_tree.bottom = new FormAttachment(100, -213);
		tree.setLayoutData(fd_tree);
		formToolkit.adapt(tree);
		formToolkit.paintBordersFor(tree);
		
		txtChair = new Text(composite_3, SWT.BORDER);
		fd_txtTable.right = new FormAttachment(100, -430);
		txtChair.setText("Chair");
		FormData fd_txtChair = new FormData();
		fd_txtChair.left = new FormAttachment(txtTable, 11);
		fd_txtChair.bottom = new FormAttachment(txtTable, 0, SWT.BOTTOM);
		fd_txtChair.top = new FormAttachment(txtTable, 0, SWT.TOP);
		txtChair.setLayoutData(fd_txtChair);
		formToolkit.adapt(txtChair, true, true);
		
		txtWaiter = new Text(composite_3, SWT.BORDER);
		fd_txtChair.right = new FormAttachment(100, -365);
		txtWaiter.setText("Waiter");
		FormData fd_txtWaiter = new FormData();
		fd_txtWaiter.right = new FormAttachment(txtChair, 60, SWT.RIGHT);
		fd_txtWaiter.top = new FormAttachment(txtTable, 0, SWT.TOP);
		fd_txtWaiter.left = new FormAttachment(txtChair, 6);
		txtWaiter.setLayoutData(fd_txtWaiter);
		formToolkit.adapt(txtWaiter, true, true);
		
		txtFloor = new Text(composite_3, SWT.BORDER);
		txtFloor.setText("Floor");
		FormData fd_txtFloor = new FormData();
		fd_txtFloor.top = new FormAttachment(txtTable, 0, SWT.TOP);
		fd_txtFloor.right = new FormAttachment(100, -260);
		txtFloor.setLayoutData(fd_txtFloor);
		formToolkit.adapt(txtFloor, true, true);
		
		txtOrderId = new Text(composite_3, SWT.BORDER);
		fd_tree.right = new FormAttachment(100, -260);
		txtOrderId.setText("Order ID");
		FormData fd_txtOrderId = new FormData();
		fd_txtOrderId.top = new FormAttachment(0, 61);
		fd_txtOrderId.right = new FormAttachment(100, -156);
		fd_txtOrderId.left = new FormAttachment(tree, 17);
		txtOrderId.setLayoutData(fd_txtOrderId);
		formToolkit.adapt(txtOrderId, true, true);
		
		Button button = new Button(composite_3, SWT.NONE);
		FormData fd_button = new FormData();
		fd_button.left = new FormAttachment(tree, 17);
		fd_button.top = new FormAttachment(txtOrderId, 31);
		button.setLayoutData(fd_button);
		formToolkit.adapt(button, true, true);
		button.setText("<<");
		
		Button button_1 = new Button(composite_3, SWT.NONE);
		FormData fd_button_1 = new FormData();
		fd_button_1.left = new FormAttachment(button, 31);
		fd_button_1.top = new FormAttachment(button, 0, SWT.TOP);
		button_1.setLayoutData(fd_button_1);
		formToolkit.adapt(button_1, true, true);
		button_1.setText(">>");
		
		Label lblChair = new Label(composite_3, SWT.NONE);
		FormData fd_lblChair = new FormData();
		fd_lblChair.top = new FormAttachment(lblTable, 0, SWT.TOP);
		fd_lblChair.left = new FormAttachment(txtChair, 0, SWT.LEFT);
		lblChair.setLayoutData(fd_lblChair);
        lblChair.setText("Chair: ");
        
        Label lblWaiter = new Label(composite_3, SWT.NONE);
        FormData fd_lblWaiter = new FormData();
        fd_lblWaiter.top = new FormAttachment(lblTable, 0, SWT.TOP);
        fd_lblWaiter.left = new FormAttachment(txtWaiter, 0, SWT.LEFT);
        lblWaiter.setLayoutData(fd_lblWaiter);
        lblWaiter.setText("Waiter: ");
        
        Label lblFloor = new Label(composite_3, SWT.NONE);
        FormData fd_lblFloor = new FormData();
        fd_lblFloor.top = new FormAttachment(lblTable, 0, SWT.TOP);
        fd_lblFloor.left = new FormAttachment(txtFloor, 0, SWT.LEFT);
        lblFloor.setLayoutData(fd_lblFloor);
        lblFloor.setText("Floor: ");
		
        Label lblDDMenu = new Label(composite_3, SWT.NONE);
        FormData fd_lblDDMenu = new FormData();
        fd_lblDDMenu.top = new FormAttachment(lblTable, 0, SWT.TOP);
        fd_lblDDMenu.right = new FormAttachment(100, -51);
        lblDDMenu.setLayoutData(fd_lblDDMenu);
        lblDDMenu.setText("Drill Down Options: ");
        
        btnSelectOrderId = new Button(composite_3, SWT.NONE);
        FormData fd_btnSelectOrderId = new FormData();
        fd_btnSelectOrderId.left = new FormAttachment(tree, 17);
        fd_btnSelectOrderId.top = new FormAttachment(txtOrderId, 6);
        btnSelectOrderId.setLayoutData(fd_btnSelectOrderId);
        formToolkit.adapt(btnSelectOrderId, true, true);
        btnSelectOrderId.setText("Select Order ID");
        
        btnNewOrderId = new Button(composite_3, SWT.NONE);
        FormData fd_btnNewOrderId = new FormData();
        fd_btnNewOrderId.right = new FormAttachment(100, -156);
        fd_btnNewOrderId.left = new FormAttachment(tree, 17);
        fd_btnNewOrderId.top = new FormAttachment(button, 16);
        btnNewOrderId.setLayoutData(fd_btnNewOrderId);
        formToolkit.adapt(btnNewOrderId, true, true);
        btnNewOrderId.setText("New Order ID");
        
        lblSeparator1 = new Label(composite_3, SWT.SEPARATOR | SWT.VERTICAL);
//        lblSeparator1.setText("1");
        FormData fd_lblSeparator1 = new FormData();
        fd_lblSeparator1.left = new FormAttachment(txtOrderId, 3);
        fd_lblSeparator1.height = 1000;
        fd_lblSeparator1.top = new FormAttachment(0);
        lblSeparator1.setLayoutData(fd_lblSeparator1);
        formToolkit.adapt(lblSeparator1, true, true);
        
        ToolBar toolBar = new ToolBar(composite_3, SWT.FLAT | SWT.RIGHT);
        FormData fd_toolBar = new FormData();
        fd_toolBar.top = new FormAttachment(txtTable, 0, SWT.TOP);
        fd_toolBar.left = new FormAttachment(lblDDMenu, 0, SWT.LEFT);
        toolBar.setLayoutData(fd_toolBar);
        formToolkit.adapt(toolBar);
        formToolkit.paintBordersFor(toolBar);
        
        ToolItem tltmDropdownItem = new ToolItem(toolBar, SWT.DROP_DOWN);
        tltmDropdownItem.setText("DropDown Item");
        
        text_7 = new Text(composite_3, SWT.BORDER);
        FormData fd_text_7 = new FormData();
        fd_text_7.top = new FormAttachment(btnSelectOrderId, 0, SWT.TOP);
        fd_text_7.left = new FormAttachment(lblDDMenu, 0, SWT.LEFT);
        text_7.setLayoutData(fd_text_7);
        formToolkit.adapt(text_7, true, true);
        
        Label lblMenuID = new Label(composite_3, SWT.NONE);
        FormData fd_lblMenuID = new FormData();
        fd_lblMenuID.bottom = new FormAttachment(txtOrderId, 0, SWT.BOTTOM);
        fd_lblMenuID.left = new FormAttachment(lblDDMenu, 0, SWT.LEFT);
        lblMenuID.setLayoutData(fd_lblMenuID);
        lblMenuID.setText("Menu ID");
        
        btnSelectMenuId = new Button(composite_3, SWT.NONE);
        FormData fd_btnSelectMenuId = new FormData();
        fd_btnSelectMenuId.right = new FormAttachment(lblDDMenu, 84);
        fd_btnSelectMenuId.left = new FormAttachment(lblDDMenu, 0, SWT.LEFT);
        fd_btnSelectMenuId.top = new FormAttachment(text_7, 6);
        btnSelectMenuId.setLayoutData(fd_btnSelectMenuId);
        formToolkit.adapt(btnSelectMenuId, true, true);
        btnSelectMenuId.setText("Select Menu ID");
        
		
//		Group grpExamplegrp = new Group(composite, SWT.NONE);
//		grpExamplegrp.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseDown(MouseEvent e) {
//				Point origLocation = new Point(grpExamplegrp.getBounds().x, grpExamplegrp.getBounds().y);
////				Point cursorLocation = Display.getCurrent().getCursorLocation();
////				Point relativeCursorLocation = Display.getCurrent().getFocusControl().toControl(cursorLocation);
//				dragComponent(origLocation, grpExamplegrp);
//			}
//			@Override
//			public void mouseUp(MouseEvent e) {
////				int newX = Display.getCurrent().getCursorLocation().x;
////				int newY = Display.getCurrent().getCursorLocation().y;
////				Point newLocation = new Point(newX, newY);  //  new Point(newX, newY);
//				Point cursorLocation = Display.getCurrent().getCursorLocation();
//				Point relativeCursorLocation = Display.getCurrent().getFocusControl().toControl(cursorLocation);
//				dropComponent(relativeCursorLocation, grpExamplegrp);
//			}
//		});
//		grpExamplegrp.setText("examplegrp");
//		grpExamplegrp.setBounds(100, 100, 70, 80);
//		formToolkit.adapt(grpExamplegrp);
//		formToolkit.paintBordersFor(grpExamplegrp);
		
		mainSashForm.setWeights(new int[] {1, 2});
		
		MenuItem mntmOpenFile = new MenuItem(menu_1, SWT.NONE);
		mntmOpenFile.addSelectionListener(new SelectionAdapter() {			
			@Override
			public void widgetSelected(SelectionEvent e) {
				 	FileDialog fd = new FileDialog(shell, SWT.OPEN);
			        fd.setText("Open XML file");
			        fd.setFilterPath("C:\\");
			        String[] filterExt = { "*.XML", "*.xml"};
			        fd.setFilterExtensions(filterExt);
			        String selected = fd.open();
			        Document doc;
			        try {
					
			        String fstr = readFileToString(selected, Charset.defaultCharset());
					doc = loadXMLFromString(fstr);
					parseXmlDocument(doc, trtmRoot);
					
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			        
			 }
			
		});
		mntmOpenFile.setText("Open File");
		
		MenuItem mntmSave = new MenuItem(menu_1, SWT.NONE);
		mntmSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
	            String newString = writeTreeIntoString(root, trtmRoot);

				File saveFile = new File("c:\\saved_sample_template.xml");
				
				try {
						FileWriter fileWriter = new FileWriter(saveFile);
						fileWriter.write(newString);
						fileWriter.flush();
						fileWriter.close();
				} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
				System.out.println(newString);
			}
		});
		mntmSave.setText("Save");
		
		MenuItem mntmAbout = new MenuItem(menu, SWT.CASCADE);
		mntmAbout.setText("About");
		
		Menu menu_2 = new Menu(mntmAbout);
		mntmAbout.setMenu(menu_2);

		m_bindingContext = initDataBindings();

	}
	

	void dragComponent(Point point, Group grp) {
		Point origLocation = point; 
		System.out.println("CLICK   " + origLocation.x + " x - "+ origLocation.y + " y" );	
	}

	void dropComponent(Point point, Group grp) {
		grp.setBounds(point.x, point.y, grp.getBounds().width, grp.getBounds().height);
		System.out.println("UNCLICK " + point.x + " x - "+ point.y + " y" );	
	}
	
	
	public void addNewButton(int height, int width, int buttonCount, String name) {
		
		Button newButton = new Button(this.currentActive, SWT.PUSH);
		formToolkit.adapt(newButton, true, true);
		newButton.setText(name);
		if (buttonCount == 0) {
			newButton.setBounds(4, 14, width, height);
		} else {
			int lastButtonHeight = height + 10;
			newButton.setBounds(4, lastButtonHeight, width, height);
		}
	
	}
	
	
	public void addNewDrillDown(int height, int width, String name) {
		
		currentActive = new Group(this.composite, SWT.NONE);
		currentActive.setText(name);
		currentActive.setBounds(composite.getBounds().x, composite.getBounds().y + 60, width, height);
		//formToolkit.adapt(name);
		formToolkit.paintBordersFor(currentActive);
		currentActive.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				makeActive(currentActive);
				Point origLocation = new Point(currentActive.getBounds().x, currentActive.getBounds().y);
				dragComponent(origLocation, currentActive);
			}
			@Override
			public void mouseUp(MouseEvent e) {
				Point cursorLocation = Display.getCurrent().getCursorLocation();
//				composite.isFocusControl();
				Point relativeCursorLocation = Display.getCurrent().getFocusControl().toControl(cursorLocation);
				dropComponent(relativeCursorLocation, currentActive);
			}
		});
	}
	
	void putNewItemIntoTree(GMTreeItem treeItem, String newName, String parent, int height, int width) {
		
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
				putNewItemIntoTree(newItem, newName, parent, height, width);
			}
		}
	}

	void makeActive(Group grp) { 
		grp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				Group grp = currentActive;
				System.out.println(grp.getText() + " is now active");
			}
		});
	}


	String assignUUID() {
		 Random rd = new Random(); // creating Random object
	     String uuid = String.valueOf(rd.nextLong());
	     return uuid;
	}
	
	void deleteAllGroupsandButtons() {
		
	}
	
	String writeToString(Tree treein, GMTreeItem treeitem) {
		String result = "";
		Control[] kids = treein.getChildren();
		
		int ic = treeitem.getItemCount();
		TreeItem its[] = treeitem.getItems();
		
		for(int j = 0; j<its.length;++j) {
			TreeItem test[] = its[j].getItems();
			int testing = 0;
		}
		
		for(int i = 0; i<kids.length;++i) {
			if(kids[i] instanceof Element) {
				Element kid = (Element)kids[i];
				int di = 0;
			}
		}
		return result;
	}
	
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		return bindingContext;
	}
}
