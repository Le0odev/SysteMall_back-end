package com.example.SysteMall_backend.DTOs;

import com.example.SysteMall_backend.entity.Product;
import jakarta.websocket.server.ServerEndpoint;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class SalesDTO {

    private Long id;
    private String productName;
    private LocalDateTime saleDate;
    private BigDecimal saleTotals;
    private List<SaleItemDTO> items;
    private BigDecimal weight;
    private BigDecimal discount;
    private String methodPayment;
    private List<SaleItemDTO> itemss; // Lista de itens da venda


}
