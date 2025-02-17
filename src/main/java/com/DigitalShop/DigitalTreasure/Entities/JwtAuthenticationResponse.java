
package com.DigitalShop.DigitalTreasure.Entities;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@Data
public class JwtAuthenticationResponse {

    // Сеттер для accessToken (если нужно)
    // Геттер для accessToken
    private String accessToken;
    // Сеттер для tokenType (если нужно)
    // Геттер для tokenType
    private String tokenType = "Bearer";  // Обычно указывают тип токена

    // Конструктор с параметрами
    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

}