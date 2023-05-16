package com.tongtong.oms.order.queue.local;

import com.tongtong.oms.order.queue.MQProducer;

public class LocalMQProducer implements MQProducer {

    private static LocalMQProducer messageQueue = new LocalMQProducer();

    @Override
    public void send(String message) {
        LocalMQ queue = LocalMQ.getInstance();
        if (!queue.offer(message)) {
        }
    }

}