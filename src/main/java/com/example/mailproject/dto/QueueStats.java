package com.example.mailproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueueStats {
    private String queueName;
    private Integer messageCount;
    private Integer consumerCount;
    private String status;
}
