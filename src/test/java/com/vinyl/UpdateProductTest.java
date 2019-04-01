package com.vinyl;

import com.vinyl.helper.TokenHeaderHelper;
import com.vinyl.model.Product;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

public class UpdateProductTest extends ProductManagementBaseIntegration {

    @Autowired
    private TokenHeaderHelper tokenHeaderHelper;

    private Product product;

    @Override
    public void setUp() {
        super.setUp();
        product = defaultEntitiesHelper.createProduct();
    }

    @Test
    public void testWhenProductIdIsInvalid() {
        ResponseEntity<?> response = setUpHeaderAndGetTheResponse(-1);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> setUpHeaderAndGetTheResponse() {
        return setUpHeaderAndGetTheResponse(product.getId());
    }

    protected ResponseEntity<?> setUpHeaderAndGetTheResponse(Integer productId) {
        HttpHeaders headers = tokenHeaderHelper.setupToken(token.getHash());
        ResponseEntity<?> response = trt.exchange("/products/" + productId, HttpMethod.PUT, getHttpEntity(headers), Void.class);

        return response;
    }

    @Override
    protected String getUrl() {
        return "/products/" + product.getId();
    }

    @Override
    protected HttpMethod getMethod() {
        return HttpMethod.PUT;
    }

    @Override
    protected Product getActualProduct() {
        return productRepository.findById(product.getId()).get();
    }

    @Override
    protected HttpStatus getExpectedStatus() {
        return HttpStatus.OK;
    }

    @Override
    protected HttpEntity getHttpEntity(HttpHeaders headers) {
        return new HttpEntity<>(request, headers);
    }
}