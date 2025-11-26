package com.easystock.dto.order;

import com.easystock.dto.client.ClientResponseDto;
import com.easystock.entity.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {
    private Long id;
    private ClientResponseDto client;
    private List<OrderItemResponseDto> items;
    private LocalDateTime orderDate;
    private OrderStatus status;


    private double subtotal;
    private double discountAmount;
    private double taxAmount;
    private double total;
    private double amountRemaining;
    private String promoCode;
}

