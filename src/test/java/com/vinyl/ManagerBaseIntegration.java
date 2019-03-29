package com.vinyl;

import com.vinyl.model.Role;
import com.vinyl.modelDTO.ProductManagementDTO;
import com.vinyl.repository.RoleRepository;
import com.vinyl.repository.TokenRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class ManagerBaseIntegration extends BaseIntegration {

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    protected RoleRepository roleRepository;

    protected ProductManagementDTO request;

    @Override
    public void setUp() {
        super.setUp();
        Role r = new Role("ADMIN");
        roleRepository.save(r);
        user.setRole(r);
        userRepository.save(user);

        setUpRequest();
    }

    @Test
    public void testWhenUserIsNotAdmin() {
        Role basicRole = new Role("BASIC_USER");
        roleRepository.save(basicRole);
        user.setRole(basicRole);
        userRepository.save(user);

        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    public void setUpRequest() {
        request = new ProductManagementDTO();
        request.setStock(30);
        request.setProductName("Test Product");
        request.setDescription("Best Test Product");
        request.setCategory("Test");
        request.setArtist("Raluca Simionescu");
        request.setPrice(150.25);
    }

    public abstract ResponseEntity<?> setUpHeaderAndGetTheResponse();
}