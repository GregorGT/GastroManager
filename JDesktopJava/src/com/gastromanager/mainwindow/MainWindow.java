/*Copyright 2021 GastroRice

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/


package com.gastromanager.mainwindow;


import java.awt.BorderLayout;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.HashSet;

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
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultTreeModel;

import org.w3c.dom.Document;

import com.gastromanager.util.PropertiesUtil;
import com.gastromanager.util.PublicVariables;
import com.gastromanager.util.XmlUtil;
import com.sun.star.xforms.Model;


public class MainWindow extends JFrame {

	private final static String VERSION_TAG = "v 1.0 11/8/2021";
	
	private JPanel contentPane;
	private GMTreeItem root = new GMTreeItem("Restaurant name");
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
	private String openedFile;
	private LayoutMenu tabLayout;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame =
							new MainWindow();
					frame.setTitle("Main Window");
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


		tabLayout = new LayoutMenu(this);
		tabbedPane.addTab("Layout", null, tabLayout, null);
		tabLayout.setLayout(new FlowLayout());
		tree.setLayoutMenu(tabLayout);


		drillDownMenu = new DrillDownMenu(root, defaultModel, tree, newMElement);
		JScrollPane drillDownScroll = new JScrollPane(drillDownMenu,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		tabbedPane.addTab("Drill Down Menu", null, drillDownScroll, null);
		drillDownMenu.setLayout(null);

		tabOrdering = new OrderingMenu(this);
		tabbedPane.addTab("Ordering", null, tabOrdering, null);
		tabOrdering.setLayout(null);

		ReportsMenu tabReports = new ReportsMenu();
//		reportsTab.setLayout(new BoxLayout());
		tabbedPane.addTab("Reports", null, tabReports, null);

		PaymentMenu tabPayment = new PaymentMenu();
		tabbedPane.addTab("Payment", null,tabPayment, null);
		tabPayment.setPreferredSize(new Dimension(750,650));
		tabPayment.setLayout(null);

		BookingsMenu tabBookings = new BookingsMenu();
		tabbedPane.addTab("Bookings", null,tabBookings, null);
		

		ServerSocketMenu serverSocketMenu = new ServerSocketMenu(VERSION_TAG);
		tabbedPane.addTab("Server/Settings", null, serverSocketMenu, null);
		
		JMenuItem mntmLoad = new JMenuItem("Load");
		mntmLoad.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				JFileChooser fc = new JFileChooser();
				FileNameExtensionFilter filterExt = new FileNameExtensionFilter("XML Docs", "xml");
				fc.setFileFilter(filterExt);

				int returnVal = fc.showDialog(null, "Open XML File...");
//				String selected = fc.getSelectedFile().toString();
				openedFile = fc.getSelectedFile().toString();

				try {
					String fstr = xmlUtil.readFileToString(openedFile, Charset.defaultCharset());
					doc = xmlUtil.loadXMLFromString(fstr);
					xmlUtil.parseXmlDocument(doc, root, newMElement);//, tabOrdering);
					tree.init(tree, drillDownMenu, root);
					tree.loaded = true;
					PublicVariables publicVariables = PublicVariables.getInstance();
					publicVariables.setTree(root);
					PropertiesUtil.setAndSavePropertyValue("xml_file", openedFile);
					tabLayout.setIsFileLoaded(true);
					tabOrdering.fileHasChanged();
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		mnFileMenu.add(mntmLoad);
		
		autoLoadPreviousFile();

		JMenuItem mntmNewMenuItem = new JMenuItem("Save");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				String ex = xmlUtil.writeTreeIntoString(root);
//				System.out.println(ex);
//				System.out.println(root.getChildCount());

				tabLayout.saveToXmlAndDb();

				root = (GMTreeItem) defaultModel.getRoot();


				String newString = xmlUtil.writeTreeIntoString(root);

				String autoFile = PropertiesUtil.getPropertyValue("xml_file");
				File saveFile = null;
				
				if (!autoFile.isEmpty() && autoFile != null) {
					saveFile = new File(autoFile);
				} else if (!openedFile.isEmpty() && openedFile != null) {
					saveFile = new File(openedFile);
				} else {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setDialogTitle("Specify a file to save");   
					 
					int userSelection = fileChooser.showSaveDialog(contentPane);
					 
					if (userSelection == JFileChooser.APPROVE_OPTION) {
					    saveFile = fileChooser.getSelectedFile();
					    System.out.println("Save as file: " + saveFile.getAbsolutePath());
					}
				}
				
				try {
					FileWriter fileWriter = new FileWriter(saveFile);
					fileWriter.write(newString);
					fileWriter.flush();
					fileWriter.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
//				System.out.println(newString);
			}
		});
		mnFileMenu.add(mntmNewMenuItem);

//		JButton btnDebugLoadFile = new JButton("Load File (Debug)");
//		btnDebugLoadFile.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				Document doc;
//				String fstr;
//				try {
//					fstr = xmlUtil.readFileToString("C:\\GastroManager\\JDesktopJava\\data\\sample_tempalte.xml", Charset.defaultCharset());
//					doc = xmlUtil.loadXMLFromString(fstr);
//					xmlUtil.parseXmlDocument(doc, root, newMElement);//, tabOrdering);
//					tree.init(tree, drillDownMenu, root);
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				} catch (Exception e1) {
//					e1.printStackTrace();
//				}
//			}
//		});
//		menuBar.add(btnDebugLoadFile);
		
		tabbedPane.addChangeListener(l -> {
			if (tabbedPane.getSelectedComponent() instanceof BookingsMenu) {
				tabBookings.fillTable();
			}
		
		});
	}

	private void autoLoadPreviousFile() {
		openedFile = PropertiesUtil.getPropertyValue("xml_file");
		
		if (openedFile.isEmpty() || openedFile == null) {
			return;
		}
		
		
		try {
			String fstr = xmlUtil.readFileToString(openedFile, Charset.defaultCharset());
			doc = xmlUtil.loadXMLFromString(fstr);
			xmlUtil.parseXmlDocument(doc, root, newMElement);//, tabOrdering);
			tree.init(tree, drillDownMenu, root);
			tree.loaded = true;
			PublicVariables publicVariables = PublicVariables.getInstance();
			publicVariables.setTree(root);
			tabLayout.setIsFileLoaded(true);
			tabOrdering.fileHasChanged();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
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

	public GMTreeItem getRoot() {
		return root;
	}

	public void setRoot(GMTreeItem root) {
		this.root = root;
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

	public MenuElement getNewMElement() {
		return newMElement;
	}

	public void setNewMElement(MenuElement newMElement) {
		this.newMElement = newMElement;
	}

	public XmlUtil getXmlUtil() {
		return xmlUtil;
	}

	public void setXmlUtil(XmlUtil xmlUtil) {
		this.xmlUtil = xmlUtil;
	}

	public DrillDownMenu getDrillDownMenu() {
		return drillDownMenu;
	}

	public void setDrillDownMenu(DrillDownMenu drillDownMenu) {
		this.drillDownMenu = drillDownMenu;
	}

}