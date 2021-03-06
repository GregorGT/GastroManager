package com.gastromanager.models;

import java.io.Serializable;

public class OrderDetailQuery implements Serializable {
    private String floorId;
    private String tableId;
    private String humanreadableId;

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

    public String getHumanreadableId() {
        return humanreadableId;
    }

    public void setHumanreadableId(String humanreadableId) {
        this.humanreadableId = humanreadableId;
    }
}
