package com.vinyl.modelDTO;

public class ProductDTO {

    private String name;
    private Integer quantity;
    private Double productPrice;

    public ProductDTO(String name, Integer quantity, Double productPrice) {
        this.name = name;
        this.quantity = quantity;
        this.productPrice = productPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }
}
