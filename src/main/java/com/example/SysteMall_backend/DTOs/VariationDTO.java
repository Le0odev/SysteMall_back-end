package com.example.SysteMall_backend.DTOs;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariationDTO {

    private Long id;
    private String flavor;
    private String codeBar;
    private int productQuantity;
    private double estoquePeso;

    // Construtores
    public VariationDTO() {
    }

    public VariationDTO(Long id, String flavor, String codeBar, int productQuantity, double estoquePeso) {
        this.id = id;
        this.flavor = flavor;
        this.codeBar = codeBar;
        this.productQuantity = productQuantity;
        this.estoquePeso = estoquePeso;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public String getCodeBar() {
        return codeBar;
    }

    public void setCodeBar(String codeBar) {
        this.codeBar = codeBar;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public double getEstoquePeso() {
        return estoquePeso;
    }

    public void setEstoquePeso(double estoquePeso) {
        this.estoquePeso = estoquePeso;
    }

    @Override
    public String toString() {
        return "VariationDTO{" +
                "id=" + id +
                ", flavor='" + flavor + '\'' +
                ", codeBar='" + codeBar + '\'' +
                ", productQuantity=" + productQuantity +
                ", estoquePeso=" + estoquePeso +
                '}';
    }
}
