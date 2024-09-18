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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
@CrossOrigin(origins = {"http://localhost:5173", "http://10.0.0.107:5173", "http://10.0.0.108:5173"})
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

    @GetMapping("/period")
    public ResponseEntity<List<Sales>> getSalesByPeriod(@RequestParam String startDate, @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<Sales> sales = salesReportService.getSalesByPeriod(start, end);
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @GetMapping("/sales-categories")
    public ResponseEntity<Map<Long, BigDecimal>> getSalesByCategories(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<Long, BigDecimal> salesByCategory = salesReportService.getTotalSalesByCategory(startDate, endDate);
        return new ResponseEntity<>(salesByCategory, HttpStatus.OK);
    }

    @GetMapping("/sales-products")
    public ResponseEntity<Map<Long, BigDecimal>> getSalesByProducts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<Long, BigDecimal> salesByProduct = salesReportService.getTotalSalesByProduct(startDate, endDate);
        return new ResponseEntity<>(salesByProduct, HttpStatus.OK);
    }


}
