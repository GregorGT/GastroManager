/*Copyright 2021 GastroRice

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/


package com.gastromanager.print;

import com.gastromanager.mainwindow.ServerSocketMenu;
import com.gastromanager.models.OrderDetailQuery;
import com.gastromanager.models.OrderInfo;
import com.gastromanager.models.OrderItem;
import com.gastromanager.models.OrderItemInfo;
import com.gastromanager.util.DbUtil;
import com.gastromanager.util.GastroManagerConstants;
import com.gastromanager.util.PropertiesUtil;
import com.gastromanager.util.Util;

import io.github.escposjava.PrinterService;
import io.github.escposjava.print.NetworkPrinter;
import io.github.escposjava.print.Printer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.*;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class PrintServiceImpl implements PrintService {
    @Override
    public boolean print(String orderId) {
        List<OrderItem> orderItems = DbUtil.getOrderDetails(orderId, true);
        OrderInfo orderInfo = DbUtil.getOrderInfo(orderId);
        return executePrint(formatOrderText(orderItems, orderInfo, "", 0));
        //return executePrintOverNetwork(formatOrderText(orderItems, orderInfo));
    }

    public Boolean printToSelectedPrinter(OrderDetailQuery orderDetailQuery) {
        Boolean isFullOrderPrinted = false;
        List<OrderItem> orderItems = DbUtil.getOrderDetails(orderDetailQuery, true);
        OrderInfo orderInfo = DbUtil.getOrderInfo(orderDetailQuery.getHumanreadableId());
        

        Map<String, List<OrderItem>> groupedOrderItems = groupOrderItemsByPrinter(orderItems);
        
        MainPrintService mainPrintService = new MainPrintService();
        int allItems = 0;
        for (Map.Entry<String, List<OrderItem>> kv : groupedOrderItems.entrySet()) {
        	try {
				executePrintOverNetwork(formatOrderText(kv.getValue(), orderInfo, orderDetailQuery.getServerName(), 0), PropertiesUtil.getPropertyValue("printer.ip." + kv.getKey()), Integer.parseInt(PropertiesUtil.getPropertyValue("printer.port."+kv.getKey())));
				mainPrintService.printString(PropertiesUtil.getPropertyValue("printer."+kv.getKey()), formatOrderText(kv.getValue(), orderInfo, orderDetailQuery.getServerName(), 0));
        		allItems++;
			} catch (Exception ex) {
				System.out.print(ex.toString());
			}
        }
        
        if (allItems == groupedOrderItems.size()) {
        	isFullOrderPrinted = true;
        }
        
        if(isFullOrderPrinted) {
            isFullOrderPrinted = DbUtil.updatePrintedOrderItems(orderDetailQuery, true);
        }
        return isFullOrderPrinted;    	
    }
    
    private Map<String, List<OrderItem>> groupOrderItemsByPrinter(List<OrderItem> orderItems) {
    	Map<String, List<OrderItem>> items = new HashMap<>();
    			
    	for (OrderItem i : orderItems) {
    		boolean inserted = false;
    		for (Map.Entry<String, List<OrderItem>> kv : items.entrySet()) {
    			if (kv.getKey().equals(i.getPrinter())) {
    				kv.getValue().add(i);
    				inserted = true;
    			}
    		}
    		if (!inserted) {
    			ArrayList<OrderItem> a = new ArrayList<>();
    			a.add(i);
    			items.put(i.getPrinter(), a);
    		}
    	}
    	
    	return items;
    }
    
    @Override
    public Boolean print(OrderDetailQuery orderDetailQuery) {
        Boolean isOrderPrinted = false;
        List<OrderItem> orderItems = DbUtil.getOrderDetails(orderDetailQuery, true);
        OrderInfo orderInfo = DbUtil.getOrderInfo(orderDetailQuery.getHumanreadableId());
        isOrderPrinted = executePrint(formatOrderText(orderItems, orderInfo, orderDetailQuery.getServerName(), 0));
        if(isOrderPrinted) {
            isOrderPrinted = DbUtil.updatePrintedOrderItems(orderDetailQuery, true);
        }
        return isOrderPrinted;
    }

    @Override
    public String getPrintInfo(String orderId, String servername, List<OrderItemInfo> selectedOrderItems, int constant) {
        List<OrderItem> orderItems = DbUtil.getOrderItems(orderId, true);
        
        List<OrderItem> finalItems = new ArrayList<>();
        
        if (selectedOrderItems != null) {
	        
	        for (OrderItemInfo o : selectedOrderItems) {
		        for (OrderItem or : orderItems) {
		    		if (or.getItemId().equals(o.getItemId())) {
		    			finalItems.add(or);
		    			break;
		    		}
		    	}
	        }
        } else {
        	finalItems = orderItems;
        }
        
        OrderInfo orderInfo = DbUtil.getOrderInfo(orderId);
        return formatOrderText(finalItems, orderInfo, servername, constant);
        //return executePrintOverNetwork(formatOrderText(orderItems, orderInfo));
    }

    private String formatOrderText(List<OrderItem> orderItems, OrderInfo orderInfo, String servername, int constant) {
        StringBuilder orderDetailsBuilder = new StringBuilder();
        if(orderInfo != null){
            AtomicReference<Double> total = new AtomicReference<>(new Double(0));
            //String currency = PropertiesUtil.getPropertyValue("currency");
            orderDetailsBuilder.append("Order: " + orderInfo.getHumanReadableId() + "\n");
            orderDetailsBuilder.append("Floor: " + orderInfo.getFloorId() + "(" + orderInfo.getFloorName()
                    + ")" + "\n");
            orderDetailsBuilder.append("Table: " + orderInfo.getTableId() + "(" + orderInfo.getTableName() + ")" + "\n");
          //  orderDetailsBuilder.append("Waitress: " + orderInfo.getStaffId() + "("
          //          + orderInfo.getStaffName() + ")" + "\n");
            if(servername == null || servername.length() == 0)
            	orderDetailsBuilder.append("Server(terminal): " + ServerSocketMenu.serverTextField.getText() + "\n");
            else
            	orderDetailsBuilder.append("Server(app): " + servername + "\n");
            	
            orderDetailsBuilder.append("Ordered At: " + orderInfo.getTimestamp() + "\n");
            orderDetailsBuilder.append("*************************\n");
            orderItems.forEach(orderItem -> {
                Document xml = orderItem.getXml();
                total.updateAndGet(v -> v + orderItem.getPrice());
                //Main Item
                Node item = xml.getDocumentElement();
                if (item.getNodeName() == "item") {
                	if (constant == GastroManagerConstants.PRINT_RECEIPT) {
                		orderDetailsBuilder.append(item.getAttributes().getNamedItem("name").getNodeValue() + GastroManagerConstants.PRICE_SPACING + orderItem.getPrice() + PropertiesUtil.getPropertyValue("currency") + "\n");	
                	} else {
                		orderDetailsBuilder.append(item.getAttributes().getNamedItem("name").getNodeValue() + GastroManagerConstants.PRICE_SPACING + orderItem.getQuantity() + "\n");
                	}
                	//addOptionOrderInfo(item, orderDetailsBuilder);
                    //Linked items
                    addChildItemInfo(item.getChildNodes(), orderDetailsBuilder);
                    //addChildItems(item, orderDetailsBuilder);
                    orderDetailsBuilder.append("\n");

                }
            });
            addTotal(total.get(), orderDetailsBuilder);
        }
        System.out.println(orderDetailsBuilder.toString());
        return orderDetailsBuilder.toString().trim();
    }

    private void addTotal(double total, StringBuilder orderDetailsBuilder) {
    	Double salsetax = Double.parseDouble(PropertiesUtil.getPropertyValue("salsetax"));
		
    	double tmpvalue = (total * (salsetax / 100));
		tmpvalue = Util.roundDouble(tmpvalue, 2);
		double taxes = tmpvalue;
				
		Double finalPrice = total + taxes;
		finalPrice = Util.roundDouble(finalPrice, 2);
		
	
		orderDetailsBuilder.append("--------------------------------\n");
		orderDetailsBuilder.append("SubTotal:  " + GastroManagerConstants.FOUR_SPACES + total+ PropertiesUtil.getPropertyValue("currency") + "\n");
		orderDetailsBuilder.append("Taxes("+ PropertiesUtil.getPropertyValue("salsetax") +"%):" + GastroManagerConstants.FOUR_SPACES + taxes + PropertiesUtil.getPropertyValue("currency") + "\n");
		orderDetailsBuilder.append("Total:     " + GastroManagerConstants.FOUR_SPACES + finalPrice + PropertiesUtil.getPropertyValue("currency") + "\n");
		orderDetailsBuilder.append("--------------------------------\n");
    }

    private void addChildItemInfo(NodeList children, StringBuilder orderDetailsBuilder) {
        for (int childId = 0; childId < children.getLength(); childId++) {
            Node child = children.item(childId);
            String childItemName  = child.getNodeName();
            if(childItemName == "item") {
                orderDetailsBuilder.append(GastroManagerConstants.FOUR_SPACES + child.getAttributes().getNamedItem("name").getNodeValue());
                addOptionOrderInfo(child, orderDetailsBuilder);
                //orderDetailsBuilder.append("\n");
                if(child.hasChildNodes()) {
                    addChildItemInfo(child.getChildNodes(), orderDetailsBuilder);
                }
            }
        }
    }

    private void addOptionOrderInfo(Node node, StringBuilder orderDetailsBuilder) {
        NodeList childNodes  = node.getChildNodes();
        for (int childId = 0; childId < childNodes.getLength(); childId++) {
            Node child = childNodes.item(childId);
            if(child.getNodeName() == "option") {
                orderDetailsBuilder.append(GastroManagerConstants.FOUR_SPACES + child.getAttributes().getNamedItem("name").getNodeValue());
                if(child.hasChildNodes()) {
                    NodeList optionChildNodes  = child.getChildNodes();
                    for (int optionChildId = 0; optionChildId < optionChildNodes.getLength(); optionChildId++) {
                        Node optionChild = optionChildNodes.item(optionChildId);
                        if(optionChild.getNodeName() == "choice") {
                            orderDetailsBuilder.append(GastroManagerConstants.FOUR_SPACES + optionChild.getAttributes().getNamedItem("name").getNodeValue());
                            break;
                        }
                    }
                }
                orderDetailsBuilder.append("\n");
                break;
            }

        }
    }

    /*private void addChildItems(Node node,StringBuilder orderDetailsBuilder) {
        NodeList childItems = node.getChildNodes();
        for (int childItemIndex = 0; childItemIndex < childItems.getLength(); childItemIndex++) {
            Node childItem = childItems.item(childItemIndex);
            String childItemName  = childItem.getNodeName();
            switch (childItemName) {
                case "item" :
                    orderDetailsBuilder.append("\t" + childItem.getAttributes().getNamedItem("name").getNodeValue() + "\n");
                    addChildItems(childItem, orderDetailsBuilder);
                    break;
                case "option" :
                    //addOptionOrderInfo(chil);
                    break;
            }
        }
    }*/

    private void addChildItems(Node node,StringBuilder orderDetailsBuilder) {
        NodeList childItems = node.getChildNodes();
        for (int childItemIndex = 0; childItemIndex < childItems.getLength(); childItemIndex++) {
            Node childItem = childItems.item(childItemIndex);
            String childItemName  = childItem.getNodeName();
            switch (childItemName) {
                case "item" :
                    orderDetailsBuilder.append("\t" + childItem.getAttributes().getNamedItem("name").getNodeValue());
                    addOptionOrderInfo(childItem, orderDetailsBuilder);
                    if(childItem.hasChildNodes()) {
                        addChildItems(childItem, orderDetailsBuilder);
                    }
                    break;
                case "option" :
                    //addOptionOrderInfo(chil);
                    break;
            }
        }
    }

    private static boolean jobRunning = true;

    private boolean executePrint(String text) {
        Boolean isPrintSuccessful = false;
        String defaultPrinter =
                PrintServiceLookup.lookupDefaultPrintService().getName();
        System.out.println("Default printer: " + defaultPrinter);

        javax.print.PrintService service = PrintServiceLookup.lookupDefaultPrintService();

        InputStream is = null;
        try {

            System.out.println(text);
            is = new ByteArrayInputStream(text.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        pras.add(new Copies(1));


        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;

        Doc doc = new SimpleDoc(is, flavor, null);

        DocPrintJob job = service.createPrintJob();
        job.addPrintJobListener(new JobCompleteMonitor());


        //PrintJobWatcher pjw = new PrintJobWatcher(job);
        try {
            job.print(doc, pras);
            isPrintSuccessful = true;
        } catch (PrintException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //pjw.waitForDone();
        try {
            is.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return isPrintSuccessful;
    }

    public Boolean executePrintOverNetwork(String text, String ip, int port) {
        Boolean isPrintSuccessful = false;
        try {
            Printer printer = new NetworkPrinter(ip, port);
            PrinterService printerService = new PrinterService(printer);

            printerService.print(text);

            printerService.close();
            isPrintSuccessful = true;
        } catch(Exception e) {
            e.printStackTrace();
        }

        return isPrintSuccessful;
    }


    public static void main(String[] args) throws Exception {
        PrintServiceImpl printService = new PrintServiceImpl();
        printService.print("1");
        //printService.checkPrint(); //TO print all available printers
    }

    private static class JobCompleteMonitor extends PrintJobAdapter {
        @Override
        public void printJobCompleted(PrintJobEvent jobEvent) {
            System.out.println("Job completed");
            jobRunning = false;
        }
    }

    private boolean autoCut() {
        Boolean isAutCutDone = false;
        DocPrintJob job = PrintServiceLookup.lookupDefaultPrintService().createPrintJob();
        byte[] bytes = {27, 100, 3};
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(bytes, flavor, null);
        try {
            job.print(doc, null);
            isAutCutDone = true;
        } catch (PrintException e) {
            e.printStackTrace();
        }
        return isAutCutDone;
    }

    //TODO edit/remove after test
    private void checkPrint() {
        // TODO code application logic here

        javax.print.PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null,null);

        for ( javax.print.PrintService printService : printServices ){

            System.out.println("******************************************************************" );
            System.out.println("Printer Name is (" + printService.getName()+ ")" );


            PrintServiceAttributeSet printServiceAttributeSet = printService.getAttributes();

            System.out.println("PrinterLocation is (" + printServiceAttributeSet.get(PrinterLocation.class));
            System.out.println("PrinterInfo is (" + printServiceAttributeSet.get(PrinterInfo.class));

            System.out.println("PrinterState is (" + printServiceAttributeSet.get(PrinterState.class));

            System.out.println("Destination is (" + printServiceAttributeSet.get(Destination.class));

            System.out.println("PrinterMakeAndModel is (" + printServiceAttributeSet.get(PrinterMakeAndModel.class));

            System.out.println("PrinterIsAcceptingJobs is (" + printServiceAttributeSet.get(PrinterIsAcceptingJobs.class));

        }

    }
}
