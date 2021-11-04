package com.gastromanager.print;

import java.util.List;

import com.gastromanager.models.OrderDetailQuery;
import com.gastromanager.models.OrderItemInfo;

public interface PrintService {
    boolean print(String orderInfo);
    Boolean print(OrderDetailQuery orderDetailQuery);
    Boolean printToSelectedPrinter(OrderDetailQuery orderDetailQuery);
    String getPrintInfo(String orderInfo, String serverName, List<OrderItemInfo> selectedOrderItems, int constant);
}
