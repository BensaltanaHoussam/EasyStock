package com.easystock.dto.client;

import com.easystock.entity.enums.CustomerTier;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ClientResponseDto {
    private Long id;
    private String name;
    private String email;
    private CustomerTier loyaltyTier;

    // Statistics fields
    private int totalOrders;
    private double totalSpent;
    private LocalDate firstOrderDate;
    private LocalDate lastOrderDate;
}