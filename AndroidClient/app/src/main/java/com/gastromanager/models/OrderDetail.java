package com.gastromanager.models;

import java.io.Serializable;
import java.util.List;

public class OrderDetail implements Serializable {

    private List<OrderItem> orderItemList;

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
