package com.gastromanager.service.impl;

import com.gastromanager.models.*;
import com.gastromanager.service.PaymentService;
import com.gastromanager.util.DbUtil;

import java.util.ArrayList;
import java.util.List;

public class PaymentServiceImpl implements PaymentService {
    @Override
    public List<Order> retrieveOrders(OrderListQuery orderListQuery) {
        return DbUtil.getOrderList(orderListQuery.getFloorId(), orderListQuery.getTableId());
    }

    @Override
    public List<OrderItemInfo> retrieveOrderItems(OrderDetailQuery orderDetailQuery) {
        List<OrderItem> orderItems = DbUtil.getOrderDetails(orderDetailQuery, false);
        List<OrderItemInfo> orderItemInfoList =  null;
        if(orderItems != null) {
            orderItemInfoList = new ArrayList<>();
            for(OrderItem orderItem: orderItems) {
                OrderItemInfo orderItemInfo = new OrderItemInfo(orderItem);
                orderItemInfoList.add(orderItemInfo);
            }
        }

        return orderItemInfoList;
    }

    @Override
    public List<OrderItemInfo> processTransactionInfo(OrderItemTransactionInfo orderItemTransactionInfo) {
        List<OrderItemInfo> orderItemInfo = orderItemTransactionInfo.getOrderItemInfo();
        TransactionInfo transactionInfo = orderItemTransactionInfo.getTransactionInfo();
        if(orderItemTransactionInfo.getAddTransaction()) { //Add transaction
            DbUtil.addTransactionInfo(orderItemInfo, transactionInfo);
        } else { //remove transaction
            DbUtil.removeTransactionInfo(orderItemInfo, transactionInfo);
        }
        return translateToOrderItemInfo(DbUtil.getOrderDetails(orderItemInfo.get(0).getOrderId().toString(), false));
    }

    @Override
    public void undoPayment(OrderItemInfo orderItemInfo) {
        Boolean isPaymentReset = DbUtil.undoPayment(orderItemInfo);
    }

    private List<OrderItemInfo> translateToOrderItemInfo(List<OrderItem> orderItems) {
        List<OrderItemInfo> orderItemInfoList =  null;
        if(orderItems != null) {
            orderItemInfoList = new ArrayList<>();
            for(OrderItem orderItem: orderItems) {
                OrderItemInfo orderItemInfo = new OrderItemInfo(orderItem);
                orderItemInfoList.add(orderItemInfo);
            }
        }

        return orderItemInfoList;
    }


}
