package com.gastromanager.models;

public class OrderPrice {
    Double price;
    Double choicePrice;
    Boolean isOverwritePrice;

    public OrderPrice(Double price, Boolean isOverwritePrice) {
        this.price = price;
        this.isOverwritePrice = isOverwritePrice;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getChoicePrice() {
        return choicePrice;
    }

    public void setChoicePrice(Double choicePrice) {
        this.choicePrice = choicePrice;
    }

    public Boolean getOverwritePrice() {
        return isOverwritePrice;
    }

    public void setOverwritePrice(Boolean overwritePrice) {
        isOverwritePrice = overwritePrice;
    }
}
