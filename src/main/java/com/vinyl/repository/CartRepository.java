package com.vinyl.repository;

import com.vinyl.model.Cart;
import com.vinyl.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends CrudRepository<Cart, Integer> {

    Cart findByUser(User user);
}
