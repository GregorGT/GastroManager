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
import org.eclipse.swt.widgets.FileDialog;

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
import java.util.UUID;

import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.*;
import org.xml.sax.*;
import org.eclipse.ui.forms.widgets.FormToolkit;
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
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.Form;

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
	public int nHeight, nWidth, buttonCount, nDrillDownHeight, nDrillDownWidth;
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
	private Group grpNewDrillDownGroup;
	private Composite grpNewDrillDownGroupComp;

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
	
	public static Document loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

	public static String readFileToString(String path, Charset encoding)
			  throws IOException
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}
		
	void treeBuild(GMTreeItem rootNode, Node xmlNode) {
		
	 if (xmlNode.getNodeName().contains("#"))
		return;
	 
	 GMTreeItem newNode = new GMTreeItem(rootNode, SWT.NONE);
	 
	 NamedNodeMap attributes = xmlNode.getAttributes();
	 
	 if(attributes != null)
	 {
		 for(int k = 0; k<attributes.getLength(); ++k)
		 {
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
		 if (xmlNode.getFirstChild().hasChildNodes() == false)
		 {
			 String nodeName = xmlNode.getFirstChild().getNodeName();
			 
			 if (nodeName == "#text") {
				 newNode.m_value = xmlNode.getFirstChild().getTextContent(); 
			 }
		 }
	 }
	 
	 if(xmlNode.getNodeValue() != null && xmlNode.getNodeValue().length() > 0)
		
	 newNode.m_value = xmlNode.getNodeValue();
	 newNode.m_xmlname = xmlNode.getNodeName();
	
	 newNode.setText( newNode.getDisplayString() );
	  
	 NodeList children = xmlNode.getChildNodes();
	 
	 
	 for (int i = 0; i < children.getLength(); ++i) {
		 treeBuild(newNode, children.item(i));
		 newNode.setExpanded(true);
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
		String result = "<" + treeItem.m_xmlname + " ";		
		
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
		
		Menu menu_3 = new Menu(root);
		root.setMenu(menu_3);
		menu_3.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
				MenuItem[] items = menu_3.getItems();
				for (int i = 0; i < items.length; ++i) {
					items[i].dispose();
				}
				
				MenuItem newItem = new MenuItem(menu_3, SWT.NONE);
				newItem.setText("Menu for " + root.getSelection()[0].getText());
				MenuItem mntmEditValue = new MenuItem(menu_3, SWT.NONE);
				mntmEditValue.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						//editSelectedNodeValue();
						
						
					}
				});
				mntmEditValue.setText("Edit Value");
				
				MenuItem mntmEditName = new MenuItem(menu_3, SWT.NONE);
				mntmEditName.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						//editSelectedNodeName();
					}
				});
				mntmEditName.setText("Edit Name");
				
				mntmExpandAll = new MenuItem(menu_3, SWT.NONE);
				mntmExpandAll.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						
						expandAll(root);
						
					}
				});
				mntmExpandAll.setText("Expand All");
				
				//MenuItem mntmDeleteNode = new MenuItem(menu_3, SWT.NONE);
				MenuItem mntmDeleteNode = new MenuItem(menu_3, SWT.NONE);
				mntmDeleteNode.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						
						//deleteSelectedNode();
						trtmRoot.dispose();
						
					}
				});
				mntmDeleteNode.setText("Delete Node");
				
				
				MenuItem mntmAddNewNode = new MenuItem(menu_3, SWT.NONE);
				mntmAddNewNode.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						
						//AddNewNode();
						
					}
				});
				mntmAddNewNode.setText("Add new Node");
				
				MenuItem mntmSetPrice = new MenuItem(menu_3, SWT.NONE);
				mntmSetPrice.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						
						//setPrice();
						
					}
				});
				mntmSetPrice.setText("Set Price");
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
		txtHeight.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				
				if (txtHeight.getText() != null) {
				String btnHeight = txtHeight.getText();
				nHeight = Integer.parseInt(btnHeight);
				}
				
			}
		});
		formToolkit.adapt(txtHeight, true, true);
		
		txtWidth = new Text(composite_2, SWT.BORDER);
		txtWidth.setBounds(150, 0, 50, 18);
		txtWidth.addModifyListener(new ModifyListener() {
			
			public void modifyText(ModifyEvent arg0) {
				if (txtHeight.getText() != null) {
					String btnWidth = txtWidth.getText();
					nWidth = Integer.parseInt(btnWidth);
					}
			}
		});
		formToolkit.adapt(txtWidth, true, true);
		
		text_1 = new Text(composite_2, SWT.BORDER);
		text_1.setBounds(50, 20, 150, 20);
		text_1.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
					buttonName = text_1.getText();
			}
		});
		formToolkit.adapt(text_1, true, true);
		
		Button btnAddButton = new Button(composite_2, SWT.CENTER);
		btnAddButton.setBounds(200, 0, 100, 42);
		btnAddButton.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
				addNewButton(nHeight, nWidth, buttonCount, buttonName);
				putNewItemIntoTree(trtmRoot, buttonName);
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
		btnAddDrillDown.setFont(SWTResourceManager.getFont("Tahoma", 7, SWT.NORMAL));
		btnAddDrillDown.setBounds(202, 0, 98, 42);
		btnAddDrillDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				//Add Drill Down menu to tree and represent as button on composite
				addNewDrillDown(nDrillDownHeight, nDrillDownWidth, drillDownMenuName);
				trtmRoot.m_attributes.put("height", nDrillDownHeight);
				
				putNewItemIntoTree(trtmRoot, drillDownMenuName);
				trtmRoot.m_attributes.putIfAbsent("name", drillDownMenuName);
				System.out.println(trtmRoot.m_attributes.get("name"));
				composite_2.redraw();
			}
		});
		btnAddDrillDown.setText("Add Drill Down Menu");
		formToolkit.adapt(btnAddDrillDown, true, true);
		
		text_5 = new Text(composite_1, SWT.BORDER);
		text_5.setBounds(150, 0, 50, 18);
		text_5.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				
				//Drill Down menu Width
				String drillDownMenuWidth = text_5.getText();
				nDrillDownWidth = Integer.parseInt(drillDownMenuWidth);
			}
		});
		formToolkit.adapt(text_5, true, true);
		
		text_4 = new Text(composite_1, SWT.BORDER);
		text_4.setBounds(50, 0, 50, 18);
		text_4.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				
				//Drill Down menu Height
				String drillDownMenuHeight = text_4.getText();
				nDrillDownHeight = Integer.parseInt(drillDownMenuHeight);
				
			}
		});
		formToolkit.adapt(text_4, true, true);
		
		text_6 = new Text(composite_1, SWT.BORDER);
		text_6.setBounds(50, 18, 150, 20);
		text_6.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				
				//Drill Down Menu Name
				drillDownMenuName = text_6.getText();
			}
		});
		formToolkit.adapt(text_6, true, true);
		
		Menu menu_4 = new Menu(composite);
		composite.setMenu(menu_4);
		
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

	
	void expandAll(Tree root) {
		
		boolean expanded = true;
		TreeItem [] items = root.getItems();
		
		for (TreeItem item : items) {
			item.setExpanded(expanded);
		}
		root.setRedraw(true);
	}
	
	public void addNewButton(int height, int width, int buttonCount, String name) {
		
		Button newButton = new Button(this.grpNewDrillDownGroup, SWT.PUSH);
		formToolkit.adapt(newButton, true, true);
		newButton.setText(name);
		putNewItemIntoTree(null, name);
		if (buttonCount == 0) {
			newButton.setBounds(4, 14, width, height);
		} else {
			int lastButtonHeight = height + 10;
			newButton.setBounds(4, lastButtonHeight, width, height);
		}
	}
	
	
	public void addNewDrillDown(int height, int width, String name) {
		
		grpNewDrillDownGroup = new Group(this.composite, SWT.NONE);
		grpNewDrillDownGroup.setText(name);
		grpNewDrillDownGroup.setBounds(0, 110, width, height);
		//formToolkit.adapt(name);
		formToolkit.paintBordersFor(grpNewDrillDownGroup);
	
		/*
		Button newButton = new Button(this.composite, SWT.PUSH);
		newButton.setAlignment(SWT.LEFT);
		formToolkit.adapt(newButton, true, true);
		newButton.setText(name);
		if (buttonCount == 0) {
			newButton.setBounds(0, 100, width, height);
		} else {
			int lastButtonHeight = height + 10;
			newButton.setBounds(0, lastButtonHeight, width, height);
		}
		*/
		
	}

	void putNewItemIntoTree(GMTreeItem treeItem, String newName) {
		
		TreeItem node[] = treeItem.getItems();
		for (int i = 0; i < node.length; ++i) {
			
			if (node[i] instanceof GMTreeItem) {
				GMTreeItem newItem = (GMTreeItem) node[i];
				if (newItem.m_xmlname == "drilldownmenues")
				{
					GMTreeItem newDrillDownMenu = new GMTreeItem(node[i], SWT.NONE);
					newDrillDownMenu.setText(newName);
					//GMTreeItem newDDItem = new GMTreeItem(newDrillDownMenu, SWT.NONE);
					//newDDItem.setText(newName);
				}
				//System.out.println(newItem.m_xmlname + i);
				putNewItemIntoTree(newItem, newName);
			}
		}
	}
	
	/*
	void putNewButtonItemIntoTree(TreeItem treeItem, String drillDownName, String buttonName) {
		
		TreeItem node[] = treeItem.getItems();
		for (int i = 0 ; i < node.length; ++i) {
			
			if (node[i] instanceof GMTreeItem) {
				GMTreeItem nItem = (GMTreeItem) node[i];
				if (nItem.m_xmlname == drillDownName) {
				GMTreeItem newItem_1 = new GMTreeItem(treeItem, SWT.NONE);
				newItem_1.setText(buttonName);
				}
				putNewButtonItemIntoTree(treeItem, drillDownName, buttonName);
			}
			
		}
		
	}
	*/
	
	String assignUUID() {
		
		final String uuid = UUID.randomUUID().toString().replace("-", "");
		return uuid;
		
	}
	
	void deleteAllGroupsandButtons() {
		
		
		
	}
	
	String writeToString(Tree treein, GMTreeItem treeitem)
	{
		String result = "";
		//TreeItem ti = treeitem.getChildren();
		//child = treein.getChildren();
			
		Control[] kids = treein.getChildren();
		
		int ic = treeitem.getItemCount();
		TreeItem its[] = treeitem.getItems();
		
		for(int j = 0; j<its.length;++j)
		{
			TreeItem test[] = its[j].getItems();
			int testing = 0;
		}
						
		
		for(int i = 0; i<kids.length;++i)
		{
			if(kids[i] instanceof Element)
			{
				Element kid = (Element)kids[i];
				int di = 0;
				
			}
		}
		
		return result;
	}
	
	
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		return bindingContext;
	}
}
