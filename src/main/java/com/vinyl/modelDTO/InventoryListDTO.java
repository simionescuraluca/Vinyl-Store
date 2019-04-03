package com.vinyl.modelDTO;

import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel(description = "The list containing the products in the inventory")
public class InventoryListDTO {

    private List<InventoryDTO> products;

    public InventoryListDTO() {
    }

    public InventoryListDTO(List<InventoryDTO> products) {
        this.products = products;
    }

    public List<InventoryDTO> getProducts() {
        return products;
    }

    public void setProducts(List<InventoryDTO> products) {
        this.products = products;
    }
}
