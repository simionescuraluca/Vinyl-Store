package com.vinyl.modelDTO;

import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel(description = "List containing customers details")
public class CustomerListDTO {

    private List<CustomerDTO> users;

    public CustomerListDTO() {
    }

    public CustomerListDTO(List<CustomerDTO> users) {
        this.users = users;
    }

    public List<CustomerDTO> getUsers() {
        return users;
    }

    public void setUsers(List<CustomerDTO> users) {
        this.users = users;
    }
}