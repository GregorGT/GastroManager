package com.gastromanager.comparator;

import com.gastromanager.models.OrderItemInfo;

import java.util.Comparator;

public class OrderItemComparator implements Comparator<OrderItemInfo> {
    @Override
    public int compare(OrderItemInfo o1, OrderItemInfo o2) {
        return o1.getDateTime().compareTo(o2.getDateTime());
    }
}
