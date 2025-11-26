package com.easystock.dto.product;

import lombok.Data;

@Data
public class ProductResponseDto {
    private Long id;
    private String name;
    private double unitPrice;
    private int stock;
    private boolean deleted;
}