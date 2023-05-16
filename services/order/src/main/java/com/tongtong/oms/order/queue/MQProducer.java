package com.tongtong.oms.order.queue;

public interface MQProducer {
    void send(String message);
}
