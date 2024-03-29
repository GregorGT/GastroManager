/*Copyright 2021 GastroRice

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.gastromanager.service.impl;

import com.gastromanager.mainwindow.MainWindow;
import com.gastromanager.models.*;
import com.gastromanager.service.MenuService;
import com.gastromanager.util.PublicVariables;
import com.gastromanager.util.SaxParserForGastromanager;
import com.gastromanager.util.XmlUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

public class MenuServiceImpl implements MenuService {

	private MainWindow mainWindow;
	
    public MenuDetail loadMenu() {
        MenuDetail menuDetail = null;

        PublicVariables publicVariables = PublicVariables.getInstance();
        
        String xmlContent = XmlUtil.writeTreeIntoString(publicVariables.getTree());
        
        SaxParserForGastromanager parser = SaxParserForGastromanager.getInstance();
        menuDetail = parser.parseXml(xmlContent);
        System.out.println("Drill down menu types available count " +menuDetail.getDrillDownMenus().getDrillDownMenuTypes().size());
            
       

        return menuDetail;
    }
    
    public void loadQuickMenuMap(MenuDetail menuDetail) {
        Menu menu = menuDetail.getMenu();
        Stack<DrillDownMenuItemDetail> drillDownMenuItemDetailStack = null;
        for(Map.Entry<String, DrillDownMenuItemDetail> orderItemEntry : menu.getMainItemMap().entrySet()) {
            DrillDownMenuItemDetail parent = orderItemEntry.getValue();
            System.out.println("item name "+parent.getMenuItemName());
            buildQuickMenuItem(parent, drillDownMenuItemDetailStack, menu);
        }
    }

    private void buildQuickMenuItem(DrillDownMenuItemDetail parent,
                                    Stack<DrillDownMenuItemDetail> drillDownMenuItemDetailStack, Menu menu) {
        for(DrillDownMenuItemDetail subItem : parent.getSubItems()) {
            System.out.println("subitem "+subItem.getMenuItemName());
            if((subItem.getSubItems() == null || subItem.getSubItems().size() == 0) &&
                    subItem.getOptionsMap() != null) {
                addQuickMenuEntry(subItem, parent, menu, drillDownMenuItemDetailStack);
            } else {
                if(subItem.getOptionsMap() != null || subItem.getSubItems() != null) {
                    if (drillDownMenuItemDetailStack == null) {
                        drillDownMenuItemDetailStack = new Stack<>();
                    }
                    drillDownMenuItemDetailStack.push(parent);
                    drillDownMenuItemDetailStack.push(subItem);
                    buildQuickMenuItem(subItem, drillDownMenuItemDetailStack, menu);
                } else { //no subitem and options
                    buildQuickMenuEntry(subItem, parent, menu, drillDownMenuItemDetailStack);
                }
            }
        }
    }

    private void buildQuickMenuEntry(DrillDownMenuItemDetail subItem, DrillDownMenuItemDetail parent,  Menu menu,
                                     Stack<DrillDownMenuItemDetail> drillDownMenuItemDetailStack) {
        if(drillDownMenuItemDetailStack == null) {
            //main Item
            SelectedOrderItem selectedOrderItem = buildParentChildOrderSelection(subItem, parent, null);
            menu.getQuickMenuIdRefMap().put(subItem.getMenuId(), selectedOrderItem);
            System.out.println("added entry for " + subItem.getMenuItemName() + "menu id = " + subItem.getMenuId());
        } else {
            Stack<DrillDownMenuItemDetail> localDrillDownMenuItemStack =
                    (Stack<DrillDownMenuItemDetail>) drillDownMenuItemDetailStack.clone();
            SelectedOrderItem parentItem = null;
            Boolean isNestedItem = false;
            SelectedOrderItem previousParent = null;
            while (!localDrillDownMenuItemStack.empty()) {
                DrillDownMenuItemDetail currentParentMenuItem = localDrillDownMenuItemStack.pop();
                parentItem = buildParentChildOrderSelection(subItem, currentParentMenuItem, parentItem);
                /*if(isNestedItem) {
                    parentItem = buildParentChildOrderSelection(previousParent, currentParentMenuItem);
                } else {
                    parentItem = buildParentChildOrderSelection(subItem, currentParentMenuItem);
                }
                previousParent = parentItem;
                isNestedItem = true;*/
            }
            menu.getQuickMenuIdRefMap().put(subItem.getMenuId(), parentItem);
            System.out.println("added entry for " + subItem.getMenuItemName() + "menu id = " + subItem.getMenuId());

        }
    }

    private SelectedOrderItem buildParentChildOrderSelection(DrillDownMenuItemDetail subItem, DrillDownMenuItemDetail parent,
                                                             SelectedOrderItem child) {
        SelectedOrderItem selectedOrderItem = new SelectedOrderItem();
        selectedOrderItem.setItemName(parent.getMenuItemName());
        selectedOrderItem.setTarget(parent.getUuid());
        selectedOrderItem.setPrice(subItem.getPrice());
        SelectedOrderItem selectedOrderSubItem = null;
        if(child != null) {
            selectedOrderSubItem = child;
        } else {
            selectedOrderSubItem = new SelectedOrderItem();
            selectedOrderSubItem.setItemName(subItem.getMenuItemName());
            selectedOrderSubItem.setTarget(subItem.getUuid());

        }
        List<SelectedOrderItem> selectedOrderItemList = new ArrayList<>();
        selectedOrderItemList.add(selectedOrderSubItem);
        selectedOrderItem.setSubItems(selectedOrderItemList);

        return selectedOrderItem;
    }



    private void addQuickMenuEntry(DrillDownMenuItemDetail subItem, DrillDownMenuItemDetail parent,  Menu menu,
                                   Stack<DrillDownMenuItemDetail> drillDownMenuItemDetailStack) {

        for (Map.Entry<String, DrillDownMenuItemOptionDetail> orderItemOptionEntry :
                subItem.getOptionsMap().entrySet()) {
            DrillDownMenuItemOptionDetail optionDetail = orderItemOptionEntry.getValue();
            Stack<DrillDownMenuItemDetail> localDrillDownMenuItemStack = (drillDownMenuItemDetailStack != null ?
                    (Stack<DrillDownMenuItemDetail>) drillDownMenuItemDetailStack.clone(): null);

            if (optionDetail.getMenuId() != null) {
                //main Item
                SelectedOrderItem selectedOrderItem = new SelectedOrderItem();
                selectedOrderItem.setItemName(parent.getMenuItemName());
                selectedOrderItem.setTarget(parent.getUuid());
                selectedOrderItem.setPrice(optionDetail.getPrice());
                //add option to parent
                DrillDownMenuItemOptionDetail menuItemOptionDetail = (parent.getOptionsMap() != null) ? parent.getOptionsMap().get(optionDetail.getName())
                        : null;
                if(menuItemOptionDetail != null) {
                    SelectedOrderItemOption selectedOrderItemOption = new SelectedOrderItemOption();
                    selectedOrderItemOption.setName(menuItemOptionDetail.getName());
                    selectedOrderItemOption.setId(menuItemOptionDetail.getId());
                    selectedOrderItem.setOption(selectedOrderItemOption);
                }

                //sub item
                SelectedOrderItem selectedOrderSubItem = new SelectedOrderItem();
                selectedOrderSubItem.setItemName(subItem.getMenuItemName());
                selectedOrderSubItem.setTarget(subItem.getUuid());

                SelectedOrderItemOption selectedOrderItemOption = new SelectedOrderItemOption();
                selectedOrderItemOption.setName(optionDetail.getName());
                selectedOrderItemOption.setId(optionDetail.getId());
                //selectedOrderItem.setOption(selectedOrderItemOption);
                selectedOrderSubItem.setOption(selectedOrderItemOption);
                List<SelectedOrderItem> subItems = new ArrayList<>();
                subItems.add(selectedOrderSubItem);
                selectedOrderItem.setSubItems(subItems);
                if (menu.getQuickMenuIdRefMap() == null) {
                    menu.setQuickMenuIdRefMap(new HashMap<>());
                }
                if(localDrillDownMenuItemStack != null) {
                    while (!localDrillDownMenuItemStack.empty()) {
                        DrillDownMenuItemDetail drillDownMenuItemDetail = localDrillDownMenuItemStack.pop();
                        Map<String, SelectedOrderItemOption> optionsMap = null;
                        Map<String, DrillDownMenuItemOptionDetail> menuItemOptionDetailMap = drillDownMenuItemDetail.getOptionsMap();
                        if(menuItemOptionDetailMap != null) {
                            for (Map.Entry<String, DrillDownMenuItemOptionDetail> optionDetailEntry : menuItemOptionDetailMap.entrySet()) {
                                DrillDownMenuItemOptionDetail drillDownMenuItemOptionDetail = optionDetailEntry.getValue();
                                if (!drillDownMenuItemOptionDetail.getId().equals(selectedOrderItemOption.getId())) {
                                    SelectedOrderItemOption currItemOption = new SelectedOrderItemOption();
                                    currItemOption.setId(drillDownMenuItemOptionDetail.getId());
                                    currItemOption.setName(drillDownMenuItemOptionDetail.getName());
                                    if (optionsMap == null) {
                                        optionsMap = new HashMap<>();
                                    }
                                    optionsMap.put(currItemOption.getName(), currItemOption);
                                } else {
                                    break;
                                }
                            }
                        }
                        if(drillDownMenuItemDetail.getMenuItemName().equals(parent.getMenuItemName())) {
                            selectedOrderItem.setAllOptions(optionsMap);
                        } else {
                            SelectedOrderItem currParentSelectedOrderItem = new SelectedOrderItem();
                            currParentSelectedOrderItem.setItemName(drillDownMenuItemDetail.getMenuItemName());
                            currParentSelectedOrderItem.setTarget(drillDownMenuItemDetail.getUuid());
                            //currParentSelectedOrderItem.setAllOptions(optionsMap);
                            currParentSelectedOrderItem.setOption(selectedOrderItemOption);
                            List<SelectedOrderItem> currSubItems = new ArrayList<>();
                            currSubItems.add(selectedOrderItem);
                            currParentSelectedOrderItem.setSubItems(currSubItems);
                            selectedOrderItem = currParentSelectedOrderItem;
                        }
                    }
                }
                menu.getQuickMenuIdRefMap().put(optionDetail.getMenuId(), selectedOrderItem);
                System.out.println("added entry for " + optionDetail.getMenuId());
            }
        }
    }

    public SelectedOrderItem buildMenuIdSelectionItem(DrillDownMenuItemDetail subItem, DrillDownMenuItemDetail parent,
                                                       DrillDownMenuItemOptionDetail optionDetail) {
        //main Item
        SelectedOrderItem selectedOrderItem = new SelectedOrderItem();
        selectedOrderItem.setItemName(parent.getMenuItemName());
        selectedOrderItem.setTarget(parent.getUuid());
        selectedOrderItem.setPrice(optionDetail.getPrice());

        //sub item
        SelectedOrderItem selectedOrderSubItem = new SelectedOrderItem();
        selectedOrderSubItem.setItemName(subItem.getMenuItemName());
        selectedOrderSubItem.setTarget(subItem.getUuid());

        SelectedOrderItemOption selectedOrderItemOption = new SelectedOrderItemOption();
        selectedOrderItemOption.setName(optionDetail.getName());
        selectedOrderItemOption.setId(optionDetail.getId());
        //selectedOrderItem.setOption(selectedOrderItemOption);
        selectedOrderSubItem.setOption(selectedOrderItemOption);
        List<SelectedOrderItem> subItems = new ArrayList<>();
        subItems.add(selectedOrderSubItem);
        selectedOrderItem.setSubItems(subItems);

        return selectedOrderItem;
    }
}
