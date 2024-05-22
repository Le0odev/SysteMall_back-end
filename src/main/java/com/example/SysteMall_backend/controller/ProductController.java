package com.example.SysteMall_backend.controller;

import com.example.SysteMall_backend.DTOs.CadProductDTO;
import com.example.SysteMall_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<CadProductDTO> createProduct(@RequestBody CadProductDTO productDTO) {
        CadProductDTO newProduct = productService.saveProduct(productDTO);
        return ResponseEntity.ok(newProduct);
    }

    @GetMapping
    public ResponseEntity<List<CadProductDTO>> getAllProducts() {
        List<CadProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CadProductDTO> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


}