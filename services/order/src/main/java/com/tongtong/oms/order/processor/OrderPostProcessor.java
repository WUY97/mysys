package com.tongtong.oms.order.processor;

import com.tongtong.oms.order.dao.OrderDao;
import com.tongtong.oms.order.dao.OrderDaoFactory;
import com.tongtong.oms.order.entity.InventoryReservation;
import com.tongtong.oms.order.entity.Order;
import com.tongtong.oms.order.entity.OrderLine;
import com.tongtong.oms.order.entity.OrderStatus;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class OrderPostProcessor {

    @Autowired
    private OrderDaoFactory orderDaoFactory;

    private OrderDao orderDaoBean;

    @Value("${database.type}")
    private String orderDBType;

    @Autowired
    private InventoryClient inventoryClientBean;

    @PostConstruct
    public void postConstruct() throws Exception {
        this.orderDaoBean = orderDaoFactory.getOrderDao(orderDBType);
    }

    public OrderDao getOrderDaoBean() {
        return orderDaoBean;
    }

    public void setOrderDaoBean(OrderDao orderDaoBean) {
        this.orderDaoBean = orderDaoBean;
    }

    public void setInventoryClientBean(InventoryClient inventoryClientBean) {
        this.inventoryClientBean = inventoryClientBean;
    }

    public InventoryClient getInventoryClientBean() {
        return inventoryClientBean;
    }

    public boolean processOrder(Order order) {
        return reserveInventory(order);
    }

    private boolean reserveInventory(Order order) {
        List<OrderLine> orderLines = order.getOrderLines();
        InventoryReservation inventoryReservation = new InventoryReservation();
        for (OrderLine ol : orderLines) {
            inventoryReservation.addInvResLine(ol.getProductId(), ol.getQuantity());
        }
        try {
            if (!getInventoryClientBean().reserveInventory(inventoryReservation)) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        // persist order
        order.setStatus(OrderStatus.CREATED);
        if (!getOrderDaoBean().saveOrder(order)) {
            // ToDo: Async Rollback inventory reservations
        }
        return true;
    }

}
