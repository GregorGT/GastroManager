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
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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

public class MainWindow {
	private DataBindingContext m_bindingContext;

	protected Shell shell;
	private SashForm sashForm;
	private TabFolder tabFolder;
	private TabItem tbtmView;
	private TabItem tbtmNewItem;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	protected Document selected;

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
		String result = "<" + treeItem.m_xmlname + treeItem.m_attributes + "/>";
		
		for (int i = 0; i < node.length; ++i) {
		
			if (node[i] instanceof GMTreeItem) {
				GMTreeItem newItem = (GMTreeItem) node[i];
				result += writeTreeIntoString(treeIn, newItem);
			}
			
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
		
		sashForm = new SashForm(shell, SWT.NONE);
		
		Tree root = new Tree(sashForm, SWT.BORDER);
		formToolkit.adapt(root);
		formToolkit.paintBordersFor(root);
		
		GMTreeItem trtmRoot = new GMTreeItem(root, SWT.NONE);
		trtmRoot.setText("root");
		
		tabFolder = new TabFolder(sashForm, SWT.NONE);
		
		tbtmView = new TabItem(tabFolder, SWT.NONE);
		tbtmView.setText("View");
		
		tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("Layout");
		sashForm.setWeights(new int[] {1, 1});
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmLoadFile = new MenuItem(menu, SWT.CASCADE);
		mntmLoadFile.setText("File");
		
		Menu menu_1 = new Menu(mntmLoadFile);
		mntmLoadFile.setMenu(menu_1);
		
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
				
				/* FileDialog fileSave = new FileDialog(shell, SWT.SAVE);
	            fileSave.setFilterNames(new String[] {".XML"});
	            fileSave.setFilterExtensions(new String[] {"*.xml"});
	            fileSave.setFilterPath("c:\\"); // Windows path
	            fileSave.setFileName("new_sample_template.xml");
	            fileSave.open();

	            System.out.println("File Saved as: " + fileSave.getFileName());
	            
				String newString = writeTreeIntoString(root, trtmRoot); 
				fileSave.getFileName();
				File file = new File(newString);
				try {
					file.createNewFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				//SAVE FEATURE 1st try
				 
				*/
				
				String newString = writeTreeIntoString(root, trtmRoot);
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
	
	
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		return bindingContext;
	}
}
