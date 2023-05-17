package com.tongtong.oms.inventory.service;

import com.tongtong.common.config.AppConfig;
import com.tongtong.common.entity.Role;
import com.tongtong.common.security.Secured;
import com.tongtong.oms.inventory.entity.Inventory;
import com.tongtong.oms.inventory.entity.InventoryReservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Rest interface for adding, searching, modifying and deleting products
 */
@RestController
@RequestMapping(AppConfig.INVENTORY_RESOURCE_PATH)
public class InventoryService {

    @Autowired
    InventoryServiceImpl inventoryServiceBean;

    public InventoryServiceImpl getInventoryServiceBean() {
        return inventoryServiceBean;
    }

    /**
     * Get inventory for all products. Only for user role admin.
     *
     * @return list of product inventory
     */
    @Secured({Role.ADMIN})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Inventory>> getAllInventory() {
        List<Inventory> inventoryList = getInventoryServiceBean().getInventory();
        if (inventoryList == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().body(inventoryList);
    }

    /**
     * Get inventory for a product
     *
     * @param productId id of product for which inventory is to be fetched
     * @return
     */
    @Secured({Role.ADMIN, Role.USER})
    @GetMapping(path = AppConfig.INVENTORY_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Inventory> getInventory(@PathVariable("productId") String productId) {
        Inventory inventory = getInventoryServiceBean().getInventory(productId);
        if (inventory == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(inventory);
        }
        return ResponseEntity.ok().body(inventory);
    }

    /**
     * Insert inventory for a product. To be used by admin role only.
     *
     * @param productId product id
     * @param inventory inventory object
     * @return
     */
    @Secured({Role.ADMIN})
    @PutMapping(path = AppConfig.INVENTORY_PATH, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity insertInventory(@PathVariable("productId") String productId,
                                          @RequestBody Inventory inventory) {
        if (getInventoryServiceBean().insertInventory(inventory)) {
            return ResponseEntity.ok("INSERTED INVENTORY");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INSERT FAILED");
    }

    /**
     * Update inventory
     *
     * @param inventory updated inventory object
     * @return
     */
    @Secured({Role.ADMIN, Role.USER})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity updateInventory(@RequestBody Inventory inventory) {
        if (getInventoryServiceBean().updateInventory(inventory)) {
            return ResponseEntity.ok().body("UPDATE SUCCESSFUL");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("UPDATE FAILED");
    }

    /**
     * Reserve inventory for product order lines
     *
     * @param inventoryReservation object carrying inventory requirements of order lines
     * @return
     */
    @Secured({Role.ADMIN, Role.USER})
    @PostMapping(path = AppConfig.INVENTORY_RESERVATION_PATH, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity reserveInventory(@RequestBody InventoryReservation inventoryReservation) {
        if (getInventoryServiceBean().reserveInventory(inventoryReservation)) {
            return ResponseEntity.ok().body("SUCCESS");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("UPDATE FAILED");
    }

    @Secured({Role.ADMIN})
    @DeleteMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity deleteInventory() {
        if (getInventoryServiceBean().deleteInventory()) {
            return ResponseEntity.ok("DELETED ALL INVENTORY");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DELETE INVENTORY FAILED");
    }

}
