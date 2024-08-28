package com.example.SysteMall_backend.service;

import com.example.SysteMall_backend.DTOs.CategoryDTO;
import com.example.SysteMall_backend.entity.Category;
import com.example.SysteMall_backend.exception.CustomException;
import com.example.SysteMall_backend.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setCategoryName(categoryDTO.getCategoryName());
        category.setCategoryDescription(categoryDTO.getCategoryDescription());
        Category savedCategory = categoryRepository.save(category);
        return mapToDTO(savedCategory);
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Page<CategoryDTO> getAllCategories(Pageable pageable) {
        return categoryRepository.findByCategoryNameContainingIgnoreCase("", pageable)
                .map(this::mapToDTO);
    }


    public Optional<CategoryDTO> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(this::mapToDTO);
    }

    public List<Category> searchCategory(String categoryName, Long id) {
        if (id != null && id != 0) {
            return categoryRepository.findByCategoryNameContainingIgnoreCaseAndId(categoryName, id);
        }
        return categoryRepository.findByCategoryNameContainingIgnoreCase(categoryName);
    }

    public CategoryDTO updateCategory(Long id, CategoryDTO updatedCategoryDTO) {
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setCategoryName(updatedCategoryDTO.getCategoryName());
                    category.setCategoryDescription(updatedCategoryDTO.getCategoryDescription());
                    return mapToDTO(categoryRepository.save(category));
                })
                .orElseThrow(() -> new CustomException("Categoria não encontrada"));
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public void deleteAllCategory() {categoryRepository.deleteAll();}


    private CategoryDTO mapToDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setCategoryName(category.getCategoryName());
        categoryDTO.setCategoryDescription(category.getCategoryDescription());
        return categoryDTO;
    }
}
