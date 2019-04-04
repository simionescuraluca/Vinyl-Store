package com.vinyl.repository;

import com.vinyl.model.Purchase;
import com.vinyl.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends CrudRepository<Purchase, Integer> {

    List<Purchase> findByUser(User user);
}