package com.easystock.service.product;

import com.easystock.dto.product.ProductRequestDto;
import com.easystock.dto.product.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponseDto create(ProductRequestDto dto);
    ProductResponseDto findById(Long id);
    Page<ProductResponseDto> findAll(Pageable pageable, boolean includeDeleted);
    ProductResponseDto update(Long id, ProductRequestDto dto);
    void delete(Long id);

    // Methods for stock management (will be used by OrderService later)
    boolean isStockAvailable(Long productId, int quantity);
    void decreaseStock(Long productId, int quantity);
}