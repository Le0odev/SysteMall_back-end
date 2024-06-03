package com.example.SysteMall_backend.DTOs;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CadProductDTO {
    private Long id;
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private Integer productQuantity;
    private Long categoryId;
    private String codeBar;



}


