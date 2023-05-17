package com.tongtong.oms.order.queue.rabbitmq;

import com.rabbitmq.client.*;
import com.tongtong.oms.order.queue.MQConsumer;
import com.tongtong.oms.order.queue.OrderConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

class RabbitMQCallback implements DeliverCallback {
    private OrderConsumer orderConsumer;

    public void setOrderConsumer(OrderConsumer orderConsumer) {
        this.orderConsumer = orderConsumer;
    }

    @Override
    public void handle(String s, Delivery delivery) throws IOException {
        String message = new String(delivery.getBody(), "UTF-8");
        orderConsumer.processOrder(message);
    }

}

public class RabbitMQConsumer implements MQConsumer {

    private String queueName;
    private Connection connection;
    private Channel channel;

    private OrderConsumer orderConsumer;

    public OrderConsumer getOrderConsumer() {
        return orderConsumer;
    }

    @Override
    public void setOrderConsumer(OrderConsumer orderProcessor) {
        this.orderConsumer = orderProcessor;
    }

    public RabbitMQConsumer(String hostName, String queueName)
            throws IOException, TimeoutException {
        this.queueName = queueName;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(hostName);
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(queueName, false, false, false, null);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void startConsumer() throws IOException {
        RabbitMQCallback callback = new RabbitMQCallback();
        callback.setOrderConsumer(getOrderConsumer());
        try {
            channel.basicConsume(queueName, true, callback, consumerTag -> {
            });
        } catch (Exception e) {
            throw e;
        }
    }

}
