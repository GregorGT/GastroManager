package com.gastromanager.models;

import java.io.Serializable;
import java.util.List;

public class SelectedOrderItem implements Serializable {
    private String itemName;
    private SelectedOrderItemOption option;
    private List<SelectedOrderItem> subItems;
    private String orderId;
    private String floorId;
    private String tableId;
    private String staffId;

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
}
