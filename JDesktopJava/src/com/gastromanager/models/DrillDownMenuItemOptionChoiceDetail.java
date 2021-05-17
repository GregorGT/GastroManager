package com.gastromanager.models;

import java.io.Serializable;

public class DrillDownMenuItemOptionChoiceDetail implements Serializable {
    String name;
    Double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
