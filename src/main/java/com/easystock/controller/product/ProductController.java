package com.easystock.controller.product;

import com.easystock.config.auth.Auth;
import com.easystock.dto.product.ProductRequestDto;
import com.easystock.dto.product.ProductResponseDto;
import com.easystock.entity.enums.UserRole;
import com.easystock.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody @Valid ProductRequestDto dto) {
        log.info("Request to create product with name: {}", dto.getName());
        ProductResponseDto response = productService.create(dto);
        log.info("Product created successfully with ID: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Auth(allowedRoles = {UserRole.ADMIN, UserRole.CLIENT}) // Both can view a product
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        log.info("Request to get product by ID: {}", id);
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping
    @Auth(allowedRoles = {UserRole.ADMIN, UserRole.CLIENT}) // Both can list products
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
            Pageable pageable,
            @RequestParam(defaultValue = "false") boolean includeDeleted) {
        log.info("Request to get all products for page: {}, includeDeleted: {}", pageable.getPageNumber(), includeDeleted);
        return ResponseEntity.ok(productService.findAll(pageable, includeDeleted));
    }

    @PutMapping("/{id}")
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequestDto dto) {
        log.info("Request to update product with ID: {}", id);
        ProductResponseDto response = productService.update(id, dto);
        log.info("Product updated successfully with ID: {}", response.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("Request to delete product with ID: {}", id);
        productService.delete(id);
        log.info("Product deleted successfully with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
