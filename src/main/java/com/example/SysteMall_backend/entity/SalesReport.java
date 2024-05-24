package com.example.SysteMall_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "sales_report")
public class SalesReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private BigDecimal totalSales;

    @Column
    private Integer totalProductsSold;

    public SalesReport(LocalDate date, BigDecimal totalSales, Integer totalProductsSold){
        this.date = date;
        this.totalSales = totalSales;
        this.totalProductsSold = totalProductsSold;


    }




}
