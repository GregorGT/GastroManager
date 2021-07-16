package com.gastromanager.ui;

import com.gastromanager.models.OrderItemInfo;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class OrderItemListTableModel extends AbstractTableModel {

    private List<OrderItemInfo> orderItemInfoList;

    private final String[] columnNames = new String[] {
            "Item Info", "Price"
    };

    private final Class[] columnClass = new Class[] {
            String.class, Double.class, Integer.class, Integer.class
    };

    public OrderItemListTableModel(List<OrderItemInfo> orderItemInfoList) {
        this.orderItemInfoList = orderItemInfoList;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public List<OrderItemInfo> getOrderItemInfoList() {
        return orderItemInfoList;
    }

    public void setOrderItemInfoList(List<OrderItemInfo> orderItemInfoList) {
        this.orderItemInfoList = orderItemInfoList;
    }

    @Override
    public int getRowCount() {
        return orderItemInfoList == null ? 0:orderItemInfoList.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }
    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return columnClass[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value = null;
        OrderItemInfo orderItemInfo = orderItemInfoList.get(rowIndex);
        switch(columnIndex) {
            case 0 : value = orderItemInfo.getXmlText();
                     break;
            case 1 : value = orderItemInfo.getPrice();
                     break;
            case 2 : value = orderItemInfo.getItemId();
                     break;
            case 3 : value = orderItemInfo.getPayed();
                break;
        }

        return value;
    }

    /*private String[] buildRowInfo(OrderItemInfo orderItemInfo) {
        String[] rowInfo = new String[columnCount];
        for(int i = 0; i < columnCount; i++) {
            switch (i) {
                case 0 : orderItemInfo.getXmlText();
                         break;
                case 1 : orderItemInfo.getPrice();
                         break;
            }
        }

        return rowInfo;
    }*/
}
