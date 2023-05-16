package com.tongtong.oms.order.processor;

import com.tongtong.oms.order.entity.Order;
import com.tongtong.oms.order.queue.OrderProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderPreProcessor {

    @Autowired
    private OrderProducer orderProducer;

    public boolean queueOrder(Order order) {
        try {
            orderProducer.queueOrder(order);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
