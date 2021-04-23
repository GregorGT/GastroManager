package com.gastromanager.print;

import com.gastromanager.models.OrderItem;
import com.gastromanager.util.DbUtil;
import com.gastromanager.util.GastroManagerConstants;
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
import java.io.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PrintServiceImpl implements PrintService {
    @Override
    public boolean print(String orderId) {
        List<OrderItem> orderItems = DbUtil.getOrderDetails(orderId);

        //return executePrint(formatOrderText(orderItems));
        return executePrintOverNetwork(formatOrderText(orderItems));
    }

    private String formatOrderText(List<OrderItem> orderItems) {
        StringBuilder orderDetailsBuilder = new StringBuilder();
        AtomicReference<Double> total = new AtomicReference<>(new Double(0));
        orderDetailsBuilder.append("        RESTAURANT            \n");
        orderDetailsBuilder.append("******* Order Details: *******\n");
        orderItems.forEach(orderItem -> {
            Document xml = orderItem.getXml();
            total.updateAndGet(v -> v + orderItem.getPrice());
            //Main Item
            Node item  = xml.getDocumentElement();
            if(item.getNodeName() == "item") {
                orderDetailsBuilder.append(item.getAttributes().getNamedItem("name").getNodeValue() + GastroManagerConstants.PRICE_SPACING + orderItem.getPrice() + "\n");
                //addOptionOrderInfo(item, orderDetailsBuilder);
                //Linked items
                addChildItemInfo(item.getChildNodes(), orderDetailsBuilder);
                //addChildItems(item, orderDetailsBuilder);
                orderDetailsBuilder.append("\n");

            }
        });
        addTotal(total.get(), orderDetailsBuilder);
        System.out.println(orderDetailsBuilder.toString());
        return orderDetailsBuilder.toString();
    }

    private void addTotal(double total, StringBuilder orderDetailsBuilder) {
        orderDetailsBuilder.append("------------------------------------\n");
        orderDetailsBuilder.append("Total:"+GastroManagerConstants.PRICE_SPACING + total +"\n");
        orderDetailsBuilder.append("------------------------------------");
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

    private Boolean executePrintOverNetwork(String text) {
        Boolean isPrintSuccessful = false;
        try {
            Printer printer = new NetworkPrinter("127.0.0.1", 9100);
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
        //printService.checkPrint();
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
