package com.gastromanager.service;

import com.gastromanager.models.*;

import java.util.List;

public interface PaymentService {

    List<Order> retrieveOrders(OrderListQuery orderListQuery);

    List<OrderItemInfo> retrieveOrderItems(OrderDetailQuery orderDetailQuery);

    List<OrderItemInfo> processTransactionInfo(OrderItemTransactionInfo orderItemTransactionInfo);




}
