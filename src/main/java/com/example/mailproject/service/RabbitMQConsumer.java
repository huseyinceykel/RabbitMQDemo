package com.example.mailproject.service;

import com.example.mailproject.config.RabbitMQConfig;
import com.example.mailproject.dto.EmailMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class RabbitMQConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public RabbitMQConsumer(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    // @RabbitListener anotasyonu bu metodun sürekli belirtilen kuyruğu dinlemesini sağlar
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consumeMessage(EmailMessage emailMessage) throws MessagingException {
        LOGGER.info("RabbitMQ'dan mesaj alındı, HTML e-posta hazırlanıyor: -> {}", emailMessage.toString());

        try {
            // Thymeleaf Context hazırlığı
            Context context = new Context();
            context.setVariable("subject", emailMessage.getSubject());
            context.setVariable("body", emailMessage.getBody());

            // HTML içeriğini render et
            String htmlContent = templateEngine.process("email-template", context);

            // MimeMessage oluşturuyoruz
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("hello@canmedya.com");
            helper.setTo(emailMessage.getTo());
            helper.setSubject(emailMessage.getSubject());
            helper.setText(htmlContent, true); // true = HTML içerik

            // Maili gönder!
            javaMailSender.send(mimeMessage);

            LOGGER.info("HTML E-Posta başarıyla gönderildi: {}", emailMessage.getTo());

        } catch (Exception e) {
            LOGGER.error("E-posta gönderimi sırasında hata oluştu, mesaj DLQ'ya düşebilir: ", e);
            // Exception fırlatarak RabbitMQ'nun (retry bittikten sonra) DLQ'ya atmasını sağlıyoruz.
            throw e;
        }
    }
}