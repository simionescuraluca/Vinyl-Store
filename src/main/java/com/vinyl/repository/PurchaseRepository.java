package com.vinyl.repository;

import com.vinyl.model.Purchase;
import com.vinyl.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends CrudRepository<Purchase, Integer> {

    Purchase findByUser(User user);
}
