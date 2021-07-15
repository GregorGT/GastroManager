package com.gastromanager.service;

import com.gastromanager.models.*;

import java.util.List;

public interface OrderService {

    List<OrderItemInfo> retrieveOrderItems(OrderDetailQuery orderDetailQuery);
    void addOrderItem(SelectedOrderItem selectedOrderItem);
    void removeOrderItem(OrderItemInfo orderItemInfo);
    Boolean signOffOrder(SignOffOrderInfo signOffOrderInfo);
    Integer getStartingHumanReadableId(HumanReadableIdQuery humanReadableIdQuery);;
}
