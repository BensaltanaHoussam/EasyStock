package com.easystock.controller.product;

import com.easystock.config.auth.Auth;
import com.easystock.dto.product.ProductRequestDto;
import com.easystock.dto.product.ProductResponseDto;
import com.easystock.entity.enums.UserRole;
import com.easystock.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody @Valid ProductRequestDto dto) {
        return new ResponseEntity<>(productService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Auth(allowedRoles = {UserRole.ADMIN, UserRole.CLIENT}) // Both can view a product
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping
    @Auth(allowedRoles = {UserRole.ADMIN, UserRole.CLIENT}) // Both can list products
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
            Pageable pageable,
            @RequestParam(defaultValue = "false") boolean includeDeleted) {
        return ResponseEntity.ok(productService.findAll(pageable, includeDeleted));
    }

    @PutMapping("/{id}")
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequestDto dto) {
        return ResponseEntity.ok(productService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Auth(allowedRoles = {UserRole.ADMIN})
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}