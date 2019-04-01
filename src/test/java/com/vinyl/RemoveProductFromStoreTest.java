package com.vinyl;

import com.vinyl.helper.TokenHeaderHelper;
import com.vinyl.model.Product;
import com.vinyl.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

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

    @Override
    protected String getUrl() {
        return "/products/" + product.getId();
    }

    @Override
    protected HttpMethod getMethod() {
        return HttpMethod.DELETE;
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