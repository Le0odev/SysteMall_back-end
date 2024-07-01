package com.example.SysteMall_backend.repository;

import com.example.SysteMall_backend.entity.Category;
import com.example.SysteMall_backend.entity.Product;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByCategoryNameContainingIgnoreCase(String categoryName);
    List<Category> findByCategoryNameContainingIgnoreCaseAndId(String categoryName, Long id);

    Page<Category> findByCategoryNameContainingIgnoreCase(String catgoryName, Pageable pageable);

}
