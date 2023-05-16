package com.tongtong.oms.inventory;

import com.tongtong.oms.inventory.dao.InventoryDao;
import com.tongtong.oms.inventory.entity.Inventory;
import com.tongtong.oms.inventory.entity.InventoryReservation;
import com.tongtong.oms.inventory.entity.InventoryReservationLine;

import java.util.List;

public class InventoryMockDao implements InventoryDao {

    private Inventory mockInventory;

    public InventoryMockDao() {
        mockInventory = new Inventory();
        mockInventory.setProductId("");
        mockInventory.setQuantity(0);
    }

    @Override
    public List<Inventory> getInventory() {
        return null;
    }

    @Override
    public Inventory getInventory(String productId) {
        if (mockInventory.getProductId().equals(productId)) {
            return mockInventory;
        }
        return null;
    }

    @Override
    public synchronized boolean updateInventory(Inventory inventory) {
        mockInventory = inventory;
        return true;
    }

    @Override
    public boolean insertInventory(Inventory inventory) {
        mockInventory = inventory;
        return true;
    }

    @Override
    public boolean deleteInventory() {
        return false;
    }

    @Override
    public boolean reserveInventory(InventoryReservation inventoryReservation) {
        List<InventoryReservationLine> invResLines = inventoryReservation.getInventoryReservationLines();
        if (invResLines.size() == 0)
            return true;
        InventoryReservationLine inventory = invResLines.get(0);
        if (mockInventory.getQuantity() >= inventory.getQuantity()) {
            mockInventory.setQuantity(mockInventory.getQuantity() - inventory.getQuantity());
            return true;
        }
        return false;
    }
}
