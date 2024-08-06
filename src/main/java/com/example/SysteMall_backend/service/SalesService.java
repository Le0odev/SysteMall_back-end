package com.example.SysteMall_backend.service;

import com.example.SysteMall_backend.DTOs.CreateSaleRequest;
import com.example.SysteMall_backend.DTOs.SaleItemDTO;
import com.example.SysteMall_backend.DTOs.SalesDTO;
import com.example.SysteMall_backend.entity.Product;
import com.example.SysteMall_backend.entity.SaleItem;
import com.example.SysteMall_backend.entity.Sales;
import com.example.SysteMall_backend.repository.ProductRepository;
import com.example.SysteMall_backend.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
        BigDecimal total = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);

        List<SaleItem> saleItems = new ArrayList<>();
        for (SaleItemDTO itemDTO : request.getItemsSale()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            BigDecimal subtotal = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
            if (Boolean.TRUE.equals(itemDTO.getIsBulk())) {
                if (itemDTO.getWeight() == null || itemDTO.getWeight().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new RuntimeException("O peso do produto a granel não está especificado ou é inválido");
                }
                // Corrigindo o cálculo para produtos a granel (considerando o peso em gramas e preço por kg)
                BigDecimal weightInKg = itemDTO.getWeight().divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP);
                subtotal = product.getProductPrice().multiply(weightInKg).setScale(2, BigDecimal.ROUND_HALF_UP);
            } else {
                if (itemDTO.getQuantity() == null || itemDTO.getQuantity() <= 0) {
                    throw new RuntimeException("A quantidade do produto não está especificada ou é inválida");
                }
                subtotal = product.getProductPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())).setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            total = total.add(subtotal).setScale(2, BigDecimal.ROUND_HALF_UP);

            SaleItem saleItem = new SaleItem();
            saleItem.setProduct(product);
            saleItem.setProductPrice(product.getProductPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            saleItem.setQuantity(itemDTO.getQuantity());
            saleItem.setWeight(itemDTO.getWeight());
            saleItem.setSubtotal(subtotal);
            saleItems.add(saleItem);
        }

        // Aplica o desconto percentual se fornecido
        BigDecimal discount = request.getDiscount() != null ? request.getDiscount() : BigDecimal.ZERO;
        if (discount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discountAmount = total.multiply(discount).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
            total = total.subtract(discountAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        // Cria a venda
        Sales sale = new Sales();
        sale.setSaleDate(LocalDateTime.now());
        sale.setSaleItems(saleItems);
        sale.setSaleTotals(total);
        sale.setDiscount(discount);
        sale.setMethodPayment(request.getMethodPayment() != null ? request.getMethodPayment() : "Não especificado");

        saleItems.forEach(item -> item.setSale(sale));

        // Salva a venda no banco de dados
        Sales savedSale = salesRepository.save(sale);

        // Mapeia a venda para DTO e retorna
        return mapToDTO(savedSale);
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
}
