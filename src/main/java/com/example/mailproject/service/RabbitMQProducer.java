package com.example.mailproject.service;

import com.example.mailproject.config.RabbitMQConfig;
import com.example.mailproject.dto.EmailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

    // Konsola bilgi yazdırmak için logger ekliyoruz.
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);

    // RabbitMQ ile konuşmamızı sağlayan ana sınıf
    private final RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // Bu metod çağrıldığında mesajı kuyruğa fırlatacak
    public void sendMessage(EmailMessage emailMessage) {
        LOGGER.info("Mesaj RabbitMQ'ya gönderiliyor: -> {}", emailMessage.toString());

        // Hangi exchange'e? Hangi routing key? ve Hangi mesaj?
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                emailMessage
        );
    }
}