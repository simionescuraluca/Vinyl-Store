package com.vinyl;

import com.vinyl.helper.TokenHeaderHelper;
import com.vinyl.model.Product;
import com.vinyl.model.Purchase;
import com.vinyl.model.PurchaseProduct;
import com.vinyl.modelDTO.StatusDTO;
import com.vinyl.repository.PurchaseRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

public class ChangeOrderStatusTest extends ManagerBaseIntegration {

    @Autowired
    private TokenHeaderHelper tokenHeaderHelper;

    @Autowired
    private PurchaseRepository purchaseRepository;

    private Purchase order;

    private Product product;

    private StatusDTO status;

    @Override
    public void setUp() {
        super.setUp();
        product = defaultEntitiesHelper.createProduct();
        order = purchaseSetup();
        status = new StatusDTO();
        status.setStatus("SENT");
    }

    @Test
    public void testWhenOk() {
        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testWhenStatusIsInvalid(){
        status.setStatus("INVALID_STATUS");
        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(purchaseRepository.findById(order.getId()).get().getStatus()).isNotEqualTo("SENT");
    }

    @Test
    public void testWhenOrderIdIsInvalid(){
        ResponseEntity<?> response = setUpHeaderAndGetTheResponse("/orders/" + 00);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    protected ResponseEntity<?> setUpHeaderAndGetTheResponse() {
        return setUpHeaderAndGetTheResponse(getUrl());
    }

    private ResponseEntity<?> setUpHeaderAndGetTheResponse(String url) {
        HttpHeaders headers = tokenHeaderHelper.setupToken(token.getHash());
        ResponseEntity<?> response = trt.exchange(url, getMethod(), getHttpEntity(headers), Void.class);

        return response;
    }

    private Purchase purchaseSetup() {
        Purchase purchase = defaultEntitiesHelper.createPurchase(user);
        PurchaseProduct pp = defaultEntitiesHelper.createPurchaseProduct(purchase, product);
        purchase.setProducts(Lists.newArrayList(pp));

        return purchaseRepository.save(purchase);
    }

    @Override
    protected String getUrl() {
        return "/orders/" + order.getId();
    }

    @Override
    protected HttpEntity getHttpEntity(HttpHeaders headers) {
        return new HttpEntity<>(status, headers);
    }

    @Override
    protected HttpMethod getMethod() {
        return HttpMethod.PUT;
    }
}