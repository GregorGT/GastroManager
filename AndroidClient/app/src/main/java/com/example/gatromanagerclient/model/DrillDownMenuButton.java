package com.example.gatromanagerclient.model;

public class DrillDownMenuButton {

    String name;
    String width;
    String height;
    DrillDownMenuItemDetail menuItemDetail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public DrillDownMenuItemDetail getMenuItemDetail() {
        return menuItemDetail;
    }

    public void setMenuItemDetail(DrillDownMenuItemDetail menuItemDetail) {
        this.menuItemDetail = menuItemDetail;
    }
}
