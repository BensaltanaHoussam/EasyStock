package com.easystock.repository;

import com.easystock.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Finds products, filtering by their 'deleted' status.
    Page<Product> findByDeleted(boolean deleted, Pageable pageable);
}