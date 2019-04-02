package com.vinyl.service;

import com.vinyl.model.*;
import com.vinyl.modelDTO.*;
import com.vinyl.repository.*;
import com.vinyl.service.exception.BadRequestException;
import com.vinyl.service.exception.UnauthorizedException;
import com.vinyl.service.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("userService")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ValidatorFactory validatorFactory;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private ProductCartRepository productCartRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseProductRepository purchaseProductRepository;

    public User addUser(User user) {

        Role r = new Role("BASIC_USER");
        r = roleRepository.save(r);
        user.setRole(r);

        Address defaultAddress = addressRepository.findByCountryAndCityAndStreetAndNumber("Romania", "Iasi",
                "Strada Palat", 1);
        user.setAddress(defaultAddress);

        validatorFactory.getUserNameValidator().validate(user);
        validatorFactory.getUserEmailValidator().validate(user);
        validatorFactory.getUserPasswordValidator().validate(user);

        user.setPass(passwordEncoder.encode(user.getPass()));
        return userRepository.save(user);
    }

    public void deleteUser(EmailPassDTO credentials) {

        validatorFactory.getEmailAndPasswordValidator().validate(credentials);
        Optional<User> user = userRepository.findByEmail(credentials.getEmail());
        user.ifPresent(userRepository::delete);
    }

    public Token loginUser(EmailPassDTO info) {

        validatorFactory.getEmailAndPasswordValidator().validate(info);

        User user = userRepository.findByEmail(info.getEmail()).get();
        Token token = tokenRepository.findFirstByUserOrderByValidUntilDesc(user);

        if (token == null) {
            token = new Token();
            token.setUser(userRepository.findByEmail(info.getEmail()).get());
            token.setHash(String.valueOf(Math.abs(info.hashCode())));

            LocalDate tokenCreateDate = LocalDate.now();
            LocalDate tokenValidUntil = tokenCreateDate.plusMonths(1);
            token.setValidUntil(tokenValidUntil);
        } else {
            if (LocalDate.now().compareTo(token.getValidUntil()) < 0) {
                return token;
            }
        }

        return tokenRepository.save(token);
    }

    public CartDetailsDTO getCartDetails(String tokenHash) {

        Token token = getToken(tokenHash);
        User user = token.getUser();

        Cart cart = cartRepository.findByUser(user);
        List<ProductCart> productCartList = productCartRepository.findByCart(cart);
        List<ProductDTO> productDetails = new ArrayList<>();
        double cost = 0.0;
        for (ProductCart product : productCartList) {
            String name = product.getProduct().getProductName();
            productDetails.add(new ProductDTO(name, product.getNrItems(), product.getProductPrice()));

            cost = cost + (product.getProductPrice() * product.getNrItems());
        }
        CartDetailsDTO cartDetails = new CartDetailsDTO();
        cartDetails.setNrProducts(productDetails.size());
        cartDetails.setProducts(productDetails);
        cartDetails.setTotalCost(cost);

        return cartDetails;
    }

    public void deleteProductFromCart(String tokenHash, Integer productId, Integer userId) {

        Token token = getToken(tokenHash);
        User user = token.getUser();
        if (userId != user.getId()) {
            throw new UnauthorizedException("You cannot access the cart of another user!");
        }

        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            throw new BadRequestException("You don't have a cart!");
        }
        ProductCart toDelete = productCartRepository.findByCartIdAndProductId(cart.getId(), productId);
        if (toDelete == null) {
            throw new BadRequestException("The item you want to delete is invalid!");
        }

        productCartRepository.delete(toDelete);
    }

    public void placeOrder(String tokenHash) {
        Token token = getToken(tokenHash);

        Purchase purchase = createPurchase(token.getUser());
        List<PurchaseProduct> orderedProducts = getPurchaseProducts(token.getUser(), purchase);
        purchase.setProducts(orderedProducts);
        purchaseRepository.save(purchase);
        updateStocks(orderedProducts);
    }

    private List<PurchaseProduct> getPurchaseProducts(User user, Purchase purchase) {
        List<ProductCart> productCartList = getProductList(user);
        List<PurchaseProduct> orderedProducts = new ArrayList<>();
        for (ProductCart productCart : productCartList) {
            validateStock(productCart);
            PurchaseProduct pp = new PurchaseProduct();
            pp.setProduct(productCart.getProduct());
            pp.setNrItems(productCart.getNrItems());
            pp.setPurchase(purchase);
            orderedProducts.add(pp);
        }
        return orderedProducts;
    }

    private List<ProductCart> getProductList(User user) {
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            throw new BadRequestException("The cart is empty!");
        }
        return productCartRepository.findByCart(cart);
    }

    private void validateStock(ProductCart productCart) {
        if (productCart.getNrItems() > productCart.getProduct().getStock()) {
            throw new BadRequestException("The stock is limited!");
        }
    }

    private Purchase createPurchase(User user) {
        Purchase purchase = new Purchase();
        purchase.setDateCreated(LocalDate.now());
        purchase.setStatus("PROCESSED");
        purchase.setUser(user);
        return purchase;
    }

    private void updateStocks(List<PurchaseProduct> orderedProducts) {
        for (PurchaseProduct pp : orderedProducts) {
            pp.getProduct().setStock(pp.getProduct().getStock() - pp.getNrItems());
            productRepository.save(pp.getProduct());
        }
    }

    public GetUserListDTO getAllCustomers(String tokenHash) {
        validateAdminToken(tokenHash);

        List<User> userList = userRepository.findAll();
        List<GetUserDTO> customers = new ArrayList<>();

        for (User u : userList) {
            customers.add(new GetUserDTO(u.getEmail(), u.getFirstName(), u.getSecondName()));
        }

        GetUserListDTO allCustomers = new GetUserListDTO(customers);
        return allCustomers;
    }

    private Token getToken(String tokenHash) {
        validatorFactory.getTokenValidator().validate(tokenHash);
        return tokenRepository.findByHash(tokenHash);
    }


    public void validateAdminToken(String tokenHash) {
        validatorFactory.getTokenValidator().validate(tokenHash);
        validatorFactory.getAdminValidator().validate(tokenRepository.findByHash(tokenHash));
    }
}