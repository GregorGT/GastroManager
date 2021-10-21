package com.gastromanager.models;

import java.io.Serializable;
import java.util.List;

public class OrderItemTransactionInfo implements Serializable {

    private List<OrderItemInfo> orderItemInfoList;
    private TransactionInfo transactionInfo;
    private Boolean addTransaction;
    private String orderId;

    public List<OrderItemInfo> getOrderItemInfo() {
        return orderItemInfoList;
    }

    public void setOrderItemInfo(List<OrderItemInfo> orderItemInfoList) {
        this.orderItemInfoList = orderItemInfoList;
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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

    
}
