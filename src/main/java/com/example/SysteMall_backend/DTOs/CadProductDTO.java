package com.example.SysteMall_backend.DTOs;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

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
    private boolean bulk;
    private String imageUrl;
    private BigDecimal estoquePeso;
    private BigDecimal stockAlertLimit;

    private List<VariationDTO> variations;



    public void setCategoryName(String categoryName) {
    }

    public void setCategoryDescription(String categoryDescription) {
    }

    public void setIsBulk(boolean bulk) {
    }
}


