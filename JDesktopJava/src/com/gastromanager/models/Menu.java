/*Copyright 2021 GastroRice

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/


package com.gastromanager.models;

import java.io.Serializable;
import java.util.Map;

public class Menu implements Serializable {
    Map<String, DrillDownMenuItemDetail> itemMap;
    Map<String, DrillDownMenuItemDetail> mainItemMap;
    Map<String, SelectedOrderItem> quickMenuIdRefMap;

    public Map<String, DrillDownMenuItemDetail> getItemMap() {
        return itemMap;
    }

    public void setItemMap(Map<String, DrillDownMenuItemDetail> itemMap) {
        this.itemMap = itemMap;
    }

    public Map<String, DrillDownMenuItemDetail> getMainItemMap() {
        return mainItemMap;
    }

    public void setMainItemMap(Map<String, DrillDownMenuItemDetail> mainItemMap) {
        this.mainItemMap = mainItemMap;
    }

    public Map<String, SelectedOrderItem> getQuickMenuIdRefMap() {
        return quickMenuIdRefMap;
    }

    public void setQuickMenuIdRefMap(Map<String, SelectedOrderItem> quickMenuIdRefMap) {
        this.quickMenuIdRefMap = quickMenuIdRefMap;
    }
}
