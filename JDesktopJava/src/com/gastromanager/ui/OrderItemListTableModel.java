/*Copyright 2021 GastroRice

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/


package com.gastromanager.ui;

import com.gastromanager.models.OrderItemInfo;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class OrderItemListTableModel extends AbstractTableModel {

    private List<OrderItemInfo> orderItemInfoList;

    private final String[] columnNames = new String[] {
            "Item Info", "Price", "Item Id","Payed"
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
    public String getColumnName(int col) {
        return columnNames[col];
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


    public void setValueAt(Object value, int row, int col) {
        System.out.println("Existing value "+getValueAt(row, 1));
        OrderItemInfo orderItemInfo = orderItemInfoList.get(row);
        //if(isCellEditable(row, col)) {
           if(col == 1) {
               orderItemInfo.setPrice((Double) value);
               System.out.println("New value "+getValueAt(row, 1));
           }
        //}
    }

    public void addTotal() {
        Double total =  orderItemInfoList.stream().mapToDouble(OrderItemInfo::getPrice).sum();
        OrderItemInfo orderTipInfo = new OrderItemInfo();
        orderTipInfo.setPrice(total);
        orderTipInfo.setXmlText("Total:");
        this.orderItemInfoList.add(orderTipInfo);
    }

    public void addTip(Double tipAmount) {
        OrderItemInfo orderTipInfo = new OrderItemInfo();
        orderTipInfo.setPrice(tipAmount);
        orderTipInfo.setXmlText("Tip:");
        this.orderItemInfoList.add(orderTipInfo);

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
