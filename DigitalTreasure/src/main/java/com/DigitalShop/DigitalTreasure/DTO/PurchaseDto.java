package com.DigitalShop.DigitalTreasure.DTO;

import lombok.Data;

import java.util.List;


public record PurchaseDto(String name, double price) {}
//public record UserProfileDto(String name, String email, double balance, List<PurchaseDto> purchases) {}