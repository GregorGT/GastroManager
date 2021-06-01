package com.gastromanager.mainwindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.gastromanager.util.XmlUtil;

public class LayoutMenu extends JPanel {
	private static final String LAYOUT_TREE_PATH = "layout";

	private String absolutePathToImage;
	private int windowWidth = 0;
	private int windowHeight = 0;
	private JScrollPane scrollPane = null;	
    private ImageDrawing currentDisplayedImage;    
    private MainWindow mainWindow;
    private String floorName="";
    private Map<JScrollPane, ImageDrawing> scrollPanes;
    
	public LayoutMenu(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
    	JButton newFloorButton = new JButton("<html>New<br/>floor</html>");
		JButton newTableButton = new JButton("<html>New<br/>table</html>");

		newFloorButton.setPreferredSize(new Dimension(100, 100));
		newTableButton.setPreferredSize(new Dimension(100, 100));
		
		this.add(newFloorButton);
		this.add(newTableButton);


		JPanel imageSelectPanel = new JPanel();
		
		TitledBorder border = new TitledBorder("Image layout");
		JLabel selectImage = new JLabel("Select image for the floor's layout");
		JTextField filePathString = new JTextField();
		JButton browseImageButton = new JButton("Browse");
	
		browseImageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				JFrame frameForImageSelection = new JFrame();
				JFileChooser chooser = new JFileChooser();
			    FileNameExtensionFilter filter = new FileNameExtensionFilter("jpeg", "jpg", "png");
			    chooser.setFileFilter(filter);
			    int returnVal = chooser.showOpenDialog(frameForImageSelection);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			       System.out.println("You chose to open this file: " +
			       		chooser.getSelectedFile().getAbsolutePath());
			       absolutePathToImage = chooser.getSelectedFile().getAbsolutePath();
			       filePathString.setText(chooser.getSelectedFile().getAbsolutePath());
			       displayImage();
			    }
			    if (returnVal == JFileChooser.CANCEL_OPTION) {
			    	System.out.println("Canceled");
			    }
			}
		});
		
		imageSelectPanel.setBorder(border);
		filePathString.setPreferredSize(new Dimension(530, 30));
		imageSelectPanel.add(selectImage);	
		imageSelectPanel.add(filePathString);
		imageSelectPanel.setPreferredSize(new Dimension(730, 90));
		imageSelectPanel.add(browseImageButton);
		this.add(imageSelectPanel);
		
		
		TitledBorder tb = new TitledBorder("Title goes here");
		scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(windowWidth-30, windowHeight-220));
		scrollPane.setBorder(tb);
        this.add(scrollPane, BorderLayout.CENTER);
        
		newTableButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				addNewTable();
			}
		});	
		
		newFloorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				addNewFloor();
			}
		});
		
		this.addComponentListener(new ComponentAdapter() {  
		    public void componentResized(ComponentEvent evt) {
		    	Component c = (Component)evt.getSource();
		    	
		    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	            double width = screenSize.getWidth();
	            double height = screenSize.getHeight();

		    	windowWidth = c.getWidth();
		        windowHeight = c.getHeight();

            	filePathString.setPreferredSize(new Dimension(windowWidth - 200, 30));
		        imageSelectPanel.setPreferredSize(new Dimension(windowWidth - 30, 90));
		        if (scrollPane != null) {		        	
		        	scrollPane.setPreferredSize(new Dimension(windowWidth - 30, windowHeight-220));
		        }
            }
		});
		
		//TODO: notify this class when tree event happens in MainWindow.
		mainWindow.getTree().addTreeSelectionListener(createSelectionListener());
	}

	private void addNewTable() {
		if (!mainWindow.getTree().loaded) {
			JOptionPane.showMessageDialog(this, "Please load a file first", null, JOptionPane.ERROR_MESSAGE, null);
			return;
		}
		currentDisplayedImage.addTable();
		JButton btn = new JButton();
		btn.setName("newtable");
		componentToTree(mainWindow.getRoot(), mainWindow.getDefaultModel(), floorName, btn);
	}
	
	private void addNewFloor() {
		if (!mainWindow.getTree().loaded) {
			JOptionPane.showMessageDialog(this, "Please load a file first", null, JOptionPane.ERROR_MESSAGE, null);
			return;
		}
		JButton btn = new JButton();
		String op = JOptionPane.showInputDialog("Enter the name of the new floor");
		btn.setName(op);
		componentToTree(mainWindow.getRoot(), mainWindow.getDefaultModel(), "layout", btn);
//		mainWindow.setRoot(mainWindow.getRoot());
		btn.setName("Image");
		componentToTree(mainWindow.getRoot(), mainWindow.getDefaultModel(), op, btn);
	}
	
	
	public void componentToTree(GMTreeItem item, DefaultTreeModel model, String parent, Component comp) {
		Enumeration enumer = item.children();

		if (item.toString() == parent) {
			GMTreeItem newItem = new GMTreeItem(comp.getName());
			newItem.setName(comp.getName());
			newItem.addAttributes("name", comp.getName());
			newItem.addMenuElements(item.menuElement);
			newItem.treeParent = item.getTree();
			newItem.setXmlName("floor");
			model.insertNodeInto(newItem, item, 0);
		}
		
		if(enumer != null) {
			while (enumer.hasMoreElements()) {
				componentToTree((GMTreeItem)enumer.nextElement(), mainWindow.getDefaultModel(), parent , comp);
			}
		}
	}
	
	private void displayImage() {
		currentDisplayedImage = new ImageDrawing(absolutePathToImage);
		scrollPane.setViewportView(currentDisplayedImage);
		this.add(scrollPane);
	}

    private void loadImageFromXML() {
    	System.out.println("I should load the image of the " + floorName + " floor.");
    	NodeList nodeListFloor = mainWindow.getDoc().getElementsByTagName("floor");
    	
    	for (int i=0; i<nodeListFloor.getLength(); ++i) {
    		Node node = nodeListFloor.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                	Element element = (Element) node;
                	if (element.getAttribute("name").equals(floorName)) {
                		NodeList imageList = element.getElementsByTagName("image");
                		System.out.println(imageList.getLength());
                		String imagePath = imageList.item(0).getAttributes().item(0).getNodeValue();
                		
                		System.out.println(imagePath);
                		
                		if (imagePath != "") {
	                		absolutePathToImage = imagePath;
	                		currentDisplayedImage = new ImageDrawing(absolutePathToImage); 
	                		this.scrollPane.setViewportView(currentDisplayedImage);
                		
	                		
	                		
                		} else {
                			this.remove(scrollPane);
                			repaint();
                			revalidate();
                			initScrollPane();
                		}
                		
//                		if (!scrollPanes.containsKey(floorName)) {
//                			scrollPanes.put(scrollPane, currentDisplayedImage);
//                		}
                	}
                }
            }
    	}
    }

    private void initScrollPane() {
	    TitledBorder tb = new TitledBorder(floorName);
		scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(windowWidth-30, windowHeight-220));
		scrollPane.setBorder(tb);
	    this.add(scrollPane, BorderLayout.CENTER);
	}
    
    private TreeSelectionListener createSelectionListener() {
        return new TreeSelectionListener() {
        	@Override
            public void valueChanged(TreeSelectionEvent e) {
                TreePath path = e.getPath();
                int pathCount = path.getPathCount();
                for (int i = 0; i < pathCount; i++) {
                	if (pathCount == 3 && path.getPathComponent(i).toString().contains("layout")) {
                		
                		if (floorName.equals(path.getLastPathComponent().toString())) {
                			continue;
                		}
                		
                		floorName = path.getLastPathComponent().toString();
                		loadImageFromXML();
                	}
                }
        	}
        };
    }
}
