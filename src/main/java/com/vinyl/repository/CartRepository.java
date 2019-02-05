package com.vinyl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vinyl.model.Cart;

@Repository
public interface CartRepository extends CrudRepository<Cart, Integer>{

}
