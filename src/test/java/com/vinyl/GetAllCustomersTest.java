package com.vinyl;


import com.vinyl.modelDTO.CustomerListDTO;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.OK;

public class GetAllCustomersTest extends ManagerBaseIntegration {

    @Test
    public void testWhenOk() {
        ResponseEntity<CustomerListDTO> response = setUpHeaderAndGetTheResponse();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getBody().getUsers().size()).isEqualTo(1);
    }

    @Override
    public ResponseEntity<CustomerListDTO> setUpHeaderAndGetTheResponse() {
        HttpHeaders headers = tokenHeaderHelper.setupToken(token.getHash());
        ResponseEntity<CustomerListDTO> response = trt.exchange("/users", HttpMethod.GET, new HttpEntity<>(headers), CustomerListDTO.class);

        return response;
    }

    @Override
    protected String getUrl() {
        return "/users";
    }

    @Override
    protected HttpMethod getMethod() {
        return HttpMethod.GET;
    }
}