package com.example.SysteMall_backend.service;

import com.example.SysteMall_backend.DTOs.CreateSaleRequest;
import com.example.SysteMall_backend.DTOs.SaleItemDTO;
import com.example.SysteMall_backend.DTOs.SalesDTO;
import com.example.SysteMall_backend.entity.Product;
import com.example.SysteMall_backend.entity.SaleItem;
import com.example.SysteMall_backend.entity.Sales;
import com.example.SysteMall_backend.exception.CustomException;
import com.example.SysteMall_backend.repository.ProductRepository;
import com.example.SysteMall_backend.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
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


    public SalesDTO createSale(CreateSaleRequest request) {
    BigDecimal total = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    final int SCALE = 2;
    final RoundingMode ROUND_MODE = RoundingMode.HALF_UP;

    List<SaleItem> saleItems = new ArrayList<>();
    for (SaleItemDTO itemDTO : request.getItemsSale()) {
        Product product = productRepository.findById(itemDTO.getProductId())
                .orElseThrow(() -> new CustomException("Produto não encontrado"));

        BigDecimal subtotal = BigDecimal.ZERO.setScale(SCALE, ROUND_MODE);
        if (Boolean.TRUE.equals(itemDTO.getIsBulk())) {
            if (itemDTO.getWeight() == null || itemDTO.getWeight().compareTo(BigDecimal.ZERO) <= 0) {
                throw new CustomException("O peso do produto a granel não está especificado ou é inválido");
            }
            // Calcular o peso em KG sem arredondamento intermediário
            BigDecimal weightInKg = itemDTO.getWeight().divide(BigDecimal.valueOf(1000), 10, ROUND_MODE); // Maior precisão aqui
            subtotal = product.getProductPrice().multiply(weightInKg); // Sem arredondamento intermediário
        
            // Arredondar o subtotal apenas ao final do cálculo
            subtotal = subtotal.setScale(SCALE, ROUND_MODE);
        
            if (product.getEstoquePeso().compareTo(weightInKg) < 0) {
                throw new CustomException("Estoque insuficiente para o produto a granel");
            }
            product.setEstoquePeso(product.getEstoquePeso().subtract(weightInKg).setScale(SCALE, ROUND_MODE));
        } else {
            if (itemDTO.getQuantity() == null || itemDTO.getQuantity() <= 0) {
                throw new CustomException("A quantidade do produto não está especificada ou é inválida");
            }
            subtotal = product.getProductPrice()
                              .multiply(BigDecimal.valueOf(itemDTO.getQuantity()))
                              .setScale(SCALE, ROUND_MODE);

            if (product.getProductQuantity() < itemDTO.getQuantity()) {
                throw new CustomException("Estoque insuficiente para o produto");
            }
            product.setProductQuantity(product.getProductQuantity() - itemDTO.getQuantity());
        }

        total = total.add(subtotal).setScale(SCALE, ROUND_MODE);

        SaleItem saleItem = new SaleItem();
        saleItem.setProduct(product);
        saleItem.setProductPrice(product.getProductPrice().setScale(SCALE, ROUND_MODE));
        saleItem.setQuantity(itemDTO.getQuantity());
        saleItem.setWeight(itemDTO.getWeight());
        saleItem.setSubtotal(subtotal);
        saleItems.add(saleItem);
    }

    BigDecimal discount = request.getDiscount() != null ? request.getDiscount() : BigDecimal.ZERO;
    if (discount.compareTo(BigDecimal.ZERO) > 0) {
        BigDecimal discountAmount = total.multiply(discount).divide(BigDecimal.valueOf(100), SCALE, ROUND_MODE);
        total = total.subtract(discountAmount).setScale(SCALE, ROUND_MODE);
    }

    Sales sale = new Sales();
    sale.setSaleDate(LocalDateTime.now());
    sale.setSaleItems(saleItems);
    sale.setSaleTotals(total);
    sale.setDiscount(discount);
    sale.setMethodPayment(request.getMethodPayment() != null ? request.getMethodPayment() : "Não especificado");

    saleItems.forEach(item -> item.setSale(sale));

    Sales savedSale = salesRepository.save(sale);
    productRepository.saveAll(saleItems.stream().map(SaleItem::getProduct).collect(Collectors.toList()));

    return mapToDTO(savedSale);
}



    public Long getLastSaleId() {
        Optional<Sales> lastSale = salesRepository.findTopByOrderByIdDesc();
        return lastSale.map(Sales::getId).orElse(null);
    }


    public List<SalesDTO> getAllSales() {
        return salesRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public SalesDTO getSalesById(Long id) {
        return salesRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada"));
    }

    public void deleteSales(Long id) {
        salesRepository.deleteById(id);
    }

    public void deleteAllSales() {
        salesRepository.deleteAll();
    }

    private SalesDTO mapToDTO(Sales sales) {
        SalesDTO salesDTO = new SalesDTO();
        salesDTO.setId(sales.getId());
        salesDTO.setSaleDate(sales.getSaleDate());
        salesDTO.setSaleTotals(sales.getSaleTotals());
        salesDTO.setDiscount(sales.getDiscount());
        salesDTO.setMethodPayment(sales.getMethodPayment());

        List<SaleItemDTO> itemDTOs = sales.getSaleItems().stream()
                .map(this::mapToSaleItemDTO)
                .collect(Collectors.toList());
        salesDTO.setItems(itemDTOs);

        return salesDTO;
    }

    private SaleItemDTO mapToSaleItemDTO(SaleItem saleItem) {
        SaleItemDTO dto = new SaleItemDTO();
        dto.setId(saleItem.getId());
        dto.setProductName(saleItem.getProduct().getProductName());
        dto.setProductId(saleItem.getProduct().getId());
        dto.setProductPrice(saleItem.getProductPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        dto.setQuantity(saleItem.getQuantity() != null ? saleItem.getQuantity() : 0);
        dto.setWeight(saleItem.getWeight() != null ? saleItem.getWeight().setScale(2, BigDecimal.ROUND_HALF_UP) : null);
        dto.setSubtotal(saleItem.getSubtotal().setScale(2, BigDecimal.ROUND_HALF_UP));
        dto.setIsBulk(saleItem.getProduct().isBulk());
        return dto;
    }

    private BigDecimal calculateSaleTotals(SalesDTO salesDTO) {
        BigDecimal total = BigDecimal.ZERO;
        for (SaleItemDTO item : salesDTO.getItems()) {
            BigDecimal itemTotal;
            if (Boolean.TRUE.equals(item.getIsBulk())) {
                // Peso em gramas convertido para kg
                BigDecimal weightInKg = item.getWeight().divide(BigDecimal.valueOf(1000), BigDecimal.ROUND_HALF_UP);
                itemTotal = item.getProductPrice().multiply(weightInKg);
            } else {
                itemTotal = item.getProductPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            }
            total = total.add(itemTotal);
        }
        BigDecimal discountAmount = total.multiply(salesDTO.getDiscount()).divide(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
        return total.subtract(discountAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
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

    public Map<Long, BigDecimal> getTotalSalesByProduct(LocalDateTime startDate, LocalDateTime endDate) {
        List<Sales> sales = salesRepository.findBySaleDateBetween(startDate, endDate);

        Map<Long, BigDecimal> salesByProduct = new HashMap<>();

        for (Sales sale : sales) {
            for (SaleItem saleItem : sale.getSaleItems()) {
                Product product = saleItem.getProduct();
                Long productId = product.getId();
                BigDecimal subtotal = saleItem.getSubtotal();

                salesByProduct.merge(productId, subtotal, BigDecimal::add);
            }
        }

        return salesByProduct;
    }




}
