package com.vinyl.service;

import com.vinyl.model.*;
import com.vinyl.modelDTO.AddProductToCartDTO;
import com.vinyl.repository.CartRepository;
import com.vinyl.repository.ProductCartRepository;
import com.vinyl.repository.ProductRepository;
import com.vinyl.repository.TokenRepository;
import com.vinyl.service.exception.BadRequestException;
import com.vinyl.service.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("productService")
public class ProductService {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductCartRepository productCartRepository;

    @Autowired
    private ValidatorFactory validatorFactory;

    public void addProductToCart(AddProductToCartDTO addProductToCartDTO, Integer productId, String tokenHash) {

        validatorFactory.getTokenValidator().validate(tokenHash);

        Product product = productRepository.findById(productId).orElseThrow(() -> new BadRequestException("Product not found!"));

        Integer quantity = addProductToCartDTO.getQuantity();
        if (quantity == null || quantity <= 0 || (quantity > product.getStock())) {
            throw new BadRequestException("The quantity is not valid!");
        }

        Token token = tokenRepository.findByHash(tokenHash);
        Cart cart = findOrCreateCart(token.getUser());
        ProductCart productCart = createOrUpdateProductCart(product, cart, addProductToCartDTO.getQuantity());

        productCartRepository.save(productCart);
    }

    private Cart findOrCreateCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId());
        if (cart == null) {
            cart = new Cart(user);
        }
        return cartRepository.save(cart);
    }

    private ProductCart createOrUpdateProductCart(Product product, Cart cart, Integer nrOfItems) {
        ProductCart productCart = productCartRepository.findByProductAndCart(product, cart);

        if (productCart == null) {
            productCart = new ProductCart(product, cart);
            productCart.setNrItems(nrOfItems);
        } else {
            productCart.setNrItems(nrOfItems + productCart.getNrItems());
        }
        productCart.setProductPrice(product.getPrice());

        return productCartRepository.save(productCart);
    }
}