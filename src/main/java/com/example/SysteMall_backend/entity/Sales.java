package com.example.SysteMall_backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "vendas")
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    @Column(nullable = false)
    private LocalDateTime saleDate;

    @Column
    private BigDecimal saleTotals;

    @Column
    private BigDecimal discount;

    @Column
    private String methodPayment;




    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SaleItem> saleItems;





}
