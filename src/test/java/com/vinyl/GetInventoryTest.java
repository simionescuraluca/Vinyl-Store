package com.vinyl;

import com.vinyl.helper.TokenHeaderHelper;
import com.vinyl.model.Product;
import com.vinyl.modelDTO.InventoryListDTO;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

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

    @Override
    protected String getUrl() {
        return "/products/inventory";
    }

    @Override
    protected HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ResponseEntity<InventoryListDTO> setUpHeaderAndGetTheResponse() {
        HttpHeaders headers = tokenHeaderHelper.setupToken(token.getHash());
        ResponseEntity<InventoryListDTO> response = trt.exchange("/products/inventory", HttpMethod.GET, new HttpEntity<>(headers), InventoryListDTO.class);

        return response;
    }


}