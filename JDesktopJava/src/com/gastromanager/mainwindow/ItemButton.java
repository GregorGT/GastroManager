package com.gastromanager.mainwindow;

import javax.swing.*;
import java.awt.*;

public class ItemButton extends JButton {
    private static final Color YELLOW = Color.YELLOW;
    private static final Color CYAN = Color.CYAN;

    private JComboBox comboBox;
    private int var = 0;
    private boolean mainItem;
    private String target;
    private String optionId;
    private boolean pressedOnce;

    public ItemButton() {
        this.setBackground(CYAN);
        ++var;
        this.pressedOnce = false;
    }
    public ItemButton(String menuItemName) {
        this.setText(menuItemName);
        this.setBackground(CYAN);
        ++var;
        this.pressedOnce = false;
    }

    public void click() {
        if (var % 2 == 0) {
            this.setBackground(CYAN);
        } else {
            this.setBackground(YELLOW);
        }
        ++var;
    }

    public void unselect() {
        if (this.getBackground().equals(YELLOW)) {
            ++var;
        }
        this.setBackground(CYAN);
    }

    public boolean isYellow() {
        return this.getBackground().equals(YELLOW);
    }
    public void setAssociatedOptions(JComboBox comboBox) {
        this.comboBox = comboBox;
    }
    public JComboBox getAssociatedOptions() {
        return comboBox;
    }
    public boolean isMainItem() {
        return mainItem;
    }
    public void setMainItem(boolean mainItem) {
        this.mainItem = mainItem;
    }
    public String getTarget() {
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }
    public String getOptionId() {
        return optionId;
    }
    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }
    public boolean isPressedOnce() {
        return pressedOnce;
    }
    public void setPressedOnce(boolean pressedOnce) {
        this.pressedOnce = pressedOnce;
    }
}