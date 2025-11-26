package com.easystock.mapper;

import com.easystock.dto.product.ProductRequestDto;
import com.easystock.dto.product.ProductResponseDto;
import com.easystock.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponseDto toDto(Product product);

    Product toEntity(ProductRequestDto dto);

    void updateFromDto(ProductRequestDto dto, @MappingTarget Product product);
}