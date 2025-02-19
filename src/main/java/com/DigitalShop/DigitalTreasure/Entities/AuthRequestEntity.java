package com.DigitalShop.DigitalTreasure.Entities;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Data
public class AuthRequestEntity {
    private String username;
    private String password;
}
