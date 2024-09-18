package com.example.SysteMall_backend.service;

import com.example.SysteMall_backend.entity.Product;
import com.example.SysteMall_backend.entity.SaleItem;
import com.example.SysteMall_backend.entity.Sales;
import com.example.SysteMall_backend.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public List<Sales> getSalesByDay(LocalDate day) {
        LocalDateTime startOfDay = day.atStartOfDay();
        LocalDateTime endOfDay = day.atTime(23, 59, 59);
        return salesRepository.findBySaleDateBetween(startOfDay, endOfDay);
    }

    public BigDecimal getTotalSalesByDay(LocalDate day) {
        List<Sales> sales = getSalesByDay(day);
        return sales.stream()
                .map(Sales::getSaleTotals)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalSalesByMonthAndYear(int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        List<Sales> sales = salesRepository.findBySaleDateBetween(startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
        return sales.stream()
                .map(Sales::getSaleTotals)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }



    public List<Sales> getSalesByPeriod(LocalDate startDate, LocalDate endDate) {
        return salesRepository.findBySaleDateBetween(startDate.atStartOfDay(), endDate.atStartOfDay());
    }

    public List<Sales> getAllSales() {
        return salesRepository.findAll();
    }

    public Map<Long, BigDecimal> getTotalSalesByCategory(LocalDateTime startDate, LocalDateTime endDate) {
        List<Sales> sales = salesRepository.findBySaleDateBetween(startDate, endDate);

        Map<Long, BigDecimal> salesByCategory = new HashMap<>();

        for (Sales sale : sales) {
            for (SaleItem saleItem : sale.getSaleItems()) {
                Product product = saleItem.getProduct();
                Long categoryId = product.getCategory().getId();
                BigDecimal subtotal = saleItem.getSubtotal();

                salesByCategory.merge(categoryId, subtotal, BigDecimal::add);
            }
        }

        return salesByCategory;
    }

}
