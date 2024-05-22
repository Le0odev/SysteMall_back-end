package com.example.SysteMall_backend.controller;

import com.example.SysteMall_backend.DTOs.SalesDTO;
import com.example.SysteMall_backend.entity.SaleRequest;
import com.example.SysteMall_backend.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sales")
public class SaleController {

    private final SalesService salesService;

    @Autowired
    public SaleController(SalesService salesService) {
        this.salesService = salesService;
    }

    @PostMapping("/create")
    public ResponseEntity<SalesDTO> createSale(@RequestBody SaleRequest createSaleRequest) {
        SalesDTO saleDTO = salesService.createSale(createSaleRequest.getProductIds(), createSaleRequest.getQuantities());
        return new ResponseEntity<>(saleDTO, HttpStatus.CREATED);
    }


    @GetMapping("/all")
    public ResponseEntity<List<SalesDTO>> getAllSales() {
        List<SalesDTO> allSales = salesService.getAllSales();
        return new ResponseEntity<>(allSales, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesDTO> getSaleById(@PathVariable Long id) {
        Optional<SalesDTO> optionalSale = salesService.getSalesById(id);
        return optionalSale.map(salesDTO -> new ResponseEntity<>(salesDTO, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @PutMapping("/{id}")
    public ResponseEntity<SalesDTO> updateSale(@PathVariable Long id, @RequestBody SalesDTO updatedSaleDTO) {
        SalesDTO updatedSale = salesService.updateSales(id, updatedSaleDTO);
        if (updatedSale != null) {
            return new ResponseEntity<>(updatedSale, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        salesService.deleteSales(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
