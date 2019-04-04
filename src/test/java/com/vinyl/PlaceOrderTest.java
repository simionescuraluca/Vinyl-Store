package com.vinyl;

import com.vinyl.model.Cart;
import com.vinyl.model.Product;
import com.vinyl.model.ProductCart;
import com.vinyl.model.Purchase;
import com.vinyl.repository.*;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.util.List;

public class PlaceOrderTest extends LoggedInBaseIntegration {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ProductCartRepository productCartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseProductRepository purchaseProductRepository;

    private Cart cart;
    private Product product;

    @Override
    public void setUp() {
        super.setUp();
        product = defaultEntitiesHelper.createProduct();
        cart = cartSetup();
    }

    @Test
    public void testWhenOk() {
        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Purchase> purchase = purchaseRepository.findByUser(user);
        Assertions.assertThat(purchase).isNotNull();
        Assertions.assertThat(purchaseProductRepository.findByPurchase(purchase.get(0)).getProduct().getProductName()).isEqualTo(product.getProductName());
    }

    @Test
    public void testWhenCartIsEmpty() {
        deleteCart();

        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWhenStockIsTooSmall() {
        product.setStock(0);
        productRepository.save(product);

        ResponseEntity<?> response = setUpHeaderAndGetTheResponse();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(purchaseRepository.findByUser(user)).isEmpty();
    }

    private ResponseEntity setUpHeaderAndGetTheResponse() {
        HttpHeaders headers = tokenHeaderHelper.setupToken(token.getHash());
        ResponseEntity<?> response = trt.exchange(getUrl(), getMethod(), new HttpEntity<>(headers), Void.class);

        return response;
    }

    private Cart cartSetup() {
        Cart cart = defaultEntitiesHelper.createCart(user);
        ProductCart pc = defaultEntitiesHelper.createProductCart(cart, product);
        cart.setProducts(Lists.newArrayList(pc));

        return cartRepository.save(cart);
    }

    private void deleteCart() {
        productCartRepository.deleteAll();
        cartRepository.deleteAll();
    }

    @Override
    protected String getUrl() {
        return "/users/orders";
    }

    @Override
    protected HttpMethod getMethod() {
        return HttpMethod.PUT;
    }
}