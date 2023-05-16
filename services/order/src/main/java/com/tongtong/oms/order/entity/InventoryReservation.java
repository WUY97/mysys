package com.tongtong.oms.order.entity;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class InventoryReservation {
    private List<InventoryReservationLine> inventoryReservationLines;

    public InventoryReservation() {
        this.inventoryReservationLines = new LinkedList<>();
    }

    public List<InventoryReservationLine> getInventoryReservationLines() {
        return inventoryReservationLines;
    }

    public void setInventoryReservationLines(List<InventoryReservationLine> inventoryReservationLines) {
        this.inventoryReservationLines = inventoryReservationLines;
    }

    public void addInvResLine(String productId, float quantity) {
        inventoryReservationLines.add(new InventoryReservationLine(productId, quantity));
    }

    @Override
    public String toString() {
        return "{" +
                "\"invResReqLines\":" + (inventoryReservationLines == null ?
                "null" : Arrays.toString(inventoryReservationLines.toArray())) +
                "}";
    }
}

class InventoryReservationLine {
    String productId;
    float quantity;

    public InventoryReservationLine(String productId, float quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "{" +
                "\"productId\":" + (productId == null ? "null" : "\"" + productId + "\"") + ", " +
                "\"quantity\":\"" + quantity + "\"" +
                "}";
    }
}
