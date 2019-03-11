package com.vinyl.helper;

import com.vinyl.model.*;
import com.vinyl.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DefaultEntitiesHelper {

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductCartRepository productCartRepository;

    @Autowired
    protected RoleRepository roleRepository;

    @Autowired
    protected AddressRepository addressRepository;

    @Autowired
    protected UserRepository userRepository;

    public void tearDown() {
        productCartRepository.deleteAll();
        productRepository.deleteAll();
        cartRepository.deleteAll();

        tokenRepository.deleteAll();
        userRepository.deleteAll();
        addressRepository.deleteAll();
        roleRepository.deleteAll();

    }

    public Token createToken(User user) {
        Token token = new Token();
        token.setHash("5");
        token.setValidUntil(LocalDate.now().plusMonths(1));
        token.setUser(user);

        return tokenRepository.save(token);
    }

    public Product createProduct(){
        Product product = new Product();
        product.setArtist("Beyonce");
        product.setCategory("Pop Music");
        product.setDescription("Best Seller");
        product.setPrice(100.0);
        product.setProductName("Beyonce Vinyl");
        product.setStock(10);

        return productRepository.save(product);
    }

    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);

        return cartRepository.save(cart);
    }

    public ProductCart createProductCart(Cart cart, Product product) {
        ProductCart productCart = new ProductCart();
        productCart.setNrItems(2);
        productCart.setProduct(product);
        productCart.setCart(cart);
        productCart.setProductPrice(15.25);

        return productCartRepository.save(productCart);
    }
}
