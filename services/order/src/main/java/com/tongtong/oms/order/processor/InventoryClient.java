package com.tongtong.oms.order.processor;

import com.tongtong.oms.order.entity.InventoryReservation;

import java.io.IOException;


public interface InventoryClient {

    public boolean reserveInventory(InventoryReservation inventoryReservationRequest) throws IOException;

}
