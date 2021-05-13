package com.gastromanager.mainwindow;

import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.gastromanager.db.DbConnection;
import com.gastromanager.models.OrderItem;
import com.gastromanager.util.DbUtil;

public class OrderingMenu extends JPanel {

	private JTextField txtFieldTable;
	private JTextField txtFieldChair;
	private JTextField txtFieldWaiter;
	private JTextField tfOrderID;
	private JTextField tfMenuID;
	private JList<Node> list;
	public JComboBox<GMTreeItem> ddChoice;
	private DefaultListModel<Node> listModel = new DefaultListModel<Node>();
	private DrillDownGroup mainDrillDownGrp;
	public DbUtil dbUtil;
	
	/**
	 * @wbp.parser.constructor
	 */
	public OrderingMenu(Connection connection, HashSet<GMTreeItem> items, DrillDownMenu ddmenu) { 
	    JLabel lblTable = new JLabel("Table: ");
	    lblTable.setBounds(20, 11, 46, 14);
	    this.add(lblTable);
	    
	    txtFieldTable = new JTextField();
	    txtFieldTable.setBounds(20, 24, 60, 20);
	    this.add(txtFieldTable);
	    txtFieldTable.setColumns(10);
	    
	    JLabel lblChair = new JLabel("Chair: ");
	    lblChair.setBounds(90, 11, 46, 14);
	    this.add(lblChair);
	    
	    txtFieldChair = new JTextField();
	    txtFieldChair.setBounds(90, 24, 60, 20);
	    this.add(txtFieldChair);
	    txtFieldChair.setColumns(10);
	    
	    JLabel lblWaiter = new JLabel("Waiter: ");
	    lblWaiter.setBounds(160, 11, 46, 14);
	    this.add(lblWaiter);
	    
	    txtFieldWaiter = new JTextField();
	    txtFieldWaiter.setBounds(160, 24, 60, 20);
	    this.add(txtFieldWaiter);
	    txtFieldWaiter.setColumns(10);
	    
	    list = new JList<Node>(listModel);
	    list.setCellRenderer(new ListItemStyler());
	    
	    JScrollPane sPane = new JScrollPane(list);
	    sPane.setBounds(10, 55, 236, 276);
	    this.add(sPane);
	    
	    JLabel lblOrderID = new JLabel("Order ID");
	    lblOrderID.setBounds(256, 56, 68, 14);
	    this.add(lblOrderID);
	    
	    tfOrderID = new JTextField();
	    tfOrderID.setBounds(256, 76, 109, 20);
	        
	    
	    
	    tfOrderID.getDocument().addDocumentListener(new DocumentListener() {
	    	  
	    	public void changedUpdate(DocumentEvent e) {
	    		//apparently not used in plain text fields
	    		System.out.println("change update");
	    	}
	    	public void removeUpdate(DocumentEvent e) {
//	    		ResultSet a = makeQuery(connection, tfOrderID.getText());
	    		
	    		
				System.out.println(tfOrderID.getText());
				warn();
//	    		System.out.println("remove update");
	    	}
	    	public void insertUpdate(DocumentEvent e) {
//	    		 ResultSet a = null;
//	    		 System.out.println("insert update");
//	    		 a = makeQuery(connection, tfOrderID.getText());
	    		List <OrderItem> list = dbUtil.getOrderDetails(tfOrderID.getText());
	    		list.forEach((item) -> {
	    			System.out.println(item.getItemId() + " <- ID" +
	    								item.getQuantity());
	    		});
	    	}

	    		  public void warn() {
	    		     if (tfOrderID.getText().isBlank()){
	    		       JOptionPane.showMessageDialog(null,
	    		          "Error: Please enter number bigger than 0", "Error Message",
	    		          JOptionPane.ERROR_MESSAGE);
	    		     }
	    		  }
	    		});
	    
	    
        
	    
	    this.add(tfOrderID);
	    tfOrderID.setColumns(10);
	    
	    JButton lblSelectOrderID = new JButton("Select Order ID");
	    lblSelectOrderID.setBounds(256, 101, 109, 23);
	    this.add(lblSelectOrderID);
	    
	    JButton btnPrevious = new JButton("<-");
	    btnPrevious.setBounds(256, 135, 50, 23);
	    this.add(btnPrevious);
	    
	    JButton btnNext = new JButton("->");
	    btnNext.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    	}
	    });
	    btnNext.setBounds(315, 135, 50, 23);
	    this.add(btnNext);
	    
	    JButton btnNewOrderID = new JButton("New Order ID");
	    btnNewOrderID.setBounds(256, 169, 111, 50);
	    this.add(btnNewOrderID);
	    
	    ddChoice = new JComboBox<GMTreeItem>();
	    ddChoice.setBounds(406, 50, 120, 20);
	    
	    ddChoice.addActionListener(new ActionListener() {
	    	String name = ""; String height = ""; String width = "";
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (((GMTreeItem) ddChoice.getSelectedItem()).getTree() != null) {
				
				System.out.println(((GMTreeItem) ddChoice.getSelectedItem()).getTree().toString());
				GMTreeItem newItem = (GMTreeItem) ddChoice.getSelectedItem();
				
				HashMap<String, String> attrs = newItem.getAttributes();
				
				attrs.forEach((k,v) -> {
					if (k == "name")
						 name = v;
					if(k == "height") 
						height = v;
					if(k == "width")
						width = v;
				});
				
				DrillDownGroup newDD = new DrillDownGroup(10, 10, name, ddmenu);
				newDD.setBounds(10, 340 , Integer.parseInt(width), Integer.parseInt(height));
				OrderingMenu.this.add(newDD);
				OrderingMenu.this.revalidate();
				OrderingMenu.this.repaint();
				
	    }
			}
	    	
	    });
	    
	    this.add(ddChoice);
	    
	    JLabel lblDrillDownOpts = new JLabel("Drill Down Options: ");
	    lblDrillDownOpts.setBounds(406, 27, 120, 14);
	    this.add(lblDrillDownOpts);
	    
	    tfMenuID = new JTextField();
	    tfMenuID.setBounds(406, 102, 120, 20);
	    this.add(tfMenuID);
	    tfMenuID.setColumns(10);
	    
	    JLabel lblMenuID = new JLabel("Menu ID:");
	    lblMenuID.setBounds(406, 79, 60, 14);
	    this.add(lblMenuID);
	    
	    JButton btnSelectMenuID = new JButton("Select Menu ID");
	    btnSelectMenuID.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    	}
	    });
	    btnSelectMenuID.setBounds(406, 135, 120, 23);
	    this.add(btnSelectMenuID);
	    
//	    JScrollPane scrollPane = new JScrollPane();
//		scrollPane.setBounds(10, 340, 1000, 1000);
//		this.add(scrollPane);
	    
//	    mainDrillDownGrp.setLocation(10,  340);
	}	
	
	
	ResultSet makeQuery(Connection conn, String text) {
		if (text.isBlank())
			return null;
		ResultSet epin = null;
		if (!listModel.isEmpty()) {
			listModel.removeAllElements();
		} else {
		HashSet<String> xmlStrings = new HashSet<String>();
		HashSet<Integer> itemQuantity = new HashSet<Integer>();
		try {
            conn = DbConnection.getDbConnection().gastroDbConnection;
            PreparedStatement stmt = conn.prepareStatement("select * from orderitem join orders on orders.id = orderitem.order_id where orders.id=?"); //where humanreadable_id=?");            
            stmt.setInt(1,Integer.parseInt(text));
            epin = stmt.executeQuery();
            
            while (epin.next()) {
            	xmlStrings.add(epin.getString("xml"));
            	itemQuantity.add(epin.getInt("quantity"));
            }
            parseXmlFromQuery(xmlStrings, itemQuantity);
            
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
		}
		return epin;
	}
	
	void parseXmlFromQuery(HashSet<String> xmlStrings, HashSet<Integer> quantity) {
		
		HashMap<String, Integer> queryResults = new HashMap<String, Integer>();
		//fuse both hashSets into the map
		
		xmlStrings.forEach((item) -> {
			
			Document doc = convertStringToDocument(item);
			
			documentCons(doc.getFirstChild());
			
		});
		
	}
	
	void documentCons(Node node) {
		
		if (node.getNodeName().contains("#"))
			return;
		
		listModel.addElement(node);
		
		while (node.getNextSibling() != null) {
			documentCons(node.getNextSibling());
		}
		
	}
	
	private class ListItemStyler extends DefaultListCellRenderer {

//		HashSet<String> options = new HashSet<String>();
		
        @Override
        public Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            Node source = (Node) value;
            
            String labelText = "<html>";
            
//            options.forEach((item) -> {
//            	System.out.println(item);
//            	finalLabel += item + newline;
//            });	
         
            String finalS = recursiveChildSearch(source);
            
            setText(labelText + finalS);
            return this;
        }
        
        String recursiveChildSearch(Node parent) {
        	String labelText = "";
        	String spacer = "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ";
            String newline = "<br/>";
            
            if (parent.getParentNode().getNodeType() == Node.DOCUMENT_NODE) {
            	labelText += parent.getAttributes().getNamedItem("name").getNodeValue() + newline;
            }
            
            if (parent.getNodeType() == Node.ELEMENT_NODE && parent.getParentNode().getNodeType() != Node.DOCUMENT_NODE) {
             	
//            	System.out.println(parent.getParentNode().getNodeName());
            	labelText += spacer + parent.getAttributes().getNamedItem("name").getNodeValue() + newline;
             }

            for (int j = 0; j < parent.getChildNodes().getLength(); j++) {	
            	labelText += recursiveChildSearch(parent.getChildNodes().item(j));
            }
            
            
            return labelText;
        }
    }
	
	private static String convertDocumentToString(Document doc) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String output = writer.getBuffer().toString();
            return output;
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        
        return null;
    }
	
	 private static Document convertStringToDocument(String xmlStr) {
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	        DocumentBuilder builder;  
	        try  
	        {  
	            builder = factory.newDocumentBuilder();  
	            Document doc = builder.parse(new InputSource(new StringReader(xmlStr))); 
	            return doc;
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } 
	        return null;
	    }

	public OrderingMenu(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	public OrderingMenu(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public OrderingMenu(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

}

