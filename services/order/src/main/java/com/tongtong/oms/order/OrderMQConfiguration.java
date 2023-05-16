package com.tongtong.oms.order;

import com.tongtong.oms.order.queue.*;
import com.tongtong.oms.order.queue.local.LocalMQConsumer;
import com.tongtong.oms.order.queue.local.LocalMQProducer;
import com.tongtong.oms.order.queue.rabbitmq.RabbitMQConsumer;
import com.tongtong.oms.order.queue.rabbitmq.RabbitMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


@Configuration
@PropertySource(value = {"classpath:config.properties"})
public class OrderMQConfiguration {

    @Autowired
    private Environment environment;

    @Autowired
    private OrderConsumer orderConsumer;

    public OrderConsumer getOrderConsumer() {
        return orderConsumer;
    }

    @Bean
    public MQProducer getMessageQueueProducerBean() throws IOException, TimeoutException {
        if (environment.getProperty("order.queue.type").equals("rabbitmq")) {
            return new RabbitMQProducer(environment.getProperty("order.queue.host"),
                    environment.getProperty("order.queue.name"));
        }
        // order.queue.type=local
        return new LocalMQProducer();
    }

    @Bean
    public MQConsumer getMessageQueueConsumerBean() throws IOException, TimeoutException {
        MQConsumer mqConsumer =
                (environment.getProperty("order.queue.type").equals("rabbitmq")) ?
                        new RabbitMQConsumer(environment.getProperty("order.queue.host"),
                                environment.getProperty("order.queue.name")) :
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
