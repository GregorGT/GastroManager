package com.gastromanager.models;

import java.io.Serializable;

public class SignOffOrderInfo implements Serializable {
    private OrderDetailQuery orderDetailQuery;

    public OrderDetailQuery getOrderDetailQuery() {
        return orderDetailQuery;
    }

    public void setOrderDetailQuery(OrderDetailQuery orderDetailQuery) {
        this.orderDetailQuery = orderDetailQuery;
    }
}
