package com.example.SysteMall_backend.service;

import com.example.SysteMall_backend.DTOs.SaleItemDTO;
import com.example.SysteMall_backend.DTOs.SalesDTO;
import com.example.SysteMall_backend.entity.Product;
import com.example.SysteMall_backend.entity.SaleItem;
import com.example.SysteMall_backend.entity.Sales;
import com.example.SysteMall_backend.repository.ProductRepository;
import com.example.SysteMall_backend.repository.SalesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SalesService {

    private final ProductRepository productRepository;
    private final SalesRepository salesRepository;

    @Autowired
    public SalesService(SalesRepository salesRepository, ProductRepository productRepository) {
        this.salesRepository = salesRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public SalesDTO createSale(List<Long> productIds, List<Integer> quantities) {
        if (productIds.size() != quantities.size()) {
            throw new IllegalArgumentException("Product IDs and quantities must have the same length");
        }

        Sales sale = new Sales();
        sale.setSaleDate(LocalDateTime.now());  // Define a data da venda aqui

        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i < productIds.size(); i++) {
            Long productId = productIds.get(i);
            Integer quantity = quantities.get(i);

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            BigDecimal itemTotal = product.getProductPrice().multiply(BigDecimal.valueOf(quantity));
            total = total.add(itemTotal);
        }

        sale.setSaleTotals(total);
        Sales savedSale = salesRepository.save(sale);

        SalesDTO salesDTO = new SalesDTO();
        salesDTO.setId(savedSale.getId());
        salesDTO.setSaleDate(savedSale.getSaleDate());
        salesDTO.setSaleTotals(savedSale.getSaleTotals());

        return salesDTO;
    }

    public List<SalesDTO> getAllSales() {
        return salesRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<SalesDTO> getSalesById(Long id) {
        return salesRepository.findById(id)
                .map(this::mapToDTO);
    }

    public SalesDTO updateSales(Long id, SalesDTO updatedSalesDTO) {
        return salesRepository.findById(id)
                .map(sales -> {
                    sales.setSaleTotals(calculateSaleTotals(updatedSalesDTO));
                    return mapToDTO(salesRepository.save(sales));
                })
                .orElseThrow(() -> new RuntimeException("Venda não encontrada"));
    }

    public void deleteSales(Long id) {
        salesRepository.deleteById(id);
    }

    private SalesDTO mapToDTO(Sales sales) {
        SalesDTO salesDTO = new SalesDTO();
        salesDTO.setId(sales.getId());
        salesDTO.setSaleDate(sales.getSaleDate());
        salesDTO.setSaleTotals(sales.getSaleTotals());
        return salesDTO;
    }

    private BigDecimal calculateSaleTotals(SalesDTO salesDTO) {
        BigDecimal total = BigDecimal.ZERO;
        for (SaleItemDTO item : salesDTO.getItems()) {
            BigDecimal itemTotal = item.getProductPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(itemTotal);
        }
        return total;
    }

}
