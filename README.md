# 🚀 RabbitMQ & Spring Boot: Masterpiece Email Service

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3.x-FF6600?logo=rabbitmq&logoColor=white)](https://www.rabbitmq.com/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?logo=docker&logoColor=white)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Bu proje, bir e-posta gönderim servisini temel alarak **kurumsal mimari prensipleri**, **hata toleransı** ve **modern DevOps araçları** ile nasıl güçlendirilebileceğini gösteren bir "Masterpiece" repodur.

## 🌟 Öne Çıkan Özellikler

- **DLQ & Retry Mekanizması:** E-posta gönderimi başarısız olursa sistem otomatik olarak 3 kez tekrar dener. Başarı sağlanamazsa mesaj veri kaybını önlemek için **Dead Letter Queue (DLQ)**'ya aktarılır.
- **Thymeleaf HTML Şablonları:** Sade metinler yerine modern, logolu ve kurumsal tasarımlı HTML e-postalar gönderilir.
- **Strict Validation:** API'ye gelen tüm istekler Hibernate Validator ile sıkı bir denetimden geçer. Geçersiz e-posta formatları anında reddedilir.
- **Global Exception Handling:** Tüm hatalar standart ve açıklayıcı bir JSON formatında kullanıcılara sunulur.
- **Dockerized Architecture:** Uygulama ve RabbitMQ altyapısı tek bir komutla (`docker-compose up`) ayağa kaldırılabilir.

## 🏗️ Mimari Şema

```text
[ Client (Postman/Curl) ]
          |
          v
[ REST Controller (Validation) ]
          |
          v
[ RabbitMQ Producer ] ---> ( email_exchange )
                                  |
                                  v
[ RabbitMQ Consumer ] <--- ( email_queue )
          |
          |-- (Hata Alırsa: 3 Retry) --|
          |                            v
          |--------------------> ( email_dead_letter_queue )
          |
          v
[ SMTP Server (Mailtrap) ] ---> [ Recipient Inbox ]
```

## 🛠️ Teknolojiler

- **Backend:** Java 17, Spring Boot 3.x
- **Mesaj Kuyruğu:** RabbitMQ
- **Şablon Motoru:** Thymeleaf
- **Doğrulama:** Spring Boot Starter Validation
- **Dokümantasyon:** SpringDoc OpenAPI (Swagger)
- **Konteynerleştirme:** Docker, Docker Compose

## 🚀 Hızlı Başlangıç

Sisteminizde Docker yüklü ise projeyi saniyeler içinde ayağa kaldırabilirsiniz. Proje kök dizininde şu komutu çalıştırın:

```bash
docker-compose up -d
```

Uygulama ayağa kalktığında:
- **API:** `http://localhost:8080`
- **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
- **RabbitMQ Management:** `http://localhost:15672` (guest/guest)

## 📡 API Kullanımı

### E-Posta Gönder
**Endpoint:** `POST /api/mail/send`

**Örnek Gövde:**
```json
{
  "to": "test@example.com",
  "subject": "Hoş Geldiniz!",
  "body": "Bu e-posta RabbitMQ üzerinden HTML şablonu kullanılarak gönderilmiştir."
}
```

**Hatalı İstek Örneği (400 Bad Request):**
```json
{
    "status": 400,
    "error": "Geçersiz İstek",
    "message": {
        "to": "Geçersiz e-posta formatı",
        "subject": "Konu başlığı boş olamaz"
    }
}
```

## 🛡️ Hata Yönetimi (Retry & DLQ)
Eğer SMTP sunucusuna bağlanılamazsa:
1. Spring AMQP mesajı 1, 2 ve 4 saniye aralıklarla toplam **3 kez** tekrar dener.
2. 3. deneme de başarısız olursa, mesaj `email_dead_letter_queue` kuyruğuna fırlatılır.
3. Bu sayede hiçbir mesaj kaybolmaz ve daha sonra manuel olarak incelenebilir veya tekrar işleme alınabilir.

---
*Bu proje eğitim amaçlı geliştirilmiş bir "Masterpiece" çalışmasıdır.*
