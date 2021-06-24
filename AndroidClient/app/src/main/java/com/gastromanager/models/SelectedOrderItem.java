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
