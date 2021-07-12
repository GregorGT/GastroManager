package com.gastromanager.models;

import java.io.Serializable;

public class OrderListQuery implements Serializable {

    private String floorId;
    private String tableId;

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
}
