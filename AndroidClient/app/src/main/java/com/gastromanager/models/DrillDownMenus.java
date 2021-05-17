package com.gastromanager.models;

import java.io.Serializable;
import java.util.List;

public class DrillDownMenus implements Serializable {
    List<DrillDownMenuType> drillDownMenuTypes;

    public List<DrillDownMenuType> getDrillDownMenuTypes() {
        return drillDownMenuTypes;
    }

    public void setDrillDownMenuTypes(List<DrillDownMenuType> drillDownMenuTypes) {
        this.drillDownMenuTypes = drillDownMenuTypes;
    }
}
