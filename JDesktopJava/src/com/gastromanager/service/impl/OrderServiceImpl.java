package com.gastromanager.service.impl;

import com.gastromanager.models.*;
import com.gastromanager.print.PrintService;
import com.gastromanager.print.PrintServiceImpl;
import com.gastromanager.service.OrderService;
import com.gastromanager.util.DbUtil;

import java.util.ArrayList;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    @Override
    public List<OrderItemInfo> retrieveOrderItems(OrderDetailQuery orderDetailQuery) {
        List<OrderItem> orderItems = DbUtil.getOrderDetails(orderDetailQuery, false);
        List<OrderItemInfo> orderItemArrayList =  null;
        if(orderItems != null) {
            orderItemArrayList = new ArrayList<>();
            for(OrderItem orderItem: orderItems) {
                OrderItemInfo orderItemInfo = new OrderItemInfo(orderItem);
                orderItemArrayList.add(orderItemInfo);
            }

            System.out.println("order items count "+orderItemArrayList.size());
        } else {
            System.out.println("No order items found");
        }

        return orderItemArrayList;
    }

    @Override
    public void addOrderItem(SelectedOrderItem selectedOrderItem) {

    }

    @Override
    public void removeOrderItem(OrderItemInfo orderItemInfo) {

    }

    @Override
    public Boolean signOffOrder(SignOffOrderInfo signOffOrderInfo) {
        PrintService printService = new PrintServiceImpl();
        return printService.print(signOffOrderInfo.getOrderDetailQuery());
    }

    @Override
    public Integer getStartingHumanReadableId(HumanReadableIdQuery humanReadableIdQuery) {
        return DbUtil.getStartingHumanReadableOrderId(humanReadableIdQuery.getFloorId(),
                humanReadableIdQuery.getTableId());
    }
}
