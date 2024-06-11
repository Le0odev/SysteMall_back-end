package com.example.SysteMall_backend.controller;

import com.example.SysteMall_backend.DTOs.CadProductDTO;
import com.example.SysteMall_backend.entity.Product;
import com.example.SysteMall_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:5173") // Adicione o domínio do frontend
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<Void> deleteAll() {
        productService.deleteAllProduct();
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<CadProductDTO> updateProduct(@PathVariable Long id, @RequestBody CadProductDTO productDTO) {
        try {
            CadProductDTO updatedProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String productName, @RequestParam(required = false) Long categoryId) {
        return productService.searchProducts(productName, categoryId);
    }




}