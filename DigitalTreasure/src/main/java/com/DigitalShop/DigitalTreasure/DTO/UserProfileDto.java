package com.DigitalShop.DigitalTreasure.DTO;

import java.util.List;

public record UserProfileDto(String name, String email, double balance, List<PurchaseDto> purchases) {}
