package com.gastromanager.reports;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.TimeZone;

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
import com.gastromanager.util.PropertiesUtil;
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

public class MonthlyFinancialReport {
	static XComponentContext xContext; 
	static XText xText;
	static XTextCursor xTextCursor;
	static XTextDocument writerDocument;
	static Connection connectionDB=null;
	static Double totalRevenue=0.0;
	static ArrayList<Items> items = new ArrayList<>();
	
	public static void main(String[] args) throws IllegalArgumentException, UnknownPropertyException, PropertyVetoException, WrappedTargetException, NoSuchElementException {
		connectionDB = connectDB();

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
		
		System.out.println("Writing title...");
		putTitle("Monthly Financial Report");
		
//		HashMap<String, ArrayList<Double>> results = retreiveDataFromDb(); 
		HashMap<String, Items> results = retreiveDataFromDb(); 

		System.out.println("Inserting tables...");
		XTextTable incomeTable = putTable("Income", results.size());
		XTextTable mbiTable = putTable("Most bought items", 5);
		XTextTable lbiTable = putTable("Least bough items", 5);

		fillTable(results, incomeTable);
		fillMostBoughtItemsTable(results, mbiTable);
		fillLeastBoughtItemsTable(results, lbiTable);
		
		
		insertRevenue();
		
		System.exit(0);		
	}

	private static void insertRevenue() {
		XPropertySet propertiesOfTextCursor = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTextCursor);
		try {
			propertiesOfTextCursor.setPropertyValue("CharWeight", com.sun.star.awt.FontWeight.BOLD);
			propertiesOfTextCursor.setPropertyValue("CharHeight", 14);
			propertiesOfTextCursor.setPropertyValue("ParaAdjust", com.sun.star.style.ParagraphAdjust.RIGHT);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (UnknownPropertyException e) {
			e.printStackTrace();
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		} catch (WrappedTargetException e) {
			e.printStackTrace();
		}
		String revenue = "\nTotal Revenue: "+totalRevenue+" $";
		xText.insertString(xTextCursor, revenue, false);
		xText.insertControlCharacter(xTextCursor, ControlCharacter.PARAGRAPH_BREAK, false);
	}
	


	private static void fillMostBoughtItemsTable(HashMap<String, Items> data, XTextTable xTextTable) {
		List<Entry<String, Items>> list = new LinkedList<>(data.entrySet());
		for (Entry<String, Items> l: list) {
			System.out.println(l.getKey() + " " + l.getValue().getPrice());
		}
		Collections.sort(list, new Comparator<Map.Entry<String, Items> >() {
	            public int compare(Map.Entry<String, Items> o1, 
	                               Map.Entry<String, Items> o2)
	            {
	                return (o1.getValue().getPrice().compareTo(o2.getValue().getPrice()));
	            }
        });
		for (Entry<String, Items> l: list) {
			System.out.println(l.getKey() + " " + l.getValue().getPrice());
		}

		
		char []letters = new char[xTextTable.getColumns().getCount()];
		char letter = 'A';
		for (int i=0; i<letters.length; ++i, ++letter) {
			letters[i] = letter;
		}

		
		int ltr, row=2;
		for (int j=list.size()-1; j>=(list.size()>5?list.size()-5:0); --j) {
			ltr=0;
			for (int i=0; i<xTextTable.getColumns().getCount(); ++i) {
				String cellName = String.valueOf(letters[ltr]);
				
				XText xTableText = (XText) UnoRuntime.queryInterface(XText.class, xTextTable.getCellByName(cellName+String.valueOf(row)));
				
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
					xTableText.setString(list.get(j).getKey());
					break;
				case 1:
					xTableText.setString(String.valueOf(list.get(j).getValue().getQuantity()));
					break;
				case 2:
					xTableText.setString(String.valueOf(list.get(j).getValue().getPrice())+" $");
					break;
				default:
					break;
					
				}
				
				ltr++;
				if (ltr >= xTextTable.getColumns().getCount()) {
					ltr = 0;
				}	
			}
			
			row++;
			
		}
	}

	
	private static void fillLeastBoughtItemsTable(HashMap<String, Items> data, XTextTable xTextTable) {
		List<Entry<String, Items>> list = new LinkedList<>(data.entrySet());
		for (Entry<String, Items> l: list) {
			System.out.println(l.getKey() + " " + l.getValue().getPrice());
		}
		Collections.sort(list, new Comparator<Map.Entry<String, Items> >() {
	            public int compare(Map.Entry<String, Items> o1, 
	                               Map.Entry<String, Items> o2)
	            {
	                return (o1.getValue().getPrice().compareTo(o2.getValue().getPrice()));
	            }
        });
		for (Entry<String, Items> l: list) {
			System.out.println(l.getKey() + " " + l.getValue().getPrice());
		}

		
		char []letters = new char[xTextTable.getColumns().getCount()];
		char letter = 'A';
		for (int i=0; i<letters.length; ++i, ++letter) {
			letters[i] = letter;
		}

		
		int ltr, row=2;
		for (int j=0; j<(list.size()>5?5:list.size()); ++j) {
			ltr=0;
			for (int i=0; i<xTextTable.getColumns().getCount(); ++i) {
				String cellName = String.valueOf(letters[ltr]);
				
				XText xTableText = (XText) UnoRuntime.queryInterface(XText.class, xTextTable.getCellByName(cellName+String.valueOf(row)));
				
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
					xTableText.setString(list.get(j).getKey());
					break;
				case 1:
					xTableText.setString(String.valueOf(list.get(j).getValue().getQuantity()));
					break;
				case 2:
					xTableText.setString(String.valueOf(list.get(j).getValue().getPrice())+" $");
					break;
				default:
					break;
					
				}
				
				ltr++;
				if (ltr >= xTextTable.getColumns().getCount()) {
					ltr = 0;
				}	
			}
			
			row++;
			
		}
	}
	
	private static void fillTable(/*HashMap<String, ArrayList<Double>> data*/HashMap<String, Items> data, XTextTable xTextTable) {
		char []letters = new char[xTextTable.getColumns().getCount()];
		char letter = 'A';
		for (int i=0; i<letters.length; ++i, ++letter) {
			letters[i] = letter;
		}

		
		int ltr, row=2;
		for (Map.Entry<String, Items> it: data.entrySet()) {
			System.out.println(it.getKey() + "-" + it.getValue().getQuantity() + "-" + it.getValue().getPrice());
			ltr=0;
			for (int i=0; i<xTextTable.getColumns().getCount(); ++i) {
				String cellName = String.valueOf(letters[ltr]);
				
				XText xTableText = (XText) UnoRuntime.queryInterface(XText.class, xTextTable.getCellByName(cellName+String.valueOf(row)));
				
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
					xTableText.setString(it.getKey());
					break;
				case 1:
					xTableText.setString(String.valueOf(it.getValue().getQuantity()));
					break;
				case 2:
					xTableText.setString(String.valueOf(it.getValue().getPrice())+" $");
					break;
				default:
					break;
					
				}
				
				ltr++;
				if (ltr >= xTextTable.getColumns().getCount()) {
					ltr = 0;
				}	
			}
			
			row++;
			
		}		

		//		for (Map.Entry<String, ArrayList<Double>> it: data.entrySet()) {
//			System.out.println(it.getKey() + "-" + it.getValue().get(0) + "-" + it.getValue().get(1));
//			ltr=0;
//			
//			for (int i=0; i<xTextTable.getColumns().getCount(); ++i) {
//				String cellName = String.valueOf(letters[ltr]);
//				
//				XText xTableText = (XText) UnoRuntime.queryInterface(XText.class, xTextTable.getCellByName(cellName+String.valueOf(row)));
//				
//				XTextCursor xTC = xTableText.createTextCursor();
//				
//				XPropertySet xTPS = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTC);
//				try {
//					xTPS.setPropertyValue("CharHeight", 14);
//					xTPS.setPropertyValue("ParaAdjust", com.sun.star.style.ParagraphAdjust.CENTER);
//				} catch (Exception e) {
//					System.err.println(" Exception " + e);
//					e.printStackTrace(System.err);
//				}
//				
//				switch (ltr) {
//				case 0:
//					xTableText.setString(it.getKey());
//					break;
//				case 1:
//					xTableText.setString(String.valueOf(it.getValue().get(0).intValue()));
//					break;
//				case 2:
//					xTableText.setString(String.valueOf(it.getValue().get(1))+" $");
//					break;
//				default:
//					break;
//					
//				}
//				
//				ltr++;
//				if (ltr >= xTextTable.getColumns().getCount()) {
//					ltr = 0;
//				}	
//			}
//			
//			row++;
//			
//		}		

	}
	
	private static String parseXml(String xml) {
		String itemName = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(xml)));
			Element root = document.getDocumentElement();
			itemName = root.getAttribute("name");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return itemName;
	}
	
	public static void putDate(String date1, String date2) throws IllegalArgumentException, UnknownPropertyException, PropertyVetoException, WrappedTargetException {
		XPropertySet propertiesOfTextCursor = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTextCursor);
		propertiesOfTextCursor.setPropertyValue("CharWeight", com.sun.star.awt.FontWeight.BOLD);
		propertiesOfTextCursor.setPropertyValue("CharHeight", 14);
		propertiesOfTextCursor.setPropertyValue("ParaAdjust", com.sun.star.style.ParagraphAdjust.CENTER);
		xText.insertString(xTextCursor, date1+" - "+date2, false);
		xText.insertControlCharacter(xTextCursor, ControlCharacter.PARAGRAPH_BREAK, false);
	}
	
	private static HashMap<String, Items> retreiveDataFromDb() {
		Calendar gc = new GregorianCalendar();
        gc.set(Calendar.MONTH, gc.get(Calendar.MONTH));
        gc.set(Calendar.DAY_OF_MONTH, 1);
        Date monthStart = gc.getTime();
        gc.add(Calendar.MONTH, 1);
        gc.add(Calendar.DAY_OF_MONTH, -1);
        Date monthEnd = gc.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = format.format(monthStart) + " 00:00:00";
        String endDate = format.format(monthEnd) + " 23:59:59";  
        
        try {
			putDate(format.format(monthStart), format.format(monthEnd));
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnknownPropertyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (PropertyVetoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (WrappedTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        HashMap<String, Items> results = new HashMap<>();
		
        try {
			PreparedStatement stmt = connectionDB.prepareStatement("SELECT quantity, xml, price FROM orderitem WHERE datetime>=? and datetime<=?");
		    stmt.setString(1, startDate);
		    stmt.setString(2, endDate);

	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	        	Integer quantity = Integer.parseInt(rs.getString("quantity"));
	            String xml = parseXml(rs.getString("xml"));
	            Double price = Double.parseDouble(rs.getString("price"));
	            totalRevenue += price;
	            
	            if (!results.containsKey(xml)) {
	            	results.put(xml, new Items(xml));
	            }
	            results.get(xml).setQuantity(results.get(xml).getQuantity()+quantity);
	            results.get(xml).setPrice(results.get(xml).getPrice()+price);
	        }   	        
	    } catch (SQLException e) {
			e.printStackTrace();
	    }

        
        return results;
        
//        try {
//			PreparedStatement stmt = connectionDB.prepareStatement("SELECT quantity, xml, price FROM orderitem WHERE datetime>=? and datetime<=?");
//		    stmt.setString(1, startDate);
//		    stmt.setString(2, endDate);
//
//	        ResultSet rs = stmt.executeQuery();
//	        while (rs.next()) {
//	 
//	        	Double quantity = Double.parseDouble(rs.getString("quantity"));
//	            String xml = rs.getString("xml");
//	            Double price = Double.parseDouble(rs.getString("price"));
//	            totalRevenue += price;
//	            
//	            String name = parseXml(xml);
//	            ArrayList<Double> values = new ArrayList<>();
//	            values.add(quantity);
//	            values.add(price);
//	            
//	            if (results.containsKey(name)) {
//	            	values = results.get(name);
//	            	values.set(0, values.get(0)+quantity);
//	            	values.set(1, values.get(1)+price);
//	            } 
//
//	            results.put(name, values);
//	        }
//	        
//	    } catch (SQLException e) {
//			e.printStackTrace();
//	    }
//
//		return results;
	}
	
	public static void putTitle(String title) throws IllegalArgumentException, UnknownPropertyException, PropertyVetoException, WrappedTargetException {
		XPropertySet propertiesOfTextCursor = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTextCursor);
		propertiesOfTextCursor.setPropertyValue("CharWeight", com.sun.star.awt.FontWeight.BOLD);
		propertiesOfTextCursor.setPropertyValue("CharHeight", 14);
		propertiesOfTextCursor.setPropertyValue("ParaAdjust", com.sun.star.style.ParagraphAdjust.CENTER);
		xText.insertString(xTextCursor, "Monthly Financial Report\n", false);
		xText.insertControlCharacter(xTextCursor, ControlCharacter.PARAGRAPH_BREAK, false);
	}
	
	public static XTextTable putTable(String title, int rows) throws IllegalArgumentException, UnknownPropertyException, PropertyVetoException, WrappedTargetException, NoSuchElementException {
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
		
//		XPropertySet firstRowProperties = null;
		try {
			xText.insertTextContent(xTextCursor, xTextTable, false);
			insertTitles("A1","Item Name", xTextTable);
//			insertTitles("B1","Item Id", xTextTable);
			insertTitles("B1","Amount Sold", xTextTable);
			insertTitles("C1","Income", xTextTable);
			
			//TODO: fill the tables with values
			
		} catch (Exception e) {
			System.err.println("Couldn't insert the table " + e);
            e.printStackTrace(System.err);
		}

		xText.insertControlCharacter(xTextCursor, ControlCharacter.PARAGRAPH_BREAK, false);
		return xTextTable;
	}
	
	public static void insertTitles(String CellName, String theText,
            com.sun.star.text.XTextTable xTTbl) {

		XText xTableText = (XText) UnoRuntime.queryInterface(XText.class,
		         xTTbl.getCellByName(CellName));
		
		//create a cursor object
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
		
		//inserting some Text
		xTableText.setString( theText );
		
	}
	
	public static XTextDocument openWriter(XComponentContext xContext) {
		
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
	
	public static Connection connectDB() {
		Connection connection = null;
        try {
            connection = DbConnection.getDbConnection().gastroDbConnection;//DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (Exception sqlException) {
            sqlException.printStackTrace();
        }
        return connection;
	}
	
	/*Added another same class for dbconnection because the other one is not working on linux.*/
	private static class PDbConnection {
	    private static PDbConnection dbConnection = null;
	    public Connection gastroDbConnection;

	    private PDbConnection() {
	        try {
	            String url = "jdbc:sqlite:"+ System.getProperty("user.dir")+ "/gastrodb/gastromanager.db";
	            gastroDbConnection = DriverManager.getConnection(url);
	            System.out.println("Connection to Gastro Database has been established.");
	        } catch (SQLException sqlException) {
	            sqlException.printStackTrace();
	        }
	    }

	    public static  PDbConnection getDbConnection() {
	        if(null == dbConnection) {
	            dbConnection = new PDbConnection();
	        }

	        return dbConnection;
	    }
	}
	
}
