package com.example.SysteMall_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Getter
@Setter
@Table(name = "categoria")
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(nullable = false)
    private String categoryName;

    @Column(name = "cat_description")
    private String categoryDescription;

    @OneToMany(mappedBy = "category")
    private List<Product> products;











}
