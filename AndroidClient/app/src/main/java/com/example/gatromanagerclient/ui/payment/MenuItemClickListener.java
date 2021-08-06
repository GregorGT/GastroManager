package com.example.gatromanagerclient.ui.payment;

import com.gastromanager.models.OrderItemInfo;

public interface MenuItemClickListener {

    void onMenuItemClickListener(OrderItemInfo orderItemInfo , boolean isSelectedMenuList);

    void updateTotalAmount();
}
