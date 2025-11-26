package com.easystock.dto.order;

import com.easystock.dto.product.ProductResponseDto;
import lombok.Data;

@Data
class OrderItemResponseDto {
    private ProductResponseDto product;
    private int quantity;
    private double unitPrice;
}