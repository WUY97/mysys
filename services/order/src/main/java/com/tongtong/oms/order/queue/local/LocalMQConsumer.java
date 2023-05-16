package com.tongtong.oms.order.queue.local;

import com.tongtong.oms.order.queue.MQConsumer;
import com.tongtong.oms.order.queue.OrderConsumer;

public class LocalMQConsumer implements MQConsumer {

    private OrderConsumer orderConsumer;

    public OrderConsumer getOrderConsumer() {
        return orderConsumer;
    }

    @Override
    public void setOrderConsumer(OrderConsumer orderProcessor) {
        this.orderConsumer = orderProcessor;
    }

    @Override
    public void startConsumer() {
        (new Thread(new LocalMQConsumerPoller())).start();
    }

    public void processMessage(String message) {
        getOrderConsumer().processOrder(message);
    }

    class LocalMQConsumerPoller implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                String message = LocalMQ.getInstance().getFromQueue();
                if (message != null) {
                    processMessage(message);
                }
            }
        }
    }
}