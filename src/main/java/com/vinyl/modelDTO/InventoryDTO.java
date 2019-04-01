package com.vinyl.modelDTO;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Vinyls details in the inventory list")
public class InventoryDTO {

    private Integer id;
    private String name;
    private Integer stock;

    public InventoryDTO() {
    }

    public InventoryDTO(Integer id, String name, Integer stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
