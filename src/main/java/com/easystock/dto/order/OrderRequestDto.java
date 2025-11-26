package com.easystock.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {
    @NotNull(message = "Client ID cannot be null")
    private Long clientId;

    @NotEmpty(message = "Order must contain at least one item")
    private List<@Valid OrderItemRequestDto> items;

    private String promoCode;
}