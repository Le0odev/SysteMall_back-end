package com.example.SysteMall_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class FlavorVariation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String flavor;
    private String codeBar;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // getters e setters
}
