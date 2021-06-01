package com.gastromanager.mainwindow;


import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.BoxLayout;
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
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.gastromanager.util.XmlUtil;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	private static GMTreeItem root = new GMTreeItem("Restaurant name");
	private GMTree tree;
	private DefaultTreeModel defaultModel;
	private JScrollPane treeScroll;
	private JTextField textField;
	public MenuElement newMElement = new MenuElement(); 
	public HashSet<GMTreeItem> drillDownItems = new HashSet<GMTreeItem>();
	public OrderingMenu tabOrdering;
	public Connection connection;
	public DrillDownMenu drillDownMenu;
	public XmlUtil xmlUtil;
	private Document doc;
	
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
		setBounds(100, 20, 1000, 700);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFileMenu = new JMenu("File");
		menuBar.add(mnFileMenu);


		

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setEnabled(false);
		splitPane.setDividerLocation(200);
		splitPane.getLeftComponent().setMinimumSize(new Dimension(200, MAXIMIZED_VERT));
		contentPane.add(splitPane, BorderLayout.CENTER);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		splitPane.setRightComponent(tabbedPane);
		

		JPanel tabView = new JPanel();
		tabbedPane.addTab("View", null, tabView, null);
		tabView.setLayout(null);


		
		tree = new GMTree(root);
		tree.rootItem = root;
		root.setTree(tree);

		JScrollPane treeScroll = new JScrollPane(tree, 
												 JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
												 JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		treeScroll.setMinimumSize(new Dimension(200, MAXIMIZED_VERT));
		splitPane.setLeftComponent(treeScroll);
		tree.setEditable(true);
		
		treeScroll = new JScrollPane();
		defaultModel = (DefaultTreeModel) tree.getModel(); 

		LayoutMenu tabLayout = new LayoutMenu(this);
		tabbedPane.addTab("Layout", null, tabLayout, null);
		tabLayout.setLayout(new FlowLayout());
		
		drillDownMenu = new DrillDownMenu(root, defaultModel, tree, newMElement);
		JScrollPane drillDownScroll = new JScrollPane(drillDownMenu, 
										   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
										   JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		tabbedPane.addTab("Drill Down Menu", null, drillDownScroll, null);
		drillDownMenu.setLayout(null);

		tabOrdering = new OrderingMenu(connection, drillDownItems, drillDownMenu);
		tabbedPane.addTab("Ordering", null, tabOrdering, null);
		tabOrdering.setLayout(null);
		
		ReportsMenu tabReports = new ReportsMenu();
//		reportsTab.setLayout(new BoxLayout());
		tabbedPane.addTab("Reports", null, tabReports, null);

		PaymentMenu tabPayment = new PaymentMenu();
		tabbedPane.addTab("Payment", null,tabPayment, null);
		tabPayment.setPreferredSize(new Dimension(750,650));
		tabPayment.setLayout(null);

	
	
		
		
		
		
		JMenuItem mntmLoad = new JMenuItem("Load");
		mntmLoad.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				JFileChooser fc = new JFileChooser();
				FileNameExtensionFilter filterExt = new FileNameExtensionFilter("XML Docs", "xml");
				fc.setFileFilter(filterExt);

				int returnVal = fc.showDialog(null, "Open XML File...");
				String selected = fc.getSelectedFile().toString();
				
				try {
					String fstr = xmlUtil.readFileToString(selected, Charset.defaultCharset());
					doc = xmlUtil.loadXMLFromString(fstr);
					xmlUtil.parseXmlDocument(doc, root, newMElement);//, tabOrdering);
					tree.init(tree, drillDownMenu);
					tree.loaded = true;
//					tabLayout.setDocument(doc);
//					tabLayout.setTree(tree);
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
		mnFileMenu.add(mntmLoad);

		JMenuItem mntmNewMenuItem = new JMenuItem("Save");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				String ex = xmlUtil.writeTreeIntoString(root);
//				System.out.println(ex);
				System.out.println(root.getChildCount());
				 
				 String newString = xmlUtil.writeTreeIntoString(root);

				 File saveFile = new File("C:\\saved_sample_template.xml");
					
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
		mnFileMenu.add(mntmNewMenuItem);

		JButton btnDebugLoadFile = new JButton("Load File (Debug)");
		btnDebugLoadFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Document doc;
				String fstr;
				try {
					fstr = xmlUtil.readFileToString("C:\\GastroManager\\JDesktopJava\\data\\sample_tempalte.xml", Charset.defaultCharset());
					doc = xmlUtil.loadXMLFromString(fstr);
					xmlUtil.parseXmlDocument(doc, root, newMElement);//, tabOrdering);
					tree.init(tree, drillDownMenu);
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}			
			}
		});
		menuBar.add(btnDebugLoadFile);
		
//		tree.addTreeSelectionListener(createSelectionListener());
		
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

	public GMTree getTree() {
		return tree;
	}

	public void setTree(GMTree tree) {
		this.tree = tree;
	}

	public static GMTreeItem getRoot() {
		return root;
	}

	public static void setRoot(GMTreeItem root) {
		MainWindow.root = root;
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public DefaultTreeModel getDefaultModel() {
		return defaultModel;
	}

	public void setDefaultModel(DefaultTreeModel defaultModel) {
		this.defaultModel = defaultModel;
	}
	

	
}
