package com.example.mailproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/system")
@Tag(name = "Sistem Bilgileri", description = "Uygulama ve çalışma ortamı hakkında bilgi verir.")
public class SystemInfoController {

    @GetMapping("/info")
    @Operation(summary = "Sistem Durumunu Getir", description = "Uygulama versiyonu ve güncel sunucu saatini döner.")
    public ResponseEntity<Map<String, Object>> getSystemInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("appName", "RabbitMQ Masterpiece Email Service");
        info.put("version", "1.0.0");
        info.put("javaVersion", System.getProperty("java.version"));
        info.put("serverTime", LocalDateTime.now());
        info.put("status", "UP");

        return ResponseEntity.ok(info);
    }
}
