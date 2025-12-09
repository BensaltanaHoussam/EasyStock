package com.easystock.service.product;

import com.easystock.dto.product.ProductRequestDto;
import com.easystock.dto.product.ProductResponseDto;
import com.easystock.entity.Product;
import com.easystock.mapper.ProductMapper;
import com.easystock.repository.OrderItemRepository;
import com.easystock.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void create_shouldReturnProductResponseDto() {
        // Arrange
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName("Test Product");
        requestDto.setStock(10);
        requestDto.setUnitPrice(100.0);

        Product product = Product.builder().name("Test Product").stock(10).unitPrice(100.0).build();
        Product savedProduct = Product.builder().id(1L).name("Test Product").stock(10).unitPrice(100.0).build();
        ProductResponseDto expectedResponse = new ProductResponseDto();
        expectedResponse.setId(1L);
        expectedResponse.setName("Test Product");

        when(productMapper.toEntity(requestDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(savedProduct);
        when(productMapper.toDto(savedProduct)).thenReturn(expectedResponse);

        // Act
        ProductResponseDto actualResponse = productService.create(requestDto);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getId(), actualResponse.getId());
        assertEquals(expectedResponse.getName(), actualResponse.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void findById_whenProductExists_shouldReturnProduct() {
        // Arrange
        Long productId = 1L;
        Product product = Product.builder().id(productId).name("Test").build();
        ProductResponseDto expectedDto = new ProductResponseDto();
        expectedDto.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(expectedDto);

        // Act
        ProductResponseDto actualDto = productService.findById(productId);

        // Assert
        assertNotNull(actualDto);
        assertEquals(expectedDto.getId(), actualDto.getId());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void findById_whenProductDoesNotExist_shouldThrowException() {
        // Arrange
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            productService.findById(productId);
        });
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void delete_whenProductIsNotReferenced_shouldDeleteProduct() {
        // Arrange
        Long productId = 1L;
        Product product = Product.builder().id(productId).build();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderItemRepository.existsByProductId(productId)).thenReturn(false);

        // Act
        productService.delete(productId);

        // Assert
        verify(productRepository, times(1)).delete(product);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void delete_whenProductIsReferenced_shouldSoftDeleteProduct() {
        // Arrange
        Long productId = 1L;
        Product product = Product.builder().id(productId).deleted(false).build();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderItemRepository.existsByProductId(productId)).thenReturn(true);

        // Act
        productService.delete(productId);

        // Assert
        assertTrue(product.isDeleted());
        verify(productRepository, times(1)).save(product);
        verify(productRepository, never()).delete(product);
    }

    @Test
    void update_whenProductExists_shouldUpdateAndReturnDto() {
        // Arrange
        Long productId = 1L;
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName("Updated Name");
        requestDto.setStock(50);
        requestDto.setUnitPrice(150.0);

        Product existingProduct = Product.builder().id(productId).name("Old Name").build();
        ProductResponseDto expectedResponse = new ProductResponseDto();
        expectedResponse.setId(productId);
        expectedResponse.setName("Updated Name");

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct); // save returns the updated product
        when(productMapper.toDto(existingProduct)).thenReturn(expectedResponse);

        // Act
        ProductResponseDto actualResponse = productService.update(productId, requestDto);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getId(), actualResponse.getId());
        assertEquals(expectedResponse.getName(), actualResponse.getName());
        verify(productRepository, times(1)).findById(productId);
        verify(productMapper, times(1)).updateFromDto(requestDto, existingProduct);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    void update_whenProductDoesNotExist_shouldThrowException() {
        // Arrange
        Long productId = 1L;
        ProductRequestDto requestDto = new ProductRequestDto();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            productService.update(productId, requestDto);
        });
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).save(any());
    }

    @Test
    void findAll_whenIncludeDeletedIsFalse_shouldCallFindByDeleted() {
        // Arrange
        Pageable pageable = Pageable.unpaged();
        when(productRepository.findByDeleted(false, pageable)).thenReturn(Page.empty());

        // Act
        productService.findAll(pageable, false);

        // Assert
        verify(productRepository, times(1)).findByDeleted(false, pageable);
        verify(productRepository, never()).findAll(pageable);
    }

    @Test
    void findAll_whenIncludeDeletedIsTrue_shouldCallFindAll() {
        // Arrange
        Pageable pageable = Pageable.unpaged();
        when(productRepository.findAll(pageable)).thenReturn(Page.empty());

        // Act
        productService.findAll(pageable, true);

        // Assert
        verify(productRepository, times(1)).findAll(pageable);
        verify(productRepository, never()).findByDeleted(anyBoolean(), any(Pageable.class));
    }

    @Test
    void isStockAvailable_whenStockIsSufficient_shouldReturnTrue() {
        // Arrange
        Long productId = 1L;
        int quantity = 5;
        Product product = Product.builder().id(productId).stock(10).deleted(false).build();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        boolean result = productService.isStockAvailable(productId, quantity);

        // Assert
        assertTrue(result);
    }

    @Test
    void isStockAvailable_whenStockIsInsufficient_shouldReturnFalse() {
        // Arrange
        Long productId = 1L;
        int quantity = 15;
        Product product = Product.builder().id(productId).stock(10).deleted(false).build();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        boolean result = productService.isStockAvailable(productId, quantity);

        // Assert
        assertFalse(result);
    }

    @Test
    void decreaseStock_whenStockIsSufficient_shouldDecreaseStock() {
        // Arrange
        Long productId = 1L;
        int quantityToDecrease = 5;
        Product product = Product.builder().id(productId).stock(10).build();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        productService.decreaseStock(productId, quantityToDecrease);

        // Assert
        assertEquals(5, product.getStock());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void decreaseStock_whenStockIsInsufficient_shouldThrowException() {
        // Arrange
        Long productId = 1L;
        int quantityToDecrease = 15;
        Product product = Product.builder().id(productId).stock(10).build();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            productService.decreaseStock(productId, quantityToDecrease);
        });
        verify(productRepository, never()).save(any());
    }

}
