package com.gastromanager.models;

import java.io.Serializable;

public class OrderDetailsView implements Serializable {
    private String orderDetailsView;

    public String getOrderDetailsView() {
        return orderDetailsView;
    }

    public void setOrderDetailsView(String orderDetailsView) {
        this.orderDetailsView = orderDetailsView;
    }
}
