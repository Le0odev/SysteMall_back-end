package com.example.SysteMall_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "produto_variacao")
public class ProductVariation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String flavor; // Ex: "Morango", "Chocolate", etc.

    @Column(nullable = false)
    private String codeBar; // Código de barras único para cada variação

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Product product;

    @Column(name = "estoque_quantidade")
    private Integer productQuantity;

    @Column(name = "estoque_peso")
    private Double estoquePeso;



}
