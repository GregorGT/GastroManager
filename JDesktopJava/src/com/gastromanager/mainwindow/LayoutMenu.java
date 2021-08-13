package com.gastromanager.mainwindow;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NodeList;

import com.gastromanager.db.DbConnection;


public class LayoutMenu extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static final String INSERT_TO_LOCATION = "INSERT INTO LOCATION VALUES(?,?,?,?,?,?,?,?,?)";
	private static final String SELECT_MAX_ID = "SELECT max(id)+1 FROM location";
	private static final String UPDATE_TO_LOCATION = "UPDATE location SET last_midified_date=? WHERE floor_id=? AND table_id=?";
	
	
	private String absolutePathToImage;
	private int windowWidth = 0;
	private int windowHeight = 0;
	private JScrollPane scrollPane = null;	
    private ImageDrawing currentDisplayedImage;    
    private MainWindow mainWindow;
    private String floorName="";
    private GMTreeItem root;
	private GMTree tree;
	private DefaultTreeModel defaultModel;
	private MenuElement newMElement;
	private boolean isFileLoaded;
	private Map<Floor, ImageDrawing> allFloors = new HashMap<>();
	private TitledBorder tb = new TitledBorder("Floor");
	private JTextField filePathString = new JTextField();
	private Connection connection;
	
	
	public LayoutMenu(MainWindow mainWindow) {
		try {
			connection = DbConnection.getDbConnection().gastroDbConnection;
		} catch (Exception e) {
			System.err.println("Failed to connect to database.\nClass: LayoutMenu.java\tMethod: LayoutMenu");
		}
		
		this.root = mainWindow.getRoot();
		this.tree = mainWindow.getTree();
		this.defaultModel = mainWindow.getDefaultModel();
		this.newMElement = mainWindow.getNewMElement();
		
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
		
		mainWindow.getTree().addTreeSelectionListener(createSelectionListener());
	}

	private void addNewTable() {
		if (!mainWindow.getTree().loaded) {
			JOptionPane.showMessageDialog(this, "Please load a file first", null, JOptionPane.ERROR_MESSAGE, null);
			return;
		}
		
		if (currentDisplayedImage.getPathToImage().equals("")) {
			JOptionPane.showMessageDialog(this, "Select an image", null, JOptionPane.ERROR_MESSAGE, null);
			return;	
		}
		
		JButton btn = new JButton();
		btn.setName("table");

		GMTreeItem newItem = new GMTreeItem("table");
		
		for (Map.Entry<Floor, ImageDrawing> map: allFloors.entrySet()) {
			if (map.getKey().getTitle().equals(floorName)) {
				newItem.addAttributes("value", String.valueOf(map.getValue().getNumOfRecs()+1));		
			}
		}

		newItem.setXmlName("table");
		newItem.addAttributes("name", "table");
		newItem.addAttributes("x", "");
		newItem.addAttributes("y", "");
		newItem.addAttributes("width", "");
		newItem.addAttributes("height", "");
		newItem.addAttributes("rot", "");
		
		currentDisplayedImage.addTable(newItem.getAttribute("value"), false, false);	
		componentToTree(mainWindow.getRoot(), mainWindow.getDefaultModel(), floorName, btn, "table", newItem);
	}
	
	
	private void addNewFloor() {
		if (!mainWindow.getTree().loaded) {
			JOptionPane.showMessageDialog(this, "Please load a file first", null, JOptionPane.ERROR_MESSAGE, null);
			return;
		}

		JButton btn = new JButton();
		String op = JOptionPane.showInputDialog("Enter the name of the new floor");
		if (op == null) {
			return;
		}
		for (Map.Entry<Floor, ImageDrawing> map: allFloors.entrySet()) {
			if (map.getKey().getTitle().equals(op)) {
				JOptionPane.showMessageDialog(this, "There is already a floor with the same name", null, JOptionPane.ERROR_MESSAGE, null);
				return;
			}
		}
		btn.setName(op);
		
		GMTreeItem floorItem = new GMTreeItem(op);
		floorItem.setName(btn.getName());
		floorItem.setXmlName("floor");
	
		floorItem.addAttributes("name", op);
		floorItem.addAttributes("value", String.valueOf(allFloors.size()+1));	
		
		componentToTree(mainWindow.getRoot(), mainWindow.getDefaultModel(), "layout", btn, "floor", floorItem);
		
		btn = new JButton();

		GMTreeItem imageItem = new GMTreeItem("image");
		imageItem.setXmlName("image");
		imageItem.setName("Image");
		imageItem.addAttributes("value", "");		
		
		componentToTree(mainWindow.getRoot(), mainWindow.getDefaultModel(), op, btn, "image", imageItem);

		allFloors.put(new Floor(op, String.valueOf(allFloors.size()+1), "", false), new ImageDrawing("", String.valueOf(allFloors.size()+1), connection, this));
	}
	

	public void componentToTree(GMTreeItem treeItem, DefaultTreeModel model, String parent, Component comp, String xmlName, GMTreeItem newItem) {
		Enumeration enumer = treeItem.children();

		if (treeItem.toString().equals(parent)) {
			newItem.addMenuElements(treeItem.menuElement);
			newItem.treeParent = treeItem.getTree();
			newItem.setTree(treeItem.getTree());
			defaultModel.insertNodeInto(newItem, treeItem, 0);
			treeItem.children.add(newItem);
		}
		
		if(enumer != null) {
			while (enumer.hasMoreElements()) {
				componentToTree((GMTreeItem)enumer.nextElement(), model, parent , comp, xmlName, newItem);
			}
		}
	}
	
	public void removeNodeFromTree(String id, String floorId) {
		removeNodeFromTree(root, defaultModel, "table", id, floorId);
		changeTableAttributesInTree(root, id);
	}
	
	private void removeNodeFromTree(GMTreeItem treeItem, DefaultTreeModel model, String nodeName, String id, String floorId) {
		Enumeration en = treeItem.children();
	    
    	if (treeItem.getXmlName().equals("table") && treeItem.getAttribute("value").equals(id)) {
    		GMTreeItem parent = (GMTreeItem) treeItem.getParent();
    		if (parent.getAttribute("value").equals(floorId)) {
    			defaultModel.removeNodeFromParent(treeItem);
    			treeItem.removeFromParent();
    			parent.children.remove(treeItem);
    		}
    	}        	

	    if(en != null) {
			while (en.hasMoreElements()) {
				removeNodeFromTree((GMTreeItem) en.nextElement(), model, nodeName, id, floorId);
			}
		}
	}
	
	private void displayImage() {
		for (Map.Entry<Floor, ImageDrawing> m: allFloors.entrySet()) {
			if (m.getKey().getTitle().equals(floorName)) {
				m.getValue().setPathToImage(absolutePathToImage);
				changeImageInTree(mainWindow.getRoot());
			}
		}
	}
    
	private void changeImageInTree(GMTreeItem root) {
		Enumeration enumer = root.children();
		
		if (root.toString().equals(floorName)) {
			while (enumer.hasMoreElements()) {
				root = (GMTreeItem) enumer.nextElement();
				if (root.getXmlName().equals("image")) {
					root.addAttributes("value", absolutePathToImage);
					return;
				}
			}
		}
		
		while (enumer.hasMoreElements()) {
			changeImageInTree((GMTreeItem)enumer.nextElement());
		}
	}

	private void changeTableAttributesInTree(GMTreeItem root, String id) {
		Enumeration enumer = root.children();
		
		try {
			if (root.getXmlName().equals("table")) {
				String currentValue = root.getAttribute("value");		
				for (Map.Entry<Floor, ImageDrawing> map: allFloors.entrySet()) {

					if (root.getParent().toString().equals(map.getKey().getTitle())) { 
						Tables[] tables = map.getValue().getTables();
						
						for (int i=0; i<tables.length; ++i) {
							if (tables[i] == null) {
								continue;
							}
							if (tables[i].getPreviousValue().equals(currentValue)) {
								root.addAttributes("value", String.valueOf(tables[i].getValue()));
							}
						}
					}
				}
			}
		} catch (NullPointerException e) 
			{ }
		
		if(enumer != null) {
			while (enumer.hasMoreElements()) {
				changeTableAttributesInTree((GMTreeItem)enumer.nextElement(), id);
			}
		}
				
	}
	
	private void changeTableAttributesInTree(GMTreeItem root) {
		Enumeration enumer = root.children();
		
		try {
			if (root.getXmlName().equals("table")) {
				String currentValue = root.getAttribute("value");		
				for (Map.Entry<Floor, ImageDrawing> map: allFloors.entrySet()) {

					if (root.getParent().toString().equals(map.getKey().getTitle())) { 
						Tables[] tables = map.getValue().getTables();
						
						for (int i=0; i<tables.length; ++i) {
							if (tables[i].getValue().equals(currentValue)) {
								root.addAttributes("value", String.valueOf(tables[i].getValue()));
								root.addAttributes("x", String.valueOf(tables[i].x));
								root.addAttributes("y", String.valueOf(tables[i].y));
								root.addAttributes("width", String.valueOf(tables[i].getWidth()));
								root.addAttributes("height", String.valueOf(tables[i].getHeight()));
								root.addAttributes("rot", String.valueOf(tables[i].getRotate()));
							}
						}
					}
				}
			}
		} catch (NullPointerException e) 
			{ }
		
		while (enumer.hasMoreElements()) {
			changeTableAttributesInTree((GMTreeItem)enumer.nextElement());
		}
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
                		
                		boolean found=false;
                		for (Map.Entry<Floor, ImageDrawing> m: allFloors.entrySet()) {
                			if (m.getKey().getTitle().equals(floorName)) {
                				scrollPane.setViewportView(m.getValue());
                				scrollPane.setBorder(new TitledBorder(m.getKey().getTitle()));
                				currentDisplayedImage = m.getValue();
                				filePathString.setText(m.getValue().getPathToImage());
                				found = true;
                				break;
                			}
                		}
                		if (!found) {
                			ImageDrawing im = createFloor(floorName);
                			scrollPane.setViewportView(im);
            				scrollPane.setBorder(new TitledBorder(floorName));
            				currentDisplayedImage = im;
            				filePathString.setText(im.getPathToImage());
                		}		
                	}
                }
        	}
        };
    }
    
	private ImageDrawing createFloor(String floorName) {
		ImageDrawing im = new ImageDrawing(null, String.valueOf(allFloors.size()+1), connection, this);
		allFloors.put(new Floor(floorName, String.valueOf(allFloors.size()+1), "", false), im);
		return im;
	}
	
    private void loadFloorFromFile() {
    	XPathFactory xPathfactory = XPathFactory.newInstance();
    	XPath xpath = xPathfactory.newXPath();
    	try {
			XPathExpression expr = xpath.compile("/root/layout/floor");
			NodeList nl = (NodeList) expr.evaluate(mainWindow.getDoc(), XPathConstants.NODESET);
			for (int i=0; i<nl.getLength(); ++i) {
				String flrName = nl.item(i).getAttributes().getNamedItem("name").getNodeValue();
				String flrValue = String.valueOf(nl.item(i).getAttributes().getNamedItem("value").getNodeValue());
				
				Floor floor = new Floor(flrName, flrValue, "", true);
				
				expr = xpath.compile("/root/layout/floor[@name=\""+flrName+"\"]/image");
				NodeList images = (NodeList) expr.evaluate(mainWindow.getDoc(), XPathConstants.NODESET);
				
				String imgPath = images.item(0).getAttributes().getNamedItem("value").getNodeValue();
				ImageDrawing imgDrawing = new ImageDrawing(imgPath, flrValue, connection, this);

				expr = xpath.compile("/root/layout/floor[@name=\""+flrName+"\"]/table");
				NodeList tables = (NodeList) expr.evaluate(mainWindow.getDoc(), XPathConstants.NODESET);
				
				int x=0, y=0, width=0, height=0, value=0, rotate=0;
				for (int j=0; j<tables.getLength(); ++j) {
					try {
						x = Integer.parseInt(tables.item(j).getAttributes().getNamedItem("x").getNodeValue());
						y = Integer.parseInt(tables.item(j).getAttributes().getNamedItem("y").getNodeValue());
						width = (int) Double.parseDouble(tables.item(j).getAttributes().getNamedItem("width").getNodeValue());
						height = (int) Double.parseDouble(tables.item(j).getAttributes().getNamedItem("height").getNodeValue());
						value = Integer.parseInt(tables.item(j).getAttributes().getNamedItem("value").getNodeValue());
						rotate = Integer.parseInt(tables.item(j).getAttributes().getNamedItem("rot").getNodeValue());
					} catch (NullPointerException e) {
						x = 0;
						y = 0;
						width = 30;
						height = 30;
						rotate = 0;
						value = j + 1;
					} catch (NumberFormatException e) {
						x = 0;
						y = 0;
						width = 30;
						height = 30;
						rotate = 0;
						value = j + 1;
					}
					
					imgDrawing.addTable(x, y, width, height, rotate, String.valueOf(value), true, true);
				}
				
				allFloors.put(floor, imgDrawing);
				
			}
    	} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
    }
    
    public void saveToXmlAndDb() {
    	Thread t1 = new Thread() {
    		@Override
			public void run() {
    			changeTableAttributesInTree(mainWindow.getRoot());
		    }
    	};
    	Thread t2 = new Thread() {
    		@Override
			public void run() {
    			saveToDB();
    	    }
    	};
    	Thread t3 = new Thread() {
			 @Override
			 public void run() {
				 deleteTables();
			 }
		 };
		 
		t1.start();
		t2.start();
    	t3.start();
    	try {
    		t1.join();
    		t2.join();
			t3.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}    	
    }
    
    private void deleteTables() {
    	for (Map.Entry<Floor, ImageDrawing> map: allFloors.entrySet()) {
    		ArrayList<Tables> deletedTables = map.getValue().getDeletedTables();
    		if (deletedTables.size() == 0) {
    			continue;
    		}
    		Tables[] tables = map.getValue().getTables();
    		for (Tables t: deletedTables) {
				 t.delete(connection);	
    	   	} 

    		for (Tables t: tables) {
    	   		if (t != null) {
    	   			t.update(connection);
    	   		}
    	   	}		
    		map.getValue().setDeletedTables(new ArrayList<Tables>());
    	}	
	}
    
    private void saveToDB() {
		try {
    		PreparedStatement statement = connection.prepareStatement(SELECT_MAX_ID);
			ResultSet resultSet = statement.executeQuery();
			
			int id = resultSet.getInt(1);
			
			PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TO_LOCATION);
			PreparedStatement updateStatement = connection.prepareStatement(UPDATE_TO_LOCATION);
			
			
			Map<Floor, ImageDrawing> deleteFloors = new HashMap<>();
			
			for (Map.Entry<Floor, ImageDrawing> map: allFloors.entrySet()) {
				map.getKey().save(connection);
				if (map.getKey().isToDelete()) {
					deleteFloors.put(map.getKey(), map.getValue());
					continue;
				}
				
				String flrName = map.getKey().getTitle();
				int flrId = Integer.parseInt(map.getKey().getValue());
				
				Tables[] tables = map.getValue().getTables();
				int size = map.getValue().getNumOfRecs();
				for (int i=0; i<size; ++i) {
					tables[i].save(connection);
					int tableId = Integer.parseInt(tables[i].getValue());
					
					if (!tables[i].isInDbLocationTable()) {
						preparedStatement.setInt(1, id++);
						preparedStatement.setString(2, flrName);
						preparedStatement.setString(3, "2 ppl");
						preparedStatement.setString(4, "");
						preparedStatement.setString(5, tables[i].getCreatedDate());
						preparedStatement.setString(6, "");				
						preparedStatement.setString(7, tables[i].getLastModifiedDate());				
						preparedStatement.setInt(8, flrId);
						preparedStatement.setInt(9, tableId);
						
						preparedStatement.addBatch();
						tables[i].setInDbLocationTable(true);
					} else {
						updateStatement.setString(1, tables[i].getLastModifiedDate());
						updateStatement.setInt(2, flrId);
						updateStatement.setInt(3, tableId);
						updateStatement.addBatch();
					}
				}
				
				int[] results = preparedStatement.executeBatch();
				results = updateStatement.executeBatch();
			}
			
			for (Map.Entry<Floor, ImageDrawing> del: deleteFloors.entrySet()) {
				allFloors.remove(del.getKey(), del.getValue());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
	public MainWindow getMainWindow() {
		return mainWindow;
	}
	public void setMainWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}
	public boolean getIsFileLoaded() {
		return isFileLoaded;
	}
	public JScrollPane getScrollPane() {
		return scrollPane;
	}
	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}
	public void setIsFileLoaded(boolean isFileLoaded) {
		this.isFileLoaded = isFileLoaded;
		if (this.isFileLoaded == true) {
			loadFloorFromFile();
		}
	}
	public Map<Floor, ImageDrawing> getAllFloors() {
		return allFloors;
	}
}