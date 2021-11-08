/*Copyright 2021 GastroRice

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/


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