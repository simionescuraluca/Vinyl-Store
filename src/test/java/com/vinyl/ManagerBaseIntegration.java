package com.vinyl;

import com.vinyl.model.Role;
import com.vinyl.repository.RoleRepository;
import com.vinyl.repository.TokenRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class ManagerBaseIntegration extends LoggedInBaseIntegration {

    @Autowired
    protected TokenRepository tokenRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void setUp() {
        super.setUp();
        Role r = new Role("ADMIN");
        roleRepository.save(r);
        user.setRole(r);
        userRepository.save(user);
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

    protected abstract ResponseEntity<?> setUpHeaderAndGetTheResponse();
}