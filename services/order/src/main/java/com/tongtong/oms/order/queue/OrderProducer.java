package com.tongtong.oms.order.queue;

import com.google.gson.Gson;
import com.tongtong.oms.order.entity.Order;
import com.tongtong.oms.order.service.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class OrderProducer {

    @Autowired
    private MQProducer mqProducer;

    public MQProducer getMessageQueue() {
        return mqProducer;
    }

    public void setMessageQueue(MQProducer MQProducer) {
        this.mqProducer = MQProducer;
    }

    public void queueOrder(Order order) throws Exception {
        String authHeader = OrderServiceImpl.getThreadLocal().get();
        HashMap<String, String> contextMap = new HashMap<>();
        MQOrder mqOrder = new MQOrder(order, authHeader);
        String message = (new Gson()).toJson(mqOrder);
        mqProducer.send(message);
    }
}
