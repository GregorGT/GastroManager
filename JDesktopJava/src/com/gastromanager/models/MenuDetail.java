package com.gastromanager.models;

import java.io.Serializable;

public class MenuDetail implements Serializable {
    private Menu menu;
    private DrillDownMenus drillDownMenus;

    public DrillDownMenus getDrillDownMenus() {
        return drillDownMenus;
    }

    public void setDrillDownMenus(DrillDownMenus drillDownMenus) {
        this.drillDownMenus = drillDownMenus;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
