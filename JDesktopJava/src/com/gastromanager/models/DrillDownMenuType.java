package com.gastromanager.models;

import java.util.List;

public class DrillDownMenuType {
    String name;
    String width;
    String height;
    List<DrillDownMenuButton> buttons;

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

    public List<DrillDownMenuButton> getButtons() {
        return buttons;
    }

    public void setButtons(List<DrillDownMenuButton> buttons) {
        this.buttons = buttons;
    }
}
