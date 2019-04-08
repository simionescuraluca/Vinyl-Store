package com.vinyl.service;

import com.vinyl.model.Token;
import com.vinyl.model.User;
import com.vinyl.modelDTO.*;

public interface UserService {

    User addUser(UserDTO userDTO);

    void deleteUser(EmailPassDTO credentials);

    Token loginUser(EmailPassDTO info);

    CartDetailsDTO getCartDetails(String tokenHash);

    void deleteProductFromCart(String tokenHash, Integer productId, Integer userId);

    void placeOrder(String tokenHash);

    CustomerListDTO getAllCustomers(String tokenHash);

    OrderListDTO getCustomerOrders(String tokenHash, Integer userId);
}