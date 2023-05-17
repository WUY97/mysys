package com.tongtong.oms.admin.entity;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Order {
    private String id;
    private String userId;
    private List<OrderLine> orderLines;

    public Order() {
        this.orderLines = new LinkedList<>();
    }

    public Order(String id, String userId) {
        this.id = id;
        this.userId = userId;
        this.orderLines = new LinkedList<>();
    }

    public Order(Order orderEntity) {
        this.id = orderEntity.id;
        this.userId = orderEntity.userId;
        this.orderLines = orderEntity.orderLines;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + (id == null ? "null" : "\"" + id + "\"") + ", " +
                "\"userId\":" + (userId == null ? "null" : "\"" + userId + "\"") + ", " +
                "\"orderLines\":" + (orderLines == null ? "null" : Arrays.toString(orderLines.toArray())) +
                "}";
    }
}
