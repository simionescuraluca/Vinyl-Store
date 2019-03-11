package com.vinyl;

import com.vinyl.helper.DefaultEntitiesHelper;
import com.vinyl.helper.TokenHeaderHelper;
import com.vinyl.model.*;
import com.vinyl.modelDTO.CartDetailsDTO;
import com.vinyl.repository.TokenRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.time.LocalDate;

public class GetCartDetailsTest extends BaseIntegration {

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    DefaultEntitiesHelper defaultEntitiesHelper;

    @Autowired
    TokenHeaderHelper tokenHeaderHelper;

    public ResponseEntity<?> setUpHeaderAndGetTheResponse(Token token){
        HttpHeaders headers=tokenHeaderHelper.setupToken(token.getHash());
        ResponseEntity<CartDetailsDTO> cdo = trt.exchange("/users/cart", HttpMethod.GET, new HttpEntity<>(headers), CartDetailsDTO.class);

        return cdo;
    }

    @Test
    public void testWhenUserLoggedIn() {
        Token token = defaultEntitiesHelper.createToken(user);
        Product product = defaultEntitiesHelper.createProduct();
        Cart cart =defaultEntitiesHelper.createCart(user);
        ProductCart pc=defaultEntitiesHelper.createProductCart(cart, product);
        cart.setProducts(Lists.newArrayList(pc));

        ResponseEntity<?> cdo=setUpHeaderAndGetTheResponse(token);

        Assertions.assertThat(cdo.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testWhenTokenIsMissing() {
        ResponseEntity<CartDetailsDTO> cdo = trt.exchange("/users/cart", HttpMethod.GET, HttpEntity.EMPTY, CartDetailsDTO.class);
        Assertions.assertThat(cdo.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testWhenTokenIsInvalid() {
        HttpHeaders headers=tokenHeaderHelper.setupToken("INVALID TOKEN");
        ResponseEntity<CartDetailsDTO> cdo = trt.exchange("/users/cart", HttpMethod.GET, new HttpEntity<>(headers), CartDetailsDTO.class);
        Assertions.assertThat(cdo.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWhenTokenIsExpired() {
        Token token = defaultEntitiesHelper.createToken(user);
        token.setValidUntil(LocalDate.now().minusMonths(3));
        tokenRepository.save(token);

        ResponseEntity<?> cdo=setUpHeaderAndGetTheResponse(token);

        Assertions.assertThat(cdo.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testWhenNoItemsInCart() {
        Token token = defaultEntitiesHelper.createToken(user);

        HttpHeaders headers=tokenHeaderHelper.setupToken(token.getHash());
        ResponseEntity<String> cdo = trt.exchange("/users/cart", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        Assertions.assertThat(cdo.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}