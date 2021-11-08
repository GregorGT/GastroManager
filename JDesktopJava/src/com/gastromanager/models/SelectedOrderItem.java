/*Copyright 2021 GastroRice

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/


package com.gastromanager.models;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SelectedOrderItem implements Serializable {
    private String itemName;
    private SelectedOrderItemOption option;
    private Map<String, SelectedOrderItemOption> allOptions;
    private List<SelectedOrderItem> subItems;
    private String orderId;
    private String floorId;
    private String tableId;
    private String staffId;
    private String target;
    private Double price;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public SelectedOrderItemOption getOption() {
        return option;
    }

    public void setOption(SelectedOrderItemOption option) {
        this.option = option;
    }

    public Map<String, SelectedOrderItemOption> getAllOptions() {
        return allOptions;
    }

    public void setAllOptions(Map<String, SelectedOrderItemOption> allOptions) {
        this.allOptions = allOptions;
    }

    public List<SelectedOrderItem> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<SelectedOrderItem> subItems) {
        this.subItems = subItems;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


}
