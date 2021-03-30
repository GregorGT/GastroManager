import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

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
	public String buttonName, drillDownMenuName;
	private Tree root;
	public DrillDownGroup activeDrillDownGroup;
	private TabItem tbtmOrdering;
	public OrderingMenu orderingMenu;

	
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
		 treeBuild(newNode, children.item(i));
		 newNode.setExpanded(true);
	 	}
	}
	
	void parseXmlDocument(Document doc, GMTreeItem root) {
		try {
		    	treeBuild(root, doc.getFirstChild());
		    	orderingMenu.getTreeItems(doc.getFirstChild(), orderingMenu.m_gmTree);
		} catch (Exception e) {
		    	e.printStackTrace();
		    }
	}
			
	String writeTreeIntoString(Tree treeIn, GMTreeItem treeItem) {
		
		TreeItem node[] = treeItem.getItems();
		String result = "";
		
		result += "<" + treeItem.m_xmlname;
		
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
		trtmRoot.setExpanded(true);
		
		RClickMenu treeMenu = new RClickMenu(root);
		root.setMenu(treeMenu);
		treeMenu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
				EditDialog d = new EditDialog(shell);
				treeMenu.openTreeMenu( treeMenu, root, d);
			}
		});
		
		
		tabFolder = new TabFolder(mainSashForm, SWT.NONE);
		
		tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("Layout");
		
		TabItem tbtmDrillDownMenu = new TabItem(tabFolder, SWT.NONE);
		tbtmDrillDownMenu.setText("Drill Down Menu");
	
		DrillDownMenu composite = new DrillDownMenu(tabFolder, SWT.NONE);
		tbtmDrillDownMenu.setControl(composite);
		formToolkit.adapt(composite);
		formToolkit.paintBordersFor(composite);
		composite.setLayout(new FormLayout());
		composite.init(composite, trtmRoot);
		
		tbtmOrdering = new TabItem(tabFolder, SWT.NONE);
		tbtmOrdering.setText("Ordering");
		
		orderingMenu = new OrderingMenu(tabFolder, SWT.None);
		tbtmOrdering.setControl(orderingMenu);
		formToolkit.paintBordersFor(orderingMenu);
		orderingMenu.setLayout(new FormLayout());
		orderingMenu.init(orderingMenu);
		
		tbtmView = new TabItem(tabFolder, SWT.NONE);
		tbtmView.setText("View");
		
		
		//test
		
		{	
		}
		
        //endtest		
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
