package com.example.gatromanagerclient;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.gastromanager.models.OrderItemInfo;

import java.util.Comparator;

public class OrderItemComparator implements Comparator<OrderItemInfo> {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int compare(OrderItemInfo o1, OrderItemInfo o2) {
        return o1.getDateTime().compareTo(o2.getDateTime());
    }
}
