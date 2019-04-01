package com.vinyl;

import com.vinyl.model.Product;
import com.vinyl.modelDTO.ProductManagementDTO;
import com.vinyl.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class ProductManagementBaseIntegration extends ManagerBaseIntegration {

    protected ProductManagementDTO request;

    @Autowired
    protected ProductRepository productRepository;

    @Override
    public void setUp() {
        super.setUp();
        setUpRequest();
    }

    protected void setUpRequest() {
        request = new ProductManagementDTO();
        request.setStock(30);
        request.setProductName("Test Product");
        request.setDescription("Best Test Product");
        request.setCategory("Test");
        request.setArtist("Raluca Simionescu");
        request.setPrice(150.25);
    }

    @Test
    public void testWhenOK() {
        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();
        assertOnProduct(getActualProduct());
        Assertions.assertThat(response.getStatusCode()).isEqualTo(getExpectedStatus());
    }

    protected abstract HttpStatus getExpectedStatus();

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

    protected void assertOnProduct(Product p) {
        Assertions.assertThat(p.getProductName()).isEqualTo(request.getProductName());
        Assertions.assertThat(p.getStock()).isEqualTo(request.getStock());
        Assertions.assertThat(p.getPrice()).isEqualTo(request.getPrice());
    }

    protected abstract Product getActualProduct();
}
