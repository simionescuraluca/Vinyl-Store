package com.vinyl;

import com.vinyl.helper.TokenHeaderHelper;
import com.vinyl.model.Product;
import com.vinyl.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.time.LocalDate;

public class UpdateProductTest extends ManagerBaseIntegration {

    @Autowired
    protected TokenHeaderHelper tokenHeaderHelper;

    @Autowired
    protected ProductRepository productRepository;

    protected Product product;

    @Override
    public void setUp() {
        super.setUp();
        product = defaultEntitiesHelper.createProduct();
    }

    @Test
    public void testWhenOK() {
        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Product p = productRepository.findById(product.getId()).get();
        Assertions.assertThat(p.getProductName()).isEqualTo(request.getProductName());
        Assertions.assertThat(p.getStock()).isEqualTo(request.getStock());
        Assertions.assertThat(p.getPrice()).isEqualTo(request.getPrice());
    }

    @Test
    public void testWhenProductNameIsInvalid() {
        request.setProductName("");
        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWhenProductPriceIsInvalid() {
        request.setPrice(-1.2);
        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWhenProductStockIsInvalid() {
        request.setStock(-2);
        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWhenProductIdIsInvalid() {
        Integer invalidId = 00;
        product.setId(invalidId);
        productRepository.save(product);

        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWhenTokenIsMissing() {
        ResponseEntity<?> response = trt.exchange("/products/" + product.getId(), HttpMethod.PUT, new HttpEntity<>(request), Void.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testWhenTokenIsInvalid() {
        HttpHeaders headers = tokenHeaderHelper.setupToken("INVALID_TOKEN");
        ResponseEntity<?> response = trt.exchange("/products/" + product.getId(), HttpMethod.PUT, new HttpEntity<>(request, headers), Void.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWhenTokenIsExpired() {
        token.setValidUntil(LocalDate.now().minusMonths(3));
        tokenRepository.save(token);

        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<?> setUpHeaderAndGetTheResponse() {
        HttpHeaders headers = tokenHeaderHelper.setupToken(token.getHash());
        ResponseEntity<?> response = trt.exchange("/products/" + product.getId(), HttpMethod.PUT, new HttpEntity<>(request, headers), Void.class);

        return response;
    }
}