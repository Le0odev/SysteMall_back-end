package com.example.SysteMall_backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter

public class SaleItemDTO {

    private Long id;
    private String productName;
    private Long productId;
    private BigDecimal productPrice;
    private Integer quantity;
    private BigDecimal weight; // Adicionando a propriedade weight para representar o peso
    private BigDecimal subtotal;
    private Boolean isBulk;



    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice.setScale(2, BigDecimal.ROUND_HALF_UP); //tirar imprecisao
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal.setScale(2, BigDecimal.ROUND_HALF_UP); // Set scale
    }


}
