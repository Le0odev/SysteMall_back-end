package com.example.SysteMall_backend.controller;

import com.example.SysteMall_backend.entity.Sales;
import com.example.SysteMall_backend.service.SalesReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/report")
public class SalesReportController {

    private final SalesReportService salesReportService;

    @Autowired
    public SalesReportController(SalesReportService salesReportService) {
        this.salesReportService = salesReportService;
    }

    @PostMapping
    public ResponseEntity<Sales> createSale(@RequestBody Sales sale) {
        Sales createdSale = salesReportService.createSale(sale);
        return new ResponseEntity<>(createdSale, HttpStatus.CREATED);
    }

    @GetMapping("/day/{date}")
    public ResponseEntity<List<Sales>> getSalesByDay(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Sales> sales = salesReportService.getSalesByDay(date);
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @GetMapping("/day/total/{date}")
    public ResponseEntity<BigDecimal> getTotalSalesByDay(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        BigDecimal totalSales = salesReportService.getTotalSalesByDay(date);
        return new ResponseEntity<>(totalSales, HttpStatus.OK);
    }

    @GetMapping("/month/{year}/{month}")
    public ResponseEntity<BigDecimal> getTotalSalesByMonth(@PathVariable int year, @PathVariable int month) {
        BigDecimal totalSales = salesReportService.getTotalSalesByMonthAndYear(month, year);
        return new ResponseEntity<>(totalSales, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Sales>> getAllSales() {
        List<Sales> allSales = salesReportService.getAllSales();
        return new ResponseEntity<>(allSales, HttpStatus.OK);
    }

    // Outros endpoints conforme necessário
}
