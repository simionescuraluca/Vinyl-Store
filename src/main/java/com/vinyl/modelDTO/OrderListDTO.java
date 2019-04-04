package com.vinyl.modelDTO;

import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel(description = "The list of a customer's orders")
public class OrderListDTO {

    private List<OrderDTO> orders;

    public OrderListDTO() {
    }

    public OrderListDTO(List<OrderDTO> orders) {
        this.orders = orders;
    }

    public List<OrderDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDTO> orders) {
        this.orders = orders;
    }
}