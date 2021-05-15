package com.gastromanager.models;

import java.util.Map;

public class Menu {
    Map<String, DrillDownMenuItemDetail> itemMap;

    public Map<String, DrillDownMenuItemDetail> getItemMap() {
        return itemMap;
    }

    public void setItemMap(Map<String, DrillDownMenuItemDetail> itemMap) {
        this.itemMap = itemMap;
    }
}
