package com.gastromanager.models;

public class DrillDownMenuItemOptionDetail {
    String id;
    String name;
    Double price;
    DrillDownMenuItemOptionChoiceDetail choice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public DrillDownMenuItemOptionChoiceDetail getChoice() {
        return choice;
    }

    public void setChoice(DrillDownMenuItemOptionChoiceDetail choice) {
        this.choice = choice;
    }
}
