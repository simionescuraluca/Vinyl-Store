package com.vinyl.modelDTO;

import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel(description = "List containing customers details")
public class GetUserListDTO {

    private List<GetUserDTO> users;

    public GetUserListDTO() {
    }

    public GetUserListDTO(List<GetUserDTO> users) {
        this.users = users;
    }

    public List<GetUserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<GetUserDTO> users) {
        this.users = users;
    }
}