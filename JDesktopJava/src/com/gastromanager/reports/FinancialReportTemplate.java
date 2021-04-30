package com.gastromanager.reports;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.gastromanager.db.DbConnection;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.text.ControlCharacter;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextTable;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

public class FinancialReportTemplate {
	private static XComponentContext xContext; 
	private static XText xText;
	private static XTextCursor xTextCursor;
	private static XTextDocument writerDocument;
	private static Connection connectionDB=null;
	private static Double totalRevenue=0.0;

	private static String queryForIncomeTable = "SELECT SUM(quantity) total_quantity, item_id, xml, price*(SUM(quantity)) total_price FROM orderitem WHERE datetime>=? and datetime<=? GROUP BY item_id";
	private static String queryForMostBoughtItemsTable = "SELECT SUM(quantity) total_quantity, item_id, xml, price*(SUM(quantity)) total_price FROM orderitem WHERE datetime>=? and datetime<=? GROUP BY item_id ORDER BY quantity ASC, total_price DESC LIMIT 5";
	private static String queryForLeastBoughtItemsTable = "SELECT SUM(quantity) total_quantity, item_id, xml, price*(SUM(quantity)) total_price FROM orderitem WHERE datetime>=? and datetime<=? GROUP BY item_id ORDER BY quantity DESC, total_price ASC LIMIT 5";
	private static String queryForTotalRevenue = "SELECT SUM(total_amount)+SUM(tips) revenue FROM transactions t JOIN orderitem o ON t.id=o.transaction_id WHERE datetime>=? and datetime<=?";
	
	public static void connectDB() {
		Connection connection = null;
        try {
            connection = DbConnection.getDbConnection().gastroDbConnection;
            System.out.println("Connection to SQLite has been established.");
        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }
        connectionDB = connection;
	}
	
	public static void openDocument() {
		xContext = null;
		
		try {
			xContext = Bootstrap.bootstrap();
		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.exit(1);
		}
		
		System.out.println("Opening an empty writer document...");
		writerDocument = openWriter(xContext);
	
		xText = writerDocument.getText();
		xTextCursor = xText.createTextCursor();
	}
	
	public static void putTitle(String title) throws IllegalArgumentException, UnknownPropertyException, PropertyVetoException, WrappedTargetException {
		XPropertySet propertiesOfTextCursor = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTextCursor);
		propertiesOfTextCursor.setPropertyValue("CharWeight", com.sun.star.awt.FontWeight.BOLD);
		propertiesOfTextCursor.setPropertyValue("CharHeight", 14);
		propertiesOfTextCursor.setPropertyValue("ParaAdjust", com.sun.star.style.ParagraphAdjust.CENTER);
		xText.insertString(xTextCursor, "Monthly Financial Report\n", false);
		xText.insertControlCharacter(xTextCursor, ControlCharacter.PARAGRAPH_BREAK, false);
	}
	
	public static void putDate(String date1, String date2) throws IllegalArgumentException, UnknownPropertyException, PropertyVetoException, WrappedTargetException {
		XPropertySet propertiesOfTextCursor = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTextCursor);
		propertiesOfTextCursor.setPropertyValue("CharWeight", com.sun.star.awt.FontWeight.BOLD);
		propertiesOfTextCursor.setPropertyValue("CharHeight", 14);
		propertiesOfTextCursor.setPropertyValue("ParaAdjust", com.sun.star.style.ParagraphAdjust.CENTER);
		xText.insertString(xTextCursor, date1+" - "+date2, false);
		xText.insertControlCharacter(xTextCursor, ControlCharacter.PARAGRAPH_BREAK, false);
	}
	
	public static void putRevenue(String startDate, String endDate) throws IllegalArgumentException, UnknownPropertyException, PropertyVetoException, WrappedTargetException {
		XPropertySet propertiesOfTextCursor = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTextCursor);
		propertiesOfTextCursor.setPropertyValue("CharWeight", com.sun.star.awt.FontWeight.BOLD);
		propertiesOfTextCursor.setPropertyValue("CharHeight", 14);
		propertiesOfTextCursor.setPropertyValue("ParaAdjust", com.sun.star.style.ParagraphAdjust.RIGHT);
		try {
			PreparedStatement stmt = connectionDB.prepareStatement(queryForTotalRevenue);
			stmt.setString(1, startDate);
			stmt.setString(2, endDate);
			ResultSet rs = stmt.executeQuery();
			totalRevenue = rs.getDouble("revenue");
		} catch (SQLException e) {
			e.printStackTrace();
		}
			
		
		String revenue = String.format("\nTotal Revenue: %.2f $", totalRevenue);
		xText.insertString(xTextCursor, revenue, false);
		xText.insertControlCharacter(xTextCursor, ControlCharacter.PARAGRAPH_BREAK, false);
	}
	
	public static XTextTable createAndFillIncomeTable(String startDate, String endDate) throws IllegalArgumentException, UnknownPropertyException, PropertyVetoException, WrappedTargetException, NoSuchElementException {
		List<Item> items = retreiveItemsFromDb(startDate, endDate, queryForIncomeTable);
		XTextTable incomeTable = putTable("Income", items.size());
		fillTable(items, incomeTable);
		return incomeTable;
	}
	
	public static XTextTable createAndFillMostBoughtItemsTable(String startDate, String endDate) throws IllegalArgumentException, UnknownPropertyException, PropertyVetoException, WrappedTargetException, NoSuchElementException {
		List<Item> items = retreiveItemsFromDb(startDate, endDate, queryForMostBoughtItemsTable);
		XTextTable mbiTable = putTable("Most Bought Items", items.size());
		fillTable(items, mbiTable);
		return mbiTable;
	}

	
	public static XTextTable createAndFillLeastBoughtItemsTable(String startDate, String endDate) throws IllegalArgumentException, UnknownPropertyException, PropertyVetoException, WrappedTargetException, NoSuchElementException {
		List<Item> items = retreiveItemsFromDb(startDate, endDate, queryForLeastBoughtItemsTable);
		XTextTable lbiTable = putTable("Least Bought Items", items.size());
		fillTable(items, lbiTable);
		return lbiTable;
	}
	
	private static void fillTable(List<Item> items, XTextTable table) {
		char []letters = new char[table.getColumns().getCount()];
		char letter = 'A';
		for (int i=0; i<letters.length; ++i, ++letter) {
			letters[i] = letter;
		}

		int ltr, row=2;
		for (Item it: items) {
			ltr=0;
			for (int i=0; i<table.getColumns().getCount(); ++i) {
				String cellName = String.valueOf(letters[ltr]);
				
				XText xTableText = (XText) UnoRuntime.queryInterface(XText.class, table.getCellByName(cellName+String.valueOf(row)));
				
				XTextCursor xTC = xTableText.createTextCursor();
				
				XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
				try {
					xTPS.setPropertyValue("CharHeight", 14);
					xTPS.setPropertyValue("ParaAdjust", com.sun.star.style.ParagraphAdjust.CENTER);
				} catch (Exception e) {
					System.err.println(" Exception " + e);
					e.printStackTrace(System.err);
				}
				
				switch (ltr) {
				case 0:
					xTableText.setString(it.getName().toUpperCase());
					break;
				case 1:
					xTableText.setString(String.valueOf(it.getQuantity()));
					break;
				case 2:
					xTableText.setString(String.valueOf(it.getPrice())+" $");
					break;
				default:
					break;
					
				}
				
				ltr++;
				if (ltr >= table.getColumns().getCount()) {
					ltr = 0;
				}	
			}
			
			row++;
			
		}
	}

	private static List< Item> retreiveItemsFromDb(String startDate, String endDate, String query) {
		List<Item> items = new LinkedList<>();
        try {
			PreparedStatement stmt = connectionDB.prepareStatement(query);
		    stmt.setString(1, startDate);
		    stmt.setString(2, endDate);
	
	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	        	Integer quantity = Integer.parseInt(rs.getString("total_quantity"));
	        	String item_id = rs.getString("item_id");
	        	String xml = parseXml(rs.getString("xml"));
	            Double price = Double.parseDouble(rs.getString("total_price"));
	            items.add(new Item(xml, quantity, price, item_id));
	        }   	        
	    } catch (SQLException e) {
			e.printStackTrace();
	    }
        return items;
    }
	
	private static String parseXml(String xml) {
		String itemName = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document = null;
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(new InputSource(new StringReader(xml)));
			itemName = parse(document, document.getDocumentElement());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Item name= "+itemName);
		
		return itemName;
	}
	

    private static String parse(Document doc, Element e) {
        String itemName = "";
    	NodeList children = e.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
        	Node n = children.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
            	NamedNodeMap attributes = n.getAttributes();
            	if (n.getNodeName().equals("item")) {
	            	if (attributes.getNamedItem("menu_id") != null) {
	            		if (attributes.getNamedItem("name") != null) {
	            			itemName = attributes.getNamedItem("name").getNodeValue();
	            			return itemName;
	            		}
	            	}
            	}            	
            	itemName = parse(doc, (Element) n);
            }
        }
        return itemName;
    }

	private static XTextTable putTable(String title, int rows) throws IllegalArgumentException, UnknownPropertyException, PropertyVetoException, WrappedTargetException, NoSuchElementException {
		XPropertySet propertiesOfTextCursor = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTextCursor);
		propertiesOfTextCursor.setPropertyValue("ParaAdjust", com.sun.star.style.ParagraphAdjust.LEFT);
		xText.insertString(xTextCursor, "\n"+title, false);
		
		
		XMultiServiceFactory xMultiServiceFactory =  (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class, writerDocument);
		XTextTable xTextTable = null;
		
		try {
			Object oInt = xMultiServiceFactory.createInstance("com.sun.star.text.TextTable");
			xTextTable = (XTextTable) UnoRuntime.queryInterface(XTextTable.class, oInt);
		} catch (Exception e) {
			System.err.println("Couldn't create instance " + e);
            e.printStackTrace(System.err);
		}
		
		xTextTable.initialize(rows+1, 3);
		
		try {
			xText.insertTextContent(xTextCursor, xTextTable, false);
			insertTitles("A1","Item Name", xTextTable);
			insertTitles("B1","Amount Sold", xTextTable);
			insertTitles("C1","Income", xTextTable);
			
		} catch (Exception e) {
			System.err.println("Couldn't insert the table " + e);
            e.printStackTrace(System.err);
		}

		xText.insertControlCharacter(xTextCursor, ControlCharacter.PARAGRAPH_BREAK, false);
		return xTextTable;
	}
	
	private static void insertTitles(String CellName, String theText, XTextTable xTTbl) {
		XText xTableText = (XText) UnoRuntime.queryInterface(XText.class,
		         xTTbl.getCellByName(CellName));
		
		XTextCursor xTC = xTableText.createTextCursor();
		
		XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
		
		try {
			xTPS.setPropertyValue("CharWeight", com.sun.star.awt.FontWeight.BOLD);
			xTPS.setPropertyValue("CharHeight", 14);
			xTPS.setPropertyValue("ParaAdjust", com.sun.star.style.ParagraphAdjust.CENTER);
		} catch (Exception e) {
			System.err.println(" Exception " + e);
			e.printStackTrace(System.err);
		}
		
		xTableText.setString( theText );
	}
	
	private static XTextDocument openWriter(XComponentContext xContext) {
		XComponentLoader xLoader = null;
		XTextDocument xDocument = null;
		XComponent xComponent = null;
		
		try {
			XMultiComponentFactory xMultiComponentFactory = xContext.getServiceManager();
			
			Object oDesktop = xMultiComponentFactory.createInstanceWithContext("com.sun.star.frame.Desktop", xContext);
			xLoader = (XComponentLoader) UnoRuntime.queryInterface(XComponentLoader.class, oDesktop);

			PropertyValue[] szEmptyArgs = new PropertyValue[0];
			
			String strDoc = "private:factory/swriter";
			xComponent = xLoader.loadComponentFromURL(strDoc, "_blank", 0, szEmptyArgs);
			
			xDocument = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, xComponent);
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		
		return xDocument;
	}	
}
