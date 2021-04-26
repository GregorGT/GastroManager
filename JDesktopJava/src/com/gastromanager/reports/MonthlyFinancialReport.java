package com.gastromanager.reports;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import com.gastromanager.db.DbConnection;
import com.gastromanager.util.PropertiesUtil;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.container.NoSuchElementException;
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

	public static void main(String[] args) throws IllegalArgumentException, UnknownPropertyException, PropertyVetoException, WrappedTargetException, NoSuchElementException {
		
		Connection connectionDB = connectDB();
//		System.exit(0);
		
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
		
		System.out.println("Inserting tables...");
		
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
        
        System.out.println(startDate);
        System.out.println(endDate);
        
        int totalItems=0;
        
		try {
			PreparedStatement stmt = connectionDB.prepareStatement("SELECT quantity, xml, price FROM orderitem WHERE datetime>=? and datetime<=?");
		    stmt.setString(1, startDate);
		    stmt.setString(2, endDate);

	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	            String quantity = rs.getString("quantity");
	            String xml = rs.getString("xml");
	            String price = rs.getString("price");
	            System.out.println(quantity +" "+xml+ " " + price);
	            ++totalItems;
	        }
	        
	    } catch (SQLException e) {
			e.printStackTrace();
		}
		
		putTable("Income", totalItems);
		putTable("Expenses", totalItems);
		putTable("Most bought items", 5);
		putTable("Least bough items", 5);

		System.exit(0);		
	}
	
	public static void putTitle(String title) throws IllegalArgumentException, UnknownPropertyException, PropertyVetoException, WrappedTargetException {
		XPropertySet propertiesOfTextCursor = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTextCursor);
		propertiesOfTextCursor.setPropertyValue("CharWeight", com.sun.star.awt.FontWeight.BOLD);
		propertiesOfTextCursor.setPropertyValue("CharHeight", 14);
		propertiesOfTextCursor.setPropertyValue("ParaAdjust", com.sun.star.style.ParagraphAdjust.CENTER);
		xText.insertString(xTextCursor, "Monthly Financial Report\n", false);
		xText.insertControlCharacter(xTextCursor, ControlCharacter.PARAGRAPH_BREAK, false);
	}
	
	public static void putTable(String title, int rows) throws IllegalArgumentException, UnknownPropertyException, PropertyVetoException, WrappedTargetException, NoSuchElementException {
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
            connection = PDbConnection.getDbConnection().gastroDbConnection;//DriverManager.getConnection(url);
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
