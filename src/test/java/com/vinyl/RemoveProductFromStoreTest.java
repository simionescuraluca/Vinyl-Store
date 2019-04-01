package com.vinyl;

import com.vinyl.helper.TokenHeaderHelper;
import com.vinyl.model.Product;
import com.vinyl.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.Optional;


public class RemoveProductFromStoreTest extends ManagerBaseIntegration {

    @Autowired
    private TokenHeaderHelper tokenHeaderHelper;

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @Override
    public void setUp() {
        super.setUp();
        product = defaultEntitiesHelper.createProduct();
    }

    @Test
    public void testWhenOK() {
        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(productRepository.findById(product.getId())).isEqualTo(Optional.empty());
    }

    @Test
    public void testWhenProductIdIsInvalid() {
        Integer invalidId = 00;
        product.setId(invalidId);
        productRepository.save(product);

        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(productRepository.findById(product.getId())).isNotNull();

    }

    @Test
    public void testWhenTokenIsMissing() {
        ResponseEntity<?> response = trt.exchange("/products/" + product.getId(), HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testWhenTokenIsInvalid() {
        HttpHeaders headers = tokenHeaderHelper.setupToken("INVALID_TOKEN");
        ResponseEntity<?> response = trt.exchange("/products/" + product.getId(), HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWhenTokenIsExpired() {
        token.setValidUntil(LocalDate.now().minusMonths(3));
        tokenRepository.save(token);

        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Override
    public ResponseEntity<?> setUpHeaderAndGetTheResponse() {
        ResponseEntity<?> response = setUpHeaderAndGetTheResponse(product.getId());
        return response;
    }

    public ResponseEntity<?> setUpHeaderAndGetTheResponse(Integer id){
        HttpHeaders headers = tokenHeaderHelper.setupToken(token.getHash());
        ResponseEntity<?> response = trt.exchange("/products/" + id, HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);

        return response;
    }
}