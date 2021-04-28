package com.gastromanager.print;

public interface PrintService {
    boolean print(String orderInfo);
    String getPrintInfo(String orderInfo);
}
