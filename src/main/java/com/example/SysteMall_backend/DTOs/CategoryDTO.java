package com.example.SysteMall_backend.DTOs;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {

    private Long id;
    private String categoryName;
    private String categoryDescription;
}
