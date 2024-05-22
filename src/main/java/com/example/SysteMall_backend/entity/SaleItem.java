package com.example.SysteMall_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "itens_venda")
public class SaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "venda_id", nullable = false)
    private Sales sale;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private BigDecimal productPrice;

    @Column(nullable = false)
    private int quantity;

    private BigDecimal subtotal;

    public void calculateSubtotal() {
        this.subtotal = product.getProductPrice().multiply(BigDecimal.valueOf(quantity));
    }


}
