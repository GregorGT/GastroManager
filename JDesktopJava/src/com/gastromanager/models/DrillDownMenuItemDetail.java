/*Copyright 2021 GastroRice

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/


package com.gastromanager.models;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DrillDownMenuItemDetail implements Serializable {
    String menuItemName;
    String uuid;
    Double price;
    String menuId;
    Map<String, DrillDownMenuItemOptionDetail> options;
    List<DrillDownMenuItemDetail> subItems;

    public DrillDownMenuItemDetail() {

    }

    public DrillDownMenuItemDetail(String menuItemName, Map<String, DrillDownMenuItemOptionDetail> options, List<DrillDownMenuItemDetail> subItems) {
        this.menuItemName = menuItemName;
        this.options = options;
        this.subItems = subItems;
    }

    public Map<String, DrillDownMenuItemOptionDetail> getOptionsMap() {
        return options;
    }

    public void setOptionsMap(Map<String, DrillDownMenuItemOptionDetail> optionsMap) {
        this.options = optionsMap;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public List<DrillDownMenuItemDetail> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<DrillDownMenuItemDetail> subItems) {
        this.subItems = subItems;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
