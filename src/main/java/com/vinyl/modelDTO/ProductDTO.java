package com.vinyl.modelDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Information about the vinyls in cart")
public class ProductDTO {

    @ApiModelProperty(notes = "Vinyl Name")
    private String name;

    @ApiModelProperty(notes = "Nr. of vinyls")
    private Integer quantity;

    @ApiModelProperty(notes = "Vinyl Price")
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
