package com.DigitalShop.DigitalTreasure.Entities;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.Data;

@Entity
@Data
public class Purchase{

    @Id
    private Long id;

    private String name;

    private double price;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}