package com.vinyl;

import com.vinyl.modelDTO.EmailPassDTO;
import com.vinyl.repository.PurchaseRepository;
import com.vinyl.repository.TokenRepository;
import com.vinyl.service.PasswordEncoder;
import com.vinyl.service.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.anyString;

public class DeleteUserTest extends BaseIntegration {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private PasswordEncoder mockedPasswordEncoder;

    @Autowired
    private ValidatorFactory validatorFactory;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    private EmailPassDTO request;

    @Override
    public void setUp() {
        super.setUp();
        mockedPasswordEncoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(mockedPasswordEncoder.matches(anyString(), anyString())).thenReturn(true);
        validatorFactory.setPasswordEncoder(mockedPasswordEncoder);
        tokenRepository.deleteAll();

        request = setRequest(request);
    }

    @Override
    public void tearDown() {
        validatorFactory.setPasswordEncoder(passwordEncoder);
        super.tearDown();
    }

    @Test
    public void testWhenOk() {
        ResponseEntity<?> response = trt.exchange("/users", HttpMethod.DELETE, new HttpEntity<>(request), Void.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(userRepository.findByEmail(request.getEmail())).isEmpty();
        Assertions.assertThat(purchaseRepository.findByUser(user)).isEmpty();
    }

    @Test
    public void testWhenPassIsInvalid() {
        Mockito.when(mockedPasswordEncoder.matches(anyString(), anyString())).thenReturn(false);

        request.setPass("invalid_pass");
        ResponseEntity<?> response = trt.exchange("/users", HttpMethod.DELETE, new HttpEntity<>(request), Void.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(userRepository.findByEmail(request.getEmail())).isNotEmpty();
    }

    @Test
    public void testWhenUserEmailIsInvalid() {
        request.setEmail("thisisnottheemail@yahoo.com");
        ResponseEntity<?> response = trt.exchange("/users", HttpMethod.DELETE, new HttpEntity<>(request), Void.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(userRepository.findByEmail(user.getEmail())).isNotEmpty();
    }

    public EmailPassDTO setRequest(EmailPassDTO request) {
        request = new EmailPassDTO();
        request.setEmail(user.getEmail());
        request.setPass(user.getPass());
        return request;
    }
}