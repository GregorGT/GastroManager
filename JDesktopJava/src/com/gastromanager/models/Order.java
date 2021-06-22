package com.gastromanager.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Order implements Serializable {
    private Integer Id;
    private String floorId;
    private String tableId;
    private String transactionId;
    private String humanReadableId;
    private String menuId;
    private String memberId;
    private String staffId;
    private LocalDateTime dateTime;
    private Double amount;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getHumanReadableId() {
        return humanReadableId;
    }

    public void setHumanReadableId(String humanReadableId) {
        this.humanReadableId = humanReadableId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
