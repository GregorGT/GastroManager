package com.gastromanager.print;

import com.gastromanager.models.OrderDetailQuery;

public interface PrintService {
    boolean print(String orderInfo);
    Boolean print(OrderDetailQuery orderDetailQuery);
    String getPrintInfo(String orderInfo, String serverName, int constant);
}
