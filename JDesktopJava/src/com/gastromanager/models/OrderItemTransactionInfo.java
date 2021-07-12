package com.gastromanager.models;

import java.io.Serializable;

public class OrderItemTransactionInfo implements Serializable {

    private OrderItemInfo orderItemInfo;
    private TransactionInfo transactionInfo;
    private Boolean addTransaction;


    public OrderItemInfo getOrderItemInfo() {
        return orderItemInfo;
    }

    public void setOrderItemInfo(OrderItemInfo orderItemInfo) {
        this.orderItemInfo = orderItemInfo;
    }

    public TransactionInfo getTransactionInfo() {
        return transactionInfo;
    }

    public void setTransactionInfo(TransactionInfo transactionInfo) {
        this.transactionInfo = transactionInfo;
    }

    public Boolean getAddTransaction() {
        return addTransaction;
    }

    public void setAddTransaction(Boolean addTransaction) {
        this.addTransaction = addTransaction;
    }
}
