package com.example.mailproject.controller;

import com.example.mailproject.dto.EmailMessage;
import com.example.mailproject.service.RabbitMQProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mail")
@Tag(name = "E-Posta Servisi", description = "RabbitMQ üzerinden e-posta gönderimi yapar.")
public class EmailController {

    private final RabbitMQProducer producer;

    public EmailController(RabbitMQProducer producer) {
        this.producer = producer;
    }

    // POST isteği ile JSON formatında mail bilgilerini alıyoruz
    @PostMapping("/send")
    @Operation(
            summary = "E-Posta Gönder",
            description = "Gelen e-posta içeriğini doğrular ve RabbitMQ kuyruğuna asenkron işleme için ekler.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Başarıyla kuyruğa eklendi"),
                    @ApiResponse(responseCode = "400", description = "Geçersiz e-posta verisi")
            }
    )
    public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailMessage emailMessage) {

        // Gelen isteği doğrudan RabbitMQ kuyruğuna yolluyoruz
        producer.sendMessage(emailMessage);

        // Kullanıcıyı mailin gitmesi için bekletmeden anında cevap dönüyoruz!
        return ResponseEntity.ok("E-posta gönderim isteği başarıyla sıraya alındı! (RabbitMQ)");
    }
}
