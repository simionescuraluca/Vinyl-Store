package com.vinyl;

import com.vinyl.helper.TokenHeaderHelper;
import com.vinyl.model.Product;
import com.vinyl.modelDTO.InventoryListDTO;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.time.LocalDate;

public class GetInventoryTest extends ManagerBaseIntegration {

    @Autowired
    private TokenHeaderHelper tokenHeaderHelper;

    private Product product;

    @Override
    public void setUp() {
        super.setUp();
        product = defaultEntitiesHelper.createProduct();
    }

    @Test
    public void testWhenOk() {
        ResponseEntity<InventoryListDTO> response = setUpHeaderAndGetTheResponse();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getProducts().size()).isEqualTo(1);
    }

    @Test
    public void testWhenTokenIsMissing() {
        ResponseEntity<InventoryListDTO> response = trt.exchange("/products/inventory", HttpMethod.GET, HttpEntity.EMPTY, InventoryListDTO.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testWhenTokenIsInvalid() {
        HttpHeaders headers = tokenHeaderHelper.setupToken("INVALID_TOKEN");
        ResponseEntity<InventoryListDTO> response = trt.exchange("/products/inventory", HttpMethod.GET, new HttpEntity<>(headers), InventoryListDTO.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWhenTokenIsExpired() {
        token.setValidUntil(LocalDate.now().minusMonths(3));
        tokenRepository.save(token);

        ResponseEntity<InventoryListDTO> response = setUpHeaderAndGetTheResponse();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Override
    public ResponseEntity<InventoryListDTO> setUpHeaderAndGetTheResponse() {
        HttpHeaders headers = tokenHeaderHelper.setupToken(token.getHash());
        ResponseEntity<InventoryListDTO> response = trt.exchange("/products/inventory", HttpMethod.GET, new HttpEntity<>(headers), InventoryListDTO.class);

        return response;
    }
}