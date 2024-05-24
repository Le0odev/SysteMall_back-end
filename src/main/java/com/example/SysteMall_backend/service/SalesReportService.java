package com.example.SysteMall_backend.service;

import com.example.SysteMall_backend.entity.Sales;
import com.example.SysteMall_backend.entity.SalesReport;
import com.example.SysteMall_backend.repository.SalesReportRepository;
import com.example.SysteMall_backend.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class SalesReportService {


    private final SalesRepository salesRepository;


    @Autowired
    public SalesReportService(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    public Sales createSale(Sales sale) {
            return salesRepository.save(sale);
        }

        public BigDecimal getTotalSalesByDay(LocalDate day) {
            List<Sales> sales = salesRepository.findBySaleDate(day);
            return sales.stream()
                    .map(Sales::getSaleTotals)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        public BigDecimal getTotalSalesByMonthAndYear(int month, int year) {
            List<Sales> sales = salesRepository.findBySaleDateBetween(
                    LocalDate.of(year, month, 1),
                    LocalDate.of(year, month, LocalDate.of(year, month, 1).lengthOfMonth()));
            return sales.stream()
                    .map(Sales::getSaleTotals)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        public List<Sales> getAllSales() {
            return salesRepository.findAll();
        }


    // Outros métodos conforme necessário
    }
