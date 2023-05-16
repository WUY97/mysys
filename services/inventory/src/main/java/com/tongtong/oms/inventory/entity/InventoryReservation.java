package com.tongtong.oms.inventory.entity;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * DTO class to send and receive inventory reservation request
 */
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
                "\"inventoryReservationLines\":" + (inventoryReservationLines == null ?
                "null" : Arrays.toString(inventoryReservationLines.toArray())) +
                "}";
    }
}
