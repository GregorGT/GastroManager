package com.gastromanager.mainwindow;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;
import java.awt.FlowLayout;
import javax.swing.JEditorPane;
import javax.swing.JTextPane;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JSeparator;
import java.awt.Choice;

public class MainWindow extends JFrame {

	private JPanel contentPane;
//	private DrillDownMenu drillDownMenu;
	private GMTreeItem root;
	private GMTree tree;
	private DefaultTreeModel defaultModel;
	private JScrollPane treeScroll;
	private JTextField textField;
	private boolean treeLoaded = false;
	
	
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
	 
		GMTreeItem newNode = new GMTreeItem();
		
		NamedNodeMap attributes = xmlNode.getAttributes();
	 
		if(attributes != null) {
			for(int k = 0; k<attributes.getLength(); ++k) {
			String name = attributes.item(k).getNodeName();
			String value = attributes.item(k).getNodeValue();
			
			
//			newNode.m_attributes.put(name,  value);
//			newNode.m_attributes.putIfAbsent("uuid", assignUUID());
			
			newNode.setAttributes(name, value);
			newNode.setUUID(assignUUID());
			
//			System.out.println(name  + "  ====  " + value);
			
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

	 newNode.setValue(xmlNode.getNodeValue());

	 newNode.setXmlName(xmlNode.getNodeName());
	 
	 newNode.setUserObject(newNode.getDisplayString());
	 NodeList children = xmlNode.getChildNodes();
	 rootNode.add(newNode);
	 
	 System.out.println( "NODE NAME " + newNode.m_name);

     System.out.println( "NODE VALUE " + newNode.m_value);

	 System.out.println( "NODE XML NAME  " + newNode.m_xmlname);
		
	 System.out.println("ATTRIBUTES " + newNode.getAttributes());
	 
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
	
	String writeTreeIntoString(GMTree tree, GMTreeItem treeItem) {
		
		int node = treeItem.getChildCount();
		
		String result = "";
		
		result += "<" + treeItem.getXmlName();
		
		Iterator it = treeItem.getAttributes().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
	        result += " " + pair.getKey() + "=" + "\"" + pair.getValue() + "\"";
	    }
		
		if (node > 0 || treeItem.m_value.length() > 0) {
	    	result += ">";
	    	} else {
	    	result += "/>";
	    	}
	    if (node == 0 && treeItem.m_value.length() > 0) {
	    	result += treeItem.m_value;
	    }
		
		result += treeItem.m_name;
		result += treeItem.m_value;
		
		int children = treeItem.getChildCount();
		
		
		for (int j = 0; j< children; j++) {
			
//			System.out.println(children);
			GMTreeItem newItem = (GMTreeItem) treeItem.getChildAt(j);
			writeTreeIntoString(tree, newItem);
			
		}
		
		//Write saving function
		return result;
	}
	
	
	private String assignUUID() {
		Random rd = new Random(); // creating Random object
	     String uuid = String.valueOf(rd.nextLong());
	     return uuid;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 750);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFileMenu = new JMenu("File");
		menuBar.add(mnFileMenu);
		
		
		JMenuItem mntmLoad = new JMenuItem("Load");
		mntmLoad.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                
                JFileChooser fc = new JFileChooser();
                FileNameExtensionFilter filterExt = new FileNameExtensionFilter("XML Docs", "xml");
                fc.setFileFilter(filterExt);
                
                int returnVal = fc.showDialog(null, "Open XML File...");
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                	System.out.println("Die zu oeffnende Datei ist: " +
                            fc.getSelectedFile().toString());
                }
                String selected = fc.getSelectedFile().toString();
                Document doc;
                try {
                	String fstr = readFileToString(selected, Charset.defaultCharset());
                	doc = loadXMLFromString(fstr);
                	parseXmlDocument(doc, root);
                	treeLoaded = true;
                } catch (Exception e1) {
                	e1.printStackTrace();
                }
                
            }
        });
		mnFileMenu.add(mntmLoad);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Save");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(writeTreeIntoString(tree, root));
			}
		});
		mnFileMenu.add(mntmNewMenuItem);
		
		JButton btnDebugLoadFile = new JButton("Load File (Debug)");
		btnDebugLoadFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                Document doc;
                String fstr;
				try {
					fstr = readFileToString("C:\\GastroManager\\DesktopJava\\data\\sample_tempalte.xml", Charset.defaultCharset());
                	doc = loadXMLFromString(fstr);
                	parseXmlDocument(doc, root);
                	treeLoaded = true;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}			
			}
		});
		menuBar.add(btnDebugLoadFile);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(200);
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		splitPane.setRightComponent(tabbedPane);
		
		JPanel tabView = new JPanel();
		tabbedPane.addTab("View", null, tabView, null);
		tabView.setLayout(null);
		
		JTabbedPane tabLayout = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("Layout", null, tabLayout, null);
	
		root = new GMTreeItem("Root");
	    tree = new GMTree(root);
	    splitPane.setLeftComponent(tree);
	    tree.setEditable(true);
	    
	    treeScroll = new JScrollPane();
	    defaultModel = (DefaultTreeModel) tree.getModel(); 
	    
		DrillDownMenu drillDownMenu = new  DrillDownMenu();
		drillDownMenu.init(drillDownMenu, root, defaultModel, tree);
		
		tabbedPane.addTab("Drill Down Menu", null, drillDownMenu, null);
		drillDownMenu.setLayout(null);
		   
	    tree.init(tree , drillDownMenu, defaultModel, root);
		
		
	    OrderingMenu tabOrdering = new OrderingMenu();
		tabbedPane.addTab("Ordering", null, tabOrdering, null);
	    tabOrdering.setLayout(null);	    
	    
	}
	
	public void showSelectedDrillDown() {
		
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
