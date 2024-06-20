package com.example.SysteMall_backend.service;

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


    public SalesDTO createSale(List<SaleItemDTO> itemsSale) {
        BigDecimal total = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);

        List<SaleItem> saleItems = new ArrayList<>();
        for (SaleItemDTO itemDTO : itemsSale) {
            // Validações adicionais podem ser incluídas aqui
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            BigDecimal subtotal;
            if (Boolean.TRUE.equals(itemDTO.getIsBulk())) { // Verifica se o produto está sendo vendido a granel
                if (itemDTO.getWeight() == null) {
                    throw new RuntimeException("O peso do produto a granel não está especificado");
                }
                subtotal = product.getProductPrice().multiply(itemDTO.getWeight()).setScale(2, BigDecimal.ROUND_HALF_UP);
            } else {
                if (itemDTO.getQuantity() == null) {
                    throw new RuntimeException("A quantidade do produto não está especificada");
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

        // Cria a venda
        Sales sale = new Sales();
        sale.setSaleDate(LocalDateTime.now());
        sale.setSaleItems(saleItems);
        sale.setSaleTotals(total);
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

    public void deleteAllSales() {salesRepository.deleteAll();}
    private SalesDTO mapToDTO(Sales sales) {
        SalesDTO salesDTO = new SalesDTO();
        salesDTO.setId(sales.getId());
        salesDTO.setProductName(sales.getProductName());
        salesDTO.setSaleDate(sales.getSaleDate());
        salesDTO.setWeight(salesDTO.getWeight());
        salesDTO.setSaleTotals(sales.getSaleTotals());

        salesDTO.setItems(sales.getSaleItems().stream()
                .map(this::mapToSaleItemDTO)
                .collect(Collectors.toList()));
        return salesDTO;
    }

    private SaleItemDTO mapToSaleItemDTO(SaleItem saleItem) {
        SaleItemDTO saleItemDTO = new SaleItemDTO();
        saleItemDTO.setId(saleItem.getId());
        saleItemDTO.setProductName(saleItem.getProduct().getProductName());
        saleItemDTO.setProductId(saleItem.getProduct().getId());
        saleItemDTO.setProductPrice(saleItem.getProductPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        saleItemDTO.setQuantity(saleItem.getQuantity());
        saleItemDTO.setWeight(saleItem.getWeight() != null ? saleItem.getWeight().setScale(2, BigDecimal.ROUND_HALF_UP) : null);
        saleItemDTO.setSubtotal(saleItem.getSubtotal().setScale(2, BigDecimal.ROUND_HALF_UP));
        saleItemDTO.setIsBulk(saleItem.getWeight() != null); // Set isBulk based on weight presence
        return saleItemDTO;
    }


    private BigDecimal calculateSaleTotals(SalesDTO salesDTO) {
        BigDecimal total = BigDecimal.ZERO;
        for (SaleItemDTO item : salesDTO.getItems()) {
            BigDecimal itemTotal;
            if (Boolean.TRUE.equals(item.getIsBulk())) {
                itemTotal = item.getProductPrice().multiply(item.getWeight());
            } else {
                itemTotal = item.getProductPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            }
            total = total.add(itemTotal);
        }
        return total;
    }
}
