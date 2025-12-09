package com.easystock.service.product;

import com.easystock.dto.product.ProductRequestDto;
import com.easystock.dto.product.ProductResponseDto;
import com.easystock.entity.Product;
import com.easystock.mapper.ProductMapper;
import com.easystock.repository.OrderItemRepository;
import com.easystock.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponseDto create(ProductRequestDto dto) {
        Product product = productMapper.toEntity(dto);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @Override
    public ProductResponseDto findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        return productMapper.toDto(product);
    }

    @Override
    public Page<ProductResponseDto> findAll(Pageable pageable, boolean includeDeleted) {
        Page<Product> products;
        if (includeDeleted) {
            products = productRepository.findAll(pageable);
        } else {
            products = productRepository.findByDeleted(false, pageable);
        }
        return products.map(productMapper::toDto);
    }

    @Override
    public ProductResponseDto update(Long id, ProductRequestDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        productMapper.updateFromDto(dto, product);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toDto(updatedProduct);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        boolean isReferenced = orderItemRepository.existsByProductId(id);
        if (isReferenced) {
            product.setDeleted(true);
            productRepository.save(product);
        } else {
            productRepository.delete(product);
        }
    }

    @Override
    public boolean isStockAvailable(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
        return product.getStock() >= quantity && !product.isDeleted();
    }

    @Override
    @Transactional
    public void decreaseStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

        if (product.getStock() < quantity) {
            throw new IllegalStateException("Not enough stock for product: " + product.getName());
        }

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }
}