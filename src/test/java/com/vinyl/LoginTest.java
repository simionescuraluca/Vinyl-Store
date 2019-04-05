package com.vinyl;

import com.vinyl.model.Token;
import com.vinyl.modelDTO.EmailPassDTO;
import com.vinyl.modelDTO.TokenDTO;
import com.vinyl.repository.TokenRepository;
import com.vinyl.service.PasswordEncoder;
import com.vinyl.service.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.anyString;

public class LoginTest extends BaseIntegration {

    EmailPassDTO request = new EmailPassDTO();
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ValidatorFactory validatorFactory;
    private PasswordEncoder mockedPasswordEncoder;

    @Override
    public void setUp() {
        super.setUp();
        mockedPasswordEncoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(mockedPasswordEncoder.matches(anyString(), anyString())).thenReturn(true);
        validatorFactory.setPasswordEncoder(mockedPasswordEncoder);
    }

    @Override
    public void tearDown() {
        validatorFactory.setPasswordEncoder(passwordEncoder);
        super.tearDown();
    }

    @Test
    public void loginWithValidEmailAndPasswordWhenTokenExists() {
        Token token = defaultEntitiesHelper.createToken(user);
        setRequest(request);

        ResponseEntity<TokenDTO> tdo = trt.postForEntity("/users/login", request, TokenDTO.class);

        Assertions.assertThat(tdo.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(tdo.getBody().getHash()).isEqualTo(token.getHash());
    }

    @Test
    public void loginWithValidEmailAndPasswordWhenTokenDoesNotExist() {
        setRequest(request);

        ResponseEntity<TokenDTO> tdo = trt.postForEntity("/users/login", request, TokenDTO.class);

        Assertions.assertThat(tdo.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(tokenRepository.findFirstByUserOrderByValidUntilDesc(user)).isNotNull();
    }

    @Test
    public void loginWithInvalidEmail() {
        EmailPassDTO request = new EmailPassDTO();
        request.setEmail("thisisnottheemail@yahoo.com");
        request.setPass(user.getPass());

        ResponseEntity<TokenDTO> tdo = trt.postForEntity("/users/login", request, TokenDTO.class);
        Assertions.assertThat(tdo.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void loginWithInvalidPass() {
        Mockito.when(mockedPasswordEncoder.matches(anyString(), anyString())).thenReturn(false);
        EmailPassDTO request = new EmailPassDTO();
        request.setEmail(user.getEmail());
        request.setPass("thisisnotthepassword");

        ResponseEntity<TokenDTO> tdo = trt.postForEntity("/users/login", request, TokenDTO.class);
        Assertions.assertThat(tdo.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    public void setRequest(EmailPassDTO request) {
        request.setEmail(user.getEmail());
        request.setPass(user.getPass());
    }
}