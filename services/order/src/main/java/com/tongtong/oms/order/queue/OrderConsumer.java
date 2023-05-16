package com.tongtong.oms.order.queue;

import com.google.gson.Gson;
import com.tongtong.oms.order.processor.OrderPostProcessor;
import com.tongtong.oms.order.service.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class OrderConsumer {

    @Autowired
    OrderPostProcessor orderProcessor;

    public OrderPostProcessor getOrderProcessor() {
        return orderProcessor;
    }

    public void setOrderProcessor(OrderPostProcessor orderProcessor) {
        this.orderProcessor = orderProcessor;
    }

    public MQOrder getOrder(String serializedOrderString) {
        return (new Gson()).fromJson(serializedOrderString, MQOrder.class);
    }

    public boolean processOrder(String orderJSON) {
        MQOrder mqOrder = getOrder(orderJSON);
        if (mqOrder == null) {
            return false;
        }
        return processOrder(mqOrder);
    }

    private boolean processOrder(MQOrder mqOrder) {
        OrderServiceImpl.getThreadLocal().set(mqOrder.getAuthHeader());
        boolean success = getOrderProcessor().processOrder(mqOrder.getOrder());
        return success;
    }


}
