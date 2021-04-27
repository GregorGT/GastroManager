package com.gastromanager.mainwindow;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	private GMTreeItem root;
	private GMTree tree;
	private DefaultTreeModel defaultModel;
	private JScrollPane treeScroll;
	private JTextField textField;
	public MenuElement newMElement = new MenuElement(); 
	
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

	void treeBuild(GMTreeItem rootNode, Node xmlNode, DefaultTreeModel model) {
		if (xmlNode.getNodeName().contains("#"))
			return;
		
		GMTreeItem newNode = new GMTreeItem();

		NamedNodeMap attributes = xmlNode.getAttributes();
		
		if(attributes != null) {
			for(int k = 0; k<attributes.getLength(); ++k) {
				String name = attributes.item(k).getNodeName();
				String value = attributes.item(k).getNodeValue();

				newNode.addAttributes(name, value);			
				newNode.setUUID(assignUUID());
//				newNode.setId(newNode.getAttribute("uuid"));
			}
		}

		if (xmlNode.getNodeName() == "x") {
			String emptyNode = xmlNode.getTextContent();
		}

		if (xmlNode.getChildNodes().getLength() == 1) {
			if (xmlNode.getFirstChild().hasChildNodes() == false) {
				String nodeName = xmlNode.getFirstChild().getNodeName();
				if (nodeName == "#text") {
					//				 newNode.m_value = xmlNode.getFirstChild().getTextContent();
					newNode.setValue(xmlNode.getFirstChild().getTextContent());
				}
			}
		}

		if (xmlNode.getNodeValue() != null && xmlNode.getNodeValue().length() > 0)
			newNode.setValue(xmlNode.getNodeValue());
		
		if (rootNode.toString().contains("menues")) {
			newMElement.addMenuElement(newNode);
			System.out.println(newMElement.numberOfElements);
		}
		
//		newNode.setName(xmlNode.getNodeName());
		newNode.setXmlName(xmlNode.getNodeName());
		
		newNode.setUserObject(newNode.getDisplayString());
		if(newNode.getUserObject() == "root") {
			newNode.setUserObject("All");
		}
		if (newNode.getXmlName() == "button") {			
			HashMap<String, String> btnAttrs = newNode.getAttributes();
			GMTreeItem newH = new GMTreeItem(); GMTreeItem newW = new GMTreeItem();
			GMTreeItem newX = new GMTreeItem(); GMTreeItem newY = new GMTreeItem();
			model.insertNodeInto(newNode, rootNode, 0);
			model.insertNodeInto(newH, newNode, 0);
			model.insertNodeInto(newW, newNode, 1);
			model.insertNodeInto(newX, newNode, 2);
			model.insertNodeInto(newY, newNode, 3);
		
			newH.setUserObject("Height: "  + btnAttrs.get("height"));
			newW.setUserObject("Width: " + btnAttrs.get("width"));
			newX.setUserObject("X: " + btnAttrs.get("x-position"));
			newY.setUserObject("Y: " + btnAttrs.get("y-position"));
		}
		
		NodeList children = xmlNode.getChildNodes();

		newNode.setTree(tree);
		rootNode.add(newNode);

		for (int i = 0; i < children.getLength(); ++i) {
			treeBuild(newNode, children.item(i), model);
		}
	}

	void parseXmlDocument(Document doc, GMTreeItem root) {
		try {
			treeBuild(root, doc.getFirstChild(), defaultModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	String writeTreeIntoString(GMTreeItem treeItem, DefaultTreeModel model) {

		String result = "";

		Enumeration<?> enumer = root.preorderEnumeration();
//		Enumeration<?> enumer = treeItem.children();
		
//		GMTreeItem[] enumer = treeItem.
		
		while (enumer.hasMoreElements()) {

			GMTreeItem node = (GMTreeItem) enumer.nextElement();
			int children = node.getChildCount();

			result += "<" + node.getXmlName();

			Iterator it = node.getAttributes().entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				result += " " + pair.getKey() + "=" + "\"" + pair.getValue() + "\"";
			}

			if (children > 0 || node.getValue().length() > 0) {
				result += ">";
			} else {
				result += "/>";
			}
			if (children == 0 && node.getValue().length() > 0) {
				result += node.getValue();
			}

//			for (int k = 0; k < node.getChildCount(); k++) {
//				writeTreeIntoString((GMTreeItem) node.getChildAt(k), model);
//			}

			//		String nodeValue = String.valueOf(node.getUserObject());
//			String indent = "";
//			while (node.getParent() != null) {
//				indent += "    "; 
//				node = (GMTreeItem) node.getParent();
//			}
			
			if (children > 0 || treeItem.getValue().length() > 0) {
				result += "</" + treeItem.getXmlName() + ">";
			}
		}
		return result;
	}


	public String assignUUID() {
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
//					treeLoaded = true;
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
		mnFileMenu.add(mntmLoad);

		JMenuItem mntmNewMenuItem = new JMenuItem("Save");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ex = writeTreeIntoString(root, defaultModel);
				System.out.println(ex);
			}
		});
		mnFileMenu.add(mntmNewMenuItem);

		JButton btnDebugLoadFile = new JButton("Load File (Debug)");
		btnDebugLoadFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Document doc;
				String fstr;
				try {
					fstr = readFileToString("C:\\GastroManager\\JDesktopJava\\data\\sample_tempalte.xml", Charset.defaultCharset());
					doc = loadXMLFromString(fstr);
					parseXmlDocument(doc, root);
//					treeLoaded = true;
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

		root = new GMTreeItem("Restaurant Name");
		root.setXmlName("root");
		
		tree = new GMTree(root);
		tree.rootItem = root;
		splitPane.setLeftComponent(tree);
		tree.setEditable(true);
		
		treeScroll = new JScrollPane();
		defaultModel = (DefaultTreeModel) tree.getModel(); 

		DrillDownMenu drillDownMenu = new DrillDownMenu(root, defaultModel, tree, newMElement);
//		drillDownMenu.init(drillDownMenu, root, defaultModel, tree);

		tabbedPane.addTab("Drill Down Menu", null, drillDownMenu, null);
		drillDownMenu.setLayout(null);

		tree.init(tree, drillDownMenu, defaultModel, root);

		OrderingMenu tabOrdering = new OrderingMenu();
		tabbedPane.addTab("Ordering", null, tabOrdering, null);
		tabOrdering.setLayout(null);
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
