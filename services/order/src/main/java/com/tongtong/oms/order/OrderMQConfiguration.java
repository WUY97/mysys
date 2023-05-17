package com.tongtong.oms.order;

import com.tongtong.oms.order.queue.MQConsumer;
import com.tongtong.oms.order.queue.MQProducer;
import com.tongtong.oms.order.queue.OrderConsumer;
import com.tongtong.oms.order.queue.local.LocalMQConsumer;
import com.tongtong.oms.order.queue.local.LocalMQProducer;
import com.tongtong.oms.order.queue.rabbitmq.RabbitMQConsumer;
import com.tongtong.oms.order.queue.rabbitmq.RabbitMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


@Configuration
public class OrderMQConfiguration {

    @Value("${order.queue.host}")
    private String queueHost;

    @Value("${order.queue.name}")
    private String queueName;

    @Value("${order.queue.type}")
    private String queueType;

    @Autowired
    private OrderConsumer orderConsumer;

    public OrderConsumer getOrderConsumer() {
        return orderConsumer;
    }

    @Bean
    public MQProducer getMessageQueueProducerBean() throws IOException, TimeoutException {
        if (queueType.equals("rabbitmq")) {
            return new RabbitMQProducer(queueHost,
                    queueName);
        }
        // order.queue.type=local
        return new LocalMQProducer();
    }

    @Bean
    public MQConsumer getMessageQueueConsumerBean() throws IOException, TimeoutException {
        MQConsumer mqConsumer =
                (queueType.equals("rabbitmq")) ?
                        new RabbitMQConsumer(queueHost,
                                queueName) :
                        new LocalMQConsumer();
        mqConsumer.setOrderConsumer(getOrderConsumer());
        try {
            mqConsumer.startConsumer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mqConsumer;
    }

}
