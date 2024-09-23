package com.example.SysteMall_backend.repository;

import com.example.SysteMall_backend.entity.ProductVariation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariationRepository extends JpaRepository<ProductVariation, Long> {

    // Buscar todas as variações por produto
    List<ProductVariation> findByProductId(Long productId);

    // Buscar por código de barras específico
    ProductVariation findByCodeBar(String codeBar);

    void deleteByProductId(Long productId);  // Adicionando o método delete


}
