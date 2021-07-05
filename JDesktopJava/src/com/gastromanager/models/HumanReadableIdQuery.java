package com.gastromanager.models;

import java.io.Serializable;

public class HumanReadableIdQuery implements Serializable {
    Integer floorId;
    Integer tableId;

    public Integer getFloorId() {
        return floorId;
    }

    public void setFloorId(Integer floorId) {
        this.floorId = floorId;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }
}
