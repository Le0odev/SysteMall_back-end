package com.example.SysteMall_backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Table(name = "produto")
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(nullable = false, length = 50)
    private String productName;

    @Column(nullable = true)
    private String codeBar;

    @Column(nullable = true)
    private String productDescription;

    @Column(nullable = false)
    private BigDecimal productPrice;

    @Column(name = "estoque_quant")
    private Integer productQuantity;



    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private boolean isBulk;


}
