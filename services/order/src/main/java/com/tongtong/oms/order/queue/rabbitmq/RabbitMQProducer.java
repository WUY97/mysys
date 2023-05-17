package com.tongtong.oms.order.queue.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.tongtong.oms.order.queue.MQProducer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQProducer implements MQProducer {

    private String queueName;
    private Connection connection;
    private Channel channel;

    public RabbitMQProducer(String hostName, String queueName) throws IOException, TimeoutException {
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
    public void send(String message) {
        try {
            channel.basicPublish("", queueName, null, message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
