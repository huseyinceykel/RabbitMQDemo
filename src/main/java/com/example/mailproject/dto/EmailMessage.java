package com.example.mailproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

// Lombok anotasyonları sayesinde getter, setter ve constructor yazmaktan kurtuluyoruz.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessage implements Serializable {

    @NotBlank(message = "Alıcı e-posta adresi boş olamaz")
    @Email(message = "Geçersiz e-posta formatı")
    private String to;       // Alıcının mail adresi

    @NotBlank(message = "Konu başlığı boş olamaz")
    private String subject;  // Mailin konusu

    @NotBlank(message = "Mesaj içeriği boş olamaz")
    private String body;     // Mailin içeriği

}