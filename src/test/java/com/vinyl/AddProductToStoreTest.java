package com.vinyl;

import com.vinyl.helper.TokenHeaderHelper;
import com.vinyl.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

public class AddProductToStoreTest extends ProductManagementBaseIntegration {

    @Autowired
    protected TokenHeaderHelper tokenHeaderHelper;

    @Override
    public ResponseEntity<?> setUpHeaderAndGetTheResponse() {
        HttpHeaders headers = tokenHeaderHelper.setupToken(token.getHash());
        ResponseEntity<?> response = trt.exchange("/products", HttpMethod.POST, new HttpEntity<>(request, headers), Void.class);

        return response;
    }

    @Override
    protected HttpStatus getExpectedStatus() {
        return HttpStatus.CREATED;
    }

    @Override
    protected HttpEntity getHttpEntity(HttpHeaders headers) {
        return new HttpEntity(request, headers);
    }

    @Override
    protected Product getActualProduct() {
        return productRepository.findAll().iterator().next();
    }

    @Override
    protected String getUrl() {
        return "/products";
    }

    @Override
    protected HttpMethod getMethod() {
        return HttpMethod.POST;
    }
}