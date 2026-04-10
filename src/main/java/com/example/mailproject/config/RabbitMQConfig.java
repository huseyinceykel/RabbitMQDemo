package com.example.mailproject.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "email_queue";
    public static final String EXCHANGE_NAME = "email_exchange";
    public static final String ROUTING_KEY = "email_routing_key";

    // DLQ ve DLX Tanımları
    public static final String DEAD_LETTER_QUEUE = "email_dead_letter_queue";
    public static final String DEAD_LETTER_EXCHANGE = "email_dead_letter_exchange";
    public static final String DEAD_LETTER_ROUTING_KEY = "email_dead_letter_routing_key";

    // 1. Ana Kuyruğumuz (Queue) - DLQ parametreleri ile
    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    // 2. Ana Yönlendiricimiz (Exchange)
    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    // 3. Ana Kuyruk ve Yönlendiriciyi birbirine bağlıyoruz (Binding)
    @Bean
    public Binding binding(Queue emailQueue, DirectExchange emailExchange) {
        return BindingBuilder.bind(emailQueue).to(emailExchange).with(ROUTING_KEY);
    }

    // 4. DLQ (Dead Letter Queue)
    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DEAD_LETTER_QUEUE);
    }

    // 5. DLX (Dead Letter Exchange)
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

    // 6. DLQ ve DLX Bağlantısı
    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(DEAD_LETTER_ROUTING_KEY);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    // 7. JSON Dönüştürücü
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}