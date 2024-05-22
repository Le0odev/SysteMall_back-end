package com.example.SysteMall_backend.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class SaleItemDTO {

    private Long productId;
    private BigDecimal productPrice;
    private int quantity;

}
