package com.example.SysteMall_backend.controller;


import com.example.SysteMall_backend.DTOs.CadProductDTO;
import com.example.SysteMall_backend.DTOs.CategoryDTO;
import com.example.SysteMall_backend.entity.Category;
import com.example.SysteMall_backend.entity.Product;
import com.example.SysteMall_backend.service.CategoryService;
import com.example.SysteMall_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
@CrossOrigin(origins = {"http://localhost:5173", "http://10.0.0.107:5173", "http://10.0.0.108:5173"})

public class PublicCatalogController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public PublicCatalogController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/catalog")
    public ResponseEntity<List<CadProductDTO>> getAllProducts() {
        List<CadProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/catalog/search")
    public List<Product> searchProducts(@RequestParam String productName, @RequestParam(required = false) Long categoryId) {
        return productService.searchProducts(productName, categoryId);
    }

    @GetMapping("/catalog/categories")

    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/categories/search")
    public List<Category> searchCategory(@RequestParam String categoryName, @RequestParam(required = false) Long id) {
        return categoryService.searchCategory(categoryName, id);
    }

    @GetMapping("/products/all")
    public ResponseEntity<List<CadProductDTO>> AllProducts() {
        List<CadProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }


}





