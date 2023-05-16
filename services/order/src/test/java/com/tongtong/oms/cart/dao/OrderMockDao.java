package com.tongtong.oms.cart.dao;

import com.tongtong.oms.cart.config.TestConfig;
import com.tongtong.oms.order.dao.OrderDao;
import com.tongtong.oms.order.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by anurag on 24/03/17.
 */
public class OrderMockDao implements OrderDao {

    private static final Logger logger = LoggerFactory.getLogger(OrderMockDao.class);

    @Override
    public Order getOrder(String userId, String id) {
        if (id.equals(TestConfig.TEST_ORDER_ID_1)) {
            return TestConfig.createOrder(TestConfig.TEST_ORDER_ID_1);
        }
        return null;
    }

    @Override
    public List<Order> getOrders(String userId) {
        List<Order> orders = new LinkedList<>();
        orders.add(TestConfig.createOrder(TestConfig.TEST_ORDER_ID_1));
        return orders;
    }

    @Override
    public boolean saveOrder(Order order) {
        return true;
    }

    @Override
    public boolean removeOrder(String userId, String orderId) {
        return true;
    }

    @Override
    public boolean removeOrders() {
        return false;
    }

}
