package com.example.SysteMall_backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class CreateSaleRequest {
    private List<SaleItemDTO> itemsSale;
    private BigDecimal discount;
    private String methodPayment;


}