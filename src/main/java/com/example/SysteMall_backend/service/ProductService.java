package com.example.SysteMall_backend.service;

import com.example.SysteMall_backend.DTOs.CadProductDTO;
import com.example.SysteMall_backend.entity.Category;
import com.example.SysteMall_backend.entity.Product;
import com.example.SysteMall_backend.exception.CustomException;
import com.example.SysteMall_backend.repository.CategoryRepository;
import com.example.SysteMall_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public CadProductDTO saveProduct(CadProductDTO productDTO) {
        Product product = new Product();
        product.setProductName(productDTO.getProductName());
        product.setProductDescription(productDTO.getProductDescription());
        product.setProductPrice(productDTO.getProductPrice());
        product.setCodeBar(productDTO.getCodeBar());
        product.setBulk(productDTO.isBulk());
        product.setImageUrl(productDTO.getImageUrl());
        product.setProductQuantity(productDTO.getProductQuantity());
        product.setEstoquePeso(productDTO.getEstoquePeso());
        product.setStockAlertLimit(productDTO.getStockAlertLimit());
        product.setFlavors(productDTO.getFlavors());



        if (productDTO.getCategoryId() != null) {
            Optional<Category> category = categoryRepository.findById(productDTO.getCategoryId());
            category.ifPresent(product::setCategory);
        }

        Product savedProduct = productRepository.save(product);
        return mapToDTO(savedProduct);
    }

    public CadProductDTO updateProduct(Long id, CadProductDTO updatedProductDTO) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setProductName(updatedProductDTO.getProductName());
                    product.setProductDescription(updatedProductDTO.getProductDescription());
                    product.setProductPrice(updatedProductDTO.getProductPrice());
                    product.setCodeBar(updatedProductDTO.getCodeBar());
                    product.setBulk(updatedProductDTO.isBulk()); // Atualizando o campo isBulk
                    product.setImageUrl(updatedProductDTO.getImageUrl());
                    product.setProductQuantity(updatedProductDTO.getProductQuantity()); // Atualizando o estoque
                    product.setEstoquePeso(updatedProductDTO.getEstoquePeso());
                    product.setStockAlertLimit(updatedProductDTO.getStockAlertLimit());
                    product.setFlavors(updatedProductDTO.getFlavors());


                    if (updatedProductDTO.getCategoryId() != null) {
                        Optional<Category> category = categoryRepository.findById(updatedProductDTO.getCategoryId());
                        category.ifPresent(product::setCategory);
                    }

                    return mapToDTO(productRepository.save(product));
                })
                .orElseThrow(() -> new CustomException("Produto não encontrado"));
    }


    private CadProductDTO mapToDTO(Product product) {
        CadProductDTO productDTO = new CadProductDTO();
        productDTO.setId(product.getId());
        productDTO.setProductName(product.getProductName());
        productDTO.setProductDescription(product.getProductDescription());
        productDTO.setProductPrice(product.getProductPrice());
        productDTO.setCodeBar(product.getCodeBar());
        productDTO.setProductQuantity(product.getProductQuantity());
        productDTO.setBulk(product.isBulk());
        productDTO.setImageUrl(product.getImageUrl());
        productDTO.setEstoquePeso(product.getEstoquePeso());
        productDTO.setStockAlertLimit(product.getStockAlertLimit());
        productDTO.setFlavors(product.getFlavors());


        if (product.getCategory() != null) {
            productDTO.setCategoryId(product.getCategory().getId());
        }

        return productDTO;
    }

    public List<CadProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CadProductDTO> getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::mapToDTO);
    }

    public List<Product> searchProducts(String productName, Long categoryId) {
        if (categoryId != null && categoryId != 0) {
            return productRepository.findByProductNameContainingIgnoreCaseAndCategoryId(productName, categoryId);
        }
        return productRepository.findByProductNameContainingIgnoreCase(productName);
    }

    public List<Product> searchByCodeBar(String codeBar, Long categoryId) {

        if (categoryId != null && categoryId != 0) {
            return productRepository.findByCodeBarAndCategoryId(codeBar, categoryId);
        }
        return productRepository.findByCodeBar(codeBar);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public void deleteAllProduct() {
        productRepository.deleteAll();
    }


}