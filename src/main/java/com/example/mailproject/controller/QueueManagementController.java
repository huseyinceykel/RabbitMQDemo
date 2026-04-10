package com.example.mailproject.controller;

import com.example.mailproject.config.RabbitMQConfig;
import com.example.mailproject.dto.QueueStats;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("/api/queue")
@Tag(name = "Kuyruk Yönetimi", description = "RabbitMQ kuyruklarını izlemek ve yönetmek için kullanılır.")
public class QueueManagementController {

    private final RabbitAdmin rabbitAdmin;

    public QueueManagementController(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
    }

    @GetMapping("/stats")
    @Operation(summary = "Kuyruk İstatistiklerini Getir", description = "Ana kuyruk ve DLQ'daki mesaj sayılarını döner.")
    public ResponseEntity<List<QueueStats>> getQueueStats() {
        List<QueueStats> statsList = new ArrayList<>();

        statsList.add(getStats(RabbitMQConfig.QUEUE_NAME));
        statsList.add(getStats(RabbitMQConfig.DEAD_LETTER_QUEUE));

        return ResponseEntity.ok(statsList);
    }

    @DeleteMapping("/dlq/clear")
    @Operation(summary = "DLQ Kuyruğunu Temizle", description = "Dead Letter Queue içindeki tüm hatalı mesajları siler.")
    public ResponseEntity<String> clearDlq() {
        rabbitAdmin.purgeQueue(RabbitMQConfig.DEAD_LETTER_QUEUE);
        return ResponseEntity.ok("Dead Letter Queue başarıyla temizlendi.");
    }

    private QueueStats getStats(String queueName) {
        Properties props = rabbitAdmin.getQueueProperties(queueName);
        if (props == null) {
            return QueueStats.builder().queueName(queueName).status("BULUNAMADI").build();
        }

        return QueueStats.builder()
                .queueName(queueName)
                .messageCount((Integer) props.get(RabbitAdmin.QUEUE_MESSAGE_COUNT))
                .consumerCount((Integer) props.get(RabbitAdmin.QUEUE_CONSUMER_COUNT))
                .status("AKTİF")
                .build();
    }
}
