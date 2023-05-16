package com.tongtong.oms.inventory.dao.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@ConditionalOnBean(DBInventoryDao.class)
public class ReservationTxnManagerFactory {

    @Autowired(required = false)
    TransactionTemplate transactionTemplate;

    @Value("${service.inventory.reservation.txn.type:Pessimistic}")
    private String reservationTxnType;

    public ReservationTxnManager getInstance(JdbcTemplate jdbcTemplate) {
        if (reservationTxnType.equals("Optimistic")) {
            return new OptimisticReservation(jdbcTemplate, transactionTemplate);
        }
        // reservationTxnType.equals("Pessimistic")
        return new PessimisticReservation(jdbcTemplate, transactionTemplate);
    }
}
