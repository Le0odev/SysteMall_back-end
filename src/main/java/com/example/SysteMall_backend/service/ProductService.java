package com.example.SysteMall_backend.service;

import com.example.SysteMall_backend.DTOs.CadProductDTO;
import com.example.SysteMall_backend.DTOs.VariationDTO;
import com.example.SysteMall_backend.entity.Category;
import com.example.SysteMall_backend.entity.Product;
import com.example.SysteMall_backend.entity.ProductVariation;
import com.example.SysteMall_backend.exception.CustomException;
import com.example.SysteMall_backend.repository.CategoryRepository;
import com.example.SysteMall_backend.repository.ProductRepository;
import com.example.SysteMall_backend.repository.VariationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final VariationRepository variationRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, VariationRepository variationRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.variationRepository = variationRepository;
    }

    public CadProductDTO saveProduct(CadProductDTO productDTO) {
        Product product = new Product();
        product.setProductName(productDTO.getProductName());
        product.setProductDescription(productDTO.getProductDescription());
        product.setProductPrice(productDTO.getProductPrice());
        product.setBulk(productDTO.isBulk());
        product.setImageUrl(productDTO.getImageUrl());

        if (productDTO.getCategoryId() != null) {
            Optional<Category> category = categoryRepository.findById(productDTO.getCategoryId());
            category.ifPresent(product::setCategory);
        }

        // Salvar produto
        Product savedProduct = productRepository.save(product);

        // Salvar variações (sabores, código de barras, etc.)
        if (productDTO.getVariations() != null) {
            for (var variationDTO : productDTO.getVariations()) {
                ProductVariation variation = new ProductVariation();
                variation.setProduct(savedProduct);
                variation.setFlavor(variationDTO.getFlavor());
                variation.setCodeBar(variationDTO.getCodeBar());
                variation.setProductQuantity(variationDTO.getProductQuantity());
                variation.setEstoquePeso(variationDTO.getEstoquePeso());

                variationRepository.save(variation);
            }
        }

        return mapToDTO(savedProduct);
    }

    public CadProductDTO updateProduct(Long id, CadProductDTO updatedProductDTO) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setProductName(updatedProductDTO.getProductName());
                    product.setProductDescription(updatedProductDTO.getProductDescription());
                    product.setProductPrice(updatedProductDTO.getProductPrice());
                    product.setBulk(updatedProductDTO.isBulk());
                    product.setImageUrl(updatedProductDTO.getImageUrl());

                    if (updatedProductDTO.getCategoryId() != null) {
                        Optional<Category> category = categoryRepository.findById(updatedProductDTO.getCategoryId());
                        category.ifPresent(product::setCategory);
                    }

                    // Atualizar produto
                    Product savedProduct = productRepository.save(product);

                    // Atualizar variações (sabores, código de barras, etc.)
                    if (updatedProductDTO.getVariations() != null) {
                        // Primeiro, remover as variações antigas (orphanRemoval = true)
                        variationRepository.deleteByProductId(product.getId());

                        // Agora, adicionar as novas variações
                        for (var variationDTO : updatedProductDTO.getVariations()) {
                            ProductVariation variation = new ProductVariation();
                            variation.setProduct(savedProduct);
                            variation.setFlavor(variationDTO.getFlavor());
                            variation.setCodeBar(variationDTO.getCodeBar());
                            variation.setProductQuantity(variationDTO.getProductQuantity());
                            variation.setEstoquePeso(variationDTO.getEstoquePeso());

                            variationRepository.save(variation);
                        }
                    }

                    return mapToDTO(savedProduct);
                })
                .orElseThrow(() -> new CustomException("Produto não encontrado"));
    }




    private CadProductDTO mapToDTO(Product product) {
        CadProductDTO productDTO = new CadProductDTO();
        productDTO.setId(product.getId());
        productDTO.setProductName(product.getProductName());
        productDTO.setProductDescription(product.getProductDescription());
        productDTO.setProductPrice(product.getProductPrice());
        productDTO.setBulk(product.isBulk());
        productDTO.setImageUrl(product.getImageUrl());

        if (product.getCategory() != null) {
            productDTO.setCategoryId(product.getCategory().getId());
        }

        // Mapear as variações para o DTO
        List<VariationDTO> variationDTOs = product.getVariations().stream().map(variation -> {
            VariationDTO variationDTO = new VariationDTO();
            variationDTO.setId(variation.getId());
            variationDTO.setFlavor(variation.getFlavor());
            variationDTO.setCodeBar(variation.getCodeBar());
            variationDTO.setProductQuantity(variation.getProductQuantity());
            variationDTO.setEstoquePeso(variation.getEstoquePeso());
            return variationDTO;
        }).collect(Collectors.toList());

        productDTO.setVariations(variationDTOs);

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
