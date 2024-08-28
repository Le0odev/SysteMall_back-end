package com.example.SysteMall_backend.controller;

import com.example.SysteMall_backend.DTOs.CreateSaleRequest;
import com.example.SysteMall_backend.DTOs.SaleItemDTO;
import com.example.SysteMall_backend.DTOs.SalesDTO;
import com.example.SysteMall_backend.entity.SaleItem;
import com.example.SysteMall_backend.entity.SaleRequest;
import com.example.SysteMall_backend.entity.Sales;
import com.example.SysteMall_backend.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sales")
@CrossOrigin(origins = "http://localhost:5173") // Adicione o domínio do frontend
public class SaleController {

    private final SalesService salesService;



    @Autowired
    public SaleController(SalesService salesService) {
        this.salesService = salesService;
    }

    @PostMapping("/create")
    public ResponseEntity<SalesDTO> createSale(@RequestBody CreateSaleRequest request) {
        SalesDTO salesDTO = salesService.createSale(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(salesDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SalesDTO>> getAllSales() {
        List<SalesDTO> allSales = salesService.getAllSales();
        return new ResponseEntity<>(allSales, HttpStatus.OK);
    }

    @GetMapping("/last-id")
    public ResponseEntity<Long> getLastSaleId() {
        Long lastSaleId = salesService.getLastSaleId();
        return new ResponseEntity<>(lastSaleId, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesDTO> getSaleById(@PathVariable Long id) {
        SalesDTO salesDTO = salesService.getSalesById(id);
        return ResponseEntity.ok(salesDTO);
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        salesService.deleteSales(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete/all")
    public  ResponseEntity<Void> deleteAllSale(){
        salesService.deleteAllSales();
        return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
