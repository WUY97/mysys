package com.tongtong.oms.inventory.dao.sql;

import com.tongtong.oms.inventory.dao.InventoryDao;
import com.tongtong.oms.inventory.entity.Inventory;
import com.tongtong.oms.inventory.entity.InventoryReservation;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("SQL")
public class DBInventoryDao implements InventoryDao {

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @Autowired(required = false)
    ReservationTxnManagerFactory reservationTxnManagerFactory;

    @Value("${database.type}")
    private String inventoryDBType;

    private ReservationTxnManager reservationTxnManager;

    @PostConstruct
    public void postConstruct() {
        this.reservationTxnManager = reservationTxnManagerFactory.getInstance(jdbcTemplate);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ReservationTxnManager getReservationTxnManager() {
        return reservationTxnManager;
    }

    public void setReservationTxnManager(ReservationTxnManager reservationTxnManager) {
        this.reservationTxnManager = reservationTxnManager;
    }

    private static final String invSelectSql = "select productId, quantity from inventory where productId=?";

    @Override
    public List<Inventory> getInventory() {
        String allInventorySql = "select * from Inventory";
        List<Inventory> inventoryList = jdbcTemplate.query(allInventorySql,
                new BeanPropertyRowMapper<>(Inventory.class));
        if (inventoryList == null) {
            return null;
        }
        return inventoryList;
    }

    @Override
    public Inventory getInventory(String id) {
        Inventory inventory;
        try {
            inventory = jdbcTemplate.queryForObject(invSelectSql,
                    new Object[]{id}, new BeanPropertyRowMapper<>(Inventory.class));
        } catch (EmptyResultDataAccessException ee) {
            return null;
        }
        return inventory;
    }

    private static final String invInsertSql = "insert into Inventory (productId, quantity) values(?,?)";

    @Override
    public boolean insertInventory(Inventory inventory) {
        int retValue = jdbcTemplate.update(invInsertSql,
                new Object[]{inventory.getProductId(), inventory.getQuantity()});
        if (retValue <= 0) {
            return false;
        }
        return true;
    }

    private static final String SQL_DELETE_INVENTORY = "delete from inventory";

    @Override
    public boolean deleteInventory() {
        try {
            jdbcTemplate.update(SQL_DELETE_INVENTORY);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static final String invUpdateSql = "update Inventory set quantity=? where productId=?";

    @Override
    public boolean updateInventory(Inventory inventory) {
        int retValue;
        try {
            retValue = jdbcTemplate.update(invUpdateSql,
                    new Object[]{inventory.getQuantity(), inventory.getProductId()});
        } catch (Exception e) {
            return false;
        }
        if (retValue <= 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean reserveInventory(InventoryReservation inventoryReservation) {
        return getReservationTxnManager().reserveInventory(inventoryReservation);
    }
}
