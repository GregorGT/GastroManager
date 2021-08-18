package com.gastromanager.mainwindow;

import javax.swing.JComboBox;
import java.util.HashMap;
import java.util.Map;

public class ItemOptionComboBox extends JComboBox{
    private Map<String, String> nameUuid = new HashMap<>();

    public ItemOptionComboBox()
        {}
    public void addPair(String name, String uuid) {
        nameUuid.put(name, uuid);
    }
    public Map<String, String> getNameUuid() {
        return nameUuid;
    }
    public void setNameUuid(Map<String, String> nameUuid) {
        this.nameUuid = nameUuid;
    }
}
