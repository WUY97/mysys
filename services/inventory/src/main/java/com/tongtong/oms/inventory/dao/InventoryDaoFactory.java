package com.tongtong.oms.inventory.dao;

public interface InventoryDaoFactory {
    InventoryDao getInventoryDao(String dbType);
}
