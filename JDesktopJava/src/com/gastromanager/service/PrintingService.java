package com.gastromanager.service;

public interface PrintingService {

    public Boolean isOrderPrinted(String orderId);

    public void printOrder(String print);
}
