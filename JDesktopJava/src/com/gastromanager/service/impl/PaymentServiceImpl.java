/*Copyright 2021 GastroRice

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/


package com.gastromanager.service.impl;

import com.gastromanager.models.*;
import com.gastromanager.print.MainPrintService;
import com.gastromanager.print.PrintServiceImpl;
import com.gastromanager.service.PaymentService;
import com.gastromanager.util.DbUtil;
import com.gastromanager.util.GastroManagerConstants;
import com.gastromanager.util.PropertiesUtil;
import com.gastromanager.util.Util;
import com.gastromanager.util.XmlUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaymentServiceImpl implements PaymentService {
    @Override
    public List<Order> retrieveOrders(OrderListQuery orderListQuery) {
        return DbUtil.getOrderList(orderListQuery.getFloorId(), orderListQuery.getTableId());
    }

    @Override
    public List<OrderItemInfo> retrieveOrderItems(OrderDetailQuery orderDetailQuery) {
        List<OrderItem> orderItems = DbUtil.getOrderDetails(orderDetailQuery, false);
        List<OrderItemInfo> orderItemInfoList =  null;
        if(orderItems != null) {
            orderItemInfoList = new ArrayList<>();
            for(OrderItem orderItem: orderItems) {
                orderItem.setXmlText(XmlUtil.formatOrderTextRecursive(orderItem));
                OrderItemInfo orderItemInfo = new OrderItemInfo(orderItem);
                orderItemInfoList.add(orderItemInfo);
            }
        }

        return orderItemInfoList;
    }

    @Override
    public List<OrderItemInfo> processTransactionInfo(OrderItemTransactionInfo orderItemTransactionInfo) {
        List<OrderItemInfo> orderItemInfo = orderItemTransactionInfo.getOrderItemInfo();
        TransactionInfo transactionInfo = orderItemTransactionInfo.getTransactionInfo();
        if(orderItemTransactionInfo.getAddTransaction()) { //Add transaction
            DbUtil.addTransactionInfo(orderItemInfo, transactionInfo);
        } else { //remove transaction
            DbUtil.removeTransactionInfo(orderItemInfo, transactionInfo);
        }
        printBill(orderItemTransactionInfo);
        return translateToOrderItemInfo(DbUtil.getOrderDetails(orderItemInfo.get(0).getOrderId().toString(), false));
    }

    public void printBill(OrderItemTransactionInfo orderItemTransactionInfo) {
    	StringBuilder bill = constructBill(orderItemTransactionInfo);
    	PrintServiceImpl printService = new PrintServiceImpl();
		try {
			printService.executePrintOverNetwork(bill.toString(), PropertiesUtil.getPropertyValue("networkPrinter.ip.billing"), Integer.parseInt(PropertiesUtil.getPropertyValue("networkPrinter.port.billing")));
		} catch( Exception ex)
		{
			System.out.print(ex.toString());
		}
		MainPrintService mainPrintService = new MainPrintService();
		try {
			mainPrintService.printString(PropertiesUtil.getPropertyValue("printer.billing"), bill.toString());
		}catch(Exception ex)
		{
			System.out.print(ex.toString());
		}
    }
    
    
    private StringBuilder constructBill(OrderItemTransactionInfo orderItemTransactionInfo) {
    	StringBuilder bill = new StringBuilder();
		
		writeFileToBill("resources/billinghead.txt", bill);
		
		PrintServiceImpl printService = new PrintServiceImpl();
		
		bill.append("\n");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		bill.append("Time: " + dateFormat.format(date) + "\n");
		//fileWriter.append("Server(terminal): " + ServerSocketMenu.serverTextField.getText() + "\n");//printService.getPrintInfo(txtFieldOrderID.getText(), "Server name", GastroManagerConstants.PRINT_RECEIPT));
		bill.append("\n");
		bill.append("\n");
		
		bill.append(printService.getPrintInfo(orderItemTransactionInfo.getOrderId(), orderItemTransactionInfo.getServerName(), orderItemTransactionInfo.getOrderItemInfo(), GastroManagerConstants.PRINT_RECEIPT));
		bill.append("\n");
		
		writeFileToBill("resources/billingfooter.txt", bill);
						
		System.out.println("This is the final bill\n\n\n\n");
		System.out.println(bill);
		return bill;
    }

    private void writeFileToBill(String path, StringBuilder bill) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String everything = sb.toString();
		    bill.append(everything);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    @Override
    public void undoPayment(OrderItemInfo orderItemInfo) {
        Boolean isPaymentReset = DbUtil.undoPayment(orderItemInfo);
    }

    private List<OrderItemInfo> translateToOrderItemInfo(List<OrderItem> orderItems) {
        List<OrderItemInfo> orderItemInfoList =  null;
        if(orderItems != null) {
            orderItemInfoList = new ArrayList<>();
            for(OrderItem orderItem: orderItems) {
                orderItem.setXmlText(XmlUtil.formatOrderTextRecursive(orderItem));
                OrderItemInfo orderItemInfo = new OrderItemInfo(orderItem);
                orderItemInfoList.add(orderItemInfo);
            }
        }

        return orderItemInfoList;
    }


}
