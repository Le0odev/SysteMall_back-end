package com.example.SysteMall_backend.repository;

import com.example.SysteMall_backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


        List<Product> findByProductNameContainingIgnoreCase(String productName);
        List<Product> findByProductNameContainingIgnoreCaseAndCategoryId(String productName, Long categoryId);
        List<Product> findByCodeBar(String codeBar);
        List<Product> findByCodeBarAndCategoryId(String codeBar, Long categoryId);



}
