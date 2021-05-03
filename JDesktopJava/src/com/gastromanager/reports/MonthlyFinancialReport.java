package com.gastromanager.reports;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.NoSuchElementException;

import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.text.XTextTable;

public class MonthlyFinancialReport {
	public static void main(String[] args) throws IllegalArgumentException, UnknownPropertyException, PropertyVetoException, WrappedTargetException, NoSuchElementException {
		FinancialReportTemplate template = new FinancialReportTemplate();
		
		template.connectDB();
		template.openDocument();
		template.putTitle("Monthly Financial Report");
		
		Calendar gc = new GregorianCalendar();
        gc.set(Calendar.MONTH, gc.get(Calendar.MONTH)); // -1
        gc.set(Calendar.DAY_OF_MONTH, 1);
        Date monthStart = gc.getTime();
        gc.add(Calendar.MONTH, 1);
        gc.add(Calendar.DAY_OF_MONTH, -1);
        Date monthEnd = gc.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = format.format(monthStart) + " 00:00:00";
        String endDate = format.format(monthEnd) + " 23:59:59";  
        
//        System.out.println(startDate);
//        System.out.println(endDate);
        
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        
        
        template.putDate(f.format(monthStart), f.format(monthEnd));
        System.out.println("Inserting tables...");
		
		XTextTable incomeTable = template.createAndFillIncomeTable(startDate, endDate);
		XTextTable mostBoughtItemsTable = template.createAndFillMostBoughtItemsTable(startDate, endDate);
		XTextTable leastBoughtItemsTable = template.createAndFillLeastBoughtItemsTable(startDate, endDate);
		
		template.putRevenue(startDate, endDate);
		
		System.exit(0);		
	}
}
