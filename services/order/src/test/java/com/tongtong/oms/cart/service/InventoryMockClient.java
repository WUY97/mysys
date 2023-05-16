package com.tongtong.oms.cart.service;

import com.tongtong.oms.order.entity.InventoryReservation;
import com.tongtong.oms.order.processor.InventoryClient;

import java.io.IOException;

public class InventoryMockClient implements InventoryClient {
    @Override
    public boolean reserveInventory(InventoryReservation invRes) throws IOException {
        return true;
    }
}
