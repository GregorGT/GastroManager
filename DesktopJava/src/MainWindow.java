import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
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
import java.util.Iterator;
import java.util.Map;

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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.List;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class MainWindow {
	private DataBindingContext m_bindingContext;

	protected Shell shell;
	private SashForm mainSashForm;
	private TabFolder tabFolder;
	private TabItem tbtmView;
	private TabItem tbtmNewItem;

	protected Document selected;
	private Composite parentComposite;
	private SashForm drillDownSashForm;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtHeight;
	private Text txtWidth;
	private Composite composite_1;
	private Button btnButton;

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
		
		//GMTreeItem neItem = new GMTreeItem(root, SWT.NONE)
		TreeItem node[] = treeItem.getItems();
		String result = "<" + treeItem.m_xmlname + " "; //treeItem.m_attributes + ">";
		
		
		Iterator it = treeItem.m_attributes.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        result += pair.getKey() + "=" + "\"" + pair.getValue() + "\" ";
	        // it.remove(); // avoids a ConcurrentModificationException
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
		shell.setSize(475, 350);
		shell.setText("GastroManager");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmLoadFile = new MenuItem(menu, SWT.CASCADE);
		mntmLoadFile.setText("File");
		
		Menu menu_1 = new Menu(mntmLoadFile);
		mntmLoadFile.setMenu(menu_1);
		
		parentComposite = new Composite(shell, SWT.NONE);
		parentComposite.setLayout(new StackLayout());
		
		mainSashForm = new SashForm(parentComposite, SWT.NONE);
		
		Tree root = new Tree(mainSashForm, SWT.BORDER);
		
		GMTreeItem trtmRoot = new GMTreeItem(root, SWT.NONE);
		trtmRoot.setText("root");
		
		tabFolder = new TabFolder(mainSashForm, SWT.NONE);
		
		tbtmView = new TabItem(tabFolder, SWT.NONE);
		tbtmView.setText("View");
		
		tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("Layout");
		mainSashForm.setWeights(new int[] {1, 2});
		
		drillDownSashForm = new SashForm(parentComposite, SWT.NONE);
		
		TreeViewer treeViewer = new TreeViewer(drillDownSashForm, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		
		Composite composite = new Composite(drillDownSashForm, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		Composite composite_2 = new Composite(composite, SWT.NONE);
		formToolkit.adapt(composite_2);
		formToolkit.paintBordersFor(composite_2);
		RowLayout rl_composite_2 = new RowLayout(SWT.HORIZONTAL);
		composite_2.setLayout(rl_composite_2);
		
		SashForm sashForm = new SashForm(composite_2, SWT.NONE);
		sashForm.setLayoutData(new RowData(295, SWT.DEFAULT));
		
		txtHeight = new Text(sashForm, SWT.BORDER);
		txtHeight.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				
				if (txtHeight.getText() != "Height") {
				
				String btnHeight = txtHeight.getText();
				int nHeight = Integer.parseInt(btnHeight);
				System.out.println(nHeight);
				}
				
			}
		});
		formToolkit.adapt(txtHeight, true, true);
		
		txtWidth = new Text(sashForm, SWT.BORDER);
		txtWidth.addModifyListener(new ModifyListener() {
			
			public void modifyText(ModifyEvent arg0) {
				if (txtHeight.getText() != "Width") {
					
					String btnWidth = txtWidth.getText();
					int nWidth = Integer.parseInt(btnWidth);
					sideConv(nWidth);
					System.out.println(nWidth);
					}
			}
		});
		formToolkit.adapt(txtWidth, true, true);
		
		Button btnAddButton = new Button(sashForm, SWT.CENTER);
		btnAddButton.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
				
				addNewButton(50, 50, 0);
				composite_1.layout(true);
				
				
			}
		});
		
		formToolkit.adapt(btnAddButton, true, true);
		btnAddButton.setText("Add Button");
		sashForm.setWeights(new int[] {1, 1, 1});
		
		composite_1 = new Composite(composite, SWT.NONE);
		GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_1.heightHint = 255;
		gd_composite_1.widthHint = 300;
		composite_1.setLayoutData(gd_composite_1);
		composite_1.setLayout(null);
		formToolkit.adapt(composite_1);
		formToolkit.paintBordersFor(composite_1);
		drillDownSashForm.setWeights(new int[] {1, 2});
		
		
		MenuItem mntmDrillDownMenu = new MenuItem(menu, SWT.CASCADE);
		mntmDrillDownMenu.setText("Drill Down Menu");
		
		Menu menu_3 = new Menu(mntmDrillDownMenu);
		mntmDrillDownMenu.setMenu(menu_3);
		
		MenuItem mntmEdit = new MenuItem(menu_3, SWT.NONE);
		mntmEdit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showDrillDownMenu();
			}
		});
		mntmEdit.setText("Edit");
		
		MenuItem mntmClose = new MenuItem(menu_3, SWT.NONE);
		mntmClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				closeDrillDownMenu();
			}
		});
		mntmClose.setText("Close");
		
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

	public void hConv(int h) {
		int nHeight = 0;
		h = nHeight;
		
	}
	public void wConv(int w) {
		int nWidth = 0;
		w = nWidth;
	}
	
	public void addNewButton(int height, int width, int buttonCount) {
		
		String text = "suppe";
		Button newButton = new Button(this.composite_1, SWT.PUSH);
		formToolkit.adapt(newButton, true, true);
		int lastButtonEdges[] = {10 + height, 10 + width};
		newButton.setText(text);
		newButton.setBounds(10, 10, width, height);
		buttonCount = 0;
		if (buttonCount != 0) {
			newButton.setBounds(lastButtonEdges[0], lastButtonEdges[1], height, width);
		} else if (buttonCount > 0) {
			buttonCount += 1;
		}
		
	}
	
	private void showDrillDownMenu() {
		StackLayout layout = (StackLayout) this.parentComposite.getLayout();
		layout.topControl = this.drillDownSashForm;
		this.parentComposite.layout();
	}
	
	private void closeDrillDownMenu() {
		StackLayout layout = (StackLayout) this.parentComposite.getLayout();
		layout.topControl = this.mainSashForm;
		this.parentComposite.layout();
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
