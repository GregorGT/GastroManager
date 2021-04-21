package com.gastromanager.print;

import com.gastromanager.models.OrderItem;
import com.gastromanager.util.DbUtil;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import java.io.*;
import java.util.List;

public class PrintServiceImpl implements PrintService {
    @Override
    public boolean print(String orderId) {
        List<OrderItem> orderItems = DbUtil.getOrderDetails(orderId);

        return executePrint(formatOrderText(orderItems));
    }

    private String formatOrderText(List<OrderItem> orderItems) {
        StringBuilder orderDetailsBuilder = new StringBuilder();
        orderDetailsBuilder.append("******* Order Details: ******** \n");
        orderItems.forEach(orderItem -> {
            Document xml = orderItem.getXml();
            NodeList items  = xml.getElementsByTagName("item");
            for(int i=0; i < items.getLength(); i++) {
                orderDetailsBuilder.append("Item = " +items.item(i).getAttributes().getNamedItem("name").getNodeValue());
                orderDetailsBuilder.append("\n");
            }
        });

        return orderDetailsBuilder.toString();
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


    public static void main(String[] args) throws Exception {
        PrintServiceImpl printService = new PrintServiceImpl();
        printService.print("1");
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
}
