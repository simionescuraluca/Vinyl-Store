package com.vinyl;

import com.vinyl.helper.DefaultEntitiesHelper;
import com.vinyl.helper.TokenHeaderHelper;
import com.vinyl.model.Cart;
import com.vinyl.model.Product;
import com.vinyl.model.ProductCart;
import com.vinyl.modelDTO.CartDetailsDTO;
import com.vinyl.repository.CartRepository;
import com.vinyl.repository.ProductCartRepository;
import com.vinyl.repository.TokenRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

public class GetCartDetailsTest extends LoggedInBaseIntegration {

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    DefaultEntitiesHelper defaultEntitiesHelper;

    @Autowired
    TokenHeaderHelper tokenHeaderHelper;

    @Autowired
    ProductCartRepository productCartRepository;

    @Autowired
    CartRepository cartRepository;

    @Test
    public void testWhenUserLoggedIn() {
        Product product = defaultEntitiesHelper.createProduct();
        Cart cart = defaultEntitiesHelper.createCart(user);
        ProductCart pc = defaultEntitiesHelper.createProductCart(cart, product);
        cart.setProducts(Lists.newArrayList(pc));
        cartRepository.save(cart);

        ResponseEntity<CartDetailsDTO> cdo = setUpHeaderAndGetTheResponse();

        Assertions.assertThat(cdo.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(cdo.getBody().getNrProducts()).isEqualTo(productCartRepository.findByCart(cart).size());
        Assertions.assertThat(cdo.getBody().getProducts().get(0).getName()).isEqualTo(pc.getProduct().getProductName());
        Assertions.assertThat(cdo.getBody().getTotalCost()).isEqualTo(productCartRepository.findByProductAndCart(product, cart).getProductPrice() * pc.getNrItems());
    }

    @Test
    public void testWhenNoItemsInCart() {
        HttpHeaders headers = tokenHeaderHelper.setupToken(token.getHash());
        ResponseEntity<String> cdo = trt.exchange(getUrl(), getMethod(), new HttpEntity<>(headers), String.class);

        Assertions.assertThat(cdo.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(cdo.getBody()).isEqualTo("No items in cart!");
    }

    private ResponseEntity<CartDetailsDTO> setUpHeaderAndGetTheResponse() {
        HttpHeaders headers = tokenHeaderHelper.setupToken(token.getHash());
        ResponseEntity<CartDetailsDTO> cdo = trt.exchange(getUrl(), getMethod(), new HttpEntity<>(headers), CartDetailsDTO.class);

        return cdo;
    }

    @Override
    String getUrl() {
        return "/users/cart";
    }

    @Override
    HttpMethod getMethod(){
        return HttpMethod.GET;
    }
}