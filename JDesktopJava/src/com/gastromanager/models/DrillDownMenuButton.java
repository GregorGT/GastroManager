package com.gastromanager.models;

import java.io.Serializable;

public class DrillDownMenuButton implements Serializable {

    String name;
    String width;
    String height;
    String target;
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

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
