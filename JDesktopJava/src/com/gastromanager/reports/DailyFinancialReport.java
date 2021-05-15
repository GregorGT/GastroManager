package com.gastromanager.reports;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.gastromanager.util.PropertiesUtil;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.text.XTextTable;

public class DailyFinancialReport {

	public static void main(String[] args) throws IllegalArgumentException, UnknownPropertyException, PropertyVetoException, WrappedTargetException {
		FinancialReportTemplate template = new FinancialReportTemplate();
		
		template.connectDB();
		template.openDocument();
		template.putTitle("Daily Financial Report");
	
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String d = sdf.format(date);
		String startDate = d + " 00:00:00";
		String endDate = d + " 23:59:59";

//		String startDate = "2021-04-16 00:00:00";
//		String endDate = "2021-04-16 23:59:59";
		
		sdf = new SimpleDateFormat("dd-MM-yyyy");
        String today = sdf.format(date);
		
        template.putDate(today, null);
        System.out.println("Inserting tables...");
		
        int incomeTableTotalItems = Integer.parseInt(PropertiesUtil.getPropertyValue("allItemCountReport"));
        int mbiTableTotalItems = Integer.parseInt(PropertiesUtil.getPropertyValue("moustBoughItemsReport"));
        int lbiTableTotalItems = Integer.parseInt(PropertiesUtil.getPropertyValue("leastBoughItemsReport"));
        
        
        XTextTable incomeTable = template.createAndFillIncomeTable(startDate, endDate, incomeTableTotalItems);
		XTextTable mostBoughtItemsTable = template.createAndFillMostBoughtItemsTable(startDate, endDate, mbiTableTotalItems);
		XTextTable leastBoughtItemsTable = template.createAndFillLeastBoughtItemsTable(startDate, endDate, lbiTableTotalItems);
		
		template.putRevenue(startDate, endDate);
		
	}
}
