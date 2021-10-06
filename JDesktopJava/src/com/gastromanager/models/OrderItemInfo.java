package com.gastromanager.models;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This is a twin class of OrderItem used for transferring objects over socket connections.
 */
public class OrderItemInfo implements Serializable {

    private Integer orderId;
    private Long itemId;
    private Integer quantity;
    private String remark;
    private String xmlText;
    private Double price;
    private Integer printStatus;
    private Integer payed;
    private LocalDateTime dateTime;
    private Integer status;
    private Order order;
    private String printer;
    
	public OrderItemInfo() {
    }

    public OrderItemInfo(OrderItem orderItem) {
        this.orderId = orderItem.getOrderId();
        this.itemId = orderItem.getItemId();
        this.quantity = orderItem.getQuantity();
        this.remark = orderItem.getRemark();
        this.xmlText = orderItem.getXmlText();
        this.price = orderItem.getPrice();
        this.printStatus = orderItem.getPrintStatus();
        this.payed = orderItem.getPayed();
        this.dateTime = orderItem.getDateTime();
        this.status = orderItem.getStatus();
        this.order = orderItem.getOrder();
        this.printer = orderItem.getPrinter();
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public String getXmlText() {
        return xmlText;
    }

    public void setXmlText(String xmlText) {
        this.xmlText = xmlText;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(Integer printStatus) {
        this.printStatus = printStatus;
    }

    public Integer getPayed() {
        return payed;
    }

    public void setPayed(Integer payed) {
        this.payed = payed;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getPrinter() {
		return printer;
	}

	public void setPrinter(String printer) {
		this.printer = printer;
	}
}
