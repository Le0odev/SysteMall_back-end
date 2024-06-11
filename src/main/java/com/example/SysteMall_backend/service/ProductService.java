package com.example.SysteMall_backend.service;

import com.example.SysteMall_backend.DTOs.CadProductDTO;

import com.example.SysteMall_backend.entity.Category;
import com.example.SysteMall_backend.entity.Product;
import com.example.SysteMall_backend.repository.CategoryRepository;
import com.example.SysteMall_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

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
        product.setProductQuantity(productDTO.getProductQuantity());
        product.setCodeBar(productDTO.getCodeBar());

        if (productDTO.getCategoryId() != null) {
            Optional<Category> category = categoryRepository.findById(productDTO.getCategoryId());
            category.ifPresent(product::setCategory);
        }

        Product savedProduct = productRepository.save(product);
        return mapToDTO(savedProduct);
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
        if (categoryId != null && categoryId != 0){
            return productRepository.findByProductNameContainingIgnoreCaseAndCategoryId(productName, categoryId);
        }
        return productRepository.findByProductNameContainingIgnoreCase(productName);
    }

    public CadProductDTO updateProduct(Long id, CadProductDTO updatedProductDTO) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setProductName(updatedProductDTO.getProductName());
                    product.setProductDescription(updatedProductDTO.getProductDescription());
                    product.setProductPrice(updatedProductDTO.getProductPrice());
                    product.setProductQuantity(updatedProductDTO.getProductQuantity());
                    product.setCodeBar(updatedProductDTO.getCodeBar());

                    if (updatedProductDTO.getCategoryId() != null) {
                        Optional<Category> category = categoryRepository.findById(updatedProductDTO.getCategoryId());
                        category.ifPresent(product::setCategory);
                    }

                    return mapToDTO(productRepository.save(product));
                })
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }


    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public void deleteAllProduct() {productRepository.deleteAll();}

    private CadProductDTO mapToDTO(Product product) {
        CadProductDTO productDTO = new CadProductDTO();
        productDTO.setId(product.getId());
        productDTO.setProductName(product.getProductName());
        productDTO.setProductDescription(product.getProductDescription());
        productDTO.setProductPrice(product.getProductPrice());
        productDTO.setProductQuantity(product.getProductQuantity());
        productDTO.setCodeBar(product.getCodeBar());
        if (product.getCategory() != null) {
            productDTO.setCategoryId(product.getCategory().getId());
        }
        return productDTO;
    }
}