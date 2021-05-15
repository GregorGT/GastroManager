package com.gastromanager.models;

public class MenuDetail {
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
