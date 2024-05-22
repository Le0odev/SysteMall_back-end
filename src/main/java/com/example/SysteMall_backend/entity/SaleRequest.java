package com.example.SysteMall_backend.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class SaleRequest {
    private List<Long> productIds;
    private List<Integer> quantities;

    // Getters e setters
}
