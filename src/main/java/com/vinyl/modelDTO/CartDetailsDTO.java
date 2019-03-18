package com.vinyl.modelDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "All the details about the cart")
public class CartDetailsDTO {

    @ApiModelProperty(notes = "Nr. of vinyls in cart")
    private Integer nrProducts;

    @ApiModelProperty(notes = "Total cost")
    private Double totalCost;

    @ApiModelProperty(notes = "The list of existing vinyls in cart")
    private List<ProductDTO> products;

    public Integer getNrProducts() {
        return nrProducts;
    }

    public void setNrProducts(Integer nrProducts) {
        this.nrProducts = nrProducts;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }
}
