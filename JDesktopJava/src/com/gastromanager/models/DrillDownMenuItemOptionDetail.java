package com.gastromanager.models;

import java.io.Serializable;

public class DrillDownMenuItemOptionDetail implements Serializable {
    String id;
    String name;
    Double price;
    Boolean isOverwritePrice;
    String menuId;
    String uuid;
    DrillDownMenuItemOptionChoiceDetail choice;

    public DrillDownMenuItemOptionDetail() {
        this.setOverwritePrice(false);
    }

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

    public Boolean getOverwritePrice() {
        return isOverwritePrice;
    }

    public void setOverwritePrice(Boolean overwritePrice) {
        isOverwritePrice = overwritePrice;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public DrillDownMenuItemOptionChoiceDetail getChoice() {
        return choice;
    }

    public void setChoice(DrillDownMenuItemOptionChoiceDetail choice) {
        this.choice = choice;
    }
}
