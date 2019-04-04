package com.vinyl.modelDTO;

import com.vinyl.model.Status;
import io.swagger.annotations.ApiModel;

import java.time.LocalDate;

@ApiModel(description = "Details about customer's each order")
public class OrderDTO {

    private Double cost;
    private LocalDate dateCreated;
    private Status status;

    public OrderDTO() {
    }

    public OrderDTO(Double cost, LocalDate dateCreated, Status status) {
        this.cost = cost;
        this.dateCreated = dateCreated;
        this.status = status;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}