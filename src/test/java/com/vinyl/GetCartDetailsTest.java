package com.vinyl;

import com.vinyl.model.*;
import com.vinyl.modelDTO.CartDetailsDTO;
import com.vinyl.repository.TokenRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.time.LocalDate;

public class GetCartDetailsTest extends BaseTest {

    @Autowired
    TokenRepository tokenRepository;

    @Test
    public void testWhenUserLoggedIn() {
        Token token = defaultentitiesHelper.createToken(user);
        Product product = defaultentitiesHelper.createProduct();
        Cart cart =defaultentitiesHelper.createCart(user);
        ProductCart pc=defaultentitiesHelper.createProductCart(cart, product);
        cart.setProducts(Lists.newArrayList(pc));
        HttpHeaders headers=headerSetup(token);

        ResponseEntity<CartDetailsDTO> cdo = trt.exchange("/users/cart", HttpMethod.GET, new HttpEntity<>(headers), CartDetailsDTO.class);

        Assertions.assertThat(cdo.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testWhenTokenIsMissing() {
        ResponseEntity<CartDetailsDTO> cdo = trt.exchange("/users/cart", HttpMethod.GET, HttpEntity.EMPTY, CartDetailsDTO.class);

        Assertions.assertThat(cdo.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testWhenTokenIsInvalid() {
        Token token = defaultentitiesHelper.createToken(user);
        token.setHash("Bearer 897459");
        tokenRepository.save(token);
        HttpHeaders headers=headerSetup(token);

        ResponseEntity<CartDetailsDTO> cdo = trt.exchange("/users/cart", HttpMethod.GET, new HttpEntity<>(headers), CartDetailsDTO.class);

        Assertions.assertThat(cdo.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testWhenTokenIsExpired() {
        Token token = defaultentitiesHelper.createToken(user);
        token.setValidUntil(LocalDate.now().minusMonths(3));
        tokenRepository.save(token);
        HttpHeaders headers=headerSetup(token);

        ResponseEntity<CartDetailsDTO> cdo = trt.exchange("/users/cart", HttpMethod.GET, new HttpEntity<>(headers), CartDetailsDTO.class);

        Assertions.assertThat(cdo.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testWhenNoItemsInCart() {
        Token token = defaultentitiesHelper.createToken(user);
        HttpHeaders headers=headerSetup(token);

        ResponseEntity<String> cdo = trt.exchange("/users/cart", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        Assertions.assertThat(cdo.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    public HttpHeaders headerSetup(Token token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token.getHash());

        return headers;
    }
}