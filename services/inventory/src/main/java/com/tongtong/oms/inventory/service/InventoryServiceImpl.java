package com.tongtong.oms.inventory.service;

import com.tongtong.oms.inventory.dao.InventoryDao;
import com.tongtong.oms.inventory.dao.InventoryDaoFactory;
import com.tongtong.oms.inventory.entity.Inventory;
import com.tongtong.oms.inventory.entity.InventoryReservation;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * Provides implementation of Inventory Service methods
 */
@Component
public class InventoryServiceImpl {

    @Autowired
    private InventoryDaoFactory inventoryDaoFactory;

    private InventoryDao inventoryDaoBean;

    @Value("${database.type}")
    private String inventoryDBType;

    @PostConstruct
    public void postConstruct()
    {
        this.inventoryDaoBean = inventoryDaoFactory.getInventoryDao(inventoryDBType);
    }

    public void setInventoryDaoBean(InventoryDao inventoryDaoBean) {
        this.inventoryDaoBean = inventoryDaoBean;
    }

    public InventoryDao getInventoryDaoBean() {
        return inventoryDaoBean;
    }

    /**
     *
     * @apiNote             Get inventory for all products
     * @return              success
     */
    public List<Inventory> getInventory() {
        List<Inventory> inventoryList = getInventoryDaoBean().getInventory();
        inventoryList.sort(new Comparator<Inventory>() {
            @Override
            public int compare(Inventory o1, Inventory o2) {
                return o1.getProductId().compareTo(o2.getProductId());
            }
        });
        return inventoryList;
    }

    /**
     * @param productId     productId for which availability is needed
     * @return              success
     */
    public Inventory getInventory(String productId) {
        return getInventoryDaoBean().getInventory(productId);
    }

    /**
     * @param inventoryReservation list of inventory lines for which reservation is needed
     * @return success
     */
    public boolean reserveInventory(InventoryReservation inventoryReservation) {
        return getInventoryDaoBean().reserveInventory(inventoryReservation);
    }

    /**
     * @param inventory     inventory to be updated
     * @return              success
     */
    public boolean updateInventory(Inventory inventory) {
        return getInventoryDaoBean().insertInventory(inventory);
    }

    /**
     * @param inventory     inventory to be inserted
     * @return              success
     */
    public boolean insertInventory(Inventory inventory) {
        return getInventoryDaoBean().insertInventory(inventory);
    }

    /**
     * Delete all inventory records
     * @return
     */
    public boolean deleteInventory() {
        return getInventoryDaoBean().deleteInventory();
    }
}
