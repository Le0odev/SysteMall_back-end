package com.example.SysteMall_backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    @Column(name = "estoque_peso")
    private BigDecimal estoquePeso;

    @Column(name = "alerta_estoque")
    private BigDecimal stockAlertLimit;

    @Column(name = "image_url")

    private String imageUrl;

    private boolean isBulk;


    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<SaleItem> saleItems;


}
