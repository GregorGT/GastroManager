package com.gastromanager.reports;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.text.XTextTable;

public class DailyFinancialReport {

	public static void main(String[] args) throws IllegalArgumentException, UnknownPropertyException, PropertyVetoException, WrappedTargetException {
		FinancialReportTemplate.connectDB();
		FinancialReportTemplate.openDocument();
		FinancialReportTemplate.putTitle("Daily Financial Report");
		

		//TODO: today's date
		
        
        FinancialReportTemplate.putDate(f.format(monthStart), f.format(monthEnd));
        System.out.println("Inserting tables...");
		
		XTextTable incomeTable = FinancialReportTemplate.createAndFillIncomeTable(startDate, endDate);
		XTextTable mostBoughtItemsTable = FinancialReportTemplate.createAndFillMostBoughtItemsTable(startDate, endDate);
		XTextTable leastBoughtItemsTable = FinancialReportTemplate.createAndFillLeastBoughtItemsTable(startDate, endDate);
		
		FinancialReportTemplate.putRevenue(startDate, endDate);
		
		System.exit(0);		
	}
}
