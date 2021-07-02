package com.gastromanager.models;

import java.io.Serializable;

public class DrillDownMenuButton implements Serializable {

    String name;
    String width;
    String height;
    String xPosition;
    String yPosition;
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

    public String getxPosition() {
        return xPosition;
    }

    public void setxPosition(String xPosition) {
        this.xPosition = xPosition;
    }

    public String getyPosition() {
        return yPosition;
    }

    public void setyPosition(String yPosition) {
        this.yPosition = yPosition;
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
