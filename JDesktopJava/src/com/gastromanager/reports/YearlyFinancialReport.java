package com.gastromanager.reports;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.gastromanager.util.PropertiesUtil;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.text.XTextTable;
import com.sun.star.uno.Exception;

public class YearlyFinancialReport {

	public static void main(String[] args) throws IllegalArgumentException, UnknownPropertyException, PropertyVetoException, WrappedTargetException {
		FinancialReportTemplate template = new FinancialReportTemplate();
		
		template.connectDB();
		template.openDocument();
		template.putTitle("Yearly Financial Report");
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-12);
		Date yearStart = calendar.getTime();
		calendar.add(Calendar.MONTH, 12);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date yearEnd = calendar.getTime();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = sdf.format(yearStart);	
		String endDate = sdf.format(yearEnd);	
		startDate += " 00:00:00";
		endDate += " 23:59:59";
		
//		System.out.println(startDate);
//		System.out.println(endDate);
		
		sdf = new SimpleDateFormat("dd/MM/yyyy");
        template.putDate(sdf.format(yearStart), sdf.format(yearEnd));
        
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
