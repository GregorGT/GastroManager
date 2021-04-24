package com.gastromanager.reports;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
		
//		retreiveItemsFromDB();
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
		
		System.out.println("Inserting a table...");
		putTable("Income", 3, 3);
		putTable("Exepenses", 3, 3);
		putTable("Most bought items", 3, 4);
		putTable("Lest bough items", 3, 4);

		System.exit(0);		
	}
	
	public static void retreiveItemsFromDB() {
//		try {
//            Connection connection = DbConnection.getDbConnection().gastroDbConnection;
//            System.out.println(connection);
//            PreparedStatement stmt=connection.prepareStatement("select * from orderitem where order_id=?");
//            
////            stmt.setInt(1,1);
////            ResultSet result = stmt.executeQuery();
//            //System.out.println(stmt.executeQuery().findColumn("quantity"));
////            orderItems = loadResults(result);
//        } catch (Exception sqlException) {
//            sqlException.printStackTrace();
////            orderItems = null;
//        }
	}
	
	
	public static void putTitle(String title) throws IllegalArgumentException, UnknownPropertyException, PropertyVetoException, WrappedTargetException {
		XPropertySet propertiesOfTextCursor = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTextCursor);
		propertiesOfTextCursor.setPropertyValue("CharWeight", com.sun.star.awt.FontWeight.BOLD);
		propertiesOfTextCursor.setPropertyValue("CharHeight", 14);
		propertiesOfTextCursor.setPropertyValue("ParaAdjust", com.sun.star.style.ParagraphAdjust.CENTER);
		xText.insertString(xTextCursor, "Monthly Financial Report\n", false);
		xText.insertControlCharacter(xTextCursor, ControlCharacter.PARAGRAPH_BREAK, false);
	}
	
	public static void putTable(String title, int rows, int columns) throws IllegalArgumentException, UnknownPropertyException, PropertyVetoException, WrappedTargetException, NoSuchElementException {
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
		
		xTextTable.initialize(rows, columns);
		
		XPropertySet firstRowProperties = null;
		try {
			xText.insertTextContent(xTextCursor, xTextTable, false);
		
		} catch (Exception e) {
			System.err.println("Couldn't insert the table " + e);
            e.printStackTrace(System.err);
		}

		xText.insertControlCharacter(xTextCursor, ControlCharacter.PARAGRAPH_BREAK, false);
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
}
