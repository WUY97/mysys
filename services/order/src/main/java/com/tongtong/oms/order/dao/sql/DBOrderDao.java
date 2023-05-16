package com.tongtong.oms.order.dao.sql;

import com.tongtong.oms.order.dao.OrderDao;
import com.tongtong.oms.order.entity.Order;
import com.tongtong.oms.order.entity.OrderLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component("SQL")
public class DBOrderDao implements OrderDao {

    @Autowired(required = false)
    @Qualifier("orderJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DBOrderLineDao orderLineDao;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String GET_ORDER_SQL = "select * from OrderMaster where id=?";

    @Override
    public Order getOrder(String userId, String id) {
        DBOrder dbOrder;
        try {
            dbOrder = jdbcTemplate.queryForObject(GET_ORDER_SQL, new Object[]{id},
                    new BeanPropertyRowMapper<>(DBOrder.class));
        } catch (Exception e) {
            return null;
        }
        if (dbOrder == null) {
            return null;
        }
        Order order = DBOrder.getOrder(dbOrder);
        List<OrderLine> orderLines = orderLineDao.getOrderLines(id);
        order.setOrderLines(orderLines);
        return order;
    }

    private static final String GET_ORDERS_SQL = "select * from OrderMaster where userId=?";

    @Override
    public List<Order> getOrders(String userId) {
        List<DBOrder> dbOrders;
        try {
            dbOrders = jdbcTemplate.query(GET_ORDERS_SQL, new Object[]{userId},
                    new BeanPropertyRowMapper<>(DBOrder.class));
        } catch (Exception e) {
            return null;
        }
        if (dbOrders.isEmpty()) {
            return new LinkedList<>();
        }
        List<Order> orders = new LinkedList<>();
        for (DBOrder dbOrder : dbOrders) {
            Order order = DBOrder.getOrder(dbOrder);
            List<OrderLine> orderLines = orderLineDao.getOrderLines(order.getId());
            order.setOrderLines(orderLines);
            orders.add(order);
        }
        return orders;
    }

    private static final String ORDER_INSERT_SQL =
            "insert into OrderMaster " +
                    "(id, userId, status, createdDate, createdTime) " +
                    "values(?,?,?,?,?)";

    @Override
    public boolean saveOrder(Order order) {
        DBOrder dbOrder = DBOrder.createDBOrder(order);
        try {
            int retValue = jdbcTemplate.update(ORDER_INSERT_SQL,
                    new Object[]{dbOrder.getId(), dbOrder.getUserId(), dbOrder.getStatus(),
                            dbOrder.getCreatedDate(), dbOrder.getCreatedTime()});
            if (retValue <= 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        if (!orderLineDao.saveOrderLines(order.getId(), order.getOrderLines())) {
            return false;
        }
        return true;
    }

    private static final String DELETE_ORDER_SQL = "delete from OrderMaster where id=?";

    @Override
    public boolean removeOrder(String userId, String id) {
        if (!orderLineDao.removeOrderLines(id)) {
            return false;
        }
        try {
            int rowUpdated = jdbcTemplate.update(DELETE_ORDER_SQL, new Object[]{id});
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static final String DELETE_ORDERS_SQL = "delete from OrderMaster";

    @Override
    public boolean removeOrders() {
        if (!orderLineDao.removeOrdersLines()) {
            return false;
        }
        try {
            int rowsUpdated = jdbcTemplate.update(DELETE_ORDERS_SQL);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
