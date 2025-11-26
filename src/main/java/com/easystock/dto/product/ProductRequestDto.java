package com.easystock.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductRequestDto {
    @NotBlank(message = "Product name cannot be blank")
    private String name;

    @Min(value = 0, message = "Unit price must not be negative")
    private double unitPrice;

    @Min(value = 0, message = "Stock must not be negative")
    private int stock;
}