package com.tongtong.oms.inventory.dao.sql;

import com.tongtong.oms.inventory.entity.Inventory;
import com.tongtong.oms.inventory.entity.InventoryReservation;
import com.tongtong.oms.inventory.entity.InventoryReservationLine;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

public class PessimisticReservation extends ReservationTxnManager {

    private static final String invSelectQuery = "select productId, quantity from inventory where productId=? for update";
    public static final String invUpdateSql = "update Inventory set quantity=? where productId=?";

    public PessimisticReservation(JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate) {
        super(jdbcTemplate, transactionTemplate);
    }

    @Override
    protected String getInventoryQuery() {
        return invSelectQuery;
    }

    @Override
    protected int reserveInventoryExecute(InventoryReservation inventoryReservation)
            throws Exception {
        List<Inventory> inventoryList = getInventory(inventoryReservation);

        for (int i=0; i<inventoryReservation.getInventoryReservationLines().size(); i++) {
            InventoryReservationLine line = inventoryReservation.getInventoryReservationLines().get(i);
            Inventory inventory = inventoryList.get(i);
            int retValue;
            try {
                retValue = jdbcTemplate.update(invUpdateSql,
                        new Object[]{inventory.getQuantity() - line.getQuantity(), line.getProductId()});
            } catch(Exception e) {
                throw e;
            }
            if (retValue <= 0) {
                throw new Exception("Unable to update inventory reservation line : "+line);
            }
        }
        return TXN_SUCCESS;
    }
}
