package com.gastromanager.reports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gastromanager.util.PropertiesUtil;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.text.XTextTable;

public class UserInputReport {

	public static void main(String[] args) throws IllegalArgumentException, UnknownPropertyException, PropertyVetoException, WrappedTargetException, ParseException {
		FinancialReportTemplate template = new FinancialReportTemplate();
		
		template.connectDB();
		template.openDocument();
		template.putTitle("Financial Report");
		
		Date date1 = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy").parse(args[0]);
		Date date2 = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy").parse(args[1]);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = sdf.format(date1);
		String endDate = sdf.format(date2);
		startDate += " 00:00:00";
		endDate += " 23:59:59";
		
		sdf = new SimpleDateFormat("dd/MM/yyyy");
        template.putDate(sdf.format(date1), sdf.format(date2));
        
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
